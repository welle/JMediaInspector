package jmediainspector.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.plexdb.bean.MediaPartsEntity;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import jmediainspector.JMediaInspector;
import jmediainspector.config.Configurations.Configuration;
import jmediainspector.context.Context;
import jmediainspector.helpers.ConfigurationHelper;
import jmediainspector.helpers.database.PlexDBHelper;
import jmediainspector.helpers.dialogs.DialogsHelper;
import jmediainspector.helpers.dialogs.FileChooserHelper;
import jmediainspector.helpers.nodes.SearchFileHelper;
import jmediainspector.listeners.ConfigurationsListener;
import jmediainspector.services.CopyPlexDBService;

/**
 * Controler for the Plex tools tab.
 *
 * @author Welle Charlotte
 */
public class PlexToolsTabControler extends AnchorPane implements ConfigurationsListener {

    @NonNull
    private final static Logger LOGGER = Logger.getLogger(PlexToolsTabControler.class.getName());

    @Nullable
    private File copiedPlexFileDB = null;
    @FXML
    private Button searchMissingMedia;
    @FXML
    private Button loadConfigButton;
    @FXML
    private SplitPane anchorPaneRoot;
    @FXML
    private TextFlow resultArea;

    private File plexFileDB;
    @FXML
    private ComboBox<Configuration> configurationsList;

    private ConfigurationHelper configurationHelper;

    private Configuration currentConfiguration;

    /**
     * Initialize component.
     * Internal use only.
     */
    @FXML
    public void initialize() {
        this.configurationHelper = Context.getInstance().getCurrentConfigurationHelper();

        this.configurationsList.setButtonCell(new ConfigurationListCell());
        this.configurationsList.setCellFactory(p -> new ConfigurationListCell());

        refreshConfigurationList();
        setSelectedConfiguration();
    }

    private void refreshConfigurationList() {
        this.configurationsList.getItems().clear();
        final List<Configuration> configurationsItemList = this.configurationHelper.getConfigurations().getConfiguration();
        if (configurationsItemList != null && !configurationsItemList.isEmpty()) {
            this.configurationsList.getItems().addAll(configurationsItemList);
            if (this.currentConfiguration == null) {
                this.configurationsList.setValue(this.configurationHelper.getSelectedConfiguration());
            } else {
                this.configurationsList.setValue(this.currentConfiguration);
            }
        }
    }

