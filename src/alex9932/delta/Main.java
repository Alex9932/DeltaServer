package alex9932.delta;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import alex9932.delta.handlers.IRequestHandler;
import alex9932.delta.handlers.STDHandler;

public class Main {
	public static final boolean EXPERIMENTAL_FEATURES = false;
	
	public static final String SERVER     = "Deltaserver";
	/*
	public static final String VERSION    = "2.x";
	public static final String BUILD_NAME = "Dev build";
	*/
	public static final String VERSION    = "2.2";
	public static final String BUILD_NAME = "Kohaku";
	
	
	private static final String MDL_DEFAULT = "alex9932.delta.handlers.FileIOHandler";
	private static final String MDL_PROXY   = "alex9932.delta.handlers.Proxy";
	
	// !!!Default values!!! Use -secure <YOUR KEYSTORE> <YOUR PASSWORD>
	public static String  KEY_STORE   = "keystore.jks";
	public static String  KEY_PASS    = "pass-less";
	
	public static String  PLATFORM    = "unknown";
	public static int     SERVER_PORT = 80;
	public static int     BUFFER_SIZE = 32768;
	public static boolean RUNNING     = true;
	public static boolean DEBUG       = false;
	public static boolean HTTPSECURE  = false;
	public static boolean PROXYMODE   = false;
	public static String  PROXYIP     = null;
	public static int     PROXYPORT   = 0;
	
	public static String  MODULE      = null;
	public static String  CLASS       = null;
	
	public static IRequestHandler handler = null;
	public static Server          server  = null;

	public static void main(String[] args) {
		System.out.println("Starting up...");
		
		CLASS    = MDL_DEFAULT;
		PLATFORM = System.getProperty("os.name") + " " +
				   System.getProperty("os.version") + " " +
				   System.getProperty("os.arch");
		
		if(parseCommandLine(args) != 0)
			System.exit(-1);

		printVersion();
		System.out.println("Platform: " + PLATFORM);

		if(DEBUG) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("WARNING: Server in running in DEBUG profile!");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		
		loadModule();
		server = new Server();
		
		while(RUNNING) {
			Thread server_thread = new Thread(server);
			server_thread.start();
			try {
				server_thread.join();
			} catch (InterruptedException ex) {
				System.out.println(ex);
			}
			
			//if (RUNNING) {
				System.out.println("SERVER INTERNAL ERROR! Restarting module...");
			//}
		}
		
		System.out.println("Server shutdown...");
	}
	
	private static void loadModule() {
		try {
			ClassLoader cl = Main.class.getClassLoader();
			if(MODULE != null) {
				File f = new File(MODULE);
				URL url = new URL("file://" + f.getAbsolutePath());
				cl = URLClassLoader.newInstance(new URL[] {url}, Main.class.getClassLoader());
			}
			Class<?> clazz = Class.forName(CLASS, true, (ClassLoader)cl);
			handler = (IRequestHandler)clazz.getDeclaredConstructor().newInstance();
			System.out.printf("Loaded module: %s\n", CLASS);
			handler.onLoad();
		} catch (Exception e1) {
			e1.printStackTrace();

			// Use default handler
			handler = new STDHandler();
			handler.onLoad();
		}
	}
	
	private static int parseCommandLine(String[] args) {
//		if(args.length == 0) {
//			printHelp();
//			return -1;
//		}
		
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-debug")) {
				DEBUG = true;
			} else if(args[i].equals("-secure")) {
				HTTPSECURE = true;
				if(i + 2 >= args.length) {
					System.out.println("Invalid argument: " + args[i]);
					printHelp();
					return -1;
				}
				KEY_STORE = args[i + 1];
				KEY_PASS  = args[i + 2];
				i += 2;
			} else if(args[i].equals("-proxy")) {
				if(i + 2 >= args.length) {
					System.out.println("Invalid argument: " + args[i]);
					printHelp();
					return -1;
				}
				PROXYIP = args[i + 1];
				PROXYPORT = Integer.parseInt(args[i + 2]);
				PROXYMODE = true;
				CLASS = MDL_PROXY;
				i += 2;
			} else if(args[i].equals("-module")) {
				if(i + 2 >= args.length) {
					System.out.println("Invalid argument: " + args[i]);
					printHelp();
					return -1;
				}
				MODULE = args[i + 1];
				CLASS  = args[i + 2];
				i += 2;
			} else if(args[i].equals("-port")) {
				if(i + 1 >= args.length) {
					System.out.println("Invalid argument: " + args[i]);
					printHelp();
					return -1;
				}
				SERVER_PORT = Integer.parseInt(args[i + 1]);
				i++;
			} else if(args[i].equals("-h") || args[i].equals("-?") || args[i].equals("-help")) {
				printHelp();
				return -1;
			} else if(args[i].equals("-v") || args[i].equals("-version")) {
				printVersion();
				return -1;
			} else {
				System.out.println("Invalid argument: " + args[i]);
				printHelp();
				return -1;
			}
		}
		
		if(PROXYMODE && MODULE != null) {
			System.out.println("You can not use proxy and external module simultaneously!");
			return -1;
		}
		
		return 0;
	}

	private static void printHelp() {
		System.out.println("~~~~~ " + SERVER + " " + VERSION + " (" + BUILD_NAME + ") ~~~~~");
		System.out.println(" -? / -h / -help               Print this help message");
		System.out.println(" -v / -version                 Print version");
		System.out.println(" -secure <keystore> <passwd>   Use HTTPS");
		System.out.println(" -proxy <dst ip> <port>        Use as proxy");
		System.out.println(" -port <http port>             Set server port (default 80)");
		System.out.println(" -module <module jar> <class>  Set request handler module");
		System.out.println("");
		System.out.println("Usage:");
		System.out.println(" Insecurejava -jar server.jar");
		System.out.println(" java -jar server.jar -secure -port 443");
	}

	private static void printVersion() {
		System.out.println(SERVER + " " + VERSION + " (" + BUILD_NAME + ") on " + PLATFORM);
	}
}