import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
	JTextField inp = new JTextField ();
	JButton conn = new JButton("Connect");
	JButton help = new JButton ("Instructions");
	JLabel label1 = new JLabel("Port Number:");
	JFrame frame1 = new JFrame ("Connection");
	JPanel connection = new JPanel ();
	JPanel interact = new JPanel ();

	JLabel l2 = new JLabel ("ISBN (13 digit #)");
	JLabel l3 = new JLabel ("Title");
	JLabel l4 = new JLabel ("Author");
	JLabel l5 = new JLabel ("Publisher");
	JLabel l6 = new JLabel ("Year");

	JTextField isbn = new JTextField ();
	JTextField title = new JTextField ();
	JTextField author = new JTextField ();
	JTextField publisher = new JTextField ();
	JTextField year = new JTextField ();

	JButton submit = new JButton ("Submit");
	JButton remove = new JButton ("Remove");
	JButton update = new JButton ("Update");
	JButton get = new JButton ("Get");
	JButton disc = new JButton ("Disconnect");

	Socket socket;
	DataInputStream inStream;
	ObjectOutputStream outStream;
	ObjectInputStream oiStream;


	public Client () {
		setLayout(new FlowLayout());	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,250);
		setTitle ("Client Connection");

		label1.setPreferredSize(new Dimension(100,30));
		connection.add(label1);

		inp.setPreferredSize(new Dimension(100,30));
		connection.add (inp);
		
		conn.setPreferredSize(new Dimension(150, 30));
		conn.addActionListener(this);
		connection.add(conn); 

		// help.setPreferredSize (new Dimension (150, 30));
		// help.addActionListener (this);
		// connection.add(help);

		connection.setVisible(true);

		interact.setLayout(new GridLayout (5, 3, 10, 10));
		
		submit.addActionListener (this);
		remove.addActionListener (this);
		update.addActionListener (this);
		get.addActionListener (this);
		disc.addActionListener (this);

		l2.setPreferredSize(new Dimension (50, 25));
		l3.setPreferredSize(new Dimension (50, 25));
		l4.setPreferredSize(new Dimension (50, 25));
		l5.setPreferredSize(new Dimension (50, 25));
		l6.setPreferredSize(new Dimension (50, 25));

		interact.add (l2);
		interact.add (isbn);
		interact.add (submit);
		
		interact.add (l3);
		interact.add (title);
		interact.add (remove);

		interact.add (l4);
		interact.add (author);
		interact.add (update);

		interact.add (l5);
		interact.add (publisher);
		interact.add (get);

		interact.add (l6);
		interact.add (year);
		interact.add (disc);

		interact.setVisible (false);
		add (connection);
		add (interact);

		setLocationRelativeTo (null);
		setVisible (true);
		
	}

	public void actionPerformed (ActionEvent e) {
		String command = e.getActionCommand();
		System.out.println(command);
		boolean success;
		boolean valid;

		try {
			if (command.equals("Connect")) {
				if (inp.getText().length() == 0) {
					throw new Exception ("Need to enter in a port number.");
				}
				success = test (inp.getText());

				if (success == true) {
					System.out.println ("Connection made.");
					connection.setVisible (false);
					inp.setText("");
					interact.setVisible(true);

				} else {
					throw new Exception ("Wrong port number. Cannot connect to server.");
				}
			} else if (command.equals ("Disconnect")) {
				outStream.close();
				inStream.close();
				socket.close();

				interact.setVisible(false);
				connection.setVisible(true);
			} else if (command.equals ("Submit")) {
				success = verifySubmit ();
				valid = validISBN(isbn.getText());

				if (success && valid) {
					//System.out.println ("Adding to library");
					lib_action ("Submit");

				} else {
					throw new Exception ("You need to specify a valid ISBN, title, and author to add a book to the library");
				}
			} else if (command.equals ("Remove")) {
				success = verifyRemove();

				if (success) {
					lib_action ("Remove");

				} else {
					throw new Exception ("You need to specify at least one field correctly (i.e. ISBN) in order to remove books from the library");
				}
			} else if (command.equals("Update")) {
				success = verifyUpdate ();
				valid = validISBN(isbn.getText());

				if (success && valid) {
					lib_action ("Update");

				} else {
					throw new Exception ("A valid ISBN and minimum one other field must be filled to update a book in the library.");
				}
			} else if (command.equals ("Get")) {
				if (isbn.getText().length() != 0) {
					valid = validISBN (isbn.getText());

					if (valid) {
						get_action("Get");
					} else {
						throw new Exception ("You need a valid ISBN if you want to search for something, or search for nothing to get all.");
					}
				} else {
					get_action ("Get");
				}
			}
		} catch (Exception x) {
			JOptionPane.showMessageDialog(null, x.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void lib_action (String action) {
		String [] input = {action, isbn.getText(), title.getText(), author.getText(), publisher.getText(), year.getText()};
		//System.out.println (input[0] + " , " + input[1] + " , " + input[2] + " , " + input[3] + 
			//" , " + input[4] + " , " + input[5]);

		try{
			String status;

			outStream.writeObject(input);
			status = inStream.readLine();

			JOptionPane.showMessageDialog(null, status);
			//System.out.println(status);

		} catch(Exception e) {
			System.out.println (e.getMessage());
		}
	}

	public void get_action (String action) {
		String [] input = {"Get", isbn.getText(), title.getText(), author.getText(), publisher.getText(), year.getText()};

		try{
			String status;
			
			outStream.writeObject(input);
			String [][] ret = (String[][])oiStream.readObject();

			int j = 0;
			while(ret[j] != null) {
				System.out.println (ret[j][0] + " , " + ret[j][1] + " , " + ret[j][2] + " , " + ret[j][3] + 
					" , " + ret[j][4]);
				j++;
			}

		} catch(Exception e) {
			//JOptionPane.showMessageDialog(null, status, "Server Message");
		}

	}

	public boolean verifySubmit () {
		if (title.getText().trim().length()==0 || author.getText().trim().length()==0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean verifyRemove () {
		if (isbn.getText().length() ==0 && (title.getText().trim().length()==0 && author.getText().trim().length()==0
		 && year.getText().trim().length()==0 && publisher.getText().trim().length()==0)) {
			return false;
		} else if (isbn.getText().length() != 0 && validISBN(isbn.getText()) ==false) {
			return false;
		} else {
			return true;
		}
	}

	public boolean verifyUpdate () {
		if (validISBN(isbn.getText()) ==false || (title.getText().trim().length()==0 && author.getText().trim().length()==0
		 && year.getText().trim().length()==0 && publisher.getText().trim().length()==0)) {
			return false;
		} else {
			return true;
		}
	}

	public static void main (String []args) {
		new Client();
	}

	public boolean validISBN (String code) {
		if ( code == null ) {
            return false;
        }

        //remove any hyphens
        code = code.replaceAll( "-", "" );

        //must be a 13 digit ISBN
        if ( code.length() != 13 ) {
            return false;
        }

        try {
            int total = 0;
            for ( int i = 0; i < 12; i++ ) {
                int digit = Integer.parseInt( code.substring( i, i + 1 ) );
                total += (i % 2 == 0) ? digit * 1 : digit * 3;
            }

            //checksum must be 0-9. If calculated as 10 then = 0
            int checksum = 10 - (total % 10);
            if (checksum == 10 ) {
                checksum = 0;
            }

            return checksum == Integer.parseInt(code.substring( 12 ) );
        }
        catch ( NumberFormatException nfe ){
            //to catch invalid ISBNs that have non-numeric characters in them
            return false;
        }
	}

	public boolean test(String port) {
		try {
			socket = new Socket("localhost", Integer.parseInt(port));
			inStream = new DataInputStream(socket.getInputStream());
			outStream = new ObjectOutputStream(socket.getOutputStream());
			oiStream = new ObjectInputStream(socket.getInputStream());
			return true;

	    } catch (Exception e) {
			return false;
		}
	}
}