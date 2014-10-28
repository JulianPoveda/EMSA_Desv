package ws_connect;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import sistema.Utilidades;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import clases.ClassConfiguracion;
import clases.ClassInSolicitudes;


public class JsonConnection extends AsyncTask<String, Integer, InputStream>{
	private ClassInSolicitudes	FcnSolicitudes;
	private ClassConfiguracion	FcnConfiguracion;
	private Utilidades			FcnUtilidades;
	
	ProgressDialog 	_pDialog;
	
	private Context ConnectJSON;
	private String	FolderJSON, _ip_servidor, _puerto, _modulo, _solicitud;
	private ArrayList<NameValuePair> _parametros = new ArrayList<NameValuePair>();
	
	private HttpClient 		ConexionCliente = new DefaultHttpClient();
	private HttpPost 		ConexionPost;
	private HttpResponse 	RespuestaPeticion;
	private InputStream		RespuestaServidor;	

	//Contructor de la clase
	public JsonConnection(Context context, String Directorio){
		this.ConnectJSON 		= context;
		this.FolderJSON 		= Directorio;
		this.FcnUtilidades		= new Utilidades();
		this.FcnSolicitudes		= new ClassInSolicitudes(this.ConnectJSON, this.FolderJSON);
		this.FcnConfiguracion	= new ClassConfiguracion(this.ConnectJSON, this.FolderJSON);
	}

	protected void onPreExecute(){
		/***Codigo para el cargue desde la base de datos de la ip y puerto de conexion para el web service***/
		this._ip_servidor 	= this.FcnConfiguracion.getServidor();
		this._puerto 		= this.FcnConfiguracion.getPuerto();
		this._modulo		= this.FcnConfiguracion.getModulo();
		
		this.ConexionPost = new HttpPost(this._ip_servidor+":"+this._puerto+"/"+this._modulo+"/ServerJSON.php");
		Toast.makeText(this.ConnectJSON,"Iniciando conexion con el servidor, por favor espere...", Toast.LENGTH_SHORT).show();	
		_pDialog = new ProgressDialog(this.ConnectJSON);
		_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		_pDialog.setCancelable(false);
		_pDialog.setProgress(0);
		_pDialog.setMax(100);
		_pDialog.show();
	}
		
	@Override
	protected InputStream doInBackground(String... params) {
		try {
			this._solicitud = params[0];
			this._parametros.add(new BasicNameValuePair("peticion","codigo_apertura"));
			this._parametros.add(new BasicNameValuePair("solicitud",params[0]));
			this.ConexionPost.setEntity(new UrlEncodedFormEntity(this._parametros));
			this.RespuestaPeticion = this.ConexionCliente.execute(this.ConexionPost);
			this.RespuestaServidor = this.RespuestaPeticion.getEntity().getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.RespuestaServidor;
	}
		
	@Override
	protected void onPostExecute(InputStream rta) {
		String datos;
		if(rta != null){
			datos = this.FcnUtilidades.getStringFromInputStream(rta);
			if(this.FcnSolicitudes.VerificarCodigoApertura(this._solicitud, datos)){
				this.FcnSolicitudes.setEstadoSolicitud(this._solicitud, "E");
				Toast.makeText(this.ConnectJSON,"Codigo de apertura recibido correctamente.", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this.ConnectJSON,"Codigo de apertura recibido es erroneo.", Toast.LENGTH_LONG).show();
			}			
		}else{
			Toast.makeText(this.ConnectJSON,"Error al conectarse al servidor.", Toast.LENGTH_LONG).show();
		}
		_pDialog.dismiss();
	}	
		
		
	@Override
	protected void onProgressUpdate(Integer... values) {
		int progreso = values[0].intValue();
		_pDialog.setProgress(progreso);
	}
}
