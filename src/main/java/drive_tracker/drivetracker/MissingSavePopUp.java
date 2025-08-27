package drive_tracker.drivetracker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MissingSavePopUp {
    String filePath;
    public void HandleMissingSaveFile(Stage primaryStage) {
        Stage missingSavePopUp = new Stage();
        missingSavePopUp.initModality(Modality.APPLICATION_MODAL);
        missingSavePopUp.initOwner(primaryStage);
        missingSavePopUp.setAlwaysOnTop(true);

        FXMLLoader fxmlLoader = new FXMLLoader(MissingSavePopUp.class.getResource("missing-save-popup.fxml"));
        try {
            Scene baseScene = new Scene(fxmlLoader.load(), 600, 400);
            baseScene.getStylesheets().add(String.valueOf(getClass().getResource("/atlantafx/base/css/base.css")));
            baseScene.getStylesheets().add(String.valueOf(getClass().getResource("/atlantafx/base/css/nord-dark.css")));
            baseScene.getStylesheets().add(String.valueOf(getClass().getResource("dark-nord.css")));

            missingSavePopUp.setTitle("Missing Save");
            missingSavePopUp.setScene(baseScene);
            missingSavePopUp.show();
        } catch (IOException e) {
            ErrLogger.error("Failed to load missing save screen", e);

        }

        MissingSavePopupController missingSavePopupController = new MissingSavePopupController();



    }

}
