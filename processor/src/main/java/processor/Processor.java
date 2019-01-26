package processor;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.mqtt.MQTTUtils;

public class Processor {

	public static void main(String[] args) {
		
		// Create spark configuration
		SparkConf sparkConf = new SparkConf().setAppName("Processor").setMaster("local[*]");
		 
		// spark streaming context with a 10 second batch size
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(1));
		 
		//Define MQTT url and topic
		String brokerUrl = "tcp://localhost:1883";
		String topic = "sa/test";
		 
        //2. MQTTUtils to collect MQTT messages
        JavaReceiverInputDStream<String> messages = MQTTUtils.createStream(jssc, brokerUrl, topic);

        
        //process the messages on the queue and save them to the database
        messages.foreachRDD(new SaveRDD());

        
        // Start the context
        jssc.start();
        try {
			jssc.awaitTermination();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
