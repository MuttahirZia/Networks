import java.net.*;
import java.io.*;

public class ServerThread extends Server implements Runnable {
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

			String[] list1 = {"0123456789012","test1"};
			String[] list2 = {"0012213123121","test2"};
			boolean running = true;

			while (running) {
					
				String line = inStream.readLine();
				outStream.println("Server:" + line);
				
				//test display on server
				System.out.println("Sent:" + line);

				if (line.matches("submit")) {
					lib.submitBook(list1);
					lib.submitBook(list2);
					System.out.println("submit");
				}
				else if (line.matches("get")) {
					lib.getBook(0);
					lib.getBook(1);
					System.out.println("get");
				}
				else if (line.matches("remove")) {
					lib.removeBook(0);
					System.out.println("remove");
				}
				else if (line.matches("display")) {
					lib.displayLibrary();
					System.out.println("display");
				}
				else if (line.matches("exit")) {
					running = false;
					System.out.println("Closing");
				}
			} 

			outStream.close();
			inStream.close();
			socket.close();

		} catch (IOException e) {
			System.err.println(e);
		}
	}
}