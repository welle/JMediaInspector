package aka.jmediainspector;

import java.io.IOException;
import java.util.ListResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.constants.ApplicationConstants;
import aka.jmediainspector.controllers.tabs.ToolsTabControler;
import aka.jmediainspector.helpers.dialogs.DialogsHelper;
import aka.jmediainspector.helpers.effects.EffectUtilities;
import aka.jmediainspector.helpers.effects.ResizeHelper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main Application.
 *
 * @author Welle Charlotte
 */
public class JMediaInspector extends Application {

    private @NonNull final static Logger LOGGER = Logger.getLogger(JMediaInspector.class.getName());

    private static Stage primaryStage;
    private AnchorPane rootLayout;
    @FXML
    private final ToolsTabControler plexToolsTabPage = new ToolsTabControler();

    private Node menuBar;

    /**
     * Main.
     *
     * @param args
     *            not used.
     */
    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * Get primary stage.
     *
     * @return primary stage
     */
    @NonNull
    static public Stage getPrimaryStage() {
        final Stage currentPrimaryStage = JMediaInspector.primaryStage;
        assert currentPrimaryStage != null;
        return currentPrimaryStage;
    }

    @Override
    public void start(final Stage primaryStageInitial) {
        JMediaInspector.primaryStage = primaryStageInitial;
        JMediaInspector.primaryStage.setTitle(ApplicationConstants.TITLE);
        JMediaInspector.primaryStage.initStyle(StageStyle.UNDECORATED);
        JMediaInspector.primaryStage.initStyle(StageStyle.TRANSPARENT);
        initRootLayout();
        this.menuBar = this.rootLayout.lookup("#menuBarMainApp");

        EffectUtilities.makeDraggable(primaryStageInitial, this.menuBar);
        ResizeHelper.addResizeListener(primaryStageInitial);
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            final FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getClassLoader().getResource("fxml/MainWindows.fxml"));
            loader.setResources(new ResourceWrapper());
            this.rootLayout = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            final Scene scene = new Scene(this.rootLayout);
            scene.getStylesheets().add(getClass().getClassLoader().getResource(ApplicationConstants.CSS_FILE).toExternalForm());
            JMediaInspector.primaryStage.setScene(scene);
            JMediaInspector.primaryStage.show();
        } catch (final IOException e) {
            LOGGER.logp(Level.SEVERE, "JMediaInspector", "initRootLayout", e.getMessage(), e);
        }
    }

    // an empty resource bundle
    private static class ResourceWrapper extends ListResourceBundle {
        @Override
        protected Object[][] getContents() {
            return new Object[0][];
        }
    }

    /**
     * Handle the close menu item.
     *
     * @param event
     */
    public void handleClose(final ActionEvent event) {
        System.exit(0);
    }

    /**
     * Handle the "New Configuration" item menu.
     */
    @FXML
    public void handleNewConfiguration() {
        try {
            DialogsHelper.createConfigurationsDialog(getPrimaryStage());
        } catch (final IOException e) {
            LOGGER.logp(Level.SEVERE, "JMediaInspector", "handleNewConfiguration", e.getMessage(), e);
        }
    }
}
