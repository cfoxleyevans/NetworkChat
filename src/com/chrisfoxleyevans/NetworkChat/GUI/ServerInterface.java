//package identifier
package com.chrisfoxleyevans.NetworkChat.GUI;

//lib imports
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.chrisfoxleyevans.NetworkChat.Client.Client;
import com.chrisfoxleyevans.NetworkChat.Server.Server;

//***********************************************************************//
// ServerInterface: the view that each server uses
//***********************************************************************//
public class ServerInterface extends JFrame implements ActionListener
{
	/*** Instance Vars ***/
	private JPanel rootPanel;
	private JPanel connectPanel;
	
	private JButton btnBind;
	
	private JTextArea txtMessages;
	
	private JLabel lblPortNumber;
	
	private JTextField txtPortNumber;

	/*** Constructors ***/
	public ServerInterface()
	{
		//set up the JPanel
		this.rootPanel = new JPanel();
		this.rootPanel.setLayout(new BorderLayout());
		this.connectPanel = new JPanel();
		
		//set up the components
		this.lblPortNumber = new JLabel("Port:");
		this.txtPortNumber = new JTextField(5);
		this.txtPortNumber.setText("4567");
		
		this.txtMessages = new JTextArea(25,10);
		this.txtMessages.setEditable(false);
		
		this.btnBind = new JButton("Bind");
		this.btnBind.addActionListener(this);
		
		//set up the connectPanel
		this.connectPanel.add(this.lblPortNumber);
		this.connectPanel.add(this.txtPortNumber);
		this.connectPanel.add(this.btnBind);
		
		//add the panels to the interface
		this.add(rootPanel);
		rootPanel.add(this.connectPanel, BorderLayout.NORTH);
		rootPanel.add(this.txtMessages, BorderLayout.CENTER);

		//set interface options
		this.setTitle("Server Interface");
		this.setSize(400, 400);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/*** Private Methods ***/
	private void btnBindHandler()
	{
		//extract the values from the view
		//spool a new thread and start it
		int port = Integer.parseInt(this.txtPortNumber.getText());
		
		Thread th = new Thread(new Server(port, this));
		th.start();
	}
	
	public void addMessage(String message)
	{
		//allows the server to update the view
		this.txtMessages.append("\n" + message);
	}

	/*** Public Methods ***/
	public void actionPerformed(ActionEvent e)
	{
		//call the relevant handlers
		if(e.getSource() == this.btnBind)
		{
			btnBindHandler();
		}
	}	
}
