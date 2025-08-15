package drive_tracker.drivetracker;

import data_organization.Client;
import data_organization.Drive;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManagement {
    public static void main(String []args) {
        setupDB();
    }

    public static void setupDB() {
        String url = "jdbc:sqlite:core.db";

        var coreSql = "CREATE TABLE IF NOT EXISTS listItems ("
                + "listItemID INTEGER PRIMARY KEY,"
                + "name TEXT UNIQUE NOT NULL,"
                + "description TEXT,"
                + "dateCreated REAL,"
                + "dateLastModified REAL NOT NULL);";

        var drMapSql = "CREATE TABLE IF NOT EXISTS driveMap ("
                + "PRIMARY KEY(listItemID, driveID),"
                + "clientID INTEGER  NOT NULL,"
                + "driveID INTEGER  NOT NULL,"
                + "dateAssociated INTEGER,"
                + "FOREIGN KEY(listItemID) REFERENCES listItems(listItemID) ON DELETE CASCADE,"
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
                + "fileID INTEGER PRIMARY KEY"
                + "scanID INTEGER NOT NULL"
                + "parentID INTEGER"
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

    public static void insertClient(Client client) {
        var url = "jdbc:sqlite:core.db";
        String insStatement = "INSERT INTO listItems(name, description, dateCreated, dateLastModified) VALUES(?,?,?,?)";

        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(insStatement)) {
            stmt.setString(2, client.getName());
            stmt.setString(3, null);
            stmt.setInt(4, (int) client.getDateCreated());
            stmt.setInt(5, (int) client.getDateLastModified());
            stmt.execute();
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }


    public static void createNewDB() {
        String url = "jdbc:sqlite:my.db";

        try (var conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                var meta = conn.getMetaData();
                    System.out.println(meta.getDriverName());
            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

    public static void createNewTable() {
        var url = "jdbc:sqlite:my.db";

        var sql = "CREATE TABLE IF NOT EXISTS drives ("
                 + "    driveName TEXT PRIMARY KEY,"
                 + "    description TEXT NOT NULL,"
                 + "    capacity REAL"
                 + ");";

        try (var conn = DriverManager.getConnection(url);
            var stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

    public static void insertNewElement() {
        String url = "jdbc:sqlite:my.db";

        var names = new String[] {"PB Drive 1", "PB Drive 2"};
        var description = new String[] {"PB drive", "pb drive 2"};
        var capacities = new int[] {5000000, 5000000};

        String sqlIns = "INSERT INTO drives(description, capacity) VALUES(?,?)";

        try (var conn = DriverManager.getConnection(url); var pstmt = conn.prepareStatement(sqlIns)) {
            for  (int i = 0;i < names.length; i++) {
                pstmt.setString(1, description[i]);
                pstmt.setInt(2, capacities[i]);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
        }
    }
    public static void insert2() {
        String url = "jdbc:sqlite:my.db";

        var name = "BVK 1";
        var description = "2012-14";
        int capacity = 10000000;

        String sqlIns = "INSERT INTO drives(driveName, description, capacity) VALUES(?,?,?)";


        try (var conn = DriverManager.getConnection(url); var stmt = conn.prepareStatement(sqlIns)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, capacity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
