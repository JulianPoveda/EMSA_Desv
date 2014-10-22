package clases;

import java.text.DecimalFormat;
import java.util.ArrayList;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;

public class ClassInSolicitudes {
	private SQLite 	InSolicitudesSQL;
	
	private ArrayList<ContentValues>_tempTabla = new ArrayList<ContentValues>();
	private ContentValues			_tempRegistro = new ContentValues();
	private Context 				_ctxInSolicitudes;
	private String 					_folderAplicacion;	
	private String 					LineasSQL[];
	
	private DecimalFormat formatoIdSerial = new DecimalFormat("0000"); 
	
	public ClassInSolicitudes(Context _ctx, String _folder){
		this._ctxInSolicitudes	= _ctx;
		this._folderAplicacion	= _folder;
		this.InSolicitudesSQL	= new SQLite(this._ctxInSolicitudes, this._folderAplicacion);
	}
	
	
	public void InsertSolicitudes(ArrayList<String> _informacion){
		for(int i=0;i<_informacion.size();i++){
			this._tempRegistro.clear();
			this.LineasSQL = _informacion.get(i).toString().split("\\|");
			if(this.LineasSQL[0].equals("D")){
				this.InSolicitudesSQL.DeleteRegistro("in_ordenes_trabajo", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("in_sellos", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("in_consumos", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_acometida", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_contador", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_actas", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_censo_carga", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_sellos", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_irregularidades", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_observaciones", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_datos_actas", "solicitud='"+this.LineasSQL[1]+"'");
				this.InSolicitudesSQL.DeleteRegistro("dig_adecuaciones", "solicitud='"+this.LineasSQL[1]+"'");				
			}else if(this.LineasSQL[0].equals("T")){
				this._tempRegistro.put("id_serial", 		this.LineasSQL[1]);
				this._tempRegistro.put("solicitud", 		this.LineasSQL[2]);
				this._tempRegistro.put("dependencia", 		this.LineasSQL[3]);
				this._tempRegistro.put("pda", 				this.LineasSQL[4]);
				this._tempRegistro.put("cuenta", 			this.LineasSQL[5]);
				this._tempRegistro.put("municipio", 		this.LineasSQL[6]);
				this._tempRegistro.put("suscriptor", 		this.LineasSQL[7]);
				this._tempRegistro.put("direccion", 		this.LineasSQL[8]);
				this._tempRegistro.put("clase_servicio",	this.LineasSQL[9]);
				this._tempRegistro.put("estrato", 			this.LineasSQL[10]);
				this._tempRegistro.put("nodo", 				this.LineasSQL[11]);
				this._tempRegistro.put("marca", 			this.LineasSQL[12]);
				this._tempRegistro.put("serie", 			this.LineasSQL[13]);
				this._tempRegistro.put("carga_contratada", 	this.LineasSQL[14]);
				this.InSolicitudesSQL.InsertRegistro("in_ordenes_trabajo", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("S")){
				this._tempRegistro.put("id_serial",	this.LineasSQL[1]);
				this._tempRegistro.put("solicitud", this.LineasSQL[2]);
				this._tempRegistro.put("marca", 	this.LineasSQL[3]);
				this._tempRegistro.put("serie", 	this.LineasSQL[4]);
				this._tempRegistro.put("numero", 	this.LineasSQL[5]);
				this._tempRegistro.put("tipo", 		this.LineasSQL[6]);
				this._tempRegistro.put("clase", 	this.LineasSQL[7]);
				this.InSolicitudesSQL.InsertRegistro("in_sellos", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("C")){
				this._tempRegistro.put("id_serial", 		this.LineasSQL[1]);
				this._tempRegistro.put("solicitud", 		this.LineasSQL[2]);
				this._tempRegistro.put("cuenta", 			this.LineasSQL[3]);
				this._tempRegistro.put("marca", 			this.LineasSQL[4]);
				this._tempRegistro.put("serie", 			this.LineasSQL[5]);
				this._tempRegistro.put("fecha_tomada", 		this.LineasSQL[6]);
				this._tempRegistro.put("fecha_anterior", 	this.LineasSQL[7]);
				this._tempRegistro.put("lectura_tomada", 	this.LineasSQL[8]);
				this._tempRegistro.put("lectura_anterior",	this.LineasSQL[9]);
				this._tempRegistro.put("consumo", 			this.LineasSQL[10]);
				this.InSolicitudesSQL.InsertRegistro("in_consumos", this._tempRegistro);
			}
		}		
	}
	
	
	public ArrayList<String> getNodosSolicitudes(){
		ArrayList<String> _listaNodos = new ArrayList<String>();
		_listaNodos.add("Todos");
		this._tempTabla = this.InSolicitudesSQL.SelectData("in_ordenes_trabajo","nodo","nodo IS NOT NULL");
		for(int i=0;i<this._tempTabla.size();i++){
			_listaNodos.add(this._tempTabla.get(i).getAsString("nodo"));
		}	
		return _listaNodos;
	}
	
	
	/*public String getNumeroActa(String _solicitud){
		this._tempRegistro = this.InSolicitudesSQL.SelectDataRegistro("in_ordenes_trabajo","id_serial,dependencia,solicitud","solicitud='"+_solicitud+"'");
		return this._tempRegistro.getAsString("dependencia")+this._tempRegistro.getAsString("solicitud")+ formatoIdEquipo.format(this.InSolicitudesSQL.IntSelectShieldWhere("amd_configuracion", "valor", "item='equipo'")) + formatoIdSerial.format(this._tempRegistro.getAsDouble("id_serial"));
	}*/
	
	public String getCuenta(String _solicitud){
		return this.InSolicitudesSQL.StrSelectShieldWhere("in_ordenes_trabajo", "cuenta", "solicitud='"+_solicitud+"'");
	}
	
	
	public String getDependencia(String _solicitud){
		return this.InSolicitudesSQL.StrSelectShieldWhere("in_ordenes_trabajo", "dependencia", "solicitud='"+_solicitud+"'");
	}
	
	
	public String getIdSerial(String _solicitud){
		return formatoIdSerial.format(this.InSolicitudesSQL.DoubleSelectShieldWhere("in_ordenes_trabajo", "id_serial", "solicitud='"+_solicitud+"'"));
	}
	
	public double getCargaContratada(String _solicitud){
		return this.InSolicitudesSQL.DoubleSelectShieldWhere("in_ordenes_trabajo", "carga_contratada", "solicitud='"+_solicitud+"'");
	}
		
	public boolean IniciarSolicitud(String _solicitud){
		boolean _retorno = false;
		if(this.InSolicitudesSQL.ExistRegistros("in_ordenes_trabajo", "estado = 'E' AND solicitud<>'"+_solicitud+"'")){
			_retorno = false;
		}else if(this.InSolicitudesSQL.ExistRegistros("in_ordenes_trabajo", "estado = 'T' AND solicitud = '"+_solicitud+"'")){
			_retorno = false;
		}else{
			_retorno = true;
		}
		return _retorno;
	}
	
	public void setEstadoSolicitud(String _solicitud, String _estado){
		this._tempRegistro.clear();
		this._tempRegistro.put("estado", _estado);
		this.InSolicitudesSQL.UpdateRegistro("in_ordenes_trabajo", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	public String getEstadoSolicitud(String _solicitud){
		return this.InSolicitudesSQL.StrSelectShieldWhere("in_ordenes_trabajo", "estado", "solicitud='"+_solicitud+"'");
	}
}
