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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainPageController {
    @FXML
    private StackPane clientsAndProjectsPane, driveListPane;

    @FXML
    private Button homePaneCreateNewButton, defaultViewOpenSelectedButton, driveCreateNewButton;

    @FXML
    private ListView<String> itemListView, driveListView;

    @FXML
    public void initialize() {
        createNewListItemStackPane.setVisible(false);
        createNewListItemStackPane.setMouseTransparent(true);
        actionMenuCreateNewDriveVBox.setVisible(false);
        actionMenuCreateNewDriveVBox.setMouseTransparent(true);

        updateHomePane();

        String url = "jdbc:sqlite:core.db";
        var listItemQueryStmt = "SELECT name FROM listItems;";
        var driveQrStmt = "SELECT driveName FROM drives;";

        try (var conn = DriverManager.getConnection(url); var stmt = conn.createStatement(); var rs = stmt.executeQuery(listItemQueryStmt); var ns = stmt.executeQuery(driveQrStmt)) {
            while (rs.next()) {
                itemListView.getItems().add(rs.getString("name"));
            }
            while (ns.next()) {
                driveListView.getItems().add("name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    public void updateHomePane() {
        String url = "jdbc:sqlite:core.db";
        String qryListItemStmt = "SELECT name FROM listItems";
        String qryDriveStmt = "SELECT driveName FROM drives";
        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(qryListItemStmt);
             var rs = stmt.executeQuery(); var stmtDr = conn.prepareStatement(qryDriveStmt); var ds = stmtDr.executeQuery()) {
            itemListView.getItems().clear();
            itemListView.getItems().clear();
            while (rs.next()) {
                itemListView.getItems().add(rs.getString(1));
            }
            while (ds.next()) {
                driveListView.getItems().add(ds.getString(1));
            }
        } catch (SQLException s) {

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
    private void onDriveCreateNewButton(ActionEvent event) {
        clientsAndProjectsPane.setDisable(true);
        clientsAndProjectsPane.setOpacity(.5);
        driveListPane.setDisable(true);
        driveListPane.setOpacity(.5);

        switchToCreateNewDrive(event);
    }

    @FXML
    private void setActionMenuCreateNewMenuItemClientClick(ActionEvent event) {
        actionMenuCreateNewMenuButton.setText("Client");
        actionMenuCreateNewProjectList.setDisable(false);
        actionMenuCreateNewProjectList.getItems().removeAll();

        actionMenuCreateNewProjectListText.setText("Contains Projects");
        String url = "jdbc:sqlite:core.db";
        String queryStmt = "SELECT name FROM listItems WHERE isClient = 0";

        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(queryStmt)) {
            var rs = stmt.executeQuery();
            actionMenuCreateNewProjectList.getItems().clear();
            while (rs.next()) {
                actionMenuCreateNewProjectList.getItems().add(rs.getString("name"));
            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }

    }

    @FXML
    private void onActionMenuCreateNewMenuItemProjectClick(ActionEvent event) {
        actionMenuCreateNewMenuButton.setText("Project");
        actionMenuCreateNewProjectList.setDisable(false);
        actionMenuCreateNewProjectList.getItems().removeAll();

        actionMenuCreateNewProjectListText.setText("Member of");
        String url = "jdbc:sqlite:core.db";
        String queryStr = "SELECT name FROM listItems WHERE isClient = 1";

        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(queryStr)) {
            var rs = stmt.executeQuery();
            actionMenuCreateNewProjectList.getItems().clear();
            while (rs.next()) {
               actionMenuCreateNewProjectList.getItems().add(rs.getString("name"));
            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

    @FXML
    private Button cancelNewListItemButton;

    @FXML
    private void cancelNewListItemButtonClick() {
        actionMenuCreateNewProjectList.getItems().removeAll();
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

        ObservableList<String> selectedDrives = actionMenuCreateNewDriveList.getSelectionModel().getSelectedItems();

        ObservableList<String> selectedProjects = actionMenuCreateNewProjectList.getSelectionModel().getSelectedItems();

        if (actionMenuCreateNewMenuButton.getText().equals("Client")) {
            try {
                DBManagement.insertListItem(clientName, null, 1, null);
                DBManagement.updateClientDriveMap(clientName, selectedDrives);
                DBManagement.updateClientIDs(clientName, selectedProjects);
            } catch (SQLException e) {
                actionMenuCreateNewMessageText.setText("Failed to add Client to database");
                return;
            }
        } else if (actionMenuCreateNewMenuButton.getText().equals("Project")) {
            try {
                if (!selectedProjects.isEmpty()) {
                    DBManagement.insertListItem(clientName, null, 0, selectedProjects.getFirst());
                } else {
                    DBManagement.insertListItem(clientName, null, 0, null);
                }
                DBManagement.updateClientDriveMap(clientName, selectedDrives);
            } catch (SQLException e) {
                actionMenuCreateNewMessageText.setText("Failed to add Project to database");
                return;
            }
        }

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
    private VBox actionMenuCreateNewDriveVBox;


    @FXML
    private TextField actionMenuCreateNewDriveNameTextArea;

    @FXML
    private TextArea actionMenuCreateNewDriveDescriptionTextField;

    @FXML
    private ListView<String> actionMenuCreateNewDriveClientsListView, actionMenuCreateNewDriveProjectsListView;

    @FXML
    private Button actionMenuCreateNewDriveConfirmAndScan, actionMenuCreateNewDriveConfirm, actionMenuCreateNewDriveCancel;

    @FXML
    private void switchToCreateNewDrive(ActionEvent event) {
        actionMenuCreateNewDriveVBox.setDisable(false);
        actionMenuCreateNewDriveVBox.setMouseTransparent(false);
        actionMenuCreateNewDriveVBox.setVisible(true);
        actionMenuCreateNewDriveClientsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        actionMenuCreateNewDriveProjectsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        String url = "jdbc:sqlite:core.db";
        String clientQryStmt = "SELECT name FROM listItems WHERE isClient = 1";
        String projQryStmt = "SELECT name FROM listItems WHERE isClient = 0";

        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(clientQryStmt); var projStmt = conn.prepareStatement(projQryStmt)) {
            var rs = stmt.executeQuery();
            actionMenuCreateNewDriveClientsListView.getItems().clear();
            while (rs.next()) {
                actionMenuCreateNewDriveClientsListView.getItems().add(rs.getString(1));
            }
            rs = projStmt.executeQuery();
            actionMenuCreateNewDriveProjectsListView.getItems().clear();
            while (rs.next()) {
                actionMenuCreateNewDriveProjectsListView.getItems().add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void switchAwayFromNewDrive(ActionEvent event) {
        actionMenuCreateNewDriveVBox.setVisible(false);
        actionMenuCreateNewDriveVBox.setDisable(true);
        actionMenuCreateNewDriveVBox.setMouseTransparent(true);

        clientsAndProjectsPane.setDisable(false);
        clientsAndProjectsPane.setOpacity(1);
        driveListPane.setDisable(false);
        driveListPane.setOpacity(1);
    }

    @FXML
    private void onAMCreateDriveConfirmAndScanClick(ActionEvent event) {

    }

    @FXML
    private void onAMCreateDriveConfirmClick(ActionEvent event) {

    }

    @FXML
    private void onAMCreateDriveCancelClick(ActionEvent event) {
        actionMenuCreateNewDriveDescriptionTextField.setText("");
        actionMenuCreateNewDriveNameTextArea.setText("");

        switchAwayFromNewDrive(event);
    }
}
