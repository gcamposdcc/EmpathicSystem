package cl.automind.empathy.fw.data.sql;

import gcampos.dev.interfaces.structural.INamed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QueryDescriptor implements INamed{
	private Map<String, Collection<Integer>> paramPositions;
//	private Map<String, Sql.Type> typeMap;
	private String name;

	public QueryDescriptor(String name){
		this.name = name;
		this.paramPositions = new HashMap<String, Collection<Integer>>();
//		setTypeMap(new HashMap<String, Sql.Type>());
	}
	protected void setName(String name){
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	public void setParamPositions(Map<String,Collection<Integer>> paramPositions) {
		this.paramPositions = paramPositions;
	}
	public Map<String,Collection<Integer>> getParamPositions() {
		return paramPositions;
	}

	public boolean containsKey(String key){
		return getParamPositions().containsKey(key);
	}

	public void addParam(String paramName){
		if (getParamPositions().containsKey(paramName)) return;
		getParamPositions().put(paramName, new ArrayList<Integer>());
	}

	public void addParamInPosition(String paramName, int position){
		if (!getParamPositions().containsKey(paramName)) return;
		Collection<Integer> positions =  getParamPositions().get(paramName);
		if(positions.contains(position)) return;
		positions.add(position);
	}

	public Collection<Integer> getParamPosition(String paramName){
		if (!getParamPositions().containsKey(paramName)) return Collections.emptyList();
		return Collections.unmodifiableCollection(getParamPositions().get(paramName));
	}
//	public void setTypeMap(Map<String, Sql.Type> typeMap) {
//		this.typeMap = typeMap;
//	}
//	public Map<String, Sql.Type> getTypeMap() {
//		return typeMap;
//	}

}
