package jmediainspector.controllers;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import javafx.fxml.FXML;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper.Type;

public class SearchCriteriaControler extends AbstractSearchCriteriaController {

    private MetadataSearchConfigurationHelper metadataSearchCriteriaHelper;

    /**
     * Initialize component.
     * Internal use only.
     */
    @Override
    @FXML
    public void initialize() {
        super.initialize();

        initExistingSearches(Type.File);
    }

    @Override
    void handleSearchResult(final List<@NonNull File> result) {
        // display
    }

}
