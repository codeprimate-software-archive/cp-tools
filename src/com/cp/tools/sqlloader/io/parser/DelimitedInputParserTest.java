
package com.cp.tools.sqlloader.io.parser;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.mapping.util.ConstraintType;
import com.cp.tools.sqlloader.validation.ConstraintValidationException;
import com.cp.common.enums.State;
import com.cp.common.util.DateUtil;
import com.cp.common.util.SystemException;
import com.cp.common.util.record.Record;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class DelimitedInputParserTest extends AbstractInputParserTestCase {

  private final Mockery context = new Mockery();

  public DelimitedInputParserTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DelimitedInputParserTest.class);
    return suite;
  }

  @Override
  protected InputParser getInputParser() {
    return getInputParser(",");
  }

  protected DelimitedInputParser getInputParser(final String delimiter) {
    return new DelimitedInputParser() {
      protected String getDelimiter() {
        return delimiter;
      }
    };
  }

  public void testParseLine() throws Exception {
    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class, "person");

    final ColumnDefinition idColumn = getColumnDefinition("id", Integer.class);
    final ColumnDefinition firstNameColumn = getColumnDefinition("firstName", String.class);
    final ColumnDefinition lastNameColumn = getColumnDefinition("lastName", String.class);
    final ColumnDefinition dobColumn = getColumnDefinition("dateOfBirth", Calendar.class);

    context.checking(new Expectations() {{
      one(mockTableDefinition).getColumnDefinitionCount();
      will(returnValue(4));
      one(mockTableDefinition).getName();
      will(returnValue("person"));
      one(mockTableDefinition).getColumnDefinitionAtIndex(0);
      will(returnValue(idColumn));
      one(mockTableDefinition).getColumnDefinitionAtIndex(1);
      will(returnValue(firstNameColumn));
      one(mockTableDefinition).getColumnDefinitionAtIndex(2);
      will(returnValue(lastNameColumn));
      one(mockTableDefinition).getColumnDefinitionAtIndex(3);
      will(returnValue(dobColumn));
    }});

    Record record = null;

    assertNull(record);

    try {
      record = getInputParser(",").parseLine(mockTableDefinition, "1, Jon, Doe, 08/18/1991");
    }
    catch (Exception e) {
      fail("Calling parseLine threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(record);
    assertEquals(4, record.size());
    assertTrue(record.containsField("id"));
    assertTrue(record.containsField("firstName"));
    assertTrue(record.containsField("lastName"));
    assertTrue(record.containsField("dateOfBirth"));
    assertEquals(1, record.get("id"));
    assertEquals("Jon", record.get("firstName"));
    assertEquals("Doe", record.get("lastName"));
    assertEquals(DateUtil.getCalendar(1991, Calendar.AUGUST, 18), record.get("dateOfBirth"));

    context.assertIsSatisfied();
  }

  public void testParseLineThrowsSystemException() throws Exception {
    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class, "address");

    final ColumnDefinition streetColumn = getColumnDefinition("street", String.class,
      getConstraintDefinition(ConstraintType.NOT_EMPTY.getName()));
    final ColumnDefinition cityColumn = getColumnDefinition("city", String.class,
      getConstraintDefinition(ConstraintType.BOUNDED_LENGTH.getName(), "5", "15"));
    final ColumnDefinition stateColumn = getColumnDefinition("state", State.class,
      getConstraintDefinition(ConstraintType.NOT_NULL.getName()));
    final ColumnDefinition zipColumn = getColumnDefinition("zip", String.class,
      getConstraintDefinition(ConstraintType.DIGITS_ONLY.getName()));

    context.checking(new Expectations() {{
      one(mockTableDefinition).getColumnDefinitionCount();
      will(returnValue(4));
      one(mockTableDefinition).getName();
      will(returnValue("address"));
      one(mockTableDefinition).getColumnDefinitionAtIndex(0);
      will(returnValue(streetColumn));
      one(mockTableDefinition).getColumnDefinitionAtIndex(1);
      will(returnValue(cityColumn));
      one(mockTableDefinition).getColumnDefinitionAtIndex(2);
      will(returnValue(stateColumn));
      one(mockTableDefinition).getColumnDefinitionAtIndex(3);
      will(returnValue(zipColumn));
    }});

    Record record = null;

    assertNull(record);

    try {
      record = getInputParser(",").parseLine(mockTableDefinition, "100 Main St., Portland, OR, l0l01");
      fail("Calling parseLine with an input line containing a non-digit-only zip should have thrown a SystemExeption!");
    }
    catch (SystemException e) {
      assertEquals("Failed to parse input line (100 Main St., Portland, OR, l0l01)!", e.getMessage());
      assertTrue(e.getCause() instanceof ConstraintValidationException);
      assertEquals("Constraint validation failed!", e.getCause().getMessage());
    }
    catch (Exception e) {
      fail("Calling parseLine with an input line containing a non-digit-only zip threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(record);

    context.assertIsSatisfied();
  }

  public void testParseLineWithUnequalColumnAndTokenCounts() throws Exception {
    final TableDefinition mockTableDefinition = context.mock(TableDefinition.class, "mockTable");

    context.checking(new Expectations() {{
      one(mockTableDefinition).getColumnDefinitionCount();
      will(returnValue(2));
      one(mockTableDefinition).getName();
      will(returnValue("mockTable"));
    }});

    Record record = null;

    assertNull(record);

    try {
      record = getInputParser(",").parseLine(mockTableDefinition, "firstValue, secondValue, thirdValue");
      fail("Calling parseLine with a TableDefinition having a column count unequal to the token count of the values in the input line should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The number number of tokens (3) in the input line (firstValue, secondValue, thirdValue) does not equal the number of columns (2) in the table definition (mockTable)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parseLine with a TableDefinition having a column count unequal to the token count of the values in the input line threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(record);

    context.assertIsSatisfied();
  }

  public void testParseLineWithEmptyInputLine() throws Exception {
    Record record = null;

    assertNull(record);

    try {
      record = getInputParser(",").parseLine(context.mock(TableDefinition.class, "anotherMockTableDefinition"), " ");
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
      record = getInputParser(",").parseLine(context.mock(TableDefinition.class, "mockTableDefinition"), null);
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
      record = getInputParser(",").parseLine(null, "1, jon, doe");
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
