import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	public static Library lib;

	public static void main(String[] args) {

		try {		

			//initialize the library once the server begins
			lib = new Library();

			//establish server socket with specified port
			int port = Integer.parseInt(args[0]);	
			ServerSocket serverSocket = new ServerSocket(port);
			
			//server listens for new clients to accept and creates new threads for them
			while (true) {
				Socket clientSocket = serverSocket.accept();

				new Thread(new ServerThread(clientSocket)).start();
			}

		} catch (Exception e) {
			System.err.println("Error with establishing server or incorrect port parameters entered");
		}
	}
}