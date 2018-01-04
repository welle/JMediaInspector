package jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.constants.LanguageEnum;
import aka.jmetadataquery.main.types.search.audio.AudioLanguageSearch;
import aka.jmetadataquery.main.types.search.audio.AudioNumberOfStreamSearch;
import aka.jmetadataquery.main.types.search.operation.AndSearch;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxCriteria;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Criteria for Audio Language.
 *
 * @author charlottew
 */
public class AudioLanguageCriteria extends AbstractComboboxCriteria<LanguageEnum> {

    /**
     * Default Constructor.
     */
    public AudioLanguageCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioLanguageCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Language";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioLanguageCriteria newCriteria = new AudioLanguageCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = getSelectedOperator();
        OperatorSearchInterface result = null;
        final Enum<?> value = getSelectedEnumValue();
        AudioLanguageSearch audioLanguageSearch;
        if (operation != null && value instanceof LanguageEnum) {
            final LanguageEnum languageEnum = (LanguageEnum) value;
            audioLanguageSearch = new AudioLanguageSearch(operation, languageEnum);
            if (getConditionFilter() == ConditionFilter.CONTAINS_ONLY) {
                final AndSearch andSearch = new AndSearch(false);
                andSearch.addSearch(audioLanguageSearch);
                final AudioNumberOfStreamSearch audioNumberOfStreamSearch = new AudioNumberOfStreamSearch(operation, Long.valueOf(1));
                andSearch.addSearch(audioNumberOfStreamSearch);
                result = andSearch;
            } else {
                result = audioLanguageSearch;
            }
        }
        return result;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);
        this.availableTypes.add(ConditionFilter.CONTAINS_ONLY);

        this.availableValues = new ArrayList<>(Arrays.asList(LanguageEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<LanguageEnum> result = new ComboBox<>();
        final StringConverter<LanguageEnum> converter = new StringConverter<LanguageEnum>() {
            @Override
            public String toString(final LanguageEnum object) {
                String name = object.name();
                if (name != null && name.trim().length() > 0) {
                    name = WordUtils.capitalizeFully(name);
                }
                return name;
            }

            @Override
            public LanguageEnum fromString(final String string) {
                return null;
            }
        };
        result.setConverter(converter);
        result.getItems().setAll(LanguageEnum.values());
        return result;
    }
}
