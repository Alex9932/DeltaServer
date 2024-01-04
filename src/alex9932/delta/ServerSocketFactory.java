package alex9932.delta;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class ServerSocketFactory {

	public static ServerSocket newSocket() throws Exception {
		ServerSocket socket;
		
		if (Main.HTTPSECURE) {
			socket = getSecureSocket(Main.SERVER_PORT);
		} else {
			socket = new ServerSocket(Main.SERVER_PORT);
		}
		
		System.out.println("Socket opened! Port: " + Main.SERVER_PORT);
		return socket;
	}
	
	private static ServerSocket getSecureSocket(int port) throws Exception {
		int backlog = 0;
		char[] keyStorePassword = Main.KEY_PASS.toCharArray();
		Path storePath = FileSystems.getDefault().getPath(".", Main.KEY_STORE);
		ServerSocket serverSocket = getSslContext(storePath, keyStorePassword).getServerSocketFactory().createServerSocket(port, backlog);
		Arrays.fill(keyStorePassword, '0');
		return serverSocket;
	}
	
	private static SSLContext getSslContext(Path keyStorePath, char[] keyStorePass) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream(keyStorePath.toFile()), keyStorePass);
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, keyStorePass);
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagerFactory.getKeyManagers(), (TrustManager[])null, (SecureRandom)null);
		return sslContext;
	}

}
