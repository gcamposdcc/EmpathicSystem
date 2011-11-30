package cl.automind.empathy.fw.data.sql;

import java.util.Properties;

import cl.automind.empathy.data.sql.ISqlConnectionInfo;

import util.Property;

public class PropertySqlConnectionInfo extends AbstractSqlConnectionInfo{

	public PropertySqlConnectionInfo(Properties properties){
		setProperties(properties);
	}

	@Override
	public String getUsername() {
		return getProperties()
		.getProperty(ISqlConnectionInfo.Fields.Username.toString(), "");
	}

	@Override
	public String getPassword() {
		return getProperties()
		.getProperty(ISqlConnectionInfo.Fields.Password.toString(), "");
	}

	@Override
	public String getHostname() {

		return getProperties()
		.getProperty(ISqlConnectionInfo.Fields.Hostname.toString(), "");
	}

	@Override
	public String getDatabase() {

		return getProperties()
		.getProperty(ISqlConnectionInfo.Fields.Database.toString(), "");
	}

	@Override
	public String getDriver() {

		return getProperties()
		.getProperty(ISqlConnectionInfo.Fields.Driver.toString(), "");
	}

	@Override
	public String getDriverClassname() {
		return getProperties()
		.getProperty(ISqlConnectionInfo.Fields.DriverClassname.toString(), "");
	}

	@Override
	public String getDatabaseUrl() {
		return getProperties()
		.getProperty(ISqlConnectionInfo.Fields.DatabaseUrl.toString(), "");
	}

	@Override
	public String getPort() {
		return getProperties()
		.getProperty(ISqlConnectionInfo.Fields.Port.toString(), "");
	}

	@Override
	public Property[] getAdditionalConfig() {
		return new Property[]{};
	}

}
