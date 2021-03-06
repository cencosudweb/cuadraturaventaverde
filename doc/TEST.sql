--select  N_Orden_Distribu  from CUADRATURA_VENTAV_EOM A  WHERE N_Orden_Distribu NOT IN (SELECT NUMDO FROM CUADRATURA_VENTAV_JDA B WHERE B.NUMDO = A.n_orden_distribu);


SELECT 
  (select CVV_EOM_2.Solicitudoriginal from CUADRATURA_VENTAV_EOM CVV_EOM_2 where CVV_EOM_2.N_Orden_Distribu = CVV_EOM.N_Orden_Distribu) AS Solicitudoriginal,
  CVV_EOM.N_Orden_Distribu,
  (select to_char(REPLACE(CVV_EOM_2.Fecha_Creacion_Orden,',000000000',''))  from CUADRATURA_VENTAV_EOM CVV_EOM_2 where CVV_EOM_2.N_Orden_Distribu = CVV_EOM.N_Orden_Distribu) AS FECHA_CREACION_ORDEN,
  (select CVV_EOM_2.CANCELADO from CUADRATURA_VENTAV_EOM CVV_EOM_2 where CVV_EOM_2.N_Orden_Distribu = CVV_EOM.N_Orden_Distribu) AS CANCELADO,
  
  CASE WHEN (SELECT CVV_JDA.NUMDO FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170418' AND CVV_JDA.Pofece <= '20170418' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu) IS NOT NULL THEN 'Encontrado' ELSE 'No Encontrado' END    as ENCONTRADOENJDA,
  CASE WHEN (SELECT CVV_JDA.NUMDO FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170418' AND CVV_JDA.Pofece <= '20170418' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu) IS NOT NULL THEN (SELECT CVV_JDA.NUMDO FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170418' AND CVV_JDA.Pofece <= '20170418' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu) ELSE 'Numero solicitud Jda No Encontrado' END    as NUMEROSOLICITUDJDA,
  
  CASE WHEN (SELECT CVV_JDA.EX14ERROR FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170418' AND CVV_JDA.Pofece <= '20170418' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu) IS NOT NULL THEN (SELECT CVV_JDA.EX14ERROR FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170418' AND CVV_JDA.Pofece <= '20170418' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu) ELSE 'Mensaje de Error Jda No Encontrado'  END    as MENSAJEERRORJDA,
  CASE WHEN (SELECT CVV_JDA.Pofece FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170418' AND CVV_JDA.Pofece <= '20170418' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu) IS NOT NULL THEN (SELECT CVV_JDA.Pofece FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170418' AND CVV_JDA.Pofece <= '20170418' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu) ELSE 'Fecha Jda No Encontrado'  END as POFECEJDA
  --NVL((SELECT CVV_JDA.NUMDO FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170417' AND CVV_JDA.Pofece <= '20170417' AND  CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu),'----'),   
  --NVL((SELECT CVV_JDA.EX14ERROR FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170417' AND CVV_JDA.Pofece <= '20170417' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu),'----'),
  --NVL((SELECT CVV_JDA.Pofece FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.Pofece >= '20170417' AND CVV_JDA.Pofece <= '20170417' AND CVV_JDA.NUMDO = CVV_EOM.n_orden_distribu),'----')

from CUADRATURA_VENTAV_EOM CVV_EOM where CVV_EOM.fecha_creacion_orden >= '18/04/17 00:00:00' and CVV_EOM.fecha_creacion_orden <= '18/04/17 23:59:59'
group by CVV_EOM.N_Orden_Distribu ORDER BY CVV_EOM.N_Orden_Distribu ASC


--(SELECT CVV_JDA.NUMDO FROM CUADRATURA_VENTAV_JDA CVV_JDA WHERE CVV_JDA.NUMDO = '118602985')
--TO_CHAR(INVWMS.MOD_DATE_TIME, 'DD/MM/YYYY')
--SELECT TO_CHAR('03/04/17 09:00:07,000000000', 'DD/MM/YYYY') FROM DUAL;
--SELECT TO_CHAR(INVWMS.MOD_DATE_TIME, 'DD/MM/YYYY') FROM INVENTARIO_WMS INVWMS
--SELECT *  FROM INVENTARIO_WMS INVWMS


select * from CUADRATURA_OC_VTAV_EOM
select * from CUADRATURA_OC_VTAV_JDA

