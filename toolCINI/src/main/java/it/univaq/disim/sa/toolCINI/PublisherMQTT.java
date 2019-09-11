package it.univaq.disim.sa.toolCINI;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * A Mqtt topic publisher
 *
 */
public class PublisherMQTT {

	private String topic;

	private String host;

	public PublisherMQTT(String host, String topic) {
		this.host = host;
		this.topic=topic;
	}

	public void publish(String payload, String tool) {
		System.out.println("TopicPublisher initializing...");
		String rand = String.valueOf(Math.random());
		try {
			// Create an Mqtt client
			MqttClient mqttClient = new MqttClient(host, tool + rand);
			/*
			 * MqttConnectOptions connOpts = new MqttConnectOptions();
			 * connOpts.setCleanSession(true); connOpts.setUserName(username);
			 * connOpts.setPassword(password.toCharArray());
			 */

			// Connect the client
			System.out.println("Connecting to Solace messaging at " + host);
			/* mqttClient.connect(connOpts); */
			mqttClient.connect();
			System.out.println("Connected");

			// Create a Mqtt message
			MqttMessage message = new MqttMessage(payload.getBytes());
			// Set the QoS on the Messages -
			// Here we are using QoS of 0 (equivalent to Direct Messaging in Solace)
			message.setQos(0);

			System.out.println("Publishing message: " + payload);

			// Publish the message
			mqttClient.publish(topic, message);

			// Disconnect the client
			mqttClient.disconnect();

			System.out.println("Message published. Exiting");

			//System.exit(0);
		} catch (MqttException me) {
		/*	System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();*/
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			publish(payload, tool);
			
		}
	}

}
