package data_organization;

import java.util.ArrayList;
import java.util.Objects;

public class Scan {
    private int scanID, driveID;
    private long dateScan;
    private ArrayList<FileEntry> rootFiles;

    public Scan(int scanID, int driveID, long dateScan, ArrayList<FileEntry> rootFiles) {
        this.scanID = scanID;
        this.driveID = driveID;
        this.dateScan = dateScan;
        this.rootFiles = rootFiles;
    }

    public Scan(int scanID, int driveID, long dateScan) {
        this.scanID = scanID;
        this.driveID = driveID;
        this.dateScan = dateScan;
    }

    public Scan() {
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Scan scan = (Scan) object;
        return scanID == scan.scanID && driveID == scan.driveID && dateScan == scan.dateScan && Objects.equals(rootFiles, scan.rootFiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scanID, driveID, dateScan, rootFiles);
    }

    public int getScanID() {
        return scanID;
    }

    public void setScanID(int scanID) {
        this.scanID = scanID;
    }

    public int getDriveID() {
        return driveID;
    }

    public void setDriveID(int driveID) {
        this.driveID = driveID;
    }

    public long getDateScan() {
        return dateScan;
    }

    public void setDateScan(long dateScan) {
        this.dateScan = dateScan;
    }

    public ArrayList<FileEntry> getRootFiles() {
        return rootFiles;
    }

    public void setRootFiles(ArrayList<FileEntry> rootFiles) {
        this.rootFiles = rootFiles;
    }
}
