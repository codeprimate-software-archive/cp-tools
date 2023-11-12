
package com.cp.tools.sqlloader.test;

import com.cp.common.lang.ObjectUtil;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public final class TestUtil {

  private TestUtil() {
  }

  public static class ReturnListAction<T> implements Action {

    private final List<T> expectedList;

    public ReturnListAction(final List<T> expectedList) {
      this.expectedList = getList(expectedList);
    }

    public static <T> ReturnListAction<T> returnList(final List<T> expectedList) {
      return new ReturnListAction<T>(expectedList);
    }

    protected List<T> getList(final List<T> expectedList) {
      return (ObjectUtil.isNotNull(expectedList) ? new LinkedList<T>(expectedList) : new LinkedList<T>());
    }

    public void describeTo(final Description description) {
      description.appendText("Returns a List of elements.");
    }

    public Object invoke(final Invocation invocation) throws Throwable {
      return expectedList;
    }
  }

}
