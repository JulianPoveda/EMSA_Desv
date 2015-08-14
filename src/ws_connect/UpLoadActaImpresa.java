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
//import clases.ClassInSolicitudes;

import sistema.Archivos;
import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;


public class  UpLoadActaImpresa extends AsyncTask<Void, Void, Void>{
	private Context 			WS_UpLoadContext;
	private ClassConfiguracion 	FcnCfg;
	private SQLite				UpLoadSQL;
	private Archivos 			ArchUpLoadWS;
	private String 				FolderWS;
	
	private ContentValues			_tempRegistro 	= new ContentValues();
	private ArrayList<ContentValues>_tempTabla 		= new ArrayList<ContentValues>();
	
	private String 	_ip_servidor	= "";
	private String  _puerto			= "";
	private String  _modulo 		= "";
	private String 	_web_service 	= "";
	
	private String URL; 			
	private String NAMESPACE; 	
	private static final String METHOD_NAME	= "UpLoadActaImpresa";	
	private static final String SOAP_ACTION	= "UpLoadActaImpresa";
	SoapPrimitive response = null;	
	//ProgressDialog 	_pDialog;

	
	public  UpLoadActaImpresa(Context context, String Directorio){
		this.WS_UpLoadContext 	= context;
		this.FolderWS 			= Directorio;
		
		this.FcnCfg = new ClassConfiguracion(this.WS_UpLoadContext, this.FolderWS);		
		ArchUpLoadWS= new Archivos(this.WS_UpLoadContext, this.FolderWS, 10);
		UpLoadSQL 	= new SQLite(this.WS_UpLoadContext, this.FolderWS);
		
		this._ip_servidor 	= this.FcnCfg.getServidor();
		this._puerto 		= this.FcnCfg.getPuerto(); 
		this._modulo		= this.FcnCfg.getModulo();
		this._web_service	= this.FcnCfg.getWebService();	
		
		this.URL 			= _ip_servidor+":"+_puerto+"/"+_modulo+"/"+_web_service;
		this.NAMESPACE 		= _ip_servidor+":"+_puerto+"/"+_modulo;
	}

	
   	@Override
	protected void onPreExecute(){
	}
	
   	
	@Override
	protected Void doInBackground(Void... params) {
		//publishProgress(valor_Tipo_duranteBackground); //Opcional. Para pasar el valor y llamar a onProgressUpdate()
		//cancel(true); //Opcional. Para cancelar en vez de ejecutar al terminar onPostExecute(), ejecutar onCancelled(). Comprobar si se ha cancelado con isCancelled()
		
		String usuario = this.UpLoadSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='nombre_tecnico'");
		try {
			this._tempTabla = this.UpLoadSQL.SelectData("dig_impresiones_inf", "solicitud, id_impresion, nombre_archivo, fecha_imp", "solicitud IS NOT NULL ORDER BY solicitud, id_impresion");
			for(int i=0; i<this._tempTabla.size();i++){
				SoapObject so	=	new SoapObject(NAMESPACE, METHOD_NAME);
				this._tempRegistro 	= this._tempTabla.get(i);
				so.addProperty("solicitud", this._tempRegistro.getAsString("solicitud"));
				so.addProperty("id_impresion", this._tempRegistro.getAsString("id_impresion"));
				so.addProperty("nombre_archivo", this._tempRegistro.getAsString("nombre_archivo"));
				so.addProperty("archivo", this.ArchUpLoadWS.FileToArrayBytes(this.FolderWS+File.separator+this._tempRegistro.getAsString("solicitud")+File.separator+this._tempRegistro.getAsString("nombre_archivo")));
				so.addProperty("fecha_impresion", this._tempRegistro.getAsString("fecha_imp"));
				so.addProperty("usuario", usuario);
				
				SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
				new MarshalBase64().register(sse);
				sse.dotNet=true;
				sse.setOutputSoapObject(so);
				HttpTransportSE htse=new HttpTransportSE(URL);		
				htse.call(SOAP_ACTION, sse);
				response=(SoapPrimitive) sse.getResponse();
				if(response==null) {					
				}else if(response.toString().isEmpty()){					
				}else{
					this.ArchUpLoadWS.DeleteFile(this.FolderWS+File.separator+this._tempRegistro.getAsString("solicitud")+File.separator+this._tempRegistro.getAsString("nombre_archivo"));
					this.UpLoadSQL.DeleteRegistro("dig_impresiones_inf", "solicitud = '"+this._tempRegistro.getAsString("solicitud")+"' AND id_impresion = "+this._tempRegistro.getAsString("id_impresion"));
				}	
			}			
		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;	
	}

	
	protected void onPostExecute() {
    }
	
	
	protected void onProgressUpdate() {
    }
	
	
	protected void onCancelled(){
	}
	
}