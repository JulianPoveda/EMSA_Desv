package desviaciones.emsa;


import clases.ClassDatosActas;

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

public class DatosActas extends Activity implements OnClickListener{
	private ClassDatosActas				FcnDatosActas;
	
	private Intent new_form;
	private int 						NivelUsuario = 1;
	private String 						Solicitud;
	private String 						FolderAplicacion = "";
	
	private String[] _strIrregularidades 	= {"...","Si","No"};
	private String[] _strPruebaRozamiento	= {"...","No se hacen pruebas","Si","No"};
	private String[] _strPruebaFrenado	 	= {"...","No se hacen pruebas","Si","No"};
	private String[] _strPruebaVacio		= {"...","No se hacen pruebas","Si","No"};
	private String[] _strFamilias 			= {"...","0","1","2","3","4","5","6","7","8","9","10"};
	private String[] _strFotos 				= {"...","Si","No"};
	private String[] _strElectricista		= {"...","Si","No"};
	private String[] _strClaseMedidor		= {"...","Interno","Sin Medidor","Induccion","Electronico"};
	private String[] _strUbicacionMedidor	= {"...","Sin Medidor","Interno","Externo"};
	private String[] _strAplomado			= {"...","Interno","No Hay Medidor","Si","No"};
	private String[] _strRegistrador		= {"...","Interno","Sin Medidor","Digital","Ciclometrico"};
	
	private ArrayAdapter<String> AdaptIrregularidades, AdaptPruebaRozamiento, AdaptPruebaFrenado, AdaptPruebaVacio, AdaptFamilias, AdaptFotos;
	private ArrayAdapter<String> AdaptElectricista, AdaptClaseMedidor, AdaptUbicacionMedidor, AdaptAplomado, AdaptRegistrador;
	
