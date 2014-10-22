package desviaciones.emsa;

import java.util.ArrayList;


import clases.ClassInSolicitudes;
import dialogos.DialogoInformacion;

import sistema.SQLite;
import adaptadores.AdaptadorEightItems;
import adaptadores.DetalleEightItems;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class ListaTrabajo extends Activity implements OnItemSelectedListener{
	private static int 			CONFIRMACION_INFORMACION 	= 1;
	private static int 			CONFIRMACION_INICIO_ORDEN	= 2;
	private static int 			CONFIRMACION_CERRAR_ORDEN	= 3;
	private static int 			CONFIRMACION_COD_APERTURA	= 4;
	
	private Intent 				IniciarSolicitud;
	private Intent 				DialogInformacion; 
	private Intent 				DialogConfirmacion; 
	private SQLite				FcnSQL;				
	private ClassInSolicitudes	FcnInSolicitudes;	
	
	private Spinner 	_cmbNodos;
	private ListView 	_lstSolicitudes;
		
	AdaptadorEightItems AdaptadorSolicitudes;
	ArrayList<DetalleEightItems> ArraySolicitudes = new ArrayList<DetalleEightItems>();
	
	private String 						SolicitudSeleccionada = "";
	private ArrayAdapter<String> 		AdaptadorNodos;
	private ArrayList<String> 			Nodos = new ArrayList<String>();
	private int 						NivelUsuario;
	private String 						FolderAplicacion;
	private ContentValues				_tempRegistro = new ContentValues();
	private ArrayList<ContentValues>	_tempTabla = new ArrayList<ContentValues>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_trabajo);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelLogged");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.FcnInSolicitudes	= new ClassInSolicitudes(this,this.FolderAplicacion);
		this.FcnSQL				= new SQLite(this,this.FolderAplicacion);
		
		this.DialogInformacion	= new Intent(this,DialogoInformacion.class);
		this.DialogConfirmacion = new Intent(this,DialogoConfirmacion.class);
		
		this._cmbNodos 			= (Spinner) findViewById(R.id.TrabajoCmbNodos);
		this._lstSolicitudes 	= (ListView) findViewById(R.id.TrabajoLstSolicitudes);
			
		this.Nodos = FcnInSolicitudes.getNodosSolicitudes();
		this.AdaptadorNodos = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.Nodos);
		this._cmbNodos.setAdapter(this.AdaptadorNodos);
		
		AdaptadorSolicitudes = new AdaptadorEightItems(this,ArraySolicitudes);
		_lstSolicitudes.setAdapter(AdaptadorSolicitudes);
		
		_cmbNodos.setOnItemSelectedListener(this);
		//_lstSolicitudes.setOnItemClickListener(this);
		
		registerForContextMenu(this._lstSolicitudes);
	}

	/**Funciones para el control del menu contextual del listview que muestra las ordenes de trabajo**/
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		this.SolicitudSeleccionada = ArraySolicitudes.get(info.position).getItem1();		
		switch(v.getId()){
			case R.id.TrabajoLstSolicitudes:
				menu.setHeaderTitle("Solicitud " +ArraySolicitudes.get(info.position).getItem1());
			    super.onCreateContextMenu(menu, v, menuInfo);
			    MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.menu_solicitudes, menu);
			    break;				
		}
	  
		/*if (v.getId() == R.id.TrabajoLstSolicitudes) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    this.SolicitudSeleccionada = ArraySolicitudes.get(info.position).getItem1();
	    menu.setHeaderTitle("Solicitud " +ArraySolicitudes.get(info.position).getItem1());
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_solicitudes, menu);
	  }*/
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {				
			case R.id.IniciarSolicitud:
				if(this.FcnInSolicitudes.IniciarSolicitud(this.SolicitudSeleccionada)){
					this.DialogConfirmacion.putExtra("informacion", "Desea Iniciar La Orden "+ this.SolicitudSeleccionada);
					startActivityForResult(DialogConfirmacion, this.CONFIRMACION_INICIO_ORDEN);
				}else{
					DialogInformacion.putExtra("informacion", "No se puede iniciar la actividad, comprobar:\n->Que no exista otra solicitud abierta\n->Que la solicitud seleccionada no este en estado 'TERMINADA'");
					startActivityForResult(DialogInformacion, CONFIRMACION_INFORMACION);
				}					
				return true;
			
			
			case R.id.TerminarSolicitud:
				if(this.FcnInSolicitudes.getEstadoSolicitud(this.SolicitudSeleccionada).equals("E")){
					DialogConfirmacion.putExtra("informacion", "Desea Cerrar La Solicitud "+this.SolicitudSeleccionada);
					startActivityForResult(DialogConfirmacion, CONFIRMACION_CERRAR_ORDEN);	
				}
				return true;
			
			/*case R.id.AbrirSolicitud:
				/*if(FcnSolicitudes.IniciarAperturaOrden(_txtOrden.getText().toString())){
					DialogApertura.putExtra("titulo","INGRESE EL CODIGO DE APERTURA");
					DialogApertura.putExtra("lbl1", "Codigo:");
					DialogApertura.putExtra("txt1", "");
					startActivityForResult(DialogApertura, CONFIRMACION_COD_APERTURA);
				}else{
					DialogInformacion.putExtra("informacion", "No es posible abrir la orden "+_txtOrden.getText().toString()+", verifique que:\n->Este en estado terminada.\n->No exista otra orden abierta.");
					startActivityForResult(DialogInformacion, CONFIRMACION_INFORMACION);
				}*/
				
			
				
			default:
	            return super.onContextItemSelected(item);        
	    }
	}
	

	public void CargarOrdenesTrabajo(){
		ArraySolicitudes.clear();
		if(_cmbNodos.getSelectedItem().toString().equals("Todos")){
			_tempTabla = FcnSQL.SelectData("in_ordenes_trabajo", "solicitud,dependencia,cuenta,suscriptor,direccion,nodo,marca,serie,estado", "solicitud IS NOT NULL ORDER BY solicitud");
			for(int i=0;i<_tempTabla.size();i++){
				_tempRegistro = _tempTabla.get(i);
				ArraySolicitudes.add(new DetalleEightItems(_tempRegistro.getAsString("solicitud"),_tempRegistro.getAsString("dependencia"),_tempRegistro.getAsString("cuenta"),_tempRegistro.getAsString("suscriptor"),_tempRegistro.getAsString("direccion"),_tempRegistro.getAsString("nodo"),_tempRegistro.getAsString("marca"),_tempRegistro.getAsString("serie"),_tempRegistro.getAsString("estado")));
			}
		}else{
			_tempTabla = FcnSQL.SelectData("in_ordenes_trabajo", "solicitud,dependencia,cuenta,suscriptor,direccion,nodo,marca,serie,estado", "nodo='"+_cmbNodos.getSelectedItem().toString()+"' ORDER BY solicitud");
			for(int i=0;i<_tempTabla.size();i++){
				_tempRegistro = _tempTabla.get(i);
				ArraySolicitudes.add(new DetalleEightItems(_tempRegistro.getAsString("solicitud"),_tempRegistro.getAsString("dependencia"),_tempRegistro.getAsString("cuenta"),_tempRegistro.getAsString("suscriptor"),_tempRegistro.getAsString("direccion"),_tempRegistro.getAsString("nodo"),_tempRegistro.getAsString("marca"),_tempRegistro.getAsString("serie"),_tempRegistro.getAsString("estado")));
			}
		}
		AdaptadorSolicitudes.notifyDataSetChanged();
	}
	
		
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == CONFIRMACION_INICIO_ORDEN && data.getExtras().getBoolean("accion")) {
			this.FcnInSolicitudes.setEstadoSolicitud(this.SolicitudSeleccionada, "E");
			finish();
			IniciarSolicitud = new Intent(this, Actas.class);
			this.IniciarSolicitud.putExtra("Solicitud", this.SolicitudSeleccionada);
			this.IniciarSolicitud.putExtra("NivelUsuario", this.NivelUsuario);
			this.IniciarSolicitud.putExtra("FolderAplicacion", this.FolderAplicacion);
			startActivity(IniciarSolicitud);
		}else if(resultCode == RESULT_OK && requestCode == CONFIRMACION_CERRAR_ORDEN && data.getExtras().getBoolean("accion")){
			_tempRegistro.clear();
			_tempRegistro.put("estado","T");
			this.FcnInSolicitudes.setEstadoSolicitud(this.SolicitudSeleccionada, "T");
			CargarOrdenesTrabajo();			
		}
		
		/*else if(resultCode == RESULT_OK && requestCode == CONFIRMACION_COD_APERTURA){
			if(data.getExtras().getBoolean("response")){
				if(FcnSolicitudes.verificarCodigoApertura(_txtOrden.getText().toString(),data.getExtras().getString("txt1"))){
					_tempRegistro.clear();
					_tempRegistro.put("estado","E");
					SolicitudesSQL.UpdateRegistro("amd_ordenes_trabajo", _tempRegistro, "id_orden='"+_txtOrden.getText().toString()+"'");	
					CargarOrdenesTrabajo();
				}else{
					DialogInformacion.putExtra("informacion", "Codigo De Apertura Erroneo");
					startActivityForResult(DialogInformacion, CONFIRMACION_INFORMACION);
				}
			}			
		}*/
    }
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()){
			case R.id.TrabajoCmbNodos:
				CargarOrdenesTrabajo();
				break;
		}		
	}
	

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
