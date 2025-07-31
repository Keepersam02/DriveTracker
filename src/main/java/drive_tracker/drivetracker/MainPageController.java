package drive_tracker.drivetracker;

import data_organization.CentralData;
import data_organization.Drive;
import data_organization.ListItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MainPageController {
    @FXML
    private StackPane clientsAndProjectsPane, driveListPane;

    @FXML
    private Button homePaneCreateNewButton, defaultViewOpenSelectedButton;

    @FXML
    private ListView<String> itemListView;

    @FXML
    public void initialize() {
        actionMenuCreateNewPane.setVisible(false);
        actionMenuCreateNewPane.setMouseTransparent(true);
        actionMenuCreateNewPane.setOpacity(0);

        ArrayList<ListItem> itemList = CentralData.getInstance().getDataStorage();

        for (ListItem item : itemList) {
            itemListView.getItems().add(item.interfaceGetName());
        }

    }



    @FXML
    private StackPane actionMenuPane, actionMenuCreateNewPane;

    @FXML
    private Button confirmNewListItemButton;

    @FXML
    private MenuButton actionMenuCreateNewMenuButton;

    @FXML
    private MenuItem actionMenuCreateNewMenuItemClient, actionMenuCreateNewMenuItemProject;

    @FXML
    private TextField actionMenuCreateNewNameField;

    @FXML
    private ListView<String> actionMenuCreateNewDriveList, actionMenuCreateNewProjectList;

    @FXML
    private Text actionMenuCreateNewMessageText;

    @FXML
    private void homePaneCreateNewButtonClick(ActionEvent event) {
        actionMenuCreateNewPane.setVisible(true);
        actionMenuCreateNewPane.setMouseTransparent(false);
        actionMenuCreateNewPane.setOpacity(1);

        CentralData data = CentralData.getInstance();
        for (ListItem item : data.getDataStorage()) {
            actionMenuCreateNewProjectList.getItems().add(item.interfaceGetName());
        }
        for (Drive drive : data.getDriveList()) {
            actionMenuCreateNewDriveList.getItems().add(drive.getDriveName());
        }

        actionMenuCreateNewDriveList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        actionMenuCreateNewProjectList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void confirmNewListItemButtonClick() {
        String clientName = actionMenuCreateNewNameField.getText();
        if (clientName.isEmpty()) {
            actionMenuCreateNewMessageText.setText("Please enter a name for the client.");
            return;
        }
        MainPage mainPage = new MainPage();
        mainPage.addNewClient(clientName);
    }
}
