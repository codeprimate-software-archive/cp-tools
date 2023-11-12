
package com.cp.tools.sqlloader.io.parser.support;

import com.cp.tools.sqlloader.io.parser.AbstractInputParserTestCase;
import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import static com.cp.tools.sqlloader.test.TestUtil.ReturnListAction.returnList;
import com.cp.common.enums.State;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.DateUtil;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.RecordTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jmock.Expectations;

public class XlsInputParserTest extends AbstractInputParserTestCase {

  private static final String XLS_FILE = "C:/Projects/MyProject/etc/tmp/testSqlLoaderData.xls";

  public XlsInputParserTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(XlsInputParserTest.class);
    return suite;
  }

  protected XlsInputParser getInputParser() {
    return new XlsInputParser();
  }

  public void testParse() throws Exception {
    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class, "address");

    final Object[][] columnsMetaData = {
      { "address_id", "addressId", Integer.class },
      { "street_1", "street1", String.class },
      { "street_2", "street2", String.class },
      { "city", "city", String.class },
      { "county", "county", String.class },
      { "state", "state", State.class },
      { "zip", "zip", String.class },
      { "created_by", "createdBy", String.class },
      { "created_date", "createdDate", Calendar.class },
      { "edited_by", "editedBy", String.class },
      { "edited_date", "editedDate", Calendar.class }
    };

    final List<ColumnDefinition> mockColumnDefinitions = new ArrayList<ColumnDefinition>(11);

    for (final Object[] columnMetaData : columnsMetaData) {
      mockColumnDefinitions.add(getColumnDefinition(columnMetaData[0].toString(), columnMetaData[1].toString(),
        (Class) columnMetaData[2]));
    }

    context.checking(new Expectations() {{
      one(mockTableDefinition).getColumnDefinitionCount();
      will(returnValue(mockColumnDefinitions.size()));
      one(mockTableDefinition).getColumnDefinitions();
      will(returnList(mockColumnDefinitions));

      int columnIndex = 0;

      for (final ColumnDefinition columnDefinition : mockColumnDefinitions) {
        allowing(mockTableDefinition).getColumnDefinitionAtIndex(columnIndex++);
        will(returnValue(columnDefinition));
      }
    }});

    RecordTable actualRecordTable = null;

    try {
      actualRecordTable = getInputParser().parse(mockTableDefinition, new File(XLS_FILE));
    }
    catch (Exception e) {
      fail("Calling parse on input file (" + XLS_FILE + ") threw an unexpected Exception (" + e.getMessage() + ")!");
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
      { 5, "1010 NW Parkrose St.", "PO BOX 14441", "Portland", "Multnomah", State.OREGON, "55555", "tool", DateUtil.getCalendar(2008, Calendar.JULY, 18), "individual", DateUtil.getCalendar(2008, Calendar.JULY, 21) },
      { 6, "81670 Benton Lane", null, "Beaverton", "Washington", State.OREGON, "12131", "process", DateUtil.getCalendar(2008, Calendar.JULY, 17), "individual", DateUtil.getCalendar(2008, Calendar.JULY, 21) }
    };

    final Object[][] actualData = actualRecordTable.toTabular();

    assertNotNull(actualData);
    TestUtil.assertEquals(expectedData, actualData);

    context.assertIsSatisfied();
  }

  public void testParseWithNonExistingXlsFile() throws Exception {
    RecordTable recordTable = null;

    assertNull(recordTable);

    try {
      recordTable = getInputParser().parse(context.mock(TableDefinition.class, "anotherMockTable"), new File("C:/tmp/nonExistingFile.xls"));
      fail("Calling parse with a non-existing XLS file should have thrown a FileNotFoundException!");
    }
    catch (FileNotFoundException e) {
      assertEquals("The XLS file (C:\\tmp\\nonExistingFile.xls) could not be found in the file system!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a non-existing XLS file threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(recordTable);
  }

  public void testParseWithNullInputFile() throws Exception {
    RecordTable recordTable = null;

    assertNull(recordTable);

    try {
      recordTable = getInputParser().parse(context.mock(TableDefinition.class, "mockTable"), null);
      fail("Calling parse with a null input file should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The XLS input file cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a null input file threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(recordTable);
  }

  public void testParseWithNullTableDefinition() throws Exception {
    RecordTable recordTable = null;

    assertNull(recordTable);

    try {
      recordTable = getInputParser().parse(null, new File(XLS_FILE));
      fail("Calling parse with a null TableDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The table definition describing the format of the input file cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a null TableDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(recordTable);
  }

}
