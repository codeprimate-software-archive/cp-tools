
package com.cp.tools.sqlloader.mapping.util;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.common.util.record.Column;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class ColumnDefinitionConverterTest extends TestCase {

  private final Mockery context = new Mockery();

  public ColumnDefinitionConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ColumnDefinitionConverterTest.class);
    return suite;
  }

  public void testToColumn() throws Exception {
    final ColumnDefinition mockColumnDefinition = context.mock(ColumnDefinition.class);

    context.checking(new Expectations() {{
      one(mockColumnDefinition).getProperty();
      will(returnValue("dateOfBirth"));
      one(mockColumnDefinition).getType();
      will(returnValue(Calendar.class));
    }});

    final Column actualColumn = ColumnDefinitionConverter.toColumn(mockColumnDefinition);

    assertNotNull(actualColumn);
    assertNull(actualColumn.getDefaultValue());
    assertNull(actualColumn.getDescription());
    assertNull(actualColumn.getDisplayName());
    assertEquals("dateOfBirth", actualColumn.getName());
    assertEquals(0, actualColumn.getSize());
    assertEquals(Calendar.class, actualColumn.getType());

    context.assertIsSatisfied();
  }

}
