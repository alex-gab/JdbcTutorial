package com.alex.database;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.builder.DataSetBuilder;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.GregorianCalendar;

import static com.alex.database.PropertiesHolder.*;
import static java.nio.charset.Charset.forName;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.JANUARY;
import static org.dbunit.builder.EmployeeRow.EmployeeRowBuilder.anEmployeeRow;

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
        final IDataSet dataSet = readGeneratedBuilderDataSet();
        cleanlyInsert(dataSet);
    }

    @Test
    public void testInsertUser() throws Exception {
        final UserRepository userRepository = new UserRepositoryImpl();
        userRepository.insertUser("VASILE", "1986-02-02", 2500);

        final IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
        final ITable actualTable = actualDataSet.getTable("Employee");

        final FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
        IDataSet expectedDataSet = flatXmlDataSetBuilder.build(new File(getPath("/expectedDataSet.xml")));
        ITable expectedTable = expectedDataSet.getTable("Employee");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    private IDataSet readGeneratedBuilderDataSet() throws DataSetException {
        final DataSetBuilder dataSetBuilder = DataSetBuilder.aDataSet().
                withRow(anEmployeeRow().
                        name("JAMES").
                        hiredate(new GregorianCalendar(1981, DECEMBER, 3).getTime()).
                        salary(950).
                        build()).
                withRow(anEmployeeRow().
                        name("SMITH").
                        hiredate(new GregorianCalendar(1980, DECEMBER, 17).getTime()).
                        salary(800).
                        build()).
                withRow(anEmployeeRow().
                        name("ADAMS").
                        hiredate(new GregorianCalendar(1983, JANUARY, 12).getTime()).
                        salary(1100).
                        build()).
                withRow(anEmployeeRow().
                        name("MILLER").
                        hiredate(new GregorianCalendar(1982, JANUARY, 23).getTime()).
                        salary(1900).
                        build());

        return dataSetBuilder.build();
    }

    private void cleanlyInsert(IDataSet dataSet) throws Exception {
        databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    private static String getPath(String filname) {
        return UserRepositoryImplTest.class.getResource(filname).getPath();
    }
}