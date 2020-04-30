/**
 * @author - Daniel Lier and Preston McIllece.
 * 
 * This class contains the logic that connects to the user's keyboard, parses their input and sends the
 * necessary information to ChatServer. This class also handles status codes for "JOIN" messages.
 */

import java.net.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.LocalDate;
import java.util.concurrent.*;
import java.io.*;
import java.util.*;

public class ChatClient
{
	public static final int DEFAULT_PORT = 1337;
	private static final Executor exec = Executors.newCachedThreadPool();
	private static BufferedReader localBin = null;		// the reader from the local keyboard
	private static String userName = "";
	
	//returns the numerical value in the status code string
	private static int parseStatusCode(String statusCode) {
		return Integer.parseInt(statusCode.substring(statusCode.indexOf("|") + 1));
	}

	//returns a date in the protocol's format, YYYY-MM-DD-HH-MM-SS
	private static String parseDate(Date uglyDate) {
		Instant instant = uglyDate.toInstant();
		LocalDateTime ldt = instant.atOffset(ZoneOffset.UTC).toLocalDateTime();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd-HH-mm-ss");
		return ldt.format(timeFormatter);
	}

	//creates the header for a broadcast message
	private static String broadcast(String name) {
		Date date = new Date();
		return "BDMG|" + name + "|all|" + parseDate(date);
	}
	
	private static String privateMessage(String name, String destination) {
		Date date = new Date();
		return "PVMG|" + name + "|" + destination + "|" + parseDate(date);
	}
	
	//creates the header for a leave
	private static String leave(String name) {
		Date date = new Date();
		return "LEAV|" + name + "|all|" + parseDate(date);
	}
	
	//creates the header for a join
	private static String join(String name) {
		Date date = new Date();
		return "JOIN|" + name + "|all|" + parseDate(date) + "\r\n" + name + "\r\n";
	}

	//prompts the user and then returns their username input
	private static String retrieveUsername() throws IOException {
		try {
			System.out.println("Enter a username:");
			return localBin.readLine();
		}
		catch (IOException ioe) {
			System.err.println(ioe);
			return "";
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: java EchoClient <echo server>");
			System.exit(0);
		}
		
		PrintWriter networkPout = null;		// the writer to the network
		Socket sock = null;			// the socket
		ReaderThread reader = null;
		BufferedReader listeningForStatusCode = null;
		
		try {
			sock = new Socket(args[0], DEFAULT_PORT);
			localBin = new BufferedReader(new InputStreamReader(System.in));
			networkPout = new PrintWriter(sock.getOutputStream(),true);
			
			userName = retrieveUsername();
			boolean stupidUserName = true;
			String regex = "^[a-zA-Z0-9_$^`; -]*$";
			//checks if a username has valid characters
			while (stupidUserName) {
				if (userName.contains("|") || !userName.matches(regex)) {
					System.out.println("Your username cannot contain '|' or other non-sensible symbols. Please try again.\r\n");
					userName = retrieveUsername();
				}
				else {stupidUserName = false;}
			}
			networkPout.println(join(userName)); //sends username to server
			listeningForStatusCode = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String statusCode = listeningForStatusCode.readLine(); //status code pertaining the username
			int statusCodeNumber = parseStatusCode(statusCode);
			
			//if the username is valie
			if (statusCodeNumber == 200) { 
				System.out.println("Welcome " + userName + "! \r\nType a '@' to private message. \r\nType a '.' to leave the chat.");

				reader = new ReaderThread(sock);
				exec.execute(reader);
				
				boolean isLeaving = false;
				boolean properSpacing = true;
				while (!isLeaving) {
					String line2 = localBin.readLine();
					String line = "";
					Character at = '@';
					if (line2.length() < 513){ //limit's a user's message to 512 characters
						if (at.equals(line2.charAt(0))){ //checks if a user wants to send a private message
							if (line2.contains(" ")) {
								String destinationUser = line2.substring(1, line2.indexOf(" "));
								line = privateMessage(userName, destinationUser);
							}
							else {
								System.out.println("You need to add a space before your message.🤦‍♂️");
								properSpacing = false;
							}
						}
						else {line = broadcast(userName);}
						if (line2.equals(".")){ //checks if a user wants to leave the chat
							isLeaving = true;
							line = leave(userName);
							networkPout.println(line);
						}
						else {
							if (properSpacing){
								networkPout.println(line);
								networkPout.println(line2);
							}
							properSpacing = true;
						}
					}
					else {System.out.println("Relax bro, your message is too long. Tone it down please.✋");}
				}
			}
			//the username is invalid
			else if (statusCodeNumber == 420) {
				if (userName.length() > 15) {System.out.println("\r\nSorry, your username must be 15 characters or fewer! \r\nPlease try again!");}
				else {System.out.println("\r\nSorry the username \"" + userName + "\" is already taken! \r\nPlease try again!");}
			}
			//unknown error, STAT|400
			else {System.out.println("We have no clue what happened. Please contact server owner.");}
		}
		catch (IOException ioe) {System.err.println(ioe);}
		finally {
			if (localBin != null)
				localBin.close();
			if (networkPout != null)
				networkPout.close();
			if (sock != null)
				sock.close();
		}
	}
}
