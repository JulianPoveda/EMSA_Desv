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
	
	
	public boolean datosContador(String _solicitud, String _marca, String _tipo, String _serie, String _lectura1, String _lectura2, String _lectura3){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("marca",_marca);
		this._tempRegistro.put("tipo",_tipo);
		this._tempRegistro.put("serie",_serie);
		this._tempRegistro.put("lectura1", _lectura1);
		this._tempRegistro.put("lectura2", _lectura2);
		this._tempRegistro.put("lectura3", _lectura3);
		return this.ContadorSQL.InsertRegistro("dig_contador", this._tempRegistro);
	}
	
	public boolean eliminarDatosContador(String _solicitud){
		return this.ContadorSQL.DeleteRegistro("dig_contador", "solicitud='"+_solicitud+"'");
	}
	
	public boolean datosPrueba(String _solicitud, String _lectIni, String _lectFin, String _coeficiente){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud",_solicitud);
		this._tempRegistro.put("lectura_inicial", _lectIni);
		this._tempRegistro.put("lectura_final", _lectFin);
		this._tempRegistro.put("coeficiente", _coeficiente);
		return this.ContadorSQL.InsertRegistro("dig_pruebas", this._tempRegistro);
	}
	
	public boolean eliminarDatosPruebas(String _solicitud){
		return this.ContadorSQL.DeleteRegistro("dig_pruebas", "solicitud='"+_solicitud+"'");
	}
}
