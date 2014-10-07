package clases;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class ClassContador {
	private SQLite 			ContadorSQL;
	private ContentValues	_tempRegistro = new ContentValues();
	private Context	 		_ctxContador;
	private String 			_folderAplicacion;

	public ClassContador(Context _ctx, String _folder){
		this._ctxContador		= _ctx;
		this._folderAplicacion	= _folder;
		this.ContadorSQL	= new SQLite(this._ctxContador, this._folderAplicacion);
	}
	
	
	public void datosContador(String _solicitud, String _marca, String _tipo, String _serie, String _lectura1, String _lectura2){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("marca",_marca);
		this._tempRegistro.put("tipo",_tipo);
		this._tempRegistro.put("serie",_serie);
		this._tempRegistro.put("lectura1",_lectura1);
		this._tempRegistro.put("lectura2",_lectura2);
		Toast.makeText(this._ctxContador, this.ContadorSQL.InsertOrUpdateRegistro("dig_contador", this._tempRegistro, "solicitud='"+_solicitud+"'"), Toast.LENGTH_SHORT).show();
	}
	
	public void eliminarDatosContador(String _solicitud){
		if(this.ContadorSQL.DeleteRegistro("dig_contador", "solicitud='"+_solicitud+"'")){
			Toast.makeText(this._ctxContador, "Registros eliminados correctamente.", Toast.LENGTH_SHORT).show();
		}
	}
}
