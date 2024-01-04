package alex9932.delta.handlers;

import java.io.IOException;
import java.net.Socket;

import alex9932.delta.http.HTTPRequest;

public class STDHandler implements IRequestHandler {

	@Override
	public void onLoad() {
		System.out.println("Loaded STD request handler!");
	}

	@Override
	public void handle(HTTPRequest request, Socket socket) throws IOException {
		if (request.getMethod().equals("POST")) {
			byte[] buffer = new byte[1024];
			socket.getInputStream().read(buffer);
			System.out.println(new String(buffer));
			socket.close();
		}
		
		
	}

}
