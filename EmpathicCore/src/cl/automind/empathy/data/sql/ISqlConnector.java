package cl.automind.empathy.data.sql;

import java.sql.PreparedStatement;
import java.sql.Statement;

public interface ISqlConnector {
	ISqlConnectionInfo getConnectionInfo();
	boolean isConnected();
	void openConnection();
	void closeConnection();
	Statement createStatement();
	PreparedStatement prepareStatement(String statement);
	ISqlTranslator getTranslator();
}
