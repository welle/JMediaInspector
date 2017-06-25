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
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Combobox Criteria.
 *
 * @author charlottew
 * @param <T> enum type
 */
public abstract class AbstractComboboxCriteria<T extends Enum<?>> extends AbstractInterface {

    /**
     * Available types.
     */
    protected static List<@NonNull ConditionFilter> AVAILABLE_TYPES;
    private static List<? extends Enum<?>> AVAILABLE_VALUES;

    static {
        AVAILABLE_VALUES = new ArrayList<>(Arrays.asList(LanguageEnum.values()));
    }

    /**
     * Default Constructor.
     */
    public AbstractComboboxCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @see Criteria
     */
    public AbstractComboboxCriteria(@NonNull final Criteria criteria) {
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
