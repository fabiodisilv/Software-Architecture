package processor;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.DecodedInsertedElement;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoSQLManager {
	private String hostNoSQL;
	private int portOneNoSQL;
	private int portTwoNoSQL;
	private String schemeNoSQL;

	private static RestHighLevelClient restHighLevelClient;
	private static ObjectMapper objectMapper = new ObjectMapper();

	private static final String indexNoSQL = "results";
	private static final String typeNoSQL = "events";

	private synchronized RestHighLevelClient makeConnection() {

		if (restHighLevelClient == null) {
			restHighLevelClient = new RestHighLevelClient(
					RestClient.builder(new HttpHost(hostNoSQL, portOneNoSQL, schemeNoSQL), new HttpHost(hostNoSQL, portTwoNoSQL, schemeNoSQL)));
		}

		return restHighLevelClient;
	}

	private synchronized void closeConnection() throws IOException {
		restHighLevelClient.close();
		restHighLevelClient = null;
	}

	public void insertEvent(DecodedInsertedElement decodedInsertedElement) {
		makeConnection();

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

		IndexRequest indexRequest = new IndexRequest(indexNoSQL, typeNoSQL, eventID).source(dataMap);
		try {

			IndexResponse response = restHighLevelClient.index(indexRequest);

			closeConnection();

		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}

	}

}
