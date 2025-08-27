package data_organization;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Objects;

public class Drive {
    private int driveID;
    private String name, description;
    private long dateCreated, dateLastModified;
    private ArrayList<Scan> scans;

    public Drive(int driveID, String name, String description, long dateCreated, long dateLastModified, ArrayList<Scan> scans) {
        this.driveID = driveID;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        this.scans = scans;
    }

    public Drive(int driveID, String name, String description, long dateCreated, long dateLastModified) {
        this.driveID = driveID;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    public Drive() {
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Drive drive = (Drive) object;
        return driveID == drive.driveID && dateCreated == drive.dateCreated && dateLastModified == drive.dateLastModified && Objects.equals(name, drive.name) && Objects.equals(description, drive.description) && Objects.equals(scans, drive.scans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driveID, name, description, dateCreated, dateLastModified, scans);
    }

    public int getDriveID() {
        return driveID;
    }

    public void setDriveID(int driveID) {
        this.driveID = driveID;
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

    public ArrayList<Scan> getScans() {
        return scans;
    }

    public void setScans(ArrayList<Scan> scans) {
        this.scans = scans;
    }
}
