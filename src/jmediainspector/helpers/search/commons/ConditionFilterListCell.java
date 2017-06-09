package jmediainspector.helpers.search.commons;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import jmediainspector.config.Filter;

/**
 * ListCell for {@link jmediainspector.config.Configuration}
 *
 * @author charlottew
 */
public final class ConditionFilterListCell implements ChangeListener<String> {

    private final Filter filter;

    public ConditionFilterListCell(final Filter filter) {
        this.filter = filter;
    }

    @Override
    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
        this.filter.setType(newValue);
        System.out.println(ConditionFilter.getConditionFilter(this.filter.getType()));
    }

}
