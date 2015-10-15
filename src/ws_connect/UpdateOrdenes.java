package ws_connect;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import clases.ClassConfiguracion;
import clases.ClassUsuario;
import clases.Data;

import sistema.DateTime;
import sistema.SQLite;

import adaptadores.DetalleThreeItems;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class UpdateOrdenes extends AsyncTask<String,Void,String> {
	 URL url;
	 HttpURLConnection conn = null;
	 DataOutputStream dos = null;
	 int serverResponseCode = 0;
	 String upLoadServerUri = null;
	 FileInputStream fileInputStream;
	 
	 private SQLite		   FcnSQL;
	 private String 	   FolderAplicacion;
	 private DateTime      FcnTime;
	 private ClassConfiguracion  FcnUsuario;
	 
	 private ContentValues				_tempRegistro 	= new ContentValues();
	 private ArrayList<ContentValues>	_tempTabla 		= new ArrayList<ContentValues>();
	
	 
	 private Context context;
	 
	 public UpdateOrdenes(Context ctx, String folderAplicacion){
		 this.context 		   = ctx;
		 this.FolderAplicacion = folderAplicacion;
		 this.FcnSQL		   = new SQLite(this.context,this.FolderAplicacion);
		 this.FcnTime   	   = DateTime.getInstance(); 
		 this.FcnUsuario       = new ClassConfiguracion(this.context,this.FolderAplicacion);
	 }
	 
	 protected void onPreExecute() {
	        upLoadServerUri = "http://45.33.62.183/DesviacionesLecturas/WS/UploadJSON.php";
	    }
	 

	    @Override
	    protected String doInBackground(String... params) {
	    	
	    	JSONObject json = new JSONObject();
	    	JSONArray ordenes = new JSONArray();	   
	    	
	    	String fecha = "";
	    	String inspector = this.FcnUsuario.getEquipo();
	    	
	    	try{
		    	this._tempTabla = FcnSQL.SelectData("in_ordenes_trabajo", "solicitud,dependencia,estado", "solicitud is NOT NULL");	    	
				for(int i=0;i<this._tempTabla.size();i++){
					_tempRegistro = this._tempTabla.get(i);				 
					 JSONObject orden = new JSONObject();
					   fecha = FcnSQL.StrSelectShieldWhere("dig_actas","fecha_ins","solicitud='"+_tempRegistro.getAsString("solicitud")+"'");
					   if(fecha == null){
						   fecha = this.FcnTime.GetFecha();						  
					   }					   
			    	   orden.put("fecha", fecha);
			    	   orden.put("solicitud",_tempRegistro.getAsString("solicitud"));
			    	   orden.put("dependencia", _tempRegistro.getAsString("dependencia"));
			    	   orden.put("inspector", inspector);
			    	   orden.put("estado", _tempRegistro.getAsString("estado"));
			    	   ordenes.put(orden);
				}
				json.put("Ordenes", ordenes);
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	try {
	    		 url = new URL(upLoadServerUri);
	    		 conn = (HttpURLConnection) url.openConnection();
	             conn.setDoInput(true); 
	             conn.setDoOutput(true);
	             conn.setUseCaches(false); 	             	            
	             conn.setInstanceFollowRedirects(false); 
	             conn.setRequestMethod("POST"); 
	             conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
	             conn.setRequestProperty("charset", "utf-8");	            
	             
	             dos = new DataOutputStream(conn.getOutputStream());
	             	             
                 String data = "cuenta="+json.toString()+"&Peticion=UpdateRendimientos";                                                  
                 dos.writeBytes(data);
                 
                 serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();

                 Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                 if(serverResponseCode == 200){
                     Log.i("uploadJson", "Json Upload Complete");
                 }
                 dos.flush();
                 dos.close();
	            	            	    	
	    	}
	    	catch (MalformedURLException ex) {
	            ex.printStackTrace();
	            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

	        } catch (Exception e) {
	            e.printStackTrace();
	            Log.e("Upload file", "Exception : " + e.getMessage(), e);
	        }finally {
	            conn.disconnect();
	        }
	    	 return serverResponseCode+"";
	    }
}
