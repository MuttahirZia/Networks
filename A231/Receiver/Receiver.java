import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Receiver extends JFrame implements ActionListener {
	JPanel interact = new JPanel ();
	JPanel two = new JPanel();

	JLabel l1 = new JLabel ("Senders host address: ");
	JLabel l2 = new JLabel ("Sender port number: ");
	JLabel l3 = new JLabel ("Receiver port number: ");
	JLabel l4 = new JLabel ("Name of output file: ");
	JLabel l5 = new JLabel ("Number of packets received:");
	JLabel l6 = new JLabel ("");

	JTextField address = new JTextField ();
	JTextField send_port = new JTextField ();
	JTextField rec_port = new JTextField ();
	JTextField file_name = new JTextField ();

	JButton transport = new JButton ("Transport");
	
	JRadioButton r1 = new JRadioButton("Reliable");    
	JRadioButton r2 = new JRadioButton("Unreliable");    

	ButtonGroup bg = new ButtonGroup();    

	public Receiver () {
		setLayout(new FlowLayout());	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,500);
		setTitle ("Receiver GUI");

		interact.setLayout(new GridLayout (7, 2, 20, 20));
		
		transport.addActionListener (this);

		l1.setPreferredSize(new Dimension (200, 50));
		l2.setPreferredSize(new Dimension (200, 50));
		l3.setPreferredSize(new Dimension (200, 50));
		l4.setPreferredSize(new Dimension (200, 50));

		interact.add (l1);
		interact.add (address);
		
		interact.add (l2);
		interact.add (send_port);

		interact.add (l3);
		interact.add (rec_port);

		interact.add (l4);
		interact.add (file_name);

		bg.add(r1);
		bg.add(r2);
		two.add(r1);
		two.add(r2);
		two.setVisible(true);

		interact.add(two);
		interact.add(transport);
		
		interact.add (l5);
		interact.add(l6);

		interact.setVisible (true);

		add (interact);
		setLocationRelativeTo (null);
		setVisible (true);		
	}

	 public void actionPerformed (ActionEvent e) {
	 	try {
	 		if (address.getText().equals("") || send_port.getText().equals("") || rec_port.getText().equals("") 
	 			|| file_name.getText().equals("") || (!r1.isSelected() && !r2.isSelected())) {
	 			throw new Exception ("You need to enter in all fields and select secure/unsecure before transport can begin.");
	 		} else {
	 			accept_packets ();
	 		}
	 	} catch (Exception x) {
	 		JOptionPane.showMessageDialog(null, x.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
	 	}
	 }

	public void accept_packets () {
		try {
			//establish connection
			DatagramSocket dataSocket = new DatagramSocket(4444); //send data through
			DatagramSocket ackSocket = new DatagramSocket(); //receive acks through

			//receive packet init
			Boolean notEnd = true;
			Boolean endOfTranmission = false;
			byte[] buf;
			DatagramPacket dp;
			String str;
			int count = 1;

			//ack packet init
			String testack = "ack received";
			InetAddress ip = InetAddress.getLocalHost();
			DatagramPacket da;

			while (notEnd) {

				buf = new byte[124];  
	    		dp = new DatagramPacket(buf, 124);

				dataSocket.receive(dp);
	    		if (!(r2.isSelected() && count % 10 == 0)) {
					str = new String(dp.getData(), 0, dp.getLength());  
	    			System.out.println(str + "END");

	    			if (str.contains(">>EOT<<")) {
	    				notEnd = false;
	    			}
				} 

				count += 1;
				l6.setText(String.valueOf(count));

    			da = new DatagramPacket(testack.getBytes(), testack.length(), ip, 4445);
				ackSocket.send(da);

	    	}
    	

			dataSocket.close();
			ackSocket.close();

		} catch(Exception e) {

			System.err.println("Error in Receiver main: " + e);
		}
	}

	public static void main (String []args) {
		new Receiver();
	}
}
