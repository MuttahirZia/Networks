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

			PrintStream outStream = new PrintStream(socket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream ooStream = new ObjectOutputStream(socket.getOutputStream());

            String[] line = (String[])inStream.readObject();
            //System.out.println("String is: '" + Arrays.toString(line) + "'");


			boolean running = true;

			while (running) {

				String[] values = {line[1], line[2], line[3], line[4], line[5]};


				if (line[0].equals("Submit")) {
					outStream.println(lib.submitBook(values));
					lib.displayLibrary();
				}

				if (line[0].equals("Remove")) {
					outStream.println(lib.removeBook(values));
					lib.displayLibrary();
				}

				if (line[0].equals("Update")) {
					outStream.println(lib.updateBook(values));
					lib.displayLibrary();
				}

				if (line[0].equals("Get")) {
					ooStream.writeObject(lib.getBook(values));
				}

	            line = (String[])inStream.readObject();
			} 

			
			socket.close();

		} catch (Exception e) {
			System.err.println(e);
		}
	}
}