package sistema;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTime {
	protected static String     NameOfMonth[]   = { "Enero",
	        "Febrero",
	        "Marzo",
	        "Abril",
	        "Mayo",
	        "Junio",
	        "Julio",
	        "Agosto",
	        "Septiembre",
	        "Octubre",
	        "Noviembre",
	        "Diciembre"};
	
	protected static String     NameOfMonthShort[]   = {"Ene",
	            "Feb",
	            "Mar",
	            "Abr",
	            "May",
	            "Jun",
	            "Jul",
	            "Ago",
	            "Sept",
	            "Oct",
	            "Nov",
	            "Dic"};
	
	protected static DateTime   instance        = null;
	
	public static DateTime getInstance(){
		if(instance == null){
			instance = new DateTime();
		}
		
		return instance;
	}
		
	private DateTime(){
		
	}
		
		
	public String reformatDate(String _oldDate){
		String dateSplit[] = _oldDate.split("\\-");		
		int _mes = -1;		
		for(int i=0;i<NameOfMonthShort.length;i++){
			if(dateSplit[1].equals(NameOfMonthShort[i].toUpperCase())){
				_mes = i+1;
			}
		}
		if(_mes < 10){
			return "20"+dateSplit[2]+"-"+"0"+_mes+"-"+dateSplit[0];
		}
		else{
			return "20"+dateSplit[2]+"-"+_mes+"-"+dateSplit[0];	
		}		
	}
	
	public String GetFecha(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df1.format(c.getTime());
		return formattedDate;
	}
	
	
	public String DateWithNameMonth(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate[] = df1.format(c.getTime()).split("-");
		return NameOfMonth[Integer.parseInt(formattedDate[1])]+" "+formattedDate[0]+"-"+formattedDate[2];
	}
	
	public String DateWithNameMonthShort() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate[] = df1.format(c.getTime()).split("-");
		return NameOfMonthShort[Integer.parseInt(formattedDate[1])-1]+" "+formattedDate[0]+" de "+formattedDate[2];
	}
	
	public String GetHora(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss a");
		String formattedDate = df1.format(c.getTime());
		return formattedDate;
	}
	
	public String GetDateTimeHora(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String formattedDate = df1.format(c.getTime());
		return formattedDate;
	}
}
