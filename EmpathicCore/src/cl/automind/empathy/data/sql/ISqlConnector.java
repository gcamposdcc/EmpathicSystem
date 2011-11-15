package cl.automind.empathy.data.sql;

import java.sql.PreparedStatement;
import java.sql.Statement;

public interface ISqlConnector {
	public ISqlConnectionInfo getConnectionInfo();
	public void openConnection();
	public void closeConnection();
	public Statement getStatement(String statement);
	public PreparedStatement preparedStatement(String statement);
}
