/*****************************************************************************************************
 * Fecha: 	10-06-2014
 * Version:	1.1
 * Modifcaciones:	Se incluye carga de los parametros de conexion directamente desde la base de datos
 * 					Se crea un progress dialog el cual informa el estado actual de la carga de trabajo
 * 					Se incluyen mensajes informativos segun el estado de la finalizacion de la descarga
*****************************************************************************************************/

package ws_connect;

import java.io.IOException;
import java.util.ArrayList;

import sistema.Archivos;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import clases.ClassConfiguracion;
import clases.ClassParametros;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DownLoadParametros extends AsyncTask<String, Integer, Integer>{ //doInBakGround, Progress, onPostExecute
	
	/**Instancias a clases**/
	private Archivos 			FcnArch;
	private ClassConfiguracion 	FcnCfg;
	private ClassParametros		FcnParametros;
	//private SQLite 				DownloadSQL;
	
	/**Variables Locales**/
	private Context 			ConnectServerContext;
	private String 				DirectorioConexionServer;
	private ArrayList<String> 	InformacionDescarga = new ArrayList<String>();	
	//private String LineasSQL[];
	
	/**Variables para el consumo del web service a traves de nusoap**/
	private String 	_ip_servidor	= "";
	private String  _puerto			= "";
	private String  _modulo 		= "";
	private String 	_web_service 	= "";
	
	private String URL;				//= "http://190.93.133.87:8080/ControlEnergia/WS/AndroidWS.php?wsdl";
	private String NAMESPACE;		//= "http://190.93.133.87:8080/ControlEnergia/WS";
	
	//Variables con la informacion del web service
	private static final String METHOD_NAME	= "DownLoadParametros";
	private static final String SOAP_ACTION	= "DownLoadParametros";
	SoapPrimitive 	response = null;
	ProgressDialog 	_pDialog;
	
		
	//Contructor de la clase
	public DownLoadParametros(Context context, String Directorio){
		this.ConnectServerContext 		= context;
		this.DirectorioConexionServer 	= Directorio;		
		this.FcnParametros				= new ClassParametros(this.ConnectServerContext, this.DirectorioConexionServer);
		this.FcnCfg						= new ClassConfiguracion(this.ConnectServerContext, this.DirectorioConexionServer);
		this.FcnArch					= new Archivos(this.ConnectServerContext,this.DirectorioConexionServer,10);
	}


	//Operaciones antes de realizar la conexion con el servidor
	protected void onPreExecute(){
		/***Codigo para el cargue desde la base de datos de la ip y puerto de conexion para el web service***/
		this._ip_servidor 	= this.FcnCfg.getServidor();
		this._puerto 		= this.FcnCfg.getPuerto();
		this._modulo		= this.FcnCfg.getModulo();
		this._web_service	= this.FcnCfg.getWebService();
		this.URL 			= _ip_servidor+":"+_puerto+"/"+_modulo+"/"+_web_service;
		this.NAMESPACE 		= _ip_servidor+":"+_puerto+"/"+_modulo;
		
		
		Toast.makeText(this.ConnectServerContext,"Iniciando conexion con el servidor, por favor espere...", Toast.LENGTH_SHORT).show();	
		_pDialog = new ProgressDialog(this.ConnectServerContext);
        _pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _pDialog.setMessage("Ejecutando operaciones...");
        _pDialog.setCancelable(false);
        _pDialog.setProgress(0);
        _pDialog.setMax(100);
        _pDialog.show();
	}
	
	//Conexion con el servidor en busca de actualizaciones de trabajo programado
	@Override
	protected Integer doInBackground(String... params) {
		int _retorno = 0;
		try{
			SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
			so.addProperty("id_interno", params[0]);	
			SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sse.dotNet=true;
			sse.setOutputSoapObject(so);
			HttpTransportSE htse=new HttpTransportSE(URL);
			htse.call(SOAP_ACTION, sse);
			response=(SoapPrimitive) sse.getResponse();
			
			/**Inicio de tratamiento de la informacion recibida**/
			if(response.toString()==null) {
				_retorno = -1;
			}else if(response.toString().isEmpty()){
				_retorno = -2;
			}else{
				//byte[] resultado = Base64.decode(response.toString());
				String informacion = new String(Base64.decode(response.toString()), "ISO-8859-1");
				try {
					//this.FcnArch.ByteArrayToFile(resultado, "Parametros.txt");
					this.FcnArch.DoFile("", "Parametros.txt", informacion);
					
					this.InformacionDescarga = FcnArch.FileToArrayString("Parametros.txt",false);
					this.FcnParametros.InsertParametros(this.InformacionDescarga);
					this.FcnArch.DeleteFile("Parametros.txt");
					_retorno = 1;
				} catch (Exception e) {
					e.printStackTrace();
					_retorno = -3;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return _retorno;
	}
	
	
	
	//Operaciones despues de finalizar la conexion con el servidor
	@Override
	protected void onPostExecute(Integer rta) {
		if(rta==1){
			Toast.makeText(this.ConnectServerContext,"Carga de parametros finalizada.", Toast.LENGTH_LONG).show();
		}else if(rta==-1){
			Toast.makeText(this.ConnectServerContext,"Intento fallido, el servidor no ha respondido.", Toast.LENGTH_SHORT).show();
		}else if(rta==-2){
			Toast.makeText(this.ConnectServerContext,"No hay nuevas ordenes pendientes para cargar.", Toast.LENGTH_SHORT).show();	
		}else{
			Toast.makeText(this.ConnectServerContext,"Error desconocido.", Toast.LENGTH_SHORT).show();
		}
		_pDialog.dismiss();
	}	
	
	
	@Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();
        _pDialog.setProgress(progreso);
    }
}

