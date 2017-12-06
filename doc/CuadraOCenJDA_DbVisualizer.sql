DROP TABLE BJPROYEC.Q_40REV3;

CREATE TABLE BJPROYEC.Q_40REV3 AS (SELECT ponumb, POCORR, POFLEN, POEREN, POFECE, POHORE, pojob, POVNUM, POEDAT, POFECG, POHORG FROM exisbugd.exiff40a A WHERE a.pocorr = 
(select max(b.pocorr) from exisbugd.exiff40a B where a.ponumb = b.ponumb) and POFECG >= 20170403 and pofecg <= 20170403) WITH DATA ; 

DELETE FROM BJPROYEC.Q_40REV3 WHERE POJOB = 'PO_CANCEL';


delete from bjproyec.Q_40REV3  a where not exists (select 1 from mmsp4lib.pomhdr b where a.ponumb = b.ponumb);

delete from bjproyec.Q_40REV3  a where  exists (select 1 from mmsp4lib.pomhdr b where a.ponumb = b.ponumb and b.postat <> '3' );          

delete from bjproyec.Q_40REV3  a where  exists (select 1 from exisbugd.exiff40cl2 b where a.ponumb = b.ponumb);

drop table bjproyec.Q40REVb;

Create table bjproyec.Q40REVb AS (SELECT A.PONUMB, A.POCORR, A.POFLEN, A.POEREN, A.POFECE, A.POHORE, A.POJOB, A.POVNUM, A.POEDAT, A.POFECG, A.POHORG, B.EX14ESTA, B.EX14ERROR, B.EX14FREGI, B.EX14HOREG FROM bjproyec.Q_40REV3 a left outer join exisbugd.EXMHF14IB B ON CHAR(a.ponumb) = b.EX14IDEN) with data;

ALTER TABLE BJPROYEC.Q40REVb  ADD COLUMN DESPA CHARACTER (3 ) NOT  
NULL WITH DEFAULT  ;    
ALTER TABLE BJPROYEC.Q40REVb  ADD COLUMN NUMDO CHARACTER (20 ) NOT  
NULL WITH DEFAULT  ;    


update bjproyec.Q40REVb  A                                     
     set a.DESPA =(select max(b.RELDSP)                   
                   from EXISBUGD.EXMHF56A  B              
                  where b.relord = a.ponumb)        
   where a.ponumb in (select   c.relord                
                      from exisbugd.exmhf56a c )   ;     

update bjproyec.Q40REVb  A                                     
     set a.NUMDO =(select max(b.RELBL5)                   
                   from EXISBUGD.EXMHF56A  B              
                  where b.relord = a.ponumb)        
   where a.ponumb in (select   c.relord                
                      from exisbugd.exmhf56a c )   ;           

SELECT PONUMB, POVNUM, NUMDO, POFECE, POHORE, EX14ERROR, EX14FREGi, DESPA FROM bjproyec.Q40REVb;
