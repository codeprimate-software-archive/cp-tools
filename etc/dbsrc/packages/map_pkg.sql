CREATE OR REPLACE PACKAGE map_pkg IS

TYPE map_entry IS RECORD 
(
  key  VARCHAR2(256),
  value VARCHAR2(256)
);

-- NOTE: unfortunately, this is not a true hash map since the keys and values
-- cannot be arbitrary Objects.
TYPE hash_map IS TABLE OF map_entry;

PROCEDURE clear(theMap IN OUT hash_map);
FUNCTION contains_key(theMap IN hash_map, key IN VARCHAR2) RETURN BOOLEAN;
FUNCTION contains_value(theMap IN hash_map, value IN VARCHAR2) RETURN BOOLEAN;
FUNCTION get(theMap IN hash_map, key IN VARCHAR2) RETURN VARCHAR2;
FUNCTION is_empty(theMap IN hash_map) RETURN BOOLEAN;
PROCEDURE put(theMap IN OUT hash_map, key IN VARCHAR2, value IN VARCHAR2);
PROCEDURE remove(theMap IN OUT hash_map, key IN VARCHAR2);

END map_pkg;
/

CREATE OR REPLACE PACKAGE BODY map_pkg IS

-- Removes all mappings from this map.
PROCEDURE clear(theMap IN OUT hash_map)
IS
BEGIN
  IF NOT map_pkg.is_empty(theMap) THEN
    theMap.DELETE;
	theMap.TRIM;
  END IF;
END;

-- Returns true if this map contains a mapping for the specified key.
FUNCTION contains_key(theMap IN hash_map,
		 			  key IN VARCHAR2)
RETURN BOOLEAN IS
  mapEntry map_entry := NULL;
BEGIN
  IF NOT map_pkg.is_empty(theMap) THEN
    FOR indx IN theMap.FIRST..theMap.LAST LOOP
      mapEntry := theMap(indx);
	  IF mapEntry.key = key THEN
	    RETURN TRUE;
	  END IF;
    END LOOP;
  END IF;
  RETURN FALSE;
END;

-- Returns true if this map maps one or more keys to the specified value.
FUNCTION contains_value(theMap IN hash_map, 
                        value IN VARCHAR2) 
RETURN BOOLEAN IS
  mapEntry map_entry := NULL;
BEGIN
  IF NOT map_pkg.is_empty(theMap) THEN
    FOR indx IN theMap.FIRST..theMap.LAST LOOP
      mapEntry := theMap(indx);
	  IF mapEntry.value = value THEN
	    RETURN TRUE;
	  END IF;
    END LOOP;
  END IF;
  RETURN FALSE;
END;

-- Returns the value to which this map maps the specified key.
FUNCTION get(theMap IN hash_map,
             key IN VARCHAR2)
RETURN VARCHAR2 IS
  mapEntry map_entry;
BEGIN
  IF NOT map_pkg.is_empty(theMap) THEN
    FOR indx IN theMap.FIRST..theMap.LAST LOOP
      mapEntry := theMap(indx);
	  IF mapEntry.key = key THEN
	    RETURN mapEntry.value;
	  END IF;
    END LOOP;
  END IF;
  RETURN NULL;
END;

-- Returns true if this map contains no key-value mappings or is NULL.
-- NOTE: If the collection is empty, FIRST and LAST return NULL (from OTN).
FUNCTION is_empty(theMap IN hash_map)
RETURN BOOLEAN IS
BEGIN
  --RETURN theMap.FIRST IS NULL AND theMap.LAST IS NULL;
  RETURN theMap IS NULL OR theMap.COUNT = 0;
END;

-- Associates the specified value with the specified key in this map
PROCEDURE put(theMap IN OUT hash_map,
              key IN VARCHAR2,
			  value IN VARCHAR2)
AS
  mapEntry map_entry;
BEGIN
  mapEntry.key := key;
  mapEntry.value := value;
  
  IF map_pkg.is_empty(theMap) THEN
    theMap := hash_map(mapEntry);
  ELSE
    theMap.EXTEND;
	theMap(theMap.LAST) := mapEntry;
  END IF;
END;

-- Removes the mapping for this key from this map if present.
PROCEDURE remove(theMap IN OUT hash_map,
		  		 key IN VARCHAR2)
IS
  mapEntry map_entry := NULL;
BEGIN
  IF NOT map_pkg.is_empty(theMap) THEN
    FOR indx IN theMap.FIRST..theMap.LAST LOOP
      mapEntry := theMap(indx);
	  IF mapEntry.key = key THEN
	    theMap.DELETE(indx);
	  END IF;
    END LOOP;
  END IF;
END;

END map_pkg;
/
