package jmediainspector.controllers;

import javafx.scene.control.ListCell;
import jmediainspector.config.Application;

/**
 * ListCell for {@link Application.Plex.Configuration}
 *
 * @author charlottew
 */
public final class ConfigurationListCell extends ListCell<Application.Plex.Configuration> {

    @Override
    protected void updateItem(final Application.Plex.Configuration item, final boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getName());
        }
    }
}
