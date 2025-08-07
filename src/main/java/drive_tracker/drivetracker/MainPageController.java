package drive_tracker.drivetracker;

import data_organization.*;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainPageController {
    @FXML
    private StackPane clientsAndProjectsPane, driveListPane;

    @FXML
    private Button homePaneCreateNewButton, defaultViewOpenSelectedButton;

    @FXML
    private ListView<String> itemListView, driveListView;

    @FXML
    public void initialize() {
        createNewListItemStackPane.setVisible(false);
        createNewListItemStackPane.setMouseTransparent(true);

        ArrayList<ListItem> itemList = CentralData.getInstance().getDataStorage();

        for (ListItem item : itemList) {
            itemListView.getItems().add(item.interfaceGetName());
        }

        for (Drive drive : CentralData.getInstance().getDriveList()) {
            driveListView.getItems().add(drive.getDriveName());
        }

    }

    @FXML
    public void updateHomePane(Object object) {
        if (object.getClass() == Project.class || object.getClass() == Client.class) {
            ListItem item = (ListItem) object;
            itemListView.getItems().add(item.interfaceGetName());
        } else if (object.getClass() == Drive.class) {
            Drive drive = (Drive) object;
            driveListView.getItems().add(drive.getDriveName());
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
    private Text actionMenuCreateNewMessageText, actionMenuCreateNewProjectListText;

    @FXML
    private StackPane createNewListItemStackPane;

    @FXML
    private void homePaneCreateNewButtonClick(ActionEvent event) {
        createNewListItemStackPane.setVisible(true);
        createNewListItemStackPane.setMouseTransparent(false);

        CentralData data = CentralData.getInstance();
        for (ListItem item : data.getDataStorage()) {
            actionMenuCreateNewProjectList.getItems().add(item.interfaceGetName());
        }
        for (Drive drive : data.getDriveList()) {
            actionMenuCreateNewDriveList.getItems().add(drive.getDriveName());
        }

        actionMenuCreateNewDriveList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        actionMenuCreateNewProjectList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        clientsAndProjectsPane.setDisable(true);
        clientsAndProjectsPane.setOpacity(.5);
        driveListPane.setOpacity(.5);
        driveListPane.setDisable(true);
    }

    @FXML
    private void setActionMenuCreateNewMenuItemClientClick(ActionEvent event) {
        actionMenuCreateNewMenuButton.setText("Client");
        actionMenuCreateNewProjectList.setDisable(false);
        actionMenuCreateNewProjectList.setOpacity(1);
        actionMenuCreateNewProjectListText.setOpacity(1);
    }

    @FXML
    private void onActionMenuCreateNewMenuItemProjectClick(ActionEvent event) {
        actionMenuCreateNewMenuButton.setText("Project");
        actionMenuCreateNewProjectList.setDisable(true);
        actionMenuCreateNewProjectList.setOpacity(.5);
        actionMenuCreateNewProjectListText.setOpacity(.5);
    }

    @FXML
    private Button cancelNewListItemButton;

    @FXML
    private void cancelNewListItemButtonClick() {
        homePaneCreateNewButton.setOpacity(1);
        homePaneCreateNewButton.setDisable(false);
        actionMenuCreateNewNameField.setText("");
        actionMenuCreateNewMenuButton.setText("Select");
        createNewListItemStackPane.setVisible(false);
        createNewListItemStackPane.setMouseTransparent(true);
        clientsAndProjectsPane.setDisable(false);
        clientsAndProjectsPane.setOpacity(1);
        driveListPane.setOpacity(1);
        driveListPane.setDisable(false);
    }

    @FXML
    private void confirmNewListItemButtonClick() {
        String clientName = actionMenuCreateNewNameField.getText();
        if (clientName.isEmpty()) {
            actionMenuCreateNewMessageText.setText("Please enter a name for the client.");
            return;
        }
        CentralData centDat = CentralData.getInstance();
        ArrayList<Drive> newClientDriveList = new ArrayList<>();
        ObservableList<String> selectedDrives = actionMenuCreateNewDriveList.getSelectionModel().getSelectedItems();
        for (String item : selectedDrives) {
            for (Drive drive : centDat.getDriveList()) {
                if (drive.getDriveName().equals(item)) {
                    newClientDriveList.add(drive);
                }
            }
        }

        ArrayList<Project> newClientProjectList = new ArrayList<>();
        ObservableList<String> selectedProjects = actionMenuCreateNewProjectList.getSelectionModel().getSelectedItems();
        for (String item : selectedProjects) {
            for (ListItem project : centDat.getDataStorage()) {
                if (project.getClass() == Client.class) {
                    continue;
                }
                newClientProjectList.add((Project) project);
            }
        }

        MainPage mainPage = new MainPage();
        if (actionMenuCreateNewMenuButton.getText().equals("Client")) {
            Client newClient = new Client(clientName, System.currentTimeMillis(), System.currentTimeMillis(), newClientProjectList, newClientDriveList);
            try {
                mainPage.addNewClient(newClient);
            } catch (IllegalArgumentException e) {
                actionMenuCreateNewMessageText.setText("Client by the same name already exists.  Please enter a unique name.");
                return;
            }
            updateHomePane(newClient);
        } else if (actionMenuCreateNewMenuButton.getText().equals("Project")) {
            Project newProject = new Project(clientName, System.currentTimeMillis(), System.currentTimeMillis(), newClientDriveList);
            try {
                mainPage.addNewProject(newProject);
            } catch (IllegalArgumentException e) {
                actionMenuCreateNewMessageText.setText("Project by the same name already exists. Please enter a unique name.");
                return;
            }
            updateHomePane(newProject);
        }
        System.out.println(CentralData.getInstance().getDataStorage().getFirst().interfaceGetName());
        actionMenuCreateNewNameField.setText("");
        actionMenuCreateNewMenuButton.setText("Select");
        createNewListItemStackPane.setVisible(false);
        createNewListItemStackPane.setMouseTransparent(true);
        clientsAndProjectsPane.setDisable(false);
        clientsAndProjectsPane.setOpacity(1);
        driveListPane.setOpacity(1);
        driveListPane.setDisable(false);
    }
}
