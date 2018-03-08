import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.*;

public class Transport implements Runnable {
	GUI client; 
	int count = 0;

	public Transport (GUI g) {
		this.client = g;
	}

	public void run () {
		try {
			int acc_port = Integer.parseInt(client.rec_port.getText());
			int go_port = Integer.parseInt(client.send_port.getText());

			//establish connection
			DatagramSocket dataSocket = new DatagramSocket(acc_port); //send data through
			DatagramSocket ackSocket = new DatagramSocket(); //receive acks through

			//receive packet init
			Boolean notEnd = true;
			Boolean endOfTranmission = false;
			byte[] buf;
			DatagramPacket dp;
			String str;
			Boolean drop = false;

			//ack packet init
			String testack = "ack received";
			InetAddress ip = InetAddress.getByName(client.address.getText()); 
			DatagramPacket da;

			//create file 
			FileOutputStream outputStream = new FileOutputStream(client.file_name.getText()); 

			while (notEnd) {

				buf = new byte[124];  
	    		dp = new DatagramPacket(buf, 124);

	    		if (count != 0 && count % 10 == 0 && !drop) {
	    			drop = true;
	    		} else {
	    			drop = false;
	    		}

				dataSocket.receive(dp);

	    		if (!(client.r2.isSelected() && drop)) {

	    			byte[] seqNumBuf = Arrays.copyOfRange(dp.getData(),0,4);
	    			byte[] dataBuf = Arrays.copyOfRange(dp.getData(),4,124);

    			    ByteBuffer seqByteBuffer = ByteBuffer.wrap(seqNumBuf);

	    			int n = seqByteBuffer.getInt();

	    			String seq = new String(seqNumBuf, "UTF-8");
	    			String dat = new String(dataBuf, "UTF-8");


	    			//send ack to sender
	    			da = new DatagramPacket(testack.getBytes(), testack.length(), ip, go_port);
					ackSocket.send(da);


					//check for end of transmission
	    			if (dat.contains(">>EOT<<")) {
	    				notEnd = false;
	    			} else {
	    				outputStream.write(dataBuf);
		    			count += 1;		
	    			}

	    			seqNumBuf = new byte[4];
	    			dataBuf = new byte[120];
				} 

			client.l6.setText("" + count);
			client.frame.validate ();
			client.frame.repaint();
		

	    	}
    	
	    	//clean up
			dataSocket.close();
			ackSocket.close();
			outputStream.close();

		} catch(Exception e) {
			System.err.println("Error in Receiver main: " + e);
		}

	client.finished();
	}
}