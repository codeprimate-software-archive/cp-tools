
package com.cp.tools.sqlloader.mapping.util;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import static com.cp.tools.sqlloader.test.TestUtil.ReturnListAction.returnList;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.RecordTable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class TableDefinitionConverterTest extends TestCase {

  private Mockery context = new Mockery();

  public TableDefinitionConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(TableDefinitionConverterTest.class);
    return suite;
  }

  protected ColumnDefinition getColumnDefinition(final String property, final Class type) {
    final ColumnDefinition mockColumnDefinition = context.mock(ColumnDefinition.class, property);

    context.checking(new Expectations() {{
      one(mockColumnDefinition).getProperty();
      will(returnValue(property));
      one(mockColumnDefinition).getType();
      will(returnValue(type));
    }});

    return mockColumnDefinition;
  }

  public void testToRecordTable() throws Exception {
    final Object[][] columnsMetaData = {
      { "person_id", Integer.class },
      { "first_name", String.class },
      { "last_name", String.class },
      { "date_of_birth", Calendar.class },
      { "ssn", String.class }
    };

    final List<ColumnDefinition> mockColumnDefinitions = new ArrayList<ColumnDefinition>(5);

    for (final Object[] columnMetaData : columnsMetaData) {
      mockColumnDefinitions.add(getColumnDefinition(columnMetaData[0].toString(), (Class) columnMetaData[1]));
    }

    assertEquals(5, mockColumnDefinitions.size());

    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class, "person");

    context.checking(new Expectations() {{
      one(mockTableDefinition).getColumnDefinitionCount();
      will(returnValue(mockColumnDefinitions.size()));
      one(mockTableDefinition).getColumnDefinitions();
      will(returnList(mockColumnDefinitions));
    }});

    final RecordTable actualRecordTable = TableDefinitionConverter.toRecordTable(mockTableDefinition);

    assertNotNull(actualRecordTable);
    assertEquals(mockColumnDefinitions.size(), actualRecordTable.columnCount());
    assertEquals(0, actualRecordTable.rowCount());

    int rowIndex = 0;

    for (final Column column : actualRecordTable.getColumns()) {
      assertEquals(columnsMetaData[rowIndex][0], column.getName());
      assertEquals(columnsMetaData[rowIndex++][1], column.getType());
    }

    context.assertIsSatisfied();
  }

  public void testGetColumnsThrowsNullPointerException() throws Exception {
    Column[] columns = null;

    assertNull(columns);

    try {
      columns = TableDefinitionConverter.getColumns(null);
      fail("Calling getColumns with a null TableDefinition should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The table definition cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getColumns with a null TableDefinition threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(columns);
  }

}
