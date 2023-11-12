
package com.cp.tools.sqlloader.sql.util;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.CollectionUtil;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class SqlUtilTest extends TestCase {

  private final Mockery context = new Mockery();

  public SqlUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(SqlUtilTest.class);
    return suite;
  }

  protected ColumnDefinition getColumnDefinition(final String propertyName) {
    final ColumnDefinition mockColumnDefinition = context.mock(ColumnDefinition.class, propertyName);

    context.checking(new Expectations() {{
      one(mockColumnDefinition).getProperty();
      will(returnValue(propertyName));
    }});

    return mockColumnDefinition;
  }

  public void testGetParameterNames() throws Exception {
    final ColumnDefinition personId = getColumnDefinition("personId");
    final ColumnDefinition firstName = getColumnDefinition("firstName");
    final ColumnDefinition lastName = getColumnDefinition("lastName");
    final ColumnDefinition dob = getColumnDefinition("dateOfBirth");
    final ColumnDefinition ssn = getColumnDefinition("ssn");

    final List<ColumnDefinition> columns = CollectionUtil.getList(personId, firstName, lastName, dob, ssn);

    final TableDefinition mockTable = context.mock(TableDefinition.class);

    context.checking(new Expectations() {{
      one(mockTable).getColumnDefinitionCount();
      will(returnValue(columns.size()));
      one(mockTable).getColumnDefinitions();
      will(returnValue(columns));
    }});

    final String[] expectedParameterNames = { "personId", "firstName", "lastName", "dateOfBirth", "ssn" };
    final String[] actualParameterNames = SqlUtil.getParameterNames(mockTable);

    TestUtil.assertEquals(expectedParameterNames, actualParameterNames);
  }

  public void testGetParameterNamesThrowsException() throws Exception {
    String[] parameterNames = null;

    assertNull(parameterNames);

    try {
      parameterNames = SqlUtil.getParameterNames(null);
      fail("Calling getParameterNames on the SqlUtil class with a null TableDefinition object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The table definition cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getParameterNames on the SqlUtil class threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(parameterNames);
  }

}
