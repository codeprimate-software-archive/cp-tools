
package com.cp.tools.sqlloader.io.parser.support;

import com.cp.tools.sqlloader.io.parser.AbstractInputParserTestCase;
import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.mapping.util.ConstraintType;
import static com.cp.tools.sqlloader.test.TestUtil.ReturnListAction.returnList;
import com.cp.tools.sqlloader.validation.ConstraintValidationException;
import com.cp.common.enums.State;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.DateUtil;
import com.cp.common.util.SystemException;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordTable;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jmock.Expectations;

public class FixedWidthInputParserTest extends AbstractInputParserTestCase {

  private static final String FXW_FILE = "c:/Projects/MyProject/etc/tmp/testSqlLoaderData.fxw";

  public FixedWidthInputParserTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(FixedWidthInputParserTest.class);
    return suite;
  }

  @Override
  protected FixedWidthInputParser getInputParser() {
    return new FixedWidthInputParser();
  }

  public void testParse() throws Exception {
    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class, "address");

    final Object[][] columnsMetaData = {
      { "address_id", "addressId", Integer.class, 1, 3},
      { "street_1", "street1", String.class, 4, 53 },
      { "street_2", "street2", String.class, 54, 79 },
      { "city", "city", String.class, 80, 99 },
      { "county", "county", String.class, 100, 119 },
      { "state", "state", State.class, 120, 121 },
      { "zip", "zip", String.class, 122, 126 },
      { "created_by", "createdBy", String.class, 127, 151 },
      { "created_date", "createdDate", Calendar.class, 152, 176 },
      { "edited_by", "editedBy", String.class, 177, 201 },
      { "edited_date", "editedDate", Calendar.class, 202, 226 }
    };

    final List<ColumnDefinition> mockColumnDefinitions = new ArrayList<ColumnDefinition>(11);

    for (final Object[] columnMetaData : columnsMetaData) {
      mockColumnDefinitions.add(getColumnDefinition(columnMetaData[0].toString(), columnMetaData[1].toString(),
        (Class) columnMetaData[2], (Integer) columnMetaData[3], (Integer) columnMetaData[4]));
    }

    context.checking(new Expectations() {{
      one(mockTableDefinition).getColumnDefinitionCount();
      will(returnValue(mockColumnDefinitions.size()));
      exactly(3).of(mockTableDefinition).getColumnDefinitions();
      will(returnList(mockColumnDefinitions));
    }});

    RecordTable actualRecordTable = null;

    try {
      actualRecordTable = getInputParser().parse(mockTableDefinition, new File(FXW_FILE));
    }
    catch (Exception e) {
      fail("Calling parse on input file (" + FXW_FILE + ") threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(actualRecordTable);
    assertEquals(2, actualRecordTable.rowCount());

    final List<Column> actualColumns = actualRecordTable.getColumns();
    int columnIndex = 0;

    for (final Object[] columnMetaData : columnsMetaData) {
      assertEquals(columnMetaData[1], actualColumns.get(columnIndex).getName());
      assertEquals(columnMetaData[2], actualColumns.get(columnIndex++).getType());
    }

    final Object[][] expectedData = {
      { 3,  "2420 Palmer Ave.", "Suite 88", "Missoula", "Missoula", State.MONTANA, "12345", "ROOT", DateUtil.getCalendar(2008, Calendar.JULY, 17, 18, 0, 0, 0), "ADMIN", DateUtil.getCalendar(2008, Calendar.JULY, 17, 18, 30, 15, 0) },
      { 4, "15015 Lexington St.", "PO BOX 4144", "Salem", "Grant", State.OREGON, "12345", "ROOT", DateUtil.getCalendar(2008, Calendar.JULY, 17, 18, 0, 0, 0), "ADMIN", DateUtil.getCalendar(2008, Calendar.JULY, 17, 18, 30, 15, 0) }
    };

    final Object[][] actualData = actualRecordTable.toTabular();

    assertNotNull(actualData);
    TestUtil.assertEquals(expectedData, actualData);

    context.assertIsSatisfied();
  }

  public void testParseLine() throws Exception {
    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class, "person");

    final List<ColumnDefinition> mockColumnDefinitions = new ArrayList<ColumnDefinition>(3);
    mockColumnDefinitions.add(getColumnDefinition("person_id", "personId", Integer.class, 1, 2));
    mockColumnDefinitions.add(getColumnDefinition("first_name", "firstName", String.class, 3, 12));
    mockColumnDefinitions.add(getColumnDefinition("last_name", "lastName", String.class, 13, 22));

    context.checking(new Expectations() {{
      one(mockTableDefinition).getColumnDefinitions();
      will(returnList(mockColumnDefinitions));
    }});

    Record actualRecord = null;

    try {
      actualRecord = getInputParser().parseLine(mockTableDefinition, "1 Jon       Doe       ");
    }
    catch (Exception e) {
      fail("Calling parseLine with a valid TableDefinition and input line threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(actualRecord);
    assertEquals(3, actualRecord.size());
    assertTrue(actualRecord.containsField("personId"));
    assertTrue(actualRecord.containsField("firstName"));
    assertTrue(actualRecord.containsField("lastName"));
    assertEquals(1, actualRecord.get("personId"));
    assertEquals("Jon", actualRecord.get("firstName"));
    assertEquals("Doe", actualRecord.get("lastName"));

    context.assertIsSatisfied();
  }

  public void testParseLineThrowsSystemException() throws Exception {
    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class, "phoneNumber");

    final List<ColumnDefinition> mockColumnDefinitions = new ArrayList<ColumnDefinition>(4);
    mockColumnDefinitions.add(getColumnDefinition("area_code", "areaCode", String.class, 1, 3));
    mockColumnDefinitions.add(getColumnDefinition("prefix", "prefix", String.class, 4, 6,
      getConstraintDefinition(ConstraintType.NOT_EMPTY.getName())));
    mockColumnDefinitions.add(getColumnDefinition("suffix", "suffix", String.class, 7, 10,
      getConstraintDefinition(ConstraintType.NOT_EMPTY.getName())));
    mockColumnDefinitions.add(getColumnDefinition("extension", "extension", String.class, 11, 14));

    context.checking(new Expectations() {{
      one(mockTableDefinition).getColumnDefinitions();
      will(returnList(mockColumnDefinitions));
    }});

    Record record = null;

    assertNull(record);

    try {
      record = getInputParser().parseLine(mockTableDefinition, "503555    4444");
      fail("Calling parseLine with an empty suffix should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("Failed to parse input line (503555    4444)!", e.getMessage());
      assertTrue(e.getCause() instanceof ConstraintValidationException);
      assertEquals("Constraint validation failed!", e.getCause().getMessage());
    }
    catch (Exception e) {
      fail("alling parseLine with an empty suffix threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(record);

    context.assertIsSatisfied();
  }

  public void testParseLineWithEmptyInputLine() throws Exception {
    Record record = null;

    assertNull(record);

    try {
      record = getInputParser().parseLine(context.mock(TableDefinition.class, "mockTable"), " ");
      fail("Calling parseLine with an empty input line should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The input line to parse cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parseLine with an empty input line threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(record);
  }

  public void testParseLineWithNullInputLine() throws Exception {
    Record record = null;

    assertNull(record);

    try {
      record = getInputParser().parseLine(context.mock(TableDefinition.class, "anotherMockTable"), null);
      fail("Calling parseLine with a null input line should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The input line to parse cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parseLine with a null input line threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(record);
  }

  public void testParseLineWithNullTableDefinition() throws Exception {
    Record record = null;

    assertNull(record);

    try {
      record = getInputParser().parseLine(null, "1 Jon       Doe       ");
      fail("Calling parseLine with a null TableDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The table definition cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parseLine with a null TableDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(record);
  }

}
