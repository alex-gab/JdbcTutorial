package com.alex.database;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

public final class DatabaseExportSample {
    public static void main(String[] args) throws Exception {
        // database connection
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:oracle:thin:@//localhost:1521/XE", "user0", "user0");
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        // partial database export
        QueryDataSet partialDataSet = new QueryDataSet(connection);
        partialDataSet.addTable("EMP_CLERK", "SELECT * FROM EMP_CLERK");
        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("dataset.xml"));
    }
}
