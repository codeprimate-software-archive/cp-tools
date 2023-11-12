
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jmock.Expectations;

public class CsvInputParserTest extends AbstractInputParserTestCase {

  private static final String CSV_FILE = "c:/Projects/MyProject/etc/tmp/testSqlLoaderData.csv";

  public CsvInputParserTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CsvInputParserTest.class);
    return suite;
  }

  @Override
  protected CsvInputParser getInputParser() {
    return new CsvInputParser();
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

    final List<ColumnDefinition> columnDefinitionList = new ArrayList<ColumnDefinition>(13);

    for (final Object[] columnMetaData : columnsMetaData) {
      columnDefinitionList.add(getColumnDefinition(columnMetaData[0].toString(), columnMetaData[1].toString(),
        (Class) columnMetaData[2]));
    }

    context.checking(new Expectations() {{
      exactly(3).of(mockTableDefinition).getColumnDefinitionCount();
      will(returnValue(columnDefinitionList.size()));
      one(mockTableDefinition).getColumnDefinitions();
      will(returnList(columnDefinitionList));
      exactly(2).of(mockTableDefinition).getName();
      will(returnValue("address"));

      int columnIndex = 0;

      for (final ColumnDefinition columnDefinition : columnDefinitionList) {
        allowing(mockTableDefinition).getColumnDefinitionAtIndex(columnIndex++);
        will(returnValue(columnDefinition));
      }
    }});

    RecordTable actualRecordTable = null;

    try {
      actualRecordTable = getInputParser().parse(mockTableDefinition, new File(CSV_FILE));
    }
    catch (Exception e) {
      fail("Parsing the CSV file (" + CSV_FILE + ") using the CsvParser threw an unexpected Exception ("
        + e.getMessage() + ")!");
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
      { 1, "100 Main St.", "Apt. 22", "Portland", "Multnomah", State.OREGON, "12345", "root", DateUtil.getCalendar(2008, Calendar.JUNE, 25), "admin", DateUtil.getCalendar(2008, Calendar.JUNE, 25) },
      { 2, "2220 Pine Ave.", "Apt. 2212", "Gresham", "Multnomah", State.OREGON, "22345", "root", DateUtil.getCalendar(2008, Calendar.JUNE, 25), "admin", DateUtil.getCalendar(2008, Calendar.JUNE, 25) }
    };

    final Object[][] actualData = actualRecordTable.toTabular();

    assertNotNull(actualData);
    TestUtil.assertEquals(expectedData, actualData);

    context.assertIsSatisfied();
  }

}
