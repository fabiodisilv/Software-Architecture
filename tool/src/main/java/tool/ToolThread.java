package tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import model.Deleted;
import model.InhibitEvent;
import model.Inserted;
import utils.Util;

public class ToolThread extends Thread {
	
	private String hostMQTT;
	
	@Override
	public void run() {
		
		setConfiguration();

		while (true) {

			String equip_OID = Util.getRandomEquip_OID();
			String recipe_OID = Util.getRecipe_OID();
			String step_OID = Util.getStep_OID_OID();
			boolean hold_flagBoolean = Util.getHold_flagBoolean();
			String hold_flagInserted = "";
			String hold_flagDeleted = "";
			String event_datetime = Util.getData().toString();

			if (hold_flagBoolean) {
				hold_flagInserted = "Y";
				hold_flagDeleted = "N";
			} else {
				hold_flagInserted = "N";
				hold_flagDeleted = "Y";
			}

			Inserted inserted = new Inserted(equip_OID, recipe_OID, step_OID, hold_flagInserted, event_datetime );

			Deleted deleted = new Deleted(equip_OID, recipe_OID, step_OID, hold_flagDeleted, event_datetime);

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

				PublisherMQTT publisherMQTT = new PublisherMQTT(hostMQTT);
				publisherMQTT.publish(messagePayload, equip_OID);

				//Thread.sleep(5000);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
	
	public void setConfiguration() {

		try {
			Properties prop = new Properties();
			String propFileName = "resources/config.properties";

			File initialFile = new File(propFileName);

			InputStream inputStream = new FileInputStream(initialFile);

			prop.load(inputStream);

			// get the property value
			hostMQTT = prop.getProperty("hostMQTT");

			inputStream.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	
}
