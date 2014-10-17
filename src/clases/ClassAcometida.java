package clases;

import java.util.ArrayList;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;

public class ClassAcometida {
	private SQLite	AcometidaSQL;
	private Context _ctxAcometida;
	private String	_folderAplicacion;
	
	private ContentValues				_tempRegistro	= new ContentValues();
	private ArrayList<ContentValues> 	_tempTabla		= new ArrayList<ContentValues>();
	

	public ClassAcometida(Context _ctx, String _folder){
		this._ctxAcometida		= _ctx;
		this._folderAplicacion	= _folder;
		this.AcometidaSQL	= new SQLite(this._ctxAcometida, this._folderAplicacion);
	}
	
	public String getConductor(String _conductor){
		return _conductor.substring(0,1);
	}
	
	public String getTipo(String _tipo){
		String _retorno="";
		if(_tipo.equals("Alambre")){
			_retorno = "A";					
		}else if(_tipo.equals("Cable")){
			_retorno = "C";
		}else if(_tipo.equals("Concentrico")){
			_retorno = "N";
		}else{
			_retorno = "";
		}
		return _retorno;
	}
	
	public void saveAcometida(String _solicitud, String _tipoIngreso, String _conductor, String _tipo, String _calibre, String _clase, String _fases, String _longitud){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", 	_solicitud);
		this._tempRegistro.put("tipo_ingreso", 	_tipoIngreso);
		this._tempRegistro.put("conductor",	 	_conductor);
		this._tempRegistro.put("tipo", 			_tipo);
		this._tempRegistro.put("calibre", 		_calibre);
		this._tempRegistro.put("clase", 		_clase);
		this._tempRegistro.put("fases", 		_fases);
		this._tempRegistro.put("longitud", 		_longitud);
		this.AcometidaSQL.InsertOrUpdateRegistro("dig_acometida", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	public void deleteAcometida(String _solicitud){
		this.AcometidaSQL.DeleteRegistro("dig_acometida", "solicitud='"+_solicitud+"'");
	}
	
	public ContentValues getAcometidaRegistrada(String _solicitud){
		this._tempRegistro= this.AcometidaSQL.SelectDataRegistro("dig_acometida", "tipo_ingreso,conductor,tipo,calibre,clase,fases,longitud", "solicitud='"+_solicitud+"'");
		return this._tempRegistro;
	}
}
