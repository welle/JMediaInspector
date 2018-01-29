package aka.jmediainspector.services;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmediainspector.services.tasks.FileSearchTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Run search service.
 *
 * @author Cha
 */
public final class FileSearchService extends Service<List<@NonNull File>> {

    private @NonNull final List<@NonNull String> filePathList;
    @NonNull
    private final List<@NonNull AbstractInterface<?>> filterList;

    /**
     * Constructor.
     *
     * @param filePathList
     * @param filterList
     */
    public FileSearchService(@NonNull final List<@NonNull String> filePathList, @NonNull final List<@NonNull AbstractInterface<?>> filterList) {
        this.filePathList = filePathList;
        this.filterList = filterList;
    }

    @Override
    protected Task<List<@NonNull File>> createTask() {
        return new FileSearchTask(this.filePathList, this.filterList);
    }
}
