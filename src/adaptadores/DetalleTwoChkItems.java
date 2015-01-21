package adaptadores;

public class DetalleTwoChkItems{
	protected long 		Id;
	protected boolean 	estado;
	protected String 	check1;
	protected String 	item1;

	
	public DetalleTwoChkItems(boolean _estado, String _check1, String _item1){
		super();
		this.estado = _estado;
		this.check1	= _check1;
		this.item1 	= _item1;
	}
	
	public boolean getEstadoCheck1(){
		return this.estado;
	}
	
	public void setEstadoCheck1(boolean _estado){
		this.estado = _estado;
	}
	
	
	public String getTextCheck1(){
		return this.check1;
	}
	
	public void setTextCheck1(String _check1){
		this.check1 = _check1;
	}
	
	
	public String getItem1(){
		return this.item1;
	}
	
	public void setItem1(String _item){
		this.item1 = _item;
	}
	
	public long getId(){
		return this.Id;
	}
		
	public void setId(long id){
		this.Id= id;
	}
}
