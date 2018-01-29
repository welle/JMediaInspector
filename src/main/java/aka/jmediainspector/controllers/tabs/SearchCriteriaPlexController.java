package aka.jmediainspector.controllers.tabs;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import aka.jmediainspector.config.Configuration;
import aka.jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import aka.jmediainspector.config.helpers.MetadataSearchConfigurationHelper.Type;
import aka.jmediainspector.services.FileSearchService;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

/**
 * SearchCriteriaPlexController.
 *
 * @author charlottew
 */
public class SearchCriteriaPlexController extends AbstractSearchCriteriaController {

    private MetadataSearchConfigurationHelper metadataSearchCriteriaHelper;
    private WebView webView;

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
    Service<List<@NonNull File>> getSearchService(@Nullable final Configuration selectedConfiguration, @NonNull final List<@NonNull String> pathsList) {
        return new FileSearchService(pathsList, this.searchHelper.getFiltersList());
    }

    /**
     * Set the webview where to display results.
     *
     * @param webView parent webview
     */
    public void setResultWebView(@NonNull final WebView webView) {
        this.webView = webView;
    }

}
