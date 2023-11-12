BEGIN
  logging_pkg.init(logging_pkg.FATAL_LEVEL);
  DBMS_OUTPUT.PUT_LINE('Log level is set to ' || logging_pkg.getLogLevel().description);
  logging_pkg.p_fatal('Fatal Log Message');
  logging_pkg.p_error('Error Log Message');
  logging_pkg.p_warn('Warn Log Message');
  logging_pkg.p_info('Info Log Message');
  logging_pkg.p_debug('Debug Log Message');
  
  logging_pkg.setLogLevel(logging_pkg.ERROR_LEVEL);
  DBMS_OUTPUT.PUT_LINE('Log level is set to ' || logging_pkg.getLogLevel().description);
  logging_pkg.p_fatal('Fatal Log Message');
  logging_pkg.p_error('Error Log Message');
  logging_pkg.p_warn('Warn Log Message');
  logging_pkg.p_info('Info Log Message');
  logging_pkg.p_debug('Debug Log Message');
  
  logging_pkg.setLogLevel(logging_pkg.WARN_LEVEL);
  DBMS_OUTPUT.PUT_LINE('Log level is set to ' || logging_pkg.getLogLevel().description);
  logging_pkg.p_fatal('Fatal Log Message');
  logging_pkg.p_error('Error Log Message');
  logging_pkg.p_warn('Warn Log Message');
  logging_pkg.p_info('Info Log Message');
  logging_pkg.p_debug('Debug Log Message');
  
  logging_pkg.setLogLevel(logging_pkg.INFO_LEVEL);
  DBMS_OUTPUT.PUT_LINE('Log level is set to ' || logging_pkg.getLogLevel().description);
  logging_pkg.p_fatal('Fatal Log Message');
  logging_pkg.p_error('Error Log Message');
  logging_pkg.p_warn('Warn Log Message');
  logging_pkg.p_info('Info Log Message');
  logging_pkg.p_debug('Debug Log Message');
  
  logging_pkg.setLogLevel(logging_pkg.DEBUG_LEVEL);
  DBMS_OUTPUT.PUT_LINE('Log level is set to ' || logging_pkg.getLogLevel().description);
  logging_pkg.p_fatal('Fatal Log Message');
  logging_pkg.p_error('Error Log Message');
  logging_pkg.p_warn('Warn Log Message');
  logging_pkg.p_info('Info Log Message');
  logging_pkg.p_debug('Debug Log Message');
END;

