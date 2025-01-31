package alex9932.delta.http;

import java.util.HashMap;
import java.util.Iterator;

import alex9932.delta.Main;

public class HTTPHeader {
	private boolean isRequest; // Request from client
	private String  address;
	private String  method;
	private String  version;
	private HashMap<String, String> headers;
	
	//private int headerLength;
	
	// For request
	public HTTPHeader(String method, String address, String version) {
		this.headers   = new HashMap<String, String>();
		this.method    = method;
		this.address   = address;
		this.version   = version;
		this.isRequest = true;
	}
	
	// For response
	public HTTPHeader(String status, String version) {
		this.headers   = new HashMap<String, String>();
		this.method    = status;
		this.address   = "/response/"; // NOT USED IN RESPONSE HEADER
		this.version   = version;
		this.isRequest = false;
		
		// Add default headers
		this.put("Server", Main.SERVER + " " + Main.VERSION + " / " + Main.BUILD_NAME);
	}
	
	public void put(String header, String value) {
		headers.put(header, value);
	}
	
	public String get(String header) {
		return headers.get(header);
	}

	public String getAddress() {
		return this.address;
	}

	public String getMethod() {
		return this.method;
	}
	
	public String getVersion() {
		return version;
	}
/*
	public int getHeaderLength() {
		return headerLength;
	}
	
	public void setHeaderLength(int headerLength) {
		this.headerLength = headerLength;
	}
	*/
	private static void append(StringBuilder sb, String key, String value) {
		sb.append(key);
		sb.append(": ");
		sb.append(value);
		sb.append("\n");
	}
	
	private void appendHeaders(StringBuilder sb) {
		Iterator<String> keys = this.headers.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			String value = this.headers.get(key);
			append(sb, key, value);
		}
		
		sb.append("\n");
		
		if (Main.DEBUG) {
			if(this.isRequest) { System.out.printf("\n[HTTP]: Client -> Server:\n"); }
			else { System.out.printf("\n[HTTP]: Server -> Client:\n"); }
			System.out.printf("%s\n", sb.toString());
		}
	}
	
	private void buildRequestString(StringBuilder sb) {
		sb.append(this.method);
		sb.append(" ");
		sb.append(this.address);
		sb.append(" ");
		sb.append(this.version);
		sb.append("\n");
		appendHeaders(sb);
	}
	
	private void buildResponseString(StringBuilder sb) {
		sb.append("HTTP/");
		sb.append(this.version);
		sb.append(" ");
		sb.append(this.method);
		sb.append("\n");
		appendHeaders(sb);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(this.isRequest) { buildRequestString(sb); }
		else { buildResponseString(sb); }
		return sb.toString();
	}
}