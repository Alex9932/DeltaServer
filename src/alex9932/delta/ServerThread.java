package alex9932.delta;

import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {

	private Socket socket;
	private Server server;
	
	public ServerThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			Main.handler.handle(this.socket);
		} catch (Exception e) {
			System.out.printf("ERROR: %s\n", e.toString());
			if (Main.DEBUG) {
				System.out.println("Stack: ");
				e.printStackTrace(System.out);
			}
			
			try {
				this.socket.close();
			} catch (IOException  e2) {
				e2.printStackTrace();
			}
			
			System.out.println("[Thread] INTERNAL ERROR");
		}
		this.server.decrementConnection();
	}

}
