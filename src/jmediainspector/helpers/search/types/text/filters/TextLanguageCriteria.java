package jmediainspector.helpers.search.types.text.filters;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadataquery.main.types.constants.LanguageEnum;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxCriteria;

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

    static {
        AVAILABLE_TYPES = new ArrayList<>();
        AVAILABLE_TYPES.add(ConditionFilter.EQUALS);
        AVAILABLE_TYPES.add(ConditionFilter.NOT_EQUALS);
    }

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
        // TODO Auto-generated method stub

    }
}
