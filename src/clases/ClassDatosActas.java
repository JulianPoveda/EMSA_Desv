package clases;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class ClassDatosActas {
	private SQLite	DatosActasSQL;
	
	private Context _ctxDatosActas;
	private String 	_folderAplicacion;
	private ContentValues			_tempRegistro	= new ContentValues();
	
	public ClassDatosActas(Context _ctx, String _folder){
		this._ctxDatosActas		= _ctx;
		this._folderAplicacion	= _folder;
		this.DatosActasSQL	= new SQLite(this._ctxDatosActas, this._folderAplicacion);
	}
	
	
	public void saveDatosActas(String _sol, String _irreg, String _pRozamiento, String _pFrenado, String _pVacio, String _fami, String _fotos, String _elect, String _cMedidor, String _uMedidor, String _aplom, String _reg, String _tel, String _porc){
		if(_irreg.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado el campo irregularidad.", Toast.LENGTH_SHORT).show();
		}else if(_pRozamiento.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado el campo pruebas de rozamiento.", Toast.LENGTH_SHORT).show();
		}else if(_pFrenado.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado el campo pruebas de frenado.", Toast.LENGTH_SHORT).show();
		}else if(_pVacio.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado el campo pruebas de vacio.", Toast.LENGTH_SHORT).show();
		}else if(_fami.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado la cantidad de familias.", Toast.LENGTH_SHORT).show();
		}else if(_fotos.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado fotos.", Toast.LENGTH_SHORT).show();
		}else if(_elect.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado si asistio electricista.", Toast.LENGTH_SHORT).show();
		}else if(_cMedidor.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado la clase del medidor.", Toast.LENGTH_SHORT).show();
		}else if(_uMedidor.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado la ubicacion del medidor.", Toast.LENGTH_SHORT).show();
		}else if(_aplom.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado el campo aplomado.", Toast.LENGTH_SHORT).show();
		}else if(_reg.equals("...")){
			Toast.makeText(this._ctxDatosActas, "No ha seleccionado el campo pruebas registrador.", Toast.LENGTH_SHORT).show();
		}else if(_tel.isEmpty()){
			Toast.makeText(this._ctxDatosActas, "No ha ingresado un numero telefonico.", Toast.LENGTH_SHORT).show();
		}else if(_porc.isEmpty()){
			Toast.makeText(this._ctxDatosActas, "No ha ingresado el porcentaje no residencial.", Toast.LENGTH_SHORT).show();
		}else{
			this.saveIrregularidades(_sol, _irreg);
			this.savePruebaRozamiento(_sol, _pRozamiento);
			this.savePruebaFrenado(_sol, _pFrenado);
			this.savePruebaVacio(_sol, _pVacio);
			this.saveFamilias(_sol, _fami);
			this.saveFotos(_sol, _fotos);
			this.saveElectricista(_sol, _elect);
			this.saveClaseMedidor(_sol, _cMedidor);
			this.saveUbicacionMedidor(_sol, _uMedidor);
			this.saveAplomado(_sol, _aplom);
			this.saveRegistrador(_sol, _reg);
			this.saveTelefono(_sol, _tel);
			this.saveProcentajeNoResidencial(_sol, _porc);
			Toast.makeText(this._ctxDatosActas, "Datos actas guardados correctamente.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public String getIrregularidades(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "irregularidades", "solicitud='"+_solicitud+"'");
	}
	
	public String getPruebaRozamiento(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "prueba_rozamiento", "solicitud='"+_solicitud+"'");
	}
	
	public String getPruebaFrenado(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "prueba_frenado", "solicitud='"+_solicitud+"'");
	}
	
	public String getPruebaVacio(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "prueba_vacio", "solicitud='"+_solicitud+"'");
	}
	
	public String getFamilias(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "familias", "solicitud='"+_solicitud+"'");
	}
	
	public String getFotos(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "fotos", "solicitud='"+_solicitud+"'");
	}
	
	public String getElectricista(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "electricista", "solicitud='"+_solicitud+"'");
	}
	
	public String getClaseMedidor(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "clase_medidor", "solicitud='"+_solicitud+"'");
	}
	
	public String getUbicacionMedidor(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "ubicacion_medidor", "solicitud='"+_solicitud+"'");
	}
	
	public String getAplomado(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "aplomado", "solicitud='"+_solicitud+"'");
	}
	
	public String getRegistrador(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "registrador", "solicitud='"+_solicitud+"'");
	}
	
	public String getTelefono(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "telefono", "solicitud='"+_solicitud+"'");
	}
	
	public String getPorcentajeNoResidencial(String _solicitud){
		return this.DatosActasSQL.StrSelectShieldWhere("dig_datos_actas", "porcentaje_no_res", "solicitud='"+_solicitud+"'");
	}
	
	
	private void saveIrregularidades(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("irregularidades", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void savePruebaRozamiento(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("prueba_rozamiento", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void savePruebaFrenado(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("prueba_frenado", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void savePruebaVacio(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("prueba_vacio", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveFamilias(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("familias", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveFotos(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("fotos", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveElectricista(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("electricista", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveClaseMedidor(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("clase_medidor", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveUbicacionMedidor(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("ubicacion_medidor", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveAplomado(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("aplomado", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveRegistrador(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("registrador", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveTelefono(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("telefono", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
	
	private void saveProcentajeNoResidencial(String _solicitud, String _informacion){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("porcentaje_no_res", _informacion);
		this.DatosActasSQL.InsertOrUpdateRegistro("dig_datos_actas", this._tempRegistro, "solicitud='"+_solicitud+"'");
	}
}
