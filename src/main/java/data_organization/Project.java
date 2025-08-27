package data_organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Project {
    private int listItemID, isClient, clientID;
    private String name, description;
    private long dateCreated, dateLastModified;
    private ArrayList<Drive> drives;

    public Project(int listItemID, int isClient, int clientID, String name, String description, long dateCreated, long dateLastModified, ArrayList<Drive> drives) {
        this.listItemID = listItemID;
        this.isClient = isClient;
        this.clientID = clientID;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        this.drives = drives;
    }

    public Project(int listItemID, int isClient, int clientID, String name, String description, long dateCreated) {
        this.listItemID = listItemID;
        this.isClient = isClient;
        this.clientID = clientID;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public Project() {
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Project project = (Project) object;
        return listItemID == project.listItemID && isClient == project.isClient && clientID == project.clientID && dateCreated == project.dateCreated && dateLastModified == project.dateLastModified && Objects.equals(name, project.name) && Objects.equals(description, project.description) && Objects.equals(drives, project.drives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listItemID, isClient, clientID, name, description, dateCreated, dateLastModified, drives);
    }

    public int getListItemID() {
        return listItemID;
    }

    public void setListItemID(int listItemID) {
        this.listItemID = listItemID;
    }

    public int getIsClient() {
        return isClient;
    }

    public void setIsClient(int isClient) {
        this.isClient = isClient;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ArrayList<Drive> getDrives() {
        return drives;
    }

    public void setDrives(ArrayList<Drive> drives) {
        this.drives = drives;
    }
}
