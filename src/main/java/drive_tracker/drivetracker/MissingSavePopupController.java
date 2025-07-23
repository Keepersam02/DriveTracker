package drive_tracker.drivetracker;

import com.google.gson.Gson;
import data_organization.CentralData;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MissingSavePopupController {
    CentralData centralData;

    public void init(CentralData centData) {
        centralData = centData;
    }
    @FXML
    private StackPane menuPane, createNewPane, findMissingPane;

    @FXML
    private Button findMissingButton, createNewButton;

    @FXML
    private void findMissingButtonClick(ActionEvent event) {
        MissingSavePopUp missingSavePopUp = new MissingSavePopUp();
        Stage stage = (Stage) menuPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Find Missing File");
        File selectedFile = fileChooser.showOpenDialog(stage);

        FileReader reader;
        try {
            reader = new FileReader(selectedFile);
        } catch (FileNotFoundException f) {
            ErrLogger.error("Could not find file specified by FileChooser", f);
            //Add more here to change text on menuPane
            return;
        }
        Gson gson = new Gson();
        centralData = gson.fromJson(reader, CentralData.class);

        System.out.println("Called");
    }

    @FXML
    private void createNewButtonClick(ActionEvent event) {
        MissingSavePopUp missingSavePopUp = new MissingSavePopUp();
    }
}
