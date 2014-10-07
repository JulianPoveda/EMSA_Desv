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

public class AdaptadorFiveItems extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<DetalleFiveItems> items;
	
	public AdaptadorFiveItems(Activity activity, ArrayList<DetalleFiveItems> items){
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
			v = inf.inflate(R.layout.five_items_horizontal, null);
		}
				
		DetalleFiveItems Item = items.get(position);		
		TextView item1 = (TextView) v.findViewById(R.id.fiveItem1);
		TextView item2 = (TextView) v.findViewById(R.id.fiveItem2);
		TextView item3 = (TextView) v.findViewById(R.id.fiveItem3);
		TextView item4 = (TextView) v.findViewById(R.id.fiveItem4);
		TextView item5 = (TextView) v.findViewById(R.id.fiveItem5);
		
		item1.setText(Item.getItem1());
		item2.setText(Item.getItem2());
		item3.setText(Item.getItem3());
		item4.setText(Item.getItem4());
		item5.setText(Item.getItem5());
		
		return v;
	}

}
