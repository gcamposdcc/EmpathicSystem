package cl.automind.empathy.data.sql;

import java.util.Properties;

import util.Property;

public interface ISqlConnectionInfo {
	public String getUsername();
	public String getPassword();
	public String getHostname();
	public String getDatabase();
	public String getDriver();
	public String getDriverClassname();
	public String getDatabaseUrl();
	public String getPort();
	public Property[] getAdditionalConfig();
	public Properties asProperties();

	public enum Fields {
		Username {@Override public String toString() {return "user";} },
		Password {@Override public String toString() {return "password";} },
		Hostname, Database, Driver, DriverClassname, DatabaseUrl, Port
	}
}
