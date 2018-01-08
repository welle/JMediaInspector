package jmediainspector.services;

import java.io.File;

import org.eclipse.jdt.annotation.NonNull;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jmediainspector.services.tasks.CopyFileTask;

/**
 * Copy file service.
 *
 * @author Cha
 */
public final class CopyFileService extends Service<File> {

    private @NonNull final File sourceFile;
    private @NonNull final File targetFile;

    /**
     * Constructor.
     *
     * @param sourceFile
     * @param targetFile
     */
    public CopyFileService(@NonNull final File sourceFile, @NonNull final File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    protected Task<File> createTask() {
        return new CopyFileTask(this.sourceFile, this.targetFile);
    }
}
