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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import com.chrisfoxleyevans.NetworkChat.Client.Client;

//***********************************************************************//
// ClientInterface: the view that each client uses
//***********************************************************************//
public class ClientInterface extends JFrame implements ActionListener
{
	/*** Instance Vars ***/
	private JPanel rootPanel;
	private JPanel connectPanel;
	private JPanel sendPanel;
	
	private JButton btnSend;
	private JButton btnConnect;
	
	private JTextArea txtMessages;
	private JScrollPane txtMessagesScroller;
	
	private JLabel lblMessage;
	private JLabel lblHostName;
	private JLabel lblPortNumber;
	private JLabel lblUser;
	
	private JTextField txtMessage;
	private JTextField txtHostName;
	private JTextField txtPortNumber;
	private JTextField txtUser;
	
	private Client client;

	/*** Constructors ***/
	public ClientInterface()
	{
		//set up the JPanels
		this.rootPanel = new JPanel();
		this.rootPanel.setLayout(new BorderLayout());
		
		this.connectPanel = new JPanel();
		this.sendPanel = new JPanel();

		//set up the components
		this.lblHostName = new JLabel("Hostname:");
		this.txtHostName = new JTextField(20);
		this.txtHostName.setText("localhost");
		this.lblPortNumber = new JLabel("Port:");
		this.txtPortNumber = new JTextField(5);
		this.txtPortNumber.setText("4567");
		this.lblUser = new JLabel("User Name:");
		this.txtUser = new JTextField(10);
		this.btnConnect = new JButton("Connect");
		this.btnConnect.addActionListener(this);
		
		this.txtMessages = new JTextArea(25,10);
		this.txtMessages.setEditable(false);
		this.txtMessagesScroller = new JScrollPane(this.txtMessages);
		DefaultCaret caret = (DefaultCaret)this.txtMessages.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		this.lblMessage = new JLabel("Message:");
		this.txtMessage = new JTextField(45);		
		this.btnSend = new JButton("Send");
		this.btnSend.addActionListener(this);
		
		//set up the connectPanel
		this.connectPanel.add(this.lblHostName);
		this.connectPanel.add(this.txtHostName);
		this.connectPanel.add(this.lblPortNumber);
		this.connectPanel.add(this.txtPortNumber);
		this.connectPanel.add(this.lblUser);
		this.connectPanel.add(this.txtUser);
		this.connectPanel.add(this.btnConnect);
		
		//set up the commands panel
		this.sendPanel.add(lblMessage);
		this.sendPanel.add(txtMessage);
		this.sendPanel.add(btnSend);

		//add the panels to the interface
		this.add(rootPanel);
		rootPanel.add(this.connectPanel, BorderLayout.NORTH);
		rootPanel.add(this.txtMessagesScroller, BorderLayout.CENTER);
		rootPanel.add(this.sendPanel, BorderLayout.SOUTH);

		//set interface options
		this.setTitle("Client Interface");
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/*** Private Methods ***/
	private void btnConnectHandler()
	{
		//reads the values from the interface components
		//trims the user name
		//creates a new client and starts it
		String hostname = txtHostName.getText();
		int port = Integer.parseInt(txtPortNumber.getText());
		
		String username = txtUser.getText();
		username = username.trim();
		
		client = new Client(hostname, port, username, this);
		Thread th = new Thread(client);
		th.start();
	}
	
	private void btnSendHandler()
	{
		//extracts the values from the view
		//sends them to the server
		String message = this.txtMessage.getText();
		
		client.sendMessage(message);
		this.txtMessage.setText("");
	}
	
	/*** Public Methods ***/
	public void addMessage(String message)
	{
		//allows the client to update the view
		txtMessages.append("\n" + message);
	}

	public void actionPerformed(ActionEvent e)
	{
		//calls the relevant handler
		if(e.getSource() == btnConnect)
		{
			btnConnectHandler();
		}
		else if(e.getSource() == btnSend)
		{
			btnSendHandler();
		}
	}
}
