import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.util.Timer;

public class Sender {

	public static void main(String[] args) {

		final int packetSize = 124;

		try {

			//<host address of the receiver> 
			String address = args[0];
			//<UDP port number used by the receiver to receive data from the sender>
			int dataPort = Integer.parseInt(args[1]);
			//<UDP port number used by the sender to receive ACKs from the receiver>
			int ackPort = Integer.parseInt(args[2]);
			//<name of the file to be transferred>
			String fileName = args[3];
			//<timeout> integer number for timeout (in microseconds)	
			int timeout = Integer.parseInt(args[4]);


			//read file and fill buffer with file contents
			byte[] buffer = new byte[120];
            FileInputStream inputStream = new FileInputStream(fileName);

			//create datagramSocket
			DatagramSocket dataSocket = new DatagramSocket();//send data through
			DatagramSocket ackSocket = new DatagramSocket(ackPort);//receive acks through

			//create packets
			InetAddress ip = InetAddress.getByName(address); 
			DatagramPacket dp;
			Boolean notEnd = true;
			int i;
			Boolean next;
			byte[] buf;
			DatagramPacket da;
			String strack;

			int sequenceNumber = 0;
			byte[] seqNumBuf = new byte[4];
			byte[] packetBuffer;// = new byte[packetSize];

			System.out.println("Start timer");
			long startTime = System.nanoTime();

			//begin main sending loop
			while((i = inputStream.read(buffer)) != -1) {

                next = false;

                while (!next) {
                	try {

                		//send data packets
                		seqNumBuf = ByteBuffer.allocate(4).putInt(sequenceNumber).array();
                		//add byte[]s
						packetBuffer = new byte[seqNumBuf.length + buffer.length];
						System.arraycopy(seqNumBuf, 0, packetBuffer, 0, seqNumBuf.length);
						System.arraycopy(buffer, 0, packetBuffer, seqNumBuf.length, buffer.length);


                		dp = new DatagramPacket(packetBuffer, packetSize, ip, dataPort);
						dataSocket.send(dp);
						ackSocket.setSoTimeout(timeout);//timeout);

						//listen for acknowledgemnt packet
						buf = new byte[packetSize];
						da = new DatagramPacket(buf, packetSize);
						ackSocket.receive(da);

						//diplay acknowledgement
						strack = new String(da.getData(), 0, da.getLength());  
						//System.out.println(strack);

						//continue sending until inputStream returns empty
						next = true;

                	} catch (SocketTimeoutException e) {
                		
                		//timeout occured resend last packet
                		next = false;
                	}
                }

                buffer = new byte[packetSize];
                sequenceNumber++;

            }

            //send end of transmission
			String EOTstring = "    >>EOT<<";
			dp = new DatagramPacket(EOTstring.getBytes(), EOTstring.length(), ip, dataPort);
			dataSocket.send(dp);

			//receive acknowledgement for end of transmission
			buf = new byte[packetSize];
			da = new DatagramPacket(buf, packetSize);
			ackSocket.receive(da);

			//display end of transmission acknowledgemnt
			strack = new String(da.getData(), 0, da.getLength());  
			//System.out.println(strack + " EOT");


			long stopTime = System.nanoTime();
			long elapsedTime = stopTime - startTime;
			double seconds = (double)elapsedTime / 1000000000.0;
     		System.out.println("Total time elapsed: " + seconds);

			
			//clean up
			dataSocket.close();
			ackSocket.close();
			inputStream.close();

		} catch (Exception e) {

			System.err.println("Error in Sender main: " + e);
		}
	}
}
