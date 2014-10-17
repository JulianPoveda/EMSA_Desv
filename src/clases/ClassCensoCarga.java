package clases;

import java.util.ArrayList;

import sistema.SQLite;
import android.content.ContentValues;
import android.content.Context;

public class ClassCensoCarga {
	private SQLite	CensoSQL;
	private Context _ctxCenso;
	private String 	_folderAplicacion;
	
	private ArrayList<ContentValues> _tempTabla = new ArrayList<ContentValues>();
	private double _subtotal;
	
	
	
	public ClassCensoCarga(Context _ctx, String _folder){
		this._ctxCenso		= _ctx;
		this._folderAplicacion	= _folder;
		this.CensoSQL	= new SQLite(this._ctxCenso, this._folderAplicacion);
	}
	
	
	public ArrayList<ContentValues> getElementosCensoResidencial(String _solicitud){
		this._tempTabla.clear();
		this._tempTabla = this.CensoSQL.SelectData("vista_censo_carga", "descripcion,carga,vatios,cantidad", "solicitud='"+_solicitud+"' AND servicio='R'");
		return this._tempTabla;
	}
	
	public ArrayList<ContentValues> getElementosCensoNoResidencial(String _solicitud){
		this._tempTabla.clear();
		this._tempTabla = this.CensoSQL.SelectData("vista_censo_carga", "descripcion,carga,vatios,cantidad", "solicitud='"+_solicitud+"' AND servicio='N'");
		return this._tempTabla;
	}
	
	public double getTotalCenso(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"'");
	}
	
	public double getTotalCensoResidencial(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"' AND servicio='R'");
	}
	
	public double getTotalCensoNoResidencial(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"' AND servicio='N'");
	}
	
	public double getTotalCensoRegistrada(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"' AND carga='R'");
	}
	
	public double getTotalCensoDirecta(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"' AND carga='D'");
	}
	
	public double getTotalCensoResidencialRegistrada(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"' AND servicio='R' AND carga='R'");
	}
	
	public double getTotalCensoResidencialDirecta(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"' AND servicio='R' AND carga='D'");
	}
	
	public double getTotalCensoNoResidencialRegistrada(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"' AND servicio='N' AND carga='R'");
	}
	
	public double getTotalCensoNoResidencialDirecta(String _solicitud){
		return  this.CensoSQL.DoubleSelectShieldWhere("dig_censo_carga", "sum(cantidad*vatios) as sumatoria", "solicitud='"+_solicitud+"' AND servicio='N' AND carga='D'");
	}
}
