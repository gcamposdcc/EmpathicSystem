package gcampos.dev.patterns.creational;

import java.util.Map;

import gcampos.dev.interfaces.structural.INamed;

public abstract class FlyweightNamedFactory<T> implements NamedFactory<T>{
	private Map<String, T> registry;
	
	public FlyweightNamedFactory(){
		registry = initializeMap();
	}
	
	public abstract Map<String, T> initializeMap();
	
	protected Map<String, T> getRegistry(){
		return registry;
	}
	
	@Override
	public <TI extends INamed> T createElement(TI param) {
		return isValidName(param)? getRegistry().get(param.getName()): null;
	}
	@Override
	public T createElement(String name){
		return isValidName(name)? getRegistry().get(name): null;
	}

	public void registerElement(INamed key, T value) {
		if (isValidName(key)) getRegistry().put(key.getName(),value);
	}
	public void registerElement(String name, T value) {
		if (isValidName(name)) getRegistry().put(name,value);
	}

	@Override
	public boolean isValidName(INamed name) {
		if(name == null) return false;
		return isValidName(name.getName());
	}
	@Override
	public boolean isValidName(String name){
		if(name == null) return false;
		if(name.length() == 0) return false;
		return true;
	}
	protected String fixName(String s){
		return s.replace("\t", " ").replace("\n\r", " ").replace("\n", " ").replace(" ","_");
	}
	
	public void printElements(){
		for(Map.Entry<String, T> entry: getRegistry().entrySet()){
			System.out.println("KEY::"+entry.getKey()+";;VALUE::+"+entry.getValue());
		}
	}
	
	public boolean elementRegistered(String name){
		return getRegistry().containsKey(name);
	}
	public boolean elementRegistered(INamed named){
		return getRegistry().containsKey(named.getName());
	}
	
}
