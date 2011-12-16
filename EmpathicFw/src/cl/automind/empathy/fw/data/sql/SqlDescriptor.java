package cl.automind.empathy.fw.data.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlDescriptor {
	private Map<String, QueryDescriptor> queryDescriptorsMap;
	private String idName;
	private List<String> fieldNames;

	public SqlDescriptor(){
		setQueryPositionsMap(new HashMap<String, QueryDescriptor>());
		setIdName("id");
		setFieldNames(new ArrayList<String>());
	}
	public void addQuery(String queryName){
		if (getQueryDescriptorsMap().containsKey(queryName)) return;
		getQueryDescriptorsMap().put(queryName, new QueryDescriptor(queryName));
	}
	public void addParamToQuery(String queryName, String paramName){
		addQuery(queryName);
		getQueryDescriptorsMap().get(queryName).addParam(paramName);
	}
	public void addParamToQuery(String queryName, String paramName, int position){
		addQuery(queryName);
		getQueryDescriptorsMap().get(queryName).addParam(paramName);
		getQueryDescriptorsMap().get(queryName).addParamInPosition(paramName, position);
	}
	public Collection<Integer> getParamPosition(String queryName, String paramName){
		if (getQueryDescriptorsMap().containsKey(queryName)){
			return getQueryDescriptorsMap().get(queryName).getParamPosition(paramName);
		}
		return Collections.emptyList();
	}
	private void setQueryPositionsMap(Map<String, QueryDescriptor> queryDescriptorsMap) {
		this.queryDescriptorsMap = queryDescriptorsMap;
	}

	private Map<String, QueryDescriptor> getQueryDescriptorsMap() {
		return queryDescriptorsMap;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public String getIdName() {
		return idName;
	}
	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}
	public List<String> getFieldNames() {
		return fieldNames;
	}
	public QueryDescriptor getQueryDescriptor(String queryName){
		return getQueryDescriptorsMap().get(queryName);
	}
}
