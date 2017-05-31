package jmediainspector.controllers;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import jmediainspector.config.Criteria;
import jmediainspector.config.Filter;
import jmediainspector.config.Search;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.context.ApplicationContext;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.types.audio.filters.AudioCodecCriteria;
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

    /**
     * Initialize component.
     * Internal use only.
     */
    @FXML
    public void initialize() {
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

    @Override
    public void onChanges() {
        // reload file
    }
}
