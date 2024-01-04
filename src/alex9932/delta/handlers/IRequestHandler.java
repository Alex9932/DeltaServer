package alex9932.delta.handlers;

import java.io.IOException;
import java.net.Socket;

import alex9932.delta.http.HTTPRequest;

public interface IRequestHandler {
	void onLoad();
	void handle(HTTPRequest request, Socket socket) throws IOException;
}
