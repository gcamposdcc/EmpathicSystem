package cl.automind.empathy.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import cl.automind.empathy.model.EmpathyData;
import cl.automind.empathy.model.SagdeData;
import cl.automind.empathy.model.UserData;

public class RegisterMessageController {

	public void doTask(EmpathyData eData, UserData uData, SagdeData sData){
		int idEmpathy = insertEmpathyData(eData);
		insertUserData(idEmpathy, uData);
		insertSagdeData(idEmpathy, sData);
	}
	
	private int insertEmpathyData(EmpathyData data){
		//TODO implement query
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.warning("performingQuery");
		String queryString = 
			"INSERT INTO empathy " +
			"(created_at, idsource, idtype, evaluated, answered, liked) "+
			"VALUES (now(), ?, ? ,? , ?, ?) RETURNING id";
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/sagde", "carlos", "123456");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		if (connection != null) {
			try {
				PreparedStatement ps = connection.prepareStatement(queryString);
				ps.setInt(1, data.getIdSource());
				ps.setInt(2, data.getIdType());
				ps.setBoolean(3, data.isEvaluated());
				ps.setBoolean(4, data.isAnswered());
				ps.setBoolean(5, data.isLiked());
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					return rs.getInt(1);
				}
				ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Failed to make connection!");
		}
		return 0;
	}
	private void insertUserData(int idEmpathy, UserData data){
		//TODO implement query
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.warning("performingQuery");
		String queryString = 
			"INSERT INTO empathy_info_sagdeuser " +
			"(idempathy, idestablishment, idcourse, iduser) "+
			"VALUES (?, ?, ? ,?)";
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/sagde", "carlos", "123456");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		if (connection != null) {
			try {
				PreparedStatement ps = connection.prepareStatement(queryString);
				ps.setInt(1, idEmpathy);
				ps.setInt(2, data.getIdEstablishment());
				ps.setInt(3, data.getIdCourse());
				ps.setInt(4, data.getIdUser());
				ps.execute();
				ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Failed to make connection!");
		}
	}
	private void insertSagdeData(int idEmpathy, SagdeData data){
		//TODO implement query
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.warning("performingQuery");
		String queryString = 
			"INSERT INTO empathy_info_sagde " +
			"(idempathy, idsector, ideje, idcmo, idobjcon, idnivel) "+
			"VALUES (?, ?, ? ,? , ?, ?)";
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/sagde", "carlos", "123456");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
		}
		if (connection != null) {
			try {
				PreparedStatement ps = connection.prepareStatement(queryString);
				ps.setInt(1, idEmpathy);
				ps.setInt(2, data.getIdSector());
				ps.setInt(3, data.getIdAxis());
				ps.setInt(4, data.getIdCmo());
				ps.setInt(5, data.getIdKo());
				ps.setInt(6, data.getIdLevel());
				ps.execute();
				ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Failed to make connection!");
		}
	}
}
