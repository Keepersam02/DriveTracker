package data_organization;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class FileEntry {
    private String name;
    private String path;
    private int isDirectory;
    private long size;
    private int hash;
    private long lastModified;
    private long dateCreated;
    private int parentID;
    private FileEntry parentFile;
    private ArrayList<FileEntry> childrenFiles;

    public FileEntry(String name, String path, int isDirectory, long size, int hash, long lastModified, long dateCreated, int parentID, FileEntry parentFile, ArrayList<FileEntry> childrenFiles) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.hash = hash;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
        this.parentID = parentID;
        this.parentFile = parentFile;
        this.childrenFiles = childrenFiles;
    }

    public FileEntry(String name, String path, int isDirectory, long size, int hash, long lastModified, long dateCreated) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.hash = hash;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
    }

    public FileEntry() {
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        FileEntry fileEntry = (FileEntry) object;
        return isDirectory == fileEntry.isDirectory && size == fileEntry.size && hash == fileEntry.hash && lastModified == fileEntry.lastModified && dateCreated == fileEntry.dateCreated && parentID == fileEntry.parentID && Objects.equals(name, fileEntry.name) && Objects.equals(path, fileEntry.path) && Objects.equals(parentFile, fileEntry.parentFile) && Objects.equals(childrenFiles, fileEntry.childrenFiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, isDirectory, size, hash, lastModified, dateCreated, parentID, parentFile, childrenFiles);
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(int directory) {
        isDirectory = directory;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public FileEntry getParentFile() {
        return parentFile;
    }

    public void setParentFile(FileEntry parentFile) {
        this.parentFile = parentFile;
    }

    public ArrayList<FileEntry> getChildrenFiles() {
        return childrenFiles;
    }

    public void setChildrenFiles(ArrayList<FileEntry> childrenFiles) {
        this.childrenFiles = childrenFiles;
    }
}
