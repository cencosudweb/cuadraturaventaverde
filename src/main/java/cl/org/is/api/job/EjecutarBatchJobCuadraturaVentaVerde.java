/**
 *@name CopiarArchivosCuadraturaJob2.java
 * 
 *@version 1.0 
 * 
 *@date 30-03-2017
 * 
 *@author EA7129
 * 
 *@copyright Cencosud. All rights reserved.
 */
package cl.org.is.api.job;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;

/**
 * @description 
 */
public class EjecutarBatchJobCuadraturaVentaVerde {
	
	private static BufferedWriter bw;
	private static String path;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map <String, String> mapArguments = new HashMap<String, String>();
		String sKeyAux = null;

		for (int i = 0; i < args.length; i++) {

			if (i % 2 == 0) {

				sKeyAux = args[i];
			}
			else {

				mapArguments.put(sKeyAux, args[i]);
			}
		}

		try {

			File info              = null;
			File miDir             = new File(".");
			path                   =  miDir.getCanonicalPath();
			//path                   =  "C:\\PROYECTOS\\C8INVENTARIOS\\MASIVOS\\";
			//info                   = new File(path+"/LOG/info.txt");
			info                   = new File(path+"/info.txt");
			bw = new BufferedWriter(new FileWriter(info));
			info("El programa se esta ejecutando...");
			crearTxt(mapArguments);
			System.out.println("El programa finalizo.");
			info("El programa finalizo.");
			bw.close();
		}
		catch (Exception e) {

			System.out.println(e.getMessage());
		}
	}
	
	private static void crearTxt(Map<String, String> mapArguments) {
		// TODO Auto-generated method stub
		Connection dbconnection = crearConexion();
		Connection dbconnOracle = crearConexionOracle();
		Connection dbconnOracle2 = crearConexionOracle2();
		
		
		//File fileComparativo              = null;
		File fileEom              = null;
		File fileJda              = null;
		File fileJdaStock              = null;
		File fileJdaComp              = null;
		
		
		BufferedWriter bwComparativo       = null;
		BufferedWriter bwEom      = null;
		BufferedWriter bwJda      = null;
		BufferedWriter bwJdaStock      = null;
		BufferedWriter bwJdaComp      = null;
		
		PreparedStatement pstmtComparativo = null;
		PreparedStatement pstmtJda = null;
		PreparedStatement pstmtJdaStock = null;
		PreparedStatement pstmtJdaComp = null;
		
		PreparedStatement pstmtEom = null;
		PreparedStatement pstmtEomInsert = null;
		PreparedStatement pstmtJdaInsert = null;
		PreparedStatement pstmtJdaStockInsert = null;
		PreparedStatement pstmtJdaInsertComp = null;

		StringBuffer sbEom         = null;
		StringBuffer sbJda         = null;
		StringBuffer sbJdaStock         = null;
		StringBuffer sbJdaComp         = null;
		
		Date now3 = new Date();
		SimpleDateFormat ft3 = new SimpleDateFormat ("YYYYMMdd");
		String currentDate3 = ft3.format(now3);
		
		
		Date nowPro = new Date();
		SimpleDateFormat ftPro = new SimpleDateFormat ("YYYY-MM-dd HH:mm:ss");
		String currentDatePro = ftPro.format(nowPro);
		
		
		String fechaEom = String.valueOf(restarDia(currentDate3));
		String fechaEom2 = String.valueOf((currentDate3));
		
		//System.out.println("currentDatePro="+currentDatePro);
		//System.out.println("=1="+fechaEom.substring(6,8)+"-"+fechaEom.substring(4,6)+"-"+fechaEom.substring(0,4));
		//System.out.println("=2="+fechaEom2.substring(6,8)+"-"+fechaEom2.substring(4,6)+"-"+fechaEom2.substring(0,4));
		//System.out.println("=2="+fechaEom2.substring(6,8)+"/"+fechaEom2.substring(4,6)+"/"+fechaEom2.substring(2,4));

		try {

			try {
				//iFechaIni = restarDia(mapArguments.get("-fi"));
				//iFechaFin = restarDia(mapArguments.get("-ff"));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("currentDate3: " + currentDate3);
			
			Thread.sleep(60);
			System.out.println("Pausa para Insertar  HORARIO_VTAV_ACT sleep(60 seg)");						
			agregarRegistroProceso(dbconnOracle2, currentDatePro, "1");
			
			Thread.sleep(60);
			System.out.println("Pausa para Insertar  HORARIO_VTAV_ACT_STOCK sleep(60 seg)");						
			agregarRegistroProcesoStock(dbconnOracle2, currentDatePro, "1");
			
			Thread.sleep(60);
			System.out.println("Pausa para Eliminar CUADRATURA_OC_VTAV_EOM sleep(60 seg)");						
			elimnarInventario(dbconnOracle2,"DELETE FROM  CUADRATURA_OC_VTAV_EOM  where 1 = 1 AND FECHA_CREACION_ORDEN >= '"+fechaEom.substring(0,4)+"-"+fechaEom.substring(4,6)+"-"+fechaEom.substring(6,8)+" 00:00:00' AND FECHA_CREACION_ORDEN <= '"+fechaEom2.substring(0,4)+"-"+fechaEom2.substring(4,6)+"-"+fechaEom2.substring(6,8)+" 23:59:59' ");
			
			commit(dbconnOracle2,"COMMIT");
			
			Thread.sleep(60);
			System.out.println("Pausa para Eliminar CUADRATURA_OC_VTAV_JDA_STOCK sleep(60 seg)");						
			elimnarInventario(dbconnOracle2,"DELETE FROM  CUADRATURA_OC_VTAV_JDA_STOCK where 1 = 1 AND POFECE >= '"+restarDia(currentDate3)+"' and POFECE <= '"+(currentDate3)+"'");
			//elimnarInventario(dbconnOracle2,"DELETE FROM  CUADRATURA_OC_VTAV_JDA_STOCK where 1 = 1 AND POFECE >= '20170423' and POFECE <= '20170504' ");
			//elimnarInventario(dbconnOracle2,"DELETE FROM  CUADRATURA_OC_VTAV_JDA_STOCK_T where 1 = 1 AND POFECE >= '"+restarDia(currentDate3)+"' and POFECE <= '"+(currentDate3)+"'");

			commit(dbconnOracle2,"COMMIT");
			
			//fileComparativo           = new File(path + "/InventarioComparativo-" + currentDate3 + ".txt");
			fileEom                   = new File(path + "/InventarioEom-" + currentDate3 + ".txt");
			fileJda                   = new File(path + "/InventarioJda-" + currentDate3 + ".txt");
			fileJdaStock                   = new File(path + "/InventarioJdaStock-" + currentDate3 + ".txt");
			
			//sbComparativo = new StringBuffer();
			sbEom = new StringBuffer();
			sbJda = new StringBuffer();
			sbJdaStock = new StringBuffer();
			
			
			//********************--CARGAR JDA--**********************
			
			Thread.sleep(60);
			System.out.println("Pausa para el proceso de obtener datos del JDA sleep(60 seg)");
			
			eliminarTablaRev3(dbconnection);
			crearTablaRev3(dbconnection);
			eliminarTablaRev3Pojob(dbconnection);
			eliminarRev3NotExistInPomhdr(dbconnection);
			eliminarRev3NotExistInPomhdrPostat(dbconnection);
			eliminarRev3NotExistInExiff(dbconnection);
			eliminarTablaRevb(dbconnection);
			crearTablaRevb(dbconnection);
			alterarTablaRevbDespacho(dbconnection);
			alterarTablaRevbNumdo(dbconnection);
			actualizarTablaRevbDespacho(dbconnection);
			actualizarTablaRevbNumdo(dbconnection);
			
			String sqlJdaIsert = "Insert into CUADRATURA_OC_VTAV_JDA (PONUMB,POVNUM,NUMDO,POFECE,POHORE,EX14ERROR,EX14FREGi,DESPA) values (?,?,?,?,?,?,?,?)";
			//String sqlJdaIsert = "Insert into CUADRATURA_OC_VTAV_JDA_T (PONUMB,POVNUM,NUMDO,POFECE,POHORE,EX14ERROR,EX14FREGi,DESPA) values (?,?,?,?,?,?,?,?)";

			pstmtJdaInsert = dbconnOracle2.prepareStatement(sqlJdaIsert);
			//sbJda.append("SELECT A.PONUMB, A.POVNUM, A.NUMDO, A.POFECE, A.POHORE, A.EX14ERROR, A.EX14FREGi, A.DESPA FROM bjproyec.Q40REVb A");
			sbJda.append("SELECT A.PONUMB , A.POVNUM, CASE WHEN A.NUMDO = '' OR A.NUMDO IS NULL THEN  '-1' ELSE A.NUMDO END as NUMDO, A.POFECE, A.POHORE, A.EX14ERROR, A.EX14FREGi, A.DESPA FROM bjproyec.Q_VALPS2 A");
			System.out.println("Sql sbJda: " + sbJda);
			info("Sql sbJda: " + sbJda);
			pstmtJda         = dbconnection.prepareStatement(sbJda.toString());
			sbJda = new StringBuffer();
			ResultSet rsJda = pstmtJda.executeQuery();
			bwJda  = new BufferedWriter(new FileWriter(fileJda));
			bwJda.write("ESTADO;");
			bwJda.write("PONUMB;");
			bwJda.write("POVNUM;");
			bwJda.write("NUMDO;");
			bwJda.write("POFECE;");
			bwJda.write("POHORE;");
			bwJda.write("EX14ERROR;");
			bwJda.write("EX14FREGi;");
			bwJda.write("DESPA;\n");
			
			while (rsJda.next()) {
				if (  rsJda.getFloat("PONUMB") > 0 && rsJda.getString("POVNUM") != null && rsJda.getString("NUMDO") != null && !"-1".equals(rsJda.getString("NUMDO")) ) {
					bwJda.write(ejecutarQuery2(	rsJda.getFloat("PONUMB"), rsJda.getString("POVNUM"), rsJda.getString("NUMDO").trim(), rsJda.getString("POFECE"), 
												rsJda.getString("POHORE"), rsJda.getString("EX14ERROR"), rsJda.getString("EX14FREGi"), rsJda.getString("DESPA"), dbconnOracle2, pstmtJdaInsert));
					pstmtJdaInsert.executeUpdate();
					commit(dbconnOracle2,"COMMIT");	
				} else {
					System.out.println("NO HACER NADA");
				}
			}
			
			
			Thread.sleep(60);
			System.out.println("Pausa para Eliminar CUADRATURA_OC_VTAV_JDA sleep(60 seg)");
			
			elimnarInventario(dbconnOracle2,"DELETE FROM  CUADRATURA_OC_VTAV_JDA where 1 = 1 AND PONUMB = 'NULO'");
			//elimnarInventario(dbconnOracle2,"DELETE FROM  CUADRATURA_OC_VTAV_EOM_TEMP where 1 = 1 AND PONUMB = 'NULO'");
			//elimnarInventario(dbconnOracle2,"DELETE FROM  CUADRATURA_OC_VTAV_JDA_T where 1 = 1 AND PONUMB = 'NULO'");
			System.out.println("SQL: " + "DELETE FROM  CUADRATURA_OC_VTAV_JDA where 1 = 1  AND PONUMB = 'NULO'");
			commit(dbconnOracle2,"COMMIT");
			
			
			Thread.sleep(60);
			System.out.println("Pausa para Insertar  HORARIO_VTAV_ACT_STOCK sleep(60 seg)");						
			acutalizarRegistroProceso(dbconnOracle2, "2");
			
			
			//CARGAR EOM
			
			Thread.sleep(60);
			System.out.println("Regreso del Proceso EOM sleep(60 seg)");
			
			String sqlWmsInsert = "Insert into CUADRATURA_OC_VTAV_EOM (SOLICITUDORIGINAL,N_ORDEN_DISTRIBU,SOLICITUD,FECHA_CREACION_ORDEN,ORIGEN,CANCELADO) values (?,?,?,?,?,?)";
			//String sqlWmsInsert = "Insert into CUADRATURA_OC_VTAV_EOM_T (SOLICITUDORIGINAL,N_ORDEN_DISTRIBU,SOLICITUD,FECHA_CREACION_ORDEN,ORIGEN,CANCELADO) values (?,?,?,?,?,?)";
			

			pstmtEomInsert = dbconnOracle2.prepareStatement(sqlWmsInsert);
			sbEom.append("SELECT  PO.tc_purchase_orders_id  as SolicitudOriginal,O.tc_order_id as N_Orden_Distribu,o.Purchase_Order_id as Solicitud,TO_CHAR(O.created_dttm, 'YYYY-MM-DD HH24:MI:SS.FF') AS Fecha_Creacion_Orden,o.O_FACILITY_ALIAS_ID as Origen, CASE WHEN o.Is_Cancelled = 1 THEN 'Cancelado' ELSE 'No Cancelado' END    as Cancelado FROM CA14.orders o INNER JOIN CA14.Purchase_Orders PO ON O.Purchase_Order_Id = Po.Purchase_Orders_Id LEFT JOIN  CA14.order_type ot ON ot.order_type_id =  po.order_category  WHERE o.o_facility_alias_id  NOT IN ('012', '200', '400') AND (ot.order_type <> 'Pickup') AND O.created_dttm >= to_date('"+fechaEom.substring(6,8)+"-"+fechaEom.substring(4,6)+"-"+fechaEom.substring(0,4)+" 00:00:01','DD-MM-YYYY HH24:MI:SS') AND O.created_dttm <= to_date('"+fechaEom2.substring(6,8)+"-"+fechaEom2.substring(4,6)+"-"+fechaEom2.substring(0,4)+" 23:59:59','DD-MM-YYYY HH24:MI:SS')");
			//sbEom.append("SELECT  PO.tc_purchase_orders_id  as SolicitudOriginal,O.tc_order_id as N_Orden_Distribu,o.Purchase_Order_id as Solicitud,TO_CHAR(O.created_dttm, 'YYYY-MM-DD HH24:MI:SS.FF') AS Fecha_Creacion_Orden,o.O_FACILITY_ALIAS_ID as Origen, CASE WHEN o.Is_Cancelled = 1 THEN 'Cancelado' ELSE 'No Cancelado' END    as Cancelado FROM CA14.orders o INNER JOIN CA14.Purchase_Orders PO ON O.Purchase_Order_Id = Po.Purchase_Orders_Id LEFT JOIN  order_type ot ON ot.order_type_id =  po.order_category  WHERE o.o_facility_alias_id  NOT IN ('012', '200', '400') AND (ot.order_type <> 'Pickup') AND O.created_dttm >= to_date('23-04-2017 00:00:01','DD-MM-YYYY HH24:MI:SS') AND O.created_dttm <= to_date('05-05-2017 23:59:59','DD-MM-YYYY HH24:MI:SS')");
			info("Sql sbEom: " + sbEom);
			pstmtEom         = dbconnOracle.prepareStatement(sbEom.toString());
			sbEom = new StringBuffer();
			ResultSet rsEom = pstmtEom.executeQuery();
			
			bwEom  = new BufferedWriter(new FileWriter(fileEom));
			bwEom.write("SolicitudOriginal;");
			bwEom.write("N_Orden_Distribu;");
			bwEom.write("Solicitud;");
			bwEom.write("Fecha_Creacion_Orden;");
			bwEom.write("Origen;");
			bwEom.write("Cancelado;\n");
			
			while (rsEom.next()) {
				bwEom.write(rsEom.getString("SolicitudOriginal") + ";");
				bwEom.write(rsEom.getString("N_Orden_Distribu") + ";");
				bwEom.write(rsEom.getString("Solicitud") + ";");
				bwEom.write(rsEom.getString("Fecha_Creacion_Orden") + ";");
				bwEom.write(rsEom.getString("Origen") + ";");
				bwEom.write(rsEom.getString("Cancelado") + "\n");
				
				pstmtEomInsert.setString(1, rsEom.getString("SolicitudOriginal"));
				pstmtEomInsert.setString(2, rsEom.getString("N_Orden_Distribu"));
				pstmtEomInsert.setString(3, rsEom.getString("Solicitud"));
				pstmtEomInsert.setString(4, rsEom.getString("Fecha_Creacion_Orden"));
				pstmtEomInsert.setString(5, rsEom.getString("Origen"));
				pstmtEomInsert.setString(6, rsEom.getString("Cancelado"));
				
				pstmtEomInsert.executeUpdate();
				commit(dbconnOracle2,"COMMIT");
				
			}
			
			//********************--CARGAR JDA STOCK
			
			Thread.sleep(60);
			System.out.println("Pausa para Insertar  JDA STOCK sleep(60 seg)");
			
			eliminarTablaRev3(dbconnection);
			crearTablaRev3(dbconnection);
			eliminarTablaRev3Pojob(dbconnection);
			eliminarRev3NotExistInPomhdr(dbconnection);
			eliminarRev3NotExistInPomhdrPostat(dbconnection);
			eliminarRev3NotExistInExiff(dbconnection);
			eliminarTablaRevb(dbconnection);
			crearTablaRevb(dbconnection);
			alterarTablaRevbDespacho(dbconnection);
			alterarTablaRevbNumdo(dbconnection);
			actualizarTablaRevbDespacho(dbconnection);
			actualizarTablaRevbNumdo(dbconnection);
			
			String sqlJdaStock = "Insert into CUADRATURA_OC_VTAV_JDA_STOCK (PONUMB,POVNUM,NUMDO,POFECE,POHORE,EX14ERROR,EX14FREGi,DESPA) values (?,?,?,?,?,?,?,?)";
			pstmtJdaStockInsert = dbconnOracle2.prepareStatement(sqlJdaStock);
			sbJdaStock.append("SELECT CASE WHEN A.PONUMB IS NULL THEN  '-1' ELSE A.PONUMB END AS PONUMB, CASE WHEN A.POVNUM IS NULL THEN  '-1' ELSE A.POVNUM END AS POVNUM, CASE WHEN A.NUMDO IS NULL THEN  '-1' ELSE A.NUMDO END as NUMDO, CASE WHEN A.POFECE IS NULL THEN  '-1' ELSE A.POFECE END AS POFECE, CASE WHEN A.POHORE IS NULL THEN  '-1' ELSE A.POHORE END AS POHORE, CASE WHEN A.EX14ERROR IS NULL THEN  '-1' ELSE A.EX14ERROR END AS EX14ERROR, CASE WHEN A.EX14FREGi IS NULL THEN  '-1' ELSE A.EX14FREGi END AS EX14FREGi, CASE WHEN A.DESPA IS NULL THEN  '-1' ELSE A.DESPA END AS DESPA FROM bjproyec.Q_VALPS2 A");
			System.out.println("SqlsbJdaStock: " + sbJdaStock);
			pstmtJdaStock         = dbconnection.prepareStatement(sbJdaStock.toString());
			sbJdaStock = new StringBuffer();
			ResultSet rsJdaStock = pstmtJdaStock.executeQuery();
			
			bwJdaStock  = new BufferedWriter(new FileWriter(fileJdaStock));
			bwJdaStock.write("ESTADO;");
			bwJdaStock.write("PONUMB;");
			bwJdaStock.write("POVNUM;");
			bwJdaStock.write("NUMDO;");
			bwJdaStock.write("POFECE;");
			bwJdaStock.write("POHORE;");
			bwJdaStock.write("EX14ERROR;");
			bwJdaStock.write("EX14FREGi;");
			bwJdaStock.write("DESPA;\n");
			
			while (rsJdaStock.next()) {
				bwJdaStock.write(rsJdaStock.getString("PONUMB") + ";");
				bwJdaStock.write(rsJdaStock.getString("POVNUM") + ";");
				bwJdaStock.write(rsJdaStock.getString("NUMDO") + ";");
				bwJdaStock.write(rsJdaStock.getString("POFECE") + ";");
				bwJdaStock.write(rsJdaStock.getString("POHORE") + ";");
				bwJdaStock.write(rsJdaStock.getString("EX14ERROR") + ";");
				bwJdaStock.write(rsJdaStock.getString("EX14FREGi") + ";");
				bwJdaStock.write(rsJdaStock.getString("DESPA") + "\n");
				
				pstmtJdaStockInsert.setString(1, rsJdaStock.getString("PONUMB"));
				pstmtJdaStockInsert.setString(2, rsJdaStock.getString("POVNUM"));
				pstmtJdaStockInsert.setString(3, rsJdaStock.getString("NUMDO").trim());
				pstmtJdaStockInsert.setString(4, rsJdaStock.getString("POFECE"));
				pstmtJdaStockInsert.setString(5, rsJdaStock.getString("POHORE"));
				pstmtJdaStockInsert.setString(6, rsJdaStock.getString("EX14ERROR"));
				pstmtJdaStockInsert.setString(7, rsJdaStock.getString("EX14FREGi"));
				pstmtJdaStockInsert.setString(8, rsJdaStock.getString("DESPA"));
				pstmtJdaStockInsert.executeUpdate();
				commit(dbconnOracle2,"COMMIT");
			}
			
			
			Thread.sleep(60);
			System.out.println("Pausa para Insertar  HORARIO_VTAV_ACT_STOCK sleep(60 seg)");						
			acutalizarRegistroProcesoStock(dbconnOracle2, "2");
			
			
			
			///###########################ARCHIVO JDA COMPA###########################
			
			fileJdaComp                   = new File(path + "/InventarioJdaComp-" + currentDate3 + ".txt");
			
			Thread.sleep(60);
			System.out.println("Pausa para Eliminar  CUADRATURA_OC_VTAV_COMP sleep(60 seg)");	
			elimnarInventario(dbconnOracle2,"DELETE FROM  CUADRATURA_OC_VTAV_COMP where 1 = 1 AND FECHA_INGRESO = '"+currentDate3+"'");
			System.out.println("DELETE FROM  CUADRATURA_OC_VTAV_COMP where 1 = 1 AND FECHA_INGRESO = '"+currentDate3+"'");
			sbJdaComp = new StringBuffer();
			
			Thread.sleep(60);
			System.out.println("Pausa para Insertar  CUADRATURA_OC_VTAV_COMP sleep(60 seg)");	
			String sqlJdaComp = "Insert into CUADRATURA_OC_VTAV_COMP (RELNUM,POMUMB,INUMBR,RELBL5,CANTIDAD_COMP1,CANTIDAD_COMP2,FECHA_INGRESO) values (?,?,?,?,?,?,?)";
			pstmtJdaInsertComp = dbconnOracle2.prepareStatement(sqlJdaComp);
			System.out.println("sqlJdaComp: " + sqlJdaComp);
			
			sbJdaComp.append("select relnum,ponumb,inumbr,relbl5,sum(relcnt) as compara1, max(pomqty) as compara2 from exisbugd.exmhf56a a inner join mmsp4lib.pomdtl b on a.relord=b.ponumb and a.relmbr = b.inumbr where relemi ="+currentDate3+" group by relnum,ponumb,relbl5,inumbr ");
			//sbJdaComp.append("select relnum,ponumb,inumbr,relbl5,sum(relcnt) as compara1, max(pomqty) as compara2 from exisbugd.exmhf56a a inner join mmsp4lib.pomdtl b on a.relord=b.ponumb and a.relmbr = b.inumbr where relemi ="+"20170616"+" group by relnum,ponumb,relbl5,inumbr ");
			
			pstmtJdaComp         = dbconnection.prepareStatement(sbJdaComp.toString());

			sbJdaComp = new StringBuffer();
			ResultSet rsJdaComp = pstmtJdaComp.executeQuery();
			
			bwJdaComp  = new BufferedWriter(new FileWriter(fileJdaComp));
			bwJdaComp.write("RELNUM;");
			bwJdaComp.write("POMUMB;");
			bwJdaComp.write("INUMBR;");
			bwJdaComp.write("RELBL5;");
			bwJdaComp.write("CANTIDAD_COMP1;");
			bwJdaComp.write("CANTIDAD_COMP2;");
			bwJdaComp.write("FECHA_INGRESO;\n");
			
			while (rsJdaComp.next()) {
				
				bwJdaComp.write(rsJdaComp.getFloat("relnum") + ";");
				bwJdaComp.write(rsJdaComp.getFloat("ponumb") + ";");
				bwJdaComp.write(rsJdaComp.getFloat("inumbr") + ";");
				bwJdaComp.write(rsJdaComp.getInt("relbl5") + ";");
				bwJdaComp.write(rsJdaComp.getInt("compara1") + ";");
				bwJdaComp.write(rsJdaComp.getInt("compara2") + ";");
				bwJdaComp.write(currentDate3 + "\n");
				
				
				pstmtJdaInsertComp.setFloat(1, rsJdaComp.getFloat("relnum"));
				pstmtJdaInsertComp.setFloat(2, rsJdaComp.getFloat("ponumb"));
				pstmtJdaInsertComp.setFloat(3, rsJdaComp.getFloat("inumbr"));
				pstmtJdaInsertComp.setInt(4, rsJdaComp.getInt("relbl5"));
				pstmtJdaInsertComp.setInt(5, rsJdaComp.getInt("compara1"));
				pstmtJdaInsertComp.setInt(6, rsJdaComp.getInt("compara2"));
				pstmtJdaInsertComp.setString(7, currentDate3);
				pstmtJdaInsertComp.executeUpdate();
				commit(dbconnOracle2,"COMMIT");
			}
			
						


			info("Archivos creados.");
		}
		catch (Exception e) {

			System.out.println(e.getMessage());
			info("[crearTxt1]Exception:"+e.getMessage());
		}
		finally {

			cerrarTodo(dbconnOracle,pstmtComparativo,bwComparativo);
			cerrarTodo(dbconnOracle, pstmtEom, bwEom);
			cerrarTodo(dbconnection, pstmtJda, bwJda);
			cerrarTodo(dbconnection, pstmtJdaStock, bwJdaStock);
		}
		
	}
	
	/**
	 * Metodo que agregar un registro para monitorear los procesos a cada hora 
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @param String, fecha actual del sistema
	 * @return String, estado actual de la tabla
	 * 
	 */
	private static void agregarRegistroProcesoStock(Connection dbconnection, String fecha, String estado){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarTablaRev3Pojob]");
			
			sb = new StringBuffer();
			//sb.append("DELETE FROM BJPROYEC.Q_40REV3 WHERE POJOB = 'PO_CANCEL'");
			sb.append("INSERT INTO HORARIO_VTAV_ACT_STOCK (FECHA, ESTADO) VALUES ('"+fecha+"', '"+estado+"')");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Registros agregADOS HORARIO_VTAV_ACT_STOCK: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarTablaRev3Pojob]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarTablaRev3Pojob]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	
	/**
	 * Metodo que agregar un registro para monitorear los procesos a cada hora 
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @param String, fecha actual del sistema
	 * @return String, estado actual de la tabla
	 * 
	 */
	private static void agregarRegistroProceso(Connection dbconnection, String fecha, String estado){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarTablaRev3Pojob]");
			
			sb = new StringBuffer();
			//sb.append("DELETE FROM BJPROYEC.Q_40REV3 WHERE POJOB = 'PO_CANCEL'");
			sb.append("INSERT INTO HORARIO_VTAV_ACT (FECHA, ESTADO) VALUES ('"+fecha+"', '"+estado+"')");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Registros agrados HORARIO_VTAV_ACT: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarTablaRev3Pojob]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarTablaRev3Pojob]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que agregar un registro para monitorear los procesos a cada hora 
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @param String, fecha actual del sistema
	 * @return String, estado actual de la tabla
	 * 
	 */
	private static void acutalizarRegistroJda(Connection dbconnection,  String description, String id){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarTablaRev3Pojob]");
			
			sb = new StringBuffer();
			//sb.append("DELETE FROM BJPROYEC.Q_40REV3 WHERE POJOB = 'PO_CANCEL'");
			sb.append("UPDATE  CUADRATURA_OC_VTAV_JDA SET EX14ERROR = '"+description+"' WHERE ID = '"+id+"'");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("UPDATE  CUADRATURA_OC_VTAV_JDA SET EX14ERROR = '"+description+"' WHERE ID = '"+id+"'");	
			
			info("Registros actualizados en JDA: "+pstmt.executeUpdate());	

			info("[Fin Metodo acutalizarRegistroJda]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo acutalizarRegistroJda]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que agregar un registro para monitorear los procesos a cada hora 
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @param String, fecha actual del sistema
	 * @return String, estado actual de la tabla
	 * 
	 */
	private static void acutalizarRegistroProceso(Connection dbconnection,  String estado){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarTablaRev3Pojob]");
			
			sb = new StringBuffer();
			//sb.append("DELETE FROM BJPROYEC.Q_40REV3 WHERE POJOB = 'PO_CANCEL'");
			sb.append("UPDATE  HORARIO_VTAV_ACT SET ESTADO = '"+estado+"'");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Registros eliminados donde campo POJOB ES =  'PO_CANCEL' de BJPROYEC.Q_40REV3: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarTablaRev3Pojob]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarTablaRev3Pojob]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que agregar un registro para monitorear los procesos a cada hora 
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @param String, fecha actual del sistema
	 * @return String, estado actual de la tabla
	 * 
	 */
	private static void acutalizarRegistroProcesoStock(Connection dbconnection,  String estado){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarTablaRev3Pojob]");
			
			sb = new StringBuffer();
			//sb.append("DELETE FROM BJPROYEC.Q_40REV3 WHERE POJOB = 'PO_CANCEL'");
			sb.append("UPDATE  HORARIO_VTAV_ACT_STOCK SET ESTADO = '"+estado+"'");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Registros actualizados  HORARIO_VTAV_ACT_STOCK "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarTablaRev3Pojob]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarTablaRev3Pojob]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	
	
	/*
	private static String ejecutarQueryEom(String numero, String fecha, String fecha2, Connection dbconnection, PreparedStatement pstmtCuadratura) {

		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {

			sb = new StringBuffer();
			//sb.append("SELECT VTA_JDA.PONUMB, VTA_JDA.POVNUM, VTA_JDA.NUMDO, VTA_JDA.POFECE, VTA_JDA.POHORE, VTA_JDA.EX14ERROR, VTA_JDA.EX14FREGI, VTA_JDA.DESPA FROM CUADRATURA_OC_VTAV_JDA VTA_JDA WHERE VTA_JDA.POFECE >= '20170419' AND VTA_JDA.POFECE <= '20170419' AND VTA_JDA.NUMDO = '"+numero+"'");
			sb.append("SELECT A.PONUMB, A.POVNUM, A.NUMDO, A.POFECE, A.POHORE, A.EX14ERROR, A.EX14FREGi, A.DESPA FROM bjproyec.Q40REVb A WHERE A.NUMDO = Trim('"+numero+"')");

			
			//sb.append("SELECT Invaud.Itrloc, Invaud.INUMBR, Invaud.ITRTYP, Invaud.ITRDAT, Invaud.ITPDTE, Invaud.IDEPT, Invaud.ITRREF, Invaud.ITRAF1 FROM mmsp4lib.Invaud Invaud WHERE (Invaud.Itrtyp=31 AND Invaud.Itrdat>170101 AND Invaud.Itrloc=12)");
			pstmt = dbconnection.prepareStatement(sb.toString());
			ResultSet rs = pstmt.executeQuery();
			sb = new StringBuffer();

			boolean reg = false;
			do{
				reg = rs.next();
				if (reg){
					sb.append("Encontrado" + ";");
					
					pstmtCuadratura.setString(6, "Encontrado");
					break;
				}else{
					//sb.append("\n");
					sb.append("No Encontrado" + ";");
					
					pstmtCuadratura.setString(6, "No Encontrado");
				}
			}
			while (reg);
		}
		catch (Exception e) {

			e.printStackTrace();
			info("[crearTxt2]Exception:"+e.getMessage());
		}
		finally {

			cerrarTodo(null,pstmt,null);
		}
		return sb.toString();
	}
	*/
	
	
	
	private static String ejecutarQuery2(float PONUMB, String POVNUM, String NUMDO, 
			String POFECE, 
			String POHORE, 
			String EX14ERROR, 
			String EX14FREGi, 
			String DESPA,
			
			Connection dbconnection, PreparedStatement pstmtCuadratura) {
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		Date now = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("YYYYMMdd");
		String currentDate = ft.format(now);
		
		try {

			sb = new StringBuffer();
			sb.append("SELECT A.ID, CASE WHEN PONUMB = '' OR PONUMB IS NULL THEN  '-1' ELSE PONUMB END as PONUMB, CASE WHEN POVNUM = '' OR POVNUM IS NULL THEN  '-1' ELSE POVNUM END as POVNUM, CASE WHEN NUMDO = '' OR NUMDO IS NULL THEN  '-1' ELSE NUMDO END as NUMDO, CASE WHEN POFECE = '' OR POFECE IS NULL THEN  '-1' ELSE POFECE END as POFECE, CASE WHEN POHORE = '' OR POHORE IS NULL THEN  '-1' ELSE POHORE END as POHORE, CASE WHEN EX14ERROR = '' OR EX14ERROR IS NULL THEN  '-1' ELSE EX14ERROR END as EX14ERROR, CASE WHEN EX14FREGI = '' OR EX14FREGI IS NULL THEN  '-1' ELSE EX14FREGI END  as EX14FREGi, CASE WHEN DESPA = '' OR DESPA IS NULL THEN  '-1' ELSE DESPA END as DESPA FROM CUADRATURA_OC_VTAV_JDA A WHERE 1 = 1 AND A.POFECE >= '"+restarDia(currentDate)+"' and A.POFECE <= '"+(currentDate)+"' AND PONUMB = '"+PONUMB+"' AND POVNUM = '"+POVNUM+"' AND NUMDO = '"+NUMDO+"'");
			//sb.append("SELECT A.ID, CASE WHEN PONUMB = '' OR PONUMB IS NULL THEN  '-1' ELSE PONUMB END as PONUMB, CASE WHEN POVNUM = '' OR POVNUM IS NULL THEN  '-1' ELSE POVNUM END as POVNUM, CASE WHEN NUMDO = '' OR NUMDO IS NULL THEN  '-1' ELSE NUMDO END as NUMDO, CASE WHEN POFECE = '' OR POFECE IS NULL THEN  '-1' ELSE POFECE END as POFECE, CASE WHEN POHORE = '' OR POHORE IS NULL THEN  '-1' ELSE POHORE END as POHORE, CASE WHEN EX14ERROR = '' OR EX14ERROR IS NULL THEN  '-1' ELSE EX14ERROR END as EX14ERROR, CASE WHEN EX14FREGI = '' OR EX14FREGI IS NULL THEN  '-1' ELSE EX14FREGI END  as EX14FREGi, CASE WHEN DESPA = '' OR DESPA IS NULL THEN  '-1' ELSE DESPA END as DESPA FROM CUADRATURA_OC_VTAV_JDA_T A WHERE 1 = 1 AND A.POFECE >= '"+restarDia(currentDate)+"' and A.POFECE <= '"+(currentDate)+"' AND PONUMB = '"+PONUMB+"' AND POVNUM = '"+POVNUM+"' AND NUMDO = '"+NUMDO+"'");

			//sb.append("SELECT A.ID, CASE WHEN PONUMB = '' OR PONUMB IS NULL THEN  '-1' ELSE PONUMB END as PONUMB, CASE WHEN POVNUM = '' OR POVNUM IS NULL THEN  '-1' ELSE POVNUM END as POVNUM, CASE WHEN NUMDO = '' OR NUMDO IS NULL THEN  '-1' ELSE NUMDO END as NUMDO, CASE WHEN POFECE = '' OR POFECE IS NULL THEN  '-1' ELSE POFECE END as POFECE, CASE WHEN POHORE = '' OR POHORE IS NULL THEN  '-1' ELSE POHORE END as POHORE, CASE WHEN EX14ERROR = '' OR EX14ERROR IS NULL THEN  '-1' ELSE EX14ERROR END as EX14ERROR, CASE WHEN EX14FREGI = '' OR EX14FREGI IS NULL THEN  '-1' ELSE EX14FREGI END  as EX14FREGi, CASE WHEN DESPA = '' OR DESPA IS NULL THEN  '-1' ELSE DESPA END as DESPA FROM CUADRATURA_OC_VTAV_JDA A WHERE 1 = 1 AND A.POFECE >= '20170423' and A.POFECE <= '20170505' AND PONUMB = '"+PONUMB+"' AND POVNUM = '"+POVNUM+"' AND NUMDO = '"+NUMDO+"'");
			System.out.println("sbQuery2 ===" + sb);
			pstmt = dbconnection.prepareStatement(sb.toString());
			ResultSet rs = pstmt.executeQuery();
			sb = new StringBuffer();
			
			boolean reg = false;
			System.out.println("reg1="+reg);
			do{
				reg = rs.next();
				System.out.println("reg2="+reg);
				if (reg){
					System.out.println("Encontrado");
					
					//No se especifica rut de cliente
					if (EX14ERROR.substring(19,47).equals(rs.getString("EX14ERROR").substring(19,47))) {
						sb.append("ENCONTRADO 1 " + EX14ERROR.substring(19,47) + rs.getString("EX14ERROR").substring(19,47) + ";");
						
					} /*else if("No se especifica rut de cliente".equals(EX14ERROR.substring(19,50))) {
						status = "No se especifica rut de cliente";
					}*/ else {
						sb.append("ENCONTRADO 2 " + EX14ERROR.substring(19,47) + rs.getString("EX14ERROR").substring(19,47) + "-" + rs.getString("ID") +  ";");
						acutalizarRegistroJda(dbconnection,  EX14ERROR, rs.getString("ID"));
						commit(dbconnection,"COMMIT");	
					}
					//sb.append("ENCONTRADO" + ";");
					sb.append("NULO" + ";");
					sb.append("NULO" + ";");
					sb.append("NULO" + ";");
					sb.append("NULO" + ";");
					sb.append("NULO" + ";");
					sb.append("NULO" + ";");
					sb.append("NULO" + ";");
					sb.append("NULO" + "\n");
					
					pstmtCuadratura.setString(1, "NULO");
					pstmtCuadratura.setString(2, "NULO");
					pstmtCuadratura.setString(3, "NULO");
					pstmtCuadratura.setString(4, "NULO");
					pstmtCuadratura.setString(5, "NULO");
					pstmtCuadratura.setString(6, "NULO");
					pstmtCuadratura.setString(7, "NULO");
					pstmtCuadratura.setString(8, "NULO");
					break;
				} else {
					System.out.println("No encontrado");
					sb.append("NO ENCONTRADO" + ";");
					sb.append(PONUMB + ";");
					sb.append(POVNUM + ";");
					sb.append(NUMDO + ";");
					sb.append(POFECE + ";");
					sb.append(POHORE + ";");
					sb.append(EX14ERROR + ";");
					sb.append(EX14FREGi + ";");
					sb.append(DESPA + "\n");
					
					
					pstmtCuadratura.setString(1, ""+String.valueOf(PONUMB).replace(".0",""));
					pstmtCuadratura.setString(2, POVNUM);
					pstmtCuadratura.setString(3, NUMDO);
					pstmtCuadratura.setString(4, POFECE);
					pstmtCuadratura.setString(5, POHORE);
					pstmtCuadratura.setString(6, EX14ERROR);
					pstmtCuadratura.setString(7, EX14FREGi);
					pstmtCuadratura.setString(8, DESPA);
				}
			}
			while (reg);
		}
		catch (Exception e) {

			e.printStackTrace();
			info("[crearTxt2]Exception:"+e.getMessage());
		}
		finally {

			cerrarTodo(null,pstmt,null);
		}
		return sb.toString();
	}
	
	
	/**
	 * Metodo que elimina la tabla temporal BJPROYEC.Q_VALPS1 en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void eliminarTablaRev3(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarTablaRev3]");
			
			sb = new StringBuffer();
			//sb.append("DROP TABLE BJPROYEC.Q_40REV3");
			sb.append("DROP TABLE BJPROYEC.Q_VALPS1");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Tabla eliminada de BJPROYEC.Q_VALPS1: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarTablaRev3]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarTabla Q_VALPS1]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que crear una tabla temporal BJPROYEC.Q_VALPS1 para venta en verde en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void crearTablaRev3(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		Date now = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("YYYYMMdd");
		String currentDate = ft.format(now);
		
		try {
			info("[Ini Metodo crearTablaRev3]");
			
			sb = new StringBuffer();
			//sb.append("CREATE TABLE BJPROYEC.Q_40REV3 AS (SELECT ponumb, POCORR, POFLEN, POEREN, POFECE, POHORE, pojob, POVNUM, POEDAT, POFECG, POHORG FROM exisbugd.exiff40a A WHERE a.pocorr = (select max(b.pocorr) from exisbugd.exiff40a B where a.ponumb = b.ponumb) and POFECG >= "+restarDia(currentDate)+" and pofecg <= "+(currentDate)+") WITH DATA");
			sb.append("CREATE TABLE BJPROYEC.Q_VALPS1 AS (SELECT ponumb, POCORR, POFLEN, POEREN, POFECE, POHORE, pojob, POVNUM, POEDAT, POFECG, POHORG FROM exisbugd.exiff40a A WHERE a.pocorr = (select max(b.pocorr) from exisbugd.exiff40a B where a.ponumb = b.ponumb) and POFECG >= "+restarDia(currentDate)+" and pofecg <= "+(currentDate)+") WITH DATA");
			//sb.append("CREATE TABLE BJPROYEC.Q_VALPS1 AS (SELECT ponumb, POCORR, POFLEN, POEREN, POFECE, POHORE, pojob, POVNUM, POEDAT, POFECG, POHORG FROM exisbugd.exiff40a A WHERE a.pocorr = (select max(b.pocorr) from exisbugd.exiff40a B where a.ponumb = b.ponumb) and POFECG >= 20170423 and pofecg <= 20170505 ) WITH DATA");

			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Tabla Creada BJPROYEC.Q_VALPS1: "+pstmt.executeUpdate());	

			info("[Fin Metodo crearTabla Q_VALPS1]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo crearTablaQ_VALPS1]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que crear una tabla temporal BJPROYEC.Q_VALPS1 donde venta en verde es igual cancelado en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void eliminarTablaRev3Pojob(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarTablaQ_VALPS1]");
			
			sb = new StringBuffer();
			//sb.append("DELETE FROM BJPROYEC.Q_40REV3 WHERE POJOB = 'PO_CANCEL'");
			sb.append("DELETE FROM BJPROYEC.Q_VALPS1 WHERE POJOB = 'PO_CANCEL'");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Registros eliminados donde campo POJOB ES =  'PO_CANCEL' de BJPROYEC.Q_40REV3: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarTablaQ_VALPS1]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarTablaQ_VALPS1]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que elimina una tabla temporal bjproyec.Q_VALPS1  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void eliminarRev3NotExistInPomhdr(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarRev3NotExistInPomhdr]");
			
			sb = new StringBuffer();
			//sb.append("delete from bjproyec.Q_40REV3  a where not exists (select 1 from mmsp4lib.pomhdr b where a.ponumb = b.ponumb)");
			sb.append("delete from bjproyec.Q_VALPS1  a where not exists (select 1 from mmsp4lib.pomhdr b where a.ponumb = b.ponumb)");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Registros eliminados donde no existen en la tabla mmsp4lib.pomhdr  de BJPROYEC.Q_40REV3: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarRev3NotExistInPomhdr]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarRev3NotExistInPomhdr]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que elimina una tabla bjproyec.Q_VALPS1  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void eliminarRev3NotExistInPomhdrPostat(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarRev3NotExistInPomhdrPostat]");
			
			sb = new StringBuffer();
			//sb.append("delete from bjproyec.Q_40REV3  a where  exists (select 1 from mmsp4lib.pomhdr b where a.ponumb = b.ponumb and b.postat <> '3' )");
			sb.append("delete from bjproyec.Q_VALPS1  a where  exists (select 1 from mmsp4lib.pomhdr b where a.ponumb = b.ponumb and b.postat <> '3' )");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Registros eliminados donde no existen en la tabla mmsp4lib.pomhdr y b.postat <> '3' de BJPROYEC.Q_40REV3: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarRev3NotExistInPomhdrPostat]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarRev3NotExistInPomhdrPostat]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que elimina una tabla bjproyec.Q_VALPS1  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void eliminarRev3NotExistInExiff(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarRev3NotExistInExiff]");
			
			sb = new StringBuffer();
			//sb.append("delete from bjproyec.Q_40REV3  a where  exists (select 1 from exisbugd.exiff40cl2 b where a.ponumb = b.ponumb)");
			sb.append("delete from bjproyec.Q_VALPS1  a where  exists (select 1 from exisbugd.exiff40cl2 b where a.ponumb = b.ponumb)");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Registros eliminados donde no existen en la tabla exisbugd.exiff40cl2 de BJPROYEC.Q_40REV3: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarRev3NotExistInExiff]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarRev3NotExistInExiff]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que elimina una  BJPROYEC.Q_40REV3  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void eliminarTablaRevb(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo eliminarTablaRevb]");
			
			sb = new StringBuffer();
			//sb.append("drop table bjproyec.Q40REVb");
			sb.append("drop table bjproyec.Q_VALPS2");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Tabla eliminada de BJPROYEC.Q_40REV3: "+pstmt.executeUpdate());	

			info("[Fin Metodo eliminarTablaRevb]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo eliminarTablaRevb]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que crea una tabla temporal  bjproyec.Q_VALPS2  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void crearTablaRevb(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo crearTablaRevb]");
			
			sb = new StringBuffer();
			//sb.append("Create table bjproyec.Q40REVb AS (SELECT A.PONUMB, A.POCORR, A.POFLEN, A.POEREN, A.POFECE, A.POHORE, A.POJOB, A.POVNUM, A.POEDAT, A.POFECG, A.POHORG, B.EX14ESTA, B.EX14ERROR, B.EX14FREGI, B.EX14HOREG FROM bjproyec.Q_40REV3 a left outer join exisbugd.EXMHF14IB B ON CHAR(a.ponumb) = b.EX14IDEN) with data");
			sb.append("Create table bjproyec.Q_VALPS2 AS (SELECT A.PONUMB, A.POCORR, A.POFLEN, A.POEREN, A.POFECE, A.POHORE, A.POJOB, A.POVNUM, A.POEDAT, A.POFECG, A.POHORG, B.EX14ESTA, B.EX14ERROR, B.EX14FREGI, B.EX14HOREG FROM bjproyec.Q_VALPS1 a left outer join exisbugd.EXMHF14IB B ON CHAR(a.ponumb) = b.EX14IDEN) with data");
			
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Crear Tabla de bjproyec.Q40REVb: "+pstmt.executeUpdate());	

			info("[Fin Metodo crearTablaRevb]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo crearTablaRevb]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que agrega una columna de despacho  a la tabla temporal  BJPROYEC.Q_VALPS2  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void alterarTablaRevbDespacho(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo alterarTablaRevbDespacho]");
			
			sb = new StringBuffer();
			//sb.append("ALTER TABLE BJPROYEC.Q40REVb  ADD COLUMN DESPA CHARACTER (3 ) NOT  NULL WITH DEFAULT");
			sb.append("ALTER TABLE BJPROYEC.Q_VALPS2  ADD COLUMN DESPA CHARACTER (3 ) NOT  NULL WITH DEFAULT");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Alterar Tabla y agregar columna DESPA con caracter de 3 de BJPROYEC.Q40REVb: "+pstmt.executeUpdate());	

			info("[Fin Metodo alterarTablaRevbDespacho]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo alterarTablaRevbDespacho]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	/**
	 * Metodo que agrega una columna de numero de despacho  a la tabla temporal BJPROYEC.Q_VALPS2  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void alterarTablaRevbNumdo(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo alterarTablaRevbNumdo]");
			
			sb = new StringBuffer();
			//sb.append("ALTER TABLE BJPROYEC.Q40REVb  ADD COLUMN NUMDO CHARACTER (20 ) NOT  NULL WITH DEFAULT ");
			sb.append("ALTER TABLE BJPROYEC.Q_VALPS2  ADD COLUMN NUMDO CHARACTER (20 ) NOT  NULL WITH DEFAULT ");
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Alterar Tabla agregar columna NUMDO con caracter de 20 de BJPROYEC.Q40REVb: "+pstmt.executeUpdate());	

			info("[Fin Metodo alterarTablaRevbNumdo]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo alterarTablaRevbNumdo]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	
	/**
	 * Metodo que actualiza el campo despacho de la tabla temporal bjproyec.Q_VALPS2  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void actualizarTablaRevbDespacho(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo actualizarTablaRevbDespacho]");
			
			sb = new StringBuffer();
			//sb.append("update bjproyec.Q40REVb  A set a.DESPA =(select max(b.RELDSP)  from EXISBUGD.EXMHF56A  B where b.relord = a.ponumb)  where a.ponumb in (select   c.relord from exisbugd.exmhf56a c ) ");
			sb.append("update bjproyec.Q_VALPS2  A set a.DESPA =(select max(b.RELDSP)  from EXISBUGD.EXMHF56A  B where b.relord = a.ponumb)  where a.ponumb in (select   c.relord from exisbugd.exmhf56a c ) ");
			
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Actualizar  Tabla y establecer campo DESPA de bjproyec.Q40REVb: "+pstmt.executeUpdate());	

			info("[Fin Metodo actualizarTablaRevbDespacho]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo actualizarTablaRevbDespacho]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	
	
	/**
	 * Metodo que actualiza el campo numero de despacho de la tabla temporal bjproyec.Q_VALPS2  en JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static void actualizarTablaRevbNumdo(Connection dbconnection){
		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {
			info("[Ini Metodo actualizarTablaRevbNumdo]");
			
			sb = new StringBuffer();
			//sb.append("update bjproyec.Q40REVb  A set a.NUMDO =(select max(b.RELBL5) from EXISBUGD.EXMHF56A  B where b.relord = a.ponumb)  where a.ponumb in (select   c.relord from exisbugd.exmhf56a c ) ");
			sb.append("update bjproyec.Q_VALPS2  A set a.NUMDO =(select max(b.RELBL5) from EXISBUGD.EXMHF56A  B where b.relord = a.ponumb)  where a.ponumb in (select   c.relord from exisbugd.exmhf56a c ) ");
			
			pstmt        = dbconnection.prepareStatement(sb.toString());
			info("Actualizar  Tabla y establecer campo NUMDO de bjproyec.Q40REVb: "+pstmt.executeUpdate());	

			info("[Fin Metodo actualizarTablaRevbNumdo]");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			info("[Metodo actualizarTablaRevbNumdo]Exception:"+e.getMessage());
		}
		finally {
			cerrarTodo(null,pstmt,null);
		}
	}
	/*
	private static String ejecutarQueryJda(String Citaan, String total, Connection dbconnection, PreparedStatement pstmtCuadratura) {

		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		
		try {

			sb = new StringBuffer();
			//sb.append("Select CASE WHEN E.Citaan IS NOT NULL THEN E.Citaan ELSE '0' END  AS ASN, SUM(P.Pounts) AS Cantidad  FROM  exisbugd.Exiff17G E, mmsp4lib.Pomrch P    WHERE (P.Ponasn=E.Citaac) AND (P.Poloc=12  AND P.Pordat>160619 AND E.Citaan='"+Citaan+"') GROUP BY E.Citaan");
			sb.append("Select E.Citaan  AS ASN, SUM(P.Pounts) AS Cantidad  FROM  exisbugd.Exiff17G E, mmsp4lib.Pomrch P    WHERE (P.Ponasn=E.Citaac) AND (P.Poloc=12  AND P.Pordat>160619 AND E.Citaan='"+Citaan.replace("T", "").replace("C", "").replace("D", "")+"') GROUP BY E.Citaan");
			
			//sb.append("SELECT Invaud.Itrloc, Invaud.INUMBR, Invaud.ITRTYP, Invaud.ITRDAT, Invaud.ITPDTE, Invaud.IDEPT, Invaud.ITRREF, Invaud.ITRAF1 FROM mmsp4lib.Invaud Invaud WHERE (Invaud.Itrtyp=31 AND Invaud.Itrdat>170101 AND Invaud.Itrloc=12)");
			pstmt = dbconnection.prepareStatement(sb.toString());
			ResultSet rs = pstmt.executeQuery();
			sb = new StringBuffer();

			boolean reg = false;
			do{
				reg = rs.next();
				if (reg){
					//sb.append(rs.getString("ASN") + ";");
					//sb.append(rs.getString("Cantidad") + ";");
					//sb.append(String.valueOf((Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total))) + ";");
					//sb.append(obtenerEstado(rs.getString("ASN") , (Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total))) + "\n");
					sb.append( String.valueOf((Integer.parseInt(rs.getString("Cantidad")) > 0 ? rs.getString("ASN") : "#N/A") ) + ";");
					//sb.append(rs.getString("Cantidad") + ";");
					sb.append( String.valueOf((Integer.parseInt(rs.getString("Cantidad")) > 0 ? rs.getString("Cantidad") : "#N/A") ) + ";");
					//sb.append(( (Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total)))  + ";");
					sb.append( String.valueOf((Integer.parseInt(rs.getString("Cantidad")) > 0 ? ( (Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total))) : "#N/A") ) + ";");
					sb.append(obtenerEstado(rs.getString("ASN") , rs.getString("Cantidad"), (Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total))) + "\n");
					
					
					pstmtCuadratura.setString(4, rs.getString("ASN"));
					pstmtCuadratura.setString(5, rs.getString("Cantidad"));
					pstmtCuadratura.setString(6, String.valueOf( (Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total))));
					pstmtCuadratura.setString(7, obtenerEstado(rs.getString("ASN") , rs.getString("Cantidad"), (Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total))));
					
					
					break;
				}else{
					//sb.append("\n");
					
					sb.append("#N/A" + ";");
					sb.append("#N/A" + ";");
					sb.append("#N/A" + ";");
					sb.append("#N/A" + "\n");
					
					
					pstmtCuadratura.setInt(4, 999999999);
					pstmtCuadratura.setInt(5, 999999999);
					pstmtCuadratura.setInt(6, 999999999);
					pstmtCuadratura.setString(7, "#N/A");
					
				}
			}
			while (reg);
		}
		catch (Exception e) {

			e.printStackTrace();
			info("[crearTxt2]Exception:"+e.getMessage());
		}
		finally {

			cerrarTodo(null,pstmt,null);
		}
		return sb.toString();
	}
	
	*/
	
	
	/**
	 * Metodo que elimina un archivo con la ruta especifica
	 * 
	 * @param String,  Ruta del archivo
	 * @return 
	 * 
	 */
	public static void eliminarArchivo(String archivo){

	     File fichero = new File(archivo);
	   
	     if(fichero.delete()){

	          System.out.println("archivo eliminado");
	    
	     } else {
	    	 System.out.println("archivo no eliminado");
	     }

	} 
	
	/*
	private static String ejecutarQueryDiario(String Citaan, String total, Connection dbconnection, PreparedStatement pstmtCuadratura) {

		StringBuffer sb         = new StringBuffer();
		PreparedStatement pstmt = null;
		System.out.println("Citaan="+Citaan);
		System.out.println("Citaan2="+Citaan.replace("T", ""));

		try {

			sb = new StringBuffer();
			//sb.append("Select CASE WHEN E.Citaan IS NOT NULL THEN E.Citaan ELSE '0' END  AS ASN, SUM(P.Pounts) AS Cantidad  FROM  exisbugd.Exiff17G E, mmsp4lib.Pomrch P    WHERE (P.Ponasn=E.Citaac) AND (P.Poloc=12  AND P.Pordat>160619 AND E.Citaan='"+Citaan+"') GROUP BY E.Citaan");
			sb.append("Select E.Citaan  AS ASN, SUM(P.Pounts) AS Cantidad  FROM  exisbugd.Exiff17G E, mmsp4lib.Pomrch P    WHERE (P.Ponasn=E.Citaac) AND (P.Poloc=12  AND P.Pordat>160619 AND E.Citaan='"+Citaan.replace("T", "").replace("C", "").replace("D", "")+"') GROUP BY E.Citaan");
			
			//sb.append("SELECT Invaud.Itrloc, Invaud.INUMBR, Invaud.ITRTYP, Invaud.ITRDAT, Invaud.ITPDTE, Invaud.IDEPT, Invaud.ITRREF, Invaud.ITRAF1 FROM mmsp4lib.Invaud Invaud WHERE (Invaud.Itrtyp=31 AND Invaud.Itrdat>170101 AND Invaud.Itrloc=12)");
			pstmt = dbconnection.prepareStatement(sb.toString());
			ResultSet rs = pstmt.executeQuery();
			sb = new StringBuffer();

			boolean reg = false;
			do{
				reg = rs.next();
				if (reg){
					sb.append(rs.getString("ASN") + ";");
					sb.append(rs.getString("Cantidad") + ";");
					sb.append(String.valueOf((Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total))) + ";");
					sb.append(obtenerEstado(rs.getString("ASN") , (Integer.parseInt(rs.getString("Cantidad")) - Integer.parseInt(total))) + "\n");
					break;
				}else{
					//sb.append("\n");
					
					sb.append("#N/A" + ";");
					sb.append("#N/A" + ";");
					sb.append("#N/A" + ";");
					sb.append("#N/A" + "\n");
				}
			}
			while (reg);
		}
		catch (Exception e) {

			e.printStackTrace();
			info("[crearTxt2]Exception:"+e.getMessage());
		}
		finally {

			cerrarTodo(null,pstmt,null);
		}
		return sb.toString();
	}
	*/

	
	/**
	 * Metodo que crea la conexion a la base de datos ROBLE JDA
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static Connection crearConexion() {

		System.out.println("Creado conexion a ROBLE.");
		AS400JDBCDriver d = new AS400JDBCDriver();
		String mySchema = "RDBPARIS2";
		Properties p = new Properties();
		//AS400 o = new AS400("roble.cencosud.corp","usrcom", "usrcom");
		AS400 o = new AS400("roble.cencosud.corp","usrcom", "usrcom");
		Connection dbconnection = null;

		try {

			System.out.println("AuthenticationScheme: "+o.getVersion());
			dbconnection = d.connect (o, p, mySchema);
			System.out.println("Conexion a ROBLE CREADA.");
		}
		catch (Exception e) {

			System.out.println(e.getMessage());
		}
		return dbconnection;
	}

	/**
	 * Metodo que crea la conexion a la base de MEOMCLP (EOM)
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static Connection crearConexionOracle() {

		Connection dbconnection = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//Shareplex
			dbconnection = DriverManager.getConnection("jdbc:oracle:thin:@g500603svcr9.cencosud.corp:1521:MEOMCLP","REPORTER","RptCyber2015");
			
			//El servidor g500603sv0zt corresponde a ProducciÃ³n. por el momento
			//dbconnection = DriverManager.getConnection("jdbc:oracle:thin:@g500603sv0zt.cencosud.corp:1521:MEOMCLP","ca14","Manhattan1234");
			

		}
		catch (Exception e) {

			e.printStackTrace();
		}
		return dbconnection;
	}
	
	
	/**
	 * Metodo que crea la conexion a la base de MEOMCLP (EOM)
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @return 
	 * 
	 */
	private static Connection crearConexionOracle2() {

		Connection dbconnection = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			//Comentado por cambio de base de datos. El servidor g500603svcr9 corresponde shareplex.
			//dbconnection = DriverManager.getConnection("jdbc:oracle:thin:@g500603svcr9:1521:MEOMCLP","REPORTER","RptCyber2015");
			
			//El servidor g500603sv0zt corresponde a Producción.
			dbconnection = DriverManager.getConnection("jdbc:oracle:thin:@172.18.163.15:1521/XE", "kpiweb",
					"kpiweb");
		}
		catch (Exception e) {

			e.printStackTrace();
		}
		return dbconnection;
	}
	/*
	private static Connection crearConexionDB2() {
		
		System.out.println("Creando conexion a HUB.");
		Connection dbconnection = null;

		try {

			Class.forName("com.ibm.db2.jcc.DB2Driver");
			dbconnection = DriverManager.getConnection("jdbc:db2://198.61.129.39:50051/PHUBP01","con_hubp","82ndy78hdjos");
			//dbconnection = DriverManager.getConnection("jdbc:db2://172.18.149.133:50051/PHUBP01","USINTXD","USINTXD1");
			System.out.println("Conexion a HUB CREADA.");
		}
		catch (Exception e) {

			e.printStackTrace();
		}
		return dbconnection;
	}
	*/
	
	/*
	private static String limpiarCeros(String str) {

		int iCont = 0;

		while (str.charAt(iCont) == '0') {

			iCont++;
		}
		return str.substring(iCont, str.length());
	}
	*/

	/**
	 * Metodo que cierra la conexion, Procedimintos,  BufferedWriter
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @param PreparedStatement, Objeto que representa una instrucción SQL precompilada. 
	 * @return retorna
	 * 
	 */
	private static void cerrarTodo(Connection cnn, PreparedStatement pstmt, BufferedWriter bw){

		try {

			if (cnn != null) {

				cnn.close();
				cnn = null;
			}
		}
		catch (Exception e) {

			System.out.println(e.getMessage());
			info("[cerrarTodo]Exception:"+e.getMessage());
		}
		try {

			if (pstmt != null) {

				pstmt.close();
				pstmt = null;
			}
		}
		catch (Exception e) {

			System.out.println(e.getMessage());
			info("[cerrarTodo]Exception:"+e.getMessage());
		}
		try {

			if (bw != null) {

				bw.flush();
				bw.close();
				bw = null;
			}
		}
		catch (Exception e) {

			System.out.println(e.getMessage());
			info("[cerrarTodo]Exception:"+e.getMessage());
		}
	}
	
	

	/**
	 * Metodo que muestra informacion 
	 * 
	 * @param String, texto a mostra
	 * @param String, cantidad para restar dias
	 * @return String retorna los dias a restar
	 * 
	 */
	private static void info(String texto){

		try {

			bw.write(texto+"\n");
			bw.flush();
		}
		catch (Exception e) {

			System.out.println("Exception:"+e.getMessage());
		}
	}

	/**
	 * Metodo que resta dias 
	 * 
	 * @param String, dia que se resta
	 * @param String, cantidad para restar dias
	 * @return String retorna los dias a restar
	 * 
	 */
	private static int restarDia(String sDia) {

		int dia = 0;
		String sFormato = "yyyyMMdd";
		Calendar diaAux = null;
		String sDiaAux = null;
		SimpleDateFormat df = null;

		try {

			diaAux = Calendar.getInstance();
			df = new SimpleDateFormat(sFormato);
			diaAux.setTime(df.parse(sDia));
			diaAux.add(Calendar.DAY_OF_MONTH, -1);
			sDiaAux = df.format(diaAux.getTime());
			dia = Integer.parseInt(sDiaAux);
		}
		catch (Exception e) {

			info("[restarDia]Exception:"+e.getMessage());
		}
		return dia;
	}
	
	
	/**
	 * Metodo que hace commit en la base de datos
	 * 
	 * @param Connection, conexion a la base de datos
	 * @return si valor de retorno
	*/
	
	private static void commit(Connection dbconnection,  String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = dbconnection.prepareStatement(sql);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			cerrarTodo(null, pstmt, null);
		}
	}
	
	/**
	 * Metodo que ejecuta la eliminacion de registros en una tabla 
	 * 
	 * @param Connection, conexion de las base de datos
	 * @param String, query para la eliminacion  
	 * @return 
	 */
	private static void elimnarInventario(Connection dbconnection,  String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = dbconnection.prepareStatement(sql);
			System.out.println("registros elimnados Cumplimient : " + pstmt.executeUpdate());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			cerrarTodo(null, pstmt, null);
		}
		
	}
	
	
	/**
	 * Metodo que da un formato de fecha
	 * 
	 * @param String, fecha para formatear  
	 * @return 
	 */
	public static String getFormatDay(String fecha){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		//String dateInString = "01/11/2014";
	 
		try {
	 
			Date date = formatter.parse(fecha);
			//System.out.println(date);
			//System.out.println(formatter.format(date));
			return formatter.format(date);
	 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
