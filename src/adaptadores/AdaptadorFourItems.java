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

public class AdaptadorFourItems extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<DetalleFourItems> items;
	
	public AdaptadorFourItems(Activity activity, ArrayList<DetalleFourItems> items){
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
			v = inf.inflate(R.layout.four_items_horizontal, null);
		}
				
		DetalleFourItems Item = items.get(position);		
		TextView item1 = (TextView) v.findViewById(R.id.fourItem1);
		TextView item2 = (TextView) v.findViewById(R.id.fourItem2);
		TextView item3 = (TextView) v.findViewById(R.id.fourItem3);
		TextView item4 = (TextView) v.findViewById(R.id.fourItem4);
		
		item1.setText(Item.getItem1());
		item2.setText(Item.getItem2());
		item3.setText(Item.getItem3());
		item4.setText(Item.getItem4());		
		return v;
	}
}
