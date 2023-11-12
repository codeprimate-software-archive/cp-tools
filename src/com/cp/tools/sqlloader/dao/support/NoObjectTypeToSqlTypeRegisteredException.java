
package com.cp.tools.sqlloader.dao.support;

public class NoObjectTypeToSqlTypeRegisteredException extends RuntimeException {

  public NoObjectTypeToSqlTypeRegisteredException() {
  }

  public NoObjectTypeToSqlTypeRegisteredException(final String message) {
    super(message);
  }

  public NoObjectTypeToSqlTypeRegisteredException(final Throwable cause) {
    super(cause);
  }

  public NoObjectTypeToSqlTypeRegisteredException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
