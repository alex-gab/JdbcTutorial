package com.alex.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.alex.database.PropertiesHolder.*;

public final class ConnectionHandler {
    public static Connection getConnection() {
        try {
            Class.forName(getDriver());
            return DriverManager.getConnection(getUrl(), getUser(), getPassword());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: unable to load driver class!", e);
        } catch (SQLException e) {
            throw new RuntimeException("Could not obtain a connection", e);
        }
    }
}
