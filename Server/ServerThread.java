import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
	private Socket socket;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			DataInputStream inStream = new DataInputStream(
				socket.getInputStream());

			PrintStream outStream = new PrintStream(
				socket.getOutputStream());

			while (true) {
					
				String line = inStream.readLine();
				outStream.println("Server:" + line);
			} 
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}