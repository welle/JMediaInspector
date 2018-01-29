package aka.jmediainspector.helpers.search.types.text.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.text.WordUtils;
import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.comparators.EnumByNameComparator;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.LanguageEnum;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.text.TextLanguageSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Criteria for subtitles language.
 *
 * <filter selected="true">
 * <type>EQUALS</type>
 * <value>R_1080</value>
 * </filter>
 *
 * @author charlottew
 */
public class TextLanguageCriteria extends AbstractComboboxCriteria<LanguageEnum> {

    /**
     * Default Constructor.
     */
    public TextLanguageCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @see Criteria
     */
    public TextLanguageCriteria(@NonNull final Criteria criteria) {
        super(criteria);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.TEXT;
    }

    @Override
    public @NonNull String getFullName() {
        return "Language";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final TextLanguageCriteria newCriteria = new TextLanguageCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        TextLanguageSearch textLanguageSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof LanguageEnum) {
            final LanguageEnum codecEnum = (LanguageEnum) value;
            textLanguageSearch = new TextLanguageSearch(operation, codecEnum);
        }
        return textLanguageSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

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
        final LanguageEnum[] values = LanguageEnum.values();
        Arrays.sort(values, EnumByNameComparator.INSTANCE);
        result.getItems().setAll(values);
        return result;
    }
}
