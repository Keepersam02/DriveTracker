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
import java.util.HexFormat;
import java.util.Objects;

public class FileEntry implements Comparable<FileEntry> {
    private String name;
    private String path;
    private int isDirectory;
    private long size;
    private String hash;
    private long lastModified;
    private long dateCreated;
    private int parentID;
    private int scanFileID;
    private String parentPath;

    public FileEntry(String name, String path, int isDirectory, long size, String hash,
                     long lastModified, long dateCreated, int parentID, String parentPath) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.hash = hash;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
        this.parentID = parentID;
        this.parentPath = parentPath;
    }

    public FileEntry(String name, String path, int isDirectory, long size, String hash, long lastModified, long dateCreated, int parentID, int scanFileID, String parentPath) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.hash = hash;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
        this.parentID = parentID;
        this.scanFileID = scanFileID;
        this.parentPath = parentPath;
    }

    public FileEntry(String name, String path, int isDirectory, long size, String hash,
                     long lastModified, long dateCreated) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.hash = hash;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
    }

    public FileEntry(String name, String path,long size, long lastModified, long dateCreated) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
    }

    public FileEntry() {
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
            throw new RuntimeException();
        }
    }

    public void hashFullFile(byte[] contentHash) throws IOException {
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
            byte[] fileHash = digest.digest();

            HexFormat format = HexFormat.of();
            this.hash = format.formatHex(fileHash);
        } catch (IOException i) {
            throw new IOException(i);
         } catch (NoSuchAlgorithmException n) {
            throw new RuntimeException();
        }
    }

    public int getScanFileID() {
        return scanFileID;
    }

    public void setScanFileID(int scanFileID) {
        this.scanFileID = scanFileID;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
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

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }


    @Override
    public int compareTo(FileEntry o) {
        return Long.compare(this.size, o.size);
    }
}
