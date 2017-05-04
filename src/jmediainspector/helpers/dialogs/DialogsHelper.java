package jmediainspector.helpers.dialogs;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.annotation.NonNull;

import javafx.concurrent.Service;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jmediainspector.constants.ApplicationConstants;
import jmediainspector.controllers.ConfigurationsDialogController;

/**
 * Dialogs helper.
 *
 * @author Cha
 */
public final class DialogsHelper {

    /**
     * Get process copy plex db dialog.
     *
     * @param service
     * @param owner
     * @return process copy plex db dialog
     */
    @NonNull
    public static Dialog<String> createProgressCopyDBPlexDialog(@NonNull final Service<File> service, @NonNull final Stage owner) {
        final Dialog<String> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        dialog.initOwner(owner);

        final DialogPane dialogPane = dialog.getDialogPane();
        final ProgressBar indicator = new ProgressBar();
        indicator.setMinWidth(200);
        // have the indicator display the progress of the service:
        indicator.progressProperty().bind(service.progressProperty());

        // Create content
        final GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(3);
        grid.setMaxWidth(Double.MAX_VALUE);

        // Do layout
        Text text = new Text("Importing Plex Database");
        text.setStyle("-fx-font-weight: bold; -fx-fill: #FFFFFF; -fx-underline: true;");
        grid.add(text, 0, 0);

        dialog.setTitle("Importing Plex Database");

        grid.add(indicator, 0, 1);
        text = new Text("Processing import, please wait.");
        text.setStyle("-fx-font-style: italic; -fx-fill: #FFFFFF; -fx-text-alignment: center;");

        indicator.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(indicator, Priority.ALWAYS);
        GridPane.setFillWidth(text, Boolean.TRUE);

        grid.add(text, 0, 2);
        GridPane.setHalignment(text, HPos.CENTER);
        dialogPane.setContent(grid);

        dialogPane.getStylesheets().add(DialogsHelper.class.getClassLoader().getResource(ApplicationConstants.CSS_FILE).toExternalForm());
        return dialog;
    }

    /**
     * Get process configurations dialog.
     *
     * @param owner stage owner
     * @throws IOException
     */
    public static void createConfigurationsDialog(@NonNull final Stage owner) throws IOException {
        final Stage manageConfigurationsStage = new Stage();
        // Load root layout from fxml file.
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogsHelper.class.getClassLoader().getResource("fxml/ConfigurationsDialog.fxml"));
        final AnchorPane rootLayout = (AnchorPane) loader.load();
        manageConfigurationsStage.setTitle(ApplicationConstants.TITLE);
        manageConfigurationsStage.initStyle(StageStyle.UNDECORATED);
        manageConfigurationsStage.initStyle(StageStyle.TRANSPARENT);
        manageConfigurationsStage.initModality(Modality.APPLICATION_MODAL);
        manageConfigurationsStage.initOwner(owner);

        final Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(DialogsHelper.class.getClassLoader().getResource(ApplicationConstants.CSS_FILE).toExternalForm());

        final ConfigurationsDialogController controller = (ConfigurationsDialogController) loader.getController();
        controller.setStage(owner);

        controller.finished().addListener((obs, wasPrompted, isNowFinished) -> {
            if (isNowFinished.booleanValue()) {
                manageConfigurationsStage.hide();
            }
        });

        manageConfigurationsStage.setScene(scene);
        manageConfigurationsStage.showAndWait();
    }

    /**
     * Get alert dialog.
     *
     * @param stage initial stage
     * @param alertType alert type
     * @param contentText dialog box text
     * @return Alert dialog
     */
    public static Alert getAlert(@NonNull final Stage stage, final Alert.AlertType alertType, final String contentText) {
        final Alert alert = new Alert(alertType);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(DialogsHelper.class.getClassLoader().getResource(ApplicationConstants.CSS_FILE).toExternalForm());
        alert.initOwner(stage);

        return alert;
    }
}
