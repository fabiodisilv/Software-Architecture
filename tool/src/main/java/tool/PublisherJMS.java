package tool;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import com.solacesystems.jms.SolConnectionFactory;
import com.solacesystems.jms.SolJmsUtility;

/**
 * Publishes a messages to a topic using Solace JMS 1.1 API implementation.
 * 
 * This is the Publisher in the Publish/Subscribe messaging pattern.
 */
public class PublisherJMS {

	final String TOPIC_NAME = "SA/test";

	private String host;
	private String vpnName;
	private String username;
	private String password;
    
    public PublisherJMS(String host, String vpnName, String username, String password) {
		this.host = host;
		this.vpnName = vpnName;
		this.username = username;
		this.password = password;
	} 

    public void publish(String payload) throws Exception {
    	
        System.out.printf("TopicPublisher is connecting to Solace messaging at %s...%n", host);

        // Programmatically create the connection factory using default settings
        SolConnectionFactory connectionFactory = SolJmsUtility.createConnectionFactory();
        connectionFactory.setHost(this.host);
        connectionFactory.setVPN(this.vpnName);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);

        // Create connection to the Solace router
        Connection connection = connectionFactory.createConnection();

        // Create a non-transacted, auto ACK session.
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        System.out.printf("Connected to the Solace Message VPN '%s' with client username '%s'.%n", vpnName,
                username);

        // Create the publishing topic programmatically
        Topic topic = session.createTopic(TOPIC_NAME);

        // Create the message producer for the created topic
        MessageProducer messageProducer = session.createProducer(topic);

        // Create the message
        TextMessage message = session.createTextMessage(payload);

        System.out.printf("Sending message '%s' to topic '%s'...%n", message.getText(), topic.toString());

        // Send the message
        // NOTE: JMS Message Priority is not supported by the Solace Message Bus
        messageProducer.send(topic, message, DeliveryMode.NON_PERSISTENT,
                Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
        System.out.println("Sent successfully. Exiting...");

        // Close everything in the order reversed from the opening order
        // NOTE: as the interfaces below extend AutoCloseable,
        // with them it's possible to use the "try-with-resources" Java statement
        // see details at https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        messageProducer.close();
        session.close();
        connection.close();
    }

}