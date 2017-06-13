package jmediainspector.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import jmediainspector.JMediaInspector;
import jmediainspector.config.Configuration;
import jmediainspector.config.Criteria;
import jmediainspector.config.Filter;
import jmediainspector.config.Paths;
import jmediainspector.config.Search;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.config.helpers.PlexConfigurationHelper;
import jmediainspector.context.ApplicationContext;
import jmediainspector.helpers.dialogs.DialogsHelper;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.commons.ConditionFilter;
import jmediainspector.helpers.search.types.audio.filters.AudioCodecCriteria;
import jmediainspector.helpers.search.types.interfaces.FiltersInterface;
import jmediainspector.helpers.search.types.video.filters.VideoResolutionCriteria;
import jmediainspector.listeners.ApplicationConfigurationsListener;

public class SearchCriteriaControler extends AnchorPane implements ApplicationConfigurationsListener {

    @NonNull
    private final static Logger LOGGER = Logger.getLogger(SearchCriteriaControler.class.getName());
    private MetadataSearchConfigurationHelper metadataSearchCriteriaHelper;

    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;

    private SearchHelper searchHelper;
    private PlexConfigurationHelper configurationHelper;

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

//        this.configurationsList.setButtonCell(new ConfigurationListCell());
//        this.configurationsList.setCellFactory(p -> new ConfigurationListCell());
//
//        refreshConfigurationList();
//        setSelectedConfiguration();
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

    @Override
    public void onChanges() {
        // reload file
    }
}
