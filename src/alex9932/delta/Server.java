package alex9932.delta;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private ServerSocket server;
	private Thread thread;

	public Server(ServerSocket socket) {
		this.server = socket;
		this.thread = new Thread(this);
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Socket port: " + server.getLocalPort());
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
	}
	
	public Thread getThread() {
		return thread;
	}
}