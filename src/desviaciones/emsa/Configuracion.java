package desviaciones.emsa;

import clases.ClassConfiguracion;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Configuracion extends Activity implements OnClickListener{
	private ClassConfiguracion FcnCfg;
	
	private int NivelUsuario;
	private String FolderAplicacion;
	
	Spinner 	_impresora;
	EditText 	_servidor, _puerto, _modulo, _web_service, _version, _equipo, _cedula, _nombre;
	Button 		_btnGuardar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion);
		
		_servidor	= (EditText)findViewById(R.id.CfgTxtServidor);
		_puerto		= (EditText)findViewById(R.id.CfgTxtPuerto);
		_modulo 	= (EditText)findViewById(R.id.CfgTxtModulo);
		_web_service= (EditText)findViewById(R.id.CfgTxtWebService);
		_version 	= (EditText)findViewById(R.id.CfgTxtVersion);
		_equipo 	= (EditText)findViewById(R.id.CfgTxtEquipo);
		_cedula 	= (EditText)findViewById(R.id.CfgTxtCedula);
		_nombre		= (EditText)findViewById(R.id.CfgTxtNombre);
		_impresora	= (Spinner) findViewById(R.id.CfgCmbImpresora);
		_btnGuardar = (Button) findViewById(R.id.CfgBtnGuardar);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelLogged");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.FcnCfg 			= new ClassConfiguracion(this,this.FolderAplicacion);
		
		if(this.NivelUsuario == 0){
			_servidor.setEnabled(true);
			_puerto.setEnabled(true);
			_modulo.setEnabled(true);
			_web_service.setEnabled(true);
			_version.setEnabled(false);
			_equipo.setEnabled(true);
			_cedula.setEnabled(false);
			_nombre.setEnabled(false);
			_impresora.setEnabled(true);
		}else{
			_servidor.setEnabled(false);
			_puerto.setEnabled(false);
			_modulo.setEnabled(false);
			_web_service.setEnabled(false);
			_version.setEnabled(false);
			_equipo.setEnabled(false);
			_cedula.setEnabled(false);
			_nombre.setEnabled(false);
			_impresora.setEnabled(false);
		}
		
		_servidor.setText(FcnCfg.getServidor());
		_puerto.setText(FcnCfg.getPuerto());
		_modulo.setText(FcnCfg.getModulo());
		_web_service.setText(FcnCfg.getWebService());
		_version.setText(FcnCfg.getVersion());
		_equipo.setText(FcnCfg.getEquipo());
		_cedula.setText(FcnCfg.getCedula());
		_nombre.setText(FcnCfg.getTecnico());		
		_btnGuardar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.CfgBtnGuardar:
				this.FcnCfg.setConfiguracion(_servidor.getText().toString(),_puerto.getText().toString(),_modulo.getText().toString(),_web_service.getText().toString(),_version.getText().toString(),_equipo.getText().toString(),_cedula.getText().toString(),_nombre.getText().toString());			
				break;
		}		
	}
}
