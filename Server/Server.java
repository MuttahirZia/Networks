import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	public Library lib = new Library();

	public static void main(String args[]) {
		try {			
			
			ServerSocket serverSocket = new ServerSocket(4444);
			
			while (true) {
				Socket clientSocket = serverSocket.accept();

				new Thread(new ServerThread(clientSocket)).start();
			}

		} catch (IOException e) {
			System.err.println(e);
		}
	}
}