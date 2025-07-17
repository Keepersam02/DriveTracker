package data_organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Client implements ListItem {
    private String name;
    private LocalDateTime dateCreated;
    private LocalDateTime dateLastModified;
    private ArrayList<Project> projects;

    public Client() {
    }

    public Client(String name, LocalDateTime dateCreated, LocalDateTime dateLastModified) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    public Client(String name, LocalDateTime dateCreated, LocalDateTime dateLastModified, ArrayList<Project> projects) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(LocalDateTime dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
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
