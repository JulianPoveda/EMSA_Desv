package desviaciones.emsa;

import java.util.ArrayList;

import clases.ClassContador;
import dialogos.DialogoConfirmacion;
import dialogos.DialogoInformacion;

import sistema.DateTime;
import sistema.SQLite;
import sistema.Utilidades;

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
import android.widget.Toast;

import android.text.Editable;
import android.text.TextWatcher;

public class Contador extends Activity implements OnClickListener{
	private Intent new_form;
	private Intent DialogConfirmacion;
	private Intent DialogInformacion;
	
	private static int    	CONFIRMACION_MARCA	 = 1;
	private static int    	CONFIRMACION_SERIE	 = 2;
	
	private DateTime		ContadorTime;
	private Utilidades 		ContadorUtil;
	private SQLite 			ContadorSQL;
	private ClassContador	FcnContador;
	
	private ContentValues			_tempRegistro;
	private ArrayList<ContentValues> _tempTabla;
	
	private int 		NivelUsuario = 1;
	private int         intentoMarca = 0;
	private int         intentoSerie = 0;
	private String 		FolderAplicacion = "";
	private String 		Solicitud	= "";
	private ArrayList<String> StringMarcaMedidores = new ArrayList<String>();
	
	private ArrayAdapter<String> AdaptadorMarcaMedidor;
	private ArrayAdapter<String> AdaptadorTipoMedidor;
	
	private Spinner 	_cmbMarcaMedidor, _cmbTipoMedidor;
	private EditText 	_txtSerie, _txtLectura1, _txtLectura2, _txtLectura3;
	private EditText    et;
	private ArrayList<String> array_sort = new ArrayList<String>();
    int textlength = 0;
	private Button   	_btnRegistrarContador, _btnEliminarContador;
	
	private String 	StringTipo[]		= {"MONOFASICO","BIFASICO","TRIFASICO"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contador);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.Solicitud			= bundle.getString("Solicitud");
		
		this.FcnContador 		= new ClassContador(this, this.FolderAplicacion);
		this.ContadorSQL 		= new SQLite(this, this.FolderAplicacion);
		this.ContadorUtil		= new Utilidades();
		this.ContadorTime		= DateTime.getInstance();
		
		DialogConfirmacion 	= new Intent(this,DialogoConfirmacion.class);
		DialogInformacion 	= new Intent(this,DialogoInformacion.class);
		
		this._cmbMarcaMedidor	= (Spinner) findViewById(R.id.ContadorCmbMarca);
		this._cmbTipoMedidor	= (Spinner) findViewById(R.id.ContadorCmbTipo);	
		this._txtSerie			= (EditText) findViewById(R.id.ContadorTxtSerie);
		this._txtLectura1		= (EditText) findViewById(R.id.ContadorTxtLectura1);
		this._txtLectura2		= (EditText) findViewById(R.id.ContadorTxtLectura2);
		this._txtLectura3		= (EditText) findViewById(R.id.ContadorTxtLectura3);
		this.et 				= (EditText) findViewById(R.id.BuscarMarca);
		this._btnRegistrarContador	= (Button) findViewById(R.id.ContadorBtnRegistrar);
		this._btnEliminarContador	= (Button) findViewById(R.id.ContadorBtnEliminar);		
			 
