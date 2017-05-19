package jmediainspector.helpers.search.video.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadata.main.constants.CodecVideoConstants;
import javafx.scene.control.CheckBox;
import jmediainspector.helpers.search.commons.ConditionFilter;

/**
 * Criteria for video resolution.
 *
 * @author charlottew
 */
public class VideoResolutionCriteria {

    private static List<ConditionFilter> AVAILABLE_FILTERS;
    private static List<CodecVideoConstants.RESOLUTION> AVAILABLE_VALUES;

    static {
        AVAILABLE_FILTERS = new ArrayList<>();
        AVAILABLE_FILTERS.add(ConditionFilter.EQUALS);
        AVAILABLE_FILTERS.add(ConditionFilter.GREATER_THAN);
        AVAILABLE_FILTERS.add(ConditionFilter.GREATER_THAN_OR_EQUAL_TO);
        AVAILABLE_FILTERS.add(ConditionFilter.LESS_THAN);
        AVAILABLE_FILTERS.add(ConditionFilter.LESS_THAN_OR_EQUAL_TO);
        AVAILABLE_FILTERS.add(ConditionFilter.NOT_EQUALS);

        AVAILABLE_VALUES = new ArrayList<>(Arrays.asList(CodecVideoConstants.RESOLUTION.values()));
    }

    @NonNull
    public List<CheckBox> getChoices() {
        final List<CheckBox> result = new ArrayList<>();

        return result;
    }

}