    /**
     * Handle the select configuration button.
     *
     * @param event
     */
    @FXML
    public void handleLoadConfigButton(final ActionEvent event) {
        this.copiedPlexFileDB = null;
        File configFile = null;
        try {
            if (this.currentConfiguration != null) {
                configFile = new File(this.currentConfiguration.getFile());
            }
        } catch (final Exception e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "handleLoadConfigButton", e.getMessage(), e);
        }
        if (configFile != null) {
            final File file = configFile;
            // first copy db file as it can be used and so it is locked
            try {
                final File target = File.createTempFile("plex", ".db");
                final CopyPlexDBService service = new CopyPlexDBService(file, target);

                final Stage stage = (Stage) this.anchorPaneRoot.getScene().getWindow();
                assert stage != null;
                final Dialog<String> progressDialog = DialogsHelper.createProgressCopyDBPlexDialog(service, stage);
                final Effect parentEffect = new BoxBlur();

                JMediaInspector.getPrimaryStage().getScene().getRoot().setEffect(parentEffect);
                service.stateProperty().addListener((ChangeListener<State>) (observable, oldValue, newValue) -> {
                    if (newValue == State.CANCELLED || newValue == State.FAILED || newValue == State.SUCCEEDED) {
                        final Window window = progressDialog.getDialogPane().getScene().getWindow();
                        window.hide();
                        JMediaInspector.getPrimaryStage().getScene().getRoot().setEffect(null);
                    }
                });
                progressDialog.show();
                service.reset();
                service.setOnSucceeded(e -> {
                    final Window window = progressDialog.getDialogPane().getScene().getWindow();
                    window.hide();
                    JMediaInspector.getPrimaryStage().getScene().getRoot().setEffect(null);
                    this.copiedPlexFileDB = target;
                    final boolean isPlexDatabase = isPlexDatabase();
                    if (isPlexDatabase) {
                        this.plexFileDB = file;
                    } else {
                        this.copiedPlexFileDB = null;
                        final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "This is not a Plex database file.");

                        alert.showAndWait();
                    }
                });
                service.start();

                // everything ok, config loaded, disable load button
                this.loadConfigButton.setDisable(true);
            } catch (final IOException e) {
                LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "handleLoadConfigButton", e.getMessage(), e);
                final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "This is not a Plex database file.");

                alert.showAndWait();
                // failed! enable load button
                this.loadConfigButton.setDisable(false);
            }
        }
    }

    @FXML
    private void setSelectedConfiguration() {
        final Configuration currentSelectedConfiguration = this.configurationsList.getValue();
        if (currentSelectedConfiguration != null) {
            if (this.currentConfiguration != currentSelectedConfiguration) {
                this.currentConfiguration = currentSelectedConfiguration;
                this.loadConfigButton.setDisable(false);
            }
        }
    }

    private boolean isPlexDatabase() {
        boolean result = false;
        Connection connection = null;
        try {
            connection = getPlexDatabaseConnection();
            final Statement stmt = connection.createStatement();
            final ResultSet rs = stmt.executeQuery("Select * from media_parts;");
            rs.close();
            stmt.close();
            connection.close();

            result = true;
        } catch (final ClassNotFoundException | SQLException e) {
            result = false;
        } catch (final NoSuchFileException e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "isPlexDatabase", e.getMessage(), e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (final SQLException e) {
                LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "isPlexDatabase", e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * Handle the search missing media button.
     *
     * @param event
     */
    public void searchMissingMediaButton(final ActionEvent event) {
        final FileChooser fileChooser = FileChooserHelper.getMediaFileChooser();
        final List<@NonNull File> files = fileChooser.showOpenMultipleDialog(this.anchorPaneRoot.getScene().getWindow());
        if (files == null) {
            final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "No file selected!");

            alert.showAndWait();
        } else {
            processFileInformationSearch(files);
        }
    }

    @NonNull
    private File getCurrentCopiedPlexDBFile() throws NoSuchFileException {
        final File currentPlexFileDB = this.copiedPlexFileDB;
        if (currentPlexFileDB == null) {
            final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "Missing Plex database file!");
            alert.showAndWait();

            throw new NoSuchFileException("Missing Plex database file!");
        }

        return currentPlexFileDB;
    }

    @NonNull
    private File getCurrentPlexDBFile() throws NoSuchFileException {
        final File currentPlexFileDB = this.plexFileDB;
        if (currentPlexFileDB == null) {
            final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "Missing Plex database file!");
            alert.showAndWait();

            throw new NoSuchFileException("Missing Plex database file!");
        }

        return currentPlexFileDB;
    }

    @NonNull
    private File getCurrentPlexDBDirectory() throws NoSuchFileException {
        final File currentPlexFileDB = getCurrentPlexDBFile();
        final File parentFile = currentPlexFileDB.getParentFile().getParentFile().getParentFile();
        if (parentFile == null) {
            final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "Can not determine Plex source folder!");
            alert.showAndWait();

            throw new NoSuchFileException("Can not determine Plex source folder!");
        }

        return parentFile;
    }

    private void processFileInformationSearch(@NonNull final List<@NonNull File> files) {
        try {
            this.resultArea.getChildren().clear();
            for (final File file : files) {
                final List<@NonNull MediaPartsEntity> mediaItemsList = PlexDBHelper.getMediaParts(file);
                final LinkedList<@NonNull Node> resultList = SearchFileHelper.processResultFileInformationSearch(mediaItemsList, file);
                this.resultArea.getChildren().addAll(resultList);
            }
        } catch (final Exception e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "searchMissingMediaButton", e.getMessage(), e);
            final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "Can not open Plex database file!");
            alert.showAndWait();
        }
    }

    @NonNull
    private Connection getPlexDatabaseConnection() throws ClassNotFoundException, SQLException, NoSuchFileException {
        Class.forName("org.sqlite.JDBC");
        final StringBuilder sb = new StringBuilder();
        sb.append("jdbc:sqlite:");
        final File currentCopiedPlexFileDB = getCurrentCopiedPlexDBFile();
        final URI absoluteFileName = currentCopiedPlexFileDB.toURI();
        sb.append(absoluteFileName.toString());
        final Connection c = DriverManager.getConnection(sb.toString());
        return c;
    }

    @Override
    public void onChanges() {
        refreshConfigurationList();
    }

}
