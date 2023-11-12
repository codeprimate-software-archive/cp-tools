CREATE OR REPLACE FUNCTION isEmpty(p_value IN VARCHAR2)
RETURN BOOLEAN IS

BEGIN
  RETURN (p_value IS NULL OR trim(p_value) = '');
END;
/