package drive_tracker.drivetracker;

import java.io.IOException;
import java.util.logging.*;

public class ErrLogger {
    private static final Logger LOGGER = Logger.getLogger("ErrLog");

    static {
        for (Handler handler : LOGGER.getHandlers()) {
            LOGGER.removeHandler(handler);
        }
        try {
            FileHandler errorHandler = new FileHandler("log/error.log", true);
            FileHandler warningHandler = new FileHandler("log/warning.log", true);
            FileHandler infoHandler = new FileHandler("log/info.log", true);
            FileHandler debugHandler = new FileHandler("log/debug.log", true);

            SimpleFormatter formatter = new SimpleFormatter();
            errorHandler.setFilter(record -> record.getLevel() == Level.SEVERE);
            errorHandler.setFilter(record -> record.getLevel() == Level.WARNING);
            errorHandler.setFilter(record -> record.getLevel() == Level.INFO);
            errorHandler.setFilter(record -> record.getLevel() == Level.FINE);

            LOGGER.addHandler(errorHandler);
            LOGGER.addHandler(warningHandler);
            LOGGER.addHandler(infoHandler);
            LOGGER.addHandler(debugHandler);

            LOGGER.setLevel(Level.ALL);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(formatter);
            LOGGER.addHandler(consoleHandler);
        } catch (IOException i) {
            System.err.println("Failed to initialize logger: " + i.getMessage());
        }
    }

    public static void debug(String msg) {
        LOGGER.fine(msg);
    }

    public static void info(String msg) {
        LOGGER.info(msg);
    }

    public static void warn(String msg) {
        LOGGER.warning(msg);
    }

    public static void error(String msg) {
        LOGGER.severe(msg);
    }

    public static void error(String msg, Throwable thrown) {
        LOGGER.log(Level.SEVERE, msg, thrown);
    }
}
