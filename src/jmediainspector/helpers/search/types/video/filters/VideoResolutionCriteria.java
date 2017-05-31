package jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmetadata.main.constants.CodecVideoConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import jmediainspector.config.Filter;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.interfaces.FiltersInterface;

/**
 * Criteria for video resolution.
 *
 * @author charlottew
 */
public class VideoResolutionCriteria extends FiltersInterface {

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

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Filter
     */
    public VideoResolutionCriteria(@NonNull final Filter filter) {
        super(filter);

        final ObservableList<ConditionFilter> observableList = FXCollections.observableArrayList(AVAILABLE_FILTERS);
        final ComboBox<ConditionFilter> listViewAvailableFilters = new ComboBox<>(observableList);
        this.rightPane.add(listViewAvailableFilters, 1, 0);
        final ObservableList<CodecVideoConstants.RESOLUTION> observableList2 = FXCollections.observableArrayList(AVAILABLE_VALUES);
        final ComboBox<CodecVideoConstants.RESOLUTION> listViewAvailableFilters2 = new ComboBox<>(observableList2);
        this.rightPane.add(listViewAvailableFilters2, 2, 0);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.VIDEO;
    }

}
