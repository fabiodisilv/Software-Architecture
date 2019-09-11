package it.univaq.disim.sa.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.univaq.disim.sa.model.ESBResponse;
import it.univaq.disim.sa.model.ESBResponseInner;


public class ESBUtils {
	public static String login(String user, String pass) throws Exception {
		URL url = new URL("http://esb.sytes.net/login.php");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");

		Map<String, String> parameters = new HashMap<>();
		parameters.put("user", user);
		parameters.put("pass", pass);

		conn.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		out.flush();
		out.close();

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = "";
		String line = "";

		System.out.println("Login");
		while ((line = br.readLine()) != null) {
			output += line;
		}

		System.out.println(output);

		conn.disconnect();

		return output;
	}

	public static void logout(String user, String session) throws Exception {
		URL url = new URL("http://esb.sytes.net/logout.php?user=" + user + "&id_sess=" + session);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		//conn.setRequestProperty("Accept", "application/json");

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;

		System.out.println("Logout");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
	}

	public static void write(String session, String topic, String data) throws Exception {
		URL url = new URL("http://esb.sytes.net/write.php?topic=" + topic + "&data=" + data + "&id_sess=" + session);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// conn.setRequestProperty("Accept", "application/json");

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;

		System.out.println("Write");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
	}

	public static void read(String session, String topic) throws Exception {
		URL url = new URL("http://esb.sytes.net/read.php?topic=" + topic + "&id_sess=" + session);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// conn.setRequestProperty("Accept", "application/json");

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;

		System.out.println("Read");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
	}
	
	public static List<String> readToObject(String session, String topic) throws Exception {
		List<String> resultList = new ArrayList<String>();
		
		URL url = new URL("http://esb.sytes.net/read.php?topic=" + topic + "&id_sess=" + session);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// conn.setRequestProperty("Accept", "application/json");

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = "";
		String line = "";

		System.out.println("readToObject");
		while ((line = br.readLine()) != null) {
			output += line;
		}

		System.out.println(output);

		ObjectMapper mapper = new ObjectMapper();
		ESBResponse esbResponse = mapper.readValue(output, ESBResponse.class);
		
		ESBResponseInner[] esbResponseInner = esbResponse.getdata();
		
		for (ESBResponseInner esbResponseInnerCurrent : esbResponseInner) {
			
			System.out.println(esbResponseInnerCurrent.getData());
			resultList.add(esbResponseInnerCurrent.getData());
			
		}

		conn.disconnect();
		
		return resultList;
	}
}
