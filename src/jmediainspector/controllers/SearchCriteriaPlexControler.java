package jmediainspector.controllers;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import javafx.fxml.FXML;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper.Type;

public class SearchCriteriaPlexControler extends AbstractSearchCriteriaController {

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

    @Override
    void handleSearchResult(@NonNull final List<@NonNull File> result) {
        // For each result: match with plex

        // display
        for (final File file : result) {
            System.err.println("[SearchCriteriaControler] handleSearchResult - " + file.getAbsolutePath());
        }
    }

}
