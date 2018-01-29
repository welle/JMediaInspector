package aka.jmediainspector.helpers.search.componenttype;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.sun.javafx.collections.ObservableListWrapper;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.commons.ConditionFilterListCell;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadataquery.search.constants.conditions.Operator;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Combobox Criteria.
 *
 * @author charlottew
 * @param <T> enum type
 */
public abstract class AbstractComboboxCriteria<T extends Enum<?>> extends AbstractInterface<T> {

    /**
     * Available types.
     */
    protected List<@NonNull ConditionFilter> availableTypes;
    /**
     * Available values.
     */
    protected List<? extends Enum<?>> availableValues;

    private ComboBox<? extends Enum<?>> valueCombobox;
    private ComboBox<String> comboboxFiltersType;

    /**
     * Default Constructor.
     */
    public AbstractComboboxCriteria() {
        // Internal use, do not delete, used in reflection.
        init();
    }

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @see Criteria
     */
    public AbstractComboboxCriteria(@NonNull final Criteria criteria) {
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
        this.valueCombobox = getCombobox();
        this.rightPane.add(this.valueCombobox, 4, 0);
    }

    /**
     * Get the combobox to be displayed on screen.
     *
     * @return Combobox
     */
    public abstract ComboBox<? extends Enum<?>> getCombobox();

    @Override
    public Operator getSelectedOperator() {
        final String value = this.comboboxFiltersType.getSelectionModel().getSelectedItem();
        final ConditionFilter conditionFilter = ConditionFilter.getConditionFilter(value);
        Operator result = null;
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
        final Enum<?> value = this.valueCombobox.getSelectionModel().getSelectedItem();

        return value;
    }

    @Override
    public T getSelectedValue() {
        return null;
    }

    @Override
    public T getSelectedComboboxValue() {
        return null;
    }
}
