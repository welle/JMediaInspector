package jmediainspector.controllers;

import org.eclipse.jdt.annotation.NonNull;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * ListCell for Path of Configuration.
 *
 * @author charlottew
 */
public final class ConfigurationPathListCell extends ListCell<String> {

    @NonNull
    private final HBox hbox = new HBox();
    @NonNull
    private final Label label = new Label("(empty)");
    @NonNull
    private final Pane pane = new Pane();
    @NonNull
    private final Button deleteButton = new Button("Delete");
    @NonNull
    private final ListView<String> listView;

    /**
     * Constructor.
     *
     * @param listView Listview to manage
     */
    public ConfigurationPathListCell(@NonNull final ListView<String> listView) {
        super();
        this.listView = listView;
        this.hbox.getChildren().addAll(this.label, this.pane, this.deleteButton);
        HBox.setHgrow(this.pane, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(final String item, final boolean empty) {
        super.updateItem(item, empty);
        setText(null); // No text in label of super class
        if (empty) {
            setGraphic(null);
        } else {
            String toDisplay = item;
            if (toDisplay == null) {
                toDisplay = "<null>";
            } else {
                if (toDisplay.length() > 95) {
                    toDisplay = toDisplay.substring(0, 95) + "...";
                }
            }
            this.label.setText(toDisplay);
            setGraphic(this.hbox);
        }
        this.deleteButton.setOnAction(event -> ConfigurationPathListCell.this.listView.getItems().remove(item));
    }
}
