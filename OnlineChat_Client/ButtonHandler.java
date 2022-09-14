package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import client.ChatClient;

public class ButtonHandler implements ActionListener{
	public ChatClient chatClient;
	public ButtonHandler(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("連接")) {
			chatClient.connectServer();
		}else if(command.equals("斷開連接")) {
			chatClient.disconnectServer();
		}else if(command.equals("發送")) {
			chatClient.singleSendMsg();
		}else if(command.equals("群發")) {
			chatClient.groupSend();
		}
	}
}
