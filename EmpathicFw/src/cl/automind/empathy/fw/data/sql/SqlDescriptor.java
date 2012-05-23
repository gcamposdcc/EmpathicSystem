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
	private boolean usesId;
	private List<String> fieldNames;
	private Map<String, ColumnDescriptor> columnDescriptorMap;

	public SqlDescriptor(){
		setUsesId(false);
		setIdName("id");
		setQueryDescriptorsMap(new HashMap<String, QueryDescriptor>());
		setFieldNames(new ArrayList<String>());
		setColumnDescriptorMap(new HashMap<String, ColumnDescriptor>());
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
	protected void setQueryDescriptorsMap(Map<String, QueryDescriptor> queryDescriptorsMap) {
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
	public Map<String, ColumnDescriptor> getColumnDescriptorMap() {
		return columnDescriptorMap;
	}
	public void setColumnDescriptorMap(Map<String, ColumnDescriptor> columnDescriptorMap) {
		this.columnDescriptorMap = columnDescriptorMap;
	}
	public boolean getUsesId() {
		return usesId;
	}
	public void setUsesId(boolean usesId) {
		this.usesId = usesId;
	}
}
