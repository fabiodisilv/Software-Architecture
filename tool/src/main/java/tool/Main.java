package tool;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import model.Deleted;
import model.InhibitEvent;
import model.Inserted;
import utils.Util;

public class Main {

	static String hostAMQP = "amqp://192.168.43.180"; // message borker host
	static String hostJMS = "smf://192.168.43.178"; // message borker host
	static String hostMQTT = "tcp://localhost:1883";
	static String vpnName = "Solace_Spark_VPN";
	static String username = "spark_client";
	static String password = "spark_client";

	public static void main(String[] args) {
		
		while (true) {
			
		
		String equip_OID = Util.getRandomEquip_OID();
		String recipe_OID = Util.getRecipe_OID();
		String step_OID = Util.getStep_OID_OID();
		boolean hold_flagBoolean = Util.getHold_flagBoolean();
		String hold_flagInserted = "";
		String hold_flagDeleted = "";

		if (hold_flagBoolean) {
			hold_flagInserted = "Y";
			hold_flagDeleted = "N";
		} else {
			hold_flagInserted = "N";
			hold_flagDeleted = "Y";
		}

		Inserted inserted = new Inserted(equip_OID, recipe_OID, step_OID, hold_flagInserted);

		Deleted deleted = new Deleted(equip_OID, recipe_OID, step_OID, hold_flagDeleted);

		InhibitEvent inhibitEvent = new InhibitEvent(inserted, deleted);

		try {
			JAXBContext context = JAXBContext.newInstance(InhibitEvent.class);
			Marshaller m = context.createMarshaller();
			// for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter writer = new StringWriter();
			// Write to list to a writer
			m.marshal(inhibitEvent, writer);
			String messagePayload = writer.toString();
			// System.out.println(messagePayload);

			/*PublisherAMQP publisherAMQP = new PublisherAMQP(hostAMQP, username, password);
			publisherAMQP.publish(messagePayload);*/
			
			/*PublisherJMS publisherJMS = new PublisherJMS(hostJMS, username, password, vpnName);
			publisherJMS.publish(messagePayload);*/
			
			PublisherMQTT publisherMQTT = new PublisherMQTT(hostMQTT);
			publisherMQTT.publish(messagePayload, equip_OID);
			
			Thread.sleep(5000);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		}
		
	}

}
