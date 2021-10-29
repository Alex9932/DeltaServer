package alex9932.delta;

import java.net.Socket;

public class Handler implements Runnable {
	Socket socket;

	public Handler(final Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		if (this.socket == null) {
			System.out.println("[jserver] ERR: Null socket error!");
			return;
		}
		Web.handle(this.socket);
	}
}
