package alex9932.delta.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import alex9932.delta.Main;

public class Proxy implements IRequestHandler {
	
	private static final int BUFFERSIZE = 1;

	@Override
	public void onLoad() {
		System.out.println("Loaded proxy handler!");
	}

	@Override
	public void handle(Socket socket) throws IOException {
		
		Socket client = new Socket(Main.PROXYIP, Main.PROXYPORT);
		
		ProxyListener pl = new ProxyListener(client, socket);
		Thread t = new Thread(pl);
		t.start();
		
		try {
			byte[] buffer = new byte[BUFFERSIZE];
			InputStream is = socket.getInputStream();
			OutputStream os = client.getOutputStream();
			while (Main.RUNNING && pl.isRunning()) {
				is.read(buffer);
				os.write(buffer);
			}
		} catch (Exception e) {
			System.out.println("Connection (main) reset: " + e);
			pl.stop();
		}
		
	}
	
	class ProxyListener implements Runnable {
		private Socket client, server;
		private boolean running = true;
		
		public ProxyListener(Socket client, Socket server) {
			this.client = client;
			this.server = server;
		}
		
		public boolean isRunning() { return running; }
		public void stop() { running = false; }
		
		@Override
		public void run() {

			try {
				byte[] buffer = new byte[BUFFERSIZE];
				InputStream is = client.getInputStream();
				OutputStream os = server.getOutputStream();
				while (Main.RUNNING && running) {
					is.read(buffer);
					os.write(buffer);
				}
			} catch (Exception e) {
				System.out.println("Connection (side) reset: " + e);
				running = false;
			}
			
		}
	}
}
