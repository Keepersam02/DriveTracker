package data_organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class CentralData {
    ArrayList<ListItem> dataStorage;

    public CentralData() {
    }

    public CentralData(ArrayList<ListItem> dataStorage) {
        this.dataStorage = dataStorage;
    }
}
