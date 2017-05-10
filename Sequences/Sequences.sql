DROP SEQUENCE MailIdSequence;
CREATE SEQUENCE MailIdSequence
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 50;

DROP SEQUENCE PMIDSEQUENCE;
CREATE SEQUENCE PmIdSequence
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 50;
  
DROP SEQUENCE CLIDSEQUENCE;
CREATE SEQUENCE ClIdSequence
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 50;
  
DROP SEQUENCE CAIDSEQUENCE;
CREATE SEQUENCE CaIdSequence
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 50;
  
  
  
Drop sequence BuildingIdSequence;
CREATE SEQUENCE BuildingIdSequence
  MINVALUE 1
  MAXVALUE 2
  START WITH 1
  INCREMENT BY 1
  CACHE 50;

Drop sequence PMExtensionSequence;
CREATE SEQUENCE PMExtensionSequence
  MINVALUE 2000
  MAXVALUE 3000
  START WITH 2000
  INCREMENT BY 1
  CACHE 50;
Drop sequence PMOfficeNumSequence;
  CREATE SEQUENCE PMOfficeNumSequence
  MINVALUE 1
  MAXVALUE 100
  START WITH 1
  INCREMENT BY 1
  CACHE 50;
Drop sequence CLOfficeNumSequence;
    CREATE SEQUENCE CLOfficeNumSequence
  MINVALUE 101
  MAXVALUE 200
  START WITH 101
  INCREMENT BY 1
  CACHE 50;

Drop sequence CLExtensionSequence;  
  CREATE SEQUENCE CLExtensionSequence
  MINVALUE 1000
  MAXVALUE 1999
  START WITH 1000
  INCREMENT BY 1
  CACHE 50;
  
Drop sequence MailIdSequence;    
  CREATE SEQUENCE MailIdSequence
  MINVALUE 0001
  START WITH 0001
  INCREMENT BY 0001
  CACHE 50;
  
Drop sequence RouteIdSequence;  
  CREATE SEQUENCE RouteIdSequence
  MINVALUE 0001
  START WITH 0001
  INCREMENT BY 0001
  CACHE 50;

Drop sequence ScheduleIdSequence;
  CREATE SEQUENCE ScheduleIdSequence
  MINVALUE 0001
  START WITH 0001
  INCREMENT BY 0001
  CACHE 50;

Drop sequence VehicleIdSequence;  
  CREATE SEQUENCE VehicleIdSequence
  MINVALUE 0001
  START WITH 0001
  INCREMENT BY 0001
  CACHE 50;
Drop sequence VehicleStatusIdSequence;   
  CREATE SEQUENCE VehicleStatusIdSequence
  MINVALUE 0001
  START WITH 0001
  INCREMENT BY 0001
  CACHE 50;