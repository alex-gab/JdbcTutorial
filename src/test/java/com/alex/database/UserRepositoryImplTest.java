package com.alex.database;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static com.alex.database.PropertiesHolder.*;
import static java.nio.charset.Charset.forName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserRepositoryImplTest {

    private static final String JDBC_DRIVER = getDriver();
    private static final String JDBC_URL = getUrl();
    private static final String USER = getUser();
    private static final String PASSWORD = getPassword();

    private IDatabaseTester databaseTester;

    @BeforeClass
    public static void createSchema() throws Exception {
        RunScript.execute(JDBC_URL, USER, PASSWORD, getPath("/schema.sql"), forName("UTF-8"), false);
    }

    @Before
    public void importDataSet() throws Exception {
        IDataSet dataSet = readDataSet();
        cleanlyInsert(dataSet);
    }

    @Test
    public void testInsertUser() throws Exception {
        final UserRepository userRepository = new UserRepositoryImpl();
        userRepository.insertUser("VASILE", "1986-02-02", 2500);

        final IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
        final ITable actualTable = actualDataSet.getTable("EMP_CLERK");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(getPath("/expectedDataSet.xml")));
        ITable expectedTable = expectedDataSet.getTable("EMP_CLERK");

        assertThat(actualTable, is(equalTo(expectedTable)));
    }

    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new File(getPath("/dataset.xml")));
    }

    private void cleanlyInsert(IDataSet dataSet) throws Exception {
        databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    private static String getPath(String filename) {
        return UserRepositoryImplTest.class.getResource(filename).getPath();
    }
}