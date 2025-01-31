package alex9932.delta.handlers;

import java.io.IOException;
import java.net.Socket;

public interface IRequestHandler {
	void onLoad();
	void handle(Socket socket) throws IOException;
}
