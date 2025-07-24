package data_handling;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private String saveFilePath;
    private String version;

    public void writeSaveFilePath(String saveFilePath) {
        Config config = new Config();
        try {
            FileInputStream fileInputStream = new FileInputStream("config.json");
            Gson gson = new Gson();
            config = gson.fromJson(fileInputStream.toString(), Config.class);
        } catch (FileNotFoundException f) {
            config = new Config(saveFilePath, "0.0.1");
            Gson gson = new Gson();
            String gsonString;
            try {
                gsonString = gson.toJson(config);
            } catch (JsonIOException j) {

            }

        }
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
