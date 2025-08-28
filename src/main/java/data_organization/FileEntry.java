package data_organization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String fileHash;

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

    public byte[] hashFileContents() throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream fileStream = new FileInputStream(new File(this.path));
            DigestInputStream digestStream = new DigestInputStream(fileStream, digest);

            byte[] buffer = new byte[8192];
            while (digestStream.read(buffer) != -1) {}
            return digest.digest();
        } catch (IOException i) {
            throw new IOException(i);
        } catch (NoSuchAlgorithmException n) {
            System.out.println("Bad algo");
        }
        return null;
    }

    public String hashFullFile(byte[] contentHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            File file = new File(this.path);
            BasicFileAttributes attributes = Files.readAttributes(Path.of(this.getPath()), BasicFileAttributes.class);
            ByteBuffer bBuffer = ByteBuffer.allocate(Long.BYTES);
            bBuffer.putLong(attributes.size());
            byte[] sizeArr = bBuffer.array();
            digest.update(contentHash);

            digest.update(file.getName().getBytes(StandardCharsets.UTF_8));
            digest.update(file.getAbsolutePath().getBytes(StandardCharsets.UTF_8));
            digest.update(sizeArr);

            return Arrays.toString(digest.digest());
        } catch (IOException i) {

         } catch (NoSuchAlgorithmException n) {

        }
        return null;
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
