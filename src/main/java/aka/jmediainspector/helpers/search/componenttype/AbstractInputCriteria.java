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
import javafx.scene.control.TextField;

/**
 * Combobox Criteria.
 *
 * @author charlottew
 * @param <T> enum type
 */
public abstract class AbstractInputCriteria<T> extends AbstractInterface<T> {

    /**
     * Available types.
     */
    protected List<@NonNull ConditionFilter> availableTypes;
    /**
     * Available values.
     */
    protected List<?> availableValues;

    private TextField valueTextField;
    private ComboBox<String> comboboxFiltersType;
    private final @NonNull Class<T> clazz;

    /**
     * Default Constructor.
     */
    public AbstractInputCriteria(@NonNull final Class<T> clazz) {
        this.clazz = clazz;
        // Internal use, do not delete, used in reflection.
        init();
    }

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @param clazz
     * @see Criteria
     */
    public AbstractInputCriteria(@NonNull final Criteria criteria, @NonNull final Class<T> clazz) {
        super(criteria);
        init();
        // TODO LINK WITH FILTER
        // Link type

        this.clazz = clazz;

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
        this.valueTextField = getTextField();
        this.rightPane.add(this.valueTextField, 4, 0);
    }

    public abstract TextField getTextField();

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
        return null;
    }

    @Override
    public T getSelectedValue() {
        T result = null;
        final String value = this.valueTextField.getText();
        if (this.clazz.isInstance(String.class)) {
            result = (T) value;
        } else if (this.clazz.equals(Long.class)) {
            result = (T) Long.valueOf(value);
        }

        return result;
    }

    @Override
    public T getSelectedComboboxValue() {
        return null;
    }
}
