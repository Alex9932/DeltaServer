package alex9932.delta.http;

import alex9932.delta.Main;

public class HTTP {
	public static final String HTTP_OK = "200 OK";
	public static final String HTTP_NOT_FOUND = "404 Not Found";

	private static void append(StringBuilder sb, String key, String value) {
		sb.append(key);
		sb.append(": ");
		sb.append(value);
		sb.append("\n");
	}
	
	public static HTTPRequest parseRequest(String req) {
		String[] httpreq = req.split("\n");
		String[] reqString = httpreq[0].split(" ");
		
		HTTPRequest request = new HTTPRequest(reqString[0], reqString[1], reqString[2]);
		
		for (int i = 1; i < httpreq.length; i++) {
			String[] header = httpreq[i].split(":");
			request.put(header[0].trim(), header[1].trim());
		}
		
		return request;
	}
	
	public static String buildResponse(String status, String contentType, long contentLength) {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.x ");
		sb.append(status);
		sb.append("\n");

		append(sb, "Server", Main.SERVER);
		append(sb, "Content-Type", String.valueOf(contentType));
		append(sb, "Content-Length", String.valueOf(contentLength));
		
		sb.append("\n");
		return sb.toString();
	}

}
