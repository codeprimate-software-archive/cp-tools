
package com.cp.tools.sqlloader.mapping.parser;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.ConstraintDefinition;
import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import com.cp.tools.sqlloader.mapping.beans.MappingBeanFactory;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dom4j.Element;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class DefaultMappingParserTest extends TestCase {

  private static final String SQL_LOADER_MAPPING_FILE_PATHNAME = "c:/Projects/MyProject/etc/tmp/testSqlLoaderMapping.xml";

  private final Mockery context = new Mockery();

  public DefaultMappingParserTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultMappingParserTest.class);
    return suite;
  }

  protected void assertEquals(final DataSourceDefinition expectedDataSourceDefinition, final DataSourceDefinition actualDataSourceDefinition) {
    assertEquals(expectedDataSourceDefinition.getJdbcDriver(), actualDataSourceDefinition.getJdbcDriver());
    assertEquals(expectedDataSourceDefinition.getJdbcPassword(), actualDataSourceDefinition.getJdbcPassword());
    assertEquals(expectedDataSourceDefinition.getJdbcUrl(), actualDataSourceDefinition.getJdbcUrl());
    assertEquals(expectedDataSourceDefinition.getJdbcUsername(), actualDataSourceDefinition.getJdbcUsername());
    assertEquals(expectedDataSourceDefinition.getName(), actualDataSourceDefinition.getName());

    final List<TableDefinition> expectedTableDefinitions = expectedDataSourceDefinition.getTableDefinitions();
    final List<TableDefinition> actualTableDefinitions = actualDataSourceDefinition.getTableDefinitions();

    assertEquals(expectedTableDefinitions.size(), actualTableDefinitions.size());

    int index = 0;

    for (final TableDefinition expectedTableDefinition : expectedTableDefinitions) {
      assertEquals(expectedTableDefinition, actualTableDefinitions.get(index++));
    }
  }

  protected void assertEquals(final TableDefinition expectedTableDefinition, final TableDefinition actualTableDefinition) {
    assertEquals(expectedTableDefinition.getName(), actualTableDefinition.getName());
    assertEquals(expectedTableDefinition.getSource(), actualTableDefinition.getSource());

    final List<ColumnDefinition> expectedColumnDefinitions = expectedTableDefinition.getColumnDefinitions();
    final List<ColumnDefinition> actualColumnDefinitions = actualTableDefinition.getColumnDefinitions();

    assertEquals(expectedColumnDefinitions.size(), actualColumnDefinitions.size());

    int index = 0;

    for (final ColumnDefinition expectedColumnDefinition : expectedColumnDefinitions) {
      assertEquals(expectedColumnDefinition, actualColumnDefinitions.get(index++));
    }
  }

  protected void assertEquals(final ColumnDefinition expectedColumnDefinition, final ColumnDefinition actualColumnDefinition) {
    assertEquals(expectedColumnDefinition.getEndIndex(), actualColumnDefinition.getEndIndex());
    assertEquals(expectedColumnDefinition.getName(), actualColumnDefinition.getName());
    assertEquals(expectedColumnDefinition.getProperty(), actualColumnDefinition.getProperty());
    assertEquals(expectedColumnDefinition.getStartIndex(), actualColumnDefinition.getStartIndex());
    assertEquals(expectedColumnDefinition.getType(), actualColumnDefinition.getType());

    final List<ConstraintDefinition> expectedConstraintDefinitions = expectedColumnDefinition.getConstraintDefinitions();
    final List<ConstraintDefinition> actualConstraintDefinitions = actualColumnDefinition.getConstraintDefinitions();

    assertEquals(expectedConstraintDefinitions.size(), actualConstraintDefinitions.size());

    int index = 0;

    for (final ConstraintDefinition expectedConstraintDefinition : expectedConstraintDefinitions) {
      assertEquals(expectedConstraintDefinition, actualConstraintDefinitions.get(index++));
    }
  }

  protected ColumnDefinition getColumnDefinition(final String name,
                                                 final String property,
                                                 final String type,
                                                 final ConstraintDefinition... constraintDefinitions)
    throws ClassNotFoundException
  {
    return getColumnDefinition(name, -1, property, -1, type, constraintDefinitions);
  }

  protected ColumnDefinition getColumnDefinition(final String name,
                                                 final int startIndex,
                                                 final String property,
                                                 final int endIndex,
                                                 final String type,
                                                 final ConstraintDefinition... constraintDefinitions)
    throws ClassNotFoundException
  {
    final ColumnDefinition column = MappingBeanFactory.getColumnDefinition(name);
    column.setEndIndex(endIndex);
    column.setProperty(property);
    column.setStartIndex(startIndex);
    column.setType(type);

    for (final ConstraintDefinition constraint : constraintDefinitions) {
      column.add(constraint);
    }

    return column;
  }

  protected ConstraintDefinition getConstraintDefinition(final String name) {
    return getConstraintDefinition(name, null, null, null);
  }

  protected ConstraintDefinition getConstraintDefinition(final String name,
                                                         final String min,
                                                         final String max,
                                                         final String pattern)
  {
    final ConstraintDefinition constraint = MappingBeanFactory.getConstraintDefinition(name);
    constraint.setMax(max);
    constraint.setMin(min);
    constraint.setPattern(pattern);
    return constraint;
  }

  protected Element getDataSourceElement(final DataSourceDefinition dataSourceDefinition) {
    final Element mockDataSourceElement = context.mock(Element.class, dataSourceDefinition.getName());

    final Collection<Element> tableElementCollection = new ArrayList<Element>(dataSourceDefinition.getTableDefinitionCount());

    for (final TableDefinition tableDefinition : dataSourceDefinition.getTableDefinitions()) {
      tableElementCollection.add(getTableElement(tableDefinition));
    }

    context.checking(new Expectations() {{
      one(mockDataSourceElement).attributeValue(DefaultMappingParser.JDBC_DRIVER_ATTRIBUTE);
      will(returnValue(dataSourceDefinition.getJdbcDriver()));
      one(mockDataSourceElement).attributeValue(DefaultMappingParser.JDBC_PASSWORD_ATTRIBUTE);
      will(returnValue(dataSourceDefinition.getJdbcPassword()));
      one(mockDataSourceElement).attributeValue(DefaultMappingParser.JDBC_URL_ATTRIBUTE);
      will(returnValue(dataSourceDefinition.getJdbcUrl()));
      one(mockDataSourceElement).attributeValue(DefaultMappingParser.JDBC_USERNAME_ATTRIBUTE);
      will(returnValue(dataSourceDefinition.getJdbcUsername()));
      one(mockDataSourceElement).attributeValue(DefaultMappingParser.NAME_ATTRIBUTE);
      will(returnValue(dataSourceDefinition.getName()));
      one(mockDataSourceElement).elementIterator(DefaultMappingParser.TABLE_TAG);
      will(returnIterator(tableElementCollection));
    }});

    return mockDataSourceElement;
  }

  protected Element getTableElement(final TableDefinition tableDefinition) {
    final Element mockTableElement = context.mock(Element.class, tableDefinition.getName());

    final Collection<Element> columnElementCollection = new ArrayList<Element>(tableDefinition.getColumnDefinitionCount());

    for (final ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitions()) {
      columnElementCollection.add(getColumnElement(columnDefinition));
    }

    context.checking(new Expectations() {{
      one(mockTableElement).attributeValue(DefaultMappingParser.NAME_ATTRIBUTE);
      will(returnValue(tableDefinition.getName()));
      one(mockTableElement).attributeValue(DefaultMappingParser.SOURCE_ATTRIBUTE);
      will(returnValue(tableDefinition.getSource().getAbsolutePath()));
      one(mockTableElement).elementIterator(DefaultMappingParser.COLUMN_TAG);
      will(returnIterator(columnElementCollection));
    }});

    return mockTableElement;
  }

  protected Element getColumnElement(final ColumnDefinition columnDefinition) {
    final Element mockColumnElement = context.mock(Element.class, columnDefinition.getName());

    final Collection<Element> constraintElementCollection = new ArrayList<Element>(columnDefinition.getConstraintDefinitionCount());

    for (final ConstraintDefinition constraintDefinition : columnDefinition.getConstraintDefinitions()) {
      constraintElementCollection.add(getConstraintElement(constraintDefinition));
    }

    context.checking(new Expectations() {{
      one(mockColumnElement).attributeValue(DefaultMappingParser.END_INDEX_ATTRIBUTE);
      will(returnValue(String.valueOf(columnDefinition.getEndIndex())));
      one(mockColumnElement).attributeValue(DefaultMappingParser.NAME_ATTRIBUTE);
      will(returnValue(columnDefinition.getName()));
      one(mockColumnElement).attributeValue(DefaultMappingParser.PROPERTY_ATTRIBUTE);
      will(returnValue(columnDefinition.getProperty()));
      one(mockColumnElement).attributeValue(DefaultMappingParser.START_INDEX_ATTRIBUTE);
      will(returnValue(String.valueOf(columnDefinition.getStartIndex())));
      one(mockColumnElement).attributeValue(DefaultMappingParser.TYPE_ATTRIBUTE);
      will(returnValue(columnDefinition.getType().getName()));
      one(mockColumnElement).elementIterator(DefaultMappingParser.CONSTRAINT_TAG);
      will(returnIterator(constraintElementCollection));
    }});

    return mockColumnElement;
  }

  protected Element getConstraintElement(final ConstraintDefinition constraintDefinition) {
    final Element mockConstraintElement = context.mock(Element.class, constraintDefinition.getName());

    context.checking(new Expectations() {{
      one(mockConstraintElement).attributeValue(DefaultMappingParser.MAX_ATTRIBUTE);
      will(returnValue(constraintDefinition.getMax()));
      one(mockConstraintElement).attributeValue(DefaultMappingParser.MIN_ATTRIBUTE);
      will(returnValue(constraintDefinition.getMin()));
      one(mockConstraintElement).attributeValue(DefaultMappingParser.NAME_ATTRIBUTE);
      will(returnValue(constraintDefinition.getName()));
      one(mockConstraintElement).attributeValue(DefaultMappingParser.PATTERN_ATTRIBUTE);
      will(returnValue(constraintDefinition.getPattern()));
    }});

    return mockConstraintElement;
  }

  protected DefaultMappingParser getMappingParser() {
    return new DefaultMappingParser();
  }

  public void testParse() throws Exception {
    final DataSourceDefinition expectedDataSourceDefinition = MappingBeanFactory.getDataSourceDefinition("flhk");
    expectedDataSourceDefinition.setJdbcDriver("oracle.jdbc.driver.OracleDriver");
    expectedDataSourceDefinition.setJdbcPassword("flhk_rodan_blumj");
    expectedDataSourceDefinition.setJdbcUrl("jdbc:oracle:thin:@ecomr01:1521:FHKDEV1");
    expectedDataSourceDefinition.setJdbcUsername("flhk_rodan_blumj");

    final TableDefinition expectedTable = MappingBeanFactory.getTableDefinition("stage_address");
    expectedTable.setSource(new File("c:/Projects/MyProject/etc/tmp/testSqlLoaderData.csv"));
    expectedDataSourceDefinition.add(expectedTable);

    expectedTable.add(getColumnDefinition("stage_address_id", 1, "id", 3, "java.lang.Integer"));
    expectedTable.add(getColumnDefinition("address_1", 4, "street1", 53, "java.lang.String"));
    expectedTable.add(getColumnDefinition("address_2", 54, "street2", 79, "java.lang.String"));
    expectedTable.add(getColumnDefinition("city", 80, "city", 99, "java.lang.String"));
    expectedTable.add(getColumnDefinition("county", 100, "county", 119, "java.lang.String"));
    expectedTable.add(getColumnDefinition("state", 120, "state", 121, "java.lang.String"));
    expectedTable.add(getColumnDefinition("zip", 122, "zip", 126, "java.lang.String"));
    expectedTable.add(getColumnDefinition("created_by", 127, "createdBy", 151, "java.lang.String"));
    expectedTable.add(getColumnDefinition("created_date", 152, "createdDate", 176, "java.util.Calendar"));
    expectedTable.add(getColumnDefinition("edited_by", 177, "editedBy", 201, "java.lang.String"));
    expectedTable.add(getColumnDefinition("edited_date", 202, "editedDate", 226, "java.util.Calendar"));

    final List<DataSourceDefinition> actualDataSourceDefinitionList =
      getMappingParser().parse(new File(SQL_LOADER_MAPPING_FILE_PATHNAME));

    assertNotNull(actualDataSourceDefinitionList);
    assertFalse(actualDataSourceDefinitionList.isEmpty());
    assertEquals(1, actualDataSourceDefinitionList.size());
    assertEquals(expectedDataSourceDefinition, actualDataSourceDefinitionList.get(0));
  }

  public void testParseDataSourceElement() throws Exception {
    final DataSourceDefinition expectedDataSourceDefinition = MappingBeanFactory.getDataSourceDefinition("myDataSource");
    expectedDataSourceDefinition.setJdbcDriver("com.mycompany.mypackage.jdbc.driver.MockDriver");
    expectedDataSourceDefinition.setJdbcPassword("123");
    expectedDataSourceDefinition.setJdbcUrl("mock.jdbc.driver@myServer:1521:myDb");
    expectedDataSourceDefinition.setJdbcUsername("root");

    final TableDefinition address = MappingBeanFactory.getTableDefinition("address");
    address.setSource(new File(System.getProperty("java.io.tmpdir") + File.separator + "address.fxw"));
    expectedDataSourceDefinition.add(address);

    address.add(getColumnDefinition("address_id", 1, "addressId", 3, "java.lang.Integer",
      getConstraintDefinition("notNullAddressId"), getConstraintDefinition("uniqueAddressId")));

    address.add(getColumnDefinition("street", 4, "street", 23, "java.lang.String",
      getConstraintDefinition("notNullStreet")));

    address.add(getColumnDefinition("city", 24, "city", 43, "java.lang.String",
      getConstraintDefinition("notNullCity")));

    address.add(getColumnDefinition("state", 45, "state", 47, "com.cp.common.enums.State",
      getConstraintDefinition("notNullState")));

    address.add(getColumnDefinition("zip", 48, "zipCode", 52, "java.lang.String",
      getConstraintDefinition("notNullZip")));

    final Element mockDataSourceElement = getDataSourceElement(expectedDataSourceDefinition);

    final DataSourceDefinition actualDataSourceDefinition = getMappingParser().parseDataSourceElement(mockDataSourceElement);

    assertNotNull(actualDataSourceDefinition);
    assertEquals(expectedDataSourceDefinition, actualDataSourceDefinition);

    context.assertIsSatisfied();
  }

  public void testParseTableElement() throws Exception {
    final TableDefinition expectedTableDefinition = MappingBeanFactory.getTableDefinition("personTable");
    expectedTableDefinition.setSource(new File(System.getProperty("java.io.tmpdir") + File.separator + "person.csv"));

    expectedTableDefinition.add(getColumnDefinition("person_id", "personId", "java.lang.Integer",
      getConstraintDefinition("notNullPersonId"), getConstraintDefinition("uniquePersonId")));

    expectedTableDefinition.add(getColumnDefinition("first_name", "firstName", "java.lang.String",
      getConstraintDefinition("notNullFirstName")));

    expectedTableDefinition.add(getColumnDefinition("last_name", "lastName", "java.lang.String",
      getConstraintDefinition("notNullLastName")));

    expectedTableDefinition.add(getColumnDefinition("ssn", "ssn", "java.lang.String",
      getConstraintDefinition("uniqueSsn")));

    final Element mockTableElement = getTableElement(expectedTableDefinition);

    final TableDefinition actualTableDefinition = getMappingParser().parseTableElement(mockTableElement);

    assertNotNull(actualTableDefinition);
    assertEquals(expectedTableDefinition, actualTableDefinition);

    context.assertIsSatisfied();
  }

  public void testParseColumnElement() throws Exception {
    final ColumnDefinition expectedColumnDefinition = MappingBeanFactory.getColumnDefinition("date_of_birth");
    expectedColumnDefinition.setEndIndex(50);
    expectedColumnDefinition.setProperty("dob");
    expectedColumnDefinition.setStartIndex(2);
    expectedColumnDefinition.setType(Calendar.class);

    final ConstraintDefinition notNullDob = MappingBeanFactory.getConstraintDefinition("notNullDob");
    expectedColumnDefinition.add(notNullDob);

    final ConstraintDefinition minMaxDob = MappingBeanFactory.getConstraintDefinition("minMaxDob");
    minMaxDob.setMax("12/31/2074");
    minMaxDob.setMin("01/01/1974");
    minMaxDob.setPattern("MM/dd/yyyy");
    expectedColumnDefinition.add(minMaxDob);

    final Element mockColumnElement = getColumnElement(expectedColumnDefinition);

    final ColumnDefinition actualColumnDefinition = getMappingParser().parseColumnElement(mockColumnElement);

    assertNotNull(actualColumnDefinition);
    assertEquals(expectedColumnDefinition, actualColumnDefinition);

    context.assertIsSatisfied();
  }

  public void testParseConstraintElement() throws Exception {
    final ConstraintDefinition expectedConstraintDefinition = MappingBeanFactory.getConstraintDefinition("myConstraint");
    expectedConstraintDefinition.setMax("12/31/2008");
    expectedConstraintDefinition.setMin("01/01/2008");
    expectedConstraintDefinition.setPattern("MM/dd/yyyy hh:mm:ss a");

    final Element mockConstraintElement = getConstraintElement(expectedConstraintDefinition);

    final ConstraintDefinition actualConstraintDefinition = getMappingParser().parseConstraintElement(mockConstraintElement);

    assertNotNull(actualConstraintDefinition);
    assertEquals(expectedConstraintDefinition, actualConstraintDefinition);

    context.assertIsSatisfied();
  }

}
