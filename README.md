# 🌀 DeltaServer

![Java](https://img.shields.io/badge/Java-8%2B-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

**DeltaServer** is a lightweight and easy-to-use web server written in **Java**.  
It’s designed to serve static files quickly and can be extended with custom request handler modules.

---

## 🚀 Features

- 📁 Serves static files via HTTP/HTTPS requests  
- 🧩 Simple module system for custom request handling  
- ⚙️ Minimal dependencies and easy setup  
- 🧠 Mime types handling

---

## ⚙️ Build & Run

Currently, **Delta** is built using **Eclipse IDE**. Compile it in IDE or download pre-build binaries.

Use .bat/.sh files or:
```bash
java -cp delta.jar alex9932.delta.Main [args]
```
By default, the server runs on port 80 and serves files from the www directory.

### Launch arguments
- Print help message -? / -h / -help
- Print version	-v / -version
- Use HTTPS -secure "keystore" "passwd"
- Set server port (default 80) -port 'http port"
- Set request handler module -module "module jar" "class"

---

## 🧩 Creating a Custom Module

DeltaServer supports user-defined modules for handling custom requests.
Example:

```java
public class CustomHandler implements IRequestHandler {

	private static final String HTTP_VERSION = "1.1";

	@Override
	public void onLoad() {
		System.out.println("Loaded custom handler!");
	}

	@Override
	public void handle(Socket socket) throws IOException {
		HTTPHeader header = HTTP.parse(socket.getInputStream());
		String resp = "Hello, world!";
		String resp_header = HTTP.buildResponse(HTTP_VERSION, HTTP.HTTP_200, Utils.getType(".html"), resp.length(), false);
		socket.getOutputStream().write(header.getBytes());
		socket.getOutputStream().write(resp.getBytes());
		socket.close();
	}
}
```
And use this by pass arguments: -module "modulename".jar "class"

---

## 📜 License

MIT License — free for personal and commercial use.
