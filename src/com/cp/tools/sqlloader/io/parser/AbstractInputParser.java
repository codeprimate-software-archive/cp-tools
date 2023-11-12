
package com.cp.tools.sqlloader.io.parser;

import com.cp.tools.sqlloader.common.converter.TrimmedStringConverter;
import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.ConstraintDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.mapping.util.ConstraintDefinitionConverter;
import com.cp.tools.sqlloader.mapping.util.TableDefinitionConverter;
import com.cp.tools.sqlloader.validation.ComposableConstraint;
import com.cp.tools.sqlloader.validation.Constraint;
import com.cp.tools.sqlloader.validation.ConstraintValidationException;
import com.cp.tools.sqlloader.validation.support.ConstraintFactory;
import com.cp.common.beans.util.ConvertUtil;
import com.cp.common.io.IoUtil;
import com.cp.common.lang.Assert;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordAdapter;
import com.cp.common.util.record.RecordTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractInputParser implements InputParser {

  protected final Log logger = LogFactory.getLog(getClass());

  static {
    ConvertUtil.register(String.class, new TrimmedStringConverter());
  }

  protected Object convert(final Class type, final Object value) {
    return ConvertUtil.convert(type, value);
  }

  protected Object convert(final ColumnDefinition columnDefinition, final Object columnValue) {
    return convert(columnDefinition.getType(), columnValue);
  }

  public RecordTable parse(final TableDefinition tableDefinition, final File inputFile) throws IOException {
    Assert.notNull(tableDefinition, "The table definition describing the format of the input file cannot be null!");
    Assert.notNull(inputFile, "The input file cannot be null!");

    if (inputFile.exists()) {
      final BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
      String inputLine;

      final RecordTable recordTable = TableDefinitionConverter.toRecordTable(tableDefinition);
      final List<Column> columns = recordTable.getColumns();

      try {
        while ((inputLine = fileReader.readLine()) != null) {
          recordTable.addRow(new RecordAdapter(parseLine(tableDefinition, inputLine), columns));
        }

        return recordTable;
      }
      finally {
        IoUtil.close(fileReader);
      }
    }
    else {
      logger.warn("The data input file (" + inputFile + ") could not be found in the file system!");
      throw new FileNotFoundException("The data input file (" + inputFile + ") could not be found in the file system!");
    }
  }

  protected abstract Record parseLine(TableDefinition tableDefinition, String inputLine);

  protected void validateColumnValue(final ColumnDefinition columnDefinition, final Object columnValue) 
    throws ConstraintValidationException
  {
    Constraint constraint = ConstraintFactory.getDefaultConstraint();

    for (final ConstraintDefinition constraintDefinition : columnDefinition.getConstraintDefinitions()) {
      constraint = ComposableConstraint.composeAnd(constraint, ConstraintDefinitionConverter.toConstraint(constraintDefinition));
    }

    constraint.validate(columnValue);
  }

}
