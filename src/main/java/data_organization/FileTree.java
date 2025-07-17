package data_organization;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class FileTree {
    private String name;
    private String hash;
    private ArrayList<FileEntry> rootFiles;

    private LocalDateTime dateCreated;
    private LocalDateTime lastScan;

    public FileTree() {
    }

    public FileTree(String name, String hash, LocalDateTime dateCreated, LocalDateTime lastScan) {
        this.name = name;
        this.hash = hash;
        this.dateCreated = dateCreated;
        this.lastScan = lastScan;
    }

    public FileTree(String name, String hash, ArrayList<FileEntry> rootFiles, LocalDateTime dateCreated, LocalDateTime lastScan) {
        this.name = name;
        this.hash = hash;
        this.rootFiles = rootFiles;
        this.dateCreated = dateCreated;
        this.lastScan = lastScan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public ArrayList<FileEntry> getRootFiles() {
        return rootFiles;
    }

    public void setRootFiles(ArrayList<FileEntry> rootFiles) {
        this.rootFiles = rootFiles;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getLastScan() {
        return lastScan;
    }

    public void setLastScan(LocalDateTime lastScan) {
        this.lastScan = lastScan;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FileTree fileTree = (FileTree) o;
        return Objects.equals(name, fileTree.name) && Objects.equals(hash, fileTree.hash) && Objects.equals(rootFiles, fileTree.rootFiles) && Objects.equals(dateCreated, fileTree.dateCreated) && Objects.equals(lastScan, fileTree.lastScan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hash, rootFiles, dateCreated, lastScan);
    }
}
