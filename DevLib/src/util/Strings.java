package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Strings {
	public static boolean isNullOrEmpty(String s){
		return (s == null) ? true : s.length() == 0;
	}
	private static Map<String, String> specialEnglishPlurals;
	static {
		specialEnglishPlurals = new ConcurrentHashMap<String, String>();
		specialEnglishPlurals.put("canto", "cantos");
		specialEnglishPlurals.put("homo", "homos");
		specialEnglishPlurals.put("photo", "photos");
		specialEnglishPlurals.put("zero", "zeros");
		specialEnglishPlurals.put("piano", "pianos");
		specialEnglishPlurals.put("portico", "porticos");
		specialEnglishPlurals.put("pro", "pros");
		specialEnglishPlurals.put("quarto", "quartos");
		specialEnglishPlurals.put("kimono", "kimonos");
	}
	public static String englishPlural(String s){
		if (specialEnglishPlurals.containsKey(s)){
			return specialEnglishPlurals.get(s);
		} else if (s.endsWith("s") || s.endsWith("x") || s.endsWith("o") || s.endsWith("sh") || s.endsWith("ch")){
			return s + "es";
		} else if (s.endsWith("y")){
			if (s.endsWith("ay") || s.endsWith("ey") || s.endsWith("iy") || s.endsWith("oy") || s.endsWith("uy")) {
				return s + "s";
			} else {
				return s.substring(0,s.lastIndexOf("y")) + "ies";
			}
		} else if (s.endsWith("ife")){
			return s.substring(0,s.lastIndexOf("ife")) + "ives";
		} else if (s.endsWith("lf")){
			return s.substring(0,s.lastIndexOf("lf")) + "lves";
		}
		return s + "s";
	}
}