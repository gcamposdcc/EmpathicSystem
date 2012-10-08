package gcampos.dev.net;

import java.util.ArrayList;
import java.util.List;

public class Escaper {

	private final List<EscapablePair> escapables;
	private final static Escaper instance = new Escaper();
	private Escaper(){
		escapables = new ArrayList<EscapablePair>();
//		put("%", "%25");
//		put(" ", "%20");
		put("#", "%23");
		put("\\$", "%24");
//		put("&", "%26");
		put("@", "%40");
		put("`", "%60");
		put("/", "%2F");
		put(":", "%3A");
		put(";", "%3B");
		put("<", "%3C");
//		put("=", "%3D");
		put(">", "%3E");
		put("\\?", "%3F");
		put("\\[", "%5B");
		put("\\\\", "%5C");
		put("\\]", "%5D");
		put("\\^", "%5E");
		put("\\{", "%7B");
		put("\\|", "%7C");
		put("\\}", "%7D");
		put("~", "%7E");
		put("\"", "%22");
		put("'", "%27");
		put("\\+", "%2B");
		put(",", "%2C");
//		put("¡", "%A1");
	}
	public void put(String key, String value){
		escapables.add(new EscapablePair(key, value));
	}
	public static String escape(String toEscape){
		String escaped = toEscape;
		for(EscapablePair escapableEntry : instance.escapables){
			escaped = escaped.replaceAll(escapableEntry.getKey(), escapableEntry.getValue());
		}
		return escaped;
	}
	private static class EscapablePair{
		private String key;
		private String value;
		EscapablePair(String key, String value){
			setKey(key);
			setValue(value);
		}
		private String getKey() {
			return key;
		}
		private void setKey(String key) {
			this.key = key;
		}
		private String getValue() {
			return value;
		}
		private void setValue(String value) {
			this.value = value;
		}
	}
}
