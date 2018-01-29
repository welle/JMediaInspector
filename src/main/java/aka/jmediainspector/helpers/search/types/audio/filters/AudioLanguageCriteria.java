package aka.jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.text.WordUtils;
import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.LanguageEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.AndSearch;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.audio.AudioLanguageSearch;
import aka.jmetadataquery.search.types.audio.AudioNumberOfStreamSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

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
        final Operator operation = getSelectedOperator();
        OperatorSearchInterface result = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
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
