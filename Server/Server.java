import java.net.*;
import java.io.*;

public class Server {
	public static void main(String args[]) {
		
		try {
			ServerSocket serverSocket = new ServerSocket(4444);
			
			while (true) {
				Socket clientSocket = serverSocket.accept();

				new ServerThread(clientSocket).start();
			}

		} catch (IOException e) {
			System.err.println(e);
		}
	}
}