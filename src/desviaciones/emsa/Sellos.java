package desviaciones.emsa;

import java.util.ArrayList;

import clases.ClassSellos;

import sistema.SQLite;
import sistema.Utilidades;
import adaptadores.AdaptadorFiveItems;
import adaptadores.AdaptadorSixItems;
import adaptadores.DetalleFiveItems;
import adaptadores.DetalleSixItems;
import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Sellos extends Activity implements OnClickListener, OnItemSelectedListener, OnItemClickListener{
	private	SQLite			SellosSQL;
	private ClassSellos		FcnSellos;
	
	private ContentValues				_tempRegistro;
	private ArrayList<ContentValues> 	_tempTabla;
	private String	Solicitud;
	private String 	FolderAplicacion;
	private int 	NivelUsuario = 1;	
	private int 	FilaSeleccionada = -1;
	
	private ArrayList<String> _strTipoIngreso 	= new ArrayList<String>();
	private ArrayList<String> _strTipoSello	 	= new ArrayList<String>();
	private ArrayList<String> _strUbicacion	 	= new ArrayList<String>();
	private ArrayList<String> _strColor	 		= new ArrayList<String>();
	private ArrayList<String> _strIrregularidad	= new ArrayList<String>();
	
	private ArrayAdapter<String> 	AdaptadorTipoIngreso;
	private ArrayAdapter<String> 	AdaptadorTipoSello;
	private ArrayAdapter<String> 	AdaptadorUbicacion;
	private ArrayAdapter<String> 	AdaptadorColor;
	private ArrayAdapter<String> 	AdaptadorIrregularidad;
	
	private AdaptadorSixItems			AdaptadorTablaSellos;
	private ArrayList<DetalleSixItems> 	ArrayTablaSellos= new ArrayList<DetalleSixItems>();
	
	Spinner 	_cmbTipoIngreso, _cmbTipoSello, _cmbUbicacion, _cmbColor, _cmbIrregularidad;
	EditText	_txtSerie;
	ListView	_lstSellos;
	Button		_btnRegistrar, _btnEliminar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sellos);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.Solicitud			= bundle.getString("Solicitud");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		this.SellosSQL	= new SQLite(this, this.FolderAplicacion);
		this.FcnSellos	= new ClassSellos(this, this.FolderAplicacion);
		
		_cmbTipoIngreso 	= (Spinner) findViewById(R.id.SellosCmbTipoIngreso);
		_cmbTipoSello		= (Spinner) findViewById(R.id.SellosCmbTipoSello);
		_cmbUbicacion		= (Spinner) findViewById(R.id.SellosCmbUbicacion);
		_cmbColor			= (Spinner) findViewById(R.id.SellosCmbColor);
		_cmbIrregularidad	= (Spinner) findViewById(R.id.SellosCmbIrregularidad);
		
		_txtSerie			= (EditText)findViewById(R.id.SellosTxtSerie);
		
		_lstSellos			= (ListView) findViewById(R.id.SellosLstSellos);
		
		_btnRegistrar		= (Button) findViewById(R.id.SellosBtnRegistrar);
		_btnEliminar		= (Button) findViewById(R.id.SellosBtnEliminar);
		
		this._strTipoIngreso = this.FcnSellos.getTipoIngreso();
		this.AdaptadorTipoIngreso	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._strTipoIngreso); 
		this._cmbTipoIngreso.setAdapter(this.AdaptadorTipoIngreso);
		
		this._strTipoSello = this.FcnSellos.getTipoSello();
		this.AdaptadorTipoSello	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._strTipoSello); 
		this._cmbTipoSello.setAdapter(this.AdaptadorTipoSello);
		
		this._strUbicacion = this.FcnSellos.getUbicacion();
		this.AdaptadorUbicacion	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._strUbicacion); 
		this._cmbUbicacion.setAdapter(this.AdaptadorUbicacion);
		
		this._strColor = this.FcnSellos.getColor();
		this.AdaptadorColor	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._strColor); 
		this._cmbColor.setAdapter(this.AdaptadorColor);
		
		this._strIrregularidad = this.FcnSellos.getIrregularidad();
		this.AdaptadorIrregularidad	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._strIrregularidad); 
		this._cmbIrregularidad.setAdapter(this.AdaptadorIrregularidad);
		
		this.AdaptadorTablaSellos = new AdaptadorSixItems(this,ArrayTablaSellos);
		_lstSellos.setAdapter(this.AdaptadorTablaSellos);
		
		this.CargarSellosRegistrados();
		
		_lstSellos.setOnItemClickListener(this);
		_btnRegistrar.setOnClickListener(this);
		_btnEliminar.setOnClickListener(this);
	}
	
	
	private void CargarSellosRegistrados(){
		ArrayTablaSellos.clear();
		this._tempTabla = SellosSQL.SelectData("dig_sellos", "tipo_ingreso,tipo_sello,ubicacion,color,serie,irregularidad", "solicitud='"+this.Solicitud+"'");
		for(int i=0;i<_tempTabla.size();i++){
			this._tempRegistro = _tempTabla.get(i);
			ArrayTablaSellos.add(new DetalleSixItems(this._tempRegistro.getAsString("tipo_ingreso"),this._tempRegistro.getAsString("tipo_sello"),this._tempRegistro.getAsString("ubicacion"),this._tempRegistro.getAsString("color"),this._tempRegistro.getAsString("serie"),this._tempRegistro.getAsString("irregularidad")));
		}
		AdaptadorTablaSellos.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.FilaSeleccionada = position;		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.SellosBtnRegistrar:
				this.FcnSellos.registrarSello(	this.Solicitud, 
												_cmbTipoIngreso.getSelectedItem().toString(), 
												_cmbTipoSello.getSelectedItem().toString(), 
												_cmbUbicacion.getSelectedItem().toString(), 
												_cmbColor.getSelectedItem().toString(), 
												_txtSerie.getText().toString(), 
												_cmbIrregularidad.getSelectedItem().toString());
				this.CargarSellosRegistrados();
				break;
				
			case R.id.SellosBtnEliminar:
				this.FcnSellos.eliminarSello(	this.Solicitud, 
												this.ArrayTablaSellos.get(this.FilaSeleccionada).getItem1(),
												this.ArrayTablaSellos.get(this.FilaSeleccionada).getItem2(),
												this.ArrayTablaSellos.get(this.FilaSeleccionada).getItem5());
				this.CargarSellosRegistrados();
				break;
		}
	}
}
