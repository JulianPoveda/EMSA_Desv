package adaptadores;

import java.util.ArrayList;

import desviaciones.emsa.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
		
		if(Item.getItem9().equals("P")){
			item1.setBackgroundColor(Color.WHITE);
			item2.setBackgroundColor(Color.WHITE);
			item3.setBackgroundColor(Color.WHITE);	
			item4.setBackgroundColor(Color.WHITE);	
			item5.setBackgroundColor(Color.WHITE);	
			item6.setBackgroundColor(Color.WHITE);	
			item7.setBackgroundColor(Color.WHITE);	
			item8.setBackgroundColor(Color.WHITE);	
		}else if(Item.getItem9().equals("E")){
			item1.setBackgroundColor(Color.GREEN);
			item2.setBackgroundColor(Color.GREEN);
			item3.setBackgroundColor(Color.GREEN);	
			item4.setBackgroundColor(Color.GREEN);	
			item5.setBackgroundColor(Color.GREEN);	
			item6.setBackgroundColor(Color.GREEN);	
			item7.setBackgroundColor(Color.GREEN);	
			item8.setBackgroundColor(Color.GREEN);			
		}else if(Item.getItem9().equals("T")){
			item1.setBackgroundColor(Color.YELLOW);
			item2.setBackgroundColor(Color.YELLOW);
			item3.setBackgroundColor(Color.YELLOW);	
			item4.setBackgroundColor(Color.YELLOW);	
			item5.setBackgroundColor(Color.YELLOW);	
			item6.setBackgroundColor(Color.YELLOW);	
			item7.setBackgroundColor(Color.YELLOW);	
			item8.setBackgroundColor(Color.YELLOW);	
		}else{
			item1.setBackgroundColor(Color.RED);
			item2.setBackgroundColor(Color.RED);
			item3.setBackgroundColor(Color.RED);	
			item4.setBackgroundColor(Color.RED);	
			item5.setBackgroundColor(Color.RED);	
			item6.setBackgroundColor(Color.RED);	
			item7.setBackgroundColor(Color.RED);	
			item8.setBackgroundColor(Color.RED);	
		}
		
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
