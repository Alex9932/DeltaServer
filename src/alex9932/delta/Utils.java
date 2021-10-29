package alex9932.delta;

import java.io.File;

public class Utils {
	public static String getType(File file) {
		// Text
		if(file.getName().endsWith(".htm") || file.getName().endsWith(".html")) {
			return "text/html; charset=UTF-8";
		} else if(file.getName().endsWith(".js")) {
			return "text/javascript";
		} else if(file.getName().endsWith(".css")) {
			return "text/css";
		} else if(file.getName().endsWith(".php")) {
			return "text/php";
		} else if(file.getName().endsWith(".xml")) {
			return "text/xml";
			
		// Audio
		} else if(file.getName().endsWith(".ogg")) {
			return "audio/ogg";
			
		// Image	
		} else if(file.getName().endsWith(".gif")) {
			return "image/gif";
		} else if(file.getName().endsWith(".jpeg") || file.getName().endsWith(".jpg")) {
			return "image/jpeg";
		} else if(file.getName().endsWith(".png")) {
			return "image/png";
		} else if(file.getName().endsWith(".webp")) {
			return "image/webp";
		} else if(file.getName().endsWith(".svg")) {
			return "image/svg";

		// Video
		} else if(file.getName().endsWith(".mp4")) {
			return "video/mp4";
		} else if(file.getName().endsWith(".webm")) {
			return "video/webm";
			
		// Application
		} else if(file.getName().endsWith(".zip")) {
			return "application/zip";
		} else if(file.getName().endsWith(".pdf")) {
			return "application/pdf";
		} else if(file.getName().endsWith(".doc") || file.getName().endsWith(".docx")) {
			return "application/msword";
		
		// Other
		} else if(file.getName().endsWith(".rar")) {
			return "application/x-rar-compressed";
		} else if(file.getName().endsWith(".ttf")) {
			return "application/x-font-ttf";
		} else if(file.getName().endsWith(".tar") || file.getName().endsWith(".tar.gz") || file.getName().endsWith(".tar.xz")) {
			return "application/x-tar";
		}

		return "application/octet-stream";
	}
	
}
