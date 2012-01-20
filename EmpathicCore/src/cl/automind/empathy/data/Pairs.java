package cl.automind.empathy.data;

public abstract class Pairs {

	public enum Type {
		Boolean, Char, Date, Short, Integer, Long, Float, Double, String
	}


	static public NamedValuePair<Boolean> pair(String key, Boolean value){
		return new NamedValuePair<Boolean>(key, value);
	}
	static public NamedValuePair<java.sql.Date> pair(String key, java.sql.Date value){
		return new NamedValuePair<java.sql.Date>(key, value);
	}
	static public NamedValuePair<Short> pair(String key, Short value){
		return new NamedValuePair<Short>(key, value);
	}
	static public NamedValuePair<Integer> pair(String key, Integer value){
		return new NamedValuePair<Integer>(key, value);
	}
	static public NamedValuePair<Long> pair(String key, Long value){
		return new NamedValuePair<Long>(key, value);
	}
	static public NamedValuePair<Float> pair(String key, Float value){
		return new NamedValuePair<Float>(key, value);
	}
	static public NamedValuePair<Double> pair(String key, Double value){
		return new NamedValuePair<Double>(key, value);
	}
	static public NamedValuePair<String> pair(String key, String value){
		return new NamedValuePair<String>(key, value);
	}
}
