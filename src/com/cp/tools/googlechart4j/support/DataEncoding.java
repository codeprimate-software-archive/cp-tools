
package com.cp.tools.googlechart4j.support;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;

public enum DataEncoding {
  EXTENDED("e"),
  SIMPLE("s"),
  TEXT("t");

  private final String code;

  DataEncoding(final String code) {
    Assert.notNull(code, "The code specifying the Data Encoding cannot be null or empty!");
    this.code = code;
  }

  public static DataEncoding getDataEncodingByCode(final String code) {
    for (final DataEncoding encoding : values()) {
      if (ObjectUtil.equals(encoding.getCode(), code)) {
        return encoding;
      }
    }

    return null;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return getCode();
  }

}
