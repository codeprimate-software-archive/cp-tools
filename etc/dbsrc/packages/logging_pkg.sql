/*<TOAD_FILE_CHUNK>*/
CREATE OR REPLACE PACKAGE logging_pkg IS

DEFAULT_BUFFER_SIZE NUMBER := 1000000;

TYPE LogLevel IS RECORD
(
  priority NUMBER,
  description VARCHAR2(255)
);

DEBUG_LEVEL LogLevel;
INFO_LEVEL LogLevel;
WARN_LEVEL LogLevel;
ERROR_LEVEL LogLevel;
FATAL_LEVEL LogLevel;

DEFAULT_LOG_LEVEL LogLevel := ERROR_LEVEL;

FUNCTION getLogLevel RETURN LogLevel;
PROCEDURE init;
PROCEDURE init(p_logLevel IN LogLevel);
PROCEDURE init(p_bufferSize IN NUMBER);
PROCEDURE init(p_logLevel IN LogLevel, p_bufferSize IN NUMBER);
PROCEDURE log(p_logLevel LogLevel, p_message IN VARCHAR2);
PROCEDURE p_debug(p_message IN VARCHAR2);
PROCEDURE p_info(p_message IN VARCHAR2);
PROCEDURE p_warn(p_message IN VARCHAR2);
PROCEDURE p_error(p_message IN VARCHAR2);
PROCEDURE p_fatal(p_message IN VARCHAR2);
PROCEDURE setLogLevel(p_logLevel IN LogLevel);

END logging_pkg;
/
/*<TOAD_FILE_CHUNK>*/

CREATE OR REPLACE PACKAGE BODY logging_pkg IS

theLogLevel LogLevel;

FUNCTION getLogLevel RETURN LogLevel IS
  r_logLevel LogLevel;
BEGIN
  -- make a defensive copy
  r_logLevel.priority := theLogLevel.priority;
  r_logLevel.description := theLogLevel.description;
  RETURN r_logLevel;
END;

PROCEDURE init IS
BEGIN
  init(DEFAULT_LOG_LEVEL, DEFAULT_BUFFER_SIZE);
END;

PROCEDURE init(p_logLevel IN LogLevel) IS
BEGIN
  init(p_logLevel, DEFAULT_BUFFER_SIZE);
END;

PROCEDURE init(p_bufferSize IN NUMBER) IS
BEGIN
  init(DEFAULT_LOG_LEVEL, p_bufferSize);
END;

PROCEDURE init(p_logLevel IN LogLevel, p_bufferSize IN NUMBER) IS
BEGIN
  -- initialize the logging levels
  DEBUG_LEVEL.priority := 0;
  DEBUG_LEVEL.description := 'DEBUG';
  INFO_LEVEL.priority := 1;
  INFO_LEVEL.description := 'INFO';
  WARN_LEVEL.priority := 2;
  WARN_LEVEL.description := 'WARN';
  ERROR_LEVEL.priority := 3;
  ERROR_LEVEL.description := 'ERROR';
  FATAL_LEVEL.priority := 4;
  FATAL_LEVEL.description := 'FATAL';
  theLogLevel := p_logLevel;
  -- initialize the output buffer size
  DBMS_OUTPUT.ENABLE(p_bufferSize);
END;

PROCEDURE log(p_logLevel IN LogLevel, p_message IN VARCHAR2) IS
BEGIN
  IF p_logLevel.priority >= theLogLevel.priority THEN
    DBMS_OUTPUT.PUT_LINE(p_message);
  END IF;  
END;

PROCEDURE p_debug(p_message IN VARCHAR2) IS
BEGIN
  log(DEBUG_LEVEL, p_message);
END;

PROCEDURE p_info(p_message IN VARCHAR2) IS
BEGIN
  log(INFO_LEVEL, p_message);
END;

PROCEDURE p_warn(p_message IN VARCHAR2) IS
BEGIN
  log(WARN_LEVEL, p_message);
END;

PROCEDURE p_error(p_message IN VARCHAR2) IS
BEGIN
  log(ERROR_LEVEL, p_message);
END;

PROCEDURE p_fatal(p_message IN VARCHAR2) IS
BEGIN
  log(FATAL_LEVEL, p_message);
END;

PROCEDURE setLogLevel(p_logLevel IN LogLevel) IS
BEGIN
  -- make a defensive copy
  theLogLevel.priority := p_logLevel.priority;
  theLogLevel.description := p_logLevel.description;
END;

END logging_pkg;
/
