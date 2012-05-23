package cl.automind.empathy.data.sql;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

import cl.automind.empathy.data.IDataSource;

public interface ISqlDataSource<T> extends IDataSource<T> {
	ISqlConnector getConnector();
	ResultSet executeCustomNamedQuery(String queryName, SqlNamedValuePair<?>... constrains);
	List<T> executeNamedQuery(String queryName, SqlNamedValuePair<?>... constrains);
	List<T> parse(ResultSet rs);
	Collection<SqlNamedValuePair<?>> toPairs(T target);
}
