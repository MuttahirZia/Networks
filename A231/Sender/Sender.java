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

			byte[] buf;
			DatagramPacket da;
			String strack;

			Boolean next;

			// while(notEnd) {
				
			// 	if ((i = inputStream.read(buffer)) == -1) {
			// 		notEnd = false;
			// 	}

			// 	dp = new DatagramPacket(buffer, 124, ip, 4444);
			// 	dataSocket.send(dp);

			// 	buffer = new byte[124];
			// 	System.out.println(i);

			// 	//receive ack
			// 	buf = new byte[124];
			// 	da = new DatagramPacket(buf, 124);
			// 	ackSocket.receive(da);
			// 	strAck = new String(da.getData(), 0, da.getLength());  
			// 	System.out.println(strAck);

			// }

			while((i = inputStream.read(buffer)) != -1) {

                next = false;

                while (!next) {
                	try {

                		//send data packets
                		dp = new DatagramPacket(buffer, 124, ip, 4444);
						dataSocket.send(dp);
						ackSocket.setSoTimeout(10000);

						//listen for ack packet
						buf = new byte[124];
						da = new DatagramPacket(buf, 124);
						ackSocket.receive(da);
						strack = new String(da.getData(), 0, da.getLength());  
						System.out.println(strack);

						next = true;

                	} catch (SocketTimeoutException e) {
                		next = false;
                	}
                }
            }

			String EOTstring = ">>EOT<<";
			//buffer = EOTstring.getBytes();

			dp = new DatagramPacket(EOTstring.getBytes(), EOTstring.length(), ip, 4444);
			dataSocket.send(dp);
			

			dataSocket.close();
			ackSocket.close();
			inputStream.close();

		} catch (Exception e) {

			System.err.println("Error in Sender main: " + e);
		}
	}
}
