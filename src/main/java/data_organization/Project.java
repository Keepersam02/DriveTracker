package data_organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Project implements ListItem {
    private String name;
    private long dateCreated;
    private long lastModified;
    private ArrayList<FileTree> drives;

    public Project() {
    }

    public Project(String name, long dateCreated, long lastModified, ArrayList<FileTree> drives) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
        this.drives = drives;
    }

    public Project(String name, long dateCreated, long lastModified) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
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

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public ArrayList<FileTree> getDrives() {
        return drives;
    }

    public void setDrives(ArrayList<FileTree> drives) {
        this.drives = drives;
    }

    public String interfaceGetName() {
        return this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(name, project.name) && Objects.equals(dateCreated, project.dateCreated) && Objects.equals(lastModified, project.lastModified) && Objects.equals(drives, project.drives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dateCreated, lastModified, drives);
    }
}
