
package com.cp.tools.sqlloader.dao.support;

import com.cp.common.beans.util.converters.AbstractConverter;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;

public class StringToUpperVarcharConverter extends AbstractConverter {

  public static final StringToUpperVarcharConverter INSTANCE = new StringToUpperVarcharConverter();

  protected Object convertImpl(final Class type, final Object value) {
    if (ObjectUtil.isNotNull(value)) {
      Assert.isInstanceOf(value.getClass(), String.class, "The Object value must be of type String!");
      return value.toString().toUpperCase();
    }
    else {
      return null;
    }
  }

}
