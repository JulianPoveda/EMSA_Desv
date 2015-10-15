package clases;

import sistema.SQLite;
import android.content.Context;
import android.widget.Toast;

public class ClassUsuario {
	private SQLite 	UsuarioSQL;
	private Context _ctxUsuario;
	private String 	_folderAplicacion;
	
	
	public ClassUsuario(Context _ctx, String _folder){
		this._ctxUsuario		= _ctx;
		this._folderAplicacion	= _folder;
		this.UsuarioSQL	= new SQLite(this._ctxUsuario, this._folderAplicacion);
	}
	
	
	public boolean LogginUsuario(String _usuario, String _contrasena){
		boolean _retorno; 
		if(this.UsuarioSQL.ExistRegistros("amd_usuarios", "login='"+_usuario+"' AND password='"+_contrasena+"'")){
			Toast.makeText(this._ctxUsuario, "Iniciada la sesion como "+_usuario, Toast.LENGTH_SHORT).show();
			_retorno = true;
		}else{
			Toast.makeText(this._ctxUsuario, "Error al iniciar sesion como "+_usuario, Toast.LENGTH_SHORT).show();
			_retorno = false;
		}
		return _retorno;
	}
	
	
	public int getNivelUsuario(String _usuario){
		return (this.UsuarioSQL.IntSelectShieldWhere("amd_usuarios", "perfil", "login='"+_usuario+"'"));
	}	

}
