package com.alex.database;

import org.dbunit.Assertion;
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
//        final IDataSet dataSet = readBuilderDataSet();
//        final IDataSet dataSet = readGeneratedBuilderDataSet();
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

    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new File(getPath("/dataset.xml")));
    }

//    private IDataSet readBuilderDataSet() throws DataSetException {
//        ColumnSpec<String> ename = ColumnSpec.newColumn("ENAME");
//        ColumnSpec<Date> hiredate = ColumnSpec.newColumn("HIREDATE");
//        ColumnSpec<Integer> sal = ColumnSpec.newColumn("SAL");
//        final DataSetBuilder builder = new DataSetBuilder();
//        builder.newRow("EMP_CLERK").with(ename, "JAMES").with(hiredate, new GregorianCalendar(1981, DECEMBER, 3).getTime()).with(sal, 950).add();
//        builder.newRow("EMP_CLERK").with(ename, "SMITH").with(hiredate, new GregorianCalendar(1980, DECEMBER, 17).getTime()).with(sal, 800).add();
//        builder.newRow("EMP_CLERK").with(ename, "ADAMS").with(hiredate, new GregorianCalendar(1983, JANUARY, 12).getTime()).with(sal, 1100).add();
//        builder.newRow("EMP_CLERK").with(ename, "MILLER").with(hiredate, new GregorianCalendar(1982, JANUARY, 23).getTime()).with(sal, 1900).add();
//        return builder.build();
//    }
//
//    private IDataSet readGeneratedBuilderDataSet() throws DataSetException {
//        final SchemaDataSetBuilder dataSetBuilder = new SchemaDataSetBuilder();
//        dataSetBuilder.newEmpClerkRow().ENAME("JAMES").HIREDATE(new GregorianCalendar(1981, DECEMBER, 3).getTime()).SAL(950).add().
//                newEmpClerkRow().ENAME("SMITH").HIREDATE(new GregorianCalendar(1980, DECEMBER, 17).getTime()).SAL(800).add().
//                newEmpClerkRow().ENAME("ADAMS").HIREDATE(new GregorianCalendar(1983, JANUARY, 12).getTime()).SAL(1100).add().
//                newEmpClerkRow().ENAME("MILLER").HIREDATE(new GregorianCalendar(1982, JANUARY, 23).getTime()).SAL(1900).add();
//        return dataSetBuilder.build();
//    }

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