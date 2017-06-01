package jmediainspector.helpers.search.commons;

import javafx.scene.control.ListCell;

/**
 * ListCell for {@link jmediainspector.config.Configuration}
 *
 * @author charlottew
 */
public final class ConditionFilterListCell extends ListCell<ConditionFilter> {

    @Override
    protected void updateItem(final ConditionFilter item, final boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.name());
        }
    }
}
