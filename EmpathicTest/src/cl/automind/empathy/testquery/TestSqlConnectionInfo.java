package cl.automind.empathy.testquery;

import gcampos.dev.util.Property;
import cl.automind.empathy.fw.data.sql.AbstractSqlConnectionInfo;

public class TestSqlConnectionInfo extends AbstractSqlConnectionInfo {



	@Override
	public String getUsername() {
		return "user";
	}

	@Override
	public String getPassword() {
		return "pass";
	}

	@Override
	public String getHostname() {
		return "host";
	}

	@Override
	public String getDatabase() {
		return "database";
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
	public String getPort() {
		return "8000";
	}
	private final static Property[] properties = new Property[0];
	@Override
	public Property[] getAdditionalConfig() {
		return properties;
	}


}
