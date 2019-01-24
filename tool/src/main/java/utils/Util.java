package utils;

import java.util.concurrent.ThreadLocalRandom;

public class Util {
	public static String getRandomEquip_OID() {
		return "equipe_OID_" + getRand();
	}

	public static String getRecipe_OID() {
		return "recipe_OID_" + getRand();
	}

	public static String getStep_OID_OID() {
		return "step_OID_OID_" + getRand();
	}

	public static boolean getHold_flagBoolean() {
		return Math.random() < 0.5;
	}

	public static int getRand() {
		int min = 1;
		int max = 5;
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

		return randomNum;
	}
}
