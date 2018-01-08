package jmediainspector.services.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.jmetadataquery.main.types.search.operation.AndSearch;
import aka.jmetadataquery.main.types.search.operation.OrSearch;
import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import jmediainspector.JMediaInspector;
import jmediainspector.context.ApplicationContext;
import jmediainspector.helpers.dialogs.DialogsHelper;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.interfaces.AbstractInterface;

/**
 * File search task to be called into a service.
 *
 * @author charlottew
 */
public class FileSearchTask extends Task<List<@NonNull File>> {

    private @NonNull final List<@NonNull String> filePathList;
    @NonNull
    private final List<@NonNull AbstractInterface<?>> filterList;

    private int totalSize;

    /**
     * Constructor.
     *
     * @param filePathList list of path to file(s)
     * @param filterList list of filter
     */
    public FileSearchTask(@NonNull final List<@NonNull String> filePathList, @NonNull final List<@NonNull AbstractInterface<?>> filterList) {
        this.filterList = filterList;
        this.filePathList = filePathList;
    }

    @Override
    protected List<@NonNull File> call() {
        final List<@NonNull File> fileMatchingList = new ArrayList<>();
        final long fileLength = this.filePathList.size();
        final List<@NonNull File> fileToProcessList = new ArrayList<>();
        for (int i = 0; i < fileLength; i++) {
            final String path = this.filePathList.get(i);
            final List<@NonNull File> files = getFilesInPath(path);
            fileToProcessList.addAll(files);
        }
        this.totalSize = fileToProcessList.size();
        updateProgress(0, this.totalSize);
        final AndSearch rootSearch = getRootSearch();
        int i = 0;
        for (final File file : fileToProcessList) {
            final boolean isFileMatchingCriteria = rootSearch.isFileMatchingCriteria(file);
            if (isFileMatchingCriteria) {
                fileMatchingList.add(file);
            }
            updateProgress(i, this.totalSize);
            i++;
        }
        updateMessage("Found all.");

        return fileMatchingList;
    }

    @NonNull
    private List<@NonNull File> getFilesInPath(@NonNull final String path) {
        @NonNull
        final List<@NonNull File> result = new ArrayList<>();
        try {
            final File directory = new File(path);
            if (directory.isDirectory()) {
                final List<@NonNull File> files = (List<@NonNull File>) FileUtils.listFiles(directory, null, true);
                for (final @NonNull File currentFile : files) {
                    result.add(currentFile);
                }
            }
        } catch (final IllegalArgumentException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "FileSearchTask", "searchInPath", e.getMessage(), e);
            final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "'" + path + "' is not reachable!");
            alert.showAndWait();
        }

        return result;
    }

    @NonNull
    private AndSearch getRootSearch() {
        final Map<SearchTypeEnum, List<AbstractInterface<?>>> sublistMap = this.filterList.stream()
                .collect(Collectors.groupingBy(AbstractInterface::getType, Collectors.toList()));

        final AndSearch rootSearch = new AndSearch(true);
        for (final Entry<SearchTypeEnum, List<AbstractInterface<?>>> entry : sublistMap.entrySet()) {
            final List<AbstractInterface<?>> list = entry.getValue();
            if (!list.isEmpty()) {
                // Get all required
                @NonNull
                final OperatorSearchInterface @NonNull [] allRequiredSearch = getAllSearch(true, list);
                if (allRequiredSearch.length > 0) {
                    final AndSearch andSearch = new AndSearch(true);
                    boolean added = false;
                    for (final OperatorSearchInterface operatorSearchInterface : allRequiredSearch) {
                        andSearch.addSearch(operatorSearchInterface);
                        added = true;
                    }
                    if (added) {
                        rootSearch.addSearch(andSearch);
                    }
                }

                // Get all not required
                @NonNull
                final OperatorSearchInterface @NonNull [] allNotRequiredSearch = getAllSearch(false, list);
                if (allNotRequiredSearch.length > 0) {
                    final OrSearch orSearch = new OrSearch();
                    boolean added = false;
                    for (final OperatorSearchInterface operatorSearchInterface : allNotRequiredSearch) {
                        orSearch.addSearch(operatorSearchInterface);
                        added = true;
                    }
                    if (added) {
                        rootSearch.addSearch(orSearch);
                    }
                }
            }
        }

        return rootSearch;
    }

    private @NonNull OperatorSearchInterface @NonNull [] getAllSearch(final boolean required, final List<AbstractInterface<?>> list) {
        final List<@NonNull OperatorSearchInterface> result = new ArrayList<>();

        for (final AbstractInterface<?> abstractInterface : list) {
            @Nullable
            final AbstractInterface<?> criteria = abstractInterface.getCriteria();
            if (abstractInterface.getSelectedCheckBox().isSelected() && abstractInterface.getRequiredCheckBox().isSelected() == required && criteria != null) {
                final OperatorSearchInterface search = criteria.getSearch();
                if (search != null) {
                    result.add(search);
                }
            }
        }

        final OperatorSearchInterface[] stockArr = new OperatorSearchInterface[result.size()];
        return result.toArray(stockArr);
    }

}
