package adaptadores;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableTwoChkItems implements Parcelable{
	private boolean checked;
	private String 	solicitud;
	private String 	estado;
	

	
	public ParcelableTwoChkItems(boolean _checked, String _solicitud, String _estado){
		this.checked	= _checked;
		this.solicitud 	= _solicitud;
		this.estado 	= _estado;
	}
	
	
	public ParcelableTwoChkItems(Parcel _in){
		this.checked	= _in.readInt() == 1 ? true:false;
		this.solicitud	= _in.readString();
		this.estado		= _in.readString();
	}
	
	public boolean getChecked(){
		return this.checked;
	}
	
	public void setChecked(boolean _check){
		this.checked = _check;
	}
	
	public String getSolicitud(){
		return this.solicitud;
	}
	
	public void setSolicitud(String _solicitud){
		this.solicitud = _solicitud;
	}
	
	public String getEstado(){
		return this.estado;
	}
	
	public void setEstado(String _estado){
		this.estado	= _estado;
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getChecked() ? 1 : 0);		
		dest.writeString(getSolicitud());
		dest.writeString(getEstado());		
	}
	
	
	public static final Parcelable.Creator<ParcelableTwoChkItems> CREATOR = new Parcelable.Creator<ParcelableTwoChkItems>() {
		public ParcelableTwoChkItems createFromParcel(Parcel in) {
			return new ParcelableTwoChkItems(in);
		}
	 
		public ParcelableTwoChkItems[] newArray(int size) {
			return new ParcelableTwoChkItems[size];
		}
	};

}
