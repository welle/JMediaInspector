package jmediainspector.controllers;

import javafx.scene.control.ListCell;
import jmediainspector.config.Configurations.Configuration;

/**
 * ListCell for {@link Configuration}
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
