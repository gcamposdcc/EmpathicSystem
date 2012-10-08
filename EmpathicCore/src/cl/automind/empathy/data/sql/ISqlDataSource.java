package cl.automind.empathy.data.sql;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

import cl.automind.empathy.data.IDataSource;

public interface ISqlDataSource<T> extends IDataSource<T> {
	ISqlConnector getConnector();
	int executeNamedUpdate(String queryName, SqlNamedValuePair<?>... constrains);
	ResultSet executeCustomNamedQuery(String queryName, SqlNamedValuePair<?>... constrains);
	List<T> executeNamedQuery(String queryName, SqlNamedValuePair<?>... constrains);
	List<T> parse(ResultSet rs);
	Collection<SqlNamedValuePair<?>> toPairs(T target);
	String getSchema();
	String getTablename();
}
