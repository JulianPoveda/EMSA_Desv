package desviaciones.emsa;

import java.math.BigDecimal;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.text.Editable;
import android.text.TextWatcher;

public class PruebaIntegracion extends Activity implements OnClickListener{
	
	private Intent          new_form;
	private DateTime		ContadorTime;	
	private SQLite 			ContadorSQL;
	private ClassContador	FcnContador;
	
	private ContentValues			_tempRegistro = new ContentValues();	
	
	private int 		NivelUsuario = 1;	
	private String 		FolderAplicacion = "";
	private String 		Solicitud	= "";
	private String      factorMultiplicador;
	
	private EditText 	_txtLecturaIni, _txtLecturaFin, _txtCoeficiente;
	private TextView    _consumoAnterior, _consumoEstimado;
	private Button   	 _btnRegistrarPrueba, _btnEliminarPrueba;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prueba_integracion);
		
		Bundle bundle           = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.Solicitud			= bundle.getString("Solicitud");
		
		this.FcnContador        = new ClassContador(this, this.FolderAplicacion);
		this.ContadorSQL        = new SQLite(this, this.FolderAplicacion);		
		this.ContadorTime       = DateTime.getInstance();
				
		
		this._txtLecturaIni	     = (EditText) findViewById(R.id.IntegracionTxtLecturaIni);
		this._txtLecturaFin	     = (EditText) findViewById(R.id.IntegracionTxtLecturaFin);
		this._txtCoeficiente     = (EditText) findViewById(R.id.IntegracionTxtKd);
		this._consumoAnterior    = (TextView) findViewById(R.id.ConsumoAnterior);
		this._consumoEstimado    = (TextView) findViewById(R.id.ConsumoEstimado);
		this._btnRegistrarPrueba = (Button) findViewById(R.id.IntegracionBtnRegistrar);
		this._btnEliminarPrueba	 = (Button) findViewById(R.id.IntegracionBtnEliminar);
			 			
		this.CargarPruebasContador();
				
		this._btnRegistrarPrueba.setOnClickListener(this);
		this._btnEliminarPrueba.setOnClickListener(this);
		
	}
	
	private void CargarPruebasContador(){
		this._tempRegistro.clear();
		this._tempRegistro = this.ContadorSQL.SelectDataRegistro("dig_pruebas", "lectura_inicial,lectura_final,coeficiente", "solicitud='"+this.Solicitud+"'");
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
			
			this.factorMultiplicador = this.ContadorSQL.StrSelectShieldWhere("in_ordenes_trabajo", "factor_multiplicacion", "solicitud='"+this.Solicitud+"'");
			
			
			
			try{
				double lectura_tomada = 0;
				if(!this._tempRegistro.getAsString("lectura_tomada").isEmpty()){
					lectura_tomada = Double.parseDouble(this._tempRegistro.getAsString("lectura_tomada"));					
					double numDias = this.ContadorSQL.DaysBetweenDateAndNow(this.ContadorTime.reformatDate(this._tempRegistro.getAsString("fecha_tomada"))+" 00:00:00");
					double promedioDiarioEstimado = ((((int)Double.parseDouble(lecturaInicial)-lectura_tomada)*Double.parseDouble(this.factorMultiplicador))/(int)numDias);
					double consumoMensualEstimado = promedioDiarioEstimado*30;
					int consumoP = (int)consumoMensualEstimado;
					this._consumoAnterior.setText(this._tempRegistro.getAsString("consumo")+" Kwh/mes");
					this._consumoEstimado.setText(consumoP+" Kwh/mes");
					this.FcnContador.datosConsumos(this.Solicitud, consumoP, this._tempRegistro.getAsString("consumo"));
				}else{
					this._consumoAnterior.setText(this._tempRegistro.getAsString("consumo"));
					this._consumoEstimado.setText("0");
					this.FcnContador.datosConsumos(this.Solicitud, 0, this._tempRegistro.getAsString("consumo"));
				}
				
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_pruebas, menu);
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
			case R.id.IntegracionBtnRegistrar:
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
				
				break;
		}
		
	}
	
	public static float round(float d, int decimalPlace) {
		   BigDecimal bd = new BigDecimal(Float.toString(d));
		   bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		   return bd.floatValue();
		}  


}
