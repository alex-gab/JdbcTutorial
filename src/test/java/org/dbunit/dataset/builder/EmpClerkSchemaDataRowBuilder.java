//package org.dbunit.dataset.builder;
//
//import java.util.Date;
//
//public final class EmpClerkSchemaDataRowBuilder extends AbstractSchemaDataRowBuilder<SchemaDataSetBuilder> {
//    private static final ColumnSpec<String> ENAME = ColumnSpec.newColumn("ENAME");
//    private static final ColumnSpec<Date> HIREDATE = ColumnSpec.newColumn("HIREDATE");
//    private static final ColumnSpec<Integer> SAL = ColumnSpec.newColumn("SAL");
//
//    EmpClerkSchemaDataRowBuilder(final SchemaDataSetBuilder schemaDataSetBuilder, final String tableName) {
//        super(schemaDataSetBuilder, tableName);
//    }
//
//    public EmpClerkSchemaDataRowBuilder ENAME(final String ENAME) {
//        dataRowBuilder.with(EmpClerkSchemaDataRowBuilder.ENAME, ENAME);
//        return this;
//    }
//
//    public EmpClerkSchemaDataRowBuilder HIREDATE(final Date HIREDATE) {
//        dataRowBuilder.with(EmpClerkSchemaDataRowBuilder.HIREDATE, HIREDATE);
//        return this;
//    }
//
//    public EmpClerkSchemaDataRowBuilder SAL(final Integer SAL) {
//        dataRowBuilder.with(EmpClerkSchemaDataRowBuilder.SAL, SAL);
//        return this;
//    }
//}
