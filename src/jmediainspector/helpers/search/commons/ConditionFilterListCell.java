package jmediainspector.helpers.search.commons;

import org.eclipse.jdt.annotation.NonNull;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import jmediainspector.config.Criteria;

/**
 * ListCell for {@link jmediainspector.config.Configuration}
 *
 * @author charlottew
 */
public final class ConditionFilterListCell implements ChangeListener<String> {

    @NonNull
    private final Criteria criteria;

    /**
     * Constructor.
     *
     * @param criteria
     */
    public ConditionFilterListCell(@NonNull final Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
        this.criteria.setType(newValue);
    }

}
