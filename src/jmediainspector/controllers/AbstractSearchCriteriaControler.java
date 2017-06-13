package jmediainspector.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import jmediainspector.JMediaInspector;
import jmediainspector.config.Configuration;
import jmediainspector.config.Criteria;
import jmediainspector.config.Filter;
import jmediainspector.config.Paths;
import jmediainspector.config.Search;
import jmediainspector.config.helpers.JAXBHelper;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper.Type;
import jmediainspector.config.helpers.PlexConfigurationHelper;
import jmediainspector.context.ApplicationContext;
import jmediainspector.helpers.dialogs.DialogsHelper;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.types.audio.filters.AudioCodecCriteria;
import jmediainspector.helpers.search.types.interfaces.FiltersInterface;
import jmediainspector.helpers.search.types.video.filters.VideoResolutionCriteria;
import jmediainspector.listeners.ApplicationConfigurationsListener;

public class AbstractSearchCriteriaControler extends AnchorPane implements ApplicationConfigurationsListener {

    @NonNull
    private final static Logger LOGGER = Logger.getLogger(AbstractSearchCriteriaControler.class.getName());
    protected MetadataSearchConfigurationHelper metadataSearchCriteriaHelper;

    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;

    private SearchHelper searchHelper;
    private PlexConfigurationHelper configurationHelper;

    @FXML
    private ListView<Search> existingSearchListView;
    private Type searchType;

    /**
     * Initialize component.
     * Internal use only.
     */
    @FXML
    public void initialize() {
        this.configurationHelper = ApplicationContext.getInstance().getCurrentPlexConfigurationHelper();

        // TODO link searchhelper with related search
        final AnchorPane currentLeftPane = this.leftPane;
        assert currentLeftPane != null;
        final AnchorPane currentRightPane = this.rightPane;
        assert currentRightPane != null;

        this.searchHelper = new SearchHelper(new Search(), currentLeftPane, currentRightPane);

        this.metadataSearchCriteriaHelper = ApplicationContext.getInstance().getCurrentMetadataSearchConfigurationHelper();
        this.existingSearchListView.setCellFactory(p -> new SearchCellListView(this.existingSearchListView));

//        this.configurationsList.setButtonCell(new ConfigurationListCell());
//        this.configurationsList.setCellFactory(p -> new ConfigurationListCell());
//
//        refreshConfigurationList();
//        setSelectedConfiguration();
    }

    /**
     * Init existing searches.
     *
     * @param type
     */
    void initExistingSearches(@NonNull final Type type) {
        this.searchType = type;
        this.existingSearchListView.getItems().clear();
        final List<Search> list = this.metadataSearchCriteriaHelper.getSearchByType(type);
        for (final Search search : list) {
            this.existingSearchListView.getItems().add(search);
        }
    }

    private final class SearchCellListView extends ListCell<Search> {

        public SearchCellListView(final ListView<Search> listView) {
            setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (listView.getItems().size() > 0)) {
                    final Search rowData = listView.getSelectionModel().getSelectedItem();
                    System.out.println(rowData);
                }
            });
        }

        @Override
        protected void updateItem(final Search item, final boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                System.err.println("[AbstractSearchCriteriaControler.SearchTableRow] updateItem - " + item.getName());
                setText(item.getName());
            }
        }
    }

    @FXML
    private void addAudioCodecCriteria() {
        final Filter filter = getNewCriteria();
        final AudioCodecCriteria audioCodecCriteria = new AudioCodecCriteria(filter);

        this.searchHelper.addCriteria(audioCodecCriteria);
    }

    @FXML
    private void addVideoResolutionCriteria() {
        final Filter filter = getNewCriteria();
        filter.setType(ConditionFilter.GREATER_THAN.name());
        final VideoResolutionCriteria videoResolutionCriteria = new VideoResolutionCriteria(filter);

        this.searchHelper.addCriteria(videoResolutionCriteria);
    }

    @NonNull
    private Filter getNewCriteria() {
        final Criteria newCriteria = this.metadataSearchCriteriaHelper.getNewCriteria();
        assert newCriteria != null;
        final Filter filter = this.metadataSearchCriteriaHelper.getNewFilter();
        filter.setSelected(true);
        return filter;
    }

    @FXML
    private void runSearch() {
        final List<@NonNull File> result = new ArrayList<>();
        @Nullable
        final Configuration selectedConfiguration = this.configurationHelper.getSelectedConfiguration();
        if (selectedConfiguration == null) {
            final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "No configuration selected!");
            alert.showAndWait();
        } else {
            final Paths paths = selectedConfiguration.getPaths();
            if (paths == null) {
                final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "No path(s) in the selected configuration!");
                alert.showAndWait();
            } else {
                for (final String path : paths.getPath()) {
                    if (path != null && path.trim().length() > 0) {
                        final List<@NonNull File> searchInPathResult = searchInPath(path);
                        result.addAll(searchInPathResult);
                    }
                }
            }
        }
    }

    @NonNull
    private List<@NonNull File> searchInPath(@NonNull final String path) {
        File file = null;
        try {
            file = new File(path);
        } catch (final IllegalArgumentException e) {
            final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "'" + path + "' is not reachable!");
            alert.showAndWait();
        }

        return searchInPath(file);
    }

    private @NonNull List<@NonNull File> searchInPath(@Nullable final File file) {
        final List<@NonNull File> result = new ArrayList<>();
        if (file != null) {
            final List<@NonNull FiltersInterface> filterList = this.searchHelper.getFiltersList();
            if (!filterList.isEmpty()) {

            }
        }

        return result;
    }

    @FXML
    private void saveSearch() {
        processSave(false);
    }

    @FXML
    private void saveAsSearch() {
        processSave(true);
    }

    private void processSave(final boolean saveAs) {
        boolean processSave = true;
        final Search currentSearch = this.existingSearchListView.getSelectionModel().getSelectedItem();
        Search newSearch;
        if (currentSearch == null) {
            final TextInputDialog dialog = DialogsHelper.getSaveAsDialog();
            final Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                final String searchName = result.get();
                newSearch = this.metadataSearchCriteriaHelper.addNewSearch(getSearchType());
                newSearch.setName(searchName);
                this.metadataSearchCriteriaHelper.getSearchByType(getSearchType()).add(newSearch);
            } else {
                final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "Cancel saving...");
                alert.showAndWait();
                processSave = false;
            }
        } else {
            if (!saveAs) {
                final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.CONFIRMATION, "Override current search ?");
                final Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    final ButtonType button = result.get();
                    if (button == ButtonType.CANCEL) {
                        processSave = false;
                    } else {
                        newSearch = JAXBHelper.deepCopyJAXB(currentSearch);
                    }
                }
            }
        }

        if (processSave) {
            this.metadataSearchCriteriaHelper.saveConfig();

            // refresh table view
            initExistingSearches(getSearchType());
        }
    }

    @NonNull
    private Type getSearchType() {
        final Type currentType = this.searchType;
        assert currentType != null;

        return currentType;
    }

    @Override
    public void onChanges() {
        // reload file
    }
}
