package desviaciones.emsa;

import java.io.File;

import ws_connect.DownLoadParametros;
import ws_connect.DownLoadTrabajo;

import clases.ClassConfiguracion;
import clases.ClassUsuario;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Loggin extends Activity implements OnClickListener{
	private Intent k;
	private ClassUsuario		FcnUsuario;
	private ClassConfiguracion 	FcnCfg;

	private String 	FolderAplicacion;
	private int 	NivelUsuario;
	private boolean UsuarioLogged;
	
	TextView 		_lblPDA, _lblVersion;
	EditText 		_txtUsuario, _txtContrasena;
	Button 			_btnLoggin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loggin);
		
		this.FolderAplicacion 	= Environment.getExternalStorageDirectory() + File.separator + "EMSA";
		this.FcnCfg				= new ClassConfiguracion(this ,this.FolderAplicacion);
		this.FcnUsuario 		= new ClassUsuario(this, this.FolderAplicacion);
		this.UsuarioLogged 		= false;
		
		_lblPDA			= (TextView) findViewById(R.id.LogginLblCodigo);
		_lblVersion 	= (TextView) findViewById(R.id.LogginLblVersion);
		_txtUsuario 	= (EditText) findViewById(R.id.LogginTxtUsuario);
		_txtContrasena 	= (EditText) findViewById(R.id.LogginTxtContrasena);		
		_btnLoggin  	= (Button) findViewById(R.id.LogginBtnIniciar);
		
		invalidateOptionsMenu(); 
		
		_lblPDA.setText(this.FcnCfg.getEquipo());
		_lblVersion.setText(this.FcnCfg.getVersion());
		_btnLoggin.setOnClickListener(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_loggin, menu);
		return true;
	}
	
	 @Override
	 public boolean onPrepareOptionsMenu(Menu menu) {
		 if(this.UsuarioLogged){
			 menu.findItem(R.id.OrdenesTrabajo).setEnabled(true);
			 menu.findItem(R.id.ImpresionPrueba).setEnabled(true);
			 menu.findItem(R.id.Cargue).setEnabled(true);
			 menu.findItem(R.id.ParametrosArchivo).setEnabled(true);
			 menu.findItem(R.id.ParametrosRed).setEnabled(true);			 
			 menu.findItem(R.id.TrabajoArchivo).setEnabled(true);
			 menu.findItem(R.id.TrabajoRed).setEnabled(true);
			 menu.findItem(R.id.Descargue).setEnabled(true);
			 menu.findItem(R.id.OrdenesRealizadas).setEnabled(true);
			 menu.findItem(R.id.OrdenesPendientes).setEnabled(true);
			 menu.findItem(R.id.Configuracion).setEnabled(true);
			 _txtUsuario.setEnabled(false);
			 _txtContrasena.setEnabled(false);
			 _btnLoggin.setEnabled(false);
			 this.NivelUsuario = this.FcnUsuario.getNivelUsuario(_txtUsuario.getText().toString());
		 }else{
			 menu.findItem(R.id.OrdenesTrabajo).setEnabled(false);
			 menu.findItem(R.id.ImpresionPrueba).setEnabled(false);
			 menu.findItem(R.id.Cargue).setEnabled(false);
			 menu.findItem(R.id.ParametrosArchivo).setEnabled(false);
			 menu.findItem(R.id.ParametrosRed).setEnabled(false);
			 menu.findItem(R.id.TrabajoArchivo).setEnabled(false);
			 menu.findItem(R.id.TrabajoRed).setEnabled(false);
			 menu.findItem(R.id.Descargue).setEnabled(false);
			 menu.findItem(R.id.OrdenesRealizadas).setEnabled(false);
			 menu.findItem(R.id.OrdenesPendientes).setEnabled(false);
			 menu.findItem(R.id.Configuracion).setEnabled(false);
			 _txtUsuario.setEnabled(true);
			 _txtContrasena.setEnabled(true);
			 _btnLoggin.setEnabled(true);
			 this.NivelUsuario = -1;
		 }    	
		 return true;  
	}

	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {				
			case R.id.Configuracion:
				this.k = new Intent(this, Configuracion.class);
				this.k.putExtra("NivelLogged", this.NivelUsuario);
				this.k.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.k);
				return true;
			
			case R.id.ParametrosRed:	
				new DownLoadParametros(this, this.FolderAplicacion).execute(this.FcnCfg.getEquipo()+"");
				return true;
				
			case R.id.TrabajoRed:
				new DownLoadTrabajo(this, this.FolderAplicacion).execute(this.FcnCfg.getEquipo()+"");
				return true;
					
			case R.id.OrdenesTrabajo:
				this.k = new Intent(this, ListaTrabajo.class);
				this.k.putExtra("NivelLogged", this.NivelUsuario);
				this.k.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.k);
				return true;
									
			default:
				return super.onOptionsItemSelected(item);	
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.LogginBtnIniciar:
				this.UsuarioLogged = this.FcnUsuario.LogginUsuario(_txtUsuario.getText().toString(), _txtContrasena.getText().toString());
				invalidateOptionsMenu(); 
				break;
		}
		
	}
}