	Spinner 	_cmbIrregularidades, _cmbPruebaRozamiento, _cmbPruebaFrenado, _cmbPruebaVacio, _cmbFamilias, _cmbFotos, _cmbElectricista;
	Spinner 	_cmbClaseMedidor, _cmbUbicacionMedidor, _cmbAplomado, _cmbRegistrador;
	EditText	_txtTelefono, _txtPorcentajeNoResidencial;
	Button		_btnRegistrar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datos_actas);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.Solicitud			= bundle.getString("Solicitud");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		this.FcnDatosActas 		= new ClassDatosActas(this, this.FolderAplicacion);
		
		_cmbIrregularidades	 	= (Spinner) findViewById(R.id.DatosCmbIrregularidades);
		_cmbPruebaRozamiento	= (Spinner) findViewById(R.id.DatosCmbPruebaRozamiento);
		_cmbPruebaFrenado		= (Spinner) findViewById(R.id.DatosCmbPruebaFrenado);
		_cmbPruebaVacio			= (Spinner) findViewById(R.id.DatosCmbPruebaVacio);
		_cmbFamilias			= (Spinner) findViewById(R.id.DatosCmbFamilias);
		_cmbFotos				= (Spinner) findViewById(R.id.DatosCmbFotos);
		_cmbElectricista		= (Spinner) findViewById(R.id.DatosCmbElectricista);
		_cmbClaseMedidor		= (Spinner) findViewById(R.id.DatosCmbClaseMedidor);
		_cmbUbicacionMedidor	= (Spinner) findViewById(R.id.DatosCmbUbicacionMedidor);
		_cmbAplomado			= (Spinner) findViewById(R.id.DatosCmbMedidorAplomado);
		_cmbRegistrador			= (Spinner) findViewById(R.id.DatosCmbRegistrador);		
		_txtTelefono 				= (EditText) findViewById(R.id.DatosTxtTelefono);
		_txtPorcentajeNoResidencial = (EditText) findViewById(R.id.DatosTxtPorcentajeNoResidencial);		
		_btnRegistrar			= (Button) findViewById(R.id.DatosBtnRegistrar);
					
		this.AdaptIrregularidades 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strIrregularidades);
		_cmbIrregularidades.setAdapter(this.AdaptIrregularidades);
		
		this.AdaptPruebaRozamiento 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strPruebaRozamiento);
		_cmbPruebaRozamiento.setAdapter(this.AdaptPruebaRozamiento);
		
		this.AdaptPruebaFrenado 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strPruebaFrenado);
		_cmbPruebaFrenado.setAdapter(this.AdaptPruebaFrenado);
		
		this.AdaptPruebaVacio 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strPruebaVacio);
		_cmbPruebaVacio.setAdapter(this.AdaptPruebaVacio);
		
		this.AdaptFamilias 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strFamilias);
		_cmbFamilias.setAdapter(this.AdaptFamilias);
		
		this.AdaptFotos 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strFotos);
		_cmbFotos.setAdapter(this.AdaptFotos);
		
		this.AdaptElectricista 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strElectricista);
		_cmbElectricista.setAdapter(this.AdaptElectricista);
		
		this.AdaptClaseMedidor 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strClaseMedidor);
		_cmbClaseMedidor.setAdapter(this.AdaptClaseMedidor);
		
		this.AdaptUbicacionMedidor 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strUbicacionMedidor);
		_cmbUbicacionMedidor.setAdapter(this.AdaptUbicacionMedidor);
		
		this.AdaptAplomado 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strAplomado);
		_cmbAplomado.setAdapter(this.AdaptAplomado);
		
		this.AdaptRegistrador 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strRegistrador);
		_cmbRegistrador.setAdapter(this.AdaptRegistrador);
		
		
		_btnRegistrar.setOnClickListener(this);
		
		_cmbIrregularidades.setSelection(this.AdaptIrregularidades.getPosition(this.FcnDatosActas.getIrregularidades(this.Solicitud)));
		_cmbPruebaRozamiento.setSelection(this.AdaptPruebaRozamiento.getPosition(this.FcnDatosActas.getPruebaRozamiento(this.Solicitud)));
		_cmbPruebaFrenado.setSelection(this.AdaptPruebaFrenado.getPosition(this.FcnDatosActas.getPruebaFrenado(this.Solicitud)));
		_cmbPruebaVacio.setSelection(this.AdaptPruebaVacio.getPosition(this.FcnDatosActas.getPruebaVacio(this.Solicitud)));
		_cmbFamilias.setSelection(this.AdaptFamilias.getPosition(this.FcnDatosActas.getFamilias(this.Solicitud)));
		_cmbFotos.setSelection(this.AdaptFotos.getPosition(this.FcnDatosActas.getFotos(this.Solicitud)));
		_cmbElectricista.setSelection(this.AdaptElectricista.getPosition(this.FcnDatosActas.getElectricista(this.Solicitud)));
		_cmbClaseMedidor.setSelection(this.AdaptClaseMedidor.getPosition(this.FcnDatosActas.getClaseMedidor(this.Solicitud)));
		_cmbUbicacionMedidor.setSelection(this.AdaptUbicacionMedidor.getPosition(this.FcnDatosActas.getUbicacionMedidor(this.Solicitud)));
		_cmbAplomado.setSelection(this.AdaptAplomado.getPosition(this.FcnDatosActas.getAplomado(this.Solicitud)));
		_cmbRegistrador.setSelection(this.AdaptRegistrador.getPosition(this.FcnDatosActas.getRegistrador(this.Solicitud)));
		_txtTelefono.setText(this.FcnDatosActas.getTelefono(this.Solicitud));
		_txtPorcentajeNoResidencial.setText(this.FcnDatosActas.getPorcentajeNoResidencial(this.Solicitud));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_datos_actas, menu);
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
				
			case R.id.Configuracion:
				finish();
				this.new_form = new Intent(this, DatosActas.class);
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
				
			case R.id.Irregularidades:
				finish();
				this.new_form = new Intent(this, Irregularidades.class);
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
				
			default:
				return super.onOptionsItemSelected(item);	
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.DatosBtnRegistrar:
				this.FcnDatosActas.saveDatosActas(	this.Solicitud, 
													_cmbIrregularidades.getSelectedItem().toString(), 
													_cmbPruebaRozamiento.getSelectedItem().toString(),
													_cmbPruebaFrenado.getSelectedItem().toString(),
													_cmbPruebaVacio.getSelectedItem().toString(), 
													_cmbFamilias.getSelectedItem().toString(), 
													_cmbFotos.getSelectedItem().toString(), 
													_cmbElectricista.getSelectedItem().toString(), 
													_cmbClaseMedidor.getSelectedItem().toString(), 
													_cmbUbicacionMedidor.getSelectedItem().toString(), 
													_cmbAplomado.getSelectedItem().toString(), 
													_cmbRegistrador.getSelectedItem().toString(), 
													_txtTelefono.getText().toString(), 
													_txtPorcentajeNoResidencial.getText().toString());
				break;
		}		
	}
}
