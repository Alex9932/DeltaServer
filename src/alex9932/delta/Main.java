package alex9932.delta;

// https://github.com/Alex9932/DeltaServer

public class Main {
	public static final String SERVER     = "Deltaserver";
	public static final String VERSION    = "1.6 pre 1";
	public static final String BUILD_NAME = "Kaito";
	
	public static String PLATFORM = "none";
	public static int SERVER_PORT = 80;
	public static int SECURE_PORT = 443;
	public static boolean running = true;

	private static boolean isHTTP = true;
	private static boolean isHTTPS = false;

	//-secure -nohttp -ports 8443
	public static void main(String[] args) {
		PLATFORM = String.valueOf(
				System.getProperty("os.name")) +
				" " + System.getProperty("os.version") +
				" " + System.getProperty("os.arch");
		if(parseCommandLine(args) != 0)
			System.exit(-1); // Exit
		
		System.out.println("Platform: " + PLATFORM);

		System.out.println(SERVER + " " + VERSION + " (" + BUILD_NAME + ") is starting up...");
		try {
			if(isHTTP)
				Server.startHTTP();
			
			if(isHTTPS)
				Server.startHTTPS();
		} catch (Exception e) {
			Main.running = false;
			System.out.println("ERR: " + e);
			e.printStackTrace();
		}
	}
	
	private static int parseCommandLine(String[] args) {
		if(args.length == 0) {
			printHelp();
			return -1;
		}
		
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-secure")) {
				isHTTPS = true;
			} else if(args[i].equals("-nohttp")) {
				isHTTP = false;
			} else if(args[i].equals("-port")) {
				if(i + 1 >= args.length) {
					System.out.println("Invalid argument: " + args[i]);
					printHelp();
					return -1;
				}
				SERVER_PORT = Integer.parseInt(args[i + 1]);
				i++;
			} else if(args[i].equals("-ports")) {
				if(i + 1 >= args.length) {
					System.out.println("Invalid argument: " + args[i]);
					printHelp();
					return -1;
				}
				SECURE_PORT = Integer.parseInt(args[i + 1]);
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
		
		return 0;
	}

	private static void printHelp() {
		System.out.println("~~~~~ " + SERVER + " " + VERSION + " (" + BUILD_NAME + ") ~~~~~");
		System.out.println(" -? / -h / -help      Print this help message");
		System.out.println(" -v / -version        Print version");
		System.out.println(" -secure              Use HTTPS");
		System.out.println(" -nohttp              Disable HTTP");
		System.out.println(" -port <http port>    Set HTTP server port (default 80)");
		System.out.println(" -ports <https port>  Set HTTPS server port (default 443)");
		System.out.println("");
		System.out.println("Usage:");
		System.out.println(" java -jar server.jar -secure -nohttp -port 8080 -ports 8443");
	}

	private static void printVersion() {
		System.out.println(SERVER + " " + VERSION + " (" + BUILD_NAME + ")");
		System.out.println("Runned on " + PLATFORM);
	}
}
