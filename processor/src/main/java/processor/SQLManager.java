package processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.management.Query;

import scala.annotation.meta.setter;

public class SQLManager {

	private String serverSQL;
	private String databaseSQL;
	private String userSQL;
	private String passwordSQL;

	public SQLManager() {
		setConfiguration();
	}

	public Connection connect() {

		Connection connection = null;

		try {

			connection = DriverManager.getConnection("jdbc:mysql://" + serverSQL + "/" + databaseSQL + "?" + "user="
					+ userSQL + "&password=" + passwordSQL + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;

	}

	public String getEquipeName(String equipeID) {
		String equipeName = "";
		Connection connection = connect();

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
			connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return equipeName;

	}

	public String getRecipeName(String recipeID) {
		String recipeName = "";
		Connection connection = connect();

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
			connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return recipeName;

	}

	public String getStepName(String stepID) {
		String stepName = "";
		Connection connection = connect();

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
			connection.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stepName;

	}

	private void setConfiguration() {

		try {
			Properties prop = new Properties();
			String propFileName = "resources/config.properties";

			File initialFile = new File(propFileName);

			InputStream inputStream = new FileInputStream(initialFile);

			prop.load(inputStream);

			// get the property value
			serverSQL = prop.getProperty("serverSQL");
			databaseSQL = prop.getProperty("databaseSQL");
			userSQL = prop.getProperty("userSQL");
			passwordSQL = prop.getProperty("passwordSQL");

			inputStream.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

}
