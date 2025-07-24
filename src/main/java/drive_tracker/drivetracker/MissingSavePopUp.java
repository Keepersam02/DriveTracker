package drive_tracker.drivetracker;

import com.google.gson.Gson;
import data_organization.CentralData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MissingSavePopUp {
    CentralData centralData;
    String filePath;
    public void HandleMissingSaveFile(Stage primaryStage) {
        Stage missingSavePopUp = new Stage();
        missingSavePopUp.initModality(Modality.APPLICATION_MODAL);
        missingSavePopUp.initOwner(primaryStage);

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
