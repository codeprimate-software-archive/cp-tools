-- Use the following query to test the getAge SQL function.
-- select getAge(to_date('12/03/1974', 'MM/DD/YYYY')) from dual;

CREATE OR REPLACE FUNCTION getAge(p_dateOfBirth IN DATE)
RETURN NUMBER IS

  v_currentDate DATE := sysdate;

  v_monthsBetween NUMBER := 0.0;
  v_numberOfMonthsInYear NUMBER := 12.0;
  
  INVALID_DATEOFBIRTH_EXCEPTION EXCEPTION;
  PRAGMA EXCEPTION_INIT(INVALID_DATEOFBIRTH_EXCEPTION, -20169);
  
BEGIN

  IF p_dateOfBirth IS NOT NULL THEN
    v_monthsBetween := months_between(trunc(v_currentDate), trunc(p_dateOfBirth));
	RETURN floor(v_monthsBetween / v_numberOfMonthsInYear);
  ELSE
    raise_application_error(-20169, 'Date of Birth cannot be null!'); 
  END IF;

EXCEPTION
  WHEN INVALID_DATEOFBIRTH_EXCEPTION THEN
    DBMS_OUTPUT.PUT_LINE(SQLERRM);
	RAISE;
	
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Failed to determine age from date of birth (' || p_dateOfBirth || ')!');
	DBMS_OUTPUT.PUT_LINE(SQLERRM);
	RAISE;
	
END;
