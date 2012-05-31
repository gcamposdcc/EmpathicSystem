package cl.automind.empathy.fw.data.sql;

import gcampos.dev.util.Property;
import gcampos.dev.util.Strings;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import cl.automind.empathy.data.sql.ISqlConnectionInfo;
import cl.automind.empathy.data.sql.ISqlConnector;
import cl.automind.empathy.data.sql.ISqlTranslator;

public class DefaultSqlConnector implements ISqlConnector{

	private final ISqlConnectionInfo connectionInfo;
	private Driver driver;
	private Connection connection;
	private ISqlTranslator sqlTranslator;
	
	public DefaultSqlConnector(ISqlConnectionInfo connectionInfo){
		this(connectionInfo, new DefaultSqlTranslator());
	}
	
	public DefaultSqlConnector(ISqlConnectionInfo connectionInfo, ISqlTranslator translator){
		this.connectionInfo = connectionInfo;
		setSqlTranslator(translator);
		try {
			this.driver = DriverManager.getDriver(getDriverString());
		} catch (SQLException e){
			try {
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Loading::"+getConnectionInfo().getDriverClassname());
				Class.forName(getConnectionInfo().getDriverClassname()).newInstance();
				this.driver = DriverManager.getDriver(getDriverString());
			} catch (ClassNotFoundException se) {
				e.printStackTrace();
			} catch (SQLException se) {
				e.printStackTrace();
			} catch (InstantiationException se) {
				e.printStackTrace();
			} catch (IllegalAccessException se) {
				e.printStackTrace();
			}
		}
		openConnection();
	}
	@Override
	public ISqlConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public Driver getDriver() {
		return driver;
	}
	@Override
	public void openConnection() {
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("OpeningConnection");
		try {
			if (isConnected()) closeConnection();
			String args = "";
			for (Property p : getConnectionInfo().getAdditionalConfig()){
				args += p.getKey() + "=" + p.getValue() + "&";
			}
			if (args.length() > 0){
				args = args.substring(0, args.length()-1);
				args = "?" + args;
			}
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(getFullConnectionString());
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(getConnectionInfo().getUsername()+"::"+getConnectionInfo().getPassword());
			setConnection(DriverManager.getConnection(
					getFullConnectionString(), getConnectionInfo().getUsername(), getConnectionInfo().getPassword()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void closeConnection() {
		if (getConnection() == null) return;
		else {
			try {
				getConnection().commit();
				getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public final String getDriverString(){
		return "jdbc:"+getConnectionInfo().getDriver()+":";
	}
	public final String getUrlString(){
		return
			getConnectionInfo().getHostname() +
			(Strings.isNullOrEmpty(getConnectionInfo().getPort()) ? "" : ":"+getConnectionInfo().getPort() + "/")
			+ getConnectionInfo().getDatabase();
	}
	public String getFullConnectionString(){
		String urlString = getUrlString();
		return getDriverString() + (Strings.isNullOrEmpty(urlString)? "" : "//" + getUrlString());
	}
	protected void setConnection(Connection connection) {
		this.connection = connection;
	}
	protected Connection getConnection() {
		return connection;
	}

	@Override
	public Statement createStatement(){
		try {
			Connection conn = getConnection();
			if (conn == null) {
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Connection in null");
			}
			return getConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String statement) {
		try {
			Connection conn = getConnection();
			if (conn == null) {
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Connection is null");
			}
			return getConnection().prepareStatement(statement);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean isConnected() {
		return getConnection() != null;
	}
	public void setSqlTranslator(ISqlTranslator sqlTranslator){
		this.sqlTranslator = sqlTranslator;
	}
	@Override
	public ISqlTranslator getTranslator() {
		return sqlTranslator;
	}
}
