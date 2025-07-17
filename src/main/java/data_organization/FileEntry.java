package data_organization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class FileEntry {
    private String name;
    private String path;
    private boolean isDirectory;
    private long size;
    private String hash;
    private LocalDateTime lastModified;
    private LocalDateTime dateCreated;

    ArrayList<FileEntry> subFileEntries;

    public FileEntry() {
    }

    public FileEntry(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public FileEntry(String name, String path, boolean isDirectory, long size, String hash, LocalDateTime lastModified, LocalDateTime dateCreated) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.hash = hash;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
    }

    public FileEntry(String name, String path, boolean isDirectory, long size, String hash, LocalDateTime lastModified, LocalDateTime dateCreated, ArrayList<FileEntry> subFileEntries) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.hash = hash;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
        this.subFileEntries = subFileEntries;
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

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ArrayList<FileEntry> getSubFileEntries() {
        return subFileEntries;
    }

    public void setSubFileEntries(ArrayList<FileEntry> subFileEntries) {
        this.subFileEntries = subFileEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FileEntry fileEntry = (FileEntry) o;
        return isDirectory == fileEntry.isDirectory && size == fileEntry.size && Objects.equals(name, fileEntry.name) && Objects.equals(path, fileEntry.path) && Objects.equals(hash, fileEntry.hash) && Objects.equals(lastModified, fileEntry.lastModified) && Objects.equals(dateCreated, fileEntry.dateCreated) && Objects.equals(subFileEntries, fileEntry.subFileEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, isDirectory, size, hash, lastModified, dateCreated, subFileEntries);
    }
}
