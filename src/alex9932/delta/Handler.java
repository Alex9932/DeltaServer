package alex9932.delta;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import alex9932.delta.http.HTTP;
import alex9932.delta.http.HTTPRequest;

public class Handler implements Runnable {
	private static IRequestHandler request_handler = null;
	
	public static void setHandler(IRequestHandler handler) {
		request_handler = handler;
	}
	
	private Socket socket;

	public Handler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		if (this.socket == null) {
			System.out.println("[jserver] ERR: Null socket error!");
			return;
		}
		
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			byte[] buffer = new byte[4096];
			in.read(buffer, 0, buffer.length);
			
			HTTPRequest req = HTTP.parseRequest(new String(buffer).trim());
			System.out.println("[HTTP] " + req.getMethod() + ": " + socket.getInetAddress().getHostAddress() + " => " + req.getAddress());
			
			if(request_handler == null) {
				System.out.println("Request handler is not defined!");
				socket.close();
				return;
			}
			
			RequestType type = RequestType.GET;
			
			if(req.getMethod().equals("POST")) {
				type = RequestType.POST;
			}
			
			request_handler.handle(type, req, socket, in, out, buffer);
			
			socket.close();
		} catch (Exception e) {
			System.out.println("ERR: " + e);
			if(Main.isDEBUG)
				e.printStackTrace();
		}
	}
}