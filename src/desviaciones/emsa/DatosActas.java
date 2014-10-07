package desviaciones.emsa;

import java.util.ArrayList;

import sistema.SQLite;
import sistema.Utilidades;

import clases.ClassActas;
import clases.ClassConfiguracion;
import clases.ClassInSolicitudes;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class DatosActas extends Activity implements OnClickListener{
	private Intent new_form;
	private ClassConfiguracion 	FcnConfig;
	private ClassInSolicitudes 	FcnSolicitudes;
	private Utilidades			ActasUtil;
	private SQLite				ActasSQL;
	private ClassActas 			FcnActas;
	
	//private ContentValues				_tempRegistro;
	private ArrayList<ContentValues> 	_tempTabla;
	
	private ArrayList<String> 			strTipoEnterado= new ArrayList<String>();
	private ArrayAdapter<String> 		AdaptadorTipoEnterado;
	private int 						NivelUsuario = 1;
	private String 						Solicitud;
	private String 						FolderAplicacion = "";
	
	EditText	_txtOrden, _txtActa, _txtCuenta, _txtDocEnterado, _txtNomEnterado, _txtDocTestigo, _txtNomTestigo;
	Spinner		_cmbTipoEnterado, _cmbRespuesta;
	Button		_btnRegistrar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actas);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.Solicitud			= bundle.getString("Solicitud");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		this.ActasUtil		= new Utilidades();
		this.FcnConfig		= new ClassConfiguracion(this, this.FolderAplicacion);
		this.FcnSolicitudes = new ClassInSolicitudes(this, this.FolderAplicacion);
		this.FcnActas 		= new ClassActas(this, this.FolderAplicacion);
		this.ActasSQL		= new SQLite(this, this.FolderAplicacion);
		
		_txtOrden 		= (EditText) findViewById(R.id.ActaTxtOrden);
		_txtActa 		= (EditText) findViewById(R.id.ActaTxtActa);
		_txtCuenta 		= (EditText) findViewById(R.id.ActaTxtCuenta);
		_txtDocEnterado = (EditText) findViewById(R.id.ActaTxtDocEnterado);
		_txtNomEnterado = (EditText) findViewById(R.id.ActaTxtNomEnterado);
		_txtDocTestigo 	= (EditText) findViewById(R.id.ActaTxtDocTestigo);
		_txtNomTestigo 	= (EditText) findViewById(R.id.ActaTxtNomTestigo);
		
		_cmbTipoEnterado= (Spinner) findViewById(R.id.ActaCmbTipoEnterado);
		_cmbRespuesta	= (Spinner) findViewById(R.id.ActaCmbRtaPQR);
		
		_btnRegistrar	= (Button) findViewById(R.id.ActaBtnRegistrar);
		
		this._tempTabla = this.ActasSQL.SelectData("parametros_actas", "descripcion_opcion", "combo='tipo_enterado'");
		this.ActasUtil.ArrayContentValuesToString(strTipoEnterado, this._tempTabla, "descripcion_opcion");
		this.AdaptadorTipoEnterado 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strTipoEnterado);
		this._cmbTipoEnterado.setAdapter(this.AdaptadorTipoEnterado);
		
		_txtOrden.setEnabled(false);
		_txtActa.setEnabled(false);
		_txtCuenta.setEnabled(false);
		
		_txtOrden.setText(this.FcnSolicitudes.getDependencia(this.Solicitud)+this.Solicitud);
		_txtActa.setText(this.FcnSolicitudes.getDependencia(this.Solicitud)+this.Solicitud+this.FcnConfig.getEquipo()+this.FcnSolicitudes.getIdSerial(this.Solicitud));
		_txtCuenta.setText(this.FcnSolicitudes.getCuenta(this.Solicitud));
		
		_txtDocEnterado.setText(this.FcnActas.getDocEnterado(this.Solicitud));
		_txtNomEnterado.setText(this.FcnActas.getNomEnterado(this.Solicitud));
		_txtDocTestigo.setText(this.FcnActas.getDocTestigo(this.Solicitud));
		_txtNomTestigo.setText(this.FcnActas.getNomTestigo(this.Solicitud));
		
		_cmbTipoEnterado.setSelection(AdaptadorTipoEnterado.getPosition(this.FcnActas.getTipoEnterado(this.Solicitud)));	
		
		_btnRegistrar.setOnClickListener(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_actas, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {				
			case R.id.Medidor:
				finish();
				this.new_form = new Intent(this, Contador.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("NivelUsuario", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;
			
				
			case R.id.CensoCarga:
				finish();
				this.new_form = new Intent(this, CensoCarga.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("NivelUsuario", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;
				
				
			default:
				return super.onOptionsItemSelected(item);	
		}
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.ActaBtnRegistrar:
				this.FcnActas.setDatosActas(this.Solicitud, 
											_txtDocEnterado.getText().toString(), 
											_txtNomEnterado.getText().toString(), 
											_txtDocTestigo.getText().toString(), 
											_txtNomTestigo.getText().toString(), 
											_cmbTipoEnterado.getSelectedItem().toString(), 
											"nada"/*_cmbRespuesta.getSelectedItem().toString()*/);
				break;
		}	
	}
}
