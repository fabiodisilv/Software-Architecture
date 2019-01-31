package processor;

import model.DecodedDeletedElement;
import model.DecodedInsertedElement;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class NoSQLManager {
	private String hostNoSQL;
	private int portOneNoSQL;
	private int portTwoNoSQL;
	private String schemeNoSQL;

	private String indexNoSQL;

	public NoSQLManager() {
		setConfiguration();
	}

	private RestHighLevelClient makeConnection() {

		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost(hostNoSQL, portOneNoSQL, schemeNoSQL),
						new HttpHost(hostNoSQL, portTwoNoSQL, schemeNoSQL)));
		return client;

		//
		// client.close();
	}

	public void insertInsertedEvent(DecodedInsertedElement decodedInsertedElement) {
		String typeNoSQL = "inserted";

		RestHighLevelClient client = makeConnection();

		String eventID = (UUID.randomUUID().toString());

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("eventID", eventID);
		dataMap.put("equipe_OID", decodedInsertedElement.getEquipe_OID());
		dataMap.put("equipeName", decodedInsertedElement.getEquipeName());
		dataMap.put("recipe_OID", decodedInsertedElement.getRecipe_OID());
		dataMap.put("recipeName", decodedInsertedElement.getRecipeName());
		dataMap.put("step_OID", decodedInsertedElement.getStep_OID());
		dataMap.put("stepName", decodedInsertedElement.getStepName());
		dataMap.put("hold_type", decodedInsertedElement.getHold_type());
		dataMap.put("hold_flag", decodedInsertedElement.getHold_flag());
		dataMap.put("event_datetime", decodedInsertedElement.getHold_flag());

		IndexRequest indexRequest = new IndexRequest(indexNoSQL, typeNoSQL, eventID).source(dataMap);

		try {

			IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			// IndexResponse response = client.index(indexRequest);

			client.close();

		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}

	}

	public void insertDeletedEvent(DecodedDeletedElement decodedDeletedElement) {

		String typeNoSQL = "deleted";

		RestHighLevelClient client = makeConnection();

		String eventID = (UUID.randomUUID().toString());

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("eventID", eventID);
		dataMap.put("equipe_OID", decodedDeletedElement.getEquipe_OID());
		dataMap.put("equipeName", decodedDeletedElement.getEquipeName());
		dataMap.put("recipe_OID", decodedDeletedElement.getRecipe_OID());
		dataMap.put("recipeName", decodedDeletedElement.getRecipeName());
		dataMap.put("step_OID", decodedDeletedElement.getStep_OID());
		dataMap.put("stepName", decodedDeletedElement.getStepName());
		dataMap.put("hold_type", decodedDeletedElement.getHold_type());
		dataMap.put("hold_flag", decodedDeletedElement.getHold_flag());
		dataMap.put("event_datetime", decodedDeletedElement.getHold_flag());

		IndexRequest indexRequest = new IndexRequest(indexNoSQL, typeNoSQL, eventID).source(dataMap);

		try {

			IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
			// IndexResponse response = client.index(indexRequest);

			client.close();

		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}

	}

	private void setConfiguration() {
		try {
			Properties prop = new Properties();
			String propFileName = "resources/config.properties";

			File initialFile = new File(propFileName);

			InputStream inputStream = new FileInputStream(initialFile);

			prop.load(inputStream);

			// get the property value
			hostNoSQL = prop.getProperty("hostNoSQL");
			portOneNoSQL = Integer.parseInt(prop.getProperty("portOneNoSQL"));
			portTwoNoSQL = Integer.parseInt(prop.getProperty("portTwoNoSQL"));
			schemeNoSQL = prop.getProperty("schemeNoSQL");
			indexNoSQL = prop.getProperty("indexNoSQL");
			
			inputStream.close();
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}