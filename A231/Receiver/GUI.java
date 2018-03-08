import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.*;

public class GUI extends JFrame {
	JFrame frame = new JFrame ();
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

	public GUI () {
		frame.setLayout(new FlowLayout());	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setTitle ("Receiver GUI");

		interact.setLayout(new GridLayout (7, 2, 20, 20));

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

		frame.add (interact);
		frame.setLocationRelativeTo (null);
		frame.setVisible (true);		
	}
}