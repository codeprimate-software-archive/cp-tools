
package com.cp.tools.sqlloader.io.parser;

import com.cp.tools.sqlloader.mapping.beans.TableDefinition;
import com.cp.common.util.record.RecordTable;
import java.io.File;
import java.io.IOException;

public interface InputParser {

  public RecordTable parse(TableDefinition tableDefinition, File inputFile) throws IOException;

}
