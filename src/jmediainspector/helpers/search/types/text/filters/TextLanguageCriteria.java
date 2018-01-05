package jmediainspector.helpers.search.types.text.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.constants.LanguageEnum;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.main.types.search.text.TextLanguageSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.comparators.EnumByNameComparator;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxCriteria;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

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
        final BinaryCondition.Op operation = getSelectedOperator();
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
