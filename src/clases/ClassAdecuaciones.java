package clases;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class ClassAdecuaciones {
	private SQLite	DatosActasSQL;
	
	private Context 		_ctxAdecuaciones;
	private String 			_folderAplicacion;
	private ContentValues	_tempRegistro	= new ContentValues();
	
	public ClassAdecuaciones(Context _ctx, String _folder){
		this._ctxAdecuaciones	= _ctx;
		this._folderAplicacion	= _folder;
		this.DatosActasSQL		= new SQLite(this._ctxAdecuaciones, this._folderAplicacion);
	}
	
	
	public void saveAdecuaciones(String _sol, String _susp, String _tubo, String _armario, String _soporte, String _tierra, String _acometida, String _caja, String _medidor, String _otros){
		if(_susp.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo suspension.", Toast.LENGTH_SHORT).show();
		}else if(_tubo.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo tubo.", Toast.LENGTH_SHORT).show();
		}else if(_armario.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo armario.", Toast.LENGTH_SHORT).show();
		}else if(_soporte.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo soporte.", Toast.LENGTH_SHORT).show();
		}else if(_tierra.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo tierra.", Toast.LENGTH_SHORT).show();
		}else if(_acometida.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo acometida.", Toast.LENGTH_SHORT).show();
		}else if(_caja.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo caja.", Toast.LENGTH_SHORT).show();
		}else if(_medidor.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo medidor.", Toast.LENGTH_SHORT).show();
		}else if(_otros.equals("...")){
			Toast.makeText(this._ctxAdecuaciones, "No ha seleccionado el campo otros.", Toast.LENGTH_SHORT).show();
		}else{
			this.saveSuspension(_sol, _susp);
			this.saveTubo(_sol, _tubo);
			this.saveArmario(_sol, _armario);
			this.saveSoporte(_sol, _soporte);
			this.saveTierra(_sol, _tierra);
			this.saveAcometida(_sol, _acometida);
			this.saveCaja(_sol, _caja);
			this.saveMedidor(_sol, _medidor);
			this.saveOtros(_sol, _otros);
			Toast.makeText(this._ctxAdecuaciones, "Adecuaciones guardadas correctamente.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public String getSuspension(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "suspension", "solicitud='"+_solicitud+"'");
	}
	
	public String getTubo(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "tubo", "solicitud='"+_solicitud+"'");
	}
	
	public String getArmario(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "armario", "solicitud='"+_solicitud+"'");
	}
	
	public String getSoporte(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "soporte", "solicitud='"+_solicitud+"'");
	}
	
	public String getTierra(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "tierra", "solicitud='"+_solicitud+"'");
	}
	
	public String getAcometida(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "acometida", "solicitud='"+_solicitud+"'");
	}
	
	public String getCaja(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "caja", "solicitud='"+_solicitud+"'");
	}
	
	public String getMedidor(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "medidor", "solicitud='"+_solicitud+"'");
	}
	
	public String getOtros(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_adecuaciones", "otros", "solicitud='"+_solicitud+"'");
	}
	
		
	
	private void saveSuspension(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("suspension", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveTubo(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("tubo", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveArmario(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("armario", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveSoporte(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("soporte", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveTierra(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("tierra", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveAcometida(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("acometida", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveCaja(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("caja", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveMedidor(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("medidor", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveOtros(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("otros", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_adecuaciones", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
}
