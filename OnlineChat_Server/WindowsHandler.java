package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;



public class WindowsHandler extends WindowAdapter{
	// 二次確認退出程序
	private ChatServer chatServer;
	public WindowsHandler(ChatServer chatServer) {
		this.chatServer = chatServer;
	}
	
	public void windowClosing(WindowEvent e) {
		 int choice = JOptionPane.showConfirmDialog(null, "你真的要退出程序嗎?","Exit Program", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			switch (choice) {
			case JOptionPane.YES_OPTION:
				if(chatServer.serverStatus == 1) {
					chatServer.stopServer();
				}
				
				System.exit(0);
				break;
			case JOptionPane.NO_OPTION:
				return;
			}
	}
}
