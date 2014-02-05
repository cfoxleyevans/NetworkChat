//package identifier
package com.chrisfoxleyevans.NetworkChat.Server;

//lib imports
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//***********************************************************************//
// ClientHandler: handle the interaction with the client
//***********************************************************************//
public class ClientHandler implements Runnable 
{
	/*** Instance Vars ***/
	private Server server; //the server that i report to 
	private Socket connection; //the socket used to connect to the client
	private DataInputStream in; //the input stream to the socket
	private DataOutputStream out; //the output stream from the socket
	private String uname; //the username of the client

	/*** Constructors ***/
	public ClientHandler(Server server, Socket connection, String username)
	{
		//set the server connection and the username
		//create input and output streams for the socket
		this.server = server;
		this.connection = connection;
		this.uname = username; 
		try
		{
			this.in = new DataInputStream(new BufferedInputStream(this.connection.getInputStream()));
			this.out = new DataOutputStream(new BufferedOutputStream(this.connection.getOutputStream()));
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	/*** Private Methods ***/
	//private methods go here
	
	/*** Public Methods ***/
	public String getUserName()
	{
		return uname;
	}
	
	public Socket getConnection()
	{
		return connection;
	}
	
	/*** Thread Method ***/
	public void run()
	{
		//let the client know that they are connected
		//listen for new messages from the client and send to server for broadcast
		try
		{
			out.writeUTF("You are now connected using the name, " + uname);
			out.flush();
			
			while (true) 
			{
				String message = in.readUTF();
				server.broadcastMessage(message);
			}
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e.getMessage()); 
		}
	}
}
