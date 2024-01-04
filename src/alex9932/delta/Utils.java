package alex9932.delta;

public class Utils {
	public static String getType(String file) {
		// Text
		if(file.endsWith(".htm") || file.endsWith(".html")) {
			return "text/html; charset=UTF-8";
		} else if(file.endsWith(".js")) {
			return "text/javascript";
		} else if(file.endsWith(".css")) {
			return "text/css";
		} else if(file.endsWith(".php")) {
			return "text/php";
		} else if(file.endsWith(".xml")) {
			return "text/xml";
			
		// Audio
		} else if(file.endsWith(".ogg")) {
			return "audio/ogg";
			
		// Image	
		} else if(file.endsWith(".gif")) {
			return "image/gif";
		} else if(file.endsWith(".jpeg") || file.endsWith(".jpg")) {
			return "image/jpeg";
		} else if(file.endsWith(".png")) {
			return "image/png";
		} else if(file.endsWith(".webp")) {
			return "image/webp";
		} else if(file.endsWith(".svg")) {
			return "image/svg";

		// Video
		} else if(file.endsWith(".mp4")) {
			return "video/mp4";
		} else if(file.endsWith(".webm")) {
			return "video/webm";
			
		// Application
		} else if(file.endsWith(".zip")) {
			return "application/zip";
		} else if(file.endsWith(".pdf")) {
			return "application/pdf";
		} else if(file.endsWith(".doc") || file.endsWith(".docx")) {
			return "application/msword";
		
		// Other
		} else if(file.endsWith(".rar")) {
			return "application/x-rar-compressed";
		} else if(file.endsWith(".ttf")) {
			return "application/x-font-ttf";
		} else if(file.endsWith(".tar") || file.endsWith(".tar.gz") || file.endsWith(".tar.xz")) {
			return "application/x-tar";
		}

		return "application/octet-stream";
	}
	
}
