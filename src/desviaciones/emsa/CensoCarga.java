package desviaciones.emsa;

import java.util.ArrayList;

import clases.ClassCensoCarga;
import clases.ClassInSolicitudes;
import clases.ClassIrregularidades;

import sistema.SQLite;
import sistema.Utilidades;

import adaptadores.AdaptadorFiveItems;
import adaptadores.DetalleFiveItems;
import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CensoCarga extends Activity implements OnClickListener, OnItemSelectedListener, OnItemClickListener{
	private Intent new_form;

	private SQLite 						CensoSQL;
	private Utilidades					CensoUtil;
	private ClassInSolicitudes			FcnSolicitudes;
	private ClassCensoCarga				FcnCensoCarga;
	private ClassIrregularidades		FcnIrregularidades;
	
	private ContentValues				_tempRegistro;
	private ArrayList<ContentValues> 	_tempTabla;
	
	private int		ElementoSelect;
	private String 	CargaSelect, TipoSelect;
	
	private ArrayList<String> strVatios;
	private ArrayList<String> strCantidad;
	private String[] strCarga 		= {"...","Registrada","Directa"};
	private String[] strTipoServicio= {"...","Residencial","No Residencial"};
	
	private ArrayList<String> 		strElementos= new ArrayList<String>();
	
	private ArrayAdapter<String> 	AdaptadorVatios;
	private ArrayAdapter<String> 	AdaptadorCantidad;
	private ArrayAdapter<String> 	AdaptadorElementos;
	private ArrayAdapter<String>	AdaptadorCarga;
	private ArrayAdapter<String> 	AdaptadorTipoServicio;
	private AdaptadorFiveItems		AdaptadorTablaElementos;
	private ArrayList<DetalleFiveItems> ArrayTablaElementos= new ArrayList<DetalleFiveItems>();
	
	private int 						NivelUsuario = 1;
	private String 						Solicitud;
	private String 						FolderAplicacion = "";
	
	TextView	_lblCensoCarga, _lblCargaRegistrada, _lblCargaDirecta, _lblCargaComercial, _lblCargaResidencial;
	Spinner 	_cmbElementos, _cmbCarga, _cmbTipo, _cmbVatios, _cmbCantidad;
	Button 		_btnRegistrar, _btnEliminar;
	ListView	_lstElementos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_censo_carga);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.Solicitud			= bundle.getString("Solicitud");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		this.FcnIrregularidades	= new ClassIrregularidades(this, this.FolderAplicacion);
		this.FcnCensoCarga		= new ClassCensoCarga(this, this.FolderAplicacion);
		this.FcnSolicitudes		= new ClassInSolicitudes(this, this.FolderAplicacion);
		this.CensoSQL 			= new SQLite(this, this.FolderAplicacion);
		this.CensoUtil			= new Utilidades();
		
		_lblCensoCarga		= (TextView) findViewById(R.id.CensoLblTotalCensoCarga);
		_lblCargaRegistrada	= (TextView) findViewById(R.id.CensoLblTotalCargaRegistrada);
		_lblCargaDirecta	= (TextView) findViewById(R.id.CensoLblTotalCargaDirecta);
		_lblCargaComercial	= (TextView) findViewById(R.id.CensoLblTotalCargaComercial);
		_lblCargaResidencial= (TextView) findViewById(R.id.CensoLblTotalCargaResidencial);
		
		_cmbVatios		= (Spinner) findViewById(R.id.CensoCmbVatios);
		_cmbCantidad	= (Spinner) findViewById(R.id.CensoCmbCantidad);
		_cmbElementos 	= (Spinner) findViewById(R.id.CensoCmbElemento);
		_cmbCarga 		= (Spinner) findViewById(R.id.CensoCmbCarga);
		_cmbTipo 		= (Spinner) findViewById(R.id.CensoCmbTipo);
		
		_lstElementos	= (ListView) findViewById(R.id.CensoLstElementos);
		
		_btnEliminar	= (Button) findViewById(R.id.CensoBtnEliminar);
		_btnRegistrar	= (Button) findViewById(R.id.CensoBtnRegistrar);
		
		this.strCantidad= this.CensoUtil.getRangeAdapter(1, 100, 1);
		this.AdaptadorCantidad		= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strCantidad); 
		this._cmbCantidad.setAdapter(this.AdaptadorCantidad);
		
		this._tempTabla = this.CensoSQL.SelectData("parametros_elementos_censo", "descripcion", "codigo IS NOT NULL ORDER BY descripcion");
		this.CensoUtil.ArrayContentValuesToString(strElementos, this._tempTabla, "descripcion",false);
		this.AdaptadorElementos 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strElementos);
		this._cmbElementos.setAdapter(this.AdaptadorElementos);
		
		this.AdaptadorCarga 		= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strCarga);
		this._cmbCarga.setAdapter(this.AdaptadorCarga);
		
		this.AdaptadorTipoServicio	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strTipoServicio);
		this._cmbTipo.setAdapter(this.AdaptadorTipoServicio);
		
		this.AdaptadorTablaElementos = new AdaptadorFiveItems(this,ArrayTablaElementos);
		_lstElementos.setAdapter(this.AdaptadorTablaElementos);
		
		_btnRegistrar.setOnClickListener(this);
		_btnEliminar.setOnClickListener(this);
		_cmbElementos.setOnItemSelectedListener(this);
		_lstElementos.setOnItemClickListener(this);
		
		this.CargarElementosRegistrados();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_censo_carga, menu);
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
				
			case R.id.Contador:
				finish();
				this.new_form = new Intent(this, Contador.class);
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
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()){
			case R.id.CensoCmbElemento:
				this._tempRegistro = this.CensoSQL.SelectDataRegistro("parametros_elementos_censo", "minimo,maximo", "descripcion='"+_cmbElementos.getSelectedItem().toString()+"'");
				this.strVatios	= this.CensoUtil.getRangeAdapter(this._tempRegistro.getAsInteger("minimo"), this._tempRegistro.getAsInteger("maximo"), 10);
				this.AdaptadorVatios= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strVatios); 
				this._cmbVatios.setAdapter(this.AdaptadorVatios);
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
			case R.id.CensoBtnRegistrar:
				if(this._cmbElementos.getSelectedItem().toString().equals("...")){
					Toast.makeText(this, "No ha seleccionado un elemento valido.",Toast.LENGTH_SHORT).show();	
				}else if(this._cmbCarga.getSelectedItem().toString().equals("...")){
					Toast.makeText(this, "No ha seleccionado un tipo de carga valida.",Toast.LENGTH_SHORT).show();	
				}else if(this._cmbTipo.getSelectedItem().toString().equals("...")){
					Toast.makeText(this, "No ha seleccionado un tipo de servicio valido.",Toast.LENGTH_SHORT).show();	
				}else{
					this._tempRegistro.clear();
					this._tempRegistro.put("solicitud", this.Solicitud);
					this._tempRegistro.put("id_elemento", this.CensoSQL.IntSelectShieldWhere("parametros_elementos_censo", "codigo", "descripcion='"+this._cmbElementos.getSelectedItem().toString()+"'"));
					this._tempRegistro.put("cantidad", this._cmbCantidad.getSelectedItem().toString());
					this._tempRegistro.put("vatios", this._cmbVatios.getSelectedItem().toString());
					this._tempRegistro.put("carga", this._cmbCarga.getSelectedItem().toString().substring(0,1));
					this._tempRegistro.put("servicio", this._cmbTipo.getSelectedItem().toString().substring(0,1));				
					this.CensoSQL.InsertRegistro("dig_censo_carga", this._tempRegistro);
					if(this.FcnSolicitudes.getCargaContratada(this.Solicitud)<this.FcnCensoCarga.getTotalCenso(this.Solicitud)){
						this.FcnIrregularidades.registrarIrregularidadById(this.Solicitud, "59");
					}else{
						this.FcnIrregularidades.eliminarIrregularidadById(this.Solicitud, "59");
					}				
					this.CargarElementosRegistrados();
					
				}
				break;
				
			case R.id.CensoBtnEliminar:
				this.CensoSQL.DeleteRegistro("dig_censo_carga", "id_elemento="+this.ElementoSelect+" AND carga='"+this.CargaSelect+"' AND servicio='"+this.TipoSelect+"'");
				if(this.FcnSolicitudes.getCargaContratada(this.Solicitud)<this.FcnCensoCarga.getTotalCenso(this.Solicitud)){
					this.FcnIrregularidades.registrarIrregularidadById(this.Solicitud, "59");
				}else{
					this.FcnIrregularidades.eliminarIrregularidadById(this.Solicitud, "59");
				}
				this.CargarElementosRegistrados();
				this.CalcularSumatorias();
				break;
		}		
	}
	
	
	private void CargarElementosRegistrados(){
		ArrayTablaElementos.clear();
		this._tempTabla = CensoSQL.SelectData("vista_censo_carga", "descripcion,cantidad,vatios,carga,servicio", "solicitud='"+this.Solicitud+"'");
		for(int i=0;i<_tempTabla.size();i++){
			this._tempRegistro = _tempTabla.get(i);
			ArrayTablaElementos.add(new DetalleFiveItems(this._tempRegistro.getAsString("descripcion"),this._tempRegistro.getAsString("cantidad"),this._tempRegistro.getAsString("vatios"),this._tempRegistro.getAsString("carga"),this._tempRegistro.getAsString("servicio")));
		}
		AdaptadorTablaElementos.notifyDataSetChanged();
		this.CalcularSumatorias();
	}
	
	
	private void CalcularSumatorias(){
		_lblCensoCarga.setText("Total Censo Carga: "+ this.FcnCensoCarga.getTotalCenso(this.Solicitud));
		_lblCargaRegistrada.setText("Total Carga Registrada: "+ this.FcnCensoCarga.getTotalCensoRegistrada(this.Solicitud));
		_lblCargaDirecta.setText("Total Carga Directa: "+ this.FcnCensoCarga.getTotalCensoDirecta(this.Solicitud));
		_lblCargaComercial.setText("Total Carga Comercial: "+ this.FcnCensoCarga.getTotalCensoNoResidencial(this.Solicitud));
		_lblCargaResidencial.setText("Total Carga Residencial: "+this.FcnCensoCarga.getTotalCensoResidencial(this.Solicitud));
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.ElementoSelect = this.CensoSQL.IntSelectShieldWhere("vista_censo_carga", "id_elemento", "descripcion='"+this.ArrayTablaElementos.get(position).getItem1()+"'");
		this.CargaSelect	= this.ArrayTablaElementos.get(position).getItem4(); 
		this.TipoSelect		= this.ArrayTablaElementos.get(position).getItem5();
	}
}
