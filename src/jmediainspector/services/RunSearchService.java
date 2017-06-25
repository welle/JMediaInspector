package jmediainspector.services;

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
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import jmediainspector.JMediaInspector;
import jmediainspector.context.ApplicationContext;
import jmediainspector.helpers.dialogs.DialogsHelper;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;

/**
 * Run search service.
 *
 * @author Cha
 */
public final class RunSearchService extends Service<List<@NonNull File>> {

    private @NonNull final List<@NonNull String> filePathList;
    private @NonNull List<@NonNull File> fileMatchingList = new ArrayList<>();
    @NonNull
    private final List<@NonNull AbstractInterface<?>> filterList;

    /**
     * Constructor.
     *
     * @param filePathList
     * @param result
     * @param filterList
     */
    public RunSearchService(@NonNull final List<@NonNull String> filePathList, @NonNull final List<@NonNull File> result, @NonNull final List<@NonNull AbstractInterface<?>> filterList) {
        this.filePathList = filePathList;
        this.fileMatchingList = result;
        this.filterList = filterList;
    }

    @Override
    protected Task<List<@NonNull File>> createTask() {
        return new Task<List<@NonNull File>>() {

            private int totalSize;

            @Override
            protected List<@NonNull File> call() {
                final long fileLength = RunSearchService.this.filePathList.size();
                final List<@NonNull File> fileToProcessList = new ArrayList<>();
                for (int i = 0; i < fileLength; i++) {
                    final String path = RunSearchService.this.filePathList.get(i);
                    final List<@NonNull File> files = getFilesInPath(path);
                    fileToProcessList.addAll(files);
                }
                this.totalSize = fileToProcessList.size();
                updateProgress(0, this.totalSize);
                for (int i = 0; i < this.totalSize; i++) {
                    final File file = fileToProcessList.get(i);
                    RunSearchService.this.fileMatchingList.addAll(fileMatched(file));
                    updateProgress(i, this.totalSize);
                }
                updateMessage("Found all.");

                return RunSearchService.this.fileMatchingList;
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
                    ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "RunSearchService.createTask().new Task() {...}", "searchInPath", e.getMessage(), e);
                    final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "'" + path + "' is not reachable!");
                    alert.showAndWait();
                }

                return result;
            }

            private @NonNull List<@NonNull File> fileMatched(@Nullable final File file) {
                final List<@NonNull File> result = new ArrayList<>();
                if (file != null) {
                    final Map<SearchTypeEnum, List<AbstractInterface<?>>> sublistMap = RunSearchService.this.filterList.stream()
                            .collect(Collectors.groupingBy(AbstractInterface::getType, Collectors.toList()));

                    final AndSearch rootSearch = new AndSearch();
                    for (final Entry<SearchTypeEnum, List<AbstractInterface<?>>> entry : sublistMap.entrySet()) {
                        final List<AbstractInterface<?>> list = entry.getValue();
                        if (!list.isEmpty()) {
                            final AndSearch subRootAndSearch = new AndSearch(true);
                            // Get all required
                            @NonNull
                            final OperatorSearchInterface @NonNull [] allSearch = getAllSearch(true, list);
                            if (allSearch.length > 0) {
                                final AndSearch andSearch = new AndSearch(true);
                                for (final OperatorSearchInterface operatorSearchInterface : allSearch) {
                                    andSearch.addSearch(operatorSearchInterface);
                                }
                                subRootAndSearch.addSearch(andSearch);
                            }

                            // Get all not required
                            @NonNull
                            final OperatorSearchInterface @NonNull [] allSearch2 = getAllSearch(false, list);
                            if (allSearch2.length > 0) {
                                final OrSearch orSearch = new OrSearch();
                                for (final OperatorSearchInterface operatorSearchInterface : allSearch2) {
                                    orSearch.addSearch(operatorSearchInterface);
                                }
                                subRootAndSearch.addSearch(orSearch);
                            }
                        }
                    }

                    if (rootSearch.isFileMatchingCriteria(file)) {
                        result.add(file);
                    }
                }

                return result;
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
        };
    }
}
