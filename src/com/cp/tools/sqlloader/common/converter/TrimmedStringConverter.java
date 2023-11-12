
package com.cp.tools.sqlloader.common.converter;

import com.cp.common.beans.util.converters.AbstractConverter;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;

public class TrimmedStringConverter extends AbstractConverter {

  public Object convertImpl(final Class type, final Object value) {
    return StringUtil.trim(ObjectUtil.toString(value));
  }

}
