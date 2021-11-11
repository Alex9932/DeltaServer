package alex9932.delta.http;

import java.util.HashMap;

public class HTTPRequest {
	private String address;
	private String method;
	private String version;
	private HashMap<String, String> headers;
	
	private int headerLength;
	
	public HTTPRequest(String method, String address, String version) {
		this.headers = new HashMap<String, String>();
		this.method = method;
		this.address = address;
		this.version = version;
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

	public int getHeaderLength() {
		return headerLength;
	}
	
	public void setHeaderLength(int headerLength) {
		this.headerLength = headerLength;
	}
}