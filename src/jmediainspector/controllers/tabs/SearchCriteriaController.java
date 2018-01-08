package jmediainspector.controllers.tabs;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import jmediainspector.JMediaInspector;
import jmediainspector.config.Configuration;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper;
import jmediainspector.config.helpers.MetadataSearchConfigurationHelper.Type;
import jmediainspector.context.ApplicationContext;
import jmediainspector.helpers.dialogs.DialogsHelper;
import jmediainspector.helpers.html.SearchFileResultHelper;
import jmediainspector.services.FileSearchService;

/**
 * FX Controller for file searches.
 *
 * @author charlottew
 */
public class SearchCriteriaController extends AbstractSearchCriteriaController {

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

        initExistingSearches(Type.File);
    }

    @Override
    Service<List<@NonNull File>> getSearchService(@Nullable final Configuration selectedConfiguration, @NonNull final List<@NonNull String> pathsList) {
        return new FileSearchService(pathsList, this.searchHelper.getFiltersList());
    }

    @Override
    void handleSearchResult(final Object object) {
        if (object instanceof List<?>) {
            final List<File> result = (List<File>) object;

            try {
                final WebEngine webEngine = this.webView.getEngine();
                // Delete cache for navigate back
                webEngine.load("about:blank");
                // Delete cookies
                java.net.CookieHandler.setDefault(new java.net.CookieManager());
                final String htmlResult = SearchFileResultHelper.processResultFileInformationSearch(result);
                // load new results
                webEngine.loadContent(htmlResult);
            } catch (final Exception e) {
                ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "SearchCriteriaController", "searchMissingMediaButton", e.getMessage(), e);
                final Alert alert = DialogsHelper.getAlert(JMediaInspector.getPrimaryStage(), Alert.AlertType.ERROR, "Can not open Plex database file!");
                alert.showAndWait();
            }

            System.err.println("[SearchCriteriaControler] handleSearchResult - " + result.size());
            // display
            for (final File file : result) {
                System.err.println("[SearchCriteriaControler] handleSearchResult - " + file.getAbsolutePath());
            }
        }
    }

    public void setResultWebView(final WebView webView) {
        this.webView = webView;
    }

}
