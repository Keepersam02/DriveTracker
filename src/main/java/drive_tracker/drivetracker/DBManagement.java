package drive_tracker.drivetracker;

import data_organization.Drive;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManagement {
    public static void main(String []args) {
        createNewDB();
        createNewTable();
        insertNewElement();
        insert2();
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
