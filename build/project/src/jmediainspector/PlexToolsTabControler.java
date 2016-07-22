package jmediainspector;

import java.awt.Desktop;
import java.io.File;
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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import jmediainspector.helpers.FileChooserHelper;

/**
 * Controler for the Plex tools tab.
 *
 * @author Welle Charlotte
 */
public class PlexToolsTabControler extends AnchorPane {

    private @NonNull final static Logger LOGGER = Logger.getLogger(PlexToolsTabControler.class.getName());

    @Nullable
    private File plexFileDB = null;
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

    /**
     * Handle the select plex database button.
     *
     * @param event
     */
    public void clickPlexDBButton(final ActionEvent event) {
        final FileChooser fileChooser = FileChooserHelper.getPlexDBFileChooser();
        final File file = fileChooser.showOpenDialog(this.anchorPaneRoot.getScene().getWindow());
        this.plexFileDB = file;
        if (file == null) {
            this.selectedPlexDBText.setText(null);
        } else {
            final boolean isPlexDatabase = isPlexDatabase(file);
            if (isPlexDatabase) {
                final String absoluteFileName = file.getAbsolutePath();
                this.selectedPlexDBText.setText(absoluteFileName);
            } else {
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("This is not a Plex database file.");

                alert.showAndWait();
            }
        }
    }

    private boolean isPlexDatabase(@NonNull final File file) {
        boolean result = false;
        try {
            final Connection c = getPlexDatabaseConnection(file);
            final Statement stmt = c.createStatement();
            final ResultSet rs = stmt.executeQuery("Select * from media_parts;");
            rs.close();
            stmt.close();
            c.close();

            result = true;
        } catch (final ClassNotFoundException | SQLException e) {
            result = false;
        }
        return result;
    }

    /**
     * Handle the search missing media button.
     *
     * @param event
     */
    public void searchMissingMediaButton(final ActionEvent event) {
        try {
            final File currentPlexFileDB = getCurrentPlexDBFile();
            final FileChooser fileChooser = FileChooserHelper.getMediaFileChooser();
            final List<File> files = fileChooser.showOpenMultipleDialog(this.anchorPaneRoot.getScene().getWindow());
            if (files == null) {
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No file selected!");

                alert.showAndWait();
            } else {
                processFileInformationSearch(files, currentPlexFileDB);
            }
        } catch (final NoSuchFileException e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "searchMissingMediaButton", e.getMessage(), e);
        }
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
        final File currentPlexFileDB = this.plexFileDB;
        if (currentPlexFileDB == null) {
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Missing Plex database file!");

            alert.showAndWait();

            throw new NoSuchFileException("Missing Plex database file!");
        }

        final File parentFile = currentPlexFileDB.getParentFile().getParentFile();
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

