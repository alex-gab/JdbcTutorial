package com.alex.database;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.builder.SchemaDataSetBuilder;
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
        final ITable actualTable = actualDataSet.getTable("EMP_CLERK");

        final FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
        IDataSet expectedDataSet = flatXmlDataSetBuilder.build(new File(getPath("/expectedDataSet.xml")));
        ITable expectedTable = expectedDataSet.getTable("EMP_CLERK");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    private IDataSet readGeneratedBuilderDataSet() throws DataSetException {
        final SchemaDataSetBuilder dataSetBuilder = new SchemaDataSetBuilder();
        dataSetBuilder.newEMP_CLERKRow().ENAME("JAMES").HIREDATE(new GregorianCalendar(1981, DECEMBER, 3).getTime()).SAL(950).add().
                newEMP_CLERKRow().ENAME("SMITH").HIREDATE(new GregorianCalendar(1980, DECEMBER, 17).getTime()).SAL(800).add().
                newEMP_CLERKRow().ENAME("ADAMS").HIREDATE(new GregorianCalendar(1983, JANUARY, 12).getTime()).SAL(1100).add().
                newEMP_CLERKRow().ENAME("MILLER").HIREDATE(new GregorianCalendar(1982, JANUARY, 23).getTime()).SAL(1900).add();
        return dataSetBuilder.build();
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