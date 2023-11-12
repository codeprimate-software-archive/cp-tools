CREATE OR REPLACE FUNCTION hashCode(p_value IN VARCHAR2)
RETURN NUMBER IS

  v_hashValue NUMBER;
  v_valueLen NUMBER;

BEGIN

  -- NOTE: this algorithm is taking from the hashCode method of the String class
  -- in Java 2 Platform.
  IF p_value IS NOT NULL THEN
    SELECT length(p_value)
    INTO v_valueLen
    FROM dual;
  
    v_hashValue := 17;
    FOR indx IN 1..v_valueLen LOOP
      v_hashValue := 31 * v_hashValue + ascii(substr(p_value, indx, 1));
    END LOOP;
  
    RETURN v_hashValue;
  END IF;
  
  RETURN 0;

-- NOTE: do not handle Exceptions, let the Exception propagate out so the calling code 
-- can decide what to do if a hash value cannot be computed.  If the calling code
-- depends on the hash value being computed correctly, then catching the Exception
-- could potentiall affect the behavior of the caller.

END;
