
package com.cp.tools.googlechart4j.support;

import com.cp.common.lang.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDataSet implements DataSet {

  protected final Log logger = LogFactory.getLog(getClass());

  private final DataEncoding encoding;

  public AbstractDataSet(final DataEncoding encoding) {
    Assert.notNull(encoding, "The data encoding for this DataSet cannot be null!");
    this.encoding = encoding;
  }

  public DataEncoding getEncoding() {
    return encoding;
  }

}
