package processor;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;

import com.esotericsoftware.minlog.Log;

import avro.shaded.com.google.common.collect.ImmutableBiMap.Builder;
import model.DecodedDeletedElement;
import model.DecodedInsertedElement;
import model.Deleted;
import model.InhibitEvent;
import model.Inserted;

public class SaveRDD implements VoidFunction<JavaRDD<String>> {

	public void call(JavaRDD<String> messages) throws Exception {
		if (messages != null && messages.count() > 0) {

			// Convert to list
			List<String> messagesList = messages.collect();
			int numRcds = messagesList.size();

			if (numRcds > 0) {
				try {

					for (String m : messagesList) {
						InhibitEvent inhibitEvent = buildInhibitEvent(m);
						
						DecodedInsertedElement decodedInsertedElement = decodeInsertedIDs(inhibitEvent.getInserted());
						
						DecodedDeletedElement decodedDeletedElement = decodeDeletedIDs(inhibitEvent.getDeleted());
						
						insertDecodedInsertedElement(decodedInsertedElement);
						
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					System.out.println("Completed!");
				}
			}
		}
	}

	private InhibitEvent buildInhibitEvent(String message) {

		StringReader messageReader = new StringReader(message);

		JAXBContext jaxbContext;
		
		try {
			jaxbContext = JAXBContext.newInstance(InhibitEvent.class);

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			InhibitEvent inhibitEvent = (InhibitEvent) unmarshaller.unmarshal(messageReader);
			
			return inhibitEvent;

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	private DecodedInsertedElement decodeInsertedIDs(Inserted inserted) {
		SQLManager sqlManager = new SQLManager();
		
		DecodedInsertedElement decodedInsertedElement = new DecodedInsertedElement();
		decodedInsertedElement.setEquipe_OID(inserted.getEquip_OID());
		decodedInsertedElement.setEquipeName(sqlManager.getEquipeName(inserted.getEquip_OID()));
		
		decodedInsertedElement.setRecipe_OID(inserted.getRecipe_OID());
		decodedInsertedElement.setRecipeName(sqlManager.getRecipeName(inserted.getRecipe_OID()));
		
		decodedInsertedElement.setStep_OID(inserted.getStep_OID());
		decodedInsertedElement.setStepName(sqlManager.getStepName(inserted.getStep_OID()));
		
		decodedInsertedElement.setHold_type(inserted.getHold_type());
		
		decodedInsertedElement.setHold_flag(inserted.getHold_flag());
	
		return decodedInsertedElement;
	}
	
	private DecodedDeletedElement decodeDeletedIDs(Deleted deleted) {
		SQLManager sqlManager = new SQLManager();

		DecodedDeletedElement decodedDeletedElement = new DecodedDeletedElement();
		decodedDeletedElement.setEquipe_OID(deleted.getEquip_OID());
		decodedDeletedElement.setEquipeName(sqlManager.getEquipeName(deleted.getEquip_OID()));
		
		decodedDeletedElement.setRecipe_OID(deleted.getRecipe_OID());
		decodedDeletedElement.setRecipeName(sqlManager.getRecipeName(deleted.getRecipe_OID()));

		decodedDeletedElement.setStep_OID(deleted.getStep_OID());
		decodedDeletedElement.setStepName(sqlManager.getStepName(deleted.getStep_OID()));

		decodedDeletedElement.setHold_type(deleted.getHold_type());

		decodedDeletedElement.setHold_flag(deleted.getHold_flag());
		
		return decodedDeletedElement;
	}

	private void insertDecodedInsertedElement(DecodedInsertedElement decodedInsertedElement) {
		NoSQLManager noSQLManager = new NoSQLManager();
		
		noSQLManager.insertPerson(decodedInsertedElement);
	}
	
}
