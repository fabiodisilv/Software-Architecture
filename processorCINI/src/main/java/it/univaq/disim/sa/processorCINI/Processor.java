package it.univaq.disim.sa.processorCINI;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import it.univaq.disim.sa.utils.ESBUtils;

public class Processor {

	private SQLManager sqlManager;
	private NoSQLManager noSQLManager;

	private String host;
	private String vpn;
	private String username;
	private String password;
	private String queueName;

	private String esbUser;
	private String esbPass;
	private String esbTopic;

	public Processor() {
		setConfiguration();
		sqlManager = new SQLManager();
		noSQLManager = new NoSQLManager();
	}

	public void start() throws Exception {

		while (true) {

			String esbSession = ESBUtils.login(esbUser, esbPass);
			List<String> messagesList = ESBUtils.readToObject(esbSession, esbTopic);

			new SaveData(sqlManager, noSQLManager).call(messagesList);

			ESBUtils.logout(esbUser, esbSession);
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
			host = prop.getProperty("host");
			queueName = prop.getProperty("queueName");
			vpn = prop.getProperty("vpn");
			username = prop.getProperty("username");
			password = prop.getProperty("password");

			esbUser = prop.getProperty("esbUser");
			esbPass = prop.getProperty("esbPass");
			esbTopic = prop.getProperty("esbTopic");

			inputStream.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}