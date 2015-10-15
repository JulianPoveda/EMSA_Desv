package sistema;

import java.io.File;

import ws_connect.UpdateOrdenes;
import android.content.Context;
import android.os.CountDownTimer;

public class Beacon extends CountDownTimer{
	private String 	FolderAplicacion;
	private Context	TemporizadorCtx;
	
	private SQLite	BeaconSQL; 

	public Beacon(Context _ctx, String _folder, long _millisInFuture, long _countDownInterval) {
		// TODO Auto-generated constructor stub
		super(_millisInFuture, _countDownInterval);
		this.TemporizadorCtx 	= _ctx;
		this.FolderAplicacion	= _folder;		
		this.BeaconSQL = new SQLite(this.TemporizadorCtx, this.FolderAplicacion);
	}

	
	@Override
	public void onTick(long millisUntilFinished) {
		
		/**Web Service para subir al servidor las actas impresas**/
		new UpdateOrdenes(this.TemporizadorCtx, this.FolderAplicacion).execute();	
		
	}
	
	private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
	}
}

