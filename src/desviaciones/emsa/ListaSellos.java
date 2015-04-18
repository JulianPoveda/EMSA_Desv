package desviaciones.emsa;

import java.util.ArrayList;

import sistema.SQLite;
import adaptadores.AdaptadorThreeItems;
import adaptadores.DetalleThreeItems;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.ListView;

public class ListaSellos extends Activity {
	private ListView 			_lstConsumos;
	private String 				FolderAplicacion;
	private String 				Solicitud;
	private SQLite				FcnSQL;
	AdaptadorThreeItems 		AdaptadorSellos;
	ArrayList<DetalleThreeItems> ArraySellos= new ArrayList<DetalleThreeItems>();
	
	private ContentValues				_tempRegistro 	= new ContentValues();
	private ArrayList<ContentValues>	_tempTabla 		= new ArrayList<ContentValues>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_sellos);
		
		Bundle bundle 	= getIntent().getExtras();
		this.FolderAplicacion 	= bundle.getString("FolderAplicacion");
		this.Solicitud 			= bundle.getString("Solicitud");
		
		this.FcnSQL			= new SQLite(this,this.FolderAplicacion);
		this._lstConsumos 	= (ListView) findViewById(R.id.LstConsumos);
		
		AdaptadorSellos = new AdaptadorThreeItems(this,ArraySellos);
		this._lstConsumos.setAdapter(AdaptadorSellos);
		
		this.ArraySellos.clear();
		ArraySellos.add(new DetalleThreeItems("NUMERO","TIPO","CLASE"));
		this._tempTabla = FcnSQL.SelectData("in_sellos", "numero,tipo,clase", "solicitud='"+this.Solicitud+"' ORDER BY  numero");
		for(int i=0;i<this._tempTabla.size();i++){
			_tempRegistro = this._tempTabla.get(i);
			ArraySellos.add(new DetalleThreeItems(_tempRegistro.getAsString("numero"),_tempRegistro.getAsString("tipo"),_tempRegistro.getAsString("clase")));
		}
		AdaptadorSellos.notifyDataSetChanged();
	}
}
