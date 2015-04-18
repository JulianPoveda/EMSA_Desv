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
	private static final int VERSION_BD = 5;																		
	
	private BDHelper nHelper;
	private Context nContexto;
	private String Directorio;
	private SQLiteDatabase nBD;
	
	private boolean ValorRetorno;
	
	private static class BDHelper extends SQLiteOpenHelper{
		
		public BDHelper(Context context) {
			super(context, N_BD, null, VERSION_BD);
		}

		@Override
		public void onCreate(SQLiteDatabase db){
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
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('servidor','http://192.168.0.49',0) ");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('puerto','80',0) ");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('modulo','DesviacionesLecturas/WS',0) ");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('web_service','JavaWS.php?wsdl',0)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('equipo','1',0)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('cedula_tecnico','sin_asignar',0)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('nombre_tecnico','sin_asignar',0)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('impresora','sin asignar',1)");
			db.execSQL("INSERT INTO amd_configuracion (item,valor,nivel) VALUES ('version','2.2',0)");
			
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
														" carga_contratada	INTEGER," +
														" cod_apertura		VARCHAR(10) NOT NULL," +
														" tipo_accion		INTEGER," +
														" estado			VARCHAR(1) NOT NULL DEFAULT 'P');");
			
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
													"periodo 			INTEGER NOT NULL," +			
													"cuenta				VARCHAR(10) NOT NULL," +
													"marca				VARCHAR(30) NOT NULL," +
													"serie  			VARCHAR(30) NOT NULL," +
													"fecha_tomada		VARCHAR(50) NOT NULL," +
													"fecha_anterior 	VARCHAR(50) NOT NULL," +
													"lectura_tomada		DOUBLE PRECISION NOT NULL," +
													"lectura_anterior	DOUBLE PRECISION NOT NULL," +
													"consumo			DOUBLE PRECISION NOT NULL," +
													"PRIMARY KEY(solicitud, fecha_tomada));");
			
			
			db.execSQL(	"CREATE TABLE parametros_acometida( 	 combo 					VARCHAR(100) NOT NULL," +
																"id_opcion 				INTEGER NOT NULL," +
																"descripcion_opcion 	VARCHAR(255) NOT NULL," +
																"PRIMARY KEY(combo,id_opcion));");
			
			
			db.execSQL(	"CREATE TABLE parametros_actas( 	 combo 					VARCHAR(100) NOT NULL," +
															"id_opcion 				INTEGER NOT NULL," +
															"descripcion_opcion 	VARCHAR(255) NOT NULL," +
															"PRIMARY KEY(combo,id_opcion));");
			
			
			db.execSQL(	"CREATE TABLE parametros_sellos(	 combo 					VARCHAR(100) NOT NULL," +
															"id_opcion 				INTEGER NOT NULL," +
															"descripcion_opcion 	VARCHAR(255) NOT NULL," +
															"PRIMARY KEY(combo,id_opcion));");
			
			
			db.execSQL(	"CREATE TABLE parametros_municipios(	 id_municipio			INTEGER NOT NULL PRIMARY KEY," +
																"municipio 				VARCHAR(255) NOT NULL);");
			
			db.execSQL(	"CREATE TABLE parametros_medidores(	 marca			VARCHAR(20) NOT NULL PRIMARY KEY," +
															"nombre 		VARCHAR(255) NOT NULL);");
			
			db.execSQL(	"CREATE TABLE parametros_irregularidades( 	 id_irregularidad		INTEGER NOT NULL PRIMARY KEY," +
																	"descripcion 			VARCHAR(255) NOT NULL," +
																	"tipo			 		VARCHAR(10) NOT NULL);");
			
			db.execSQL(	"CREATE TABLE parametros_elementos_censo( 	 codigo			INTEGER NOT NULL PRIMARY KEY," +
																	"descripcion 	VARCHAR(255) NOT NULL," +
																	"minimo			DOUBLE PRECISION NOT NULL," +
																	"maximo			DOUBLE PRECISION NOT NULL);");

			db.execSQL(	"CREATE TABLE parametros_codificacion_cometida(  id_acometida	VARCHAR(50) NOT NULL PRIMARY KEY," +
																		"calibre 		VARCHAR(10) NOT NULL," +
																		"tipo_acometida	VARCHAR(10) NOT NULL," +
																		"conductor		VARCHAR(10) NOT NULL);");
			
			db.execSQL( "CREATE TABLE parametros_respuesta_pqr(	 dependencia 				INTEGER NOT NULL," +
																"tipo_accion 				INTEGER NOT NULL," +
																"descripcion_tipo_solicitud VARCHAR(255) NOT NULL," +
																"id_respuesta				INTEGER NOT NULL," +
																"respuesta 					VARCHAR(255)," +
																"PRIMARY KEY(dependencia,tipo_accion,id_respuesta));");
			
			db.execSQL( "CREATE TABLE dig_acometida( solicitud 		VARCHAR(20) NOT NULL PRIMARY KEY," +
													"tipo_ingreso 	VARCHAR(20) NOT NULL," +
													"conductor		VARCHAR(20) NOT NULL," +
													"tipo			VARCHAR(20) NOT NULL," +
													"calibre		VARCHAR(20) NOT NULL," +
													"clase 			VARCHAR(20) NOT NULL," +
													"fases 			VARCHAR(20) NOT NULL," +
													"longitud 		DOUBLE PRECISION NOT NULL);");
			
			db.execSQL( "CREATE TABLE dig_contador(  solicitud 	VARCHAR(20) NOT NULL PRIMARY KEY," +
													"marca 		VARCHAR(30) NOT NULL," +
													"serie		VARCHAR(30)," +
													"lectura1	DOUBLE PRECISION," +
													"lectura2   DOUBLE PRECISION," +
													"lectura3   DOUBLE PRECISION," +
													"tipo 		VARCHAR(30));");
			
			db.execSQL( "CREATE TABLE dig_pruebas(  solicitud 			VARCHAR(20) NOT NULL PRIMARY KEY," +
													"lectura_inicial 	NUMERIC(10,1) NOT NULL," +
													"lectura_final		NUMERIC(10,1) NOT NULL," +
													"coeficiente		NUMERIC(10,3) NOT NULL);");
			
			db.execSQL( "CREATE TABLE dig_actas(     solicitud 		VARCHAR(20) NOT NULL PRIMARY KEY," +
													"doc_enterado 	VARCHAR(30)," +
													"nom_enterado	VARCHAR(100)," +
													"doc_testigo	VARCHAR(30)," +
													"nom_testigo   	VARCHAR(100)," +
													"tipo_enterado	VARCHAR(30)," +
													"respuesta_pqr	VARCHAR(50)," +
													"fecha_ins		TIMESTAMP NOT NULL DEFAULT current_timestamp);");
			
			db.execSQL( "CREATE TABLE dig_impresiones_inf(   solicitud 		VARCHAR(20) NOT NULL," +
															"id_impresion 	INTEGER NOT NULL," +
															"nombre_archivo	VARCHAR(100)," +
															"fecha_imp		TIMESTAMP NOT NULL DEFAULT current_timestamp," +
															"PRIMARY KEY(solicitud, id_impresion));");
			
			db.execSQL("CREATE TABLE dig_censo_carga(  	 solicitud 		VARCHAR(20) NOT NULL," +
														"id_elemento	INTEGER NOT NULL," +
														"cantidad		INTEGER NOT NULL," +
														"vatios			INTEGER NOT NULL," +
														"carga			VARCHAR(10) NOT NULL," +
														"servicio		VARCHAR(10) NOT NULL," +
														"PRIMARY KEY(solicitud,id_elemento,carga,servicio));");
			
			db.execSQL("CREATE TABLE dig_sellos(  	 solicitud 		VARCHAR(20) NOT NULL," +
													"tipo_ingreso	VARCHAR(50) NOT NULL," +
													"tipo_sello		VARCHAR(50) NOT NULL," +
													"ubicacion		VARCHAR(50) NOT NULL," +
													"color			VARCHAR(50) NOT NULL," +
													"serie			VARCHAR(50) NOT NULL," +
													"irregularidad	VARCHAR(50) NOT NULL," +
													"PRIMARY KEY(solicitud,tipo_ingreso,tipo_sello,serie));");
			
			db.execSQL("CREATE TABLE dig_irregularidades(solicitud 		VARCHAR(20)  NOT NULL," +
														"irregularidad	VARCHAR(255) NOT NULL," +
														"PRIMARY KEY(solicitud,irregularidad));");
			
			db.execSQL("CREATE TABLE dig_observaciones(	 solicitud 			VARCHAR(20)  NOT NULL," +
														"tipo_observacion 	VARCHAR(50)  NOT NULL," +
														"observacion		VARCHAR(255) NOT NULL," +
														"PRIMARY KEY(solicitud,tipo_observacion));");
			
			db.execSQL("CREATE TABLE dig_datos_actas(solicitud 			VARCHAR(20) NOT NULL PRIMARY KEY," +
													"irregularidades 	VARCHAR(50)," +
													"prueba_rozamiento	VARCHAR(50)," +
													"prueba_frenado		VARCHAR(50)," +
													"prueba_vacio		VARCHAR(50)," +
													"familias			INTEGER," +
													"fotos				VARCHAR(10)," +
													"electricista		VARCHAR(10)," +
													"clase_medidor		VARCHAR(50)," +
													"ubicacion_medidor	VARCHAR(50)," +
													"aplomado			VARCHAR(50)," +
													"registrador		VARCHAR(50)," +
													"telefono			VARCHAR(20)," +
													"porcentaje_no_res	DOUBLE PRECISION);");
			
			db.execSQL("CREATE TABLE dig_adecuaciones(	 solicitud 			VARCHAR(20) NOT NULL PRIMARY KEY," +
														"suspension		 	VARCHAR(50)," +
														"tubo				VARCHAR(50)," +
														"armario			VARCHAR(50)," +
														"soporte			VARCHAR(50)," +
														"tierra				VARCHAR(50)," +
														"acometida			VARCHAR(10)," +
														"caja				VARCHAR(10)," +
														"medidor			VARCHAR(50)," +
														"otros				VARCHAR(50));");
			
			db.execSQL(	"CREATE VIEW vista_parametros_medidores AS " +
							"SELECT marca, nombre , marca||' ('||nombre||')' AS resumen " +
							"FROM parametros_medidores;");
			
			db.execSQL(	"CREATE VIEW vista_censo_carga AS " +
							"SELECT a.solicitud, a.id_elemento, b.descripcion, a.cantidad, a.vatios, a.carga, a.servicio " +
							"FROM   dig_censo_carga AS a " +
							"JOIN   parametros_elementos_censo AS b " +
							"ON     a.id_elemento = b.codigo;");
			
			db.execSQL(	"CREATE VIEW vista_in_ordenes_trabajo AS " +
							"SELECT 	a.id_serial,a.solicitud,a.dependencia,a.pda,a.cuenta,a.municipio,b.municipio AS nombre_municipio,a.suscriptor," +
							"			a.direccion,a.clase_servicio,a.estrato,a.nodo,a.marca,a.serie,a.carga_contratada,a.estado " +
							"FROM		in_ordenes_trabajo as a " +
							"JOIN 		parametros_municipios as b " +
							"ON 		a.municipio = b.id_municipio;");
			
			db.execSQL("CREATE VIEW vista_dig_actas  AS " +
						"	SELECT 	a.solicitud, a.doc_enterado, a.nom_enterado, a.doc_testigo, a.nom_testigo, a.tipo_enterado, a.respuesta_pqr, " +
						"			strftime('%d',a.fecha_ins) as dia, strftime('%m',a.fecha_ins) as mes, strftime('%Y',a.fecha_ins) as anno," +
						"			strftime('%H:%M:%S',a.fecha_ins) as hora, b.suscriptor " +
						"	FROM 	dig_actas as a " +
						"	JOIN 	in_ordenes_trabajo as b " +
						"	ON 		a.solicitud = b.solicitud;");
			
			db.execSQL(	"CREATE TRIGGER tg_fecha_revision AFTER INSERT ON dig_actas FOR EACH ROW BEGIN " +
						"	UPDATE dig_actas SET fecha_ins=datetime('now','localtime') WHERE solicitud = NEW.solicitud;" +
						"END;");
			
			db.execSQL(	"CREATE TRIGGER tg_fecha_impresion AFTER INSERT ON dig_impresiones_inf FOR EACH ROW BEGIN " +
						"	UPDATE dig_impresiones_inf SET fecha_imp=datetime('now','localtime') WHERE solicitud = NEW.solicitud AND id_impresion = NEW.id_impresion;" +
						"END;");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("UPDATE amd_configuracion SET valor = '2.2' WHERE item = 'version'");
			
			/*db.execSQL(	"CREATE TRIGGER tg_fecha_impresion AFTER INSERT ON dig_impresiones_inf FOR EACH ROW BEGIN " +
						"	UPDATE dig_impresiones_inf SET fecha_imp=datetime('now','localtime') WHERE solicitud = NEW.solicitud AND id_impresion = NEW.id_impresion;" +
						"END;");
			
			db.execSQL( "CREATE TABLE dig_impresiones_inf(   solicitud 		VARCHAR(20) NOT NULL," +
															"id_impresion 	INTEGER NOT NULL," +
															"nombre_archivo	VARCHAR(100)," +
															"fecha_imp		TIMESTAMP NOT NULL DEFAULT current_timestamp," +
															"PRIMARY KEY(solicitud, id_impresion));");*/
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
	
	
	
	/**Funcion que retorna la cantidad de dias transcurridos desde la fecha actual y la recibida por parametro
	 * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
	 * @return 			->String con el resultado en minutos
	 */
	public Double DaysBetweenDateAndNow(String _oldDate){
		double _days = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT strftime('%s','now')-strftime('%s','"+_oldDate+"') as segundos", null);
			c.moveToFirst();
			_days = Double.parseDouble(c.getString(0))/86400;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
			_days = 0;
		}
		cerrar();		
		return _days;		
	}
	
	
	/**Funcion que retorna la cantidad de horas transcurridos desde la fecha actual y la recibida por parametro
	 * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
	 * @return 			->String con el resultado en minutos
	 */
	public Double HoursBetweenDateAndNow(String _oldDate){
		double _hours = 0;
		abrir();
		try{
			Cursor c = nBD.rawQuery("SELECT strftime('%s','now')-strftime('%s','"+_oldDate+"') as segundos", null);
			c.moveToFirst();
			_hours = Double.parseDouble(c.getString(0))/360;
		}catch(Exception e){
			Log.v("Excepcion",e.toString());
			_hours = 0;
		}
		cerrar();		
		return _hours;		
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
