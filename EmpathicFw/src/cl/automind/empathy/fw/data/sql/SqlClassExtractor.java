package cl.automind.empathy.fw.data.sql;

import gcampos.dev.util.Strings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

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
	public void extractTemplateData(){
		extractTemplateData(getDataSource());
	}
	@SuppressWarnings("unchecked")
	private void extractTemplateData(AbstractSqlDataSource<T> ds) {
		String idName = "id";
		String tempFieldName = "";
		T template = ds.getTemplate(); 
		Class<T> templateClass = (Class<T>) template.getClass();
		List<String> fieldNames = new ArrayList<String>();
		Map<String, ColumnDescriptor> columnDescriptorMap = new ConcurrentHashMap<String, ColumnDescriptor>();
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
							new ColumnDescriptor(tempFieldName, fieldType, columnMetadata.length(), true/*columnMetadata.nullable()*/));
					// FIXME support
				} else {
					idMetadata = field.getAnnotation(Id.class);
					if (idMetadata != null){
						idName = idMetadata.name().trim().equals("") ? field.getName() : idMetadata.name().trim();
						hasId = true;
						columnDescriptorMap.put(idName, new ColumnDescriptor(idName, SqlType.INTEGER, false));
					}
				}
			}
			ds.getDescriptor().setIdName(idName);
			ds.getDescriptor().setUsesId(hasId);
			ds.getDescriptor().setFieldNames(fieldNames);
			for (String fieldname: ds.getDescriptor().getFieldNames()){
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("FieldName::" + fieldname);
			}
//			ds.getDescriptor().getFieldNames().addAll(fieldNames);
			ds.getDescriptor().setColumnDescriptorMap(columnDescriptorMap);
		}
	}
	public void extractAndBuildQueries(){
		buildQueries(getDataSource());
	}
	private static void buildQueries(AbstractSqlDataSource<?> ds){		
		String idName = ds.getName();
		boolean hasId = ds.getDescriptor().getUsesId();
		List<String> fieldNames = ds.getDescriptor().getFieldNames();
		// BUILD QUERIES
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder(ds);
		String create = queryBuilder.buildCreateQuery(idName, hasId, ds.getDescriptor().getColumnDescriptorMap());
		ds.getQueryMap().put("create", create);
		int fieldCount = fieldNames.size();
		// BY ID
		String updateById = queryBuilder.buildUpdateByIdQuery(idName, fieldNames, fieldCount);
		ds.getQueryMap().put("selectById", "SELECT * FROM " + ds.getTablename() + " WHERE "+ idName + " = ?;");
		ds.getDescriptor().addParamToQuery("selectById", idName, 1);
		ds.getQueryMap().put("deleteById", "DELETE FROM " + ds.getTablename() + " WHERE "+ idName + " = ?;");
		ds.getDescriptor().addParamToQuery("deleteById", idName, 1);
		ds.getQueryMap().put("updataById", updateById);
		// NORMAL
		ds.getQueryMap().put("insert", queryBuilder.buildInsertQuery(fieldNames, fieldCount, idName, hasId));
		ds.getQueryMap().put("update", queryBuilder.buildUpdateQuery(fieldNames, fieldCount));
		ds.getQueryMap().put("delete", queryBuilder.buildDeleteQuery(fieldNames, fieldCount));
		ds.getQueryMap().put("select", queryBuilder.buildSelectQuery(fieldNames, fieldCount));
		ds.getQueryMap().put("count", "SELECT count(*) FROM " + ds.getTablename() + ";");
		ds.getQueryMap().put("selectAll", "SELECT * FROM " + ds.getTablename() + ";");
		ds.getQueryMap().put("drop", "DROP TABLE " + ds.getTablename() + ";");
		// CUSTOM
		ds.getQueryMap().put("updateCustom", queryBuilder.buildUpdateCustomQuery(fieldNames, fieldCount));
		ds.getQueryMap().put("deleteCustom", "DELETE FROM " + ds.getTablename());
		ds.getQueryMap().put("selectCustom", "SELECT * FROM " + ds.getTablename());
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

	public static String extractDataSourceSchemaName(SqlMetadata metadata) {
		String name = "";
		if (metadata != null){
			name = metadata.schema().trim();
		}
		return name;
	}

	public static boolean extractUsesDynamicName(SqlMetadata metadata) {
		boolean uses = false;
		if (metadata != null){
			uses = metadata.dynamicName();
		}
		return uses;
	}
	
	public void extractClassMetadata(){
		SqlMetadata metadata = getDataSource().getMetadata();
		if (metadata != null) {
			if(metadata.namedQueries() != null){
				for (NamedQuery query: metadata.namedQueries()){
					getDataSource().getQueryMap().put(
						query.name(), 
						getDataSource().usesDynamicName() ? 
								query.query().replaceAll("#DSNAME#", getDataSource().getTablename()) : 
								query.query());
				}
			}
		}
	}

	public void extractAll() {
		extractClassMetadata();
		extractTemplateData();
		extractAndBuildQueries();
	}
	
}
