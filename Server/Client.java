import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends Frame implements WindowListener {

	public Client() {

		setLayout(new FlowLayout());

		Label label1 = new Label("Title:");
		label1.setPreferredSize(new Dimension(200,40));
		add(label1);

		TextField txtf1 = new TextField();
		txtf1.setPreferredSize(new Dimension(200,40));
		add(txtf1);

		Button button1 = new Button("Submit");
		button1.setPreferredSize(new Dimension(200, 40));
		add(button1);

		Label label2 = new Label("Author:");
		label2.setPreferredSize(new Dimension(200,40));
		add(label2);

		TextField txtf2 = new TextField();
		txtf2.setPreferredSize(new Dimension(200,40));
		add(txtf2);

		Button button2 = new Button("Get");
		button2.setPreferredSize(new Dimension(200, 40));
		add(button2);

		Button btnConnect = new Button("Connect");
		btnConnect.setPreferredSize(new Dimension(200, 40));
		add(btnConnect);
		
		Button btnDisconnect = new Button("Disconnect");
		btnDisconnect.setPreferredSize(new Dimension(200, 40));
		add(btnDisconnect);

		addWindowListener(this);

		setTitle("Client");
		setSize(700,400);
		setVisible(true);
	}

	public void verifyIsbn(String isbn) {
		//verify ISBN input
	}

	@Override
	public void windowClosing(WindowEvent evt) {
		System.exit(0);
	}

	@Override 
	public void windowOpened(WindowEvent evt) { }
	@Override 
	public void windowClosed(WindowEvent evt) { }
	@Override 
	public void windowIconified(WindowEvent evt) { }
	@Override 
	public void windowDeiconified(WindowEvent evt) { }
	@Override 
	public void windowActivated(WindowEvent evt) { }
	@Override 
	public void windowDeactivated(WindowEvent evt) { }

	public static void main(String args[]) {

		new Client();

		try {
			Socket socket = new Socket("localhost", 4444);

			DataInputStream inStream = new DataInputStream(
				socket.getInputStream());

		    PrintStream outStream = new PrintStream(
		    	socket.getOutputStream());

		    DataInputStream inputLine = new DataInputStream(
		    	new BufferedInputStream(System.in));

		    String line;

		    outStream.println(inputLine.readLine());

		    while ((line = inStream.readLine()) != null) {
		    	System.out.println(line);
		    	if (line.indexOf("Ok") != -1) {
		    		break;
		    	}
		    	outStream.println(inputLine.readLine());
		    }

		    outStream.close();
		    inStream.close();
		    socket.close();

	    } catch (IOException e) {
			System.err.println(e);
		}
	}
}