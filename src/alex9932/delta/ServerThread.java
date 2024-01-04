package alex9932.delta;

import java.io.IOException;
import java.net.Socket;

import alex9932.delta.http.HTTP;
import alex9932.delta.http.HTTPRequest;

public class ServerThread implements Runnable {

	private Socket s;
	
	public ServerThread(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			HTTPRequest request = HTTP.parse(this.s.getInputStream());
			System.out.printf("[HTTP] %s %s => %s\n", request.getMethod(), this.s.getInetAddress().getHostAddress(), request.getAddress());
			Main.handler.handle(request, this.s);
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
			if (Main.DEBUG) {
				System.out.println("Stack: ");
				e.printStackTrace(System.out);
			}
			
			try {
				this.s.close();
			} catch (IOException  e2) {
				e2.printStackTrace();
			}
			
			System.out.println("[Thread] INTERNAL ERROR");
		}
	}

}
