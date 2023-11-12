
package com.cp.tools.sqlloader.io.parser.support;

import com.cp.tools.sqlloader.io.parser.DelimitedInputParser;

public class CsvInputParser extends DelimitedInputParser {

  protected static final String COMMA_DELIMETER = ",";

  protected final String getDelimiter() {
    return COMMA_DELIMETER;
  }

}
