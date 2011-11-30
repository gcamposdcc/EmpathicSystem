package cl.automind.empathy.fw.data.sql;

import java.util.Properties;

import util.Property;

import cl.automind.empathy.data.sql.ISqlConnectionInfo;

public abstract class AbstractSqlConnectionInfo implements ISqlConnectionInfo {
	private volatile Properties properties;

	public AbstractSqlConnectionInfo(){
		updateProperties();
	}

	protected void updateProperties(){
		if (getProperties() == null) setProperties(new Properties());
		getProperties().setProperty(ISqlConnectionInfo.Fields.Username.toString(), getUsername());
		getProperties().setProperty(ISqlConnectionInfo.Fields.Password.toString(), getPassword());
		getProperties().setProperty(ISqlConnectionInfo.Fields.Database.toString(), getDatabase());
		getProperties().setProperty(ISqlConnectionInfo.Fields.Hostname.toString(), getHostname());
		getProperties().setProperty(ISqlConnectionInfo.Fields.Port.toString(), getPort());
		getProperties().setProperty(ISqlConnectionInfo.Fields.Driver.toString(), getDriver());
		getProperties().setProperty(ISqlConnectionInfo.Fields.DriverClassname.toString(), getDriverClassname());
		getProperties().setProperty(ISqlConnectionInfo.Fields.DatabaseUrl.toString(), getDatabaseUrl());
		for (Property p: getAdditionalConfig()){
			getProperties().setProperty(p.getKey(), p.getValue());
		}
	}

	@Override
	public Properties asProperties() {
		if (getProperties() == null) {
			updateProperties();
		}
		return getProperties();
	}
	protected void setProperties(Properties properties) {
		this.properties = properties;
	}
	protected Properties getProperties() {
		return properties;
	}

}