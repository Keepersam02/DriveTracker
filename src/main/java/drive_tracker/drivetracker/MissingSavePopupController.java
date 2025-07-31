package drive_tracker.drivetracker;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import data_handling.Config;
import data_organization.CentralData;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

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

        System.out.println(CentralData.getInstance().toString());
        stage.close();
    }

    @FXML
    private void createNewButtonClick(ActionEvent event) {
        MissingSavePopUp missingSavePopUp = new MissingSavePopUp();

        Stage stage = (Stage) menuPane.getScene().getWindow();
        stage.setAlwaysOnTop(true);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Pick parent directory.");
        dirFile = directoryChooser.showDialog(stage);

        if (dirFile != null) {
            menuPane.setVisible(false);
            menuPane.setDisable(true);
            menuPane.setMouseTransparent(true);

            createNewPane.setDisable(false);
            createNewPane.setVisible(true);
            createNewPane.setMouseTransparent(false);
        }

    }

    @FXML
    private void saveFileNameButtonClick(ActionEvent event) {
        String fileName = fileNameTextField.getText();
        StringBuilder builder = new StringBuilder(fileName);
        builder.append(".json");

        try {
            Path path = Path.of(dirFile.getAbsolutePath());
            String outPath = String.valueOf(path.resolve(builder.toString()));

            FileOutputStream fileOutputStream = new FileOutputStream(outPath);
            CentralData centralData = CentralData.getInstance();
            Gson gson = new Gson();
            String centDataStr = gson.toJson(centralData, CentralData.class);
            fileOutputStream.write(centDataStr.getBytes(StandardCharsets.UTF_8));

            Config config = new Config(outPath, "0.0.1");
            config.setSaveFilePath(outPath);
            config.writeSaveFilePath(outPath);
        } catch (SecurityException | InvalidPathException s) {
            messageText.setText("Invalid file path.");
            createNewPane.setDisable(true);
            createNewPane.setMouseTransparent(true);
            createNewPane.setOpacity(0.0);

            menuPane.setDisable(false);
            menuPane.setOpacity(1.0);
            menuPane.setMouseTransparent(false);
        } catch (IOException e) {
            messageText.setText("Failed to write file.");
            messageText.setText("Invalid file path.");
            createNewPane.setDisable(true);
            createNewPane.setMouseTransparent(true);
            createNewPane.setOpacity(0.0);

            menuPane.setDisable(false);
            menuPane.setOpacity(1.0);
            menuPane.setMouseTransparent(false);
        }
        Stage stage = (Stage)menuPane.getScene().getWindow();
        stage.close();
    }
}
