package desviaciones.emsa;

import java.util.ArrayList;

import sistema.SQLite;
import sistema.Utilidades;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class CensoCarga extends Activity {
	private SQLite 						CensoSQL;
	private Utilidades					CensoUtil;
	
	private ContentValues				_tempRegistro;
	private ArrayList<ContentValues> 	_tempTabla;
	
	private ArrayList<String> 		strElementos= new ArrayList<String>();
	private ArrayAdapter<String> 	AdaptadorElementos;
	
	
	private int 						NivelUsuario = 1;
	private String 						Solicitud;
	private String 						FolderAplicacion = "";
	
	Spinner 	_cmbElemento, _cmbCarga, _cmbTipo, _cmbVatios, _cmbCantidad;
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
	}
}
