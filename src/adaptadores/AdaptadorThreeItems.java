package adaptadores;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import desviaciones.emsa.R;

public class AdaptadorThreeItems extends BaseAdapter{
	protected Activity activity;
	protected ArrayList<DetalleThreeItems> items;
	
	public AdaptadorThreeItems(Activity activity, ArrayList<DetalleThreeItems> items){
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
			v = inf.inflate(R.layout.three_items_horizontal, null);
		}
				
		DetalleThreeItems Item = items.get(position);		
		TextView item1 = (TextView) v.findViewById(R.id.threeItem1);
		TextView item2 = (TextView) v.findViewById(R.id.threeItem2);
		TextView item3 = (TextView) v.findViewById(R.id.threeItem3);
		
		item1.setText(Item.getItem1());
		item2.setText(Item.getItem2());
		item3.setText(Item.getItem3());
		return v;
	}
}

