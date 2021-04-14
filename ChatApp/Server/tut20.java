//Creating a chat app(server)


import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class tut20 extends JFrame{
	
	private JTextField usertext;
	private JTextArea chatbox;
	private ObjectOutputStream send;
	private ObjectInputStream recieve;
	private ServerSocket server;
	private Socket session;
	
	public tut20(){
		super("Messanger");
		usertext = new JTextField();
		usertext.setEditable(false);
		usertext.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						sendchat(event.getActionCommand());
						usertext.setText("");
				}
			}
				
		);
		add(usertext, BorderLayout.NORTH);
		chatbox = new JTextArea();
		add(new JScrollPane(chatbox));
		setSize(640,320);
		setVisible(true);
	}
	
	public void running(){
		try{
			server = new ServerSocket(6789, 100);
			while(true){
				try{
					connectionwait();
					streamsetup();
					duringchat();
				}catch(EOFException eofe){
					showalert("/n Connection Ended");
				}finally{
					closeall();
				}
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	private void connectionwait() throws IOException{
		showalert("Waiting for connection...\n");
		session = server.accept();
		showalert("Connected to"+ session.getInetAddress().getHostName());
	}
	
	private void streamsetup()throws IOException{
		send = new ObjectOutputStream(session.getOutputStream());
		send.flush();
		recieve = new ObjectInputStream(session.getInputStream());
		showalert("\n Stream now available\n");
	}
	
	private void duringchat() throws IOException{
		String message = "Connection established";
		sendchat(message);
		typeenable(true);
		do{
			try{
				message = (String) recieve.readObject();
				showalert("\n"+ message);
			}catch(ClassNotFoundException notfound){
				showalert("\n character not recognized\n");
			}
		}while(!message.equals("CLIENT - STOP"));
	}
	
	private void closeall(){
		showalert("\n Closing connection \n");
		typeenable(false);
		try{
			send.close();
			recieve.close();
			session.close();
		}catch(IOException error){
			error.printStackTrace();
		}
	}
	
	private void sendchat(String message){
		try{
			send.writeObject("Server -" + message);
			send.flush();
			showalert("\n Server -" + message);
		}catch(IOException error){
			chatbox.append("\n Error: Message Unsendable");
		}
	}
	
	private void showalert(final String alert){
		SwingUtilities.invokeLater(
		     new Runnable(){
		    	 public void run(){
		    		 chatbox.append(alert);
		    	 }
		     }
	    );
	}
	
	private void typeenable(final boolean torf){
		SwingUtilities.invokeLater(
		      new Runnable(){
			   	 public void run(){
			    	 usertext.setEditable(torf);
		    	 }
		     }
		 );
	}
}
