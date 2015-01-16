package desviaciones.emsa;

import java.util.ArrayList;

import sistema.SQLite;
import adaptadores.AdaptadorFourItems;
import adaptadores.DetalleFourItems;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.ListView;


public class ListaConsumos extends Activity {
	private ListView 			_lstConsumos;
	private String 				FolderAplicacion;
	private String 				Solicitud;
	private SQLite				FcnSQL;
	AdaptadorFourItems 			AdaptadorConsumos;
	ArrayList<DetalleFourItems> ArrayConsumos = new ArrayList<DetalleFourItems>();
	
	private ContentValues				_tempRegistro 	= new ContentValues();
	private ArrayList<ContentValues>	_tempTabla 		= new ArrayList<ContentValues>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_consumos);
		
		Bundle bundle 	= getIntent().getExtras();
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.Solicitud 			= bundle.getString("Solicitud");
		
		this.FcnSQL			= new SQLite(this,this.FolderAplicacion);
		this._lstConsumos 	= (ListView) findViewById(R.id.LstConsumos);
		
		AdaptadorConsumos = new AdaptadorFourItems(this,ArrayConsumos);
		this._lstConsumos.setAdapter(AdaptadorConsumos);
		
		this.ArrayConsumos.clear();
		ArrayConsumos.add(new DetalleFourItems("FECHA","LECT TOMADA","LECT ANTERIOR","CONSUMO"));
		this._tempTabla = FcnSQL.SelectData("in_consumos", "fecha_tomada,lectura_tomada,lectura_anterior,consumo", "solicitud='"+this.Solicitud+"' ORDER BY  periodo");
		for(int i=0;i<this._tempTabla.size();i++){
			_tempRegistro = this._tempTabla.get(i);
			ArrayConsumos.add(new DetalleFourItems(_tempRegistro.getAsString("fecha_tomada"),_tempRegistro.getAsString("lectura_tomada"),_tempRegistro.getAsString("lectura_anterior"),_tempRegistro.getAsString("consumo")));
		}
		AdaptadorConsumos.notifyDataSetChanged();
	}
}
