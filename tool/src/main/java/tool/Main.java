package tool;

public class Main {

	public static void main(String[] args) {

		for (int numThread = 0; numThread < 4; numThread++) {

			ToolThread toolThread = new ToolThread();

			toolThread.start();

		}
	}

}
