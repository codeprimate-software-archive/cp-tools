
package com.cp.tools.sqlloader.io.parser.support;

import com.cp.tools.sqlloader.io.parser.AbstractInputParser;
import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.validation.ConstraintValidationException;
import com.cp.common.lang.Assert;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.SystemException;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Record;

public class FixedWidthInputParser extends AbstractInputParser {

  @Override
  protected Record parseLine(final TableDefinition tableDefinition, final String inputLine) {
    try {
      Assert.notNull(tableDefinition, "The table definition cannot be null!");
      Assert.notEmpty(inputLine, "The input line to parse cannot be null or empty!");

      final Record record = AbstractRecordFactory.getInstance().getRecordInstance();

      for (final ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitions()) {
        final int startIndex = (columnDefinition.getStartIndex() - 1);
        final int endIndex = Math.min(inputLine.length(), columnDefinition.getEndIndex());

        final String stringColumnValue = StringUtil.trim(inputLine.substring(startIndex, endIndex));

        final Object convertedColumnValue = convert(columnDefinition, stringColumnValue);

        validateColumnValue(columnDefinition, convertedColumnValue);

        record.addField(columnDefinition.getProperty(), convertedColumnValue);
      }

      return record;
    }
    catch (ConstraintValidationException e) {
      logger.error("Failed to parse input line (" + inputLine + ")!", e);
      throw new SystemException("Failed to parse input line (" + inputLine + ")!", e);
    }
  }

}
