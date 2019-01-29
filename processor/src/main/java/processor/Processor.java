package processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.mqtt.MQTTUtils;

public class Processor {

	private String hostMQTT;
	private String topic;
	
	public Processor() {
		setConfiguration();
	}
	
	public void startProcessing() {
		// Create spark configuration
		SparkConf sparkConf = new SparkConf().setAppName("Processor").setMaster("local[*]");

		// spark streaming context with a 1 second batch size
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(1));

		// Define MQTT url and topic
		//String brokerUrl = "tcp://localhost:1883";
		//String topic = "sa/test";

		// 2. MQTTUtils to collect MQTT messages
		JavaReceiverInputDStream<String> messages = MQTTUtils.createStream(jssc, hostMQTT, topic);

		// process the messages on the queue and save them to the database
		messages.foreachRDD(new SaveRDD());

		// Start the context
		jssc.start();

		try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
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
			hostMQTT = prop.getProperty("hostMQTT");
			topic = prop.getProperty("topic");

			inputStream.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
