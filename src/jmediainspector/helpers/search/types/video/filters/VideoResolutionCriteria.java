package jmediainspector.helpers.search.types.video.filters;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadata.main.constants.video.Resolution;
import jmediainspector.config.Criteria;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.ComboboxCriteria;

/**
 * Criteria for video resolution.
 *
 * <filter selected="true">
 * <type>EQUALS</type>
 * <value>R_1080</value>
 * </filter>
 *
 * @author charlottew
 */
public class VideoResolutionCriteria extends ComboboxCriteria<Resolution> {

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @see Criteria
     */
    public VideoResolutionCriteria(@NonNull final Criteria criteria) {
        super(criteria);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.VIDEO;
    }
}
