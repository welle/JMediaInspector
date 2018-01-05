package jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.UnaryOperator;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadata.main.constants.codecs.AudioMatroskaCodecIdEnum;
import aka.jmetadataquery.main.types.search.audio.AudioChannelSearch;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractInputCriteria;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Criteria for Audio Channel.
 *
 * @author charlottew
 */
public class AudioChannelCriteria extends AbstractInputCriteria<Long> {

    /**
     * Default Constructor.
     */
    public AudioChannelCriteria() {
        // Internal use, do not delete, used in reflection.
        super(Long.class);
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioChannelCriteria(@NonNull final Criteria filter) {
        super(filter, Long.class);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Number of channels";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioChannelCriteria newCriteria = new AudioChannelCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = getSelectedOperator();
        AudioChannelSearch audioChannelSearch = null;
        final Long value = getSelectedValue();
        if (operation != null && value != null) {
            audioChannelSearch = new AudioChannelSearch(operation, value);
        }
        return audioChannelSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.GREATER_THAN);
        this.availableTypes.add(ConditionFilter.GREATER_THAN_OR_EQUAL_TO);
        this.availableTypes.add(ConditionFilter.LESS_THAN);
        this.availableTypes.add(ConditionFilter.LESS_THAN_OR_EQUAL_TO);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(AudioMatroskaCodecIdEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public TextField getTextField() {
        final TextField result = new TextField();
        final UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(final TextFormatter.Change change) {
                final String text = change.getText();
                for (int i = 0; i < text.length(); i++) {
                    if (!Character.isDigit(text.charAt(i))) {
                        return null;
                    }
                }
                return change;
            }
        };
        result.setTextFormatter(new TextFormatter<String>(filter));
        return result;
    }
}
