
package com.cp.tools.sqlloader.io.parser;

import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.context.ApplicationContext;

public class InputParserFactoryTest extends TestCase {

  private final Mockery context = new Mockery();

  public InputParserFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(InputParserFactoryTest.class);
    return suite;
  }

  protected InputParserFactory getInputParserFactory() {
    return new InputParserFactory();
  }

  protected InputParserFactory getInputParserFactory(final ApplicationContext applicationContext) {
    final InputParserFactory inputParserFactory = new InputParserFactory();
    inputParserFactory.setApplicationContext(applicationContext);
    return inputParserFactory;
  }

  public void testGetApplicationContactThrowsIllegalStateException() throws Exception {
    ApplicationContext applicationContext = null;

    assertNull(applicationContext);

    try {
      applicationContext = getInputParserFactory().getApplicationContext();
      fail("Calling getApplicationContext on the InputParserFactory not initialized with a Spring ApplicationContext should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The Spring Application Context was not properly initialized!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getApplicationContext on the InputParserFactory not initialized with a Spring ApplicationContext threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(applicationContext);
  }

  public void testSetApplicationContextThrowsNullPointerException() throws Exception {
    try {
      getInputParserFactory().setApplicationContext(null);
      fail("Setting the InputParserFactory Spring ApplicationContext to null should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The Spring Application Context cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the InputParserFactory Spring ApplicationContext to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testGetInputParser() throws Exception {
    final ApplicationContext mockApplicationContext = context.mock(ApplicationContext.class, "mockAppContext");

    final InputParser csvParser = context.mock(InputParser.class, "Comma Separated Values Parser");
    final InputParser fxwParser = context.mock(InputParser.class, "Fixed-width Parser");
    final InputParser xlsParser = context.mock(InputParser.class, "Microsoft Excel Spreadsheet");

    context.checking(new Expectations() {{
      allowing(mockApplicationContext).getBean("csvParser");
      will(returnValue(csvParser));
      allowing(mockApplicationContext).getBean("fixedWidthParser");
      will(returnValue(fxwParser));
      allowing(mockApplicationContext).getBean("xlsParser");
      will(returnValue(xlsParser));
    }});

    final InputParserFactory inputParserFactory = getInputParserFactory(mockApplicationContext);

    assertNotNull(inputParserFactory);
    assertSame(mockApplicationContext, inputParserFactory.getApplicationContext());

    try {
      assertSame(csvParser, inputParserFactory.getInputParser(new File("c:/etc/io/input/Person.csv")));
      assertSame(fxwParser, inputParserFactory.getInputParser(new File("c:/etc/io/input/Address.fxw")));
      assertSame(xlsParser, inputParserFactory.getInputParser(new File("c:/etc/io/input/PhoneNumber.xls")));
    }
    catch (Exception e) {
      fail("Calling getInputParser threw an unexpected Exception (" + e.getMessage() + ")");
    }

    context.assertIsSatisfied();
  }

}
