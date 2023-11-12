
package com.cp.tools.sqlloader.dao;

import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.sql.SqlDialectFactory;
import com.cp.common.lang.Assert;
import com.cp.common.util.record.RecordTable;
import java.util.Stack;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultLoaderDaoTest extends TestCase {

  private static final Mockery context = new Mockery();

  private static final String EXPECTED_SQL = "INSERT INTO person (?, ?, ?)";

  private static final Stack methodCallStack = new Stack();

  public DefaultLoaderDaoTest(final String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    methodCallStack.clear();
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultLoaderDaoTest.class);
    return suite;
  }

  private void assertMethodCallStack(final String... methodCalls) {
    for (final String methodCall : methodCalls) {
      assertEquals(methodCall, methodCallStack.pop());
    }
  }

  protected LoaderDao getLoaderDao(final TableDefinition tableDefinition, final RecordTable recordTable) {
    return new TestLoaderDao(tableDefinition, recordTable);
  }

  public void testPersist() throws Exception {
    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class);
    final RecordTable mockRecordTable = context.mock(RecordTable.class);
    final LoaderDao dao = getLoaderDao(mockTableDefinition, mockRecordTable);

    assertNotNull(dao);

    // call the persist method
    dao.persist(mockTableDefinition, mockRecordTable);

    context.assertIsSatisfied();
    assertMethodCallStack("batchUpdate", "getBatchPreparedStatementSetter", "getSqlDialectFactory");
  }

  protected static final class TestLoaderDao extends DefaultLoaderDao {

    private final RecordTable recordTable;
    private final TableDefinition tableDefinition;

    public TestLoaderDao(final TableDefinition tableDefinition, final RecordTable recordTable) {
      Assert.notNull(tableDefinition, "The table definition cannot be null!");
      Assert.notNull(recordTable, "The record table cannot be null!");
      this.tableDefinition = tableDefinition;
      this.recordTable = recordTable;
      setJdbcTemplate(createMockJdbcTemplate());
    }

    private JdbcTemplate createMockJdbcTemplate() {
      return  new JdbcTemplate() {

        @Override
        public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter pss) throws DataAccessException {
          assertEquals(EXPECTED_SQL, sql);
          assertNotNull(pss);
          methodCallStack.push("batchUpdate");
          return new int[0];
        }
      };
    }

    @Override
    protected BatchPreparedStatementSetter getBatchPreparedStatementSetter(final TableDefinition tableDefinition,
                                                                           final RecordTable data)
    {
      final BatchPreparedStatementSetter setter = context.mock(BatchPreparedStatementSetter.class);
      assertSame(this.tableDefinition, tableDefinition);
      assertSame(this.recordTable, data);
      methodCallStack.push("getBatchPreparedStatementSetter");
      return setter;
    }

    @Override
    protected SqlDialectFactory getSqlDialectFactory() {
      final SqlDialectFactory factory = context.mock(SqlDialectFactory.class);

      context.checking(new Expectations() {{
        one(factory).createInsertSql(tableDefinition);
        will(returnValue(EXPECTED_SQL));
      }});

      methodCallStack.push("getSqlDialectFactory");
      return factory;
    }

  }

}
