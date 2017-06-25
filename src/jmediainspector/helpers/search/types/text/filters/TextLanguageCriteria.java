package jmediainspector.helpers.search.types.text.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.constants.LanguageEnum;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.main.types.search.text.TextLanguageSearch;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
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

    private TextLanguageCriteria textLanguageCriteria;

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
        this.textLanguageCriteria = newCriteria;

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = this.textLanguageCriteria.getSelectedOperator();
        TextLanguageSearch textLanguageSearch = null;
        final Enum<?> value = this.textLanguageCriteria.getSelectedEnumValue();
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
        return this.textLanguageCriteria;
    }
}
