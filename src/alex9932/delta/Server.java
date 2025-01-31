package alex9932.delta;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private int connections;
	
	public Server() {
		this.connections = 0;
	}
	
	public void run() {
		ServerSocket sock = null;
		
		try {
			try {
				sock = ServerSocketFactory.newSocket();
			} catch (Exception e) {
				Main.RUNNING = false;
				throw new Exception("SOCKET ERROR / " + e);
			}
			
			while(Main.RUNNING) {
				Socket s = sock.accept();
				
				// Start thread for new connection
				Thread t = new Thread(new ServerThread(s, this));
				connections++;
				t.start();
			}
			
			sock.close();
			
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} finally {
			// Close socket if possible
			try { sock.close(); } catch (Exception r) {}
		}
	}
	
	public int getConnections() {
		return connections;
	}

	public void decrementConnection() {
		if(this.connections <= 0) { return; }
		this.connections--;
	}
}