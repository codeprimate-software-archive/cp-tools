
package com.cp.tools.sqlloader.validation.support;

import com.cp.tools.sqlloader.validation.AbstractConstraint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexConstraint extends AbstractConstraint<String> {

  private final Pattern pattern;

  public RegexConstraint(final String regularExpression) {
    this.pattern = Pattern.compile(regularExpression);
  }

  public String getRegularExpression() {
    return pattern.pattern();
  }

  public boolean accept(final String value) {
    final Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }

}
