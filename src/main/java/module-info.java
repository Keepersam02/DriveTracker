module drive_tracker.drivetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires com.google.gson;
    requires java.desktop;
    requires java.logging;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens drive_tracker.drivetracker to javafx.fxml;
    opens data_organization to com.google.gson;
    opens data_handling to com.google.gson;

    exports drive_tracker.drivetracker;
}