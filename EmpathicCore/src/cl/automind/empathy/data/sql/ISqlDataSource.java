package cl.automind.empathy.data.sql;

import java.sql.ResultSet;
import java.util.List;

import cl.automind.empathy.data.IDataSource;

public interface ISqlDataSource<T> extends IDataSource<T> {
	public ISqlConnector getConnector();
	public List<T> executeNamedQuery(String queryName, SqlNamedValuePair<?>... constrains);
	public List<T> parse(ResultSet rs);
}
