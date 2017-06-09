package jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.sun.javafx.collections.ObservableListWrapper;

import aka.jmetadata.main.constants.CodecVideoConstants;
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

//      Person person = new Person();
//      PathProperty prop = new PathProperty(
//                      person, "address.streetName", String.class);
//      // bind it to a JavaFX control
//      Bindings.bindBidirectional(prop, myTextField.textProperty());

        final List<String> list = new ArrayList<>();
        for (final ConditionFilter filterType : AVAILABLE_TYPES) {
            list.add(filterType.getReadableName());
        }
        final ObservableList<String> observableList = new ObservableListWrapper<>(list);
        final ComboBox<String> comboboFiltersType = new ComboBox<>(observableList);
//        comboboFiltersType.setButtonCell(new ConditionFilterListCell());
//        comboboFiltersType.setCellFactory(p -> new ConditionFilterListCell());
        comboboFiltersType.valueProperty().addListener(new ConditionFilterListCell(filter));

        String value = null;
        final ConditionFilter conditionFilter = ConditionFilter.getConditionFilter(filter.getType());
        if (conditionFilter != null) {
            value = conditionFilter.name();
        }
        comboboFiltersType.setValue(value);

        this.rightPane.add(comboboFiltersType, 1, 0);

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
