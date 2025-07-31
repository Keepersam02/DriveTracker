package data_handling;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import drive_tracker.drivetracker.ErrLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {
    private String saveFilePath;
    private String version;

    public boolean writeSaveFilePath(String newSaveFilePath) {
        Config config = new Config();
        try {
           FileInputStream fileInputStream = new FileInputStream("config.json");
            Gson gson = new Gson();
            config = gson.fromJson(fileInputStream.toString(), Config.class);
            config.setSaveFilePath(newSaveFilePath);
            String configStr = gson.toJson(config, Config.class);
            FileOutputStream fileOutputStream = new FileOutputStream("config.json");
            fileOutputStream.write(configStr.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException f) {
            config = new Config(newSaveFilePath, "0.0.1");
            Gson gson = new Gson();
            String gsonString;
            try {
                gsonString = gson.toJson(config);
            } catch (JsonIOException j) {
                ErrLogger.error("Failed to write save file path", j);
                return false;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("config.json");
                fileOutputStream.write(gsonString.getBytes(StandardCharsets.UTF_8));
            } catch (FileNotFoundException n) {
                ErrLogger.error("Could not write config file", n);
            } catch (IOException i) {
               ErrLogger.error("Could not write to config file", i);
            }

        } catch (IOException e) {
            ErrLogger.warn("Failed to save new file path");
            return false;
        }
        return true;
    }

    public String loadSaveFilePath() {
        Config config;

        try {
            FileReader reader = new FileReader("config.json");
            Gson gson = new Gson();
            config = gson.fromJson(reader, Config.class);
            if (config.getSaveFilePath() != null) {
                return config.getSaveFilePath();
            }
        } catch (IOException i) {
            return null;
        }
        return null;
    }

    public Config() {
    }

    public Config(String saveFilePath, String version) {
        this.saveFilePath = saveFilePath;
        this.version = version;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