    private void processFileInformationSearch(@NonNull final List<File> files, @NonNull final File plexDBFile) {
        try {
            final Connection c = getPlexDatabaseConnection(plexDBFile);
            this.resultArea.getChildren().clear();
            for (final File file : files) {
                System.err.println("[PlexToolsTabControler] processFileInformationSearch - " + file.toURI());
                final Statement stmt = c.createStatement();
                final ResultSet rs = stmt.executeQuery("Select T1.file, T3.* from media_parts as T1 inner join media_items as T2 on (T1.media_item_id == T2.id) inner join metadata_items as T3 on (T2.metadata_item_id == T3.id)  where T1.file =\"" + file.getAbsolutePath() + "\";");
                processResultFileInformationSearch(rs, file);
                rs.close();
                stmt.close();
            }
            c.close();
        } catch (final Exception e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "searchMissingMediaButton", e.getMessage(), e);
            final Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Can not open Plex database file!");

            alert.showAndWait();
        }
    }

    @NonNull
    private Connection getPlexDatabaseConnection(@NonNull final File plexDBFile) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        final StringBuilder sb = new StringBuilder();
        sb.append("jdbc:sqlite:");
        final URI absoluteFileName = plexDBFile.toURI();
        sb.append(absoluteFileName.toString());
        final Connection c = DriverManager.getConnection(sb.toString());
        assert c != null;
        return c;
    }

    private void processResultFileInformationSearch(@Nullable final ResultSet rs, @NonNull final File file) throws SQLException {
        if (rs == null || !rs.next()) {
            final Text text1 = new Text("No match found in plex database for file: ");
            text1.setStyle("-fx-font-weight: bold; -fx-fill: #FFFFFF");
            final Text text2 = new Text(file.getAbsolutePath());
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
                text.setStyle("-fx-fill: #FFFFFF; -fx-font-weight: bold;");
                resultList.add(text);
                addImage(resultList, rs);
                text = new Text(rs.getString("title"));
                text.setStyle("-fx-fill: #FFFFFF");
                resultList.add(text);
                final String guid = rs.getString("guid");
                addLinkInformation(resultList, guid);

            } while (rs.next());

            text = new Text("\n---------------------------------------------------------\n");
            text.setStyle("-fx-font-style: italic; -fx-fill: #FFFFFF; ");
            resultList.add(text);
            this.resultArea.getChildren().addAll(resultList);
        }
    }

    private void addImage(@NonNull final LinkedList<@NonNull Node> resultList, @NonNull final ResultSet rs) throws SQLException {
        try {
            String userThumbUrl = rs.getString("user_thumb_url");
            if (userThumbUrl != null) {
                final int beginIndex = userThumbUrl.lastIndexOf("/");
                final int endIndex = userThumbUrl.length();
                userThumbUrl = userThumbUrl.substring(beginIndex, endIndex);
                final String guid = rs.getString("guid");
                final String shaGuid = DigestUtils.sha1Hex(guid);
                final String cacheDir = shaGuid.substring(0, 1);
                final String shaDir = shaGuid.substring(1, shaGuid.length());

//                final String file = rs.getString("file");
                final StringBuilder sb = new StringBuilder();
                sb.append(getCurrentPlexDBDirectory().getAbsolutePath());
                sb.append(cacheDir);
                sb.append("/");
                sb.append(shaDir);
                sb.append(".bundle/Contents/_stored/posters/");
                sb.append(userThumbUrl);

                System.err.println("[PlexToolsTabControler] addImage - " + sb.toString());
            }

//            final ImageView imageView = new ImageView(url);

//        $cache_dir = $guid[0];
//        $item_sha1_dir = substr($guid, 1);
//
//        $base = substr($row['file'],0, strripos($row['file'], "/"));
//        $art_name = substr($row['user_art_url'] ,strripos($row['user_art_url'], "/") + 1);
//        $thumb_name = substr($row['user_thumb_url'] ,strripos($row['user_thumb_url'], "/") + 1);
//
//        $copy = $movie_dir . $cache_dir . "/" . $item_sha1_dir . ".bundle/Contents/_stored/";
//        $copyart = $copy . "art/" . $art_name;
//        $copyposter = $copy . "posters/" . $thumb_name;
//
//        echo $row['title']. "\n";
////        echo "Fanart: " . $copyart . "\n";
////        echo "Poster: " . $copyposter . "\n";
//
//        copy( $copyart, $base . "/fanart.jpg" );
//        copy( $copyposter, $base . "/poster.jpg" );
        } catch (final StringIndexOutOfBoundsException e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addImage", e.getMessage(), e);
        } catch (final NoSuchFileException e) {
            LOGGER.logp(Level.SEVERE, "PlexToolsTabControler", "addImage", e.getMessage(), e);
        }
    }

    private void addLinkInformation(final LinkedList<@NonNull Node> resultList, @Nullable final String guid) {
        if (guid != null) {
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

                    final Hyperlink link = new Hyperlink(sb.toString());
                    link.setOnAction(e -> {
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
    }
}
