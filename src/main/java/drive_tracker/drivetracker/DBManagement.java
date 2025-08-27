package drive_tracker.drivetracker;

import data_organization.Client;
import data_organization.Drive;
import data_organization.FileEntry;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DBManagement {
    public static void main(String []args) {
        setupDB();
    }

    public static void setupDB() {
        String url = "jdbc:sqlite:core.db";

        var coreSql = "CREATE TABLE IF NOT EXISTS listItems ("
                + "listItemID INTEGER PRIMARY KEY,"
                + "isClient INTEGER NOT NULL,"
                + "clientID INTEGER,"
                + "name TEXT UNIQUE NOT NULL,"
                + "description TEXT,"
                + "dateCreated INTEGER,"
                + "dateLastModified INTEGER NOT NULL);";

        var drMapSql = "CREATE TABLE IF NOT EXISTS driveMap ("
                + "mapID INTEGER PRIMARY KEY,"
                + "clientID INTEGER  NOT NULL,"
                + "driveID INTEGER  NOT NULL,"
                + "dateAssociated INTEGER,"
                + "FOREIGN KEY(clientID) REFERENCES listItems(listItemID) ON DELETE CASCADE,"
                + "FOREIGN KEY(driveID) REFERENCES drives(driveID) ON DELETE CASCADE);";

        var drSql = "CREATE TABLE IF NOT EXISTS drives ("
                + "driveID INTEGER PRIMARY KEY,"
                + "driveName TEXT UNIQUE NOT NULL,"
                + "description TEXT,"
                + "dateCreated INTEGER,"
                + "dateLastModified INTEGER);";

        var scanTableSql = "CREATE TABLE IF NOT EXISTS scans ("
                + "scanID INTEGER PRIMARY KEY,"
                + "dateScan INTEGER,"
                + "driveID INTEGER NOT NULL,"
                + "FOREIGN KEY(driveID) REFERENCES drives(driveIF) ON DELETE RESTRICT);";

        var fileTableSql = "CREATE TABLE IF NOT EXISTS files ("
                + "fileID INTEGER PRIMARY KEY,"
                + "scanID INTEGER NOT NULL,"
                + "parentID INTEGER,"
                + "name TEXT,"
                + "path TEXT,"
                + "isDir INTEGER,"
                + "hash INTEGER,"
                + "dateCreated INTEGER,"
                + "dateLastModified INTEGER,"
                + "size LONG,"
                + "FOREIGN KEY(scanID) REFERENCES scans(scanID),"
                + "FOREIGN KEY(parentID) REFERENCES files(fileID));";

        try (var conn = DriverManager.getConnection(url); var stmt = conn.createStatement()) {
            stmt.execute(coreSql);
            System.out.println("1");
            stmt.execute(drMapSql);
            System.out.println("2");
            stmt.execute(drSql);
            System.out.println("3");
            stmt.execute(scanTableSql);
            System.out.println("4");
            stmt.execute(fileTableSql);
            System.out.println("5");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertListItem(String name, String description, int isClient, String parentName) throws SQLException {
        var url = "jdbc:sqlite:core.db";
        int clientID;
        long currentTime = System.currentTimeMillis();

        if (isClient == 0) {
            String qryStmt = "SELECT listItemID FROM listItems WHERE name = ?";
            try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(qryStmt)) {
                stmt.setString(1, parentName);
                var rs = stmt.executeQuery();
                clientID = rs.getInt(1);
            } catch (SQLException s) {
                throw new SQLException(s);
            }

            String insStatement = "INSERT INTO listItems(isClient, clientID, name, description, dateCreated, dateLastModified) VALUES(?,?,?,?,?,?)";
            try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(insStatement)) {
                stmt.setInt(1, isClient);
                stmt.setInt(2, clientID);
                stmt.setString(3, name);
                stmt.setString(4, description);
                stmt.setLong(5, currentTime);
                stmt.setLong(6, currentTime);
                stmt.execute();
            }
        } else if (isClient == 1) {
            String insStmt = "INSERT INTO listItems(isClient, name, description, dateCreated, dateLastModified) VALUES(?,?,?,?,?)";
            try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(insStmt)) {
                stmt.setInt(1, 1);
                stmt.setString(2, name);
                stmt.setString(3, description);
                stmt.setLong(4, currentTime);
                stmt.setLong(5, currentTime);
                stmt.execute();
            } catch (SQLException s) {
                throw new RuntimeException(s);
            }
        } else {
            throw new IllegalArgumentException("Invalid case for isClient");
        }
    }

    public static void updateClientIDs(String clientName, ObservableList<String> projectNames) {
        String url = "jdbc:sqlite:core.db";
        String clientIDQry = "SELECT listItemID FROM listItems WHERE name = ?";
        String updateStmt = "UPDATE listItems SET clientID = ? WHERE name = ?";
        int clientID;
        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(clientIDQry); var upStmt = conn.prepareStatement(updateStmt)) {
            stmt.setString(1, clientName);
            var rs = stmt.executeQuery();
            clientID = rs.getInt(1);

            for (String projName : projectNames) {
                upStmt.setInt(1, clientID);
                upStmt.setString(2, projName);
                upStmt.executeUpdate();
            }
        } catch (SQLException s) {

        }

    }

    public static void updateClientDriveMap(String listItemName, ObservableList<String> driveNames) throws SQLException {
        String url = "jdbc:sqlite:core.db";
        String qryStmt = "SELECT driveID FROM drives WHERE driveName = ?";
        String getClientStmt = "SELECT listItemID FROM listItems WHERE name = ?";
        String insStmt = "INSERT INTO driveMap(clientID, driveID, dateAssociated) VALUES(?,?,?)";
        try (var conn = DriverManager.getConnection(url);
             var stmtQry = conn.prepareStatement(qryStmt); var stmtIns = conn.prepareStatement(insStmt);
             var stmtGetClID = conn.prepareStatement(getClientStmt)) {

            stmtGetClID.setString(1, listItemName);
            var resSet = stmtGetClID.executeQuery();
            int clientID = resSet.getInt(1);
            for (String name : driveNames) {
                stmtQry.setString(1, name);
                var rs = stmtQry.executeQuery();
                int driveID = rs.getInt(1);

                stmtIns.setInt(1, clientID);
                stmtIns.setInt(2, driveID);
                stmtIns.setLong(3, System.currentTimeMillis());
                stmtIns.execute();
            }
        } catch (SQLException s) {
            throw new SQLException(s);
        }
    }

    public static void insertNewDrive(String driveName, String description) throws SQLException{
        String url = "jdbc:sqlite:core.db";
        String insStr = "INSERT INTO drives(driveName, description, dateCreated, dateLastModified) VALUES(?,?,?,?)";

        try (var conn = DriverManager.getConnection(url); var insStmt = conn.prepareStatement(insStr)) {
            insStmt.setString(1, driveName);
            insStmt.setString(2, description);
            insStmt.setLong(3, System.currentTimeMillis());
            insStmt.setLong(4, System.currentTimeMillis());
            insStmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static void updateDriveClientMap(String driveName, ObservableList<String> listItemNames) throws SQLException {
        String url = "jdbc:sqlite:core.db";
        String qryStmt = "SELECT driveID FROM drives WHERE driveName = ?";
        String qryListItemStmt = "Select listItemID FROM listItems WHERE name = ?";

        String insStr = "INSERT INTO driveMap(driveID, listItemID, dateAssociated) VALUES(?,?,?)";

        try (var conn = DriverManager.getConnection(url); var driveQry = conn.prepareStatement(qryStmt);
             var listItemQry = conn.prepareStatement(qryListItemStmt); var insStmt = conn.prepareStatement(insStr)) {
            driveQry.setString(1, driveName);
            var driveIDRS = driveQry.executeQuery();
            int driveID = driveIDRS.getInt(1);

            ArrayList<Integer> listItemsIDs = new ArrayList<>();
            for (String listItemName : listItemNames) {
                listItemQry.setString(1, listItemName);
                var listItemRS = listItemQry.executeQuery();

                insStmt.setInt(1, driveID);
                insStmt.setInt(2, listItemRS.getInt(1));
                insStmt.setLong(3, System.currentTimeMillis());
                insStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static void scanDrive(String filePath, int scanID, PriorityBlockingQueue<FileEntry> fileQueue, AtomicBoolean finishedScanning, AtomicInteger numFilesFound) {
        finishedScanning.compareAndSet(false, false);
        numFilesFound.compareAndSet(0, 0);

        try {
            Files.walkFileTree(Path.of(filePath), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    FileEntry newEntry = new FileEntry();
                    newEntry.setName(file.getFileName().toString());
                    newEntry.setPath(file.toString());
                    newEntry.setDateCreated(attrs.creationTime().toMillis());
                    newEntry.setLastModified(attrs.lastModifiedTime().toMillis());
                    newEntry.setSize(attrs.size());
                    if (attrs.isDirectory()) {
                        newEntry.setIsDirectory(1);
                    } else {
                        newEntry.setIsDirectory(0);
                    }

                    String url = "jdbc:sqlite:core.db";
                    String getParentIDStr = "SELECT fileID FROM files WHERE path = ?";
                    String insStr = "INSERT INTO files(scanID, parentID, name, path, isDir, dateCreated, dateLastModified, size) VALUES(?,?,?,?,?,?,?,?)";
                    try (var conn = DriverManager.getConnection(url);
                         var insStmt = conn.prepareStatement(insStr); var getParentIDStmt = conn.prepareStatement(getParentIDStr)){
                        getParentIDStmt.setString(1, newEntry.getPath());
                        var rs = getParentIDStmt.executeQuery();
                        int parentID = rs.getInt(1);
                        newEntry.setParentID(parentID);

                        insStmt.setInt(2, scanID);
                        insStmt.setInt(3, parentID);
                        insStmt.setString(4, newEntry.getName());
                        insStmt.setString(5, newEntry.getPath());
                        insStmt.setInt(6, newEntry.getIsDirectory());
                        insStmt.setLong(7, newEntry.getDateCreated());
                        insStmt.setLong(8, newEntry.getLastModified());
                        insStmt.setLong(9, newEntry.getSize());
                        insStmt.execute();
                    } catch (SQLException s) {

                    }

                    fileQueue.offer(newEntry);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.println("Failed to visit file: " + file.toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {

        }
    }

    public static void fileHashManager(PriorityBlockingQueue<FileEntry> fileQueue, AtomicBoolean finishedScanning, AtomicInteger numFilesFound, AtomicBoolean finishedHashing) {
        AtomicInteger numberActiveThreads = new AtomicInteger(0);
        AtomicInteger filesHashed = new AtomicInteger(0);

        while (true) {
            if (finishedScanning.get() && numFilesFound.get() <= filesHashed.get()) {
                break;
            }
            if (!fileQueue.isEmpty() || numberActiveThreads.get() <= Runtime.getRuntime().availableProcessors()) {
                Thread thread = new Thread(() -> {
                   numberActiveThreads.incrementAndGet();
                   FileEntry fileEntry = fileQueue.poll();
                   assert fileEntry != null;

                   try {
                        FileReader reader = new FileReader(fileEntry.getPath());
                        int fileHash = reader.hashCode();
                   } catch (FileNotFoundException i) {

                   }
                });
            }

        }
    }



}
