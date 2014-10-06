package clases;

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

	public ClassInSolicitudes(Context _ctx, String _folder){
		this._ctxInSolicitudes	= _ctx;
		this._folderAplicacion	= _folder;
		this.InSolicitudesSQL	= new SQLite(this._ctxInSolicitudes, this._folderAplicacion);
	}
	
	
	public void InsertSolicitudes(ArrayList<String> _informacion){
		for(int i=0;i<_informacion.size();i++){
			this._tempRegistro.clear();
			this.LineasSQL = _informacion.get(i).toString().split("\\|");
			if(this.LineasSQL[0].equals("T")){
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
}
