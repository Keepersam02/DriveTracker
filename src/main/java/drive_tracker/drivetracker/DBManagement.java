package drive_tracker.drivetracker;

import data_organization.Client;
import data_organization.Drive;
import data_organization.FileEntry;
import data_organization.FileProcessLink;
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.*;
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

    public static Drive getDrive(String driveName) throws SQLException {
        String url = "jdbc:sqlite:core.db";
        String getDriveStr = "SELECT driveID, description FROM drives WHERE driveName = ?";
        String belongsToStr = "SELECT clientID FROM driveMap WHERE driveID = ?";

        try (var conn = DriverManager.getConnection(url); var getDriveStmt = conn.prepareStatement(getDriveStr);
        var belongsToStmt = conn.prepareStatement(belongsToStr)) {
            getDriveStmt.setString(1, driveName);
            var driveRS = getDriveStmt.executeQuery();
            Drive drive = new Drive();
            drive.setDriveID(driveRS.getInt(1));
            drive.setDescription(driveRS.getString(2));
            return drive;
        } catch (SQLException s) {
            throw new SQLException(s);
        }
    }

    public static ArrayList<Integer> getDrivesScans(String driveName) throws SQLException{
        String url = "jdbc:sqlite:core.db";
        String getDriveIDStr = "SELECT driveID FROM drives WHERE driveName = ?";
        String getScansStr = "SELECT scanID FROM scans WHERE driveID = ?";
        try (var conn = DriverManager.getConnection(url); var getDriveStmt = conn.prepareStatement(getDriveIDStr);
        var getScansStmt = conn.prepareStatement(getScansStr)) {
            getDriveStmt.setString(1, driveName);
            var driveIDRS = getDriveStmt.executeQuery();

            getScansStmt.setInt(1, driveIDRS.getInt(1));
            var scanRS = getScansStmt.executeQuery();
            ArrayList<Integer> scanIDs = new ArrayList<>();
            while (scanRS.next()) {
                scanIDs.add(scanRS.getInt(1));
            }
            return scanIDs;
        } catch (SQLException s) {
            throw new SQLException(s);
        }
    }

    public static ArrayList<String> getDrivesOwners(int driveID) throws SQLException{
        String url = "jdbc:sqlite:core.db";
        String getClientIDsStr = "SELECT clientID FROM driveMap WHERE driveID = ?";
        String getClientStr = "SELECT name FROM listItems WHERE listItemID = ?";
        ArrayList<String> listItemNames = new ArrayList<>();

        try (var conn = DriverManager.getConnection(url); var getClientIDsStmt = conn.prepareStatement(getClientIDsStr);
        var getClientStmt = conn.prepareStatement(getClientStr)) {

            getClientIDsStmt.setInt(1, driveID);
            var clientIDRS = getClientIDsStmt.executeQuery();
            while (clientIDRS.next()) {
                getClientStmt.setInt(1, clientIDRS.getInt(1));
                var clientRS = getClientStmt.executeQuery();
                listItemNames.add(clientRS.getString(1));
            }
            return listItemNames;
        } catch (SQLException s) {
            throw new SQLException(s);
        }
    }

    public static int createNewScan(long dateScan, int driveID) throws SQLException{
        String url = "jdbc:sqlite:core.db";
        String insertScanStr = "INSERT INTO scans(dateScan, driveID) VALUES(?,?)";
        try (var conn = DriverManager.getConnection(url); var insertScanStmt = conn.prepareStatement(insertScanStr, Statement.RETURN_GENERATED_KEYS)) {
            insertScanStmt.setLong(1, dateScan);
            insertScanStmt.setInt(2, driveID);
            insertScanStmt.execute();

            try (var generatedKeys = insertScanStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Could not get scanID");
                }
            }
        } catch (SQLException s) {
            throw new SQLException(s);
        }
    }

    public static void scanFileTree(ConcurrentLinkedQueue<FileProcessLink> filesToProcess, PriorityBlockingQueue<FileEntry> fileQueue, String drivePath, AtomicBoolean finishedScanning, AtomicInteger filesScanned) {
        AtomicInteger idCounter = new AtomicInteger(0);
        Stack<FileProcessLink> directoryPaths = new Stack<>();
        File homeDirectory = new File(drivePath);
        directoryPaths.push(new FileProcessLink(homeDirectory.getPath(), idCounter.getAndIncrement()));

        while (!directoryPaths.isEmpty()) {
            FileProcessLink current = directoryPaths.pop();
            File currentFile = new File(current.getFilePath());

            File[] children = currentFile.listFiles();
            for (File child : children) {
                if (child == null) {
                    continue;
                }
                if (child.isDirectory()) {
                    directoryPaths.push(new FileProcessLink(child.getPath(), current.getParentID()));
                    filesScanned.incrementAndGet();
                } else {
                    filesToProcess.offer(new FileProcessLink(child.getPath(), current.getParentID()));
                    filesScanned.incrementAndGet();
                }
            }
        }
        finishedScanning.set(true);
    }

    private void processPoolManager(ConcurrentLinkedQueue<FileProcessLink> filesToProcess, PriorityBlockingQueue<FileEntry> fileQueue, AtomicBoolean finishedScanning, AtomicInteger filesFound) {
        int filesProcessed = 0;
        AtomicInteger numActiveThreads = new AtomicInteger(0);
        while (true) {
            if (finishedScanning.get() && filesProcessed >= filesFound.get() && filesToProcess.isEmpty()) {
                break;
            }
            if (finishedScanning.get() && filesProcessed <= filesFound.get()) {
                Stack<FileProcessLink> processStack = new Stack<>();
                for (int i = 0; i < filesToProcess.size(); i++) {
                    processStack.push(filesToProcess.poll());
                }
                Thread processThread = new Thread(() -> {
                    numActiveThreads.incrementAndGet();
                    processFiles(processStack, fileQueue);
                    numActiveThreads.decrementAndGet();
                });
                processThread.start();
            }
            if (filesToProcess.size() < 70) {
                continue;
            }
            if (numActiveThreads.get() > 5) {
                continue;
            }

            Stack<FileProcessLink> processStack = new Stack<>();
            for (int i = 0; i < 70; i++) {
                processStack.push(filesToProcess.poll());
            }
            Thread processThread = new Thread(() -> {
                numActiveThreads.incrementAndGet();
                processFiles(processStack, fileQueue);
                numActiveThreads.decrementAndGet();
            });
            processThread.start();
        }
    }

    private void processFiles(Stack<FileProcessLink> filePathStack, PriorityBlockingQueue<FileEntry> fileQueue) {
        while (!filePathStack.isEmpty()) {
            FileProcessLink current = filePathStack.pop();
            File currentFile = new File(current.getFilePath());
            if (currentFile.isDirectory()) {
                FileEntry newDirEntry;
                try {
                    BasicFileAttributes attrs = Files.readAttributes(Path.of(currentFile.getPath()), BasicFileAttributes.class);
                    newDirEntry = new FileEntry(currentFile.getName(), currentFile.getPath(), 1, attrs.size(), null, attrs.lastModifiedTime().toMillis(),
                            attrs.creationTime().toMillis(), current.getParentID(), null);
                } catch (IOException e) {
                    System.out.println("Failed to read attributes: " + currentFile.getPath());
                    System.out.println(e.getMessage());
                }
                continue;
            }
            long fileSize;
            FileEntry newEntry;
            try {
                BasicFileAttributes attrs = Files.readAttributes(Path.of(currentFile.getPath()), BasicFileAttributes.class);
                newEntry = new FileEntry(currentFile.getName(), currentFile.getPath(), 0, attrs.size(), null, attrs.lastModifiedTime().toMillis(),
                        attrs.creationTime().toMillis(), current.getParentID(), null);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(currentFile.getAbsoluteFile());
                continue;
            }
            try {
                newEntry.hashFullFile(newEntry.hashFileContents());
            } catch (IOException e) {
                System.out.println("Failed hashing: " + newEntry.getName());
                System.out.println(e.getMessage());
            }
            fileQueue.offer(newEntry);
        }
    }

    private static class ProcessDirFilesLink {
        private int parentID;
        private Stack<String> filePaths;

        public ProcessDirFilesLink(int parentID, Stack<String> filePaths) {
            this.parentID = parentID;
            this.filePaths = filePaths;
        }

        public int getParentID() {
            return parentID;
        }

        public void setParentID(int parentID) {
            this.parentID = parentID;
        }

        public Stack<String> getFilePaths() {
            return filePaths;
        }

        public void setFilePaths(Stack<String> filePaths) {
            this.filePaths = filePaths;
        }

        public ProcessDirFilesLink() {
        }
    }



