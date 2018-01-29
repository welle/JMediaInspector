package aka.jmediainspector.helpers.search.types.audio.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.comparators.EnumByNameComparator;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.LanguageTagSearchEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.OrSearch;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.audio.AudioLanguageTagSearch;
import aka.jmetadataquery.search.types.general.GeneralLanguageTagSearch;
import aka.jmetadataquery.search.types.video.VideoLanguageTagSearch;
import javafx.scene.control.ComboBox;

/**
 * Criteria for Audio language tag.
 *
 * @author charlottew
 */
public class AudioLanguageTagCriteria extends AbstractComboboxCriteria<LanguageTagSearchEnum> {

    /**
     * Default Constructor.
     */
    public AudioLanguageTagCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public AudioLanguageTagCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.AUDIO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Language Tag";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final AudioLanguageTagCriteria newCriteria = new AudioLanguageTagCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        final OrSearch orSearch = new OrSearch();
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof LanguageTagSearchEnum) {
            final LanguageTagSearchEnum languageTagSearchEnum = (LanguageTagSearchEnum) value;
            final AudioLanguageTagSearch audioLanguageTagSearch;
            if (ConditionFilter.CONTAINS == getConditionFilter()) {
                audioLanguageTagSearch = new AudioLanguageTagSearch(Operator.LIKE, languageTagSearchEnum);
            } else {
                audioLanguageTagSearch = new AudioLanguageTagSearch(operation, languageTagSearchEnum);
            }
            orSearch.addSearch(audioLanguageTagSearch);
            final GeneralLanguageTagSearch generalLanguageTagSearch = new GeneralLanguageTagSearch(Operator.LIKE, languageTagSearchEnum);
            orSearch.addSearch(generalLanguageTagSearch);
            final VideoLanguageTagSearch videoLanguageTagSearch = new VideoLanguageTagSearch(Operator.LIKE, languageTagSearchEnum);
            orSearch.addSearch(videoLanguageTagSearch);
        }
        return orSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);
        this.availableTypes.add(ConditionFilter.CONTAINS);

        this.availableValues = new ArrayList<>(Arrays.asList(LanguageTagSearchEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<LanguageTagSearchEnum> result = new ComboBox<>();
        final LanguageTagSearchEnum[] values = LanguageTagSearchEnum.values();
        Arrays.sort(values, EnumByNameComparator.INSTANCE);
        result.getItems().setAll(values);
        return result;
    }
}
