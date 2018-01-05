package jmediainspector.helpers.search.types.componenttype;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.sun.javafx.collections.ObservableListWrapper;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import jmediainspector.config.Criteria;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.commons.ConditionFilterListCell;
import jmediainspector.helpers.search.types.componenttype.customs.UITimeSpinner;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Combobox Criteria.
 *
 * @author charlottew
 */
public abstract class AbstractInputTimeCriteria extends AbstractInterface<Long> {

    /**
     * Available types.
     */
    protected List<@NonNull ConditionFilter> availableTypes;
    /**
     * Available values.
     */
    protected List<?> availableValues;

    private UITimeSpinner valueTimeField;
    private ComboBox<String> comboboxFiltersType;

    /**
     * Default Constructor.
     */
    public AbstractInputTimeCriteria() {
        // Internal use, do not delete, used in reflection.
        init();
    }

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @see Criteria
     */
    public AbstractInputTimeCriteria(@NonNull final Criteria criteria) {
        super(criteria);
        init();
        // TODO LINK WITH FILTER
        // Link type

        final List<String> list = new ArrayList<>();
        for (final ConditionFilter filterType : this.availableTypes) {
            list.add(filterType.getReadableName());
        }
        final ObservableList<String> observableList = new ObservableListWrapper<>(list);
        this.comboboxFiltersType = new ComboBox<>(observableList);
        this.comboboxFiltersType.valueProperty().addListener(new ConditionFilterListCell(this.criteria));

        String value = null;
        final ConditionFilter conditionFilter = ConditionFilter.getConditionFilter(this.criteria.getType());
        if (conditionFilter != null) {
            value = conditionFilter.name();
        }
        this.comboboxFiltersType.setValue(value);

        this.rightPane.add(this.comboboxFiltersType, 3, 0);

        // link value
        this.valueTimeField = getTimeSpinner();
        this.rightPane.add(this.valueTimeField, 4, 0);
    }

    public abstract UITimeSpinner getTimeSpinner();

    @Override
    public BinaryCondition.Op getSelectedOperator() {
        final String value = this.comboboxFiltersType.getSelectionModel().getSelectedItem();
        final ConditionFilter conditionFilter = ConditionFilter.getConditionFilter(value);
        BinaryCondition.Op result = null;
        if (conditionFilter != null) {
            result = conditionFilter.getOperation();
        }
        return result;
    }

    @Override
    @Nullable
    public ConditionFilter getConditionFilter() {
        final String value = this.comboboxFiltersType.getSelectionModel().getSelectedItem();
        final ConditionFilter conditionFilter = ConditionFilter.getConditionFilter(value);

        return conditionFilter;
    }

    @Override
    public Enum<?> getSelectedComboboxEnumValue() {
        return null;
    }

    @Override
    public Long getSelectedValue() {
        Long result = null;
        final LocalTime value = this.valueTimeField.getValue();
        if (value != null) {
            final int hours = value.getHour();
            final int minutes = value.getMinute();
            final int seconds = value.getSecond();

            final int hoursInMs = hours * 60 * 60 * 1000;
            final int minutesInMs = minutes * 60 * 1000;
            final int secondsInMs = seconds * 1000;

            final int total = hoursInMs + minutesInMs + secondsInMs;

            result = Long.valueOf(total);
        }

        return result;
    }

    @Override
    public Long getSelectedComboboxValue() {
        return null;
    }
}
