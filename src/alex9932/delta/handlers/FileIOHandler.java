package alex9932.delta.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import alex9932.delta.Main;
import alex9932.delta.Utils;
import alex9932.delta.http.HTTP;
import alex9932.delta.http.HTTPRequest;

public class FileIOHandler implements IRequestHandler {

	@Override
	public void onLoad() {
		System.out.println("Loaded FileIO handler!");
	}

	@Override
	public void handle(HTTPRequest request, Socket socket) throws IOException {
		if (request.getMethod().equals("POST")) {
			String header = HTTP.buildResponse(HTTP.HTTP_501, Utils.getType(".html"), 0L);
			socket.getOutputStream().write(header.getBytes());
			socket.close();
		} else {
			File f;
			if (request.getAddress().equals("/")) {
				f = new File("www/index.html");
			} else {
				f = new File("www" + request.getAddress());
			}
			
			if (f.exists()) {
				String header = HTTP.buildResponse(HTTP.HTTP_200, Utils.getType(f.getName()), f.length());
				socket.getOutputStream().write(header.getBytes());
				this.sendFile(f, socket.getOutputStream());
			} else {
				f = new File("err/404.html");
				FileInputStream fin = new FileInputStream(f);
				byte[] data = new byte[(int)f.length()];
				fin.read(data);
				fin.close();
				String s = new String(data);
				s = s.replace("%SERVER%", "Deltaserver").replace("%VERSION%", Main.VERSION).replace("%PLATFORM%", Main.PLATFORM).replace("%BUILD_NAME%", Main.BUILD_NAME).replace("%ACCESS_FILE%", request.getAddress());
				byte[] resp = s.getBytes();
				String header = HTTP.buildResponse("404 Not Found", Utils.getType(f.getName()), (long)resp.length);
				socket.getOutputStream().write(header.getBytes());
				socket.getOutputStream().write(resp);
			}
		}
			
		socket.close();
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