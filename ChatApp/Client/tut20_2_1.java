//Chat App(Client)

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class tut20_2_1 extends JFrame {
	
	private JTextField userText;
	private JTextArea chatBoxU;
	private ObjectOutputStream sendU;
	private ObjectInputStream receiveU;
	private String message ="";
	private String ServerIP;
	private Socket sessionU;

	
	public tut20_2_1(String host){
		
		super("Client");
		ServerIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						sendData(event.getActionCommand());
						userText.setText("");
					}
				}
				
			);
		add(userText, BorderLayout.NORTH);
		chatBoxU = new JTextArea();
		add(new JScrollPane(chatBoxU), BorderLayout.CENTER);
		setSize(640,320);
		setVisible(true);
	}
	
	public void runningU(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();	
		}catch(EOFException eofException){
			showMessage("\n Client session ended");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
		    closeStuff();
		}
	}
	// connect to server
	private void connectToServer() throws IOException{
		showMessage("Connecting...  \n");
		sessionU = new Socket(InetAddress.getByName(ServerIP), 6789);
		showMessage("Connected To: " + sessionU.getInetAddress().getHostName());
	}
	
	// setup to receive and send messages
	private void setupStreams() throws IOException{
		sendU = new ObjectOutputStream(sessionU.getOutputStream());
		sendU.flush();
		receiveU = new ObjectInputStream(sessionU.getInputStream());
		showMessage("Your Streams Have Been Established \n");
	}
	 
	private void whileChatting() throws IOException{
		typeEnable(true);
		do{
			try{
				message =(String) receiveU.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n Error");
			}
			
		}while(!message.equals("SERVER - END"));
	}
	
	private void closeStuff(){
		showMessage("\n closing...");
		typeEnable(false);
		try{
			sendU.close();
			receiveU.close();
			sessionU.close();
			
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	private void sendData(String message){
		try{
			sendU.writeObject("\nCLIENT -" + message);
			sendU.flush();
			showMessage("\nCLIENT -" + message);
		}catch(IOException ioException){
			chatBoxU.append("\n error incorect");
		}
	}
	
	private void showMessage(final String mess){
		SwingUtilities.invokeLater(
		new Runnable(){
			public void run(){
				chatBoxU.append(mess);
			}
			
		 }
		
	  );
		
	}
	
	private void typeEnable(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(tof);;
					}
					
				 }
				
			  );
	}
	
}
