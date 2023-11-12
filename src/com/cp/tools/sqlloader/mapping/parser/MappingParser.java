
package com.cp.tools.sqlloader.mapping.parser;

import com.cp.tools.sqlloader.mapping.beans.DataSourceDefinition;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface MappingParser {

  public List<DataSourceDefinition> parse(File mappingFile) throws FileNotFoundException;

}
