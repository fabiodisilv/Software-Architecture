package processor3;

import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;

import java.util.concurrent.CountDownLatch;

import javax.naming.event.NamespaceChangeListener;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.JCSMPChannelProperties;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.XMLMessageListener;

import akka.actor.FSM.Timer;
import scala.reflect.internal.Trees.New;


public class CustomReceiver extends Receiver<String> {

	private String host;
	private String username;
	private String password;
	private String vpn;
	private String queueName;

	public CustomReceiver(String host, String username, String password, String vpn, String queueName,
			StorageLevel storageLevel) {
		super(storageLevel);

		this.host = host;
		this.username = username;
		this.password = password;
		this.vpn = vpn;
		this.queueName = queueName;
		
	}
	

	public void onStart() {
		// Start the thread that receives data over a connection
		new Thread() {
			@Override
			public void run() {
				
				receive();
			}
		}.start();
	}

	public void onStop() {
		// possibly close connection
	}

	/** Create a socket connection and receive data until receiver is stopped */
	private void receive() {

		try {
			System.out.println("QueueConsumer initializing...");
			// Create a JCSMP Session
			final JCSMPProperties properties = new JCSMPProperties();
			properties.setProperty(JCSMPProperties.HOST, host); // host:port
			properties.setProperty(JCSMPProperties.USERNAME, username); // client-username
			properties.setProperty(JCSMPProperties.VPN_NAME, vpn); // message-vpn
			properties.setProperty(JCSMPProperties.PASSWORD, password); // client-password
			JCSMPChannelProperties channelProperties = (JCSMPChannelProperties) properties
                    .getProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES);
            channelProperties.setConnectRetries(-1);
            channelProperties.setReconnectRetries(-1);
            channelProperties.setReconnectRetryWaitInMillis(5000);
            channelProperties.setConnectRetriesPerHost(-1);

			final JCSMPSession session = JCSMPFactory.onlyInstance().createSession(properties);
			session.connect();
			

			/*
			 * System.out.
			 * printf("Attempting to provision the queue '%s' on the appliance.%n",
			 * queueName); final EndpointProperties endpointProps = new
			 * EndpointProperties(); // set queue permissions to "consume" and access-type
			 * to "exclusive"
			 * endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
			 * endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE); //
			 * create the queue object locally final Queue queue =
			 * JCSMPFactory.onlyInstance().createQueue(queueName); // Actually provision it,
			 * and do not fail if it already exists session.provision(queue, endpointProps,
			 * JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);
			 */

			final CountDownLatch latch = new CountDownLatch(1); // used for synchronizing b/w threads

			// System.out.printf("Attempting to bind to the queue '%s' on the appliance.%n",
			// queueName);

			final Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);

			// Create a Flow be able to bind to and consume messages from the Queue.
			final ConsumerFlowProperties flow_prop = new ConsumerFlowProperties();
			flow_prop.setEndpoint(queue);
			flow_prop.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);

			EndpointProperties endpoint_props = new EndpointProperties();
			endpoint_props.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);

			FlowReceiver cons;

			cons = session.createFlow(new XMLMessageListener() {
				@Override
				public void onReceive(BytesXMLMessage msg) {
					System.out.println("Message received:\n");
					String event = convertHexToString(bytesToHex(msg.getAttachmentByteBuffer().array()));
					System.out.println(event);
					store(event);
					System.out.println("Event stored in Spark");
					msg.ackMessage();
					latch.countDown(); // unblock main thread
				}

				@Override
				public void onException(JCSMPException e) {
					System.out.printf("Consumer received exception: %s%n", e);
					latch.countDown(); // unblock main thread
					
				}
			}, flow_prop, endpoint_props);

			// Start the consumer
			System.out.println("Connected. Awaiting message ...");
			cons.start();

			while (true) {
				try {
					latch.await(); // block here until message received, and latch will flip
				} catch (InterruptedException e) {
					System.out.println("I was awoken while waiting");
				}
			}

		} catch (JCSMPException e1) {

			e1.printStackTrace();
		}
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}

}
