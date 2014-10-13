package desviaciones.emsa;

import java.util.ArrayList;

import clases.ClassIrregularidades;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Irregularidades extends Activity implements OnClickListener, OnItemSelectedListener, OnItemClickListener{
	private ClassIrregularidades 		FcnIrreg;
	
	private ContentValues				_tempRegistro;
	private ArrayList<ContentValues> 	_tempTabla;
	private String	Solicitud;
	private String 	FolderAplicacion;
	private int 	NivelUsuario = 1;	
	private int 	FilaSeleccionada = -1;
	
	private ArrayList<String> 		_strIrregularidades 	= new ArrayList<String>();
	private ArrayList<String> 		_strIrregularidadesReg 	= new ArrayList<String>();
	private ArrayAdapter<String> 	AdaptadorIrregularidades;
	private ArrayAdapter<String> 	AdaptadorIrregularidadesReg;
		
	Spinner 	_cmbIrregularidades;
	Button		_btnAgregar, _btnEliminar;
	ListView	_lstIrregularidades;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_irregularidades);
		
		Bundle bundle = getIntent().getExtras();
		this.NivelUsuario 		= bundle.getInt("NivelUsuario");
		this.Solicitud			= bundle.getString("Solicitud");
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		
		FcnIrreg 	= new ClassIrregularidades(this, this.FolderAplicacion);		
		
		_cmbIrregularidades = (Spinner) findViewById(R.id.IrregCmbIrregularidades);
		_btnAgregar			= (Button) findViewById(R.id.IrregBtnAgregar);
		_btnEliminar		= (Button) findViewById(R.id.IrregBtnEliminar);
		_lstIrregularidades	= (ListView) findViewById(R.id.IrregLstIrregularidades);
		
		this._strIrregularidades = FcnIrreg.getIrregularidades();
		this.AdaptadorIrregularidades	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._strIrregularidades); 
		this._cmbIrregularidades.setAdapter(this.AdaptadorIrregularidades);
		
		this._strIrregularidadesReg = FcnIrreg.getIrregularidadesRegistradas(this.Solicitud);
		this.AdaptadorIrregularidadesReg	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._strIrregularidadesReg); 
		this._lstIrregularidades.setAdapter(this.AdaptadorIrregularidadesReg);
		
		_btnAgregar.setOnClickListener(this);
		_btnEliminar.setOnClickListener(this);
		_lstIrregularidades.setOnItemClickListener(this);
	}

	
	private void CargarIrregularidadesRegistradas(){
		this._strIrregularidadesReg = FcnIrreg.getIrregularidadesRegistradas(this.Solicitud);
		this.AdaptadorIrregularidadesReg	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._strIrregularidadesReg);
		this._lstIrregularidades.setAdapter(this.AdaptadorIrregularidadesReg);
		this.AdaptadorIrregularidadesReg.notifyDataSetChanged();
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
			case R.id.IrregBtnAgregar:
				this.FcnIrreg.registrarIrregularidad(this.Solicitud, this._cmbIrregularidades.getSelectedItem().toString());
				this.CargarIrregularidadesRegistradas();
				break;
				
			case R.id.IrregBtnEliminar:
				if(this.FilaSeleccionada != -1){
					this.FcnIrreg.eliminarIrregularidad(this.Solicitud, this._strIrregularidadesReg.get(this.FilaSeleccionada));
					this.CargarIrregularidadesRegistradas();
				}
				break;
		}
		
	}
}
