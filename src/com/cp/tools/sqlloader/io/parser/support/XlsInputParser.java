
package com.cp.tools.sqlloader.io.parser.support;

import com.cp.tools.sqlloader.io.parser.AbstractInputParser;
import com.cp.tools.sqlloader.mapping.beans.ColumnDefinition;
import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.tools.sqlloader.mapping.util.TableDefinitionConverter;
import com.cp.tools.sqlloader.validation.ConstraintValidationException;
import com.cp.common.io.IoUtil;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.SystemException;
import com.cp.common.util.record.AbstractRecordFactory;
import com.cp.common.util.record.Column;
import com.cp.common.util.record.Record;
import com.cp.common.util.record.RecordAdapter;
import com.cp.common.util.record.RecordTable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XlsInputParser extends AbstractInputParser {

  private static final Map<Integer, CellValueGetter> GETTER_MAP = new HashMap<Integer, CellValueGetter>();

  static {
    GETTER_MAP.put(HSSFCell.CELL_TYPE_BOOLEAN, new BooleanCellValueGetter());
    GETTER_MAP.put(HSSFCell.CELL_TYPE_FORMULA, new FormulaCellValueGetter());
    GETTER_MAP.put(HSSFCell.CELL_TYPE_NUMERIC, new NumericCellValueGetter());
  }

  @Override
  public RecordTable parse(final TableDefinition tableDefinition, final File inputFile) throws IOException {
    Assert.notNull(tableDefinition, "The table definition describing the format of the input file cannot be null!");
    Assert.notNull(inputFile, "The XLS input file cannot be null!");

    if (inputFile.exists()) {
      final FileInputStream xlsFileIn = new FileInputStream(inputFile);

      HSSFCell cell = null;
      HSSFRow row = null;

      Object cellValue = null;

      try {
        final POIFSFileSystem fs = new POIFSFileSystem(xlsFileIn);
        final HSSFWorkbook workbook = new HSSFWorkbook(fs);
        final HSSFSheet sheet = workbook.getSheetAt(0);

        final RecordTable recordTable = TableDefinitionConverter.toRecordTable(tableDefinition);
        final List<Column> columns = recordTable.getColumns();

        int columnIndex = 0;

        for (final Iterator rowIt = sheet.rowIterator(); rowIt.hasNext(); ) {
          row = (HSSFRow) rowIt.next();

          final Record record = AbstractRecordFactory.getInstance().getRecordInstance();

          for (final Iterator cellIt = row.cellIterator(); cellIt.hasNext(); ) {
            cell = (HSSFCell) cellIt.next();

            final ColumnDefinition columnDefinition = tableDefinition.getColumnDefinitionAtIndex(columnIndex++);

            final CellValueGetter getter = ObjectUtil.getDefaultValue(GETTER_MAP.get(cell.getCellType()),
              DefaultCellValueGetter.INSTANCE);

            cellValue = getter.getCellValue(cell);

            final Object convertedCellValue = convert(columnDefinition, cellValue);

            if (logger.isDebugEnabled()) {
              logger.debug("column name (" + columnDefinition.getName() + ")");
              logger.debug("column property (" + columnDefinition.getProperty() + ")");
              logger.debug("column cell value (" + cellValue + ")");
            }

            validateColumnValue(columnDefinition, convertedCellValue);

            record.addField(columnDefinition.getProperty(), convertedCellValue);
          }

          recordTable.addRow(new RecordAdapter(record, columns));
          columnIndex = 0;
        }

        return recordTable;
      }
      catch (ConstraintValidationException e) {
        logger.error("Failed to parse and validate cell value (" + cellValue + ") for cell ("
          + row.getRowNum() + ", " + cell.getCellNum() + ")!");
        throw new SystemException("Failed to parse and validate cell value (" + cellValue + ") for cell ("
          + row.getRowNum() + ", " + cell.getCellNum() + ")!", e);
      }
      finally {
        IoUtil.close(xlsFileIn);
      }
    }
    else {
      logger.warn("The XLS file (" + inputFile + ") could not be found in the file system!");
      throw new FileNotFoundException("The XLS file (" + inputFile + ") could not be found in the file system!");
    }
  }

  @Override
  protected Record parseLine(final TableDefinition tableDefinition, final String inputLine) {
    throw new UnsupportedOperationException("Not Implemented!");
  }

  protected static interface CellValueGetter {

    public Object getCellValue(HSSFCell cell);

  }

  protected static final class BooleanCellValueGetter implements CellValueGetter {

    public Object getCellValue(final HSSFCell cell) {
      return cell.getBooleanCellValue();
    }
  }

  protected static final class DateCellValueGetter implements CellValueGetter {

    public Object getCellValue(final HSSFCell cell) {
      return cell.getDateCellValue();
    }
  }

  protected static final class DefaultCellValueGetter implements CellValueGetter {

    protected static final DefaultCellValueGetter INSTANCE = new DefaultCellValueGetter();

    public Object getCellValue(final HSSFCell cell) {
      return StringUtil.trim(cell.getRichStringCellValue().getString());
    }
  }

  protected static final class FormulaCellValueGetter implements CellValueGetter {

    public Object getCellValue(final HSSFCell cell) {
      return cell.getCellFormula();
    }
  }

  protected static final class NumericCellValueGetter implements CellValueGetter {

    public Object getCellValue(final HSSFCell cell) {
      return cell.getNumericCellValue();
    }
  }

}
