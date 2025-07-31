package data_organization;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Drive {
    private String driveName;
    private long dateCreated, lastDateScanned;
    private int driveCapacity, driveSize;
    private int driveScanSize;
    private ArrayList<FileInfo> rootFiles;
    private ArrayList<Client> driveContainsClients;
    private ArrayList<Project> driveContainsProjects;

    public Drive(String driveName, long dateCreated, long lastDateScanned, int driveCapacity, int driveSize, int driveScanSize, ArrayList<FileInfo> rootFiles, ArrayList<Client> driveContainsClients, ArrayList<Project> driveContainsProjects) {
        this.driveName = driveName;
        this.dateCreated = dateCreated;
        this.lastDateScanned = lastDateScanned;
        this.driveCapacity = driveCapacity;
        this.driveSize = driveSize;
        this.driveScanSize = driveScanSize;
        this.rootFiles = rootFiles;
        this.driveContainsClients = driveContainsClients;
        this.driveContainsProjects = driveContainsProjects;
    }

    public Drive() {
    }

    public ArrayList<Client> getDriveContainsClients() {
        return driveContainsClients;
    }

    public void setDriveContainsClients(ArrayList<Client> driveContainsClients) {
        this.driveContainsClients = driveContainsClients;
    }

    public ArrayList<Project> getDriveContainsProjects() {
        return driveContainsProjects;
    }

    public void setDriveContainsProjects(ArrayList<Project> driveContainsProjects) {
        this.driveContainsProjects = driveContainsProjects;
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getLastDateScanned() {
        return lastDateScanned;
    }

    public void setLastDateScanned(long lastDateScanned) {
        this.lastDateScanned = lastDateScanned;
    }

    public int getDriveCapacity() {
        return driveCapacity;
    }

    public void setDriveCapacity(int driveCapacity) {
        this.driveCapacity = driveCapacity;
    }

    public int getDriveSize() {
        return driveSize;
    }

    public void setDriveSize(int driveSize) {
        this.driveSize = driveSize;
    }

    public int getDriveScanSize() {
        return driveScanSize;
    }

    public void setDriveScanSize(int driveScanSize) {
        this.driveScanSize = driveScanSize;
    }

    public ArrayList<FileInfo> getRootFiles() {
        return rootFiles;
    }

    public void setRootFiles(ArrayList<FileInfo> rootFiles) {
        this.rootFiles = rootFiles;
    }

    public class FileInfo {
        private ArrayList<FileInfo> subFiles;
        private BasicFileAttributes fileAttributes;

        public FileInfo(ArrayList<FileInfo> subFiles, BasicFileAttributes fileAttributes) {
            this.subFiles = subFiles;
            this.fileAttributes = fileAttributes;
        }

        public FileInfo() {
        }

        public ArrayList<FileInfo> getSubFiles() {
            return subFiles;
        }

        public void setSubFiles(ArrayList<FileInfo> subFiles) {
            this.subFiles = subFiles;
        }

        public BasicFileAttributes getFileAttributes() {
            return fileAttributes;
        }

        public void setFileAttributes(BasicFileAttributes fileAttributes) {
            this.fileAttributes = fileAttributes;
        }
    }
}
