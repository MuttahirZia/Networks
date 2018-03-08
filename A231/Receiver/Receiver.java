import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.*;

public class Receiver extends JFrame {

	public static void main (String []args) {
		GUI client = new GUI ();

		client.transport.addActionListener (new ActionListener () {
			public void actionPerformed (ActionEvent e) {
			 	String senderIP = client.address.getText ();
			 	String senderPort = client.send_port.getText();
			 	String receiverPort = client.rec_port.getText();
			 	String fileName = client.file_name.getText();

			 	try {
			 		if (senderIP.equals("") || senderPort.equals("") || receiverPort.equals("") 
			 			|| fileName.equals("") || (!client.r1.isSelected() && !client.r2.isSelected())) {
			 			throw new Exception ("You need to enter in all fields and select secure/unsecure before transport can begin.");
			 		} else {
			 			Transport t = new Transport (client);
			 			Thread thread = new Thread (t); 

			 			thread.start();
			 		}
			 	} catch (Exception x) {
			 		JOptionPane.showMessageDialog(null, x.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
			 	}
			 }
		});
	}
}
