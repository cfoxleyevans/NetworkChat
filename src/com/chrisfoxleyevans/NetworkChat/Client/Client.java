//package identifier
package com.chrisfoxleyevans.NetworkChat.Client;

//lib imports
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.chrisfoxleyevans.NetworkChat.GUI.ClientInterface;

//***********************************************************************//
// Client: the client side of a chat program
//***********************************************************************//
public class Client implements Runnable
{
	/*** Instance Vars ***/
	private String uname; //the username of this client
	private Socket connection; //the socket that this client uses to connect to the server
	private DataInputStream in; //the input stream to the socket
	private DataOutputStream out; //the output stream from the socket
	private ClientInterface view; //reference to the view that this client owns
	
	/*** Constructors ***/
	public Client(String hostname, int port, String uname, ClientInterface view)
	{
		//set the username and the view
		//create the input and output streams
		//send the username that we are using
		this.uname = uname;
		this.view = view;
		try
		{
			this.connection = new Socket(hostname, port); 
			
			this.in = new DataInputStream(new BufferedInputStream(this.connection.getInputStream()));
			this.out = new DataOutputStream(new BufferedOutputStream(this.connection.getOutputStream()));
			
			this.out.writeUTF(uname);
			this.out.flush();
		}
		catch (IOException e)
		{
			this.view.addMessage("ERROR: " + e.getMessage());
		}
	}
	
	/*** Private Methods ***/
	//private methods go here
	
	/*** Public Methods ***/
	public void sendMessage(String message)
	{
		//wrap the message with the user name and send it to the server
		try
		{
			out.writeUTF(uname + ": " + message);
			out.flush();
		}
		catch (IOException e) 
		{
			view.addMessage("ERROR: " + e.getMessage());
		}
	}
	
	/*** Thread Method ***/
	public void run()
	{
		try
		{
			while (true) 
			{
				//listen for incoming messages and display them in the view
				String message = in.readUTF(); 
				view.addMessage(message); 
			}
		}
		catch (IOException e) 
		{
			view.addMessage("ERROR: " + e.getMessage());
		}
	}
}
