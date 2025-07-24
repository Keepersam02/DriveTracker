package drive_tracker.drivetracker;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import data_organization.CentralData;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MissingSavePopupController {

    @FXML
    private Text titleText, messageText;

    @FXML
    private StackPane menuPane, createNewPane, findMissingPane;

    @FXML
    private Button findMissingButton, createNewButton;

    @FXML
    public void initialize() {
        createNewPane.setVisible(false);
        createNewPane.setDisable(true);
        createNewPane.setMouseTransparent(true);
    }
    @FXML
    private void findMissingButtonClick(ActionEvent event) {

        MissingSavePopUp missingSavePopUp = new MissingSavePopUp();
        Stage stage = (Stage) menuPane.getScene().getWindow();
        stage.setAlwaysOnTop(true);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Find Missing File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));


        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                FileReader reader = new FileReader(selectedFile);
                Gson gson = new Gson();
                CentralData.setInstance(gson.fromJson(reader, CentralData.class));
            } catch (FileNotFoundException f) {
                ErrLogger.error("Could not find file specified by FileChooser", f);
                //Add more here to change text on menuPane
                messageText.setText("Could not find file.");
            } catch (JsonSyntaxException s) {
                messageText.setText("Improperly formatted JSON file.");
            } catch (JsonIOException f) {
                messageText.setText("Non JSON file provided.");
            }
        } else {
            messageText.setText("Please provide a file to continue.");
        }

        System.out.println(CentralData.getInstance().getDateCreated());
    }

    @FXML
    private void createNewButtonClick(ActionEvent event) {
        MissingSavePopUp missingSavePopUp = new MissingSavePopUp();

        Stage stage = (Stage) menuPane.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Pick parent directory.");

    }
}
