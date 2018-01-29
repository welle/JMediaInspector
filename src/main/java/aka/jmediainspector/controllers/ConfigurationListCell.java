package aka.jmediainspector.controllers;

import aka.jmediainspector.config.Configuration;
import javafx.scene.control.ListCell;

/**
 * ListCell for {@link aka.jmediainspector.config.Configuration}
 *
 * @author charlottew
 */
public final class ConfigurationListCell extends ListCell<Configuration> {

    @Override
    protected void updateItem(final Configuration item, final boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getName());
        }
    }
}
