/**
 * @author - Daniel Lier and Preston McIllece.
 * 
 * This class receives messages from the BroadcastThread, parses the data, and relays
 * the necessary information to the user on the client side.
 */

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ReaderThread implements Runnable
{
	Socket server;
	BufferedReader fromServer;

	public ReaderThread(Socket server) {
		this.server = server;
	}

	public void run() {
		try {
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

			while (true) {
				String message = fromServer.readLine();
				message = parse(message);
				System.out.println(message);
			}
		}
		catch (IOException ioe) { System.err.println(ioe); }
	}

	//parses message from BroadcastThread to determine type of message/what the user should be told.
	private String parse(String message) {
		//if the message contains a bar, it is a header
		if (message.contains("|")) {
			String[] delims = message.split("\\|");
			if (delims[0].equals("JOIN")) { //join
				return delims[1] + " has joined the chat.";
			}
			else if (delims[0].equals("BDMG")) { //broadcast message
				try {
				String msg = fromServer.readLine();
				return delims[1] + ": " + msg;
				} catch(IOException ioe) { System.err.println(ioe); }
			}
			else if (delims[0].equals("PVMG")){ //private message
				try {
					String line = fromServer.readLine();
					String msg = line.substring(line.indexOf(" ") + 1);

					return delims[1] + "(private)🍑 : " + msg;
				} 
				catch(IOException ioe) { System.err.println(ioe); }
			}
			else if (delims[0].equals("STAT")) { //status code
				if (delims[1].equals("421")) { //username of the person they're private messaging doesn't exist
					return "Sorry man, that username isn't in the chat right now.";
				}
				else if (!delims[1].equals("200")) { //other unforeseen error 
					return "Oopsy. Your message was lost champ.🤷‍♂️";
				}
				return "👍"; //message sent successfully!
			}
			else if (delims[0].equals("LEAV")) { //leave
				return delims[1] + " has left the chat.👋";
			}
			return message;}
		else {return message;}
	}
}
