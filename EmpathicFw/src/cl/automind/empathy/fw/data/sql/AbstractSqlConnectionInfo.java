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
		getProperties().setProperty("username", getUsername());
		getProperties().setProperty("password", getPassword());
		getProperties().setProperty("database", getDatabase());
		getProperties().setProperty("host", getHostname());
		getProperties().setProperty("port", ""+getPort());
		getProperties().setProperty("driver", getDriver());
		getProperties().setProperty("driver_classname", getDriverClassname());
		getProperties().setProperty("url", getDatabaseUrl());
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
