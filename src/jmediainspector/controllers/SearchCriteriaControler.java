package jmediainspector.controllers;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import jmediainspector.config.Criteria;
import jmediainspector.config.Filter;
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
    private GridPane rightCriteriaPane;

    private int rowIndex = 0;
    private SearchHelper searchHelper;

    /**
     * Initialize component.
     * Internal use only.
     */
    @FXML
    public void initialize() {
        this.searchHelper = new SearchHelper();

        this.leftPane.getChildren().add(this.searchHelper.getLeftPanel());

        this.metadataSearchCriteriaHelper = ApplicationContext.getInstance().getCurrentMetadataSearchConfigurationHelper();

//        this.configurationsList.setButtonCell(new ConfigurationListCell());
//        this.configurationsList.setCellFactory(p -> new ConfigurationListCell());
//
//        refreshConfigurationList();
//        setSelectedConfiguration();
    }

    @FXML
    private void addAudioCodecCriteria() {
        final Criteria newCriteria = this.metadataSearchCriteriaHelper.getNewCriteria();
        assert newCriteria != null;
        final Filter filter = this.metadataSearchCriteriaHelper.getNewFilter();
        filter.setSelected(true);
        final AudioCodecCriteria videoResolutionCriteria = new AudioCodecCriteria(filter);

        this.rightCriteriaPane.addRow(this.rowIndex, videoResolutionCriteria.getRightPaneChoices());
        this.rowIndex++;
    }

    @FXML
    private void addVideoResolutionCriteria() {
        final Criteria newCriteria = this.metadataSearchCriteriaHelper.getNewCriteria();
        assert newCriteria != null;
        final Filter filter = this.metadataSearchCriteriaHelper.getNewFilter();
        filter.setSelected(true);
        final VideoResolutionCriteria videoResolutionCriteria = new VideoResolutionCriteria(filter);

        this.rightCriteriaPane.addRow(this.rowIndex, videoResolutionCriteria.getRightPaneChoices());
        this.rowIndex++;
    }

    @Override
    public void onChanges() {
        // reload file
    }
}
