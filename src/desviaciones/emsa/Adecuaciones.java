package desviaciones.emsa;

import clases.ClassAdecuaciones;
import android.app.Activity;
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
import android.widget.Toast;

public class Adecuaciones extends Activity implements OnClickListener{
	private Intent new_form;
	private ClassAdecuaciones FcnAdecuaciones;
	private int 			NivelUsuario = 1;
	private String 			Solicitud;
	private String 			FolderAplicacion = "";
	
	private String[] _strSuspension = {"...","Si","No"};
	private String[] _strTubo		= {"...","Si","No"};
	private String[] _strArmario	= {"...","Si","No"};
	private String[] _strSoporte	= {"...","Si","No"};
	private String[] _strTierra 	= {"...","Si","No"};
	private String[] _strAcometida 	= {"...","Si","No"};
	private String[] _strCaja		= {"...","Si","No"};
	private String[] _strMedidor	= {"...","Si","No"};
	
	private ArrayAdapter<String> AdaptSuspension, AdaptTubo, AdaptArmario, AdaptSoporte, AdaptTierra, AdaptAcometida, AdaptCaja, AdaptMedidor;
	
	Spinner 	_cmbSuspension, _cmbTubo, _cmbArmario, _cmbSoporte, _cmbTierra, _cmbAcometida, _cmbCaja, _cmbMedidor;
	EditText	_txtOtros;
	Button		_btnRegistrar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adecuaciones);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.Solicitud			= bundle.getString("Solicitud");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		this.FcnAdecuaciones = new ClassAdecuaciones(this,this.FolderAplicacion);
		
		_cmbSuspension	= (Spinner) findViewById(R.id.AdecCmbSuspension);
		_cmbTubo		= (Spinner) findViewById(R.id.AdecCmbTubo);
		_cmbArmario		= (Spinner) findViewById(R.id.AdecCmbArmario);
		_cmbSoporte		= (Spinner) findViewById(R.id.AdecCmbSoporte);
		_cmbTierra		= (Spinner) findViewById(R.id.AdecCmbTierra);
		_cmbAcometida	= (Spinner) findViewById(R.id.AdecCmbAcometida);
		_cmbCaja		= (Spinner) findViewById(R.id.AdecCmbCaja);
		_cmbMedidor		= (Spinner) findViewById(R.id.AdecCmbMedidor);	
		_txtOtros		= (EditText) findViewById(R.id.AdecTxtOtros);
		_btnRegistrar	= (Button) findViewById(R.id.AdecBtnRegistrar);
		
		this.AdaptSuspension 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strSuspension);
		_cmbSuspension.setAdapter(this.AdaptSuspension);
		
		this.AdaptTubo 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strTubo);
		_cmbTubo.setAdapter(this.AdaptTubo);
		
		this.AdaptArmario 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strArmario);
		_cmbArmario.setAdapter(this.AdaptArmario);
		
		this.AdaptSoporte 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strSoporte);
		_cmbSoporte.setAdapter(this.AdaptSoporte);
		
		this.AdaptTierra 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strTierra);
		_cmbTierra.setAdapter(this.AdaptTierra);
		
		this.AdaptAcometida 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strAcometida);
		_cmbAcometida.setAdapter(this.AdaptAcometida);

		this.AdaptCaja 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strCaja);
		_cmbCaja.setAdapter(this.AdaptCaja);
		
		this.AdaptMedidor 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strMedidor);
		_cmbMedidor.setAdapter(this.AdaptMedidor);
		
		_btnRegistrar.setOnClickListener(this);
		
		
		_cmbSuspension.setSelection(this.AdaptSuspension.getPosition(this.FcnAdecuaciones.getSuspension(this.Solicitud)));
		_cmbTubo.setSelection(this.AdaptTubo.getPosition(this.FcnAdecuaciones.getTubo(this.Solicitud)));
		_cmbArmario.setSelection(this.AdaptArmario.getPosition(this.FcnAdecuaciones.getArmario(this.Solicitud)));
		_cmbSoporte.setSelection(this.AdaptSoporte.getPosition(this.FcnAdecuaciones.getSoporte(this.Solicitud)));
		_cmbTierra.setSelection(this.AdaptTierra.getPosition(this.FcnAdecuaciones.getTierra(this.Solicitud)));
		_cmbAcometida.setSelection(this.AdaptAcometida.getPosition(this.FcnAdecuaciones.getAcometida(this.Solicitud)));
		_cmbCaja.setSelection(this.AdaptCaja.getPosition(this.FcnAdecuaciones.getCaja(this.Solicitud)));
		_cmbMedidor.setSelection(this.AdaptMedidor.getPosition(this.FcnAdecuaciones.getMedidor(this.Solicitud)));	
		_txtOtros.setText(this.FcnAdecuaciones.getOtros(this.Solicitud));
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_adecuaciones, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {	
			case R.id.Acta:
				finish();
				this.new_form = new Intent(this, Actas.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("NivelUsuario", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;
				
			case R.id.Acometida:
				finish();
				this.new_form = new Intent(this, Acometida.class);
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
				
			case R.id.DatosActas:
				finish();
				this.new_form = new Intent(this, DatosActas.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("NivelUsuario", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;	
				
			case R.id.Irregularidades:
				finish();
				this.new_form = new Intent(this, Irregularidades.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("NivelUsuario", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;		
			
			case R.id.Contador:
				finish();
				this.new_form = new Intent(this, Contador.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("NivelUsuario", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;
			
			case R.id.Observaciones:
				finish();
				this.new_form = new Intent(this, Observaciones.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("NivelUsuario", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;	
				
			case R.id.Sellos:
				finish();
				this.new_form = new Intent(this, Sellos.class);
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
		case R.id.AdecBtnRegistrar:			
			this.FcnAdecuaciones.saveAdecuaciones(	this.Solicitud, 
													_cmbSuspension.getSelectedItem().toString(), 
													_cmbTubo.getSelectedItem().toString(),
													_cmbArmario.getSelectedItem().toString(),
													_cmbSoporte.getSelectedItem().toString(), 
													_cmbTierra.getSelectedItem().toString(), 
													_cmbAcometida.getSelectedItem().toString(), 
													_cmbCaja.getSelectedItem().toString(), 
													_cmbMedidor.getSelectedItem().toString(), 
													_txtOtros.getText().toString());
			
			break;
	}
		
	}
}
