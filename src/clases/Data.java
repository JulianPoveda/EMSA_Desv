package clases;

public class Data {	
	 	  	  
	   public Data(String fecha, String solicitud, String dependencia, String inspector, String estado) {
	      super();
	      this.fecha = fecha;
	   }
	   
	   private String fecha, solicitud,dependencia,inspector,estado;	   
	 
	   @Override
	   public String toString() {
	      return "Data [fecha=" + fecha + ", solicitud=" + solicitud + ", inspector="
	            + inspector + ", estado=" + estado +",dependencia="+dependencia+"]";
	   }
	 
	}