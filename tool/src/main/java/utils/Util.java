package utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;


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
}
