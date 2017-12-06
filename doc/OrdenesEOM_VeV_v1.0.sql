SELECT  PO.tc_purchase_orders_id  as SolicitudOriginal
       ,O.tc_order_id             as N_Orden_Distribu
       ,o.Purchase_Order_id       as Solicitud
       ,TO_CHAR(O.created_dttm, 'DD-MM-YYYY HH24:MI:SS') AS Fecha_Creacion_Orden
       ,o.O_FACILITY_ALIAS_ID     as Origen
       ,CASE 
           WHEN o.Is_Cancelled = 1 THEN 'Cancelado'
           ELSE 'No Cancelado' END    as Cancelado
FROM CA14.orders o
INNER JOIN CA14.Purchase_Orders PO
        ON O.Purchase_Order_Id = Po.Purchase_Orders_Id
  
WHERE o.o_facility_alias_id  NOT IN ('012', '200', '400')
  AND O.created_dttm >= to_date('14-03-2017 00:00:01','DD-MM-YYYY HH24:MI:SS')
  AND O.created_dttm <= to_date('15-03-2017 23:59:59','DD-MM-YYYY HH24:MI:SS');
