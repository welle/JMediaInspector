package aka.jmediainspector.services.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.context.ApplicationContext;
import javafx.concurrent.Task;

/**
 * File copy task to be called into a service.
 *
 * @author charlottew
 */
public class CopyFileTask extends Task<@NonNull File> {

    private @NonNull final File sourceFile;
    private @NonNull final File targetFile;

    /**
     * Constructor.
     *
     * @param sourceFile file to be copied
     * @param targetFile destination file
     */
    public CopyFileTask(@NonNull final File sourceFile, @NonNull final File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    protected File call() {
        final long fileLength = this.sourceFile.length();
        updateProgress(0, (int) fileLength);
        try {
            final FileInputStream fis = new FileInputStream(this.sourceFile);
            final FileOutputStream fos = new FileOutputStream(this.targetFile);

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
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "CopyFileTask", "call", e.getMessage(), e);
        }
        updateMessage("Found all.");
        return this.targetFile;
    }
}
