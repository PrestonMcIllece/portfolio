/**
 * @author - Daniel Lier and Preston McIllece.
 * 
 * 
 * Connection is an instance of a Client-Server connection.
 * Chatserver forwards its socket connections here for multithreading purposes
 * Public messages are forwarded to the broadcast thread and private messages are sent directly to other clients from here.
 */

import java.net.*;
import java.io.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;

public class Connection implements Runnable
{
	private Socket client;
	private String username;
	private Vector messages;
	private ArrayList outputStreams;
	private HashMap<String, OutputStream> usernameDictionary;
	private boolean broadcast = true;
	
	public Connection(Socket client, Vector vector, ArrayList arrayList, HashMap hashMap) {
		this.client = client;			//inherit socket, vector, arraylist, and hashmap from ChatServer
		this.username = "";
		this.messages = vector;
		this.outputStreams = arrayList;
		this.usernameDictionary = hashMap;
	}

	public void process(Socket client) throws java.io.IOException {
		BufferedReader fromClient = null;
		String username = "";
		PrintWriter printWriter = null;
		
		try {
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			username += fromClient.readLine();								//receive a join with declaration of username from client
			String statusCode = this.parse(username, fromClient);			//get the status code of that join, 200 if good and 420 if bad (already taken)
			fromClient.readLine();
			
			printWriter = new PrintWriter(this.client.getOutputStream());	
			printWriter.println(statusCode);								//send the status code back to the client
			printWriter.flush();
			boolean joinMessage = true;

			if (!statusCode.equals("STAT|200")) {							//if bad username, close connection
				client.close();
				joinMessage=false;											//do not send the join to the broadcast thread if bad status code
			}
		

			String line;
			String status;


			while(true) {
				if (joinMessage) {											//add the join message to broadcast thread
					messages.add(username);
					joinMessage = false;									//do not send joins again
				}
				line = fromClient.readLine();								//read a line from client
				status = "";
				if (line.contains("|")){
					status = parse(line, fromClient);						//if it's a header, get a status code
				}
				
				if (status.length() > 0 && !status.equals("leaving")) {		//on a "LEAV", send the status code back to the client
					printWriter.println(status);
					printWriter.flush();
				}

				if (this.broadcast) {
					messages.add(line);										//add messages to the broadcast thread
				}
				this.broadcast = true;										//reset the broadcast variable for the next loop

				if (status.equals("leaving")) {
					if (fromClient != null)
					fromClient.close();
					if (printWriter != null)
					printWriter.close();
					if (client != null)
					client.close();
					break;
				}
			}
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			// close streams and socket
			if (fromClient != null)
			fromClient.close();
		}
	}
	public void run() { 
		try {
			process(client);
		} catch(IOException ioe) {
			System.err.println(ioe);
		}
	}
	
	public String parse(String raw, BufferedReader br) throws IOException {
		String[] delims = raw.split("\\|");									//slice the header into strings on the | character
		PrintWriter printer = null;
		try {
			if (delims[0].equals("JOIN")) {
				if (delims[1].length() <= 15) {								
					if (this.usernameDictionary.putIfAbsent(delims[1], this.client.getOutputStream()) == null) { //check for valid username and be sure that it does not already exist
						this.username = delims[1];
						this.outputStreams.add(this.client.getOutputStream());
						return "STAT|200";
					}
					else {
						this.broadcast = false;
						return "STAT|420";}
				}
				else {
					this.broadcast = false;
					return "STAT|420";
				}
			}
			else if (delims[0].equals("PVMG")) {							
				this.broadcast = false;
				String message = br.readLine();								//get contents of private message
				String toWho = delims[2];									//get the recipient of the private message
				if (this.usernameDictionary.containsKey(toWho)) {
					printer = new PrintWriter(this.usernameDictionary.get(toWho));	//get the output stream of the recipient
					printer.println(raw);									//send the header to the recipient
					printer.flush();
					printer.println(message);								//send the message contents to the recipient
					printer.flush();
					return "STAT|200";
				}
				else{return "STAT|421";}
			}
			else if (delims[0].equals("BDMG")) {
				return "STAT|200";
			}
			else if (delims[0].equals("LEAV")) {
				String who = delims[1];										//get username of person leaving
				OutputStream stream = this.usernameDictionary.get(who);		//get output stream of person leaving
				this.outputStreams.remove(stream);							//remove stream from the broadcast thread arraylist
				this.usernameDictionary.remove(who);						//remove <username, stream> from the private message hashmap
				return "leaving";
			}
			else {return "STAT|666";}

		} catch (IOException ioe) {
			System.err.println(ioe);
			return "STAT|400";
		}
	}
}

