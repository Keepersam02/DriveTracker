package data_organization;

import com.google.gson.*;
import drive_tracker.drivetracker.ErrLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class CentralData {
    private static CentralData instance;

    private ArrayList<ListItem> dataStorage;
    private ArrayList<Drive> driveList;
    private long dateCreated;
    private long dateLastModified;

    private CentralData() {
        this.dateCreated = 0;
        this.dateLastModified = 0;
        this.dataStorage = new ArrayList<>();
        this.driveList = new ArrayList<>();
    }

    public static CentralData getInstance() {
        if (instance == null) {
            instance = new CentralData();
        }
        return instance;
    }

    public static void setInstance(CentralData newInstance) {
        instance = newInstance;
    }

    public CentralData(ArrayList<ListItem> dataStorage, long dateCreated, long dateLastModified) {
        this.dataStorage = dataStorage;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    public ArrayList<Drive> getDriveList() {
        return driveList;
    }

    public void setDriveList(ArrayList<Drive> driveList) {
        this.driveList = driveList;
    }

    public CentralData(ArrayList<ListItem> dataStorage) {
        this.dataStorage = dataStorage;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(long dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public ArrayList<ListItem> getDataStorage() {
        return dataStorage;
    }

    public void setDataStorage(ArrayList<ListItem> dataStorage) {
        this.dataStorage = dataStorage;
    }
}
