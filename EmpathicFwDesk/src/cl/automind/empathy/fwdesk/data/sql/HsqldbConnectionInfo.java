package cl.automind.empathy.fwdesk.data.sql;

import util.Property;
import cl.automind.empathy.fw.data.sql.AbstractSqlConnectionInfo;

public class HsqldbConnectionInfo extends AbstractSqlConnectionInfo{
	public HsqldbConnectionInfo(){
		super();
	}
	@Override
	public String getUsername() {
		return "";
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getHostname() {
		return "";
	}

	@Override
	public String getDatabase() {
		return "";
	}

	@Override
	public String getDriver() {
		return "hsqldb:mem";
	}

	@Override
	public String getDriverClassname() {
		return "org.hsqldb.jdbcDriver";
	}

	@Override
	public String getDatabaseUrl() {
		return "";
	}

	@Override
	public String getPort() {
		return "";
	}
	private final static Property[] additionalConfig = {};
	@Override
	public Property[] getAdditionalConfig() {
		return additionalConfig;
	}

}
