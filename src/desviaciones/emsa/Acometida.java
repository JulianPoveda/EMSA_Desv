package desviaciones.emsa;

import java.util.ArrayList;

import clases.ClassAcometida;

import sistema.SQLite;
import sistema.Utilidades;

import adaptadores.AdaptadorSevenItems;
import adaptadores.DetalleSevenItems;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Acometida extends Activity implements OnClickListener, OnItemSelectedListener, OnItemClickListener{
	private Intent new_form;
	
	private ArrayList<String> 	_strTipoIngreso	= new ArrayList<String>();
	private ArrayList<String> 	_strConductor 	= new ArrayList<String>();
	private ArrayList<String> 	_strTipo		= new ArrayList<String>();
	private ArrayList<String>	_strCalibre		= new ArrayList<String>();
	private ArrayList<String> 	_strClase		= new ArrayList<String>();
	private ArrayList<String> 	_strFases		= new ArrayList<String>();	
	
	private AdaptadorSevenItems			AdaptadorTablaAcometida;
	private ArrayList<DetalleSevenItems>ArrayTablaAcometida= new ArrayList<DetalleSevenItems>();
	
	private String 		_tipo = "";
	private String		_conductor = "";
	
	private ClassAcometida	FcnAcometida;
	private SQLite 			AcometidaSQL;
	private Utilidades		AcometidaUtil;
	private int 			NivelUsuario = 1;
	private String 			Solicitud;
	private String 			FolderAplicacion = "";
	
	private ContentValues				_tempRegistro;
	private ArrayList<ContentValues> 	_tempTabla		= new ArrayList<ContentValues>();
	
	ArrayAdapter<String> AdaptadorCalibreAcometida;	
	ArrayAdapter<String> AdaptadorTipoIngreso;	
	ArrayAdapter<String> AdaptadorConductor;
	ArrayAdapter<String> AdaptadorTipo;
	ArrayAdapter<String> AdaptadorClase;
	ArrayAdapter<String> AdaptadorFases;
	
	
	
	EditText	_txtLongitud;
	Spinner 	_cmbTipoIngreso, _cmbConductor, _cmbTipo, _cmbCalibre, _cmbClase, _cmbFases;
	Button 		_btnRegistrar, _btnEliminar;
	ListView	_lstAcometida;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acometida);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.Solicitud			= bundle.getString("Solicitud");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		this.FcnAcometida 	= new ClassAcometida(this, this.FolderAplicacion);
		this.AcometidaSQL 	= new SQLite(this, this.FolderAplicacion);
		this.AcometidaUtil	= new Utilidades();
		
		_txtLongitud 	= 	(EditText) findViewById(R.id.AcometidaTxtLongitud);
		_cmbTipoIngreso	= 	(Spinner) findViewById(R.id.AcometidaCmbTipoIngreso);
		_cmbConductor	=	(Spinner) findViewById(R.id.AcometidaCmbConductor);
		_cmbTipo		=	(Spinner) findViewById(R.id.AcometidaCmbTipo);
		_cmbCalibre		=	(Spinner) findViewById(R.id.AcometidaCmbCalibre);
		_cmbClase		=	(Spinner) findViewById(R.id.AcometidaCmbClase);
		_cmbFases		=	(Spinner) findViewById(R.id.AcometidaCmbFases);
		_btnRegistrar	= 	(Button) findViewById(R.id.AcometidaBtnRegistrar);
		_btnEliminar	= 	(Button) findViewById(R.id.AcometidaBtnEliminar);
		_lstAcometida	= 	(ListView) findViewById(R.id.AcometidaListAcometida);
		
		
		this._tempTabla = this.AcometidaSQL.SelectData("parametros_acometida", "descripcion_opcion", "combo='tipo_ingreso'");
		this.AcometidaUtil.ArrayContentValuesToString(_strTipoIngreso, this._tempTabla, "descripcion_opcion",false);
		AdaptadorTipoIngreso= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strTipoIngreso);
		_cmbTipoIngreso.setAdapter(AdaptadorTipoIngreso);
		
		this._tempTabla = this.AcometidaSQL.SelectData("parametros_acometida", "descripcion_opcion", "combo='conductor'");
		this.AcometidaUtil.ArrayContentValuesToString(_strConductor, this._tempTabla, "descripcion_opcion",false);
		AdaptadorConductor	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strConductor);
		_cmbConductor.setAdapter(AdaptadorConductor);		
		
		this._tempTabla = this.AcometidaSQL.SelectData("parametros_acometida", "descripcion_opcion", "combo='tipo'");
		this.AcometidaUtil.ArrayContentValuesToString(_strTipo, this._tempTabla, "descripcion_opcion",false);
		AdaptadorTipo	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strTipo);
		_cmbTipo.setAdapter(AdaptadorTipo);	
		
		this._tempTabla = this.AcometidaSQL.SelectData("parametros_acometida", "descripcion_opcion", "combo='clase'");
		this.AcometidaUtil.ArrayContentValuesToString(_strClase, this._tempTabla, "descripcion_opcion",false);
		AdaptadorClase 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strClase);
		_cmbClase.setAdapter(AdaptadorClase);
		
		this._tempTabla = this.AcometidaSQL.SelectData("parametros_acometida", "descripcion_opcion", "combo='fases'");
		this.AcometidaUtil.ArrayContentValuesToString(_strFases, this._tempTabla, "descripcion_opcion",false);
		AdaptadorFases 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strFases);
		_cmbFases.setAdapter(AdaptadorFases);
		
		//Adaptador para el combo del calibre del material segun el tipo  y el conductor
		this._tempTabla= AcometidaSQL.SelectData("parametros_codificacion_cometida", "calibre", "conductor='"+this._conductor+"' AND tipo_acometida='"+this._tipo+"'");
		AcometidaUtil.ArrayContentValuesToString(_strCalibre, this._tempTabla, "calibre",false);		
		AdaptadorCalibreAcometida= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_strCalibre);
		_cmbCalibre.setAdapter(AdaptadorCalibreAcometida);
		
		
		this.AdaptadorTablaAcometida = new AdaptadorSevenItems(this,ArrayTablaAcometida);
		_lstAcometida.setAdapter(this.AdaptadorTablaAcometida);
		
		this.verAcometidaRegistrada();
		
		_btnRegistrar.setOnClickListener(this);
		_btnEliminar.setOnClickListener(this);
		_cmbConductor.setOnItemSelectedListener(this);
		_cmbTipo.setOnItemSelectedListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_acometida, menu);
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
	
	private void MostrarOpcionCalibreAcometida(){
		this._tempTabla = AcometidaSQL.SelectData("parametros_codificacion_cometida", "calibre", "conductor='"+this._conductor+"' AND tipo_acometida='"+this._tipo+"' ORDER BY calibre ASC");
    	AcometidaUtil.ArrayContentValuesToString(_strCalibre, this._tempTabla, "calibre",false);		
    	AdaptadorCalibreAcometida.notifyDataSetChanged();
    }

	private void verAcometidaRegistrada(){
		this.ArrayTablaAcometida.clear();
		this._tempRegistro = FcnAcometida.getAcometidaRegistrada(this.Solicitud);
		ArrayTablaAcometida.add(new DetalleSevenItems(this._tempRegistro.getAsString("tipo_ingreso"),this._tempRegistro.getAsString("conductor"),this._tempRegistro.getAsString("tipo"),this._tempRegistro.getAsString("calibre"),this._tempRegistro.getAsString("clase"),this._tempRegistro.getAsString("fases"),this._tempRegistro.getAsString("longitud")));
		AdaptadorTablaAcometida.notifyDataSetChanged();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()){
			case R.id.AcometidaCmbConductor:
				this._conductor = this.FcnAcometida.getConductor(_cmbConductor.getSelectedItem().toString());
				this.MostrarOpcionCalibreAcometida();
				break;
				
			case R.id.AcometidaCmbTipo:
				this._tipo = this.FcnAcometida.getTipo(_cmbTipo.getSelectedItem().toString());
				this.MostrarOpcionCalibreAcometida();
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
			case R.id.AcometidaBtnRegistrar:
				if(_cmbTipoIngreso.getSelectedItem().toString().equals("...")){
					Toast.makeText(this, "Debe Seleccionar un tipo de Ingreso", Toast.LENGTH_SHORT).show();
				}else if(_cmbConductor.getSelectedItem().toString().equals("...")){
					Toast.makeText(this, "Debe Seleccionar un Conductor", Toast.LENGTH_SHORT).show();
				}else if(_cmbTipo.getSelectedItem().toString().equals("...")){
					Toast.makeText(this, "Debe Seleccionar un Tipo de alamabre", Toast.LENGTH_SHORT).show();
				}else if(_cmbCalibre.getSelectedItem().toString().equals("")){
					Toast.makeText(this, "Debe Seleccionar el Calibre", Toast.LENGTH_SHORT).show();
				}else if(_cmbClase.getSelectedItem().toString().equals("...")){
					Toast.makeText(this, "Debe Seleccionar la Clase", Toast.LENGTH_SHORT).show();
				}else if(_cmbFases.getSelectedItem().toString().equals("...")){
					Toast.makeText(this, "Debe Seleccionar la Fase", Toast.LENGTH_SHORT).show();
				}else if(_txtLongitud.getText().toString().equals("")){
					Toast.makeText(this, "Debe Digitar la Longitud", Toast.LENGTH_SHORT).show();
				}else{
					this.FcnAcometida.saveAcometida(this.Solicitud,
												   _cmbTipoIngreso.getSelectedItem().toString(),
													_cmbConductor.getSelectedItem().toString(),
													_cmbTipo.getSelectedItem().toString(),
													_cmbCalibre.getSelectedItem().toString(),
													_cmbClase.getSelectedItem().toString(),
													_cmbFases.getSelectedItem().toString(),
													_txtLongitud.getText().toString());
					this.verAcometidaRegistrada();
				}
								
				break;
				
			case R.id.AcometidaBtnEliminar:
				this.FcnAcometida.deleteAcometida(this.Solicitud);
				this.verAcometidaRegistrada();
				break;	
		}
		
	}
}
