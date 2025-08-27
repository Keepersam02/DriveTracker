package drive_tracker.drivetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.*;

public class MissingSavePopupController {

    private File dirFile;
    @FXML
    private Text titleText, messageText;

    @FXML
    private TextField fileNameTextField;

    @FXML
    private StackPane menuPane, createNewPane, findMissingPane;

    @FXML
    private Button findMissingButton, createNewButton, saveFileNameButton;

    @FXML
    public void initialize() {
        createNewPane.setVisible(false);
        createNewPane.setDisable(true);
        createNewPane.setMouseTransparent(true);
    }
    @FXML
    private void findMissingButtonClick(ActionEvent event) {
    }
}
