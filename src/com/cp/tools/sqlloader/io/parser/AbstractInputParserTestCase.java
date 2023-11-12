
package com.cp.tools.sqlloader.io.parser;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.ConstraintDefinition;
import static com.cp.tools.sqlloader.test.TestUtil.ReturnListAction.returnList;
import com.cp.common.beans.util.BeanUtil;
import com.cp.common.lang.ObjectUtil;
import java.util.Arrays;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;

public abstract class AbstractInputParserTestCase extends TestCase {

  protected final Mockery context = new Mockery();

  private int constraintIndex = 0;

  public AbstractInputParserTestCase(final String testName) {
    super(testName);
  }

  protected ColumnDefinition getColumnDefinition(final String property,
                                                 final Class type,
                                                 final ConstraintDefinition... constraintDefinitions)
  {
    return getColumnDefinition(property, property, type, constraintDefinitions);
  }

  protected ColumnDefinition getColumnDefinition(final String name,
                                                 final String property,
                                                 final Class type,
                                                 final ConstraintDefinition... constraintDefinitions)
  {
    return getColumnDefinition(name, property, type, -1, -1, constraintDefinitions);
  }

  protected ColumnDefinition getColumnDefinition(final String name,
                                                 final String property,
                                                 final Class type,
                                                 final int startIndex,
                                                 final int endIndex,
                                                 final ConstraintDefinition... constraintDefinitions)
  {
    final ColumnDefinition mockColumnDefinition = context.mock(ColumnDefinition.class, name);

    final ConstraintDefinition[] constraintDefinitionArray = (ConstraintDefinition[]) ObjectUtil.getDefaultValue(
      constraintDefinitions, new ColumnDefinition[0]);

    context.checking(new Expectations() {{
      allowing(mockColumnDefinition).getStartIndex();
      will(returnValue(startIndex));
      allowing(mockColumnDefinition).getEndIndex();
      will(returnValue(endIndex));
      allowing(mockColumnDefinition).getType();
      will(returnValue(type));
      allowing(mockColumnDefinition).getConstraintDefinitions();
      will(returnList(Arrays.asList(constraintDefinitionArray)));
      allowing(mockColumnDefinition).getProperty();
      will(returnValue(property));
      allowing(mockColumnDefinition).getName();
      will(returnValue(name));
    }});

    return mockColumnDefinition;
  }

  protected ConstraintDefinition getConstraintDefinition(final String name) {
    final ConstraintDefinition mockConstraintDefinition = context.mock(ConstraintDefinition.class, name + (constraintIndex++));

    context.checking(new Expectations() {{
      exactly(2).of(mockConstraintDefinition).getName();
      will(returnValue(name));
    }});

    return mockConstraintDefinition;
  }

  protected ConstraintDefinition getConstraintDefinition(final String name, final String min, final String max) {
    final ConstraintDefinition mockConstraintDefinition = getConstraintDefinition(name);

    context.checking(new Expectations() {{
      one(mockConstraintDefinition).getMin();
      will(returnValue(min));
      one(mockConstraintDefinition).getMax();
      will(returnValue(max));
    }});

    return mockConstraintDefinition;
  }

  protected abstract InputParser getInputParser();

  protected String printData(final Object[][] data) {
    final StringBuffer buffer = new StringBuffer();
    String delimiter = "";

    for (final Object[] rowData : data) {
      for (final Object columnData : rowData) {
        buffer.append(delimiter).append(BeanUtil.toString(columnData));
        delimiter = ", ";
      }

      buffer.append("\n");
      delimiter = "";
    }

    return buffer.toString();
  }

}
