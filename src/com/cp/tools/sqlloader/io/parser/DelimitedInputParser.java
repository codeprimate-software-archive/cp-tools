
package com.cp.tools.sqlloader.io.parser;

import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.validation.ConstraintValidationException;
import com.cp.common.lang.Assert;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.SystemException;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Record;
import java.util.StringTokenizer;

public abstract class DelimitedInputParser extends AbstractInputParser {

  protected abstract String getDelimiter();

  @Override
  protected Record parseLine(final TableDefinition tableDefinition, final String inputLine) {
    try {
      Assert.notNull(tableDefinition, "The table definition cannot be null!");
      Assert.notEmpty(inputLine, "The input line to parse cannot be null or empty!");

      final Record record = AbstractRecordFactory.getInstance().getRecordInstance();
      final StringTokenizer parser = new StringTokenizer(inputLine, getDelimiter(), false);

      final int columnCount = tableDefinition.getColumnDefinitionCount();
      int columnIndex = 0;
      final int tokenCount = parser.countTokens();

      Assert.state(tokenCount == columnCount, "The number number of tokens (" + tokenCount + ") in the input line ("
        + inputLine + ") does not equal the number of columns (" + columnCount + ") in the table definition ("
        + tableDefinition.getName() + ")!");

      while (parser.hasMoreTokens()) {
        final ColumnDefinition columnDefinition = tableDefinition.getColumnDefinitionAtIndex(columnIndex++);

        final String stringColumnValue = StringUtil.trim(parser.nextToken());

        final Object convertedColumnValue = convert(columnDefinition, stringColumnValue);

        validateColumnValue(columnDefinition, convertedColumnValue);

        record.addField(columnDefinition.getProperty(), convertedColumnValue);
      }

      return record;
    }
    catch (ConstraintValidationException e) {
      throw new SystemException("Failed to parse input line (" + inputLine + ")!", e);
    }
  }

}
