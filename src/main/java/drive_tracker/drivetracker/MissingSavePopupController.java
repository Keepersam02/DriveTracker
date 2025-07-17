package drive_tracker.drivetracker;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MissingSavePopupController {
    @FXML
    private StackPane menuPane, createNewPane, findMissingPane;

    @FXML
    private Button findMissingButton, createNewButton;

    @FXML
    private void init() {
        createNewPane.setOpacity(0);
        createNewPane.setVisible(false);
        findMissingPane.setOpacity(0);
        findMissingPane.setVisible(false);

        findMissingButton.setOnAction(this::handleTransition);
        createNewButton.setOnAction(this::handleTransition);
    }

    private void handleTransition(ActionEvent event) {
        if (event.getSource() == findMissingButton) {
            playFadeTransition(findMissingPane, menuPane);

        } else if (event.getSource() == createNewButton) {
            playFadeTransition(createNewPane, menuPane);
        }
    }

    private void playFadeTransition(StackPane to, StackPane from) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis((double) 500 / 2), from);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis((double) 500 / 2), to);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        SequentialTransition transition = new SequentialTransition(fadeOut, fadeIn);
        transition.play();
    }

    @FXML
    private void findMissingButtonClick(ActionEvent event) {

    }
}
