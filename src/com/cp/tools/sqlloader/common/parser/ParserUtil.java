
package com.cp.tools.sqlloader.common.parser;

import com.cp.common.lang.StringUtil;

public final class ParserUtil {

  private ParserUtil() {
  }

  public static int parseInt(final String value) {
    return (StringUtil.isEmpty(value) ? -1 : Integer.parseInt(value));
  }

}
