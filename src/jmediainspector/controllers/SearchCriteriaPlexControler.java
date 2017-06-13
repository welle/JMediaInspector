package jmediainspector.controllers;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import javafx.fxml.FXML;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper.Type;

public class SearchCriteriaPlexControler extends AbstractSearchCriteriaControler {

    @NonNull
    private final static Logger LOGGER = Logger.getLogger(SearchCriteriaPlexControler.class.getName());
    private MetadataSearchConfigurationHelper metadataSearchCriteriaHelper;

    /**
     * Initialize component.
     * Internal use only.
     */
    @Override
    @FXML
    public void initialize() {
        super.initialize();

        initExistingSearches(Type.Plex);
    }

}
