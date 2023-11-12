-- This SQL code causes an Exception (ORA-01426: numeric overflow, ORA-06512: at line 13) 
DECLARE
  TYPE hashmap IS TABLE OF "CASE".assigned_user_name%TYPE INDEX BY BINARY_INTEGER;
  userid_name_map hashmap;
  assignedUserId "CASE".assigned_user_id%TYPE;
  assignedUserName "CASE".assigned_user_name%TYPE;
  hashValue NUMBER;
BEGIN
  FOR caseRec IN (SELECT DISTINCT assigned_user_id FROM "CASE") LOOP
    assignedUserId := caseRec.assigned_user_id;
	hashValue := hashCode(assignedUserId);
	assignedUserName := getNameForLdapUserId(assignedUserId);
	userid_name_map(hashValue) := assignedUserName; -- Exception
  END LOOP;
  FOR indx IN userid_name_map.FIRST..userid_name_map.LAST LOOP
    DBMS_OUTPUT.PUT_LINE('{' || indx || ', ' || userid_name_map(indx) || '}');
  END LOOP;
END;
