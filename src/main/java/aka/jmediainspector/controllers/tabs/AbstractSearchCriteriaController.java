package aka.jmediainspector.controllers.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.jmediainspector.JMediaInspector;
import aka.jmediainspector.config.Configuration;
import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.config.Paths;
import aka.jmediainspector.config.Search;
import aka.jmediainspector.config.helpers.JAXBHelper;
import aka.jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import aka.jmediainspector.config.helpers.MetadataSearchConfigurationHelper.Type;
import aka.jmediainspector.config.helpers.PlexConfigurationHelper;
import aka.jmediainspector.context.ApplicationContext;
import aka.jmediainspector.helpers.dialogs.DialogsHelper;
import aka.jmediainspector.helpers.search.SearchEventHandler;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmediainspector.helpers.search.interfaces.SearchInterface;
import aka.jmediainspector.helpers.search.types.audio.SearchAudioEnum;
import aka.jmediainspector.helpers.search.types.general.SearchGeneralEnum;
import aka.jmediainspector.helpers.search.types.text.SearchTextEnum;
import aka.jmediainspector.helpers.search.types.video.SearchVideoEnum;
import aka.jmediainspector.listeners.ApplicationConfigurationsListener;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Abstract class for SearchCriteria Controllers.
 *
 * @author Cha
 */
public abstract class AbstractSearchCriteriaController extends AnchorPane implements ApplicationConfigurationsListener {

    private MetadataSearchConfigurationHelper metadataSearchCriteriaHelper;

    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;
    @FXML
    private Menu menuGeneral;
    @FXML
    private Menu menuVideo;
    @FXML
    private Menu menuAudio;
    @FXML
    private Menu menuText;
    @FXML
    private Button runButton;

    /**
     * SearchHelper.
     */
    protected SearchHelper searchHelper;
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

