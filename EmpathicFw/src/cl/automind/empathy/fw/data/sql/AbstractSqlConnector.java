package cl.automind.empathy.fw.data.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.Strings;

import cl.automind.empathy.data.sql.ISqlConnectionInfo;
import cl.automind.empathy.data.sql.ISqlConnector;

public abstract class AbstractSqlConnector implements ISqlConnector{

	private final ISqlConnectionInfo connectionInfo;
	private volatile Driver driver;
	private volatile Connection connection;
	public AbstractSqlConnector(ISqlConnectionInfo connectionInfo){
		this.connectionInfo = connectionInfo;
		try {
			setDriver(DriverManager.getDriver(getDriverString()));
		} catch (SQLException e){
			try {
				System.out.println("Loading::"+getConnectionInfo().getDriverClassname());
				Class.forName(getConnectionInfo().getDriverClassname()).newInstance();
				setDriver(DriverManager.getDriver(getDriverString()));
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
		try {
			if (getConnection() != null){
				closeConnection();
			}
			setConnection(getDriver().connect(getConnectionInfo().getDatabaseUrl(), getConnectionInfo().asProperties()));
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
	public String getDriverString(){
		return "jdbc:"+getConnectionInfo().getDriver()+":";
	}
	public String getUrlString(){
		return
			getConnectionInfo().getHostname() +
			(Strings.isNullOrEmpty(getConnectionInfo().getPort()) ? "" : ":"+getConnectionInfo().getPort() + "/")
			+ getConnectionInfo().getDatabase();
	}
	public String getFullConnectionString(){
		return getDriverString()+"//"+getUrlString();
	}
	protected void setConnection(Connection connection) {
		this.connection = connection;
	}
	protected Connection getConnection() {
		return connection;
	}

	@Override
	public PreparedStatement preparedStatement(String statement) {
		try {
			return getConnection().prepareStatement(statement);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
