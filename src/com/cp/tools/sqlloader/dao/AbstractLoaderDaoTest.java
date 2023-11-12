
package com.cp.tools.sqlloader.dao;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.DefaultColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.DefaultTableDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.mapping.util.TableDefinitionConverter;
import com.cp.common.util.DateUtil;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordAdapter;
import com.cp.common.util.record.RecordTable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

public class AbstractLoaderDaoTest extends TestCase {

  private Mockery context = new Mockery();

  public AbstractLoaderDaoTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractLoaderDaoTest.class);
    return suite;
  }

  protected ColumnDefinition getColumnDefinition(final String columnName,
                                                 final String property,
                                                 final Class type) {
    final ColumnDefinition columnDefinition = new DefaultColumnDefinition(columnName);
    columnDefinition.setProperty(property);
    columnDefinition.setType(type);
    return columnDefinition;
  }

  protected AbstractLoaderDao getLoaderDao() {
    return new TestLoaderDao();
  }

  protected Record getRecord(final List<Column> columns, final Object... data) {
    final Record record = AbstractRecordFactory.getInstance().getRecordInstance();
    int columnIndex = 0;

    for (final Column column : columns) {
      record.addField(column.getName(), data[columnIndex++]);
    }

    return new RecordAdapter(record, columns);
  }

  protected TableDefinition getTableDefinition(final String tableName, final ColumnDefinition... columnDefinitions) {
    final TableDefinition tableDefinition = new DefaultTableDefinition(tableName);

    for (final ColumnDefinition columnDefinition : columnDefinitions) {
      tableDefinition.add(columnDefinition);
    }

    return tableDefinition;
  }

  private void callSetValuesAndAssert(final BatchPreparedStatementSetter setter,
                                      final PreparedStatement ps,
                                      final Object[] rowData,
                                      final int rowIndex)
    throws SQLException
  {
    context.checking(new Expectations() {{
      one(ps).setObject(1, rowData[0]);
      one(ps).setObject(2, rowData[1]);
      one(ps).setObject(3, rowData[2]);
      one(ps).setObject(4, new Timestamp(((Calendar) rowData[3]).getTimeInMillis()));
      one(ps).setObject(5, rowData[4]);
    }});

    setter.setValues(ps, rowIndex);
    context.assertIsSatisfied();
  }

  public void testGetBatchPreparedStatementSetter() throws Exception {
    final ColumnDefinition[] mockColumnDefinitions = {
      getColumnDefinition("person_id", "personId", Integer.class),
      getColumnDefinition("first_name", "firstName", String.class),
      getColumnDefinition("last_name", "lastName", String.class),
      getColumnDefinition("date_of_birth", "dateOfBirth", Calendar.class),
      getColumnDefinition("ssn", "ssn", String.class)
    };

    final TableDefinition mockTableDefinition = getTableDefinition("person", mockColumnDefinitions);

    final RecordTable mockRecordTable = TableDefinitionConverter.toRecordTable(mockTableDefinition);
    final List<Column> mockColumns = mockRecordTable.getColumns();

    mockRecordTable.add(getRecord(mockColumns, 1, "Jon", "Doe", DateUtil.getCalendar(1971, Calendar.FEBRUARY, 14), "123-45-6789"));
    mockRecordTable.add(getRecord(mockColumns, 2, "Jane", "Doe", DateUtil.getCalendar(1977, Calendar.OCTOBER, 21), "333-22-4444"));
    mockRecordTable.add(getRecord(mockColumns, 3, "Pie", "Doe", DateUtil.getCalendar(1989, Calendar.DECEMBER, 3), "101-01-0101"));
    mockRecordTable.add(getRecord(mockColumns, 3, "Sour", "Doe", DateUtil.getCalendar(1993, Calendar.APRIL, 19), "100-10-0100"));

    assertEquals(4, mockRecordTable.rowCount());

    final AbstractLoaderDao dao = getLoaderDao();

    assertNotNull(dao);

    final BatchPreparedStatementSetter setter = dao.getBatchPreparedStatementSetter(mockTableDefinition, mockRecordTable);

    assertNotNull(setter);
    assertEquals(mockRecordTable.rowCount(), setter.getBatchSize());

    final PreparedStatement ps = context.mock(PreparedStatement.class);
    final Object[][] tableData = mockRecordTable.toTabular();
    int rowIndex = 0;

    for (final Object[] rowData : tableData) {
      callSetValuesAndAssert(setter, ps, rowData, rowIndex++);
    }
  }

  protected static final class TestLoaderDao extends AbstractLoaderDao {

    public void persist(final TableDefinition tableDefinition, final RecordTable data) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
