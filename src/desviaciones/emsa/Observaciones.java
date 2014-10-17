package desviaciones.emsa;

import clases.ClassObservacion;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Observaciones extends Activity implements OnClickListener, OnItemSelectedListener{
	private Intent new_form;
	private String[] strTipoObservacion = {"...","02-Acometida","03-Cuenta","04-Medidor"};
	
	private ClassObservacion FcnObservacion;
	
	private String	Solicitud;
	private String 	FolderAplicacion;
	private int 	NivelUsuario = 1;	
	
	private ArrayAdapter<String> 	AdaptadorTipoObservacion;	
	
	Button		_btnRegistrar;
	Spinner 	_cmbTipoObservacion;
	EditText	_txtObservacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_observaciones);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.Solicitud			= bundle.getString("Solicitud");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");	
		
		this.FcnObservacion 	= new ClassObservacion(this, this.FolderAplicacion);
		
		_btnRegistrar		= (Button) findViewById(R.id.ObsBtnRegistrar);
		_cmbTipoObservacion = (Spinner) findViewById(R.id.ObsCmbObservacion);
		_txtObservacion		= (EditText) findViewById(R.id.ObsTxtObservacion);
		
		this.AdaptadorTipoObservacion	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.strTipoObservacion); 
		this._cmbTipoObservacion.setAdapter(this.AdaptadorTipoObservacion);
		
		_cmbTipoObservacion.setOnItemSelectedListener(this);
		_btnRegistrar.setOnClickListener(this);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_observaciones, menu);
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
				
			case R.id.Adecuaciones:
				finish();
				this.new_form = new Intent(this, Adecuaciones.class);
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
				
			case R.id.Contador:
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
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()){
			case R.id.ObsCmbObservacion:
				_txtObservacion.setText(this.FcnObservacion.getObservacion(this.Solicitud, _cmbTipoObservacion.getSelectedItem().toString()));
				break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.ObsBtnRegistrar:
				this.FcnObservacion.setObservacion(this.Solicitud, _cmbTipoObservacion.getSelectedItem().toString(), _txtObservacion.getText().toString());
				break;
		}
		
	}
}
