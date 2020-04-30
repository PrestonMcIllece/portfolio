/**
 * @author - Daniel Lier and Preston McIllece.
 * 
 * Multithreaded server that recieves ChatClients and forwards their socket connections to an instance of Connection
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.*;


public class ChatServer
{
	public static final int DEFAULT_PORT = 1337;

    // construct a thread pool for concurrency	
	private static final Executor exec = Executors.newCachedThreadPool();
	public static Vector messages = new Vector<String>();					//vector to handle messages in the brodcast thread
	public static ArrayList outputStreams = new ArrayList<OutputStream>();	//arraylist of output streams for the broadcast thread to send messages to 
	public static BroadcastThread broadcastThread = new BroadcastThread(messages, outputStreams); 	//broadcast thread for sending public messages
	public static HashMap<String, OutputStream> usernameDictionary = new HashMap<>(50);				//hashmap of usernames and output streams for private messages

	public static void main(String[] args) throws IOException {
		ServerSocket sock = null;
		
		try {
			// establish the socket
			sock = new ServerSocket(DEFAULT_PORT);				//open the socket
			
			exec.execute(broadcastThread);
			while (true) {
				Connection task = new Connection(sock.accept(), messages, outputStreams, usernameDictionary);		//accept client connections to the socket
				exec.execute(task);
			}
		}
		catch (IOException ioe) { System.err.println(ioe); }
		finally {
			if (sock != null)
				sock.close();
		}
	}
}
