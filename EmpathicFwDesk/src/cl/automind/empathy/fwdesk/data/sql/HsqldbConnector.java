package cl.automind.empathy.fwdesk.data.sql;

import java.sql.Statement;

import cl.automind.empathy.fw.data.sql.AbstractSqlConnector;

public class HsqldbConnector extends AbstractSqlConnector {

	public HsqldbConnector() {
		super(new HsqldbConnectionInfo());
	}

	@Override
	public Statement getStatement(String statement) {
		return null;
	}

}
