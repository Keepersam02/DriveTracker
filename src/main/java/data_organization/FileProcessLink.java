package data_organization;

public class FileProcessLink {
    private String filePath;
    private int parentID;

    public FileProcessLink() {
    }

    public FileProcessLink(String filePath, int parentID) {
        this.filePath = filePath;
        this.parentID = parentID;
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
