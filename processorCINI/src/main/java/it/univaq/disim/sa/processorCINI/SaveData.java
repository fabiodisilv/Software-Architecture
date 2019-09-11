package it.univaq.disim.sa.processorCINI;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import it.univaq.disim.sa.model.DecodedDeletedElement;
import it.univaq.disim.sa.model.DecodedInsertedElement;
import it.univaq.disim.sa.model.Deleted;
import it.univaq.disim.sa.model.InhibitEvent;
import it.univaq.disim.sa.model.Inserted;

public class SaveData {

	private SQLManager sqlManager;
	private NoSQLManager noSQLManager;

	public SaveData(SQLManager sqlManager, NoSQLManager noSQLManager) {
		this.noSQLManager = noSQLManager;
		this.sqlManager = sqlManager;
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

			e.printStackTrace();
			return null;
		}

	}

	private DecodedInsertedElement decodeInsertedIDs(Inserted inserted) {
		// SQLManager sqlManager = new SQLManager();

		DecodedInsertedElement decodedInsertedElement = new DecodedInsertedElement();

		decodedInsertedElement.setEquipe_OID(inserted.getEquip_OID());
		decodedInsertedElement.setEquipeName(sqlManager.getEquipeName(inserted.getEquip_OID()));

		decodedInsertedElement.setRecipe_OID(inserted.getRecipe_OID());
		decodedInsertedElement.setRecipeName(sqlManager.getRecipeName(inserted.getRecipe_OID()));

		decodedInsertedElement.setStep_OID(inserted.getStep_OID());
		decodedInsertedElement.setStepName(sqlManager.getStepName(inserted.getStep_OID()));

		decodedInsertedElement.setHold_type(inserted.getHold_type());

		decodedInsertedElement.setHold_flag(inserted.getHold_flag());

		decodedInsertedElement.setEvent_datetime(inserted.getEvent_datetime());

		return decodedInsertedElement;
	}

	private DecodedDeletedElement decodeDeletedIDs(Deleted deleted) {
		// SQLManager sqlManager = new SQLManager();

		DecodedDeletedElement decodedDeletedElement = new DecodedDeletedElement();
		decodedDeletedElement.setEquipe_OID(deleted.getEquip_OID());
		decodedDeletedElement.setEquipeName(sqlManager.getEquipeName(deleted.getEquip_OID()));

		decodedDeletedElement.setRecipe_OID(deleted.getRecipe_OID());
		decodedDeletedElement.setRecipeName(sqlManager.getRecipeName(deleted.getRecipe_OID()));

		decodedDeletedElement.setStep_OID(deleted.getStep_OID());
		decodedDeletedElement.setStepName(sqlManager.getStepName(deleted.getStep_OID()));

		decodedDeletedElement.setHold_type(deleted.getHold_type());

		decodedDeletedElement.setHold_flag(deleted.getHold_flag());

		decodedDeletedElement.setEvent_datetime(deleted.getEvent_datetime());

		return decodedDeletedElement;
	}

	private void insertEvents(DecodedInsertedElement decodedInsertedElement,
			DecodedDeletedElement decodedDeletedElement) {

		// NoSQLManager noSQLManager = new NoSQLManager();

		noSQLManager.insertInsertedEvent(decodedInsertedElement);

		noSQLManager.insertDeletedEvent(decodedDeletedElement);

	}

	public Void call(List<String> messagesList) throws Exception {

		int numRcds = messagesList.size();

		if (numRcds > 0) {
			try {

				sqlManager = new SQLManager();
				noSQLManager = new NoSQLManager();

				for (String m : messagesList) {
					InhibitEvent inhibitEvent = buildInhibitEvent(m);

					DecodedInsertedElement decodedInsertedElement = decodeInsertedIDs(inhibitEvent.getInserted());

					DecodedDeletedElement decodedDeletedElement = decodeDeletedIDs(inhibitEvent.getDeleted());

					insertEvents(decodedInsertedElement, decodedDeletedElement);

				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sqlManager.closeConnection();
				noSQLManager.closeConnection();

				System.out.println("Completed!");
			}
		}

		return null;
	}

}
