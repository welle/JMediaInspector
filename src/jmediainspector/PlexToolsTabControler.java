package jmediainspector;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jmediainspector.constants.DevConstants;
import jmediainspector.constants.PlexConstants;
import jmediainspector.helpers.FileChooserHelper;

/**
 * Controler for the Plex tools tab.
 *
 * @author Welle Charlotte
 */
public class PlexToolsTabControler extends AnchorPane {

    private @NonNull final static Logger LOGGER = Logger.getLogger(PlexToolsTabControler.class.getName());

    @Nullable
    private File copiedPlexFileDB = null;
    @FXML
    private TextField selectedPlexDBText;
    @FXML
    private Button selectPlexDBBUtton;
    @FXML
    private Button searchMissingMedia;
    @FXML
    private SplitPane anchorPaneRoot;
    @FXML
    private TextFlow resultArea;

    private File plexFileDB;

    /**
     * Handle the select plex database button.
     *
     * @param event
     */
    public void clickPlexDBButton(final ActionEvent event) {
        this.copiedPlexFileDB = null;
        this.plexFileDB = null;
        final FileChooser fileChooser = FileChooserHelper.getPlexDBFileChooser();
        final File file = fileChooser.showOpenDialog(this.anchorPaneRoot.getScene().getWindow());
        if (file == null) {
            this.selectedPlexDBText.setText(null);
        } else {
            // first copy db file as it can be used and so it is locked
            try {
                final File target = File.createTempFile("plex", ".db");
                final Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws InterruptedException {
                                updateMessage("Processing Plex database file");
                                final long fileLength = file.length();

                                updateProgress(0, (int) fileLength);
                                try {
                                    final FileInputStream fis = new FileInputStream(file);
                                    final FileOutputStream fos = new FileOutputStream(target);

                                    final byte[] buf = new byte[2048];
                                    int size = 0;
                                    int flag = 0;
                                    while ((size = fis.read(buf)) != -1) {
                                        fos.write(buf, 0, size);
                                        flag += size;
                                        updateProgress(flag, (int) fileLength);
                                    }

                                    fis.close();
                                    fos.close();

                                } catch (final IOException e) {
                                    LOGGER.logp(Level.SEVERE, "PlexToolsTabControler.clickPlexDBButton(...).new Service() {...}.createTask().new Task() {...}", "call", e.getMessage(), e);
                                }

                                updateMessage("Found all.");
                                return null;
                            }
                        };
                    }
                };

                final Stage progressDialog = createProgressDialog(service, (Stage) this.anchorPaneRoot.getScene().getWindow());

