package desviaciones.emsa;

import java.util.ArrayList;


import clases.ClassInSolicitudes;

import sistema.DrawTablas;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

public class ListaTrabajo extends Activity implements OnItemSelectedListener, OnItemClickListener{
	private DrawTablas					GraphSellosTabla;
	private ClassInSolicitudes			FcnInSolicitudes;	
	
	private ArrayAdapter<String> AdaptadorNodos;
	private ArrayList<String> Nodos = new ArrayList<String>();
	private Spinner _cmbNodos;
	
	private int 						NivelUsuario;
	private String 						FolderAplicacion;
	private TableLayout					TablaSolicitudes;
	private LinearLayout 				FilaTablaSolicitudes;	
	private ArrayList<ContentValues>	_tempRegistro = new ArrayList<ContentValues>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_trabajo);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelLogged");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.FcnInSolicitudes	= new ClassInSolicitudes(this,this.FolderAplicacion);
		
		this.Nodos = FcnInSolicitudes.getNodosSolicitudes();
		this.AdaptadorNodos = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.Nodos);
		this._cmbNodos 		= (Spinner) findViewById(R.id.TrabajoCmbNodos);
		this._cmbNodos.setAdapter(this.AdaptadorNodos);
		
		/*GraphSellosTabla	= new DrawTablas(this, "tipo_ingreso,tipo_sello,serie,color,ubicacion,irregularidad", "165,165,165,165,165,415", 1, "#74BBEE", "#A9CFEA" ,"#EE7474");
		FilaTablaSolicitudes= (LinearLayout) findViewById(R.id.TablaSolicitudes);
		
		TablaSolicitudes 	= GraphSellosTabla.CuerpoTabla(this._tempRegistro);
		FilaTablaSolicitudes.removeAllViews();
		FilaTablaSolicitudes.addView(TablaSolicitudes);*/
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
