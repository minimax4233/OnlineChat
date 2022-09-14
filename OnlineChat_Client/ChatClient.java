package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;




public class ChatClient extends JFrame {

	private static Socket client;
	
	private static BufferedReader reader;
	private static BufferedWriter writer;

	private static final long serialVersionUID = 1L;

	// 聲明控件
	// 菜單欄
	private JMenuBar menuBar = new JMenuBar(); 
	private JMenu serverMenu = new JMenu("服務器"); // Server 菜單
	private JMenuItem runMenuItem = new JMenuItem("連接服務器");
	private JMenuItem stopMenuItem = new JMenuItem("斷開連接服務器");
	private JMenu aboutMenu = new JMenu("關於"); // 關於菜單
	private JMenuItem helpMenuItem = new JMenuItem("幫助");
	private JMenuItem aboutMenuItem = new JMenuItem("關於");
	
	
	// 連接信息
	private JPanel controlPanel;
	private JPanel infoPanel;
	private JLabel usernameLable;// 用戶名
	private JTextField usernameField; 
	private JLabel serverIPLable;// IP
	private JTextField serverIPField; 
	private JLabel portLable;// 端口
	private JTextField portField;
	
	// 用戶列表
	private JPanel listPanel;
	private JList<String> userList;// 用戶列表
	private Vector<String> userNameVector;
	private JScrollPane userListPane;

	// 連接按鈕
	private JPanel connectButtonPanel;
	private JButton connectButton;// 連接按鈕
	private JButton disconnectButton;// 斷開連接按鈕

	// 信息框
	private JPanel chatPanel;
	private JScrollPane messagePane;// 信息框
	private JTextArea messageArea;

	private JScrollPane contentPane;// 输入框
	private JTextArea contentArea;

	private JPanel buttonPane;
	private JButton singleSendButton;// 單發
	private JButton groupSendButtonl;// 群發
	
	// 連接狀態
	public int connectStatus = 0;
		
	// 常量
	public static final String SEND_USERNAME_IP = "SendUsernameIP";
	public static final String SEND_MESSAGE = "SendMessage";
	public static final String SEND_GROUP_MESSAGE = "SendGroupMessage";
	public static final String GET_MESSAGE = "GetMessage";
	public static final String GET_USERNAME_IP = "GetUsernameIP";
	public static final String GET_GROUP_MESSAGE = "GetGroupMessage";
	public static final String DISCONNECT = "Disconnect";
	
	
	public ChatClient() {
		MainForm(); //主界面
		setListener(); // 監聽器
	}
	
	// 控件監聽器
	private void setListener() {
		MenuHandler menuHandler = new MenuHandler(this);
		runMenuItem.addActionListener(menuHandler);
		stopMenuItem.addActionListener(menuHandler);
		helpMenuItem.addActionListener(menuHandler);
		aboutMenuItem.addActionListener(menuHandler);
		
		ButtonHandler buttonHandler = new ButtonHandler(this);
		connectButton.addActionListener(buttonHandler);
		disconnectButton.addActionListener(buttonHandler);
		singleSendButton.addActionListener(buttonHandler);
		groupSendButtonl.addActionListener(buttonHandler);
		
		this.addWindowListener(new WindowsHandler(this)); // 監聽窗口事件
	}

