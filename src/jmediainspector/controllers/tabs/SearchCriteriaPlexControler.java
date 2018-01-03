package jmediainspector.controllers.tabs;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import jmediainspector.config.Configuration;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper.Type;
import jmediainspector.services.FileSearchService;

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
    void handleSearchResult(final Object object) {
        if (object instanceof List<?>) {
            final List<File> result = (List<File>) object;
            System.err.println("[SearchCriteriaControler] handleSearchResult - " + result.size());
            // display
            for (final File file : result) {
                System.err.println("[SearchCriteriaControler] handleSearchResult - " + file.getAbsolutePath());
            }
        }

    }

    @Override
    Service getSearchService(@Nullable final Configuration selectedConfiguration, @NonNull final List<@NonNull String> pathsList) {
        return new FileSearchService(pathsList, this.searchHelper.getFiltersList());
    }

}
