package alex9932.delta.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import alex9932.delta.Main;
import alex9932.delta.Utils;
import alex9932.delta.http.HTTP;
import alex9932.delta.http.HTTPHeader;

public class FileIOHandler implements IRequestHandler {

	private static final String HTTP_VERSION = "1.1";
	
	@Override
	public void onLoad() {
		System.out.println("Loaded FileIO handler!");
	}

	@Override
	public void handle(Socket socket) throws IOException {
		
		boolean keepAlive = true;
		HTTPHeader header = null;
		
		while(keepAlive) {
			
			try {
				header = HTTP.parse(socket.getInputStream());
				System.out.printf("[HTTP] %s %s => %s\n", header.getMethod(), socket.getInetAddress().getHostAddress(), header.getAddress());
			} catch (Exception e) {
				System.out.printf("[HTTP] Error => %s\n", e.toString());
				break;
			}
			
			// Close connection if not 'keep-alive'
			String connectionStatus = header.get("Connection");
			if(connectionStatus == null || !connectionStatus.equals("keep-alive")) {
				keepAlive = false;
			}
		
			if (header.getMethod().equals("GET")) {
				processGet(header, socket, keepAlive);
			} else if (header.getMethod().equals("POST")) {
				processPost(header, socket, keepAlive);
			} else {
				System.out.println("FileIOHandler: Invalid HTTP method!");
				// Force close connection
				keepAlive = false;
			}
		
		}

		System.out.printf("[HTTP] %s => Connection close!\n", socket.getInetAddress().getHostAddress());
		socket.close();
	}
	
	private void processPost(HTTPHeader request, Socket socket, boolean keepAlive) throws IOException {
		System.out.println("POST: " + request.getAddress());
		String header = HTTP.buildResponse(HTTP_VERSION, HTTP.HTTP_501, Utils.getType(".html"), 0L, keepAlive);
		socket.getOutputStream().write(header.getBytes());
	}
	
	// TODO: "206 Partial Content";
	private void processGet(HTTPHeader request, Socket socket, boolean keepAlive) throws IOException {
		OutputStream out = socket.getOutputStream();
		
		// TODO: Add router
		File f;
		if (request.getAddress().equals("/")) {
			f = new File("www/index.html");
		} else {
			f = new File("www" + request.getAddress());
		}
		
		if (f.exists()) {
			String header = HTTP.buildResponse(HTTP_VERSION, HTTP.HTTP_200, Utils.getType(f.getName()), f.length(), keepAlive);
			out.write(header.getBytes());
			this.sendFile(f, out);
		} else {
			f = new File("err/404.html");
			FileInputStream fin = new FileInputStream(f);
			byte[] data = new byte[(int)f.length()];
			fin.read(data);
			fin.close();
			String s = new String(data);
			s = s.replace("%SERVER%", "Deltaserver").replace("%VERSION%", Main.VERSION).replace("%PLATFORM%", Main.PLATFORM).replace("%BUILD_NAME%", Main.BUILD_NAME).replace("%ACCESS_FILE%", request.getAddress());
			byte[] resp = s.getBytes();
			String header = HTTP.buildResponse(HTTP_VERSION, "404 Not Found", Utils.getType(f.getName()), (long)resp.length, keepAlive);
			out.write(header.getBytes());
			out.write(resp);
		}
	}
	
	private void sendFile(File f, OutputStream stream) throws IOException {
		long f_len = f.length();
		FileInputStream fin = new FileInputStream(f);
		byte[] buffer = new byte[Main.BUFFER_SIZE];
		long steps = f_len / (long)Main.BUFFER_SIZE;
		long readed = 0L;
		if (Main.DEBUG) {
			System.out.println("Sending " + f.getName());
		}

		for(long i = 0L; i < steps; ++i) {
			fin.read(buffer);
			stream.write(buffer);
			readed += (long)Main.BUFFER_SIZE;
			if (Main.DEBUG) {
				System.out.println("Sending " + f.getName() + " [" + (int)((double)readed / (double)f_len * 100.0D) + "%]");
			}
		}
		
		int b_size = (int)(f_len - readed);
		byte[] b2 = new byte[b_size];
		fin.read(b2);
		stream.write(b2);
		
		if (Main.DEBUG) {
			System.out.println("Sended " + f.getName());
		}
		fin.close();
	}
}