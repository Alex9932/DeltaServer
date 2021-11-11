package alex9932.delta;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class Server implements Runnable {
	private ServerSocket server;
	private Thread thread;

	public Server(ServerSocket socket) {
		this.server = socket;
		this.thread = new Thread(this);
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Socket port: " + server.getLocalPort());
			while (Main.running) {
				Socket socket = server.accept();
				new Thread(new Handler(socket)).start();
			}
			server.close();
		} catch (Exception e) {
			Main.running = false;
			System.out.println("ERR: " + e);
			if(Main.isDEBUG)
				e.printStackTrace();
		}
	}
	
	public Thread getThread() {
		return thread;
	}

	public static void startHTTP() throws Exception {
		new Server(getSocket()).getThread().start();
	}

	public static void startHTTPS() throws Exception {
		new Server(getSecureSocket()).getThread().start();
	}

	private static ServerSocket getSocket() throws Exception {
		return new ServerSocket(Main.SERVER_PORT);
	}
	
	private static ServerSocket getSecureSocket() throws Exception {
		int backlog = 0;
		char[] keyStorePassword = "certpassword".toCharArray();                 // USED SELFSIGNED CERTIFICATE
		Path storePath = FileSystems.getDefault().getPath(".", "keystore.jks"); // USED SELFSIGNED CERTIFICATE

		ServerSocket serverSocket = getSslContext(storePath, keyStorePassword)
			.getServerSocketFactory()
			.createServerSocket(Main.SECURE_PORT, backlog);
		Arrays.fill(keyStorePassword, '0');
		return serverSocket;
	}

	private static SSLContext getSslContext(Path keyStorePath, char[] keyStorePass) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream(keyStorePath.toFile()), keyStorePass);
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, keyStorePass);
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
		return sslContext;
	}
}