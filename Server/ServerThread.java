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

			boolean running = true;

			while (running) {
					
				String line = inStream.readLine();
				outStream.println("Server:" + line);
				//test display on server
				System.out.println("Sent:" + line);

				if (line.matches("submit")) {
					lib.submitBook("0123456789012","test1","","","hi");
					lib.submitBook("0012213123121","test3","","","hello");
					lib.submitBook("1012213123121","test3","","","hello");
					System.out.println("submit");
				}

				else if (line.matches("get")) {
					String[] t1 = new String[] {"","test3","","",""};
					String[] t2 = new String[] {"0012213123121","test3","","","hello"};
					lib.getBook(t1, t2);
					outStream.println("get");
				}

				else if (line.matches("remove")) {
					lib.removeBook(0);
					System.out.println("remove");
				}

				else if (line.matches("update")) {
					String t1 = "0012213123121";
					String[] t2 = new String[] {"0012213123121","test3","","","hello"};
					lib.updateBook(t1,t2);
				}

				else if (line.matches("display")) {
					System.out.println("display");
					lib.displayLibrary();
					System.out.println("now search");
					String[] t1 = new String[] {"","test2","","",""};
					String[] t2 = new String[] {"0012213123121","test1","","","hello"};
					lib.searchLibrary(t1,t2);
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