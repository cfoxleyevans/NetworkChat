//package identifier
package com.chrisfoxleyevans.NetworkChat.Server;

//lib imports
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.chrisfoxleyevans.NetworkChat.GUI.ServerInterface;

//***********************************************************************//
// Server: the server side of a chat program, runs in its own thread
//***********************************************************************//
public class Server implements Runnable 
{
	/*** Instance Vars ***/
	private ServerSocket serverSocket; //the server socket that we listen on
	private ArrayList<ClientHandler> clients; //list of all the handlers that are active on this server
 	private ServerInterface view; //reference to the view that this server owns
	
	/*** Constructors ***/
	public Server(int port, ServerInterface view)
	{
		//register the view, initialise the list and then try and bind to the port
		//if exception thrown catch it and display error
		this.view = view;
		this.clients = new ArrayList<ClientHandler>();
		try
		{	
			this.serverSocket = new ServerSocket(port);
			this.view.addMessage("Listening on port: " + this.serverSocket.getLocalPort());
		} 
		catch (IOException e)
		{
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	
	/*** Private Methods ***/
	//private methods go here
	
	/*** Public Methods ***/
	public ArrayList<ClientHandler> getClients() 
	{
		return clients;
	}

	public void removeConnection(Socket connection) 
	{
		clients.remove(connection);
	}

	public void broadcastMessage(String message)
		{
			//for each of the clients in the list open an output stream on the socket
			//and send out the message that has been passed
			for(ClientHandler client : getClients()) 
			{
				try
				{
					DataOutputStream out = new DataOutputStream(new BufferedOutputStream(client.getConnection().getOutputStream()));
					out.writeUTF(message);
					out.flush();
				}
				catch (IOException e)
				{
					System.out.println("ERROR: " + e.getMessage());
				}
			}
		}
	
	/*** Thread Method ***/
	public void run() 
		{
			//listen on the socket, when we have a new connection read in the username
			//create a new handler for the client add them to the list and update the view
			//spool a new thread and start it
			while(true)
			{
				try
				{
					Socket client = this.serverSocket.accept(); 
					
					DataInputStream in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
					String uname = in.readUTF();
				
					ClientHandler newClient = new ClientHandler(this, client, uname);
					clients.add(newClient);
					this.view.addMessage("User " + uname + " has connected from " + client.getInetAddress()); 
					
					Thread handler = new Thread(newClient);
					handler.start();	
				}
				catch (IOException e)
				{
					System.out.println("ERROR: " + e.getMessage());
				}
			}
		}
}
