package desviaciones.emsa;

import java.util.ArrayList;

import clases.ClassConfiguracion;

import sistema.SQLite;
import adaptadores.AdaptadorTwoChkItems;
import adaptadores.ParcelableTwoChkItems;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import ws_connect.UpLoadTrabajo;

public class FormDescarga extends Activity implements OnClickListener{
	private SQLite						FcnSQL;	
	private ClassConfiguracion 			FcnCfg;
	
	private String 						FolderAplicacion;
	private ContentValues				_tempRegistro = new ContentValues();
	private ArrayList<ContentValues>	_tempTabla = new ArrayList<ContentValues>();	
	private ListView 					_lstSolicitudes;
	private Button						_btnDescargar;
	AdaptadorTwoChkItems 				AdaptadorSolicitudes;
	ArrayList<ParcelableTwoChkItems> 	ArraySolicitudes = new ArrayList<ParcelableTwoChkItems>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_descarga);
		
		Bundle bundle = getIntent().getExtras();
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.FcnSQL				= new SQLite(this,this.FolderAplicacion);
		this.FcnCfg				= new ClassConfiguracion(this ,this.FolderAplicacion);
		
		this._lstSolicitudes 	= (ListView) findViewById(R.id.DescargaLstSolicitudes);
		this._btnDescargar		= (Button) findViewById(R.id.DescargaBtnSolicitudes);
		
		
		AdaptadorSolicitudes = new AdaptadorTwoChkItems(this,ArraySolicitudes);
		this._lstSolicitudes.setAdapter(AdaptadorSolicitudes);
		
		ArraySolicitudes.clear();
		this._tempTabla = FcnSQL.SelectData("in_ordenes_trabajo", "solicitud,estado", "estado IN ('P','E','T') ORDER BY solicitud");
		for(int i=0;i<_tempTabla.size();i++){
			this._tempRegistro = _tempTabla.get(i);
			ArraySolicitudes.add(new ParcelableTwoChkItems(true,this._tempRegistro.getAsString("solicitud"),this._tempRegistro.getAsString("estado")));
		}
		AdaptadorSolicitudes.notifyDataSetChanged();
		this._btnDescargar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.DescargaBtnSolicitudes:
				String solicitudes = "";
				for(int i=0;i<this.ArraySolicitudes.size();i++){
					if(this.ArraySolicitudes.get(i).getChecked()){
						solicitudes += this.ArraySolicitudes.get(i).getSolicitud()+"|";
					}
					
					//Toast.makeText(this, "Solicitud "+this.ArraySolicitudes.get(i).getSolicitud()+", estado "+this.ArraySolicitudes.get(i).getChecked(),Toast.LENGTH_SHORT).show();
				}
				new UpLoadTrabajo(this, this.FolderAplicacion).execute(this.FcnCfg.getEquipo()+"", solicitudes);
				break;
		}		
	}
}
