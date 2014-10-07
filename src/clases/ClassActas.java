package clases;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class ClassActas {
	private SQLite 			ActasSQL;
	private ContentValues	_tempRegistro = new ContentValues();
	private Context	 		_ctxActas;
	private String 			_folderAplicacion;
	
	
	public ClassActas(Context _ctx, String _folder){
		this._ctxActas		= _ctx;
		this._folderAplicacion	= _folder;
		this.ActasSQL	= new SQLite(this._ctxActas, this._folderAplicacion);
	}
	
	
	public void setDatosActas(String _solicitud, String _docEnterado, String _nomEnterado, String _docTestigo, String _nomTestigo, String _tipoEnterado, String _rtaPQR){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("doc_enterado", _docEnterado);
		this._tempRegistro.put("nom_enterado", _nomEnterado);
		this._tempRegistro.put("doc_testigo", _docTestigo);
		this._tempRegistro.put("nom_testigo", _nomTestigo);
		this._tempRegistro.put("tipo_enterado", _tipoEnterado);
		this._tempRegistro.put("respuesta_pqr", _rtaPQR);
		Toast.makeText(this._ctxActas,this.ActasSQL.InsertOrUpdateRegistro("dig_actas", this._tempRegistro, "solicitud='"+_solicitud+"'"),Toast.LENGTH_SHORT).show();	
	}
	
	public String getDocEnterado(String _solicitud){
		return this.ActasSQL.StrSelectShieldWhere("dig_actas", "doc_enterado", "solicitud='"+_solicitud+"'");
	}
	
	public String getNomEnterado(String _solicitud){
		return this.ActasSQL.StrSelectShieldWhere("dig_actas", "nom_enterado", "solicitud='"+_solicitud+"'");
	}
	
	public String getDocTestigo(String _solicitud){
		return this.ActasSQL.StrSelectShieldWhere("dig_actas", "doc_testigo", "solicitud='"+_solicitud+"'");
	}
	
	public String getNomTestigo(String _solicitud){
		return this.ActasSQL.StrSelectShieldWhere("dig_actas", "nom_testigo", "solicitud='"+_solicitud+"'");
	}
	
	public String getTipoEnterado(String _solicitud){
		return this.ActasSQL.StrSelectShieldWhere("dig_actas", "tipo_enterado", "solicitud='"+_solicitud+"'");
	}
	
	public String getRtaPQR(String _solicitud){
		return this.ActasSQL.StrSelectShieldWhere("dig_actas", "respuesta_pqr", "solicitud='"+_solicitud+"'");
	}
	
}
