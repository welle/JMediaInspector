package jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.search.constants.video.VideoResolutionSearchEnum;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.main.types.search.video.VideoResolutionSearch;
import jmediainspector.config.Criteria;
import jmediainspector.controllers.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.componenttype.AbstractComboboxCriteria;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

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
public class VideoResolutionCriteria extends AbstractComboboxCriteria<VideoResolutionSearchEnum> {

    private VideoResolutionCriteria videoResolutionCriteria;

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
        final VideoResolutionCriteria newCriteria = new VideoResolutionCriteria(filter);
        this.videoResolutionCriteria = newCriteria;

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final BinaryCondition.Op operation = this.videoResolutionCriteria.getSelectedOperator();
        VideoResolutionSearch videoResolutionSearch = null;
        final Enum<?> value = this.videoResolutionCriteria.getSelectedEnumValue();
        if (operation != null && value instanceof VideoResolutionSearchEnum) {
            final @NonNull VideoResolutionSearchEnum codecEnum = (VideoResolutionSearchEnum) value;
            videoResolutionSearch = new VideoResolutionSearch(operation, codecEnum);
        }
        return videoResolutionSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.GREATER_THAN);
        this.availableTypes.add(ConditionFilter.GREATER_THAN_OR_EQUAL_TO);
        this.availableTypes.add(ConditionFilter.LESS_THAN);
        this.availableTypes.add(ConditionFilter.LESS_THAN_OR_EQUAL_TO);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(VideoResolutionSearchEnum.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this.videoResolutionCriteria;
    }

}