                progressDialog.show();
                service.reset();
                service.setOnSucceeded(e -> {
                    if (DevConstants.DEBUG) {
                        System.err.println("[PlexToolsTabControler] clickPlexDBButton - copied " + file + " to " + target);
                    }
                    this.copiedPlexFileDB = target;
                    final boolean isPlexDatabase = isPlexDatabase();
                    if (isPlexDatabase) {
                        final String absoluteFileName = file.getAbsolutePath();
                        this.selectedPlexDBText.setText(absoluteFileName);
                        this.plexFileDB = file;
                    } else {
                        this.copiedPlexFileDB = null;
                        final Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("This is not a Plex database file.");

                        alert.showAndWait();
                    }
                });
                service.setOnCancelled(e -> {
                    this.plexFileDB = null;
                    this.copiedPlexFileDB = null;
                    this.selectedPlexDBText.setText(null);
                });
                service.start();

            } catch (final IOException e) {
                LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "clickPlexDBButton", e.getMessage(), e);
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("This is not a Plex database file.");

                alert.showAndWait();
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
        final List<File> files = fileChooser.showOpenMultipleDialog(this.anchorPaneRoot.getScene().getWindow());
        if (files == null) {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No file selected!");

            alert.showAndWait();
        } else {
            processFileInformationSearch(files);
        }
    }

    @NonNull
    private File getCurrentCopiedPlexDBFile() throws NoSuchFileException {
        final File currentPlexFileDB = this.copiedPlexFileDB;
        if (currentPlexFileDB == null) {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Missing Plex database file!");

            alert.showAndWait();

            throw new NoSuchFileException("Missing Plex database file!");
        }

        return currentPlexFileDB;
    }

    @NonNull
    private File getCurrentPlexDBFile() throws NoSuchFileException {
        final File currentPlexFileDB = this.plexFileDB;
        if (currentPlexFileDB == null) {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Missing Plex database file!");

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
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Can not determine Plex source folder!");

            alert.showAndWait();

            throw new NoSuchFileException("Can not determine Plex source folder!");
        }

        return parentFile;
    }

    private void processFileInformationSearch(@NonNull final List<File> files) {
        Connection connection = null;
        try {
            connection = getPlexDatabaseConnection();
            this.resultArea.getChildren().clear();
            for (final File file : files) {
                final Statement stmt = connection.createStatement();
                final String query = "Select T1.file, T4.section_type, T3.* from media_parts as T1 inner join media_items as T2 on (T1.media_item_id == T2.id) inner join metadata_items as T3 on (T2.metadata_item_id == T3.id) inner join library_sections as T4 on (T3.library_section_id = T4.id) where lower(T1.file) =\"" + file.getAbsolutePath().toLowerCase() + "\"";
                if (DevConstants.DEBUG) {
                    System.err.println("[PlexToolsTabControler] processFileInformationSearch - QUERY= " + query);
                }
                final ResultSet rs = stmt.executeQuery(query);
                processResultFileInformationSearch(rs, file);
                rs.close();
                stmt.close();
            }
        } catch (final Exception e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "searchMissingMediaButton", e.getMessage(), e);
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Can not open Plex database file!");

            alert.showAndWait();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (final SQLException e) {
                LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "processFileInformationSearch", e.getMessage(), e);
            }
        }
    }

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

    private void processResultFileInformationSearch(@Nullable final ResultSet rs, @NonNull final File file) throws SQLException {
        if (rs == null || !rs.next()) {
            final Text text1 = new Text("No match found in plex database for file: ");
            text1.setStyle("-fx-font-weight: bold; -fx-fill: #FFFFFF");
            final Text text2 = new Text(file.getAbsolutePath() + "\n");
            text2.setStyle("-fx-fill: #FFFFFF");

            this.resultArea.getChildren().addAll(text1, text2);
        } else {
            final LinkedList<@NonNull Node> resultList = new LinkedList<>();
            final int i = 1;
            Text text = new Text("Result(s) founded for:");
            text.setStyle("-fx-font-weight: bold; -fx-fill: #FFFFFF; -fx-underline: true;");
            resultList.add(text);
            text = new Text(file.getAbsolutePath() + "\n\n");
            text.setStyle("-fx-font-style: italic; -fx-fill: #FFFFFF; ");
            resultList.add(text);
            do {
                text = new Text(i + ") ");
                text.setStyle("-fx-fill: #FFFFFF; -fx-font-weight: bold; -fx-text-origin: top;");
                resultList.add(text);
                final String guid = rs.getString("guid");
                final String title = rs.getString("title");
                final Hyperlink hyperlink = addLinkInformation(resultList, guid, title);
                addImage(resultList, rs, hyperlink);
            } while (rs.next());

            text = new Text("\n---------------------------------------------------------\n");
            text.setStyle("-fx-font-style: italic; -fx-fill: #FFFFFF; ");
            resultList.add(text);

            this.resultArea.getChildren().addAll(resultList);
        }
    }

    private void addImage(@NonNull final LinkedList<@NonNull Node> resultList, @NonNull final ResultSet rs, @Nullable final Hyperlink hyperlink) throws SQLException {
        try {
            String userThumbUrl = rs.getString("user_thumb_url");
            if (userThumbUrl != null) {
                final int sectionTypeValue = rs.getInt("section_type");
                final PlexConstants.SECTION_TYPE sectionType = PlexConstants.SECTION_TYPE.getSectionType(sectionTypeValue);
                if (sectionType != null) {
                    final int beginIndex = userThumbUrl.lastIndexOf("/");
                    final int endIndex = userThumbUrl.length();
                    userThumbUrl = userThumbUrl.substring(beginIndex, endIndex);
                    final String guid = rs.getString("guid");
                    final String shaGuid = DigestUtils.sha1Hex(guid);
                    final String cacheDir = shaGuid.substring(0, 1);
                    final String shaDir = shaGuid.substring(1, shaGuid.length());

                    final StringBuilder sb = new StringBuilder();
                    sb.append(getCurrentPlexDBDirectory().getAbsolutePath());
                    sb.append("/metadata/");
                    sb.append(sectionType.getDirectoryName());
                    sb.append("/");
                    sb.append(cacheDir);
                    sb.append("/");
                    sb.append(shaDir);
                    sb.append(".bundle/Contents/_stored/posters/");
                    sb.append(userThumbUrl);
                    if (DevConstants.DEBUG) {
                        System.err.println("[PlexToolsTabControler] addImage - " + sb.toString());
                    }

                    try {
                        final Image image = new Image(new FileInputStream(sb.toString()), 300, 150, true, false);
                        final ImageView imageView = new ImageView(image);
                        if (hyperlink == null) {
                            resultList.add(imageView);
                        } else {
                            hyperlink.setGraphic(imageView);
                            resultList.add(hyperlink);
                        }

                        final Text text = new Text(" ");
                        text.setStyle("-fx-fill: #FFFFFF; -fx-font-weight: bold;");
                        resultList.add(text);
                    } catch (final FileNotFoundException e) {
                        LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addImage", e.getMessage(), e);
                    }

                }
            }
        } catch (final StringIndexOutOfBoundsException e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addImage", e.getMessage(), e);
        } catch (final NoSuchFileException e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addImage", e.getMessage(), e);
        }
    }

    @Nullable
    private Hyperlink addLinkInformation(final LinkedList<@NonNull Node> resultList, @Nullable final String guid, @Nullable final String title) {
        Hyperlink result = null;
        if (guid != null && title != null) {
            final int index = guid.indexOf("://");
            if (index > 0) {
                final int size = guid.length();
                try {
                    final String urlValue = guid.substring(index + 3, size);
                    final StringBuilder sb = new StringBuilder();
                    if (guid.toLowerCase().contains("imdb")) {
                        sb.append("http://www.imdb.com/title/");
                        sb.append(urlValue);
                    } else if (guid.toLowerCase().contains("freebase")) {
                        // deprecated
                    } else if (guid.toLowerCase().contains("themoviedb")) {
                        sb.append("https://www.themoviedb.org/movie/");
                        sb.append(urlValue);
                    } else if (guid.toLowerCase().contains("thetvdb")) {

                    }

                    final Hyperlink link = new Hyperlink(title);
                    link.setStyle("-fx-text-origin:bottom");
                    link.setOnAction(e -> {
                        try {
                            Desktop.getDesktop().browse(new URI(sb.toString()));
                        } catch (final IOException | URISyntaxException e11) {
                            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addLinkInformation", e11.getMessage(), e11);
                        }
                    });

                    result = new Hyperlink();
                    result.setOnAction(e -> {
                        try {
                            Desktop.getDesktop().browse(new URI(sb.toString()));
                        } catch (final IOException | URISyntaxException e11) {
                            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addLinkInformation", e11.getMessage(), e11);
                        }
                    });
                    resultList.add(link);
                } catch (final Exception e) {
                    LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addLinkInformation", e.getMessage(), e);
                }
            }
        }
        return result;
    }

    private Stage createProgressDialog(final Service<Void> service, final Stage owner) {
        final Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.initOwner(owner);
        final BorderPane root = new BorderPane();
        root.getStyleClass().add(".my-borderpane");
        final ProgressBar indicator = new ProgressBar();
        // have the indicator display the progress of the service:
        indicator.progressProperty().bind(service.progressProperty());
        // hide the stage when the service stops running:
        service.stateProperty().addListener((ChangeListener<State>) (observable, oldValue, newValue) -> {
            if (newValue == State.CANCELLED || newValue == State.FAILED
                    || newValue == State.SUCCEEDED) {
                stage.hide();
            }
        });
        root.setCenter(indicator);
        final Text text = new Text("Importing Plex Database");
        text.setStyle("-fx-font-weight: bold; -fx-fill: #FFFFFF; -fx-underline: true;");
        root.setTop(text);
        final Scene scene = new Scene(root, 200, 100);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("jmediainspector/application.css").toExternalForm());
        stage.setScene(scene);
        return stage;
    }
}
