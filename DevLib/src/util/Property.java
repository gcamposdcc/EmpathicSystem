package util;

public class Property{
	private final String key;
	private String value;
	public Property(String key){
		this.key = key;
	}
	public String getKey(){
		return key;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}