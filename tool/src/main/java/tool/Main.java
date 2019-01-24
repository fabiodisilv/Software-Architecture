package tool;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import utils.Util;

public class Main {

	static String host = "amqp://192.168.43.180"; // message borker host
	static String username = "admin";
	static String password = "admin";

	public static void main(String[] args) {
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

			Publisher publisher = new Publisher(host, username, password);
			publisher.publish(messagePayload);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
