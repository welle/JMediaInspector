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
import javafx.beans.property.adapter.JavaBeanBooleanProperty;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jmediainspector.config.Configuration;
import jmediainspector.config.Configurations;
import jmediainspector.config.helpers.PlexConfigurationHelper;
import jmediainspector.context.ApplicationContext;
import jmediainspector.helpers.dialogs.DialogsHelper;
import jmediainspector.helpers.dialogs.FileChooserHelper;
import jmediainspector.helpers.effects.ResizeHelper;

/**
 * Controller for the Configurations dialog.
 *
 * @author Welle Charlotte
 */
public class ConfigurationsDialogController extends AnchorPane {

    @NonNull
    private final static Logger LOGGER = Logger.getLogger(ConfigurationsDialogController.class.getName());

    private PlexConfigurationHelper configurationHelper;
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
    private Button selectPlexDBButton;
    @FXML
    private CheckBox defaultConfiguration;
    @FXML
    private ListView<String> configurationPathListView;
    private Stage primaryStageInitial;
    private Configuration currentConfiguration;
    @NonNull
    private final List<String> tempNewLineList = new ArrayList<>();
    private final List<String> tempDelLineList = new ArrayList<>();
    private JavaBeanStringProperty configurationNameProperty;
    private JavaBeanStringProperty plexDBFileTextFieldProperty;
    private JavaBeanBooleanProperty defaultConfigurationProperty;

    /**
     * Initialize.
     *
     * Internal use only.
     */
    @FXML
    public void initialize() {
        this.configurationHelper = ApplicationContext.getInstance().getCurrentPlexConfigurationHelper();
    }

    /**
     * Set the stage.
     *
     * @param primaryStageInitial
     */
    public void setStage(final Stage primaryStageInitial) {
        this.primaryStageInitial = primaryStageInitial;

        this.configurationsList.setButtonCell(new ConfigurationListCell());
        this.configurationsList.setCellFactory(p -> new ConfigurationListCell());

        ResizeHelper.addResizeListener(primaryStageInitial);
        refreshConfigurationList(null);
        setSelectedConfiguration();
    }

    private void refreshConfigurationList(final Configuration configuration) {
        this.configurationsList.getItems().clear();
        final Configurations configurations = this.configurationHelper.getConfigurations();
        if (configurations != null) {
            final List<Configuration> configurationsItemList = configurations.getConfiguration();
            if (configurationsItemList != null && !configurationsItemList.isEmpty()) {
                this.configurationsList.getItems().addAll(configurationsItemList);
                if (configuration == null) {
                    this.configurationsList.setValue(this.configurationHelper.getSelectedConfiguration());
                } else {
                    this.configurationsList.setValue(configuration);
                }
            }
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

                if (this.defaultConfigurationProperty != null) {
                    this.defaultConfiguration.selectedProperty().unbindBidirectional(this.defaultConfigurationProperty);
                }
                this.defaultConfigurationProperty = JavaBeanBooleanPropertyBuilder.create().bean(currentSelectedConfiguration).name("selected").build();
                this.defaultConfiguration.selectedProperty().bindBidirectional(this.defaultConfigurationProperty);
            } catch (final NoSuchMethodException e) {
                LOGGER.logp(Level.SEVERE, "ConfigurationsDialogController", "setSelectedConfiguration", e.getMessage(), e);
            }

            this.currentConfiguration = currentSelectedConfiguration;
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
        final List<String> list = new ArrayList<>(this.currentConfiguration.getPaths().getPath());
        list.addAll(this.tempNewLineList);
        list.removeAll(this.tempDelLineList);
        final ObservableList<String> observableList = FXCollections.observableArrayList(list);
        this.configurationPathListView.setItems(observableList);
        this.configurationPathListView.setCellFactory(param -> {
            final ListView<String> configurationPathListView2 = ConfigurationsDialogController.this.configurationPathListView;
            assert configurationPathListView2 != null;
            return new ConfigurationPathListCell(configurationPathListView2);
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

    @FXML
    private void handleEditButton() {
        switchToEditable(true);
    }

    @FXML
    private void saveConfiguration() {
        // Check if name is not empty
        final Configuration currentConfig = this.configurationsList.getValue();
        if (currentConfig.getName() == null || currentConfig.getName().trim().length() > 0) {
            final Stage currentPrimaryStageInitial = this.primaryStageInitial;
            assert currentPrimaryStageInitial != null;
            final Alert alert = DialogsHelper.getAlert(currentPrimaryStageInitial, Alert.AlertType.ERROR, "Configuration name can not be empty!");
            alert.showAndWait();
            return;
        }

        this.currentConfiguration.getPaths().getPath().clear();
        // fill with items in configuration items list
        this.currentConfiguration.getPaths().getPath().addAll(this.configurationPathListView.getItems());

        this.configurationHelper.saveConfig();
        refreshConfigurationList(this.currentConfiguration);

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
        this.defaultConfiguration.setDisable(!isEditable);
        this.selectPlexDBButton.setDisable(!isEditable);
    }

    @FXML
    private void deleteConfiguration() {
        final Stage currentPrimaryStageInitial = this.primaryStageInitial;
        assert currentPrimaryStageInitial != null;
        final Alert alert = DialogsHelper.getAlert(currentPrimaryStageInitial, Alert.AlertType.CONFIRMATION, "Deleting is definitive! Continue ?");
        final Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(button -> {
            if (button == ButtonType.OK) {
                final Configuration selectedItem = this.currentConfiguration;
                if (selectedItem != null) {
                    this.configurationHelper.deleteCurrentConfiguration(selectedItem);
                    this.configurationsList.getItems().remove(this.configurationsList.getSelectionModel());
                    // refresh configuration list
                    refreshConfigurationList(null);
                    setSelectedConfiguration();
                }
            }
        });
    }

    @FXML
    private void addConfigButton() {
        final Configuration newConfiguration = this.configurationHelper.addNewConfiguration();
        refreshConfigurationList(newConfiguration);
        switchToEditable(true);
        this.configurationName.requestFocus();
    }

    @FXML
    private void changeSelectedConfiguration() {
        if (this.defaultConfiguration.isSelected()) {
            final Configuration cConfiguration = this.currentConfiguration;
            if (cConfiguration != null) {
                this.configurationHelper.setSelectedConfiguration(cConfiguration);
            }
        }
    }

    @FXML
    private void handleSelectPlexDBButton() {
        final FileChooser fileChooser = FileChooserHelper.getPlexDBFileChooser();
        File file = null;
        try {
            final String initialDirString = this.plexDBFileTextField.getText();
            if (initialDirString == null || initialDirString.trim().length() == 0) {
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            } else {
                final File initialDir = new File(initialDirString);
                if (initialDir.getParentFile() != null) {
                    fileChooser.setInitialDirectory(initialDir.getParentFile());
                }
            }

            file = fileChooser.showOpenDialog(this.primaryStageInitial.getScene().getWindow());
        } catch (final IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            file = fileChooser.showOpenDialog(this.primaryStageInitial.getScene().getWindow());
        }

        if (file != null) {
            this.plexDBFileTextField.setText(file.getAbsolutePath());
        }
    }
}