	// 繪製程序主界面
	private void MainForm() {
		// 菜單欄
		serverMenu.add(runMenuItem);
		serverMenu.add(stopMenuItem);
		aboutMenu.add(helpMenuItem);
		aboutMenu.add(aboutMenuItem);
		menuBar.add(serverMenu);
		menuBar.add(aboutMenu);
		this.setJMenuBar(menuBar);
		
		
		// 連接信息
		usernameLable = new JLabel("用戶名：");
		usernameField = new JTextField("");
		serverIPLable = new JLabel("服務器 IP：");
		serverIPField = new JTextField("");
		portLable = new JLabel("端口：");
		portField = new JTextField("");
		
		infoPanel = new JPanel(new GridLayout(3, 2));
		infoPanel.setBorder(BorderFactory.createTitledBorder("連接信息:"));
		infoPanel.add(usernameLable);
		infoPanel.add(usernameField);
		infoPanel.add(serverIPLable);
		infoPanel.add(serverIPField);
		infoPanel.add(portLable);
		infoPanel.add(portField);

		// 用戶列表
		userList = new JList<>();
		userListPane = new JScrollPane(userList);
		userListPane.setBorder(BorderFactory.createTitledBorder("用戶列表:"));

		// 連接按鈕
		connectButton = new JButton("連接");
		disconnectButton = new JButton("斷開連接");
		disconnectButton.setEnabled(false);
		connectButtonPanel = new JPanel(new FlowLayout());
		connectButtonPanel.add(connectButton);
		connectButtonPanel.add(disconnectButton);
		
		// 底部 panel 包含 連接按鈕 和 連接信息 
		controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(infoPanel, BorderLayout.CENTER);
		controlPanel.add(connectButtonPanel, BorderLayout.SOUTH);
		
		// 列表
		listPanel = new JPanel(new BorderLayout());
		listPanel.add(userListPane, BorderLayout.CENTER);
		listPanel.add(controlPanel, BorderLayout.SOUTH);

		// 信息框
		messageArea = new JTextArea();
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setEditable(false);
		messageArea.setPreferredSize(new Dimension(283, 283));

		messagePane = new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		messagePane.setBorder(BorderFactory.createTitledBorder("信息:"));

		// 輸入框
		contentArea = new JTextArea();
		contentArea.setLineWrap(true);
		contentArea.setWrapStyleWord(true);
		contentPane = new JScrollPane(contentArea);
		contentPane.setBorder(BorderFactory.createTitledBorder("輸入信息:"));

		// 發送按鈕
		buttonPane = new JPanel(new FlowLayout());
		singleSendButton = new JButton("發送");
		groupSendButtonl = new JButton("群發");
		buttonPane.add(singleSendButton);
		buttonPane.add(groupSendButtonl);

		// 信息panel 包含  信息框、輸入框、發送按鈕
		chatPanel = new JPanel(new BorderLayout());
		chatPanel.add(messagePane, BorderLayout.NORTH);
		chatPanel.add(contentPane, BorderLayout.CENTER);
		chatPanel.add(buttonPane, BorderLayout.SOUTH);
		

		
		getContentPane().setLayout(new GridLayout(1, 2, 20, 30));
		getContentPane().add(listPanel);
		getContentPane().add(chatPanel);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);// 交給 WindowsHandler 處理
		pack();
		setVisible(true);
		setBounds(0, 0, 600, 500);
		setTitle("Online Chat - Client");
		setResizable(true);
	}

	// 群發
	public void groupSend() {
		if (contentArea.getText().equals("") || contentArea.getText().toString() == null) {
			JOptionPane.showMessageDialog(null, "輪入信息不能為空！", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		messageArea.append( "我向全部人說：" + contentArea.getText().toString() + "\n");
		try {
			// 用戶名+指令+信息
			writer.write(usernameField.getText().toString() + SEND_GROUP_MESSAGE + contentArea.getText().toString() + "\n");
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		contentArea.setText("");
	}

	// 單發
	public void singleSendMsg() {
		if (contentArea.getText().equals("") || contentArea.getText().toString() == null) {
			JOptionPane.showMessageDialog(null, "輪入信息不能為空！", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(userList.getSelectedValue() == null || userList.getSelectedValue().equals("")) {
			JOptionPane.showMessageDialog(null, "請選擇發送對象！", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(userList.getSelectedValue().split("/")[0].equals(usernameField.getText().toString())){
			JOptionPane.showMessageDialog(null, "不能给自己發送信息！", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		messageArea.append( "我向" + userList.getSelectedValue().toString().split("/")[0]
				+ "說：" + contentArea.getText().toString() + "\n");
		try {
			// 用戶名+指令+信息
			writer.write(usernameField.getText().toString()+SEND_MESSAGE+userList.getSelectedValue().toString().split("/")[0] + SEND_MESSAGE+userList.getSelectedValue().toString().split("/")[1]+SEND_MESSAGE + contentArea.getText().toString() + "\n");
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		contentArea.setText("");
	}
	
	// 斷開連接
	public void disconnectServer() {
		setSetting(true);
		try {
			writer.write(usernameField.getText().toString()+DISCONNECT);
			writer.write("\n");
			writer.flush();
			userNameVector = new Vector<>();
			connectStatus = 0;
			clearUserList();// 清空用戶列表 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 連接
	public void connectServer() {
		try {
			String ip = serverIPField.getText().toString();
			String port = portField.getText().toString();
			String userName = usernameField.getText().toString();

			// 檢查是否為空
			if (checkNull(ip) || checkNull(port) || checkNull(userName)) {
				JOptionPane.showMessageDialog(null, "連接信息不能為空！", "ERROR", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// 連接服務器
			client = new Socket(ip, Integer.parseInt(port));
			reader = getBufferedReader();
			writer = getBufferedWriter();
			setTitle("User: " + usernameField.getText().toString()+ " | Online Chat - Client ");
			sendNameToServer(userName +	SEND_USERNAME_IP + client.getLocalAddress());// 将用户名和ip发送到服务端
			setSetting(false);
			userNameVector = new Vector<>();
			connectStatus = 1;
			
			// 開啟多線程接收服務器信息
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						String line;
						while ((line = reader.readLine()) != null) {
							// 服務器單發的信息
							if (line.contains(GET_MESSAGE)) {
								String fromUser = line.split(GET_MESSAGE)[0];
								String content =  line.split(GET_MESSAGE)[1];
								messageArea.append(fromUser+" 說："+content+"\n");
								// 服務器發送用戶
							}else if(line.contains(GET_USERNAME_IP)){
								updateUserList(line);
								// 服務器群發的信息
							}else if(line.contains(GET_GROUP_MESSAGE)){
								String fromUser = line.split(GET_GROUP_MESSAGE)[0];
								String content =  line.split(GET_GROUP_MESSAGE)[1];
								messageArea.append(fromUser+" 向全部人說："+content+"\n");
							}else if(line.contains(DISCONNECT)){
								messageArea.append("用户 " + line.split(DISCONNECT)[0]+" 離線！\n");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();

		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "服務器沒有開啟，請檢查 IP 或端口信息後重試!", "ERROR", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "服務器沒有開啟，請檢查 IP 或端口信息後重試!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	// 發送用戶名
	public void sendNameToServer(String username_ip) {
		try {
			writer.write(username_ip);
			writer.write("\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 設定連接信息控件的啟用和禁用
	private void setSetting(boolean value) {
		connectButton.setEnabled(value);
		disconnectButton.setEnabled(!value);
		serverIPField.setEditable(value);
		portField.setEditable(value);
		usernameField.setEditable(value);
	}

	// 檢查連接信息
	private boolean checkNull(String toCheck) {
		if (toCheck.equals("") || toCheck == null)
			return true;
		return false;
	}
	
	// 更新用戶列表
	public void updateUserList(String line) {
		String userNameList[] = line.split(GET_USERNAME_IP);
		userNameVector.removeAllElements();
		for (String username : userNameList) {
			userNameVector.addElement(username);
		}
		userList.setModel(new DefaultComboBoxModel<>(userNameVector));
	}
	
	// 清空用戶列表 
	public void clearUserList() {
		userNameVector.removeAllElements();
		userList.setModel(new DefaultComboBoxModel<>(userNameVector));
	}
	
	public static BufferedWriter getBufferedWriter(){
		if(writer==null){
			try {
				writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writer;
	}
	
	public static BufferedReader getBufferedReader(){
		if(reader==null){
			try {
				reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reader;
	}

}
