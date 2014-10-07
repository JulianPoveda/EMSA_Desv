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

public class AdaptadorEightItems extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<DetalleEightItems> items;
	
	public AdaptadorEightItems(Activity activity, ArrayList<DetalleEightItems> items){
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
			v = inf.inflate(R.layout.eigth_items_horizontal, null);
		}
				
		DetalleEightItems Item = items.get(position);		
		TextView item1 = (TextView) v.findViewById(R.id.item1);
		TextView item2 = (TextView) v.findViewById(R.id.item2);
		TextView item3 = (TextView) v.findViewById(R.id.item3);
		TextView item4 = (TextView) v.findViewById(R.id.item4);
		TextView item5 = (TextView) v.findViewById(R.id.item5);
		TextView item6 = (TextView) v.findViewById(R.id.item6);
		TextView item7 = (TextView) v.findViewById(R.id.item7);
		TextView item8 = (TextView) v.findViewById(R.id.item8);
		
		item1.setText(Item.getItem1());
		item2.setText(Item.getItem2());
		item3.setText(Item.getItem3());
		item4.setText(Item.getItem4());
		item5.setText(Item.getItem5());
		item6.setText(Item.getItem6());
		item7.setText(Item.getItem7());
		item8.setText(Item.getItem8());		
		
		return v;
	}

}
