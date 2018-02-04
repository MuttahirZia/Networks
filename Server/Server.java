import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	public static Library lib;

	public static void main(String[] args) {

		try {		

			lib = new Library();

			int port = Integer.parseInt(args[0]);	
			ServerSocket serverSocket = new ServerSocket(port); //TODO change to variable
			
			while (true) {
				Socket clientSocket = serverSocket.accept();

				new Thread(new ServerThread(clientSocket)).start();
			}

		} catch (Exception e) {
			System.err.println(e);
		}
	}
}