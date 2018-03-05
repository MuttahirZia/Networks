import java.net.*;
import java.io.*;
import java.util.*;

public class Sender {

	public static void main(String[] args) {

		try {

			// // <host address of the receiver> 
			// int port = Integer.parseInt(args[0]);
			// //<UDP port number used by the receiver 
			// //to receive data from the sender>
			// int dataPort = Integer.parseInt(args[1]);
			// //<UDP port number used by the sender to 
			// //receive ACKs from the receiver>
			// int ackPort = Integer.parseInt(args[2]);
			// //<name of the file to be transferred>
			String fileName = "test.txt";
			// //args[3];
			// //<timeout> integer number for timeout (in microseconds)	
			// int timeout = Integer.parseInt(args[4]);


			byte[] buffer = new byte[124];
            FileInputStream inputStream = new FileInputStream(fileName);
			//inputStream.read(buffer);

			//create datagramSocket
			DatagramSocket dataSocket = new DatagramSocket();//send data through
			DatagramSocket ackSocket = new DatagramSocket(4445);//receive acks through


			//create packets
			InetAddress ip = InetAddress.getLocalHost(); 
			DatagramPacket dp;

			Boolean notEnd = true;
			int i;

			while(notEnd) {
				
				if ((i = inputStream.read(buffer)) == -1) {
					notEnd = false;
				}

				dp = new DatagramPacket(buffer, 124, ip, 4444);
				dataSocket.send(dp);


				buffer = new byte[124];
				System.out.println(i);

			}
			


			//receive ack
			byte[] buf = new byte[1024];
			DatagramPacket da = new DatagramPacket(buf, 1024);
			ackSocket.receive(da);
			String strack = new String(da.getData(), 0, da.getLength());  
			System.out.println(strack);


			dataSocket.close();
			ackSocket.close();
			inputStream.close();

		} catch(Exception e) {

			System.err.println("Error in Sender main: " + e);
		}
	}
}
