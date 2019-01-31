package processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SQLManager {

	private String hostSQL;
	private String databaseSQL;
	private String userSQL;
	private String passwordSQL;
	private Connection connection;

	public SQLManager() {
		setConfiguration();
		connection = connect();
	}

	private Connection connect() {

		Connection connection = null;

		try {

			connection = DriverManager.getConnection("jdbc:mysql://" + hostSQL + "/" + databaseSQL + "?" + "user="
					+ userSQL + "&password=" + passwordSQL + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;

	}

	public String getEquipeName(String equipeID) {
		String equipeName = "";
		// Connection connection = connect();

		// PreparedStatements can use variables and are more efficient
		PreparedStatement preparedStatement;

		String query = "SELECT name FROM equipe WHERE ID = ?";

		try {
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, equipeID);

			ResultSet rs;

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				equipeName = rs.getString("name");
			}
			// connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return equipeName;

	}

	public String getRecipeName(String recipeID) {
		String recipeName = "";
		// Connection connection = connect();

		// PreparedStatements can use variables and are more efficient
		PreparedStatement preparedStatement;

		String query = "SELECT name FROM recipe WHERE ID = ?";

		try {
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, recipeID);

			ResultSet rs;

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				recipeName = rs.getString("name");
			}
			// connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return recipeName;

	}

	public String getStepName(String stepID) {
		String stepName = "";
		// Connection connection = connect();

		// PreparedStatements can use variables and are more efficient
		PreparedStatement preparedStatement;

		String query = "SELECT name FROM step WHERE ID = ?";

		try {
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, stepID);

			ResultSet rs;

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				stepName = rs.getString("name");
			}
			// connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stepName;

	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setConfiguration() {

		try {
			Properties prop = new Properties();
			String propFileName = "resources/config.properties";

			File initialFile = new File(propFileName);

			InputStream inputStream = new FileInputStream(initialFile);

			prop.load(inputStream);

			// get the property value
			hostSQL = prop.getProperty("hostSQL");
			databaseSQL = prop.getProperty("databaseSQL");
			userSQL = prop.getProperty("userSQL");
			passwordSQL = prop.getProperty("passwordSQL");

			inputStream.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

}
