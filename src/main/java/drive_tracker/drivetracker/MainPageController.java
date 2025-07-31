package drive_tracker.drivetracker;

import data_organization.CentralData;
import data_organization.ListItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

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
    private void homePaneCreateNewButtonClick(ActionEvent event) {
        actionMenuCreateNewPane.setVisible(true);
        actionMenuCreateNewPane.setMouseTransparent(false);
        actionMenuCreateNewPane.setOpacity(1);

    }
}
