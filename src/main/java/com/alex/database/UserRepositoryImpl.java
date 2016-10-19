package com.alex.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.String.format;

public final class UserRepositoryImpl implements UserRepository {
    public void insertUser(String name, String hireDate, int salary) {
        final Connection connection = ConnectionHandler.getConnection();
        try {
            final Statement statement;
            statement = connection.createStatement();

            final String sql = format("INSERT INTO employee " +
                    "VALUES ('%s', '%s', %d)", name, hireDate, salary);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Could not execute insert statement", e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Could not close the connection", e);
            }
        }
    }
}
