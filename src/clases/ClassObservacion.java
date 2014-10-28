package clases;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class ClassObservacion {
	private SQLite		ObservacionSQL;
	private Context 	_ctxObservacion;
	private String 	 	_folderAplicacion;
	
	private ContentValues				_tempRegistro 	= new ContentValues();;

	public ClassObservacion(Context _ctx, String _folder){
		this._ctxObservacion	= _ctx;
		this._folderAplicacion	= _folder;
		this.ObservacionSQL		= new SQLite(this._ctxObservacion, this._folderAplicacion);
	}
	
	public String getObservacion(String _solicitud, String _tipoObservacion){
		return this.ObservacionSQL.StrSelectShieldWhere("dig_observaciones", "observacion", "solicitud='"+_solicitud+"' AND tipo_observacion='"+_tipoObservacion+"'");	
	}
	
	public void setObservacion(String _solicitud, String _tipoObservacion, String _observacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("tipo_observacion", _tipoObservacion);
		this._tempRegistro.put("observacion", _observacion);		
		this.ObservacionSQL.InsertOrUpdateRegistro("dig_observaciones", this._tempRegistro, "solicitud='"+_solicitud+"' AND tipo_observacion='"+_tipoObservacion+"'");
		Toast.makeText(this._ctxObservacion,"Observacion registrada correctamente.",Toast.LENGTH_SHORT).show();	
	}
}
