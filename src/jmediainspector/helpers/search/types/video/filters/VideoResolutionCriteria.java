package jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadata.main.constants.video.Resolution;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxCriteria;

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
public class VideoResolutionCriteria extends AbstractComboboxCriteria<Resolution> {

    static {
        AVAILABLE_TYPES = new ArrayList<>();
        AVAILABLE_TYPES.add(ConditionFilter.EQUALS);
        AVAILABLE_TYPES.add(ConditionFilter.GREATER_THAN);
        AVAILABLE_TYPES.add(ConditionFilter.GREATER_THAN_OR_EQUAL_TO);
        AVAILABLE_TYPES.add(ConditionFilter.LESS_THAN);
        AVAILABLE_TYPES.add(ConditionFilter.LESS_THAN_OR_EQUAL_TO);
        AVAILABLE_TYPES.add(ConditionFilter.NOT_EQUALS);
    }

    /**
     * Default Constructor.
     */
    public VideoResolutionCriteria() {
        // Internal use, do not delete, used in reflection.
    }

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

    @Override
    public @NonNull String getFullName() {
        return "Resolution";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        filter.setType(ConditionFilter.GREATER_THAN.name());
        final VideoResolutionCriteria videoResolutionCriteria = new VideoResolutionCriteria(filter);

        searchHelper.addCriteria(videoResolutionCriteria);
    }

}
