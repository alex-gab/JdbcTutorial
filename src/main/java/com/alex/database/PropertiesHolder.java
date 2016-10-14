package com.alex.database;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesHolder {
    public static final Properties properties = new Properties();

    static {
        try {
            properties.load(ConnectionHandler.class.getResourceAsStream("/database.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load properties", e);
        }
    }

    public static String getDriver() {
        return properties.getProperty("driver");
    }

    public static String getUrl() {
        return properties.getProperty("url");
    }

    public static String getUser() {
        return properties.getProperty("user");
    }

    public static String getPassword() {
        return properties.getProperty("password");
    }
}
