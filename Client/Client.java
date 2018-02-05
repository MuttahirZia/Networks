import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
	//Initialize the textfields, labels, and panels for frame
	JTextField inp = new JTextField ();
	JTextField inp2 = new JTextField ();
	JButton conn = new JButton("Connect");
	JButton help = new JButton ("Instructions");
	JLabel label1 = new JLabel("Port Number:");
	JLabel ip = new JLabel ("IP Address");
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

	/* Create two panels, one for connection and one for client interaction. Add panels to frame, 
	but set only the connection one to be visible at the start.
	*/
	public Client () {
		setLayout(new FlowLayout());	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(550,275);
		setTitle ("Client Connection");

		connection.setLayout(new GridLayout (3, 2, 10, 10));

		ip.setPreferredSize(new Dimension(100,30));
		connection.add(ip);

		inp2.setPreferredSize(new Dimension(150,30));
		connection.add (inp2);

		label1.setPreferredSize(new Dimension(100,30));
		connection.add(label1);

		inp.setPreferredSize(new Dimension(150,30));
		connection.add (inp);

		help.setPreferredSize(new Dimension(150, 30));
		help.addActionListener(this);
		connection.add(help); 

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

		isbn.setPreferredSize(new Dimension (150, 30));
		title.setPreferredSize(new Dimension (150, 30));
		author.setPreferredSize(new Dimension (150, 30));
		publisher.setPreferredSize(new Dimension (150, 30));
		year.setPreferredSize(new Dimension (150, 30));

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

	/* When user hits a button, come to actionPerformed
	*/
	public void actionPerformed (ActionEvent e) {
		String command = e.getActionCommand();
		//Determine which action was specified
		boolean success;
		boolean valid;

		try {
			//If connect, try and connect to server and switch panel if successful
			if (command.equals("Connect")) {
				if (inp.getText().length() == 0) {
					throw new Exception ("Need to enter in a port number.");
				}
				success = connectClient(inp2.getText(), inp.getText());

				if (success == true) {
					System.out.println ("Connection made.");
					connection.setVisible (false);
					inp.setText("");
					interact.setVisible(true);

				} else {
					throw new Exception ("Wrong port number or unable to connect to server.");
				}
			} else if (command.equals ("Instructions")) {
				//Show instructions if instructions entered
				String message = "Enter in the IP Address and port number for the server. Then, go through the process of adding, updating, \n" + 
					"removing, and searching the library for books. If you enter in invalid or insufficient information while \n" +
					"doing these operations, you will be prompted to correct your inputs. If correct information is entered, the \n" +
					"server will return whether the operation was completed sucessfully or why it failed.";
				JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);	

			} else if (command.equals ("Disconnect")) {
				//Disconnect from server and switch back to connection panel
				outStream.close();
				inStream.close();
				socket.close();

				interact.setVisible(false);
				connection.setVisible(true);
			} else if (command.equals ("Submit")) {
				//If submit clicked, check to make sure have required fields, and if it is go to server to add else throw exception
				success = verifySubmit ();
				valid = validISBN(isbn.getText());

				if (success && valid) {
					//System.out.println ("Adding to library");
					lib_action ("Submit");

				} else {
					throw new Exception ("You need to specify a valid ISBN, title, and author to add a book to the library");
				}
			} else if (command.equals ("Remove")) {
				//If remove clicked, check to make sure have required fields, and if it is go to server or else throw exception
				//If get clicked, check to make sure have required fields, and if it is go to server or else throw exception
				
				if (isbn.getText().length() != 0) {
					valid = validISBN (isbn.getText());
					if (!valid) {
						throw new Exception ("You've entered an invalid ISPN. Please retry.");
					} else {
						success = verifyRemove();
						if (success) {
							lib_action ("Remove");					
						} 
					}
				}

				
			} else if (command.equals("Update")) {
				//If update clicked, check to make sure have required fields, and if it is go to server or else throw exception
				success = verifyUpdate ();
				valid = validISBN(isbn.getText());

				if (success && valid) {
					lib_action ("Update");

				} else {
					throw new Exception ("A valid ISBN and minimum one other field must be filled to update a book in the library.");
				}
			} else if (command.equals ("Get")) {
				//If get clicked, check to make sure have required fields, and if it is go to server or else throw exception
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

	/* If update, add, or remove entered go to server and do operation. Return successful or failure message
	*/
	public void lib_action (String action) {
		String [] input = {action, isbn.getText(), title.getText(), author.getText(), publisher.getText(), year.getText()};
		//System.out.println (input[0] + " , " + input[1] + " , " + input[2] + " , " + input[3] + 
			//" , " + input[4] + " , " + input[5]);

		try{
			String status;

			outStream.writeObject(input);
			status = inStream.readLine();

			JOptionPane.showMessageDialog(null, status, "Server Message", JOptionPane.PLAIN_MESSAGE);
			//System.out.println(status);

		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);;
		}
	}

	/* If get action clicked, go to server and return relevant search. Display the search results in special window 
	*/
	public void get_action (String action) {
		String [] input = {"Get", isbn.getText(), title.getText(), author.getText(), publisher.getText(), year.getText()};
		String results = "";

		try{
			String status;
			
			outStream.writeObject(input);
			String [][] ret = (String[][])oiStream.readObject();

			int j = 0;
			while(ret[j] != null) {

				String[] parts = ret[j][2].split(",");

				results += 
					"@book {" + parts[0] + ret[j][4] + ",\n" +
					"   AUTHOR = {" + ret[j][2] + "},\n" +
					"    TITLE = {" + ret[j][1] + "},\n" +
					"PUBLISHER = {" + ret[j][3] + "},\n" +
					"     YEAR = {" + ret[j][4] + "},\n" +
					"     ISBN = {" + ret[j][0] + "},\n" +
					"}\n\n"
					;
				j++;
			}

		} catch(Exception e) {
			//JOptionPane.showMessageDialog(null, e.getMessage(), "Fixing Message", JOptionPane.ERROR_MESSAGE);
		}

		//System.out.println (results);
		
		if (results.length() != 0) {
			JTextArea textArea = new JTextArea(20, 50);
		 	textArea.setText(results);
		  	textArea.setEditable(false);
		  
		  	// wrap a scrollpane around it
		  	JScrollPane scrollPane = new JScrollPane(textArea);
		  
		  	// display them in a message dialog
		  	JOptionPane.showMessageDialog(null, scrollPane, "Found Books", JOptionPane.PLAIN_MESSAGE);
      	} else {
      		JOptionPane.showMessageDialog (null, "No books found");
      	}
	}

	/* Make sure valid fields have been entered for submit 
	*/
	public boolean verifySubmit () {
		if (title.getText().trim().length()==0 || author.getText().trim().length()==0) {
			return false;
		} else {
			return true;
		}
	}

	/* Make sure valid fields have been entered for remove 
	*/
	public boolean verifyRemove () {
		String ObjButtons[] = {"Yes","No"};
    	int PromptResult = JOptionPane.showOptionDialog(null, 
   			"Are you sure you want to remove from the database? \nIf no parameters are specified, then ALL books will be removed.", "Confirmation", 
        	JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
    		ObjButtons,ObjButtons[1]);
    				
    	if(PromptResult==0) {
     		return true;       
    	} else {
    		return false;
    	}
	}

	/* Make sure valid fields have been entered for update 
	*/
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

	/* Make sure valid ISBN entered
	*/
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

	/* Try and connect to client given IP address and port number. 
	*/
	public boolean connectClient(String address, String port) {
		try {
			socket = new Socket(address, Integer.parseInt(port));
			inStream = new DataInputStream(socket.getInputStream());
			outStream = new ObjectOutputStream(socket.getOutputStream());
			oiStream = new ObjectInputStream(socket.getInputStream());
			return true;

	    } catch (Exception e) {
			return false;
		}
	}
}