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

public class SQLManager {

	private String server = "localhost";
	private String database = "raw_data";
	private String user = "sa";
	private String password = "sa";

	public Connection connect() {

		Connection connection = null;

		try {
			// Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection(
					"jdbc:mysql://" + server + "/" + database + "?" + "user=" + user + "&password=" + password + "");
		} catch (Exception e) {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + "?" + "user="
						+ user + "&password=" + password
						+ "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return connection;

	}

	public String getEquipeName(String equipeID) {
		String equipeName="";
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
		String recipeName="";
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
		String stepName="";
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


}
