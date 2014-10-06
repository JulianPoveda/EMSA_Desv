/***********************************************************************************************************************************
 * Version 1.0
 * Fecha: Mayo 14-2014
 * News:	ExecuteArrayListQuery(String _campo, ArrayList<ContentValues> _informacion)
 * 			Funcion encargada de recibir un array List con los querys a ejecutar, se ejecuta un replace de DELETE a DELETE FROM
 * 			de esta forma se garantiza que la sentencia SQL DELETE se ejecute efectivamente en la base de datos SQLite, por ultimo
 * 			retorna la cantidad de ejecuciones que realizo.
***********************************************************************************************************************************/



package sistema;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLite {
	private static Archivos ArchSQL;	
	private static String N_BD = null; 	
	private static final int VERSION_BD = 1;																		
	
	private BDHelper nHelper;
	private Context nContexto;
	private String Directorio;
	private SQLiteDatabase nBD;
	
	private ContentValues TablasAmData = new ContentValues();
	
	
	private boolean ValorRetorno;
	
	private static class BDHelper extends SQLiteOpenHelper{
		
		public BDHelper(Context context) {
			super(context, N_BD, null, VERSION_BD);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			/**************************************************************************************************************************/
			/************************Creacion de las tablas basicas para el correcto funcionamiento del sistema************************/
			/**************************************************************************************************************************/
			
			//Tabla con los usuarios del sistema
			db.execSQL(	"CREATE TABLE amd_usuarios(	login 		VARCHAR(50) NOT NULL PRIMARY KEY,"
												+ "	password	VARCHAR(50) NOT NULL,"
												+ "	perfil 		INTEGER NOT NULL,"
												+ "	documento	VARCHAR(100),"
												+ "	nombre		VARCHAR(100))");
			
			db.execSQL("INSERT INTO amd_usuarios('login','password','perfil','documento','nombre') VALUES ('adm','d3sv14c10n3s',0,'0','Administrador Sistema')");
			
			//Tabla con los parametros del sistema
			db.execSQL("CREATE TABLE amd_configuracion (item TEXT PRIMARY KEY, valor TEXT NOT NULL, nivel INTEGER NOT NULL)");				
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('servidor','http://190.93.133.87',0) ");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('puerto','8080',0) ");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('modulo','ControlEnergia/WS',0) ");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('web_service','AndroidWS.php?wsdl',0)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('equipo','302',0)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('cedula_tecnico','sin_asignar',0)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('nombre_tecnico','sin_asignar',0)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('impresora','sin asignar',1)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('version','1.0',0)");
			
			/**Tabla con la lista de solicitudes que se han asignado**/
			db.execSQL( "CREATE TABLE in_ordenes_trabajo( id_serial			INTEGER NOT NULL PRIMARY KEY," +
														" solicitud			VARCHAR(20) UNIQUE NOT NULL," +
														" dependencia 		VARCHAR(20) NOT NULL," +
														" pda				INTEGER NOT NULL," +
														" cuenta			VARCHAR(20) NOT NULL," +
														" municipio 		INTEGER NOT NULL," +
														" suscriptor		VARCHAR(100) NOT NULL," +
														" direccion			VARCHAR(100) NOT NULL," +
														" clase_servicio	VARCHAR(20) NOT NULL," +
														" estrato 			INTEGER," +
														" nodo				VARCHAR(10) NOT NULL," +
														" marca				VARCHAR(20) NOT NULL," +
														" serie				VARCHAR(50) NOT NULL," +
														" carga_contratada	INTEGER);");
			
			db.execSQL(	"CREATE TABLE in_sellos( id_serial 	INTEGER NOT NULL," +
												"solicitud  VARCHAR(20) NOT NULL," +
												"marca		VARCHAR(10) NOT NULL," +
												"serie		VARCHAR(30) NOT NULL," +
												"numero 	VARCHAR(30) NOT NULL," +
												"tipo		VARCHAR(50) NOT NULL," +
												"clase 		VARCHAR(50) NOT NULL," +
												"PRIMARY KEY(solicitud,serie,numero));");
			
			db.execSQL(	"CREATE TABLE in_consumos(   id_serial 			INTEGER NOT NULL," +
													"solicitud  		VARCHAR(20) NOT NULL," +
													"cuenta				VARCHAR(10) NOT NULL," +
													"marca				VARCHAR(30) NOT NULL," +
													"serie  			VARCHAR(30) NOT NULL," +
													"fecha_tomada		VARCHAR(50) NOT NULL," +
													"fecha_anterior 	VARCHAR(50) NOT NULL," +
													"lectura_tomada		DOUBLE PRECISION NOT NULL," +
													"lectura_anterior	DOUBLE PRECISION NOT NULL," +
													"consumo			DOUBLE PRECISION NOT NULL," +
													"PRIMARY KEY(solicitud, fecha_tomada));");
			
			
			/*db.execSQL(	"CREATE TABLE amd_tipo_enterado(id_serial INTEGER PRIMARY KEY AUTOINCREMENT,descripcion VARCHAR(255) UNIQUE NOT NULL)");
			db.execSQL("INSERT INTO amd_tipo_enterado (descripcion) VALUES ('...')");
			db.execSQL("INSERT INTO amd_tipo_enterado (descripcion) VALUES ('Propietario')");
			db.execSQL("INSERT INTO amd_tipo_enterado (descripcion) VALUES ('Suscriptor')");
			db.execSQL("INSERT INTO amd_tipo_enterado (descripcion) VALUES ('Usuario')");
			db.execSQL("INSERT INTO amd_tipo_enterado (descripcion) VALUES ('Poseedor')");
			db.execSQL("INSERT INTO amd_tipo_enterado (descripcion) VALUES ('Otros')");*/
					
			/*db.execSQL(	"CREATE TABLE amd_uso_derecho(id_serial INTEGER PRIMARY KEY AUTOINCREMENT, descripcion VARCHAR(255) UNIQUE NOT NULL)");			
			db.execSQL("INSERT INTO amd_uso_derecho (descripcion) VALUES ('...')");
			db.execSQL("INSERT INTO amd_uso_derecho (descripcion) VALUES ('Si')");
			db.execSQL("INSERT INTO amd_uso_derecho (descripcion) VALUES ('No')");*/
			
			
			/*db.execSQL(	"CREATE TABLE amd_ubicacion_medidor(id_serial INTEGER PRIMARY KEY AUTOINCREMENT, descripcion VARCHAR(255) UNIQUE NOT NULL)");			
			db.execSQL("INSERT INTO amd_ubicacion_medidor (descripcion) VALUES ('...')");
			db.execSQL("INSERT INTO amd_ubicacion_medidor (descripcion) VALUES ('Caja')");
			db.execSQL("INSERT INTO amd_ubicacion_medidor (descripcion) VALUES ('Gabinete')");
			db.execSQL("INSERT INTO amd_ubicacion_medidor (descripcion) VALUES ('Otro')");*/			
			
			
			/*db.execSQL(	"CREATE TABLE amd_impresiones_inf(	id_orden   		VARHAR(20) PRIMARY KEY,"
														+ " diagrama		VARCHAR(50) NOT NULL,"
														+ " acometida		VARCHAR(20) NOT NULL,"
														+ " ubicacion 		VARCHAR(20) NOT NULL,"
														+ " uso_derecho 	VARCHAR(20) NOT NULL,"
														+ " items			VARCHAR(20) NOT NULL,"
														+ "	id_impresion	INTEGER NOT NULL DEFAULT 0);");*/
						
			/********************************************************************************************************/
			
			/*db.execSQL(	"CREATE TABLE amd_actas(	id_orden 		VARCHAR(20) PRIMARY KEY,"
												+ " id_acta 		VARCHAR(20) UNIQUE NOT NULL,"
												+ " id_revision 	VARCHAR(50),"
												+ "	codigo_trabajo 	VARCHAR(20) NOT NULL,"
												+ " nombre_enterado	VARCHAR(100) NOT NULL,"
												+ "	cedula_enterado	VARCHAR(20) NOT NULL,"
												+ "	evento			VARCHAR(50),"
												+ "	tipo_enterado	VARCHAR(50) NOT NULL,"
												+ "	fecha_revision	TIMESTAMP,"
												+ "	usuario_ins 	VARCHAR(50) NOT NULL,"
												+ " fecha_ins		TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
												+ " cedula_testigo	VARCHAR(20),"
												+ " nombre_testigo  VARCHAR(100),"
												+ " estado 			VARCHAR(2) NOT NULL)");*/
			
			/*db.execSQL( "CREATE TABLE amd_ordenes_trabajo(	id_orden 				VARCHAR(20) NOT NULL PRIMARY KEY,"
														+ "	num_acta				VARCHAR(20),"
														+ "	cuenta 					VARCHAR(20),"
														+ "	propietario				VARCHAR(100),"
														+ "	departamento			VARCHAR(50),"
														+ "	municipio				VARCHAR(50),"
														+ "	ubicacion				VARCHAR(50),"
														+ "	direccion				VARCHAR(100),"
														+ "	clase_servicio			VARCHAR(50),"
														+ "	estrato					VARCHAR(50),"
														+ "	id_nodo					VARCHAR(50) NOT NULL,"
														+ "	ruta					VARCHAR(50) NOT NULL,"
														+ "	estado_cuenta			VARCHAR(50) NOT NULL,"
														+ "	fecha_atencion 			TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
														+ "	hora_ini				VARCHAR(50),"
														+ "	hora_fin 				VARCHAR(50),"
														+ "	pda						INTEGER NOT NULL,"
														+ "	estado					VARCHAR(50) NOT NULL,"
														+ "	observacion_trabajo		VARCHAR(400) NOT NULL,"
														+ "	observacion_pad			VARCHAR(300) NOT NULL,"
														+ "	usuario					VARCHAR(50),"
														+ "	fecha_hora_estado		TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
														+ "	fecha_ven				TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
														+ "	tipo					VARCHAR(1),"
														+ "	registrado				INTEGER,"
														+ "	codigo_apertura			VARCHAR(20),"
														+ "	solicitud				VARCHAR(100),"
														+ "	bodega					VARCHAR(256),"
														+ "	hora_ini_atiende		TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
														+ "	hora_fin_atiende		TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
														+ "	persona_atiende			VARCHAR(400),"
														+ "	tel_persona_atiende		VARCHAR(20),"
														+ "	clase_solicitud			VARCHAR(50),"
														+ "	tipo_solicitud			VARCHAR(50),"
														+ "	dependencia				VARCHAR(50),"
														+ "	tipo_accion				VARCHAR(50),"
														+ "	dependencia_asignada	VARCHAR(50),"
														+ "	consecutivo_accion		VARCHAR(50),"
														+ "	carga_instalada			VARCHAR(100),"
														+ "	carga_contratada		VARCHAR(100),"
														+ "	ciclo 					VARCHAR(100))");*/
			
			
			
			/*db.execSQL(	"CREATE TABLE amd_servicio_nuevo(	id_orden			VARCHAR(20) NOT NULL PRIMARY KEY,"
														+ "	cuenta 				VARCHAR(20),"
														+ "	cuenta_vecina1		VARCHAR(20),"
														+ "	cuenta_vecina2		VARCHAR(20),"
														+ "	nodo_transformador	VARCHAR(20),"
														+ "	nodo_secundario		VARCHAR(20),"
														+ "	doc1				VARCHAR(20),"
														+ "	doc2				VARCHAR(20),"
														+ "	doc3				VARCHAR(20),"
														+ "	doc4				VARCHAR(20),"
														+ "	doc5				VARCHAR(20),"
														+ "	doc6				VARCHAR(20),"
														+ "	doc7				VARCHAR(20));");*/
			
			/*********************************************************************************************************************/
			/*******Tablas con la informacion respetiva de las anomalias generales y las aplicadas a la orden en particular*******/
			/*********************************************************************************************************************/
			/*db.execSQL(	"CREATE TABLE amd_irregularidades( 	id_anomalia 		VARCHAR(20) NOT NULL,"
														+ "	id_orden			VARCHAR(20) NOT NULL,"
														+ "	id_irregularidad	VARCHAR(20) NOT NULL,"
														+ "	usuario_ins			VARCHAR(20) NOT NULL,"
														+ "	fecha_ins 			TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
														+ "	PRIMARY KEY(id_anomalia, id_orden, id_irregularidad))");
			
			db.execSQL(	"CREATE TABLE amd_param_trabajos(	id_trabajo 				VARCHAR(20) NOT NULL PRIMARY KEY,"
														+ "	descripcion 			VARCHAR(4000),"
														+ "	id_trabajo_contrato		VARCHAR(20) NOT NULL,"
														+ "	ubicacion				VARCHAR(1) NOT NULL,"
														+ "	tipo					VARCHAR(1),"
														+ "	unidad					VARCHAR(5))");
			
			db.execSQL(	"CREATE TABLE amd_param_materiales_trabajo(	cpta_id_trabajo		VARCHAR(20) NOT NULL,"
																+ "	cpta_id_material	VARCHAR(20) NOT NULL,"
																+ "	cantidad_proyectada	VARCHAR(5)	NOT NULL,"
																+ "	material_requerido	VARCHAR(1)	NOT NULL,"
																+ "	PRIMARY KEY(cpta_id_trabajo, cpta_id_material))");
			
			db.execSQL(	"CREATE TABLE amd_param_marca_contador(	id_marca	VARCHAR(50) NOT NULL PRIMARY KEY,"
															+ "	nombre		VARCHAR(50) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_materiales(	id_material 	VARCHAR(20) NOT NULL PRIMARY KEY,"
														+ "	descripcion		VARCHAR(200) NOT NULL,"
														+ "	unidad			VARCHAR(50) NOT NULL,"
														+ "	valor			numeric(8,5))");
			
			db.execSQL( "CREATE TABLE amd_param_sistema(codigo			VARCHAR(50) NOT NULL PRIMARY KEY,"
													+ "	rango_inicial	VARCHAR(50),"
													+ "	rango_final		VARCHAR(50),"
													+ "	valor 			VARCHAR(50),"
													+ "	tipo			VARCHAR(50) NOT NULL,"
													+ "	estado 			VARCHAR(50) NOT NULL,"
													+ "	descripcion		VARCHAR(100))");
			
			db.execSQL( "CREATE TABLE amd_medidor_encontrado(id_orden	VARCHAR(20) NOT NULL,"
														+ "	marca		VARCHAR(50) NOT NULL,"
														+ "	serie		VARCHAR(50),"
														+ "	lectura 	VARCHAR(50),"
														+ "	lectura_2	VARCHAR(50),"
														+ "	lectura_3 	VARCHAR(50),"
														+ "	tipo		VARCHAR(100),"
														+ "	PRIMARY KEY(id_orden))");
			
			db.execSQL( "CREATE TABLE amd_cambios_contadores(	id_orden	VARCHAR(20) NOT NULL,"
															+ "	tipo		VARCHAR(50) NOT NULL,"
															+ "	marca		VARCHAR(50) NOT NULL,"
															+ "	serie	 	VARCHAR(50) NOT NULL,"
															+ "	lectura		VARCHAR(20) NOT NULL,"
															+ "	cuenta	 	VARCHAR(10) NOT NULL,"
															+ "	usuario_ins	VARCHAR(50) NOT NULL,"
															+ "	fecha_ins	TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
															+ "	cobro		VARCHAR(50) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_sellos(	id_orden		VARCHAR(20) NOT NULL,"
												+ "	estado			VARCHAR(50) NOT NULL,"
												+ "	tipo			VARCHAR(50) NOT NULL,"
												+ "	numero			VARCHAR(50) NOT NULL,"
												+ "	color			VARCHAR(50) NOT NULL,"
												+ "	irregularidad	VARCHAR(50) NOT NULL,"
												+ "	ubicacion 		VARCHAR(50) NOT NULL,"
												+ "	usuario_ins	 	VARCHAR(50) NOT NULL,"
												+ "	fecha_ins 		TIMESTAMP  NOT NULL DEFAULT current_timestamp,"
												+ "	PRIMARY KEY (id_orden,estado,tipo,numero))");
			
			
			db.execSQL(	"CREATE TABLE amd_param_documentos(	id_documento	INTEGER NOT NULL PRIMARY KEY,"
														+ "	descripcion 	VARCHAR(50) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_respuesta_pqr(	clase_solicitud					VARCHAR(50),"
														+ "	tipo_solicitud					VARCHAR(50),"
														+ "	dependencia						VARCHAR(50),"
														+ "	tipo_accion						VARCHAR(50),"
														+ "	dependencia_asignada			VARCHAR(50),"
														+ "	recomendacion					VARCHAR(50),"
														+ "	descripcion_tipo_recomendacion	VARCHAR(500))");
			
			db.execSQL(	"CREATE TABLE amd_param_incosistencias(	 	id_inconsistencia	VARCHAR(20) NOT NULL PRIMARY KEY,"
																+ "	descripcion			VARCHAR(100) NOT NULL,"
																+ "	tipo				VARCHAR(50) NOT NULL,"
																+ "	aplicacion 			VARCHAR(50) NOT NULL,"
																+ "	identificadornodo	INTEGER,"
																+ "	identificadorpadre	INTEGER)");
			
			db.execSQL(	"CREATE TABLE amd_param_imp_observaciones(	id_inconsistencia	VARCHAR(20) NOT NULL,"
																+ "	id_documento		VARCHAR(20)	NOT NULL,"
																+ "	PRIMARY KEY(id_inconsistencia,id_documento))");
			
			db.execSQL(	"CREATE TABLE amd_param_cobros(	id_material_automatico	VARCHAR(50) NOT NULL PRIMARY KEY,"
													+ "	codigo_tipo				VARCHAR(50) NOT NULL,"
													+ "	tipo_ingreso 			VARCHAR(50) NOT NULL,"
													+ "	codigo_retira			VARCHAR(50))");
			
			db.execSQL(	"CREATE TABLE amd_detalle_param_cobros(	id_material_automatico	VARCHAR(4) NOT NULL,"
															+ "	id_material				VARCHAR(20) NOT NULL,"
															+ "	PRIMARY KEY (id_material_automatico, id_material))");
			
			db.execSQL(	"CREATE TABLE amd_elementos_censo(	id_elemento		VARCHAR(20) NOT NULL PRIMARY KEY,"
														+ "	descripcion 	VARCHAR(50) NOT NULL,"
														+ "	capacidad_min	VARCHAR(10) NOT NULL,"
														+ "	capacidad_max	VARCHAR(10) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_opciones(	id_opcion		INTEGER NOT NULL PRIMARY KEY,"
														+ "	nombre 			VARCHAR(50) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_acometidas(	id_acometida 	VARCHAR(20)	NOT NULL PRIMARY KEY,"
														+ "	calibre 		VARCHAR(50) NOT NULL,"
														+ "	tipo_acometida	VARCHAR(50) NOT NULL,"
														+ "	conductor		VARCHAR(50) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_diagrama(	id_diagrama			VARCHAR(10) NOT NULL PRIMARY KEY,"
														+ "	descripcion			VARCHAR(100) NOT NULL,"
														+ "	path				VARCHAR(50) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_trabajos_opcion(id_trabajo			VARCHAR(20) NOT NULL,"
															+ "	id_opcion 			VARCHAR(20) NOT NULL,"
															+ "	id_trabajo_opcion	VARCHAR(20),"
															+ "	PRIMARY KEY(id_trabajo, id_opcion))");
			
			db.execSQL(	"CREATE TABLE amd_param_irregularidades(id_irregularidad	INTEGER NOT NULL PRIMARY KEY,"
															+ "	descripcion 		VARCHAR(500),"
															+ "	tipo				VARCHAR(50) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_tipo_conexion(	id_conexion		VARCHAR(5) NOT NULL PRIMARY KEY,"
															+ "	conexion		VARCHAR(50) NOT NULL);");
			
			db.execSQL(	"INSERT INTO amd_param_tipo_conexion(id_conexion, conexion) VALUES('MB','MONOFASICO BIFILAR');");
			db.execSQL(	"INSERT INTO amd_param_tipo_conexion(id_conexion, conexion) VALUES('MT','MONOFASICO TRIFILAR');");
			db.execSQL(	"INSERT INTO amd_param_tipo_conexion(id_conexion, conexion) VALUES('T','TRIFILAR');");
			db.execSQL(	"INSERT INTO amd_param_tipo_conexion(id_conexion, conexion) VALUES('B','BIFASICO');");
			db.execSQL(	"INSERT INTO amd_param_tipo_conexion(id_conexion, conexion) VALUES('SD','SERVICIO DIRECTO');");
			db.execSQL(	"INSERT INTO amd_param_tipo_conexion(id_conexion, conexion) VALUES('SN','SERVICIO NUEVO');");
			
			db.execSQL(	"CREATE TABLE amd_bodega_contadores(	 marca                  VARCHAR(50)," +
																"serie                  VARCHAR(20)," +
																"estado                 VARCHAR(50)," +
																"lectura                VARCHAR(20)," +
																"digitos                INTEGER," +
																"tipo                   VARCHAR(50)," +
																"instalado              INTEGER DEFAULT 0," +
																"bodega                 VARCHAR(255)," +
																"codigo_elemento        VARCHAR(40)," +
																"PRIMARY KEY(marca,serie));");	
			
			
			db.execSQL("CREATE TABLE amd_bodega_sellos(	 numero                 VARCHAR(100) NOT NULL," +
														"tipo                   VARCHAR(100) NOT NULL," +
														"color                  VARCHAR(100) NOT NULL," +
														"estado                 VARCHAR(100)," +
														"bodega                 VARCHAR(100)," +
														"PRIMARY KEY(numero,tipo,color));");
			
			
			db.execSQL(	"CREATE TABLE amd_param_titulos(codigo 			NUMERIC(9,2) NOT NULL PRIMARY KEY,"
													+ "	descripcion		VARCHAR(200) NOT NULL)");

			db.execSQL(	"CREATE TABLE amd_param_subtitulos(	codigo_sub		NUMERIC(9,2) NOT NULL PRIMARY KEY,"
														+ "	codigo_titulo 	NUMERIC(9,2) NOT NULL,"
														+ "	nombre 			VARCHAR(200) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_items(	codigo_item			NUMERIC(9,2) NOT NULL PRIMARY KEY,"
													+ "	codigo_sub 			NUMERIC(9,2) NOT NULL,"
													+ "	codigo_titulo		NUMERIC(9,2) NOT NULL,"
													+ "	tipo				VARCHAR(5) NOT NULL,"
													+ "	nombre				VARCHAR(200) NOT NULL,"
													+ "	requerido 			VARCHAR(1),"
													+ "	cod_inconsistencia	VARCHAR(100),"
													+ "	numerico			VARCHAR(1))");
			
			db.execSQL(	"CREATE TABLE amd_param_elementos(	codigo_elemento		NUMERIC(9,2) NOT NULL PRIMARY KEY,"
														+ "	codigo_item			NUMERIC(9,2) NOT NULL,"
														+ "	codigo_sub			NUMERIC(9,2) NOT NULL,"
														+ "	codigo_titulo 		NUMERIC(9,2) NOT NULL,"
														+ "	nombre 				VARCHAR(200) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_titulos_trabajo(id_trabajo		VARCHAR(20) NOT NULL,"
															+ "	codigo			VARCHAR(20) NOT NULL,"
															+ "	requerido		VARCHAR(40) NOT NULL,"
															+ "	PRIMARY KEY(id_trabajo, codigo))");
			
			db.execSQL(	"CREATE TABLE amd_param_tipo_sello(	codigo			VARCHAR(100) NOT NULL PRIMARY KEY,"
														+ "	descripcion 	VARCHAR(100) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_ubicacion_sello(codigo		VARCHAR(100) NOT NULL PRIMARY KEY,"
															+ "	descripcion VARCHAR(100) NOT NULL)");
			
			db.execSQL(	"CREATE TABLE amd_param_color_sello(codigo 		VARCHAR(100) NOT NULL PRIMARY KEY,"
														+ "	descripcion VARCHAR(100) NOT NULL)");
			
			
			
			/////////////////////////////////////////continuacion de las tablas que se tomaron fotos
			
			db.execSQL(	"CREATE TABLE amd_param_acometidas_tipo(tipo 			VARCHAR(20) NOT NULL PRIMARY KEY,"
															+ "	sigla_tipo		VARCHAR(1) UNIQUE NOT NULL)");
			
			db.execSQL(	"INSERT INTO amd_param_acometidas_tipo(tipo,sigla_tipo) VALUES ('Alambre','A')");
			db.execSQL(	"INSERT INTO amd_param_acometidas_tipo(tipo,sigla_tipo) VALUES ('Cable','C')");
			db.execSQL(	"INSERT INTO amd_param_acometidas_tipo(tipo,sigla_tipo) VALUES ('Concentrico','N')");
			
					
			db.execSQL(	"CREATE TABLE amd_param_acometidas_conductor(	conductor			VARCHAR(20) NOT NULL PRIMARY KEY,"
																	+ "	sigla_conductor 	VARCHAR(1) UNIQUE NOT NULL)");
			
			db.execSQL("CREATE TABLE amd_param_estado_contador (	id_cobro		VARCHAR(10) NOT NULL PRIMARY KEY,"
																+ "	descripcion	VARCHAR(50) NOT NULL);");

			db.execSQL("INSERT INTO amd_param_estado_contador(id_cobro,descripcion) VALUES('R','ENCONTRADO');");
			db.execSQL("INSERT INTO amd_param_estado_contador(id_cobro,descripcion) VALUES('P','PROVISIONAL');");
			db.execSQL("INSERT INTO amd_param_estado_contador(id_cobro,descripcion) VALUES('D','INSTALADO');");
			db.execSQL("INSERT INTO amd_param_estado_contador(id_cobro,descripcion) VALUES('DP','INSTALADO PROPIETARIO');");
			db.execSQL("INSERT INTO amd_param_estado_contador(id_cobro,descripcion) VALUES('SD','SERVICIO DIRECTO');");
			
			db.execSQL("CREATE TABLE amd_param_tipo_contador(	id_tipo			VARCHAR(10) NOT NULL PRIMARY KEY,"
															+ "	descripcion		VARCHAR(50) NOT NULL);");
			
			db.execSQL("INSERT INTO amd_param_tipo_contador(id_tipo,descripcion) VALUES('MB','MONOFASICO BIFILAR');");
			db.execSQL("INSERT INTO amd_param_tipo_contador(id_tipo,descripcion) VALUES('MT','MONOFASICO TRIFILAR');");
			db.execSQL("INSERT INTO amd_param_tipo_contador(id_tipo,descripcion) VALUES('T','TRIFILAR');");
			db.execSQL("INSERT INTO amd_param_tipo_contador(id_tipo,descripcion) VALUES('B','BIFASICO');");
			db.execSQL("INSERT INTO amd_param_tipo_contador(id_tipo,descripcion) VALUES('SD','SERVICIO DIRECTO');");	
			
			db.execSQL(	"INSERT INTO amd_param_acometidas_conductor(conductor,sigla_conductor) VALUES('Aluminio','A')");
			db.execSQL(	"INSERT INTO amd_param_acometidas_conductor(conductor,sigla_conductor) VALUES('Cobre','C')");
			
			
			db.execSQL(	"CREATE TABLE amd_param_censo_carga(estado_carga		VARCHAR(20) NOT NULL PRIMARY KEY,"
														+ "	sigla_estado_carga 	VARCHAR(1) UNIQUE NOT NULL)");

			db.execSQL(	"INSERT INTO amd_param_censo_carga(estado_carga,sigla_estado_carga) VALUES('Registrada','R')");
			db.execSQL(	"INSERT INTO amd_param_censo_carga(estado_carga,sigla_estado_carga) VALUES('Directa','D')");
			
			
			db.execSQL(	"CREATE TABLE amd_acometida(id_orden		VARCHAR(20) NOT NULL,"
												+ "	tipo_ingreso	VARCHAR(20) NOT NULL,"
												+ "	id_acometida	VARCHAR(20) NOT NULL,"
												+ "	longitud		NUMERIC(8,2) NOT NULL,"
												+ "	usuario_ins		VARCHAR(50) NOT NULL,"
												+ "	fecha_ins		TIMESTAMP NOT NULL DEFAULT current_timestamp,"
												+ "	fase			VARCHAR(20) NOT NULL,"
												+ "	clase 			VARCHAR(20) NOT NULL,"
												+ "	PRIMARY KEY(id_orden, tipo_ingreso, id_acometida))");
			
			db.execSQL(	"CREATE TABLE amd_pct_error(id_orden 		VARCHAR(20) NOT NULL,"
												+ "	tipo_carga		VARCHAR(50) NOT NULL,"
												+ "	voltaje 		NUMERIC(8,3) NOT NULL,"
												+ "	corriente		NUMERIC(8,3) NOT NULL,"
												+ "	tiempo			NUMERIC(8,3) NOT NULL,"
												+ "	vueltas			INTEGER NOT NULL,"
												+ "	total			NUMERIC(8,3) NOT NULL,"
												+ "	usuario_ins		VARCHAR(50) NOT NULL,"
												+ "	fecha_ins		TIMESTAMP NOT NULL DEFAULT current_timestamp,"
												+ "	rev				NUMERIC(8,3),"
												+ "	fp				NUMERIC(8,3),"
												+ "	fase			VARCHAR(10) NOT NULL,"
												+ "	PRIMARY KEY(id_orden,tipo_carga,fase))");
			
			
			db.execSQL(	"CREATE TABLE amd_param_trabajos_orden(	id_revision 		VARCHAR(20) NOT NULL,"
															+ "	id_orden			VARCHAR(20) NOT NULL,"
															+ "	id_trabajo			VARCHAR(20) NOT NULL,"
															+ "	cuenta				VARCHAR(20) NOT NULL,"
															+ "	nodo				VARCHAR(20) NOT NULL,"
															+ "	estado 				VARCHAR(1) NOT NULL,"
															+ "	fecha_ins			TIMESTAMP NOT NULL DEFAULT current_timestamp,"
															+ "	usuario_ins			VARCHAR(50) NOT NULL,"
															+ "	cantidad			NUMERIC(8,5) NOT NULL,"
															+ "	PRIMARY KEY (id_revision, id_orden, id_trabajo))");
			
			db.execSQL("CREATE TABLE amd_censo(	id_orden 			VARCHAR(50) NOT NULL PRIMARY KEY," +
											"	total_censo 		DOUBLE PRECISION NOT NULL DEFAULT 0," +
											"	carga_registrada 	DOUBLE PRECISION NOT NULL DEFAULT 0," +
											"	carga_directa 		DOUBLE PRECISION NOT NULL DEFAULT 0," +
											"	usuario_ins			VARCHAR(50)," +
											"	fecha_ins 			TIMESTAMP  NOT NULL DEFAULT current_timestamp," +
											"	carga_ndefinida	DOUBLE PRECISION NOT NULL DEFAULT 0);");
			
			db.execSQL(	"CREATE TABLE amd_censo_carga(	id_orden		VARCHAR(20) NOT NULL,"
													+ "	id_elemento		VARCHAR(20) NOT NULL,"
													+ "	capacidad		NUMERIC(10,2) NOT NULL,"
													+ "	cantidad		INTEGER NOT NULL,"
													+ "	usuario_ins		VARCHAR(50) NOT NULL,"
													+ "	fecha_ins		TIMESTAMP NOT NULL DEFAULT current_timestamp,"
													+ "	tipo_carga		VARCHAR(1),"
													+ "	PRIMARY KEY(id_orden,id_elemento,tipo_carga))");
			
						
			db.execSQL(	"CREATE TABLE amd_inconsistencias(	id_inconsistencia	VARCHAR(20) UNIQUE NOT NULL,"
														+ "	id_orden			VARCHAR(20) NOT NULL,"
														+ "	id_nodo				VARCHAR(20),"
														+ "	valor 				VARCHAR(4000),"
														+ "	fecha_ins			TIMESTAMP NOT NULL DEFAULT current_timestamp,"
														+ "	usuario_ins 		VARCHAR(50) NOT NULL,"
														+ "	cod_inconsistencia	VARCHAR(50) NOT NULL,"
														+ "	cuenta				VARCHAR(20),"
														+ "	tipo				VARCHAR(5),"
														+ "	trabajo				VARCHAR(20),"
														+ "	PRIMARY KEY (id_orden, cod_inconsistencia))");
			
							
			db.execSQL(	"CREATE TABLE amd_contadores_clientes(	 cuenta                 VARCHAR(20) NOT NULL," +
																"marca                  VARCHAR(50)," +
																"serie                  VARCHAR(50)," +
																"estado                 VARCHAR(50)," +
																"digitos                VARCHAR(50)," +
																"tipo                   VARCHAR(50)," +
																"provisional            VARCHAR(10)," +
																"lectura                VARCHAR(20)," +
																"con_contador           VARCHAR(10)," +
																"lectura_prom           VARCHAR(20)," +
																"factor_multiplicacion  VARCHAR(20)," +
																"suma_lecturas          VARCHAR(20)," +
																"meses_sumas            VARCHAR(10)," +
																"PRIMARY KEY(cuenta,marca,serie));");	
			
			
			db.execSQL(	"CREATE TABLE amd_datos_tranfor(	id_orden	VARCHAR(50) NOT NULL PRIMARY KEY,"
														+ "	numero		VARCHAR(50),"
														+ "	marca		VARCHAR(50),"
														+ "	kva			VARCHAR(50),"
														+ "	propietario	VARCHAR(50),"
														+ "	anno		VARCHAR(50),"
														+ "	voltaje1 	VARCHAR(50),"
														+ "	voltaje2	VARCHAR(50),"
														+ "	circuito	VARCHAR(50));");
			
			
			db.execSQL(	"CREATE TABLE amd_contador_cliente_orden(	 cuenta                 VARCHAR(20) NOT NULL," +
																	"marca                  VARCHAR(50)," +
																	"serie                  VARCHAR(50)," +
																	"estado                 VARCHAR(50)," +
																	"lectura                VARCHAR(20)," +
																	"digitos                INTEGER," +
																	"con_contador           VARCHAR(10)," +
																	"tipo                   VARCHAR(50)," +
																	"lectura_prom           VARCHAR(20)," +
																	"factor_multiplicacion  INTEGER," +
																	"provisional            VARCHAR(10)," +
																	"suma_lecturas          VARCHAR(20)," +
																	"meses_sumas_lecturas    VARCHAR(10)," +
																	"desinstalado           INTEGER DEFAULT 0," +
																	"PRIMARY KEY(cuenta,marca,serie));");	
			
			
			db.execSQL("CREATE TABLE amd_nodo(	 id_nodo                VARCHAR(50)," +
												"direccion              VARCHAR(50)," +
												"ubicacion              VARCHAR(50)," +
												"municipio              VARCHAR(50)," +
												"mapa                   VARCHAR(50)," +
												"fecha_ven              VARCHAR(20)," +
												"progreso               VARCHAR(50)," +
												"estado                 VARCHAR(50)," +
												"observacion            VARCHAR(200)," +
												"tipo                   VARCHAR(50)," +
												"PRIMARY KEY(id_nodo));");
			
			
			db.execSQL("CREATE TABLE amd_clientes_nodo(	 cuenta                 VARCHAR(20) NOT NULL," +
														"direccion              VARCHAR(100)," +
														"propietario            VARCHAR(100)," +
														"servicio               VARCHAR(50)," +
														"ubicacion              VARCHAR(50)," +
														"estrato                VARCHAR(50)," +
														"ruta                   VARCHAR(50)," +
														"nodo                   VARCHAR(50) NOT NULL," +
														"estado                 VARCHAR(50)," +
														"id_nodo_sec            VARCHAR(20)," +
														"registrado             INTEGER NOT NULL DEFAULT 0," +
														"PRIMARY KEY(cuenta));");
			
			db.execSQL("CREATE TABLE amd_borrar_orden(id_orden VARCHAR(20) NOT NULL, PRIMARY KEY(id_orden));");
			
			
			db.execSQL("CREATE TABLE amd_nodos_sec(	 id_nodo_prim           VARCHAR(20) NOT NULL," +
													"id_nodo_sec            VARCHAR(20) NOT NULL," +
													"id_orden               VARCHAR(20) NOT NULL," +
													"registrado             INTEGER NOT NULL DEFAULT 0," +
													"usuario_ins            VARCHAR(50)," +
													"fecha_ins              VARCHAR(20)," +
													"nuevo                  INTEGER DEFAULT 0," +
													"ubicacion              VARCHAR(20)," +
													"PRIMARY KEY(id_nodo_prim, id_nodo_sec,id_orden));");
			
			
			db.execSQL("CREATE TABLE amd_tramos_red(	 id_serial              INTEGER PRIMARY KEY AUTOINCREMENT," +
														"id_nodo                VARCHAR(20)," +
														"id_orden               VARCHAR(20)," +
														"nodo_ini               VARCHAR(20)," +
														"nodo_fin               VARCHAR(20)," +
														"accion                 VARCHAR(20)," +
														"registrado             INTEGER DEFAULT 0);");
			
			
			db.execSQL("CREATE TABLE amd_materiales_trabajo_orden( 	id_orden 		VARCHAR(20) NOT NULL,"
																+ "	id_trabajo		VARCHAR(20) NOT NULL,"
																+ "	id_material		VARCHAR(20) NOT NULL,"
																+ "	cantidad		DOUBLE NOT NULL DEFAULT 0,"
																+ "	cuotas			INTEGER NOT NULL DEFAULT 12,"
																+ "	automatico		INTEGER NOT NULL DEFAULT 0,"
																+ "	usuario_ins 	VARCHAR(50),"
																+ "	fecha_ins 		TIMESTAMP DEFAULT current_timestamp,"
																+ "PRIMARY KEY (id_orden,id_material));");
			
			
			db.execSQL("CREATE TABLE amd_materiales_provisionales(	 id_orden               VARCHAR(100)," +
																	"elemento               VARCHAR(100)," +
																	"marca                  VARCHAR(100)," +
																	"serie                  VARCHAR(100)," +
																	"id_agrupador           VARCHAR(100)," +
																	"cuenta                 VARCHAR(100)," +
																	"proceso                VARCHAR(100)," +
																	"estado                 VARCHAR(100)," +
																	"usuario_ins            VARCHAR(100)," +
																	"fecha_ins              VARCHAR(100)," +
																	"valor                  VARCHAR(100)," +
																	"cantidad               VARCHAR(100),"
																  + "PRIMARY KEY(id_orden,elemento));");
			
			db.execSQL("CREATE TABLE amd_material_usuario( id_orden 		VARCHAR(20) NOT NULL,"
														+ "material		VARCHAR(100) NOT NULL,"
														+ "cantidad		VARCHAR(50) NOT NULL,"
														+ "estado		VARCHAR(50) NOT NULL,"
														+ "disposicion	VARCHAR(50) NOT NULL,"
														+ "PRIMARY KEY(id_orden,material, estado));");
			
			
			db.execSQL("CREATE TABLE amd_sellos_contador(	 id_serial              INTEGER PRIMARY KEY AUTOINCREMENT," +
															"marca                  VARCHAR(100)," +
															"serie                  VARCHAR(100)," +
															"numero                 VARCHAR(100));");
			
			
			db.execSQL("CREATE TABLE amd_consumos_orden(	 id_orden               VARCHAR(20) NOT NULL," +
															"cuenta                 VARCHAR(20) NOT NULL," +
															"anomes                 VARCHAR(20) NOT NULL," +
															"marca                  VARCHAR(40)," +
															"serie                  VARCHAR(40)," +
															"consumo                VARCHAR(40)," +
															"lectura_anterior       VARCHAR(40)," +
															"lectura_tomada         VARCHAR(40)," +
															"observacion_lectura    VARCHAR(100)," +
															"medidor                VARCHAR(20)," +
															"PRIMARY KEY(id_orden,cuenta,anomes));");
			
			db.execSQL("CREATE TABLE amd_pruebas(	id_orden 		VARCHAR(50) NOT NULL PRIMARY KEY,"
												+ "	p_conexion		VARCHAR(50) NOT NULL,"
												+ "	p_continuidad	VARCHAR(50) NOT NULL,"
												+ "	p_puentes		VARCHAR(50) NOT NULL,"
												+ "	p_integrador	VARCHAR(50) NOT NULL,"
												+ "	p_vacio			VARCHAR(50) NOT NULL,"
												+ "	p_frenado		VARCHAR(50) NOT NULL,"
												+ "	p_retirado		VARCHAR(50) NOT NULL,"
												+ "	rozamiento		VARCHAR(50) NOT NULL,"
												+ "	aplomado 		VARCHAR(50) NOT NULL);");
			
			
			db.execSQL("CREATE TABLE amd_prueba_integracion(	id_orden 	VARCHAR(50) NOT NULL PRIMARY KEY,"
															+ "	lect_ini	VARCHAR(20) NOT NULL,"
															+ "	lect_final	VARCHAR(20) NOT NULL,"
															+ "	giros		VARCHAR(20) NOT NULL);");
			
			
			db.execSQL("CREATE TABLE amd_observacion_materiales(id_orden VARCHAR(20) NOT NULL PRIMARY KEY,"
															+ "	observacion VARCHAR(500) NOT NULL);");*/
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("UPDATE db_parametros SET valor = '1.6' WHERE item = 'version'");
			
			db.execSQL(	"CREATE TRIGGER tg_fecha_revision AFTER INSERT ON amd_actas FOR EACH ROW BEGIN " +
						"	UPDATE amd_actas SET fecha_revision=datetime('now','localtime') WHERE id_orden = NEW.id_orden;" +
						"END;");
		}
	}
	
	
	public boolean SQLString(String Query){
		ValorRetorno = false;
		try{
			nBD.execSQL(Query);		
			ValorRetorno = true;
		}catch(Exception e){
			ValorRetorno = false;
		}
		return ValorRetorno;
	}
	
	
	public SQLite (Context c, String CurrentDirectory){
		this.nContexto = c;
		this.Directorio = CurrentDirectory;
		SQLite.N_BD = this.Directorio + File.separator + "BdDesviaciones_Lecturas";
		
		this.TablasAmData.put("SGD_CONTADORES_FRONTERA_EXP", "amd_contadores_clientes");
		this.TablasAmData.put("SGD_CONTADORES_INSTALADOS_EXP", "amd_contador_cliente_orden");
		this.TablasAmData.put("SGD_CONTADORES_NUEVOS_EXP", "amd_bodega_contadores");
		this.TablasAmData.put("SGD_NODOS_EXP", "amd_nodo");
		this.TablasAmData.put("SGD_CUENTA_FRONTERA_EXP", "amd_clientes_nodo");
		this.TablasAmData.put("SGD_ORDENES_TRABAJO_EXP", "amd_ordenes_trabajo");
		this.TablasAmData.put("SGD_BORRAR_ORDENES_EXP", "amd_borrar_orden");
		this.TablasAmData.put("SGD_NODOS_SEC_EXP", "amd_nodos_sec");
		this.TablasAmData.put("SGD_TRAMOS_RED_EXP", "amd_tramos_red");
		this.TablasAmData.put("SGD_ELEMENTOS_PROV_EXP", "amd_materiales_provisionales");
		this.TablasAmData.put("SGD_BODEGA_SELLOS_EXP", "amd_bodega_sellos");
		this.TablasAmData.put("SGD_SELLOS_CONTADOR_EXP", "amd_sellos_contador");
		this.TablasAmData.put("SGD_CONSUMOS_ORDEN_EXP", "amd_consumos_orden");
		
		ArchSQL = new Archivos(this.nContexto, this.Directorio, 10);
		if(!ArchSQL.ExistFolderOrFile(this.Directorio)){		
			ArchSQL.MakeDirectory();
		}
	}
	
	
	private SQLite abrir(){
		nHelper = new BDHelper(nContexto);
		nBD = nHelper.getWritableDatabase();
		return this;
	}

	private void cerrar() {
		nHelper.close();
	}

	
	
	/**Funcion para ejecutar una sentencia SQL recibida por parametro
	 * @param _sql	->Sentencia SQL a ejecutar
	 * @return		->true en caso de ejecutarse correctamente, false en otros casos
	 */
	public boolean EjecutarSQL(String _sql){
		boolean _retorno = false;
		abrir();
		try{
			nBD.execSQL(_sql);  
			_retorno = true;;
		}catch(Exception e){	
		}	
		cerrar();
		return _retorno;
	}
	
	
	/**Funcion para realizar INSERT
	 * @param _tabla 		-> tabla a la cual se va a realizar el INSERT
	 * @param _informacion	-> informacion que se va a insertar 
	 * @return				-> true si se realizo el insert correctamente, false en otros casos
	 */	
	public boolean InsertRegistro(String _tabla, ContentValues _informacion){
		abrir();
		ValorRetorno = false;
		try{
			if(nBD.insert(_tabla,null, _informacion)>=0){
				ValorRetorno = true;
			}
		}catch(Exception e){			
		}			
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion para realizar UPDATE 
	 * @param _tabla		->tabla sobre la cual se va a realizar el UPDATE	
	 * @param _informacion	->informacion que se va a actualizar
	 * @param _condicion	->condcion que debe tener el registro para ejecutar el UPDATE
	 * @return				->true si se realizo el UPDATE correctamente, flase en otros casos
	 */
	public boolean UpdateRegistro(String Tabla, ContentValues Informacion, String Condicion){
		ValorRetorno = false;
		abrir();
		try{
			if(nBD.update(Tabla, Informacion, Condicion, null)>=0){
				ValorRetorno = true;
			}
		}catch(Exception e){
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion para realizar un insert en caso de no existir el registro o update en caso de existir
	 * @param _tabla		->tabla sobre la cual se va a operar
	 * @param _informacion	->informacion que se va a insertar o actualizar
	 * @param _condicion	->Condicion que debe cumplirse para realizar el update y/o insert
	 * @return				->String con el mensaje de retorno, ya puede ser insert/update realizado o no realizado.
	 */
	public String InsertOrUpdateRegistro(String _tabla, ContentValues _informacion, String _condicion){
		String _retorno = "Sin acciones";
		if(!this.ExistRegistros(_tabla, _condicion)){
			if(this.InsertRegistro(_tabla, _informacion)){
				_retorno = "Registro ingresado en "+_tabla;
			}else{	
				_retorno = "Error al ingresar el registro en "+_tabla;
			}	
		}else{
			if(this.UpdateRegistro(_tabla, _informacion, _condicion)){
					_retorno = "Registro actualizado en "+_tabla;
			}else{
				_retorno = "Error al actualizar el registro en "+_tabla;
			}
		}		
		return _retorno;		
	}
	
	
	/**Funcion para eliminar un registro de una tabla en particular
	 * @param _tabla		->tabla sobre la cual se va a trabajar
	 * @param _condicion	->condicion que debe cumplirse para ejecutar el delete respectivo	
	 * @return				->true si fue ejecutado el delete correctamente, false en caso contrario
	 */
	public boolean DeleteRegistro(String _tabla, String _condicion){
		ValorRetorno = false;
		abrir();
		try{
			if(nBD.delete(_tabla, _condicion,null)>0){
				ValorRetorno = true;
			}
		}catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion encargada de realizar una consulta y retornarla con un array list de content values, similar a un array de diccionarios
	 * @param _tabla		->tabla sobre la cual va a correr la consulta
	 * @param _campos		->campos que se van a consultar
	 * @param _condicion	->condicion para filtrar la consulta
	 * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
	 */
	public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion){
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion, null);
			String[] _columnas = c.getColumnNames();
					
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_registro.put(_columnas[i], c.getString(i));
				}
				_query.add(_registro);
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}	
		this.cerrar();		
		return _query;		
	}
	
	
	/**Funcion para realizar la consulta de un registro, retorna un contentvalues con la informacion consultada
	 * @param _tabla		->tabla sobre la cual se va a ejecutar la consulta
	 * @param _campos		->campos que se quieren consultar
	 * @param _condicion	->condicion para ejecutar la consulta
	 * @return				-->ContentValues con la informacion del registro producto de la consulta
	 */
	public ContentValues SelectDataRegistro(String _tabla, String _campos, String _condicion){
		ContentValues _query = new ContentValues();
		_query.clear();
		this.abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" LIMIT 1", null);
			String[] _columnas = c.getColumnNames();
						
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				//ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					_query.put(_columnas[i], c.getString(i));
				}
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}	
		this.cerrar();		
		return _query;		
	}
		
	
	//Relizar la consulta teniendo en cuenta varios JOIN a la izquierda
	public ArrayList<ContentValues> SelectNJoinLeftData(String _tabla, String _campos, String _join_left[], String _on_left[], String _condicion){
		String Query = "";
		ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
		_query.clear();
		this.abrir();
		try{
			Query = "SELECT DISTINCT "+ _campos + " FROM "+ _tabla;
			for(int i=0;i<_join_left.length;i++){
				Query += " LEFT JOIN " +_join_left[i] + " ON "+_on_left[i];
			}
			Query += " WHERE "+ _condicion;
			Cursor c = nBD.rawQuery(Query, null);
			String[] _columnas = c.getColumnNames();
					
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				ContentValues _registro = new ContentValues();
				for(int i=0;i<_columnas.length;i++){
					if(c.getString(i) == null){
						_registro.put(_columnas[i], "");
					}else{
						_registro.put(_columnas[i], c.getString(i));
					}
				}
				_query.add(_registro);
			}			
		}
		catch(Exception e){
			Log.i("Error en SQLite", e.toString());
		}	
		this.cerrar();		
		return _query;		
	}
	

	
	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un entero
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public int IntSelectShieldWhere(String _tabla, String _campo, String _condicion){
		int intRetorno = -1;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				intRetorno = c.getInt(0);
			}			
		}
		catch(Exception e){
			intRetorno = -1;
		}
		cerrar();
		return intRetorno;
	}
	
	
	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un double
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public double DoubleSelectShieldWhere(String _tabla, String _campo, String _condicion){
		double intRetorno = -1;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				intRetorno = c.getDouble(0);
			}			
		}
		catch(Exception e){
			intRetorno = -1;
		}
		cerrar();
		return intRetorno;
	}
	
	
	/**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un String
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _campo		->Campo que se quiere consultar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public String StrSelectShieldWhere(String _tabla, String _campo, String _condicion){
		String strRetorno = null;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				strRetorno = c.getString(0);
			}			
		}
		catch(Exception e){
			e.toString();
			strRetorno = null;
		}
		cerrar();
		return strRetorno;
	}
	
	
	/**Funcion retorna la cantidad de registros de una tabla que cumplen la condicion recibida por parametro
	 * @param _tabla		->Tabla sobre la cual se va a trabajar
	 * @param _condicion	->Condicion para filtro de la consulta
	 * @return
	 */
	public int CountRegistrosWhere(String _tabla, String _condicion){
		int ValorRetorno = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT count(*) FROM "+ _tabla +" WHERE "+ _condicion, null);
			c.moveToFirst();
			ValorRetorno = c.getInt(0);		
		}
		catch(Exception e){
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion que retorna true o falso segun existan o no registros que cumplan la condicion recibida por parametro
	 * @param _tabla		->Tabla sobre la cual se va trabajar
	 * @param _condicion	->Condicion de filtro
	 * @return
	 */
	public boolean ExistRegistros(String _tabla, String _condicion){
		ValorRetorno = false;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT count(*) as cantidad FROM " + _tabla +" WHERE " + _condicion , null);
			c.moveToFirst();
			if(c.getDouble(0)>0)
				ValorRetorno = true;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();
		return ValorRetorno;
	}
	
	
	/**Funcion que retorna la cantidad de minutos transcurridos desde la fecha actual y la recibida por parametro
	 * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
	 * @return 			->String con el resultado en minutos
	 */
	public String MinutesBetweenDateAndNow(String _oldDate){
		String _retorno = "";
		int _minutos = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT strftime('%s','now')-strftime('%s','"+_oldDate+"') as segundos", null);
			c.moveToFirst();
			_retorno = c.getString(0);
			_minutos = Integer.parseInt(_retorno)/60;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();		
		return String.valueOf(_minutos);		
	}
	
	
	/**Funcion que retorna la cantidad de minutos transcurridos entre las fechas recibidas por parametro
	 * @param _newDate	->fecha mas reciente contra la cual se quiere caldular la diferencia
	 * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
	 * @return 			->Strig con el resultado en minutos
	 */
	public String MinutesBetweenDates(String _newDate, String _oldDate){
		String _retorno = "";
		int _minutos = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT strftime('%s','"+_newDate+"')-strftime('%s','"+_oldDate+"') as segundos", null);
			c.moveToFirst();
			_retorno = c.getString(0);
			_minutos = Integer.parseInt(_retorno)/60;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
		}
		cerrar();		
		return String.valueOf(_minutos);		
	}
}
