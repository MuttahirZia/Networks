import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.*;

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
			int acc_port = Integer.parseInt(rec_port.getText());
			int go_port = Integer.parseInt(send_port.getText());

			//establish connection
			DatagramSocket dataSocket = new DatagramSocket(acc_port); //send data through
			DatagramSocket ackSocket = new DatagramSocket(); //receive acks through

			//receive packet init
			Boolean notEnd = true;
			Boolean endOfTranmission = false;
			byte[] buf;
			DatagramPacket dp;
			String str;
			int count = 1;
			Boolean drop = false;

			//ack packet init
			String testack = "ack received";
			InetAddress ip = InetAddress.getByName(address.getText()); 
			DatagramPacket da;

			//create file 
			FileOutputStream outputStream = new FileOutputStream(file_name.getText()); 

			while (notEnd) {

				buf = new byte[124];  
	    		dp = new DatagramPacket(buf, 124);

	    		if (count % 10 == 0 && !drop) {
	    			drop = true;
	    		} else {
	    			drop = false;
	    		}

				dataSocket.receive(dp);

	    		if (!(r2.isSelected() && drop)) {

	    			byte[] seqNumBuf = Arrays.copyOfRange(dp.getData(),0,4);
	    			byte[] dataBuf = Arrays.copyOfRange(dp.getData(),4,124);

    			    ByteBuffer seqByteBuffer = ByteBuffer.wrap(seqNumBuf);

	    			int n = seqByteBuffer.getInt();

	    			String r1 = new String(seqNumBuf, "UTF-8");
	    			String r2 = new String(dataBuf, "UTF-8");

	    			System.out.println("SEQ: " + n);
	    			System.out.println("DATA: " + r2);


	    			//send ack to sender
	    			da = new DatagramPacket(testack.getBytes(), testack.length(), ip, go_port);
					ackSocket.send(da);


					//check for end of transmission
	    			if (r2.contains(">>EOT<<")) {
	    				notEnd = false;
	    			} else {
	    				outputStream.write(dataBuf);
		    			count += 1;		
	    			}

	    			seqNumBuf = new byte[4];
	    			dataBuf = new byte[120];
				} 

				// l6.setText(String.valueOf(count));
				// redraw ();

	    	}
    	
	    	//clean up
			dataSocket.close();
			ackSocket.close();
			outputStream.close();

		} catch(Exception e) {

			System.err.println("Error in Receiver main: " + e);
		}
	}

	public void redraw () {
		validate();
		repaint();
	}

	public static void main (String []args) {
		new Receiver();
	}
}
