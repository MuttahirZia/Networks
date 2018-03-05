import java.net.*;
import java.io.*;
import java.util.*;

public class Receiver {

	public static void main (String args[]) {

		try {

			//make GUI
			//get initial values


			//establish connection
			DatagramSocket dataSocket = new DatagramSocket(4444);
			//send data through
			DatagramSocket ackSocket = new DatagramSocket(); 
			//receive acks through


			//receive packets
			byte[] buf = new byte[1024];  
    		DatagramPacket dp = new DatagramPacket(buf, 1024);  
			dataSocket.receive(dp);
			String str = new String(dp.getData(), 0, dp.getLength());  
    		System.out.println(str + "\n");

   //  		dataSocket.receive(dp);
			// str = new String(dp.getData(), 0, dp.getLength());  
   //  		System.out.println(str  + "\n");
   //  		dataSocket.receive(dp);
			// str = new String(dp.getData(), 0, dp.getLength());  
   //  		System.out.println(str  + "\n");


    		//send ack packet
    		String testack = "ack received";
			InetAddress ip = InetAddress.getLocalHost();
			DatagramPacket da = new DatagramPacket(testack.getBytes(), testack.length(), ip, 4445);
			ackSocket.send(da);


			dataSocket.close();
			ackSocket.close();

		} catch(Exception e) {

			System.err.println("Error in Receiver main: " + e);
		}
	}
}
