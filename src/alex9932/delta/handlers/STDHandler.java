package alex9932.delta.handlers;

import java.io.IOException;
import java.net.Socket;

import alex9932.delta.Main;
import alex9932.delta.http.HTTP;
import alex9932.delta.http.HTTPHeader;

public class STDHandler implements IRequestHandler {
	
	private static String DEFAULT_STR = "";

	@Override
	public void onLoad() {
		System.out.println("Loaded STD request handler!");
		DEFAULT_STR  = "DeltaServer " + Main.VERSION + " (" + Main.BUILD_NAME + ") on " + Main.PLATFORM;
		DEFAULT_STR += "\n\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n\n";
		DEFAULT_STR += "If you see this message, it means the web server is not configured correctly or the\n";
		DEFAULT_STR += "requested resource is unavailable. Please check the server settings, ensure the application\n";
		DEFAULT_STR	+= "is running, and verify the URL. If the issue persists, contact the server administrator.";
	}

	@Override
	public void handle(Socket socket) throws IOException {
		HTTPHeader header = HTTP.parse(socket.getInputStream());
		if (header.getMethod().equals("GET")) {
			socket.getOutputStream().write(DEFAULT_STR.getBytes());
			socket.close();
		}
	}
}
