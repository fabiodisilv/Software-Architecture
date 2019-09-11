package it.univaq.disim.sa.toolCINI;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSContext;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.qpid.jms.JmsConnectionFactory;

public class PublisherAMQP {

	final String TOPIC_NAME = "SA/test";

	private String host;
	private String username;
	private String password;
	
	
	public PublisherAMQP(String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
	}

	public void publish(String payload) throws Exception {
		String solaceHost = this.host;
		String solaceUsername = this.username;
		String solacePassword = this.password;
		System.out.println("TopicPublisher is connecting to Solace router " + solaceHost);

		// Programmatically create the connection factory using default settings
		ConnectionFactory connectionFactory = new JmsConnectionFactory(solaceUsername, solacePassword, solaceHost);

		// Establish connection that uses the Solace Message Router as a message broker
		try (JMSContext context = connectionFactory.createContext()) {

			System.out.println("Connected to the Solace router with client username " + solaceUsername);

			// Create the publishing topic programmatically
			Topic topic = context.createTopic(TOPIC_NAME);

			// Create the message
			TextMessage message = context.createTextMessage(payload);

			System.out.println("Sending message '" +  message.getText() + "' to topic '" + topic.toString() + "'");

			// Create producer and publish the message
			context.createProducer().setDeliveryMode(DeliveryMode.NON_PERSISTENT).send(topic, message);

			System.out.println("Sent successfully. Exiting...");
		}

	}
}