package clases;

import java.util.ArrayList;

import sistema.SQLite;
import sistema.Utilidades;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class ClassIrregularidades {
	private SQLite		IrregSQL;
	private Utilidades 	IrregUtil;
	
	private ContentValues 				_tempRegistro = new ContentValues();
	private ArrayList<ContentValues>	_tempTabla = new ArrayList<ContentValues>();
	
	private Context _ctxIrreg;
	private String _folderAplicacion;

	
	public ClassIrregularidades(Context _ctx, String _folder){
		this._ctxIrreg		= _ctx;
		this._folderAplicacion	= _folder;
		this.IrregSQL	= new SQLite(this._ctxIrreg, this._folderAplicacion);
		this.IrregUtil	= new Utilidades();
	}
	
	
	public ArrayList<String> getIrregularidades(){
		ArrayList<String> _retorno = new ArrayList<String>();
		this._tempTabla = this.IrregSQL.SelectData("parametros_irregularidades", "descripcion", "id_irregularidad IS NOT NULL");
		this.IrregUtil.ArrayContentValuesToString(_retorno, this._tempTabla, "descripcion");
		return _retorno;
	}
	
	public ArrayList<String> getIrregularidadesRegistradas(String _solicitud){
		ArrayList<String> _retorno = new ArrayList<String>();
		this._tempTabla = this.IrregSQL.SelectData("dig_irregularidades", "irregularidad", "solicitud='"+_solicitud+"'");
		this.IrregUtil.ArrayContentValuesToString(_retorno, this._tempTabla, "irregularidad");
		return _retorno;
	}
	
	public void registrarIrregularidad(String _solicitud, String _irregularidad){
		this._tempRegistro.clear();
		this._tempRegistro.put("solicitud", _solicitud);
		this._tempRegistro.put("irregularidad", _irregularidad);
		if(this.IrregSQL.InsertRegistro("dig_irregularidades", this._tempRegistro)){
			Toast.makeText(this._ctxIrreg, "Irregularidad registrada correctamente.",Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this._ctxIrreg, "Error al registrar la irregularidad.",Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public void eliminarIrregularidad(String _solicitud, String _irregularidad){
		if(this.IrregSQL.DeleteRegistro("dig_irregularidades", "solicitud='"+_solicitud+"' AND irregularidad='"+_irregularidad+"'")){
			Toast.makeText(this._ctxIrreg, "Irregularidad eliminada correctamente.",Toast.LENGTH_SHORT).show();
		}else{
			
		}Toast.makeText(this._ctxIrreg, "Error al eliminar la irregularidad.",Toast.LENGTH_SHORT).show();
	}
}
