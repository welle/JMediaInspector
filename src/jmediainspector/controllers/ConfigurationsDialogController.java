package jmediainspector.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import jmediainspector.config.Configurations.Configuration;
import jmediainspector.constants.ApplicationConstants;
import jmediainspector.helpers.ConfigurationHelper;
import jmediainspector.helpers.FileChooserHelper;
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
    private Button addLineButton;
    @FXML
    private Button configurationSaveButton;
    @FXML
    private Button configurationCancelButton;
    @FXML
    private Button newConfigButton;
    @FXML
    private Button deleteConfigButton;
    @FXML
    private ListView<String> configurationPathListView;
    private Stage primaryStageInitial;
    private Configuration selectedConfiguration;
    @NonNull
    private final List<String> tempNewLineList = new ArrayList<>();
    private final List<String> tempDelLineList = new ArrayList<>();
    private JavaBeanStringProperty configurationNameProperty;
    private JavaBeanStringProperty plexDBFileTextFieldProperty;

    /**
     * Set the stage.
     *
     * @param primaryStageInitial
     * @param configurationHelper
     */
    public void setStage(final Stage primaryStageInitial, @NonNull final ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
        this.primaryStageInitial = primaryStageInitial;

        this.configurationsList.setButtonCell(new ConfigurationListCell());
        this.configurationsList.setCellFactory(new Callback<ListView<Configuration>, ListCell<Configuration>>() {
            @Override
            public ListCell<Configuration> call(final ListView<Configuration> p) {
                return new ConfigurationListCell();
            }
        });

        ResizeHelper.addResizeListener(primaryStageInitial);
        refreshConfigurationList();
        setSelectedConfiguration();
    }

    private void refreshConfigurationList() {
        this.configurationsList.getItems().clear();
        final List<Configuration> configurationsItemList = this.configurationHelper.getConfigurations().getConfiguration();
        if (configurationsItemList != null && !configurationsItemList.isEmpty()) {
            this.configurationsList.getItems().addAll(configurationsItemList);
            this.configurationsList.setValue(this.configurationHelper.getSelectedConfiguration());
        }
    }

    @FXML
    private void setSelectedConfiguration() {
        final Configuration currentSelectedConfiguration = this.configurationsList.getValue();
        if (currentSelectedConfiguration == null) {
            switchToEditable(false);
            this.deleteConfigButton.setDisable(true);
            this.configurationEditButton.setDisable(true);
        } else {
            switchToEditable(false);
            this.deleteConfigButton.setDisable(false);
            this.configurationEditButton.setDisable(false);
            this.currentConfigurationTitledPane.setText(currentSelectedConfiguration.getName());
            try {
                // remove previous bind
                if (this.configurationNameProperty != null) {
                    this.configurationName.textProperty().unbindBidirectional(this.configurationNameProperty);
                }
                this.configurationNameProperty = JavaBeanStringPropertyBuilder.create().bean(currentSelectedConfiguration).name("name").build();
                this.configurationName.textProperty().bindBidirectional(this.configurationNameProperty);
                // remove previous bind
                if (this.plexDBFileTextFieldProperty != null) {
                    this.plexDBFileTextField.textProperty().unbindBidirectional(this.plexDBFileTextFieldProperty);
                }
                this.plexDBFileTextFieldProperty = JavaBeanStringPropertyBuilder.create().bean(currentSelectedConfiguration).name("file").build();
                this.plexDBFileTextField.textProperty().bindBidirectional(this.plexDBFileTextFieldProperty);
            } catch (final NoSuchMethodException e) {
                LOGGER.logp(Level.SEVERE, "ConfigurationsDialogController", "setSelectedConfiguration", e.getMessage(), e);
            }

            this.selectedConfiguration = currentSelectedConfiguration;
            this.tempNewLineList.clear();
            refreshConfigurationPaths();
        }
    }

    @FXML
    private void addLine() {
        final DirectoryChooser directoryChooser = FileChooserHelper.getDirectoryChooser();
        final File file = directoryChooser.showDialog(this.primaryStageInitial.getScene().getWindow());
        if (file != null) {
            this.tempNewLineList.add(file.getAbsolutePath());

            refreshConfigurationPaths();
        }
    }

    private void refreshConfigurationPaths() {
        final List<String> list = new ArrayList<>(this.selectedConfiguration.getPaths().getPath());
        list.addAll(this.tempNewLineList);
        list.removeAll(this.tempDelLineList);
        final ObservableList<String> observableList = FXCollections.observableArrayList(list);
        this.configurationPathListView.setItems(observableList);
        this.configurationPathListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(final ListView<String> param) {
                return new ConfigurationPathListCell(ConfigurationsDialogController.this.configurationPathListView);
            }
        });
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

    private class ConfigurationPathListCell extends ListCell<String> {
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
            this.deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent event) {
                    // delete current item
                    ConfigurationPathListCell.this.listView.getItems().remove(item);
                }
            });
        }
    }

    @FXML
    private void handleEditButton() {
        switchToEditable(true);
    }

    @FXML
    private void saveConfiguration() {
        // Check if name is not empty
        final Configuration currentConfig = this.configurationsList.getValue();
        if (currentConfig.getName() == null || "".equals(currentConfig.getName().trim())) {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Configuration name can not be empty!");

            alert.showAndWait();
            return;
        }

        this.selectedConfiguration.getPaths().getPath().clear();
        // fill with items in configuration items list
        this.selectedConfiguration.getPaths().getPath().addAll(this.configurationPathListView.getItems());

        this.configurationHelper.saveConfig();
        refreshConfigurationList();

        switchToEditable(false);
    }

    @FXML
    private void cancelEditConfiguration() {
        this.tempNewLineList.clear();
        this.configurationPathListView.getItems().addAll(this.tempDelLineList);
        this.tempDelLineList.clear();
        refreshConfigurationPaths();
        switchToEditable(false);
    }

    private void switchToEditable(final boolean isEditable) {
        this.configurationEditButton.setVisible(!isEditable);
        this.configurationCancelButton.setVisible(isEditable);
        this.addLineButton.setDisable(!isEditable);
        this.configurationSaveButton.setVisible(isEditable);
        this.configurationName.setDisable(!isEditable);
        this.configurationPathListView.setDisable(!isEditable);
        this.plexDBFileTextField.setDisable(!isEditable);
        this.configurationsList.setDisable(isEditable);
        this.newConfigButton.setDisable(isEditable);
        this.deleteConfigButton.setDisable(isEditable);
    }

    @FXML
    private void deleteConfiguration() {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("Deleting is definitive! Continue ?");
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getClassLoader().getResource(ApplicationConstants.CSS_FILE).toExternalForm());
        alert.initOwner(this.primaryStageInitial);
        final Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(button -> {
            if (button == ButtonType.OK) {
                this.configurationHelper.deleteCurrentConfiguration();
                this.configurationsList.getItems().remove(this.configurationsList.getSelectionModel());
                // refresh configuration list
                refreshConfigurationList();
                setSelectedConfiguration();
            }
        });
    }

}
