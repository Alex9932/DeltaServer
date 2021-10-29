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

public class Web {
	private static void sendFile(File file, OutputStream out) throws IOException {
		byte[] buffer = new byte[4096];
		FileInputStream fin = new FileInputStream(file);
		while (fin.available() > 4096) {
			fin.read(buffer, 0, 4096);
			out.write(buffer);
		}
		if (fin.available() > 0) {
			final byte[] s_buffer = new byte[fin.available()];
			fin.read(s_buffer, 0, s_buffer.length);
			out.write(s_buffer);
		}
		fin.close();
	}
	
	private static void sendErrorPage(File file, OutputStream out, String addr) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		byte[] b_buffer = new byte[(int)file.length()];
		fin.read(b_buffer);
		fin.close();

		String str = new String(b_buffer, StandardCharsets.UTF_8);
		out.write(str
			.replace("%PLATFORM%", Main.PLATFORM)
			.replace("%SERVER%", Main.SERVER)
			.replace("%VERSION%", Main.VERSION)
			.replace("%ACCESS_FILE%", addr).getBytes());
	}

	private static void methodGET(InputStream in, OutputStream out, Socket socket, String addr) throws IOException {
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

	private static void methodPOST(InputStream in, OutputStream out, Socket socket, String addr) throws IOException {
		// NOT IMPLEMENTED YET
	}
	
	public static void handle(Socket socket) {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			byte[] buffer = new byte[4096];
			in.read(buffer, 0, buffer.length);
			
			HTTPRequest req = HTTP.parseRequest(new String(buffer).trim());
			System.out.println("[HTTP] " + req.getMethod() + ": " + socket.getInetAddress().getHostAddress() + " => " + req.getAddress());
			
			if(req.getMethod().equals("GET")) {
				methodGET(in, out, socket, req.getAddress());
			} else if(req.getMethod().equals("POST")) {
				methodPOST(in, out, socket, req.getAddress());
			}
			
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
