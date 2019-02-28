package processor3;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.spark.SparkConf;

import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import processor3.NoSQLManager;
import processor3.SQLManager;

public class Processor {

	private SQLManager sqlManager;
	private NoSQLManager noSQLManager;

	private String host;
	private String vpn;
	private String username;
	private String password;
	private String queueName;

	public Processor() {
		setConfiguration();
		sqlManager = new SQLManager();
		noSQLManager = new NoSQLManager();
	}

	public void start() {

		// Configure and initialize SparkSession and JavaStreamingContext
		SparkConf sparkConf = new SparkConf().setAppName("Processor").setMaster("local[*]");

		JavaStreamingContext jsc = new JavaStreamingContext(sparkConf, Durations.seconds(1));

		// create the input stream receiver
		JavaReceiverInputDStream<String> lines = jsc.receiverStream(
				new CustomReceiver(host, username, password, vpn, queueName, StorageLevel.MEMORY_ONLY_2()));

		lines.foreachRDD(new SaveRDD(sqlManager, noSQLManager));

		jsc.start();
		jsc.awaitTermination();
		jsc.stop();
		jsc.close();

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

			inputStream.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}