        initMenuVideo();
        initMenuGeneral();
        initMenuAudio();
        initMenuText();
//        this.configurationsList.setButtonCell(new ConfigurationListCell());
//        this.configurationsList.setCellFactory(p -> new ConfigurationListCell());
//
//        refreshConfigurationList();
//        setSelectedConfiguration();
    }

    private void initMenuVideo() {
        try {
            // Sort by items full name
            final Map<String, AbstractInterface<?>> map = getSearchItemMenuMap(SearchVideoEnum.values());
            final SortedSet<String> keys = new TreeSet<>(map.keySet());
            for (final String key : keys) {
                final AbstractInterface<?> newInstance = map.get(key);
                // do something
                assert newInstance != null;
                final MenuItem menuItem = new MenuItem(key);
                menuItem.setOnAction(new SearchEventHandler(this.searchHelper, newInstance, this));
                this.menuVideo.getItems().add(menuItem);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "AbstractSearchCriteriaController", "initMenuVideo", e.getMessage(), e);
        }
    }

    private void initMenuGeneral() {
        try {
            // Sort by items full name
            final Map<String, AbstractInterface<?>> map = getSearchItemMenuMap(SearchGeneralEnum.values());
            final SortedSet<String> keys = new TreeSet<>(map.keySet());
            for (final String key : keys) {
                final AbstractInterface<?> newInstance = map.get(key);
                // do something
                assert newInstance != null;
                final MenuItem menuItem = new MenuItem(key);
                menuItem.setOnAction(new SearchEventHandler(this.searchHelper, newInstance, this));
                this.menuGeneral.getItems().add(menuItem);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "AbstractSearchCriteriaController", "initMenuGeneral", e.getMessage(), e);
        }
    }

    @NonNull
    private Map<String, AbstractInterface<?>> getSearchItemMenuMap(final @NonNull SearchInterface @NonNull [] searchInterfaceArray) throws InstantiationException, IllegalAccessException {
        final Map<String, AbstractInterface<?>> map = new HashMap<>();
        for (final @NonNull SearchInterface entry : searchInterfaceArray) {
            final AbstractInterface<?> newInstance = entry.getFiltersInterface().newInstance();
            assert newInstance != null;
            final String fullName = newInstance.getFullName();
            map.put(fullName, newInstance);
        }
        return map;
    }

    private void initMenuAudio() {
        try {
            // Sort by items full name
            final Map<String, AbstractInterface<?>> map = getSearchItemMenuMap(SearchAudioEnum.values());
            final SortedSet<String> keys = new TreeSet<>(map.keySet());
            for (final String key : keys) {
                final AbstractInterface<?> newInstance = map.get(key);
                // do something
                assert newInstance != null;
                final MenuItem menuItem = new MenuItem(key);
                menuItem.setOnAction(new SearchEventHandler(this.searchHelper, newInstance, this));
                this.menuAudio.getItems().add(menuItem);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "AbstractSearchCriteriaController", "initMenuAudio", e.getMessage(), e);
        }
    }

    private void initMenuText() {
        try {
            // Sort by items full name
            final Map<String, AbstractInterface<?>> map = getSearchItemMenuMap(SearchTextEnum.values());
            final SortedSet<String> keys = new TreeSet<>(map.keySet());
            for (final String key : keys) {
                final AbstractInterface<?> newInstance = map.get(key);
                // do something
                assert newInstance != null;
                final MenuItem menuItem = new MenuItem(key);
                menuItem.setOnAction(new SearchEventHandler(this.searchHelper, newInstance, this));
                this.menuText.getItems().add(menuItem);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "AbstractSearchCriteriaController", "initMenuText", e.getMessage(), e);
        }
    }

    /**
     * Init existing searches.
     *
     * @param type
     */
    void initExistingSearches(@NonNull final Type type) {
        // FIXME this method and all relevant methods must be in a upper controler so each searchhelper correspond to a search and we just have to set visible true/false on left list view click
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
                setText(item.getName());
            }
        }
    }

    /**
     * Get new criteria.
     *
     * @return new criteria
     */
    @NonNull
    public Criteria getNewCriteria() {
        final Criteria newCriteria = this.metadataSearchCriteriaHelper.getNewCriteria();
        assert newCriteria != null;
        newCriteria.setSelected(true);
        return newCriteria;
    }

    @FXML
    private void runSearch() {
        // PUT IN SERVICE, disable run button
        final List<@NonNull AbstractInterface<?>> filterList = this.searchHelper.getFiltersList();
        if (!filterList.isEmpty()) {
            @Nullable
            final Configuration selectedConfiguration = this.configurationHelper.getSelectedConfiguration();
            if (selectedConfiguration == null) {
                final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "No configuration selected!");
                alert.showAndWait();
            } else {
                final Paths paths = selectedConfiguration.getPaths();
                if (paths == null || paths.getPath().size() == 0) {
                    final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "No path(s) in the selected configuration!");
                    alert.showAndWait();
                } else {
                    final List<@NonNull String> pathsList = new ArrayList<>();
                    for (final String path : paths.getPath()) {
                        if (path != null && path.trim().length() > 0) {
                            pathsList.add(path);
                        }
                    }
                    callSearchService(selectedConfiguration, pathsList);
                }
            }
        }
    }

    /**
     * Get search service.
     *
     * @param selectedConfiguration current selected configuration
     * @param pathsList path list directories
     * @return Service
     */
    @NonNull
    abstract Service<?> getSearchService(@Nullable Configuration selectedConfiguration, @NonNull final List<@NonNull String> pathsList);

    private void callSearchService(@Nullable final Configuration selectedConfiguration, @NonNull final List<@NonNull String> pathsList) {
        final Service<?> service = getSearchService(selectedConfiguration, pathsList);

        final Stage stage = (Stage) JMediaInspector.getPrimaryStage().getScene().getWindow();
        assert stage != null;
        final Dialog<String> progressDialog = DialogsHelper.createProgressRunSearchServiceDialog(service, stage);
        final Effect parentEffect = new BoxBlur();

        this.runButton.setDisable(true);
        JMediaInspector.getPrimaryStage().getScene().getRoot().setEffect(parentEffect);
        service.stateProperty().addListener((ChangeListener<State>) (observable, oldValue, newValue) -> {
            if (newValue == State.CANCELLED || newValue == State.FAILED || newValue == State.SUCCEEDED) {
                final Window window = progressDialog.getDialogPane().getScene().getWindow();
                window.hide();
                JMediaInspector.getPrimaryStage().getScene().getRoot().setEffect(null);
            }
        });
        progressDialog.show();
        service.reset();
        service.setOnSucceeded(e -> {
            final Window window = progressDialog.getDialogPane().getScene().getWindow();
            window.hide();
            JMediaInspector.getPrimaryStage().getScene().getRoot().setEffect(null);
            handleSearchResult(service.getValue());
        });
        this.runButton.setDisable(false);
        service.start();
    }

    /**
     * Handle the result.
     *
     * @param object result of the service
     */
    abstract void handleSearchResult(Object object);

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
