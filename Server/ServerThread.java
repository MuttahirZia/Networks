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
			// DataInputStream inStream = new DataInputStream(socket.getInputStream());
			PrintStream outStream = new PrintStream(socket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

            String[] line = (String[])inStream.readObject();
            System.out.println("String is: '" + Arrays.toString(line) + "'");


			boolean running = true;

			while (running) {

				String[] values = {line[1], line[2], line[3], line[4], line[5]};


				if (line[0].equals("Submit")) {
					System.out.println ("ADDING: " + values[0] + " , " + values[1] 
						+ " , " + values[2] + " , " + values [3] + " , "  + values[4]);
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


	            line = (String[])inStream.readObject();


				// else if (line.matches("get")) {
				// 	String[] t1 = new String[] {"","test3","","",""};
				// 	String[] t2 = new String[] {"0012213123121","test3","","","hello"};
				// 	lib.getBook(t1, t2);
				// 	outStream.println("get");
				// }

				// else if (line.matches("remove")) {
				// 	String[] t1 = new String[] {"","test3","","",""};
				// 	String[] t2 = new String[] {"0012213123121","test3","","","hello"};
				// 	boolean removed = lib.removeBook(t1,t2);
				// 	System.out.println(removed);
				// }

				// else if (line.matches("update")) {
				// 	String t1 = "0012213123121";
				// 	String[] t2 = new String[] {"0012213123121","test3","","","hello"};
				// 	lib.updateBook(t1,t2);
				// }

				// else if (line.matches("display")) {
				// 	System.out.println("display");
				// 	lib.displayLibrary();
				// 	System.out.println("now search");
				// 	String[] t1 = new String[] {"","test2","","",""};
				// 	String[] t2 = new String[] {"0012213123121","test1","","","hello"};
				// 	lib.searchLibrary(t1,t2);
				// }

				// else if (line.matches("exit")) {
				// 	running = false;
				// 	System.out.println("Closing");
				// }
			} 

			
			//socket.close();

		} catch (Exception e) {
			System.err.println(e);
		}
	}
}