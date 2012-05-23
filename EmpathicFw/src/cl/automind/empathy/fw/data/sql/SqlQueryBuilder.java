package cl.automind.empathy.fw.data.sql;

import java.util.List;
import java.util.Map;
import cl.automind.empathy.data.sql.SqlType;

public class SqlQueryBuilder {

	private AbstractSqlDataSource<?> dataSource;

	public AbstractSqlDataSource<?> getDataSource() {
		return dataSource;
	}

	public void setDataSource(AbstractSqlDataSource<?> dataSource) {
		this.dataSource = dataSource;
	}
	
	public SqlQueryBuilder(AbstractSqlDataSource<?> dataSource){
		setDataSource(dataSource);
	}
	public String buildCreateQuery(String idName, boolean hasId, Map<String, ColumnDescriptor> columnDescriptorMap){
		String query = "";
		query += "CREATE TABLE " + getDataSource().getName() + " (";
		for (ColumnDescriptor columnDescriptor : columnDescriptorMap.values()){
			query += columnDescriptor.getName() + " ";
			query += getDataSource().getConnector().getTranslator().toSqlType(columnDescriptor.getType());
			query += columnDescriptor.getType() == SqlType.VARCHAR ? "(" + columnDescriptor.getLength() + ")" + " " : " ";
			query += columnDescriptor.isNullable() ? "" : "NOT NULL";
			query += ", ";
		}
		query = query.substring(0, query.length()-1);
		query += ");";
		System.out.println(query);
		return query;
	}
	public String buildUpdateByIdQuery(String idName, List<String> fieldNames, int fieldCount) {
		String updateById = "UPDATE " + getDataSource().getName() + " SET ";
		if (fieldCount > 0) {
			for (int i = 0; i < fieldCount; i++){
				updateById += fieldNames.get(i) + " =" + ((i == fieldCount - 1) ? " ? " : " ?, ");
				getDataSource().getDescriptor().addParamToQuery("updateById", fieldNames.get(i), i + 1);
			}
			updateById += " WHERE " + idName + " = ?;";
			getDataSource().getDescriptor().addParamToQuery("updateById", idName, fieldCount + 1);
		}
		return updateById;
	}

	public String buildSelectQuery(List<String> fieldNames, int fieldCount) {
		String query = "SELECT * FROM " + getDataSource().getName() + " WHERE ";
		if (fieldCount > 0) {
			SqlType fieldType = SqlType.NONE;
			for (int i = 0; i < fieldCount; i++){
				fieldType = getDataSource().getDescriptor().getColumnDescriptorMap().get(fieldNames.get(i)).getType();
				query += fieldNames.get(i) + " ";
				query += (fieldType == SqlType.VARCHAR || fieldType == SqlType.TEXT)? "LIKE" : "=";
				query += ((i == fieldCount - 1) ? " ? " : " ? AND ");
				getDataSource().getDescriptor().addParamToQuery("select", fieldNames.get(i), i + 1);
			}
		}
		query += ";";
		return query;
	}
	
	public String buildDeleteQuery(List<String> fieldNames, int fieldCount) {
		String query = "DELETE FROM " + getDataSource().getName() + " WHERE ";
		if (fieldCount > 0) {
			SqlType fieldType = SqlType.NONE;
			for (int i = 0; i < fieldCount; i++){
				fieldType = getDataSource().getDescriptor().getColumnDescriptorMap().get(fieldNames.get(i)).getType();
				query += fieldNames.get(i) + " ";
				query += (fieldType == SqlType.VARCHAR || fieldType == SqlType.TEXT)? "LIKE" : "=";
				query += ((i == fieldCount - 1) ? " ? " : " ? AND ");
				getDataSource().getDescriptor().addParamToQuery("select", fieldNames.get(i), i + 1);
			}
		}
		query += ";";
		return query;
	}

	public String buildUpdateCustomQuery(List<String> fieldNames, int fieldCount) {
		String query = "UPDATE " + getDataSource().getName() + " SET ";
		if (fieldCount > 0) {
			for (int i = 0; i < fieldCount; i++){
				query += fieldNames.get(i) + " =" + ((i == fieldCount - 1) ? " ? " : " ?, ");
				getDataSource().getDescriptor()
				.addParamToQuery("update", AbstractSqlDataSource.UPDATE_OLD_FIELD_PREFIX+fieldNames.get(i), i + 1);
			}
		}
		return query;
	}
	
	public String buildUpdateQuery(List<String> fieldNames, int fieldCount) {
		String query = "UPDATE " + getDataSource().getName() + " SET ";
		if (fieldCount > 0) {
			for (int i = 0; i < fieldCount; i++){
				query += fieldNames.get(i) + " =" + ((i == fieldCount - 1) ? " ? " : " ?, ");
				getDataSource().getDescriptor()
				.addParamToQuery("update", AbstractSqlDataSource.UPDATE_OLD_FIELD_PREFIX+fieldNames.get(i), i + 1);
			}
		}
		query += " WHERE ";
		if (fieldCount > 0) {
			SqlType fieldType = SqlType.NONE;
			for (int i = 0; i < fieldCount; i++){
				fieldType = getDataSource().getDescriptor().getColumnDescriptorMap().get(fieldNames.get(i)).getType();
				query += fieldNames.get(i) + " ";
				query += (fieldType == SqlType.VARCHAR || fieldType == SqlType.TEXT)? "LIKE" : "=";
				query += ((i == fieldCount - 1) ? " ? " : " ? AND ");
				getDataSource().getDescriptor().addParamToQuery("update", fieldNames.get(i), i + 1);
			}
		}
		query += ";";
		return query;
	}

	public String buildInsertQuery(List<String> fieldNames, int fieldCount, String idName, boolean hasId) {
		String insert = "INSERT INTO "+ getDataSource().getName() + " ";
		if (fieldCount > 0 ) {
			for (int i = 0; i < fieldCount; i++){
				insert += (i == 0 ? "(" : ", ") + fieldNames.get(i);
				getDataSource().getDescriptor().addParamToQuery("insert", fieldNames.get(i), i + 1);
			}
			insert += ") VALUES (";
			for (int i = 0; i < fieldCount; i++){
				insert += (i == fieldCount - 1) ? "?" : "?, ";
			}
			insert += ")";
			if (hasId) insert += " RETURNING " + idName;
			insert += ";";
		}
		return insert;
	}

}
