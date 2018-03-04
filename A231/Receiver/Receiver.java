import java.net.*;
import java.io.*;
import java.util.*;

public class Receiver {

	public static void main (String args[]) {

		try {

			//make GUI
			//get initial values

			//establish connection
			DatagramSocket dataSocket = new DatagramSocket(4444);//port, dataPort); //send data through
			DatagramSocket ackSocket = new DatagramSocket(); //receive acks through

			for (int i = 0; i < 2; i++){
				//receive packets
				byte[] buf = new byte[1024];  
	    		DatagramPacket dp = new DatagramPacket(buf, 1024);  
				dataSocket.receive(dp);
				String str = new String(dp.getData(), 0, dp.getLength());  
	    		System.out.println(str);

	    		// dataSocket.receive(dp);
	    		// String str1 = new String(dp.getData(), 0, dp.getLength());  
	    		// System.out.println(str1);


	    		//send ack packet
	    		String testack = "ack unloaded";
				InetAddress ip = InetAddress.getLocalHost();
				DatagramPacket da = new DatagramPacket(testack.getBytes(), testack.length(), ip, 4445);
				ackSocket.send(da);
			}



			dataSocket.close();
			ackSocket.close();

		} catch(Exception e) {

			System.err.println("Error in Receiver main: " + e);
		}
	}
}
