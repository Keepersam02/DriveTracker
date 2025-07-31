package data_organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Client implements ListItem {
    private String name;
    private long dateCreated;
    private long dateLastModified;
    private ArrayList<Project> projects;
    private ArrayList<Drive> driveList;

    public Client() {
    }

    public Client(String name, long dateCreated, long dateLastModified) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    public Client(String name, long dateCreated, long dateLastModified, ArrayList<Project> projects, ArrayList<Drive> driveList) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        this.projects = projects;
        this.driveList = driveList;
    }

    public ArrayList<Drive> getDriveList() {
        return driveList;
    }

    public void setDriveList(ArrayList<Drive> driveList) {
        this.driveList = driveList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String interfaceGetName() {
        return this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(name, client.name) && Objects.equals(dateCreated, client.dateCreated) && Objects.equals(dateLastModified, client.dateLastModified) && Objects.equals(projects, client.projects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateCreated, dateLastModified, projects);
    }
}
