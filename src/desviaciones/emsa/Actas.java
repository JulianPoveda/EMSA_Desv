package desviaciones.emsa;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import sistema.FormatosActas;
import sistema.SQLite;
import sistema.Utilidades;
import ws_connect.UpLoadFoto;
import ws_connect.UploaderFoto;

import clases.ClassActas;
import clases.ClassConfiguracion;
import clases.ClassInSolicitudes;
import clases.ManagerImageResize;
import global.global_var;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Actas extends Activity implements OnClickListener, global_var{
	private Intent new_form;
	private static int 	INICIAR_CAMARA	 	     = 2;
	
	private FormatosActas		FormatoImp;
	private ClassConfiguracion 	FcnConfig;
	private ClassInSolicitudes 	FcnSolicitudes;
	private Utilidades			ActasUtil;
	private SQLite				ActasSQL;
	private ClassActas 			FcnActas;
	private ManagerImageResize  FcnImage;
	
	Intent 	    IniciarCamara;
	
	//private ContentValues				_tempRegistro;
	private ArrayList<ContentValues> 	_tempTabla;
	
	private ArrayList<String> 			strTipoEnterado= new ArrayList<String>();
	private ArrayList<String> 			strRespuestaPQR= new ArrayList<String>();
	private ArrayAdapter<String> 		AdaptadorTipoEnterado;
	private ArrayAdapter<String> 		AdaptadorRTAPQR;
	private int 						NivelUsuario = 1;
	private String 						Solicitud;
	private String 						FolderAplicacion = "";
	private String 						fotoParcial		 = "";
	private String						nombreFoto;
	
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
		this.FormatoImp		= new FormatosActas(this, this.FolderAplicacion, true);
		this.FcnConfig		= new ClassConfiguracion(this, this.FolderAplicacion);
		this.FcnSolicitudes = new ClassInSolicitudes(this, this.FolderAplicacion);
		this.FcnActas 		= new ClassActas(this, this.FolderAplicacion);
		this.ActasSQL		= new SQLite(this, this.FolderAplicacion);
		this.FcnImage           = ManagerImageResize.getInstance();
		
		this.IniciarCamara	= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		
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
		this.ActasUtil.ArrayContentValuesToString(strTipoEnterado, this._tempTabla, "descripcion_opcion",false);
		this.AdaptadorTipoEnterado 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strTipoEnterado);
		this._cmbTipoEnterado.setAdapter(this.AdaptadorTipoEnterado);
		
		this._tempTabla = this.ActasSQL.SelectData("parametros_respuesta_pqr", "id_respuesta||'-'||respuesta as respuesta", "dependencia='"+this.FcnSolicitudes.getDependencia(this.Solicitud)+"' AND tipo_accion='"+this.FcnSolicitudes.getTipoAccion(this.Solicitud)+"'");
		this.ActasUtil.ArrayContentValuesToString(strRespuestaPQR, this._tempTabla, "respuesta",true);
		this.AdaptadorRTAPQR 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strRespuestaPQR);
		this._cmbRespuesta.setAdapter(this.AdaptadorRTAPQR);
		
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
			
			case R.id.VerConsumos:
				this.new_form = new Intent(this, ListaConsumos.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;
				
			case R.id.VerSellos:
				this.new_form = new Intent(this, ListaSellos.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;	
								
			case R.id.Volver:
				finish();
				this.new_form = new Intent(this, ListaTrabajo.class);
				this.new_form.putExtra("NivelLogged", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;
				
			case R.id.Pruebas:
				this.new_form = new Intent(this, PruebaIntegracion.class);
				this.new_form.putExtra("Solicitud", this.Solicitud);
				this.new_form.putExtra("NivelUsuario", this.NivelUsuario);
				this.new_form.putExtra("FolderAplicacion", this.FolderAplicacion);
				startActivity(this.new_form);
				return true;
				
			case R.id.ImpresionOriginal:
				this.FormatoImp.FormatoDesviacion(this.Solicitud, "Original", 1);
				return true;
				
			case R.id.ImpresionUsuario:
				this.FormatoImp.FormatoDesviacion(this.Solicitud, "Usuario", 2);
				return true;
			
			case R.id.ImpresionArchivo:
				this.FormatoImp.FormatoDesviacion(this.Solicitud, "Archivo", 3);
				return true;
			
			case R.id.Foto:
				this.getFoto();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);	
		}
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.ActaBtnRegistrar:
				if(_txtDocEnterado.getText().toString().equals("") || _txtNomEnterado.getText().toString().equals("")){
					Toast.makeText(this, "Se debe Diligenciar las datos del Enterado", Toast.LENGTH_SHORT).show();
				}else{
					if(_cmbRespuesta.getSelectedItem().toString().equals("")){
						Toast.makeText(this, "Debe Seleccionar una respueta PQR", Toast.LENGTH_SHORT).show();
					}
					else{
						this.FcnActas.setDatosActas(this.Solicitud, 
								_txtDocEnterado.getText().toString(), 
								_txtNomEnterado.getText().toString(), 
								_txtDocTestigo.getText().toString(), 
								_txtNomTestigo.getText().toString(), 
								_cmbTipoEnterado.getSelectedItem().toString(), 
								_cmbRespuesta.getSelectedItem().toString());	
					}
				}				
				break;
		}	
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
        	if(resultCode == RESULT_OK && requestCode == INICIAR_CAMARA){
        		this.FcnImage.resizeImage(this.nombreFoto,_WIDTH_PICTURE, _HEIGHT_PICTURE);
        		
        		//new UpLoadFoto(this, this.FolderAplicacion).execute(_txtOrden.getText().toString(),_txtActa.getText().toString(),_txtCuenta.getText().toString(),this.fotoParcial);        		
        		 new UploaderFoto(this, this.FolderAplicacion).execute(this.fotoParcial,_txtOrden.getText().toString(),_txtActa.getText().toString(),_txtCuenta.getText().toString());
            }
        }catch(Exception e){

        }
    }
	
	private void getFoto(){
		long ahora = System.currentTimeMillis();
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeInMillis(ahora);
		int minuto = calendario.get(Calendar.MINUTE);
		
		this.nombreFoto = this.Solicitud+"_"+minuto+".jpeg";
		
        File imagesFolder   = new File(this.FolderAplicacion);
        File image          = new File(imagesFolder,this.nombreFoto);
        
        this.fotoParcial = image.toString();
        
        Uri uriSavedImage = Uri.fromFile(image);
        this.IniciarCamara.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        
        startActivityForResult(IniciarCamara, INICIAR_CAMARA);
    }
}
