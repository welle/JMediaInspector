package jmediainspector.helpers.search.types.componenttype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.sun.javafx.collections.ObservableListWrapper;

import aka.jmetadataquery.main.types.constants.LanguageEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import jmediainspector.config.Criteria;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.commons.ConditionFilterListCell;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.interfaces.CriteriaInterface;

/**
 * Combobox Criteria.
 *
 * @author charlottew
 * @param <T> enum type
 */
public abstract class ComboboxCriteria<T extends Enum<?>> extends CriteriaInterface {

    private static List<@NonNull ConditionFilter> AVAILABLE_TYPES;
    private static List<? extends Enum<?>> AVAILABLE_VALUES;

    static {
        AVAILABLE_TYPES = new ArrayList<>();
        AVAILABLE_TYPES.add(ConditionFilter.EQUALS);
        AVAILABLE_TYPES.add(ConditionFilter.GREATER_THAN);
        AVAILABLE_TYPES.add(ConditionFilter.GREATER_THAN_OR_EQUAL_TO);
        AVAILABLE_TYPES.add(ConditionFilter.LESS_THAN);
        AVAILABLE_TYPES.add(ConditionFilter.LESS_THAN_OR_EQUAL_TO);
        AVAILABLE_TYPES.add(ConditionFilter.NOT_EQUALS);

        AVAILABLE_VALUES = new ArrayList<>(Arrays.asList(LanguageEnum.values()));
    }

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @see Criteria
     */
    public ComboboxCriteria(@NonNull final Criteria criteria) {
        super(criteria);

        // TODO LINK WITH FILTER
        // Link type

        final List<String> list = new ArrayList<>();
        for (final ConditionFilter filterType : AVAILABLE_TYPES) {
            list.add(filterType.getReadableName());
        }
        final ObservableList<String> observableList = new ObservableListWrapper<>(list);
        final ComboBox<String> comboboFiltersType = new ComboBox<>(observableList);
        comboboFiltersType.valueProperty().addListener(new ConditionFilterListCell(this.criteria));

        String value = null;
        final ConditionFilter conditionFilter = ConditionFilter.getConditionFilter(this.criteria.getType());
        if (conditionFilter != null) {
            value = conditionFilter.name();
        }
        comboboFiltersType.setValue(value);

        this.rightPane.add(comboboFiltersType, 1, 0);

        // link value
        final ObservableList<? extends Enum<?>> observableList2 = FXCollections.observableArrayList(AVAILABLE_VALUES);
        final ComboBox<? extends Enum<?>> listViewAvailableFilters2 = new ComboBox<>(observableList2);
        this.rightPane.add(listViewAvailableFilters2, 2, 0);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.VIDEO;
    }
}
