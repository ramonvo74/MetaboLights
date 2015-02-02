-- Migrate studies
DELETE FROM STUDIES;

INSERT INTO STUDIES
SELECT 
  ID,
  ACC,
  OBFUSCATIONCODE, 
  RELEASEDATE,
  CASE WHEN STATUS = 0 THEN 3
    ELSE 0
  END AS STATUS
FROM STUDY;

-- Migrate users
DELETE FROM USERS;

INSERT INTO USERS
SELECT
  ID, 
  ADDRESS,
  AFFILIATION,
  AFFILIATION_URL,
  API_TOKEN,
  EMAIL,
  FIRSTNAME,
  JOINDATE,
  LASTNAME,
  PASSWORD,
  COALESCE(ROLE, 0) AS ROLE,
  CASE WHEN STATUS='NEW' THEN 0
    WHEN STATUS='VERIFIED' THEN 1
    WHEN STATUS='ACTIVE' THEN 2  
    WHEN STATUS='FROZEN' THEN 3
  END AS STATUS,
  USERNAME
FROM USER_DETAIL;

-- Migrate Study2User
DELETE FROM STUDY_USER;

INSERT INTO STUDY_USER
  SELECT USER_ID, STUDY_ID
FROM STUDY2USER;
