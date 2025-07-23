module drive_tracker.drivetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires com.google.gson;
    requires java.desktop;
    requires java.logging;


    opens drive_tracker.drivetracker to javafx.fxml;
    exports drive_tracker.drivetracker;
}