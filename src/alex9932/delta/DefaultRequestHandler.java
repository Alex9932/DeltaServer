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

public class DefaultRequestHandler implements IRequestHandler {
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
	
	@Override
	public boolean handle(RequestType type, HTTPRequest request, Socket socket, InputStream in, OutputStream out, byte[] buffer) throws IOException {
		if(type.equals(RequestType.POST)) {
			System.out.println("POST method is not implemented yet!");
			return false;
		}
		
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
		
		return true;
	}
}
