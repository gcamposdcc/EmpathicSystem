package cl.automind.empathy.data.sql;

import gcampos.dev.util.Property;

import java.util.Properties;


public interface ISqlConnectionInfo {
	String getUsername();
	String getPassword();
	String getHostname();
	String getDatabase();
	String getDriver();
	String getDriverClassname();
	String getPort();
	Property[] getAdditionalConfig();
	Properties asProperties();

	public enum Fields {
		Username {@Override public String toString() {return "user";} },
		Password {@Override public String toString() {return "password";} },
		Hostname, Database, Driver, DriverClassname, Port
	}
}
