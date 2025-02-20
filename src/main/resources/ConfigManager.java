package main.resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
