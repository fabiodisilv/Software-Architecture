package it.univaq.disim.sa.utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;


public class Util {
	public static String getRandomEquip_OID() {
		return "equipe_OID_" + getRand();
	}

	public static String getRecipe_OID() {
		return "recipe_OID_" + getRand();
	}

	public static String getStep_OID_OID() {
		return "step_OID_" + getRand();
	}

	public static boolean getHold_flagBoolean() {
		return Math.random() < 0.5;
	}

	public static int getRand() {
		int min = 1;
		int max = 10;
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

		return randomNum;
	}
	
	public static Date getData() {
	      Date date = new Date();
	      return date;
	}
	
	// Method to encode a string value using `UTF-8` encoding scheme
	public static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
