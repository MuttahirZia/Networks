import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Server implements Runnable {
	private Socket socket;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {

			//open input and output streams to communicate to the client with
			PrintStream outStream = new PrintStream(socket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream ooStream = new ObjectOutputStream(socket.getOutputStream());

			//read request from client
            String[] line = (String[])inStream.readObject();
			boolean running = true;

			//listen for client requests and handle them as they arrive
			while (running) {

				String[] values = {line[1], line[2], line[3], line[4], line[5]};

				if (line[0].equals("Submit")) {
					outStream.println(lib.submitBook(values));
				}

				else if (line[0].equals("Remove")) {
					outStream.println(lib.removeBook(values));
				}

				else if (line[0].equals("Update")) {
					outStream.println(lib.updateBook(values));
				}

				else if (line[0].equals("Get")) {
					ooStream.writeObject(lib.getBook(values));
				}

				//read new request
	            line = (String[])inStream.readObject();
			} 

			socket.close();

		} catch (Exception e) {
			System.err.println("A client disconnected");
		}
	}
}