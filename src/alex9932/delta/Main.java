package alex9932.delta;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	public static final String SERVER = "jserver";
	public static final String VERSION = "1.4";
	
	public static String PLATFORM = "none";
	public static int SERVER_PORT = 8080;
	public static boolean running = true;

	public static void main(final String[] args) {
		System.out.println(SERVER + " " + VERSION + " starting up...");

		Main.PLATFORM = String.valueOf(
				System.getProperty("os.name")) +
				" " + System.getProperty("os.version") +
				" " + System.getProperty("os.arch");
		
		System.out.println("Platform: " + Main.PLATFORM);

		try {
			final ServerSocket server = new ServerSocket(Main.SERVER_PORT);
			System.out.println("Socket port: " + Main.SERVER_PORT);
			while (Main.running) {
				Socket socket = server.accept();
				new Thread(new Handler(socket)).start();
			}
			server.close();
		} catch (Exception e) {
			Main.running = false;
			System.out.println("ERR: " + e);
			e.printStackTrace();
		}
		System.out.println("Shutting down...");
	}
}
