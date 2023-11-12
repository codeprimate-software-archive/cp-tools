
package com.cp.tools.sqlloader.sql.support;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.sql.SqlDialectFactory;
import com.cp.common.util.CollectionUtil;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class AnsiSqlDialectFactoryTest extends TestCase {

  private final Mockery context = new Mockery();

  public AnsiSqlDialectFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AnsiSqlDialectFactoryTest.class);
    return suite;
  }

  protected ColumnDefinition getColumnDefinition(final String name,
                                                 final String columnName,
                                                 final String propertyName) {
    final ColumnDefinition mockColumnDefinition = context.mock(ColumnDefinition.class, name);

    context.checking(new Expectations() {{
      one(mockColumnDefinition).getName();
      will(returnValue(columnName));
      one(mockColumnDefinition).getProperty();
      will(returnValue(propertyName));
    }});

    return mockColumnDefinition;
  }

  protected SqlDialectFactory getSqlDialectFactory() {
    return new AnsiSqlDialectFactory();
  }

  public void testCreateInsertSql() throws Exception {
    final ColumnDefinition personId = getColumnDefinition("personId", "person_id", "personId");
    final ColumnDefinition firstName = getColumnDefinition("firstName", "first_name", "firstName");
    final ColumnDefinition lastName = getColumnDefinition("lastName", "last_name", "lastName");
    final ColumnDefinition dateOfBirth = getColumnDefinition("dob", "date_of_birth", "dateOfBirth");
    final ColumnDefinition ssn = getColumnDefinition("ssn", "ssn", "ssn");

    final List<ColumnDefinition> columnList = CollectionUtil.getList(personId, firstName, lastName, dateOfBirth, ssn);

    final TableDefinition person = context.mock(TableDefinition.class, "person");

    context.checking(new Expectations() {{
      one(person).getColumnDefinitions();
      will(returnValue(columnList));
      one(person).getName();
      will(returnValue("person"));
    }});

    final SqlDialectFactory ansiSqlDialectFactory = getSqlDialectFactory();

    final String expectedInsertSql = "INSERT INTO person (person_id, first_name, last_name, date_of_birth, ssn) VALUES (:personId, :firstName, :lastName, :dateOfBirth, :ssn)";
    final String actualInsertSql = ansiSqlDialectFactory.createInsertSql(person);

    assertEquals(expectedInsertSql, actualInsertSql);
    context.assertIsSatisfied();
  }

  public void testCreateInsertSqlThrowsException() throws Exception {
    final SqlDialectFactory ansiSqlDialectFactory = getSqlDialectFactory();
    String insertSql = null;

    assertNull(insertSql);

    try {
      insertSql = ansiSqlDialectFactory.createInsertSql(null);
      fail("Calling createInsertSql with a null TableDefinition object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The table definition used to create the INSERT SQL statement cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling createInsertSql with a null TableDefinition object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(insertSql);
  }

}
