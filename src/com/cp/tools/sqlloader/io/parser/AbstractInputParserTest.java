
package com.cp.tools.sqlloader.io.parser;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.ConstraintDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.mapping.util.ConstraintType;
import static com.cp.tools.sqlloader.test.TestUtil.ReturnListAction.returnList;
import com.cp.tools.sqlloader.validation.ConstraintValidationException;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class AbstractInputParserTest extends TestCase {

  private static final String INPUT_FILE = "c:/Projects/MyProject/etc/tmp/testSqlLoaderData.csv";

  private final Mockery context = new Mockery();

  public AbstractInputParserTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractInputParserTest.class);
    return suite;
  }

  protected AbstractInputParser getInputParser() {
    return new TestInputParser();
  }

  public void testParseWithNonExistingInputFile() throws Exception {
    RecordTable recordTable = null;

    assertNull(recordTable);

    try {
      recordTable = getInputParser().parse(context.mock(TableDefinition.class, "mockTableDefiniton"),
        new File("C:/tmp/nonExistingFile.csv"));
      fail("Calling parse with a non-existing file should have thrown a FileNotFoundException!");
    }
    catch (FileNotFoundException e) {
      assertEquals("The data input file (C:\\tmp\\nonExistingFile.csv) could not be found in the file system!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a non-existing file threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(recordTable);
  }

  public void testParseWithNullInputFile() throws Exception {
    RecordTable recordTable = null;

    assertNull(recordTable);

    try {
      recordTable = getInputParser().parse(context.mock(TableDefinition.class, "anotherMockTableDefinition"), null);
      fail("Calling parse with a null input file should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The input file cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a null input file threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(recordTable);
  }

  public void testParseWithNullTableDefiniton() throws Exception {
    RecordTable recordTable = null;

    assertNull(recordTable);

    try {
      recordTable = getInputParser().parse(null, new File(INPUT_FILE));
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

  public void testValidateColumnValue() throws Exception {
    final ColumnDefinition mockColumnDefinition = context.mock(ColumnDefinition.class, "mockColumn");
    final ConstraintDefinition mockConstraintDefiniton = context.mock(ConstraintDefinition.class, ConstraintType.NOT_NULL.getName());

    final List<ConstraintDefinition> expectedConstraintDefinitionList = new ArrayList<ConstraintDefinition>(1);
    expectedConstraintDefinitionList.add(mockConstraintDefiniton);

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefiniton).getName();
      will(returnValue(ConstraintType.NOT_NULL.getName()));
      one(mockColumnDefinition).getConstraintDefinitions();
      will(returnList(expectedConstraintDefinitionList));
    }});

    try {
      getInputParser().validateColumnValue(mockColumnDefinition, null);
      fail("Calling validateColumnValue with a mock ColumnDefinition and null value should have thrown a ConstraintValidationException!");
    }
    catch (ConstraintValidationException e) {
      assertEquals("Constraint validation failed!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling validateColumnValue with a mock ColumnDefinition and null value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    context.assertIsSatisfied();
  }

  protected static final class TestInputParser extends AbstractInputParser {

    protected Record parseLine(TableDefinition tableDefinition, String inputLine) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

}
