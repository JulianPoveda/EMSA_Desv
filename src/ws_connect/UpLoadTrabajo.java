package ws_connect;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import clases.ClassConfiguracion;
import clases.ClassInSolicitudes;

import sistema.Archivos;
import sistema.SQLite;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


public class  UpLoadTrabajo extends AsyncTask<String, Integer, Integer>{
	private Context 			WS_UpLoadContext;
	private ClassInSolicitudes	FcnSolicitudes;
	private ClassConfiguracion 	FcnCfg;
	private SQLite				UpLoadSQL;
	private Archivos 			ArchUpLoadWS;
	private String 				FolderWS;
	
	private ContentValues			_tempRegistro 	= new ContentValues();
	private ArrayList<ContentValues>_tempTabla 		= new ArrayList<ContentValues>();
	private String					_informacion;
	private String[]				_solicitudes;
	private int 					Respuesta = 0;
	
	private String 	_ip_servidor	= "";
	private String  _puerto			= "";
	private String  _modulo 		= "";
	private String 	_web_service 	= "";
	
	private String URL; 			
	private String NAMESPACE; 	
	private static final String METHOD_NAME	= "UpLoadTrabajoMixto";	
	private static final String SOAP_ACTION	= "UpLoadTrabajoMixto";
	SoapPrimitive response = null;	
	ProgressDialog 	_pDialog;

	
	public  UpLoadTrabajo(Context context, String Directorio){
		this.WS_UpLoadContext = context;
		this.FolderWS = Directorio;
		
		this.FcnCfg 		= new ClassConfiguracion(this.WS_UpLoadContext, this.FolderWS);
		this.FcnSolicitudes = new ClassInSolicitudes(this.WS_UpLoadContext, this.FolderWS);
		
		ArchUpLoadWS= new Archivos(this.WS_UpLoadContext, this.FolderWS, 10);
		UpLoadSQL 	= new SQLite(this.WS_UpLoadContext, this.FolderWS);
		
		this._ip_servidor 	= this.FcnCfg.getServidor();
		this._puerto 		= this.FcnCfg.getPuerto(); 
		this._modulo		= this.FcnCfg.getModulo();
		this._web_service	= this.FcnCfg.getWebService();	
		
		this._informacion 	= "";
		
		this.URL 			= _ip_servidor+":"+_puerto+"/"+_modulo+"/"+_web_service;
		this.NAMESPACE 		= _ip_servidor+":"+_puerto+"/"+_modulo;
	}

	
   	 
	protected void onPreExecute(){
		Toast.makeText(this.WS_UpLoadContext,"Iniciando conexion con el servidor, por favor espere...", Toast.LENGTH_SHORT).show();	
		_pDialog = new ProgressDialog(this.WS_UpLoadContext);
        _pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _pDialog.setMessage("Ejecutando operaciones...");
        _pDialog.setCancelable(false);
        _pDialog.setProgress(0);
        _pDialog.setMax(100);
        _pDialog.show();	
	}

