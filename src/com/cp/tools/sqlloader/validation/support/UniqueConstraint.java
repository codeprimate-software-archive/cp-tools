
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractConstraint;
import java.util.LinkedHashSet;
import java.util.Set;

public class UniqueConstraint<T> extends AbstractConstraint<T> {

  private final Set<T> valueSet = new LinkedHashSet<T>();

  public boolean accept(final T value) {
    return valueSet.add(value);
  }

}