		this._tempTabla = this.ContadorSQL.SelectData("vista_parametros_medidores", "resumen", "marca IS NOT NULL");
		this.ContadorUtil.ArrayContentValuesToString(StringMarcaMedidores, this._tempTabla, "resumen",false);
		this.AdaptadorMarcaMedidor 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,StringMarcaMedidores);
		this._cmbMarcaMedidor.setAdapter(this.AdaptadorMarcaMedidor);
		
		AdaptadorTipoMedidor 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,StringTipo);
		_cmbTipoMedidor.setAdapter(AdaptadorTipoMedidor);
		
		this.CargarContadoresRegistrados();
		//this.CargarPruebasContador();
		
		this._btnRegistrarContador.setOnClickListener(this);
		this._btnEliminarContador.setOnClickListener(this);
				
		 et.addTextChangedListener(new TextWatcher() {
	            public void afterTextChanged(Editable s) {
	                // Abstract Method of TextWatcher Interface.
	            }
	 
	            public void beforeTextChanged(CharSequence s, int start, int count,
	                    int after) {
	                // Abstract Method of TextWatcher Interface.
	            }
	 
	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	                textlength = et.getText().length();
	                array_sort.clear();
	                 
	                for (int i = 0; i < StringMarcaMedidores.size(); i++) {
	                    if (textlength <= StringMarcaMedidores.get(i).length()) {
	                        if (et.getText().toString().equalsIgnoreCase((String) StringMarcaMedidores.get(i).subSequence(0, textlength))) {
	                            array_sort.add(StringMarcaMedidores.get(i));
	                        }
	                    }
	                }
	                 
	                _cmbMarcaMedidor.setAdapter(new ArrayAdapter<String>(Contador.this, android.R.layout.simple_list_item_1, array_sort));
	            }
	        });
	}

	
	private void CargarContadoresRegistrados(){
		this._tempRegistro = ContadorSQL.SelectDataRegistro("dig_contador", "marca,serie,lectura1,lectura2,lectura3,tipo", "solicitud='"+this.Solicitud+"'");
		if(this._tempRegistro.size()>0){
			this._cmbMarcaMedidor.setSelection(this.AdaptadorMarcaMedidor.getPosition(this._tempRegistro.getAsString("marca")));
			this._cmbTipoMedidor.setSelection(this.AdaptadorTipoMedidor.getPosition(this._tempRegistro.getAsString("tipo")));
			this._txtSerie.setText(this._tempRegistro.getAsString("serie"));
			this._txtLectura1.setText(this._tempRegistro.getAsString("lectura1"));
			this._txtLectura2.setText(this._tempRegistro.getAsString("lectura2"));
			this._txtLectura3.setText(this._tempRegistro.getAsString("lectura3"));
			this._cmbMarcaMedidor.setEnabled(false);
			this._cmbTipoMedidor.setEnabled(false);
			this._txtSerie.setEnabled(false);
			this._txtLectura1.setEnabled(false);
			this._txtLectura2.setEnabled(false);
			this._txtLectura3.setEnabled(false);
			this._btnRegistrarContador.setEnabled(false);
			this._btnEliminarContador.setEnabled(true);
		}else{
			this._cmbMarcaMedidor.setSelection(0);
			this._cmbTipoMedidor.setSelection(0);
			this._txtSerie.setText("");
			this._txtLectura1.setText("");
			this._txtLectura2.setText("");
			this._cmbMarcaMedidor.setEnabled(true);
			this._cmbTipoMedidor.setEnabled(true);
			this._txtSerie.setEnabled(true);
			this._txtLectura1.setEnabled(true);
			this._txtLectura2.setEnabled(true);
			this._txtLectura3.setEnabled(true);
			this._btnRegistrarContador.setEnabled(true);
			this._btnEliminarContador.setEnabled(false);
		}
	}
	
	/*private void CargarPruebasContador(){
		this._tempRegistro.clear();
		this._tempRegistro = this.ContadorSQL.SelectDataRegistro("dig_pruebas", 
				"lectura_inicial,lectura_final,coeficiente", 
				"solicitud='"+this.Solicitud+"'");
		if(this._tempRegistro.size()>0){
			String lecturaInicial = this._tempRegistro.getAsString("lectura_inicial");
			this._txtLecturaIni.setText(this._tempRegistro.getAsString("lectura_inicial"));
			this._txtLecturaFin.setText(this._tempRegistro.getAsString("lectura_final"));
			this._txtCoeficiente.setText(this._tempRegistro.getAsString("coeficiente"));
			this._txtLecturaIni.setEnabled(false);
			this._txtLecturaFin.setEnabled(false);
			this._txtCoeficiente.setEnabled(false);
			this._btnRegistrarPrueba.setEnabled(false);
			this._btnEliminarPrueba.setEnabled(true);
			
			this._tempRegistro = this.ContadorSQL.SelectDataRegistro("in_consumos", 
					"fecha_tomada,lectura_tomada,consumo", 
					"solicitud='"+this.Solicitud+"' ORDER BY periodo DESC ");
			
			try{
				double numDias = this.ContadorSQL.DaysBetweenDateAndNow(this.ContadorTime.reformatDate(this._tempRegistro.getAsString("fecha_tomada"))+" 00:00:00");
				double promedioDiarioEstimado = ((Double.parseDouble(lecturaInicial)-Double.parseDouble(this._tempRegistro.getAsString("lectura_tomada")))/(int)numDias);
				int consumoMensualEstimado = (int)promedioDiarioEstimado*30;			
				Toast.makeText(this, "Consumo Anterior: "+this._tempRegistro.getAsString("consumo")+"\n Cosumo Estimado: "+consumoMensualEstimado, Toast.LENGTH_LONG).show();
			}catch(Exception e){
				
			}
		}else{
			this._txtLecturaIni.setText("");
			this._txtLecturaFin.setText("");
			this._txtCoeficiente.setText("");
			this._txtLecturaIni.setEnabled(true);
			this._txtLecturaFin.setEnabled(true);
			this._txtCoeficiente.setEnabled(true);
			this._btnRegistrarPrueba.setEnabled(true);
			this._btnEliminarPrueba.setEnabled(false);
		}
	}*/
	
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
				if(this._txtSerie.getText().toString().isEmpty()){
					Toast.makeText(this, "Debe Registrar la serie del medidor.",Toast.LENGTH_SHORT).show();
				}else if(this._txtLectura1.getText().toString().isEmpty()){
					Toast.makeText(this, "Debe Registrar la lectura del medidor.",Toast.LENGTH_SHORT).show();
				}else{
					String marca = this.ContadorSQL.StrSelectShieldWhere("in_ordenes_trabajo", "marca", "solicitud="+this.Solicitud);
					String serie = this.ContadorSQL.StrSelectShieldWhere("in_ordenes_trabajo", "serie", "solicitud="+this.Solicitud);
					if(!marca.equals(this.ContadorSQL.StrSelectShieldWhere("vista_parametros_medidores", "marca", "resumen='"+_cmbMarcaMedidor.getSelectedItem().toString()+"'")) & intentoMarca==0){
						DialogInformacion.putExtra("informacion", "La Marca del Medidor No Coincide");						
						startActivityForResult(DialogInformacion, CONFIRMACION_MARCA);
					}else{
						if(!this._txtSerie.getText().toString().equals(serie) & intentoSerie == 0){
							DialogInformacion.putExtra("informacion", "Los Datos de la Serie no Coinciden");						
							startActivityForResult(DialogInformacion, CONFIRMACION_SERIE);
						 }else{
							 if(this.FcnContador.datosContador(	this.Solicitud, 
										this.ContadorSQL.StrSelectShieldWhere("vista_parametros_medidores", "marca", "resumen='"+_cmbMarcaMedidor.getSelectedItem().toString()+"'"), 
										this._cmbTipoMedidor.getSelectedItem().toString(), 
										this._txtSerie.getText().toString(), 
										this._txtLectura1.getText().toString(), 
										this._txtLectura2.getText().toString(),
										this._txtLectura3.getText().toString())){
										this.CargarContadoresRegistrados();
										Toast.makeText(this, "Datos Contador Registrados Correctamente", Toast.LENGTH_LONG).show();
									} 
						 }												
					}					
				}
				
				break;
				
			case R.id.ContadorBtnEliminar:
				if(this.FcnContador.eliminarDatosContador(	this.Solicitud)){
					this.intentoMarca = 0;
					this.intentoSerie = 0;
					Toast.makeText(this, "Datos Contador Eliminados Correctamente", Toast.LENGTH_LONG).show();
					this.CargarContadoresRegistrados();
				}
				
				break;
				
			/*case R.id.IntegracionBtnRegistrar:
				if(this.FcnContador.datosPrueba(this.Solicitud,
						this._txtLecturaIni.getText().toString(),
						this._txtLecturaFin.getText().toString(),
						this._txtCoeficiente.getText().toString())){
					this.CargarPruebasContador();
					Toast.makeText(this, "Prueba Registrada Correctamente", Toast.LENGTH_LONG).show();
				}
				break;
				
		  case R.id.IntegracionBtnEliminar:
				if(this.FcnContador.eliminarDatosPruebas(this.Solicitud)){
					this.CargarPruebasContador();
					Toast.makeText(this, "Prueba Eliminada Correctamente", Toast.LENGTH_LONG).show();
				}
				
				break;*/
		}
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	 if(resultCode == RESULT_OK && requestCode == CONFIRMACION_MARCA){
			if(data.getExtras().getBoolean("accion")){				
				this.intentoMarca = 1;
				_cmbMarcaMedidor.setSelection(0);
			}
		}else if(resultCode == RESULT_OK && requestCode == CONFIRMACION_SERIE){
			if(data.getExtras().getBoolean("accion")){				
				this.intentoSerie = 1;
				this._txtSerie.setText("");
			}
		}
	}
}
