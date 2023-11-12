-- TEST map_pkg

DECLARE
  userid_name_map map_pkg.hash_map;
  mapEntry map_pkg.map_entry;
  assignedUserId "CASE".assigned_user_id%TYPE;
  assignedUserName "CASE".assigned_user_name%TYPE;
BEGIN
  DBMS_OUTPUT.ENABLE(256000);
  FOR caseRec IN (SELECT DISTINCT assigned_user_id FROM "CASE") LOOP
    assignedUserId := caseRec.assigned_user_id;
	assignedUserName := getNameForLdapUserId(assignedUserId);
	map_pkg.put(userid_name_map, assignedUserId, assignedUserName); 
  END LOOP;
  FOR indx IN userid_name_map.FIRST..userid_name_map.LAST LOOP
    mapEntry := userid_name_map(indx);
	assignedUserId := mapEntry.key;
	assignedUserName := mapEntry.value;
    DBMS_OUTPUT.PUT_LINE('{' || assignedUserId || ', ' || assignedUserName || '}');
  END LOOP;
  DBMS_OUTPUT.PUT_LINE('AssignedUserId (' || assignedUserId || '): ' || map_pkg.get(userid_name_map, assignedUserId));
  --DBMS_OUTPUT.PUT_LINE('AssignedUserId (dom0856): ' || map_pkg.get(userid_name_map, 'dom0856'));
END;
