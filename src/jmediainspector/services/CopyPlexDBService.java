package jmediainspector.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Copy Plex database service.
 *
 * @author Cha
 */
public final class CopyPlexDBService extends Service<File> {

    private @NonNull static final Logger LOGGER = Logger.getLogger(CopyPlexDBService.class.getPackage().getName());

    private @NonNull final File sourceFile;
    private @NonNull final File targetFile;

    /**
     * Constructor.
     *
     * @param sourceFile
     * @param targetFile
     */
    public CopyPlexDBService(@NonNull final File sourceFile, @NonNull final File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    protected Task<File> createTask() {
        return new Task<File>() {
            @Override
            protected File call() {
                final long fileLength = CopyPlexDBService.this.sourceFile.length();
                updateProgress(0, (int) fileLength);
                try {
                    final FileInputStream fis = new FileInputStream(CopyPlexDBService.this.sourceFile);
                    final FileOutputStream fos = new FileOutputStream(CopyPlexDBService.this.targetFile);

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
                return CopyPlexDBService.this.targetFile;
            }
        };
    }
}
