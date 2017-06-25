package jmediainspector.helpers.search.types.text.filters;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadataquery.main.types.constants.LanguageEnum;
import jmediainspector.config.Criteria;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.ComboboxCriteria;

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
public class TextLanguageCriteria extends ComboboxCriteria<LanguageEnum> {

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
}
