package cl.automind.empathy.fw.data.sql;

import gcampos.dev.util.Strings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.automind.empathy.data.sql.Column;
import cl.automind.empathy.data.sql.Id;
import cl.automind.empathy.data.sql.NamedQuery;
import cl.automind.empathy.data.sql.SqlMetadata;
import cl.automind.empathy.data.sql.SqlType;

public class SqlClassExtractor<T> {

	private AbstractSqlDataSource<T> dataSource;

	public AbstractSqlDataSource<T> getDataSource() {
		return dataSource;
	}

	public void setDataSource(AbstractSqlDataSource<T> dataSource) {
		this.dataSource = dataSource;
	}
	
	public SqlClassExtractor(AbstractSqlDataSource<T> dataSource){
		setDataSource(dataSource);
	}
	
	public void extractTemplateData(T template, Class<?> templateClass) {
		String idName = "id";
		String tempFieldName = "";
		List<String> fieldNames = new ArrayList<String>();
		Map<String, ColumnDescriptor> columnDescriptorMap = new HashMap<String, ColumnDescriptor>();
		boolean hasId = false;
		if (template != null){
			Column columnMetadata = null;
			Id idMetadata = null;
			for(Field field: templateClass.getDeclaredFields()){
				columnMetadata = field.getAnnotation(Column.class);
				if (columnMetadata != null){
					tempFieldName = columnMetadata.name().trim().equals("") ? field.getName() : columnMetadata.name().trim();
					if (fieldNames.contains(tempFieldName)) continue;
					fieldNames.add(tempFieldName);
					SqlType fieldType = columnMetadata.type() != SqlType.NONE ? columnMetadata.type() : SqlType.inferSqlType(field.getType());
					columnDescriptorMap.put(
							tempFieldName, 
							new ColumnDescriptor(tempFieldName, fieldType, columnMetadata.length(), columnMetadata.nullable()));
				} else {
					idMetadata = field.getAnnotation(Id.class);
					if (idMetadata != null){
						idName = idMetadata.name().trim().equals("") ? field.getName() : idMetadata.name().trim();
						hasId = true;
						columnDescriptorMap.put(idName, new ColumnDescriptor(idName, SqlType.INTEGER, false));
					}
				}
			}
			getDataSource().getDescriptor().setIdName(idName);
			getDataSource().getDescriptor().setUsesId(hasId);
			getDataSource().getDescriptor().getFieldNames().addAll(fieldNames);
			getDataSource().getDescriptor().setColumnDescriptorMap(columnDescriptorMap);
		}
		buildQueries(getDataSource());
	}
	public void buildQueries(AbstractSqlDataSource<T> ds){
		String idName = ds.getName();
		boolean hasId = ds.getDescriptor().getUsesId();
		List<String> fieldNames = ds.getDescriptor().getFieldNames();
		// BUILD QUERIES
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder(getDataSource());
		String create = queryBuilder.buildCreateQuery(idName, hasId, getDataSource().getDescriptor().getColumnDescriptorMap());
		getDataSource().getQueryMap().put("create", create);
		int fieldCount = fieldNames.size();
		// BY ID
		String updateById = queryBuilder.buildUpdateByIdQuery(idName, fieldNames, fieldCount);
		getDataSource().getQueryMap().put("selectById", "SELECT * FROM " + getDataSource().getName() + " WHERE "+ idName + " = ?;");
		getDataSource().getDescriptor().addParamToQuery("selectById", idName, 1);
		getDataSource().getQueryMap().put("deleteById", "DELETE FROM " + getDataSource().getName() + " WHERE "+ idName + " = ?;");
		getDataSource().getDescriptor().addParamToQuery("deleteById", idName, 1);
		getDataSource().getQueryMap().put("updataById", updateById);
		// NORMAL
		getDataSource().getQueryMap().put("insert", queryBuilder.buildInsertQuery(fieldNames, fieldCount, idName, hasId));
		getDataSource().getQueryMap().put("update", queryBuilder.buildUpdateQuery(fieldNames, fieldCount));
		getDataSource().getQueryMap().put("delete", queryBuilder.buildDeleteQuery(fieldNames, fieldCount));
		getDataSource().getQueryMap().put("select", queryBuilder.buildSelectQuery(fieldNames, fieldCount));
		getDataSource().getQueryMap().put("count", "SELECT count(*) FROM " + getDataSource().getName() + ";");
		getDataSource().getQueryMap().put("selectAll", "SELECT * FROM " + getDataSource().getName() + ";");
		// CUSTOM
		getDataSource().getQueryMap().put("updateCustom", queryBuilder.buildUpdateCustomQuery(fieldNames, fieldCount));
		getDataSource().getQueryMap().put("deleteCustom", "DELETE FROM " + getDataSource().getName());
		getDataSource().getQueryMap().put("selectCustom", "SELECT * FROM " + getDataSource().getName());
	}

	public static String extractDataSourceName(SqlMetadata metadata, Class<?> templateClass, Class<?> thisClass) {
		String name = "";
		if (metadata != null){
			name = metadata.name().trim().equals("") ? Strings.englishPlural(templateClass.getSimpleName()) : metadata.name().trim();
		} else {
			name = Strings.englishPlural(templateClass.getSimpleName()).toLowerCase();
		}
		return name;
	}
	public void extractClassMetadata(SqlMetadata metadata){
		if (metadata != null) {
			if(metadata.namedQueries() != null){
				for (NamedQuery query: metadata.namedQueries()){
					getDataSource().getQueryMap().put(query.name(), query.query());
				}
			}
		}
	}
	
}
