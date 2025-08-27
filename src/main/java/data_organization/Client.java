package data_organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Client {
    private int listItemID;
    private String name, description;
    private long dateCreated, dateLastModified;
    private ArrayList<Project> projects;
    private ArrayList<Drive> drives;

    public Client(int listItemID, String name, String description, long dateCreated, long dateLastModified, ArrayList<Project> projects, ArrayList<Drive> drives) {
        this.listItemID = listItemID;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        this.projects = projects;
        this.drives = drives;
    }

    public Client(int listItemID, String name, String description, long dateCreated, long dateLastModified) {
        this.listItemID = listItemID;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    public Client() {
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Client client = (Client) object;
        return listItemID == client.listItemID && dateCreated == client.dateCreated && dateLastModified == client.dateLastModified && Objects.equals(name, client.name) && Objects.equals(description, client.description) && Objects.equals(projects, client.projects) && Objects.equals(drives, client.drives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listItemID, name, description, dateCreated, dateLastModified, projects, drives);
    }

    public int getListItemID() {
        return listItemID;
    }

    public void setListItemID(int listItemID) {
        this.listItemID = listItemID;
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

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public ArrayList<Drive> getDrives() {
        return drives;
    }

    public void setDrives(ArrayList<Drive> drives) {
        this.drives = drives;
    }
}
