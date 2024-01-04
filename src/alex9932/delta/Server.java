package alex9932.delta;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
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
				Thread t = new Thread(new ServerThread(s));
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
}