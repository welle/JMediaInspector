package jmediainspector.helpers.dialogs;

import java.io.File;

import org.eclipse.jdt.annotation.NonNull;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * FileChooser helper.
 *
 * @author Welle Charlotte
 */
public final class FileChooserHelper {

    /**
     * Get file chooser for Plex database file.
     *
     * @return file chooser for Plex database file
     */
    @NonNull
    public static FileChooser getPlexDBFileChooser() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Plex database file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Plex database file", "*.db"), new FileChooser.ExtensionFilter("All Files", "*.*"));

        return fileChooser;
    }

    /**
     * Get directory chooser.
     *
     * @return directory chooser
     */
    @NonNull
    public static DirectoryChooser getDirectoryChooser() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        return directoryChooser;
    }

    /**
     * Get file chooser for media file.
     *
     * @return file chooser for media file
     */
    @NonNull
    public static FileChooser getMediaFileChooser() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select media file");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MKV", "*.mkv"), new FileChooser.ExtensionFilter("All Files", "*.*"));

        return fileChooser;
    }

    private FileChooserHelper() {
        // Singleton
    }
}
