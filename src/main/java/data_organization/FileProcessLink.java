package data_organization;

public class FileProcessLink {
    private String filePath;
    private int parentID;
    private int fileID;

    public FileProcessLink() {
    }

    public FileProcessLink(String filePath, int parentID) {
        this.filePath = filePath;
        this.parentID = parentID;
    }

    public FileProcessLink(String filePath, int parentID, int fileID) {
        this.filePath = filePath;
        this.parentID = parentID;
        this.fileID = fileID;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }
}
