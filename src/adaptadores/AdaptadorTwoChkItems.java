package adaptadores;

import java.util.ArrayList;

import desviaciones.emsa.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AdaptadorTwoChkItems extends BaseAdapter implements OnClickListener{
	protected Activity activity;
	protected ArrayList<DetalleTwoChkItems> items;
	
	CheckBox check1;
	TextView item1; 
	
	static class ViewHolder{
		CheckBox chkBox;
		TextView txtView;		
	}
	
	private ArrayList<ParcelableTwoChkItems> data;
	private LayoutInflater inflater = null;
	
	private static final String TAG = "CustomAdapter";
	private static int convertViewCounter = 0;
	
	public AdaptadorTwoChkItems(Context c, ArrayList<ParcelableTwoChkItems> d){
       //Log.v(TAG, "Constructing CustomAdapter");
       this.data 	= d;
       inflater 	= LayoutInflater.from(c);
	}
	
	
	/*public AdaptadorTwoChkItems(Activity activity, ArrayList<DetalleTwoChkItems> items){
		this.activity = activity;
		this.items = items;
	}*/

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		//return items.get(position).getId();
		return position;
	}

	@Override
	public int getViewTypeCount(){
		//Log.v(TAG, "in getViewTypeCount()");
		return 1;
	}
	
	@Override
	public int getItemViewType(int position){
		//Log.v(TAG, "in getItemViewType() for position " + position);
		return 0;
	}
	
	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.chk_two_items, null);
			convertViewCounter++;
			holder = new ViewHolder(); 
            holder.chkBox 	= (CheckBox) convertView.findViewById(R.id.two_items_chk1);
            holder.txtView 	= (TextView) convertView.findViewById(R.id.two_items_lbl1);
            convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();		
		}
		ParcelableTwoChkItems d = (ParcelableTwoChkItems) getItem(position);
		holder.chkBox.setTag(d);
		holder.chkBox.setChecked(data.get(position).getChecked());
		holder.chkBox.setText(data.get(position).getSolicitud());
		holder.txtView.setText(data.get(position).getEstado());
		holder.chkBox.setOnClickListener(this);
		return convertView;		
	}
	
	
	public void setCheck(int position){
		ParcelableTwoChkItems d = data.get(position);
		d.setChecked(!d.getChecked());
		notifyDataSetChanged();
	}
	
	/*private OnClickListener checkListener = new OnClickListener(){	 
	   @Override
	   public void onClick(View v){
		   ParcelableTwoChkItems d = (ParcelableTwoChkItems) v.getTag();
		   d.setChecked(!d.getChecked());
	   }
	};*/

	@Override
	public void onClick(View v) {
		ParcelableTwoChkItems d = (ParcelableTwoChkItems) v.getTag();
		d.setChecked(!d.getChecked());		
	}
}
