package drive_tracker.drivetracker;

import atlantafx.base.theme.NordDark;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import data_handling.Config;
import data_organization.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.Styles;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MainPage extends Application {
    String saveFilePath;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DBManagement.setupDB();
        Config fileConfig = new Config();
        String saveFilePath = fileConfig.loadSaveFilePath();
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

        if (saveFilePath == null) {
            MissingSavePopUp missingSavePopUp = new MissingSavePopUp();
            missingSavePopUp.HandleMissingSaveFile(primaryStage);
        } else {
            try {
                FileReader reader = new FileReader(saveFilePath);

            } catch (FileNotFoundException f) {

                MissingSavePopUp missingSavePopUp = new MissingSavePopUp();
                missingSavePopUp.HandleMissingSaveFile(primaryStage);

            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(MainPage.class.getResource("main-page.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
            scene.getStylesheets().add(String.valueOf(getClass().getResource("/atlantafx/base/css/base.css")));
            scene.getStylesheets().add(String.valueOf(getClass().getResource("/atlantafx/base/css/nord-dark.css")));
            scene.getStylesheets().add(String.valueOf(getClass().getResource("dark-nord.css")));


            primaryStage.setTitle("Drive Tracker");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("IO exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
