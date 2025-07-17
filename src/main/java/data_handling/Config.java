package data_handling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static String SAVE_PATH;

    public void writeSaveFile(String savePath) {
        Properties prop = new Properties();
        if (savePath != null) {
            prop.setProperty("save.path", savePath);
        }

        try {
            FileOutputStream stream = new FileOutputStream("config.properties");
            prop.store(stream, "Save File Location");
        } catch (IOException e) {
            System.err.println("Could not write to config file: " + e.getMessage());
        }
    }

    public String loadSaveFileLocation() {
        String saveFilePath;
        Properties prop = new Properties();
        try {
            FileInputStream input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (IOException e) {
            writeSaveFile(null);
            return null;
        }

        return saveFilePath = prop.getProperty("save.path");
    }

    public static String getSavePath() {
        return SAVE_PATH;
    }

    public static void setSavePath(String savePath) {
        SAVE_PATH = savePath;
    }
}
