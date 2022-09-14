package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;

public class MenuHandler implements ActionListener{
	public ChatClient chatClient;
	public MenuHandler(ChatClient chatClient) {
		this.chatClient = chatClient;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("連接服務器")) {
			if(chatClient.connectStatus == 0) {
				chatClient.connectServer();
			}
		}else if(command.equals("斷開連接服務器")) {
			if(chatClient.connectStatus == 1) {
				chatClient.disconnectServer();
				
			}
		}else if(command.equals("幫助")) {
			final String helpMessage = "使用方法:\n"
					+ "在界面底部輸入端口號後按下運行即可運行服務器。\n"
					+ "界面左方為用戶列表，顯示當前用戶以及IP。\n"
					+ "界面右方為信息框，服務器運行信息都會在上面顯示。";
			JOptionPane.showMessageDialog(null, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
		}else if(command.equals("關於")) {
			final String aboutMessage = "Online Chat - Client\n By: 譚嘉傑 (3180101826)";
			JOptionPane.showMessageDialog(null, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}
