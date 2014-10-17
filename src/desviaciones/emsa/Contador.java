package desviaciones.emsa;

import java.util.ArrayList;

import clases.ClassContador;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class Contador extends Activity implements OnClickListener{
	private Intent new_form;
	
	private Utilidades 		ContadorUtil;
	private SQLite 			ContadorSQL;
	private ClassContador	FcnContador;
	
	private ContentValues			_tempRegistro;
	private ArrayList<ContentValues> _tempTabla;

	
	private int 		NivelUsuario = 1;
	private String 		FolderAplicacion = "";
	private String 		Solicitud	= "";
	private ArrayList<String> StringMarcaMedidores = new ArrayList<String>();
	
	private ArrayAdapter<String> AdaptadorMarcaMedidor;
	private ArrayAdapter<String> AdaptadorTipoMedidor;
	
	AdaptadorFiveItems AdaptadorTablaContador;
	ArrayList<DetalleFiveItems> ArrayTablaContador = new ArrayList<DetalleFiveItems>();
	
	private Spinner 	_cmbMarcaMedidor, _cmbTipoMedidor;
	private EditText 	_txtSerie, _txtLectura1, _txtLectura2;
	private Button   	_btnRegistrar, _btnEliminar;
	private ListView	_lstContador;
	
	private String 	StringTipo[]		= {"MONOFASICO","BIFASICO","TRIFASICO"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contador);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.Solicitud			= bundle.getString("Solicitud");
		
		this.FcnContador = new ClassContador(this, this.FolderAplicacion);
		this.ContadorSQL = new SQLite(this, this.FolderAplicacion);
		this.ContadorUtil= new Utilidades();
		
		_cmbMarcaMedidor= (Spinner) findViewById(R.id.ContadorCmbMarca);
		_cmbTipoMedidor	= (Spinner) findViewById(R.id.ContadorCmbTipo);	
		_txtSerie		= (EditText) findViewById(R.id.ContadorTxtSerie);
		_txtLectura1	= (EditText) findViewById(R.id.ContadorTxtLectura1);
		_txtLectura2	= (EditText) findViewById(R.id.ContadorTxtLectura2);
		_btnRegistrar	= (Button) findViewById(R.id.ContadorBtnRegistrar);
		_btnEliminar    = (Button) findViewById(R.id.ContadorBtnEliminar);
		_lstContador 	= (ListView) findViewById(R.id.ContadorLstContador);
		
		this._tempTabla = this.ContadorSQL.SelectData("vista_parametros_medidores", "resumen", "marca IS NOT NULL");
		this.ContadorUtil.ArrayContentValuesToString(StringMarcaMedidores, this._tempTabla, "resumen");
		this.AdaptadorMarcaMedidor 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,StringMarcaMedidores);
		this._cmbMarcaMedidor.setAdapter(this.AdaptadorMarcaMedidor);
		
		AdaptadorTipoMedidor 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,StringTipo);
		_cmbTipoMedidor.setAdapter(AdaptadorTipoMedidor);
		
		AdaptadorTablaContador = new AdaptadorFiveItems(this,ArrayTablaContador);
		_lstContador.setAdapter(AdaptadorTablaContador);
		
		this.CargarContadoresRegistrados();
		
		_btnRegistrar.setOnClickListener(this);
		_btnEliminar.setOnClickListener(this);
	}

	
	private void CargarContadoresRegistrados(){
		ArrayTablaContador.clear();
		this._tempTabla = ContadorSQL.SelectData("dig_contador", "marca,serie,lectura1,lectura2,tipo", "solicitud='"+this.Solicitud+"'");
		for(int i=0;i<_tempTabla.size();i++){
			this._tempRegistro = _tempTabla.get(i);
			ArrayTablaContador.add(new DetalleFiveItems(this._tempRegistro.getAsString("marca"),this._tempRegistro.getAsString("serie"),this._tempRegistro.getAsString("lectura1"),this._tempRegistro.getAsString("lectura2"),this._tempRegistro.getAsString("tipo")));
		}
		AdaptadorTablaContador.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_contador, menu);
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
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.ContadorBtnRegistrar:
				this.FcnContador.datosContador(	this.Solicitud, 
												this.ContadorSQL.StrSelectShieldWhere("vista_parametros_medidores", "marca", "resumen='"+_cmbMarcaMedidor.getSelectedItem().toString()+"'"), 
												_cmbTipoMedidor.getSelectedItem().toString(), 
												_txtSerie.getText().toString(), 
												_txtLectura1.getText().toString(), 
												_txtLectura2.getText().toString());
				this.CargarContadoresRegistrados();
				break;
				
			case R.id.ContadorBtnEliminar:
				this.FcnContador.eliminarDatosContador(	this.Solicitud);
				this.CargarContadoresRegistrados();
				break;
		}
		
	}
}
