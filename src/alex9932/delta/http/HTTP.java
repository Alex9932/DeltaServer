package alex9932.delta.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import alex9932.delta.Main;

public class HTTP {
	public static final String HTTP_200 = "200 OK";
	public static final String HTTP_206 = "206 Partial Content";
	public static final String HTTP_400 = "400 Bad Request";
	public static final String HTTP_401 = "401 Unauthorized";
	public static final String HTTP_403 = "403 Forbidden";
	public static final String HTTP_404 = "404 Not Found";
	public static final String HTTP_405 = "405 Method Not Allowed";
	public static final String HTTP_500 = "500 Internal Server Error";
	public static final String HTTP_501 = "501 Not Implemented";
	
	private static HTTPHeader processInputStream(InputStream in) throws IOException {

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(in); // !!! DO NOT CLOSE THIS STREAM !!! //
		
		HTTPHeader request = null;

		String[] str;
		String   line  = "";
		boolean  first = true;
		

		if (Main.DEBUG) {
			System.out.printf("\n[HTTP]: Client -> Server:\n");
		}
		
		while(true) {
			line = sc.nextLine();
			
			if(line == null) { break; }
			
			// End of header
			// By default HTTP header splits from data by empty line
			if(line.trim().equals("")) { break; }

			if (Main.DEBUG) {
				System.out.printf("%s\n", line);
			}
			
			if (first) {
				first = false;
				str = line.split(" ");
				request = new HTTPHeader(str[0].trim(), str[1].trim(), str[2].trim());
			} else {
				str = line.split(":");
				request.put(str[0].trim(), str[1].trim());
			}
			
		}
		return request;
	}
	
	public static HTTPHeader parse(InputStream in) throws IOException {
		HTTPHeader header = processInputStream(in);
		if(header == null) {
			throw new IOException("Invalid HTTP haeader");
		}
		return header;
	}
	
	public static String buildResponse(String version, String status, String contentType, long contentLength, boolean keepAlive) {
		
		HTTPHeader header = new HTTPHeader(status, version);
		
		header.put("Content-Type", contentType);
		header.put("Content-Length", String.valueOf(contentLength));
		if(Main.EXPERIMENTAL_FEATURES) {
			header.put("Accept-Ranges", "bytes");
		}
		if(keepAlive) {
			header.put("Connection", "keep-alive");
		} else {
			header.put("Connection", "close");
		}
		
		return header.toString();
		
		/*
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/");
		sb.append(version);
		sb.append(" ");
		sb.append(status);
		sb.append("\n");

		append(sb, "Server", Main.SERVER);
		append(sb, "Content-Type", contentType);
		append(sb, "Content-Length", String.valueOf(contentLength));
		append(sb, "Accept-Ranges", "bytes");
		append(sb, "Connection", "close");
		
		sb.append("\n");
		
		if (Main.DEBUG) {
			System.out.printf("\n[HTTP]: Server -> Client:\n%s\n", sb.toString());
		}
		
		return sb.toString();
		*/
	}
}