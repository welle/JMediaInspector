package jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.sun.javafx.collections.ObservableListWrapper;

import aka.jmetadata.main.constants.CodecVideoConstants;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import jmediainspector.config.Filter;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.commons.ConditionFilterListCell;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.interfaces.FiltersInterface;

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
public class VideoResolutionCriteria extends FiltersInterface {

    private static List<@NonNull ConditionFilter> AVAILABLE_TYPES;
    private static List<CodecVideoConstants.RESOLUTION> AVAILABLE_VALUES;

    static {
        AVAILABLE_TYPES = new ArrayList<>();
        AVAILABLE_TYPES.add(ConditionFilter.EQUALS);
        AVAILABLE_TYPES.add(ConditionFilter.GREATER_THAN);
        AVAILABLE_TYPES.add(ConditionFilter.GREATER_THAN_OR_EQUAL_TO);
        AVAILABLE_TYPES.add(ConditionFilter.LESS_THAN);
        AVAILABLE_TYPES.add(ConditionFilter.LESS_THAN_OR_EQUAL_TO);
        AVAILABLE_TYPES.add(ConditionFilter.NOT_EQUALS);

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

        // TODO LINK WITH FILTER
        // Link type
        final List<ConditionFilter> list = new ArrayList<>();
        for (final ConditionFilter filterType : AVAILABLE_TYPES) {
            list.add(filterType);
        }
        final ObservableList<ConditionFilter> observableList = new ObservableListWrapper<>(list);
        final ComboBox<ConditionFilter> listViewFiltersType = new ComboBox<>(observableList);
        listViewFiltersType.setButtonCell(new ConditionFilterListCell());
        listViewFiltersType.setCellFactory(p -> new ConditionFilterListCell());
        JavaBeanObjectProperty value;
        try {
            value = JavaBeanObjectPropertyBuilder.create().bean(filter).name("type").build();
            listViewFiltersType.valueProperty().bindBidirectional(value);
        } catch (final NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.rightPane.add(listViewFiltersType, 1, 0);

        // link value
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
