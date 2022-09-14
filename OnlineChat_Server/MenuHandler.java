package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class MenuHandler implements ActionListener{
	public ChatServer chatServer;
	public MenuHandler(ChatServer chatServer) {
		this.chatServer = chatServer;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("運行服務器")) {
			if(chatServer.serverStatus == 0) {
				chatServer.runServer();
			}
		}else if(command.equals("關閉服務器")) {
			if(chatServer.serverStatus == 1) {
				chatServer.stopServer();
			}
		}else if(command.equals("幫助")) {
			final String helpMessage = "使用方法:\n"
					+ "在界面底部輸入端口號後按下運行即可運行服務器。\n"
					+ "界面左方為用戶列表，顯示當前用戶以及IP。\n"
					+ "界面右方為信息框，服務器運行信息都會在上面顯示。";
			JOptionPane.showMessageDialog(null, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
		}else if(command.equals("關於")) {
			final String aboutMessage = "Online Chat - Server\n By: 譚嘉傑 (3180101826)";
			JOptionPane.showMessageDialog(null, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}
