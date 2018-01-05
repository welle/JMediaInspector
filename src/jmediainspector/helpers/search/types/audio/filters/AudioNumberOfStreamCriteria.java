package jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.search.audio.AudioNumberOfStreamSearch;
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
 * Criteria for Audio Number of Stream.
 *
 * @author charlottew
 */
public class AudioNumberOfStreamCriteria extends AbstractInputCriteria<Long> {

    /**
     * Default Constructor.
     */
    public AudioNumberOfStreamCriteria() {
        // Internal use, do not delete, used in reflection.
        super(Long.class);
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioNumberOfStreamCriteria(@NonNull final Criteria filter) {
        super(filter, Long.class);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Number of streams";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioNumberOfStreamCriteria newCriteria = new AudioNumberOfStreamCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = getSelectedOperator();
        AudioNumberOfStreamSearch audioNumberOfStreamSearch = null;
        final Long value = getSelectedValue();
        if (operation != null && value != null) {
            audioNumberOfStreamSearch = new AudioNumberOfStreamSearch(operation, value);
        }
        return audioNumberOfStreamSearch;
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
