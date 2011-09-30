package cl.automind.empathy.test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MethodCache {
	private final Map<String, Map<String, Method>> classMap;
	private static volatile MethodCache instance;
	private MethodCache(){
		classMap = initializeMap();
		classes = new Class[]{};
	}
	private Map<String, Map<String, Method>> initializeMap() {
		return new Hashtable<String, Map<String,Method>>();
	}
	public static MethodCache getInstance(){
		if (instance == null) instance = new MethodCache();
		return instance;
	}
	private final Class[] classes;
	public <T> Method getMethod(Class<T> clazz, String methodname){
		String classname = clazz.getCanonicalName();
		if(!classMap.containsKey(classname)) {
			classMap.put(classname, new HashMap<String, Method>());
		}
		if (!classMap.get(classname).containsKey(methodname)) {
			try {
				Method m = clazz.getMethod(methodname, classes);
				classMap.get(classname).put(methodname, m);
			} catch (Exception e){
				return null;
			}
		}
		return classMap.get(classname).get(methodname);
	}

	public <T> Method getMethod(String classname, String methodname){
		if(!classMap.containsKey(classname)) {
			classMap.put(classname, new HashMap<String, Method>());
		}
		if (!classMap.get(classname).containsKey(methodname)) {
			try {
				Method m = Class.forName(classname).getMethod(methodname, classes);
				classMap.get(classname).put(methodname, m);
			} catch (Exception e){
				return null;
			}
		}
		return classMap.get(classname).get(methodname);
	}
}
