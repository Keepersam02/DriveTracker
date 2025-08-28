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
import org.sqlite.SQLiteErrorCode;

import javax.swing.*;
import java.sql.Driver;
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
     }

    @FXML
    public void updateHomePane() {
        String url = "jdbc:sqlite:core.db";
        String qryListItemStmt = "SELECT name FROM listItems";
        String qryDriveStmt = "SELECT driveName FROM drives";
        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(qryListItemStmt);
             var rs = stmt.executeQuery(); var stmtDr = conn.prepareStatement(qryDriveStmt); var ds = stmtDr.executeQuery()) {
            itemListView.getItems().clear();
            driveListView.getItems().clear();
            while (rs.next()) {
                itemListView.getItems().add(rs.getString(1));
            }
            while (ds.next()) {
                driveListView.getItems().add(ds.getString(1));
            }
        } catch (SQLException s) {
            System.out.println("update Home");
            System.out.println(s.getMessage());
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

        String url = "jdbc:sqlite:core.db";
        String driveQryStr = "SELECT driveName FROM drives";

        try (var conn = DriverManager.getConnection(url); var driveStmt = conn.prepareStatement(driveQryStr)) {
            var driveRS = driveStmt.executeQuery();
            actionMenuCreateNewDriveList.getItems().clear();
            while (driveRS.next()) {
                actionMenuCreateNewDriveList.getItems().add(driveRS.getString(1));
            }
        } catch (SQLException s) {

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
        updateHomePane();
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
    private TextField actionMenuCreateNewDriveNameTextField;

    @FXML
    private TextArea actionMenuCreateNewDriveDescriptionTextArea;

    @FXML
    private ListView<String> actionMenuCreateNewDriveClientsListView, actionMenuCreateNewDriveProjectsListView;

    @FXML
    private Button actionMenuCreateNewDriveConfirmAndScan, actionMenuCreateNewDriveConfirm, actionMenuCreateNewDriveCancel;

    @FXML
    private Text actionMenuCreateDriveWarningText;

    @FXML
    private void switchToCreateNewDrive(ActionEvent event) {
        actionMenuCreateNewDriveVBox.setVisible(true);
        actionMenuCreateNewDriveVBox.setMouseTransparent(false);

        actionMenuCreateNewDriveClientsListView.getItems().clear();
        actionMenuCreateNewDriveProjectsListView.getItems().clear();

        String url = "jdbc:sqlite:core.db";
        String getClientsStr = "SELECT name FROM listItems WHERE isClient = 1";
        String getProjectsStr = "SELECT name FROM listItems WHERE isClient = 0";
        try (var conn = DriverManager.getConnection(url); var getClientsStmt = conn.prepareStatement(getClientsStr); var getProjectsStmt = conn.prepareStatement(getProjectsStr)) {
            var clientRS = getClientsStmt.executeQuery();
            var projectsRS = getProjectsStmt.executeQuery();

            while (clientRS.next()) {
                actionMenuCreateNewDriveClientsListView.getItems().add(clientRS.getString(1));
            }
            while (projectsRS.next()) {
                actionMenuCreateNewDriveProjectsListView.getItems().add(projectsRS.getString(1));
            }
        } catch (SQLException s) {

        }
    }

    @FXML
    private void switchAwayFromNewDrive(ActionEvent event) {
        actionMenuCreateNewDriveVBox.setVisible(false);
        actionMenuCreateNewDriveVBox.setMouseTransparent(true);

        actionMenuCreateNewDriveNameTextField.setText("");
        actionMenuCreateNewDriveDescriptionTextArea.setText("");
    }

    @FXML
    private void onAMCreateDriveConfirmAndScanClick(ActionEvent event) {

    }

    @FXML
    private void onAMCreateDriveConfirmClick(ActionEvent event) {
        if (actionMenuCreateNewDriveNameTextField.getText().isEmpty()) {
            actionMenuCreateDriveWarningText.setText("Please enter a name");
            return;
        }

        String url = "jdbc:sqlite:core.db";
        String insertStr = "INSERT INTO drives(driveName, description, dateCreated, dateLastModified) VALUES(?,?,?,?)";
        try (var conn = DriverManager.getConnection(url); var insStmt = conn.prepareStatement(insertStr);) {
            insStmt.setString(1, actionMenuCreateNewDriveNameTextField.getText());
            insStmt.setString(2, actionMenuCreateNewDriveDescriptionTextArea.getText());
            insStmt.setLong(3, System.currentTimeMillis());
            insStmt.setLong(4, System.currentTimeMillis());
            insStmt.executeUpdate();
        } catch (SQLException s) {
            if (s.getErrorCode() == 19) {
                actionMenuCreateDriveWarningText.setText("A drive by the same name already exists.  PLease use unique names.");
                return;
            }
            actionMenuCreateDriveWarningText.setText("Failed to add drive to database.");
            return;
        }

        String insertMapStr = "INSERT INTO driveMap(clientID, driveID, dateAssociated) VALUES(?,?,?)";
        String getDriveID = "SELECT driveID FROM drives WHERE driveName = ?";
        String getClientID = "SELECT clientID FROM listItems WHERE name = ?";
        try (var conn = DriverManager.getConnection(url); var insertMapStmt = conn.prepareStatement(insertMapStr);
             var getDriveIDStmt = conn.prepareStatement(getDriveID); var getClientIDSt = conn.prepareStatement(getClientID)) {
            getDriveIDStmt.setString(1, actionMenuCreateNewDriveNameTextField.getText());
            var rs = getDriveIDStmt.executeQuery();
            int driveID = rs.getInt(1);

            for (String name : actionMenuCreateNewDriveClientsListView.getSelectionModel().getSelectedItems()) {
                getClientIDSt.setString(1, name);
                var clientRS = getClientIDSt.executeQuery();
                insertMapStmt.setInt(1, clientRS.getInt(1));
                insertMapStmt.setInt(2, driveID);
                insertMapStmt.setLong(3, System.currentTimeMillis());
            }
            for (String name : actionMenuCreateNewDriveProjectsListView.getSelectionModel().getSelectedItems()) {
                getClientIDSt.setString(1, name);
                var projRS = getClientIDSt.executeQuery();
                insertMapStmt.setInt(1, projRS.getInt(1));
                insertMapStmt.setInt(2, driveID);
                insertMapStmt.setLong(3, System.currentTimeMillis());
            }
        } catch (SQLException s) {

        }

    }

    @FXML
    private void onAMCreateDriveCancelClick(ActionEvent event) {
        switchAwayFromNewDrive(event);
    }
}
