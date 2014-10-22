package adaptadores;

public class DetalleEightItems {
	protected String 	item1;
	protected String 	item2;
	protected String 	item3;
	protected String 	item4;
	protected String 	item5;
	protected String 	item6;
	protected String 	item7;
	protected String 	item8;
	protected String 	item9;
	protected long 		Id;
	
	public DetalleEightItems(String _item1, String _item2, String _item3, String _item4, String _item5, String _item6, String _item7, String _item8, String _item9){
		super();
		this.item1 	= _item1;
		this.item2 	= _item2;
		this.item3 	= _item3;
		this.item4 	= _item4;
		this.item5 	= _item5;
		this.item6 	= _item6;
		this.item7 	= _item7;
		this.item8 	= _item8;
		this.item9 	= _item9;
	}
	
	public String getItem1(){
		return this.item1;
	}
	
	public String getItem2(){
		return this.item2;
	}
	
	public String getItem3(){
		return this.item3;
	}
	
	public String getItem4(){
		return this.item4;
	}
	
	public String getItem5(){
		return this.item5;
	}
	
	public String getItem6(){
		return this.item6;
	}
	
	public String getItem7(){
		return this.item7;
	}
	
	public String getItem8(){
		return this.item8;
	}
	
	public String getItem9(){
		return this.item9;
	}
	
	public long getId(){
		return this.Id;
	}
	
	public void setItem1(String _item){
		this.item1 = _item;
	}
	
	public void setItem2(String _item){
		this.item2 = _item;
	}
	
	public void setItem3(String _item){
		this.item3 = _item;
	}
	
	public void setItem4(String _item){
		this.item4 = _item;
	}
	
	public void setItem5(String _item){
		this.item5 = _item;
	}
	
	public void setItem6(String _item){
		this.item6 = _item;
	}
	
	public void setItem7(String _item){
		this.item7 = _item;
	}
	
	public void setItem8(String _item){
		this.item8 = _item;
	}
	
	public void setItem9(String _item){
		this.item9 = _item;
	}
	
	public void setId(long id){
		this.Id= id;
	}
}