	@Override
	protected Integer doInBackground(String... params) {
		this._solicitudes = params[1].split("\\|");
		for(int i=0;i<this._solicitudes.length;i++){
			this._tempRegistro = this.UpLoadSQL.SelectDataRegistro("in_ordenes_trabajo", "id_serial,solicitud,estado", "solicitud='"+this._solicitudes[i]+"'");
			if(this._tempRegistro.getAsString("estado").equals("P")){
				this._informacion += "PEN|"+this._tempRegistro.getAsString("id_serial")+"|"+this._tempRegistro.getAsString("solicitud")+"\n";
			}else if(this._tempRegistro.getAsString("estado").equals("T")){
				this._informacion += "SOL|"+this._tempRegistro.getAsString("id_serial")+"|"+this._tempRegistro.getAsString("solicitud")+"\n";
				this._tempRegistro = this.UpLoadSQL.SelectDataRegistro(	"dig_acometida", 
																		"tipo_ingreso,conductor,tipo,calibre,clase,fases,longitud", 
																		"solicitud='"+this._solicitudes[i]+"'");
				if(this._tempRegistro.size()>0){
					this._informacion += "DIG_ACOMETIDA|"+this._tempRegistro.getAsString("tipo_ingreso")+"|"+this._tempRegistro.getAsString("conductor")+"|"+this._tempRegistro.getAsString("tipo")+"|"+this._tempRegistro.getAsString("calibre")+"|"+this._tempRegistro.getAsString("clase")+"|"+this._tempRegistro.getAsString("fases")+"|"+this._tempRegistro.getAsString("longitud")+"\n";
				}
				
				this._tempRegistro = this.UpLoadSQL.SelectDataRegistro(	"dig_contador", 
																		"marca,serie,lectura1,lectura2,tipo",
																		"solicitud='"+this._solicitudes[i]+"'" );
				if(this._tempRegistro.size()>0){
					this._informacion += "DIG_CONTADOR|"+this._tempRegistro.getAsString("marca")+"|"+this._tempRegistro.getAsString("serie")+"|"+this._tempRegistro.getAsString("lectura1")+"|"+this._tempRegistro.getAsString("lectura2")+"|"+this._tempRegistro.getAsString("tipo")+"\n";
				}
				
				this._tempRegistro = this.UpLoadSQL.SelectDataRegistro(	"dig_actas", 
																		"nom_enterado,doc_enterado,nom_testigo,doc_testigo,tipo_enterado,respuesta_pqr,fecha_ins",
																		"solicitud='"+this._solicitudes[i]+"'" );
				if(this._tempRegistro.size()>0){
					this._informacion += "DIG_ACTAS|"+this._tempRegistro.getAsString("nom_enterado")+"|"+this._tempRegistro.getAsString("doc_enterado")+"|"+this._tempRegistro.getAsString("nom_testigo")+"|"+this._tempRegistro.getAsString("doc_testigo")+"|"+this._tempRegistro.getAsString("tipo_enterado")+"|"+this._tempRegistro.getAsString("respuesta_pqr")+"|"+this._tempRegistro.getAsString("fecha_ins")+"\n";
				}
				
				this._tempTabla = this.UpLoadSQL.SelectData("dig_censo_carga", 
															"id_elemento,cantidad,vatios,carga,servicio",
															"solicitud='"+this._solicitudes[i]+"'" );
				for(int j=0;j<this._tempTabla.size();j++){
					this._tempRegistro = this._tempTabla.get(j);
					this._informacion += "DIG_CENSO_CARGA|"+this._tempRegistro.getAsString("id_elemento")+"|"+this._tempRegistro.getAsString("cantidad")+"|"+this._tempRegistro.getAsString("vatios")+"|"+this._tempRegistro.getAsString("carga")+"|"+this._tempRegistro.getAsString("servicio")+"\n";
				}
				
				
				this._tempTabla = this.UpLoadSQL.SelectData("dig_sellos", 
															"tipo_ingreso,tipo_sello,ubicacion,color,serie,irregularidad",
															"solicitud='"+this._solicitudes[i]+"'" );
				for(int j=0;j<this._tempTabla.size();j++){
					this._tempRegistro = this._tempTabla.get(j);
					this._informacion += "DIG_SELLOS|"+this._tempRegistro.getAsString("tipo_ingreso")+"|"+this._tempRegistro.getAsString("tipo_sello")+"|"+this._tempRegistro.getAsString("ubicacion")+"|"+this._tempRegistro.getAsString("color")+"|"+this._tempRegistro.getAsString("serie")+"|"+this._tempRegistro.getAsString("irregularidad")+"\n";
				}
				
				
				this._tempTabla = this.UpLoadSQL.SelectData("dig_irregularidades", 
															"irregularidad",
															"solicitud='"+this._solicitudes[i]+"'" );
				for(int j=0;j<this._tempTabla.size();j++){
					this._tempRegistro = this._tempTabla.get(j);
					this._informacion += "DIG_IRREGULARIDADES|"+this._tempRegistro.getAsString("irregularidad")+"\n";
				}
				
				this._tempTabla = this.UpLoadSQL.SelectData("dig_observaciones", 
															"tipo_observacion,observacion",
															"solicitud='"+this._solicitudes[i]+"'" );
				for(int j=0;j<this._tempTabla.size();j++){
					this._tempRegistro = this._tempTabla.get(j);
					this._informacion += "DIG_OBSERVACIONES|"+this._tempRegistro.getAsString("tipo_observacion")+"|"+this._tempRegistro.getAsString("observacion")+"\n";
				}
				
				
				this._tempRegistro = this.UpLoadSQL.SelectDataRegistro(	"dig_datos_actas", 
																		"irregularidades,prueba_rozamiento,prueba_frenado,prueba_vacio,familias,fotos,electricista,clase_medidor,ubicacion_medidor,aplomado,registrador,telefono,porcentaje_no_res",
																		"solicitud='"+this._solicitudes[i]+"'" );
				if(this._tempRegistro.size()>0){
					this._informacion += "DIG_DATOS_ACTAS|"+this._tempRegistro.getAsString("irregularidades")+"|"+
															this._tempRegistro.getAsString("prueba_rozamiento")+"|"+
															this._tempRegistro.getAsString("prueba_frenado")+"|"+
															this._tempRegistro.getAsString("prueba_vacio")+"|"+
															this._tempRegistro.getAsString("familias")+"|"+
															this._tempRegistro.getAsString("fotos")+"|"+
															this._tempRegistro.getAsString("electricista")+"|"+
															this._tempRegistro.getAsString("clase_medidor")+"|"+
															this._tempRegistro.getAsString("ubicacion_medidor")+"|"+
															this._tempRegistro.getAsString("aplomado")+"|"+
															this._tempRegistro.getAsString("registrador")+"|"+
															this._tempRegistro.getAsString("telefono")+"|"+
															this._tempRegistro.getAsString("porcentaje_no_res")+"\n";
				}
				
				this._tempRegistro = this.UpLoadSQL.SelectDataRegistro(	"dig_adecuaciones", 
																		"suspension,tubo,armario,soporte,tierra,acometida,caja,medidor,otros",
																		"solicitud='"+this._solicitudes[i]+"'" );
				if(this._tempRegistro.size()>0){
					this._informacion += "DIG_ADECUACIONES|"+this._tempRegistro.getAsString("suspension")+"|"+
															this._tempRegistro.getAsString("tubo")+"|"+
															this._tempRegistro.getAsString("armario")+"|"+
															this._tempRegistro.getAsString("soporte")+"|"+
															this._tempRegistro.getAsString("tierra")+"|"+
															this._tempRegistro.getAsString("acometida")+"|"+
															this._tempRegistro.getAsString("caja")+"|"+
															this._tempRegistro.getAsString("medidor")+"|"+
															this._tempRegistro.getAsString("otros")+"|"+"\n";
				}			
			}			
		}
		this.ArchUpLoadWS.DoFile("", "UpLoadTrabajo.txt", this._informacion);
		try {
			SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
			so.addProperty("id_interno", params[0]);
			so.addProperty("informacion",this.ArchUpLoadWS.FileToArrayBytes(this.FolderWS+File.separator+"UpLoadTrabajo.txt"));
			SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			new MarshalBase64().register(sse);
			sse.dotNet=true;
			sse.setOutputSoapObject(so);
			HttpTransportSE htse=new HttpTransportSE(URL);		
			htse.call(SOAP_ACTION, sse);
			response=(SoapPrimitive) sse.getResponse();
		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}		
				
		if(response==null) {
			this.Respuesta = -1;
		}else if(response.toString().isEmpty()){
			this.Respuesta = -2;
		}else{
			this.Respuesta = 1;
			this.ArchUpLoadWS.DeleteFile("UpLoadTrabajo.txt");
		}	
		return this.Respuesta;
	}

	@Override
	protected void onPostExecute(Integer rta) {
		if(rta==1){
			Toast.makeText(this.WS_UpLoadContext,"Carga de trabajo finalizada.", Toast.LENGTH_LONG).show();
			String[] _solicitudes_rx = this.response.toString().split("\\|");
			for(int i=0;i<_solicitudes_rx.length;i++){
				this.FcnSolicitudes.BorrarRegistroByIdSolicitud(_solicitudes_rx[i]);
			}
		}else if(rta==-1){
			Toast.makeText(this.WS_UpLoadContext,"Intento fallido, el servidor no ha respondido.", Toast.LENGTH_SHORT).show();
		}else if(rta==-2){
			Toast.makeText(this.WS_UpLoadContext,"No hay nuevas ordenes pendientes para cargar.", Toast.LENGTH_SHORT).show();	
		}else{
			Toast.makeText(this.WS_UpLoadContext,"Error desconocido.", Toast.LENGTH_SHORT).show();
		}
		_pDialog.dismiss();
    }
	
	
	@Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
        _pDialog.setProgress(progreso);
    }
}