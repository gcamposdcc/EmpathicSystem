package cl.automind.empathy.fwdesk.data.sql;

import cl.automind.empathy.fw.data.sql.DefaultSqlConnector;

public class HsqldbConnector extends DefaultSqlConnector {

	public HsqldbConnector() {
		super(new HsqldbConnectionInfo());
	}

}
