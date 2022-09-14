package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ButtonHandler implements ActionListener{
	public ChatServer chatServer;
	public ButtonHandler(ChatServer chatServer) {
		this.chatServer = chatServer;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("運行")) {
			chatServer.runServer();
		}else if(command.equals("停止")) {
			chatServer.stopServer();
		}
	}
}
