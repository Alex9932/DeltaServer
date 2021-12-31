package alex9932.delta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import alex9932.delta.http.HTTPRequest;

public interface IRequestHandler {
	public boolean handle(RequestType type, HTTPRequest request, Socket socket, InputStream in, OutputStream out, byte[] buffer) throws IOException;
}