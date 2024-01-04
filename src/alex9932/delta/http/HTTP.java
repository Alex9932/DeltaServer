package alex9932.delta.http;

import alex9932.delta.Main;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HTTP {
	public static final String HTTP_200 = "200 OK";
	public static final String HTTP_400 = "400 Bad Request";
	public static final String HTTP_401 = "401 Unauthorized";
	public static final String HTTP_403 = "403 Forbidden";
	public static final String HTTP_404 = "404 Not Found";
	public static final String HTTP_405 = "405 Method Not Allowed";
	public static final String HTTP_500 = "500 Internal Server Error";
	public static final String HTTP_501 = "501 Not Implemented";

	private static void append(StringBuilder sb, String key, String value) {
		sb.append(key);
		sb.append(": ");
		sb.append(value);
		sb.append("\n");
	}
	
	public static HTTPRequest parse(InputStream in) throws IOException {
		DataInputStream dis = new DataInputStream(in);
		String line = "";
		boolean first = true;
		HTTPRequest request = null;
		
		while((line = dis.readLine()) != null && !line.trim().equals("")) {
			if (Main.DEBUG) {
				System.out.println("[HTTP]: " + line);
			}
		
			String[] str;
			if (first) {
				first = false;
				str = line.split(" ");
				request = new HTTPRequest(str[0].trim(), str[1].trim(), str[2].trim());
			} else {
				str = line.split(":");
				request.put(str[0].trim(), str[1].trim());
			}
		}
		return request;
	}
	
	public static String buildResponse(String status, String contentType, long contentLength) {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 ");
		sb.append(status);
		sb.append("\n");

		append(sb, "Server", Main.SERVER);
		append(sb, "Content-Type", String.valueOf(contentType));
		append(sb, "Content-Length", String.valueOf(contentLength));
		
		sb.append("\n");
		
		if (Main.DEBUG) {
			System.out.println("[HTTP]: Response:\n" + sb.toString());
		}
		
		return sb.toString();
	}
}