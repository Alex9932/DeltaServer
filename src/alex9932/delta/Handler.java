package alex9932.delta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import alex9932.delta.http.HTTP;
import alex9932.delta.http.HTTPRequest;

public class Handler implements Runnable {
	private Socket socket;

	public Handler(final Socket socket) {
		this.socket = socket;
	}

	private void sendFile(File file, OutputStream out) throws IOException {
		byte[] buffer = new byte[4096];
		FileInputStream fin = new FileInputStream(file);
		while (fin.available() > 4096) {
			fin.read(buffer, 0, 4096);
			out.write(buffer);
		}
		if (fin.available() > 0) {
			byte[] s_buffer = new byte[fin.available()];
			fin.read(s_buffer, 0, s_buffer.length);
			out.write(s_buffer);
		}
		fin.close();
	}
	
	private void sendErrorPage(File file, OutputStream out, String addr) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		byte[] b_buffer = new byte[(int)file.length()];
		fin.read(b_buffer);
		fin.close();

		String str = new String(b_buffer, StandardCharsets.UTF_8);
		out.write(str
			.replace("%PLATFORM%", Main.PLATFORM)
			.replace("%SERVER%", Main.SERVER)
			.replace("%VERSION%", Main.VERSION)
			.replace("%BUILD_NAME%", Main.BUILD_NAME)
			.replace("%ACCESS_FILE%", addr).getBytes());
	}

	private void methodGET(InputStream in, OutputStream out, Socket socket, HTTPRequest request) throws IOException {
		String addr = request.getAddress();
		if (addr.equals("/")) {
			addr = "/index.html";
		}
		
		File file = new File("www" + addr);
		String header;
		if (file.exists() && file.isFile()) {
			header = HTTP.buildResponse(HTTP.HTTP_OK, Utils.getType(file), file.length());
			out.write(header.getBytes()); // Sending header
			sendFile(file, out);          // Sending file
		} else {
			file = new File("err/404.html");
			header = HTTP.buildResponse(HTTP.HTTP_NOT_FOUND, Utils.getType(file), file.length());
			out.write(header.getBytes());   // Sending header
			sendErrorPage(file, out, addr); // Sending file
		}
	}

	private void methodPOST(InputStream in, OutputStream out, Socket socket, HTTPRequest request, byte[] buffer) throws IOException { // NOT IMPLEMENTED YET
		String str = "NOT IMPLEMENTED YET\n";
		System.out.print("[HTTP] POST: " + str);
		
		byte[] array = new byte[buffer.length - request.getHeaderLength()];
		for (int i = 0; i < array.length; i++) {
			array[i] = buffer[i + request.getHeaderLength()];
		}
		
		String data = new String(array);
		System.out.println("[HTTP] POST: DATA: " + data);
		
		String header = HTTP.buildResponse(HTTP.HTTP_OK, "text/plain", str.length());
		out.write(header.getBytes()); // Sending header
		out.write(str.getBytes());
	}
	
	@Override
	public void run() {
		if (this.socket == null) {
			System.out.println("[jserver] ERR: Null socket error!");
			return;
		}
		
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			byte[] buffer = new byte[4096];
			in.read(buffer, 0, buffer.length);
			
			HTTPRequest req = HTTP.parseRequest(new String(buffer).trim());
			System.out.println("[HTTP] " + req.getMethod() + ": " + socket.getInetAddress().getHostAddress() + " => " + req.getAddress());
			
			if(req.getMethod().equals("GET")) {
				methodGET(in, out, socket, req);
			} else if(req.getMethod().equals("POST")) {
				methodPOST(in, out, socket, req, buffer);
			}
			
			socket.close();
		} catch (Exception e) {
			System.out.println("ERR: " + e);
			if(Main.isDEBUG)
				e.printStackTrace();
		}
	}
}