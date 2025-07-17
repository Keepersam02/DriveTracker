package drive_tracker.drivetracker;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MissingSavePopUp {
    public String HandleMissingSaveFile(Stage primaryStage) {
        Stage missingSavePopUp = new Stage();
        missingSavePopUp.initModality(Modality.APPLICATION_MODAL);
        missingSavePopUp.initOwner(primaryStage);

        FXMLLoader fxmlLoader = new FXMLLoader(MissingSavePopUp.class.getResource("missing-save-popup.fxml"));
        try {
            Scene baseScene = new Scene(fxmlLoader.load(), 400, 600);
            baseScene.getStylesheets().add(String.valueOf(getClass().getResource("/atlantafx/base/css/base.css")));
            baseScene.getStylesheets().add(String.valueOf(getClass().getResource("/atlantafx/base/css/nord-dark.css")));
            baseScene.getStylesheets().add(String.valueOf(getClass().getResource("dark-nord.css")));

            missingSavePopUp.setTitle("Missing Save");
            missingSavePopUp.setScene(baseScene);
            missingSavePopUp.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public String FindMissingSave(Stage primaryStage) {
        return null;
    }

    public String CreateNewSave(Stage primaryStage) {
        return null;
    }
}
