package clases;

import java.util.ArrayList;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;

public class ClassParametros {
	private SQLite 				ParametrosSQL;
	private ClassConfiguracion 	ParametrosCfg;
	
	//private ArrayList<ContentValues>_tempTabla = new ArrayList<ContentValues>();
	private ContentValues			_tempRegistro = new ContentValues();
	private Context 				_ctxParametros;
	private String 					_folderAplicacion;	
	private String 					LineasSQL[];

	
	public ClassParametros(Context _ctx, String _folder){
		this._ctxParametros	= _ctx;
		this._folderAplicacion	= _folder;
		this.ParametrosSQL	= new SQLite(this._ctxParametros, this._folderAplicacion);
		this.ParametrosCfg	= new ClassConfiguracion(this._ctxParametros, this._folderAplicacion);
	}
	
	
	public void InsertParametros(ArrayList<String> _informacion){
		this.ParametrosSQL.DeleteRegistro("amd_usuarios", "perfil>0");
		this.ParametrosSQL.DeleteRegistro("parametros_acometida", "combo IS NOT NULL");
		this.ParametrosSQL.DeleteRegistro("parametros_actas", "combo IS NOT NULL");
		this.ParametrosSQL.DeleteRegistro("parametros_sellos", "combo IS NOT NULL");
		this.ParametrosSQL.DeleteRegistro("parametros_municipios", "id_municipio IS NOT NULL");
		this.ParametrosSQL.DeleteRegistro("parametros_irregularidades", "id_irregularidad IS NOT NULL");
		this.ParametrosSQL.DeleteRegistro("parametros_medidores", "marca IS NOT NULL");
		
		for(int i=0;i<_informacion.size();i++){
			this._tempRegistro.clear();
			this.LineasSQL = _informacion.get(i).toString().split("\\|");
			if(this.LineasSQL[0].equals("Tecnico")){
				this.ParametrosCfg.setCedula(this.LineasSQL[1]);
				this.ParametrosCfg.setTecnico(this.LineasSQL[2]);
				
				this._tempRegistro.put("documento", this.LineasSQL[1]);
				this._tempRegistro.put("nombre", this.LineasSQL[2]);
				this._tempRegistro.put("login", this.LineasSQL[3]);
				this._tempRegistro.put("password", this.LineasSQL[4]);
				this._tempRegistro.put("perfil", 1);
				this.ParametrosSQL.InsertRegistro("amd_usuarios", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("Acometida")){
				this._tempRegistro.put("combo",this.LineasSQL[1]);
				this._tempRegistro.put("id_opcion",this.LineasSQL[2]);
				this._tempRegistro.put("descripcion_opcion",this.LineasSQL[3]);
				this.ParametrosSQL.InsertRegistro("parametros_acometida", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("Actas")){
				this._tempRegistro.put("combo",this.LineasSQL[1]);
				this._tempRegistro.put("id_opcion",this.LineasSQL[2]);
				this._tempRegistro.put("descripcion_opcion",this.LineasSQL[3]);
				this.ParametrosSQL.InsertRegistro("parametros_actas", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("Sellos")){
				this._tempRegistro.put("combo",this.LineasSQL[1]);
				this._tempRegistro.put("id_opcion",this.LineasSQL[2]);
				this._tempRegistro.put("descripcion_opcion",this.LineasSQL[3]);
				this.ParametrosSQL.InsertRegistro("parametros_sellos", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("Municipios")){
				this._tempRegistro.put("id_municipio",this.LineasSQL[1]);
				this._tempRegistro.put("municipio",this.LineasSQL[2]);
				this.ParametrosSQL.InsertRegistro("parametros_municipios", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("Irregularidades")){
				this._tempRegistro.put("id_irregularidad",this.LineasSQL[1]);
				this._tempRegistro.put("descripcion",this.LineasSQL[2]);
				this._tempRegistro.put("tipo",this.LineasSQL[3]);
				this.ParametrosSQL.InsertRegistro("parametros_irregularidades", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("Medidores")){
				this._tempRegistro.put("marca",this.LineasSQL[1]);
				this._tempRegistro.put("nombre",this.LineasSQL[2]);
				this.ParametrosSQL.InsertRegistro("parametros_medidores", this._tempRegistro);
			}else if(this.LineasSQL[0].equals("Censo")){
				this._tempRegistro.put("codigo",this.LineasSQL[1]);
				this._tempRegistro.put("descripcion",this.LineasSQL[2]);
				this._tempRegistro.put("minimo",this.LineasSQL[3]);
				this._tempRegistro.put("maximo",this.LineasSQL[4]);
				this.ParametrosSQL.InsertRegistro("parametros_elementos_censo", this._tempRegistro);
			}			
		}
	}
}
