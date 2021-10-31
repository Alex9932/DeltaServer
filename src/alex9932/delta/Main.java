package alex9932.delta;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

// https://github.com/Alex9932/DeltaServer

public class Main {
	public static final String SERVER     = "Deltaserver";
	public static final String VERSION    = "1.5";
	public static final String BUILD_NAME = "Shiro";
	
	public static String PLATFORM = "none";
	public static int SERVER_PORT = 80;
	public static int SECURE_PORT = 443;
	public static boolean running = true;

	private static boolean isHTTP = false; // Temporary disabled
	private static boolean isHTTPS = true;

	public static void main(String[] args) {
		System.out.println(SERVER + " " + VERSION + " (" + BUILD_NAME + ") is starting up...");

		PLATFORM = String.valueOf(
				System.getProperty("os.name")) +
				" " + System.getProperty("os.version") +
				" " + System.getProperty("os.arch");
		
		System.out.println("Platform: " + PLATFORM);

		try {
			if(isHTTP)
				new Server(getSocket()).getThread().start();
			
			if(isHTTPS)
				new Server(getSecureSocket(new InetSocketAddress("0.0.0.0", SECURE_PORT))).getThread().start();
		} catch (Exception e) {
			Main.running = false;
			System.out.println("ERR: " + e);
			e.printStackTrace();
		}
	}
	
	private static ServerSocket getSocket() throws Exception {
		return new ServerSocket(SERVER_PORT);
	}
	
	private static ServerSocket getSecureSocket(InetSocketAddress address) throws Exception {
		int backlog = 0;
		char[] keyStorePassword = "certpassword".toCharArray();                 // USED SELFSIGNED CERTIFICATE
		Path storePath = FileSystems.getDefault().getPath(".", "keystore.jks"); // USED SELFSIGNED CERTIFICATE

		ServerSocket serverSocket = getSslContext(storePath, keyStorePassword)
				.getServerSocketFactory()
				.createServerSocket(address.getPort(), backlog, address.getAddress());
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
