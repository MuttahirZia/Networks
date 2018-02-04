import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	public static Library lib;

	public static void main(String args[]) {
		try {			
			lib = new Library();
			
			ServerSocket serverSocket = new ServerSocket(4444); //TODO change to variable
			
			while (true) {
				Socket clientSocket = serverSocket.accept();

				new Thread(new ServerThread(clientSocket)).start();
			}

		} catch (IOException e) {
			System.err.println(e);
		}
	}
}