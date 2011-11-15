package cl.automind.empathy.fwdesk.data.sql;

import java.sql.ResultSet;
import java.util.List;

import cl.automind.empathy.data.sql.ISqlConnector;
import cl.automind.empathy.fw.data.sql.AbstractSqlDataSource;

public class HsqldbDataSource<T> extends AbstractSqlDataSource<T> {

	public HsqldbDataSource(T template) {
		super(template);
	}

	@Override
	public ISqlConnector getConnector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> parse(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

}
