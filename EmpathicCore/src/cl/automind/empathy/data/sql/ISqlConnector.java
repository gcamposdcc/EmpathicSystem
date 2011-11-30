package cl.automind.empathy.data.sql;

import java.sql.PreparedStatement;
import java.sql.Statement;

public interface ISqlConnector {
	public ISqlConnectionInfo getConnectionInfo();
	public boolean isConnected();
	public void openConnection();
	public void closeConnection();
	public Statement getStatement();
	public PreparedStatement preparedStatement(String statement);
}
