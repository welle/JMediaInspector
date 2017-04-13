package jmediainspector.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import jmediainspector.config.Configurations.Configuration;
import jmediainspector.config.Configurations.Configuration.Paths;
import jmediainspector.helpers.ConfigurationHelper;
import jmediainspector.helpers.ResizeHelper;

/**
 * Controller for the Configurations dialog.
 *
 * @author Welle Charlotte
 */
public class ConfigurationsDialogController extends AnchorPane {

    private @NonNull final static Logger LOGGER = Logger.getLogger(ConfigurationsDialogController.class.getName());
    private ConfigurationHelper configurationHelper;

    @NonNull
    private final BooleanProperty finished = new SimpleBooleanProperty();
    @FXML
    private Button closeButton;
    @FXML
    private ComboBox<Configuration> configurationsList;
    @FXML
    private TitledPane currentConfigurationTitledPane;
    @FXML
    private TextField configurationName;
    @FXML
    private TextField plexDBFileTextField;
    @FXML
    private Button configurationEditButton;
    @FXML
    private Button configurationSaveButton;
    @FXML
    private Button configurationCancelButton;
    @FXML
    private ListView<Paths> configurationPathListView;

    /**
     * Set the stage.
     *
     * @param primaryStageInitial
     * @param configurationHelper
     */
    public void setStage(final Stage primaryStageInitial, @NonNull final ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;

        this.configurationsList.setButtonCell(new ConfigurationListCell());
        this.configurationsList.setCellFactory(new Callback<ListView<Configuration>, ListCell<Configuration>>() {
            @Override
            public ListCell<Configuration> call(final ListView<Configuration> p) {
                return new ConfigurationListCell();
            }
        });
        this.configurationsList.getItems().addAll(this.configurationHelper.getConfigurations().getConfiguration());
        this.configurationsList.setValue(this.configurationHelper.getSelectedConfiguration());

        ResizeHelper.addResizeListener(primaryStageInitial);

        setSelectedConfiguration();
    }

    @FXML
    private void setSelectedConfiguration() {
        final Configuration currentSelectedConfiguration = this.configurationsList.getValue();
        if (currentSelectedConfiguration == null) {
            this.configurationEditButton.setDisable(true);
        } else {
            this.configurationEditButton.setDisable(false);
            this.currentConfigurationTitledPane.setText(currentSelectedConfiguration.getName());
            try {
                this.configurationName.textProperty().bindBidirectional(JavaBeanStringPropertyBuilder.create().bean(currentSelectedConfiguration).name("name").build());
                this.plexDBFileTextField.textProperty().bindBidirectional(JavaBeanStringPropertyBuilder.create().bean(currentSelectedConfiguration).name("file").build());
            } catch (final NoSuchMethodException e) {
                LOGGER.logp(Level.SEVERE, "ConfigurationsDialogController", "setSelectedConfiguration", e.getMessage(), e);
            }

            final ObservableList<Paths> list = FXCollections.observableArrayList(this.configurationHelper.getNewPaths());
            this.configurationPathListView.setItems(list);
            this.configurationPathListView.setCellFactory(new Callback<ListView<Paths>, ListCell<Paths>>() {
                @Override
                public ListCell<Paths> call(final ListView<Paths> param) {
                    return new ConfigurationPathListCell();
                }
            });

//            this.configurationPathListView.setCellFactory(new Callback<ListView<Paths>, ListCell<Paths>>() {
//                @Override
//                public ListCell<Paths> call(final ListView<Paths> p) {
//                    return new ConfigurationPathListCell();
//                }
//            });
//            final SimpleListProperty<Paths> simpleListProperty = new SimpleListProperty<>(currentSelectedConfiguration.getPaths(), "PathListView");
//            this.configurationPathListView.itemsProperty().bind(simpleListProperty);

            this.configurationPathListView.getItems().add(this.configurationHelper.getNewPaths());
        }
    }

    @FXML
    private void handleClose() {
        this.finished.set(true);
    }

    /**
     * Get finished status.
     *
     * @return finish status
     */
    public BooleanProperty finished() {
        return this.finished;
    }

    private class ConfigurationListCell extends ListCell<Configuration> {
        @Override
        protected void updateItem(final Configuration item, final boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getName());
            }
        }
    }

    private class ConfigurationPathListCell extends ListCell<Paths> {
        private final HBox hbox = new HBox();
        private final Label label = new Label("(empty)");
        private final Pane pane = new Pane();
        private final Button button = new Button("Delete");
        private Paths lastItem;

        public ConfigurationPathListCell() {
            super();
            this.hbox.getChildren().addAll(this.label, this.pane, this.button);
            HBox.setHgrow(this.pane, Priority.ALWAYS);
            this.button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent event) {
                    System.out.println(ConfigurationPathListCell.this.lastItem + " : " + event);
                }
            });
        }

        @Override
        protected void updateItem(final Paths item, final boolean empty) {
            super.updateItem(item, empty);
//            setText(null); // No text in label of super class
            if (empty) {
                this.lastItem = null;
                setGraphic(null);
            } else {
                this.lastItem = item;
                System.err.println(item.getPath());
                this.label.setText((item.getPath() == null) ? "<null>" : item.getPath());
                setGraphic(this.hbox);
            }
        }
    }

    @FXML
    private void handleEditButton() {
        switchToEditable(true);
    }

    @FXML
    private void saveConfiguration() {
        // Check if name does not already exist
        final Configuration currentConfig = this.configurationsList.getValue();
        if (currentConfig.getName() == null || "".equals(currentConfig.getName().trim())) {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Configuration name can not be empty!");

            alert.showAndWait();
            return;
        }
        for (final Configuration configuration : this.configurationHelper.getConfigurations().getConfiguration()) {
            if (configuration != currentConfig) {
                if (currentConfig.getName().toLowerCase().equals(configuration.getName().toLowerCase())) {
                    final Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Configuration name already exist!");

                    alert.showAndWait();
                    return;
                }
            }
        }

        this.configurationHelper.saveConfig();
        setSelectedConfiguration();

        switchToEditable(false);
    }

    @FXML
    private void cancelEditConfiguration() {
        switchToEditable(false);
    }

    private void switchToEditable(final boolean isEditable) {
        this.configurationEditButton.setVisible(!isEditable);
        this.configurationCancelButton.setVisible(isEditable);
        this.configurationSaveButton.setVisible(isEditable);
        this.configurationName.setDisable(!isEditable);
        this.configurationPathListView.setDisable(!isEditable);
        this.plexDBFileTextField.setDisable(!isEditable);
    }

}
