package adaptadores;

import java.util.ArrayList;

import desviaciones.emsa.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorSixItems extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<DetalleSixItems> items;
	
	public AdaptadorSixItems(Activity activity, ArrayList<DetalleSixItems> items){
		this.activity = activity;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(convertView == null){
			LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.six_items_horizontal, null);
		}
				
		DetalleSixItems Item = items.get(position);		
		TextView item1 = (TextView) v.findViewById(R.id.sixItem1);
		TextView item2 = (TextView) v.findViewById(R.id.sixItem2);
		TextView item3 = (TextView) v.findViewById(R.id.sixItem3);
		TextView item4 = (TextView) v.findViewById(R.id.sixItem4);
		TextView item5 = (TextView) v.findViewById(R.id.sixItem5);
		TextView item6 = (TextView) v.findViewById(R.id.sixItem6);
		
		item1.setText(Item.getItem1());
		item2.setText(Item.getItem2());
		item3.setText(Item.getItem3());
		item4.setText(Item.getItem4());
		item5.setText(Item.getItem5());
		item6.setText(Item.getItem6());
		
		return v;
	}

}
