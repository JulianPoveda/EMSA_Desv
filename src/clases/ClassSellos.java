package clases;

import java.util.ArrayList;

import sistema.SQLite;
import sistema.Utilidades;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class ClassSellos {
	private 	SQLite 		SellosSQL;
	private 	Utilidades	SellosUtil;
	
	private ContentValues				_tempRegistro 	= new ContentValues();
	private ArrayList<ContentValues> 	_tempTabla		= new ArrayList<ContentValues>();
	
	private Context	_ctxSellos;
	private String 	_folderAplicacion;
	
	
	
	public ClassSellos(Context _ctx, String _folder){
		this._ctxSellos		= _ctx;
		this._folderAplicacion	= _folder;
		this.SellosSQL	= new SQLite(this._ctxSellos, this._folderAplicacion);
		this.SellosUtil	= new Utilidades();
	}
	
	
	public ArrayList<String> getTipoIngreso(){
		ArrayList<String> _retorno = new ArrayList<String>();
		this._tempTabla = this.SellosSQL.SelectData("parametros_sellos", "descripcion_opcion", "combo='tipo_ingreso'");
		this.SellosUtil.ArrayContentValuesToString(_retorno, this._tempTabla,"descripcion_opcion",false);
		return _retorno;
	}
	
	public ArrayList<String> getTipoSello(){
		ArrayList<String> _retorno = new ArrayList<String>();
		this._tempTabla = this.SellosSQL.SelectData("parametros_sellos", "descripcion_opcion", "combo='tipo_sello'");
		this.SellosUtil.ArrayContentValuesToString(_retorno, this._tempTabla,"descripcion_opcion",false);
		return _retorno;
	}
	
	public ArrayList<String> getUbicacion(){
		ArrayList<String> _retorno = new ArrayList<String>();
		this._tempTabla = this.SellosSQL.SelectData("parametros_sellos", "descripcion_opcion", "combo='ubicacion'");
		this.SellosUtil.ArrayContentValuesToString(_retorno, this._tempTabla,"descripcion_opcion",false);
		return _retorno;
	}
	
	public ArrayList<String> getColor(){
		ArrayList<String> _retorno = new ArrayList<String>();
		this._tempTabla = this.SellosSQL.SelectData("parametros_sellos", "descripcion_opcion", "combo='color'");
		this.SellosUtil.ArrayContentValuesToString(_retorno, this._tempTabla,"descripcion_opcion",false);
		return _retorno;
	}
	
	public ArrayList<String> getIrregularidad(){
		ArrayList<String> _retorno = new ArrayList<String>();
		this._tempTabla = this.SellosSQL.SelectData("parametros_sellos", "descripcion_opcion", "combo='irregularidad'");
		this.SellosUtil.ArrayContentValuesToString(_retorno, this._tempTabla,"descripcion_opcion",false);
		return _retorno;
	}
	
	public void registrarSello(String _solicitud, String _tipoIngreso, String _tipoSello, String _ubicacion, String _color, String _serie, String _irregularidad){
		if(_tipoIngreso.equals("...")){
			Toast.makeText(this._ctxSellos, "No ha seleccionado el tipo de ingreso.",Toast.LENGTH_SHORT).show();	
		}else if(_tipoSello.equals("...")){
			Toast.makeText(this._ctxSellos, "No ha seleccionado el tipo de sello.",Toast.LENGTH_SHORT).show();	
		}else if(_ubicacion.equals("...")){
			Toast.makeText(this._ctxSellos, "No ha seleccionadola ubicacion del sello.",Toast.LENGTH_SHORT).show();	
		}else if(_color.equals("...")){
			Toast.makeText(this._ctxSellos, "No ha seleccionado el color del sello.",Toast.LENGTH_SHORT).show();	
		}else if(_serie.isEmpty()){
			Toast.makeText(this._ctxSellos, "No ha ingresado la serie.",Toast.LENGTH_SHORT).show();	
		}else{
			this._tempRegistro.clear();
			this._tempRegistro.put("solicitud", _solicitud);
			this._tempRegistro.put("tipo_ingreso", _tipoIngreso);
			this._tempRegistro.put("tipo_sello", _tipoSello);
			this._tempRegistro.put("ubicacion", _ubicacion);
			this._tempRegistro.put("color", _color);
			this._tempRegistro.put("serie", _serie);
			this._tempRegistro.put("irregularidad", _irregularidad);
			if(this.SellosSQL.InsertRegistro("dig_sellos", this._tempRegistro)){
				Toast.makeText(this._ctxSellos, "Sello registrado correctamente.",Toast.LENGTH_SHORT).show();	
			}else{
				Toast.makeText(this._ctxSellos, "Error al registrar el sello.",Toast.LENGTH_SHORT).show();	
			}
		}
	}
	
	public void eliminarSello(String _solicitud, String _tipoIngreso, String _tipoSello, String _serie){
		if(this.SellosSQL.DeleteRegistro("dig_sellos", "solicitud='"+_solicitud+"' AND tipo_ingreso='"+_tipoIngreso+"' AND tipo_sello='"+_tipoSello+"' AND serie='"+_serie+"'")){
			Toast.makeText(this._ctxSellos, "Sello eliminado correctamente.",Toast.LENGTH_SHORT).show();	
		}else{
			Toast.makeText(this._ctxSellos, "Error al eliminar el sello.",Toast.LENGTH_SHORT).show();
		}
	}
}
