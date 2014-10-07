package clases;

import java.text.DecimalFormat;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class ClassConfiguracion {
	private SQLite 			CfgSQL;
	private ContentValues	_tempRegistro = new ContentValues();
	private Context	 		_ctxCfg;
	private String 			_folderAplicacion;

	private DecimalFormat formatoIdEquipo = new DecimalFormat("000"); 
		
	public ClassConfiguracion(Context _ctx, String _folder){
		this._ctxCfg		= _ctx;
		this._folderAplicacion	= _folder;
		this.CfgSQL	= new SQLite(this._ctxCfg, this._folderAplicacion);
	}
	
	public String getServidor(){
		return this.CfgSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='servidor'");
	}
	
	public String getPuerto(){
		return this.CfgSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='puerto'");
	}
	
	public String getModulo(){
		return this.CfgSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='modulo'");
	}
	
	public String getWebService(){
		return this.CfgSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='web_service'");
	}
	
	public String getVersion(){
		return this.CfgSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='version'");
	}
	
	public String getEquipo(){
		return formatoIdEquipo.format(this.CfgSQL.DoubleSelectShieldWhere("amd_configuracion", "valor", "item='equipo'"));
	}
	
	public String getCedula(){
		return this.CfgSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='cedula_tecnico'");
	}
	
	public String getTecnico(){
		return this.CfgSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='nombre_tecnico'");
	}
	
	public String getImpresora(){
		return this.CfgSQL.StrSelectShieldWhere("amd_configuracion", "valor", "item='impresora'");
	}
	
	private void setServidor(String _ip){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _ip);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='servidor'");
	}
	
	private void setPuerto(String _puerto){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _puerto);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='puerto'");
	}
	
	private void setModulo(String _modulo){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _modulo);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='modulo'");
	}
	
	private void setWebService(String _web_service){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _web_service);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='web_service'");
	}
	
	private void setVersion(String _version){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _version);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='version'");
	}
	
	private void setEquipo(String _equipo){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _equipo);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='equipo'");
	}
	
	public void setCedula(String _cedula){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _cedula);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='cedula_tecnico'");
	}
	
	public void setTecnico(String _nombre){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _nombre);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='nombre_tecnico'");
	}
	
	public void setImpresora(String _impresora){
		this._tempRegistro.clear();
		this._tempRegistro.put("valor", _impresora);
		this.CfgSQL.UpdateRegistro("amd_configuracion", this._tempRegistro, "item='impresora'");
	}	
	
	
	public boolean setConfiguracion(String _servidor, String _puerto, String _modulo, String _web_service, String _version, String _equipo, String _cedula, String _nombre){
		this.setServidor(_servidor);
		this.setPuerto(_puerto);
		this.setModulo(_modulo);
		this.setWebService(_web_service);
		this.setVersion(_version);
		this.setEquipo(_equipo);
		this.setCedula(_cedula);
		this.setTecnico(_nombre);
		Toast.makeText(this._ctxCfg, "Configuracion guardada correctamente.", Toast.LENGTH_SHORT).show();
		return true;
	}
}