/*    public static void scanDrive(String filePath, int scanID, ConcurrentHashMap<String, Integer> fileMap, PriorityBlockingQueue<FileEntry> fileQueue,
                                 AtomicBoolean finishedScanning, AtomicInteger numFilesFound) {
        finishedScanning.compareAndSet(false, false);
        numFilesFound.compareAndSet(0, 0);


        try {
            Files.walkFileTree(Path.of(filePath), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    FileEntry newEntry = new FileEntry(path.getFileName().toString(), path.toAbsolutePath().toString(),
                            attrs.size(), attrs.creationTime().toMillis(), attrs.lastModifiedTime().toMillis());

                    if (attrs.isDirectory()) {
                        newEntry.setIsDirectory(1);
                    } else {
                        newEntry.setIsDirectory(0);
                    }
                    UUID uuid = UUID.randomUUID();
                    newEntry.setParentPath(path.getParent().toAbsolutePath().toString());

                    fileMap.put(newEntry.getPath(), fileIndex[0]);

                    fileQueue.offer(newEntry);

                    fileIndex[0]++;
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
            System.out.println("Scan drive error");
            System.out.println(e.getMessage());
        }
    }*/

    public static void fileHashManager(PriorityBlockingQueue<FileEntry> fileQueue, ConcurrentLinkedQueue<FileEntry> outputQue, AtomicBoolean finishedScanning,
                                       AtomicInteger numFilesFound, AtomicBoolean finishedHashing)  {
        AtomicInteger numberActiveThreads = new AtomicInteger(0);
        AtomicInteger filesHashed = new AtomicInteger(0);

        while (true) {
            if (finishedScanning.get() && numFilesFound.get() <= filesHashed.get()) {
                break;
            }
            if (!fileQueue.isEmpty() || numberActiveThreads.get() <= Runtime.getRuntime().availableProcessors()) {
                Thread thread = new Thread(() -> {
                    try {
                        numberActiveThreads.incrementAndGet();
                        FileEntry fileEntry = fileQueue.take();
                        assert fileEntry != null;

                        if (fileEntry.getIsDirectory() == 1) {
                            fileEntry.setHash("DIRECTORY");
                            outputQue.offer(fileEntry);
                            filesHashed.incrementAndGet();
                            return;
                        }

                        byte[] contentHash = fileEntry.hashFileContents();
                        fileEntry.hashFullFile(contentHash);
                        outputQue.offer(fileEntry);
                   } catch (IOException i) {
                        System.out.println("File hash manager error");
                        System.out.println(i.getMessage());
                        System.out.println(i.getCause().toString());
                   } catch (InterruptedException x) {
                        System.out.println("File hash manager error");
                        System.out.println(x.getMessage());
                   }
                });
                thread.start();
            }

        }
    }

    public static void writeFileEntries(ConcurrentLinkedQueue<FileEntry> finishedFileQueue,
                                        AtomicBoolean finishedScan, AtomicInteger numFilesFound, AtomicBoolean finishedHashing, int scanID) {
        String url = "jdbc:sqlite:core.db";
        String insertEntryStr = "INSERT INTO files(scanID, parentID, name, path, isDir, hash, dateCreated, dateLastModified, size) VALUES(?,?,?,?,?,?,?,?,?)";
        String getNextRowID = "SELECT seq + 1 FROM sqlite_sequence WHERE name = 'files;";
        try (var conn = DriverManager.getConnection(url); var insertEntryStmt = conn.prepareStatement(insertEntryStr);
        var getNextID = conn.prepareStatement(getNextRowID)) {
            long rowIDOffset;
            var nextRowRS = getNextID.executeQuery();
            if (nextRowRS.next()) {
                rowIDOffset = nextRowRS.getInt(1);
            } else {

            }
            int numFilesWritten = 0;
            FileEntry currentEntry;
            while (!finishedScan.get() && !finishedHashing.get() && numFilesWritten <= numFilesFound.get()) {
                currentEntry = finishedFileQueue.poll();

                insertEntryStmt.setInt(1, scanID);
                insertEntryStmt.setInt(2, currentEntry.getParentID() + rowIDOffset);
                insertEntryStmt.setString(3, currentEntry.getName());
                insertEntryStmt.setString(4, currentEntry.getPath());
                insertEntryStmt.setInt(5, currentEntry.getIsDirectory());
                insertEntryStmt.setString(6, currentEntry.getHash());
                insertEntryStmt.setLong(7, currentEntry.getDateCreated());
                insertEntryStmt.setLong(8, currentEntry.getLastModified());
                insertEntryStmt.setLong(9, currentEntry.getSize());
                insertEntryStmt.execute();
                numFilesWritten++;
            }
        } catch (SQLException s) {
            System.out.println("write entries error");
            System.out.println(s.getMessage());
        }
    }

}
