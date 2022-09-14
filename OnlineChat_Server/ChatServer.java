package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

public class ChatServer extends JFrame {

	private static final long serialVersionUID = 5460745086570870800L;

	private ArrayList<Socket> sockets;
	private Vector<String> userNameList; // 用戶列表

	private BufferedReader reader;
	private BufferedWriter writer;

	// 聲明控件
	private JMenuBar menuBar = new JMenuBar(); // 菜單欄
	private JMenu serverMenu = new JMenu("服務器"); // Server 菜單
	private JMenuItem runMenuItem = new JMenuItem("運行服務器");
	private JMenuItem stopMenuItem = new JMenuItem("關閉服務器");
	private JMenu aboutMenu = new JMenu("關於"); // 關於菜單
	private JMenuItem helpMenuItem = new JMenuItem("幫助");
	private JMenuItem aboutMenuItem = new JMenuItem("關於");
	
	private JList<String> userList; // 用戶列表
	private JScrollPane userListPane; 
	public JTextArea messageTextArea; // 信息框
	private JScrollPane messageTextPane;
	private JLabel serverPortLable;// 端口
	private JTextField serverPortField; 
	private JPanel serverPortPanel;
	private JButton runServer; // 運行服務器
	private JButton stopServer; // 關閉服務器
	private JPanel buttonPanel; 
	private JPanel controlPanel; // 底部panel 含 button 和 port 
	
	// 運行狀態
	public int serverStatus = 0;
	
	
	// 常量

	public static final String GET_USERNAME_IP = "SendUsernameIP";
	public static final String GET_GROUP_MESSAGE = "SendGroupMessage";
	public static final String GET_MESSAGE = "SendMessage";
	public static final String SEND_MESSAGE = "GetMessage";
	public static final String SEND_USERNAME_IP = "GetUsernameIP";
	public static final String SEND_GROUP_MESSAGE = "GetGroupMessage";
	public static final String DISCONNECT = "Disconnect";
	
	private ServerSocket server;

	public ChatServer() {
		MainForm(); //主界面
		setListener(); // 監聽器
	}

	// 控件監聽器
	private void setListener() {
		ButtonHandler buttonHandler = new ButtonHandler(this);
		runServer.addActionListener(buttonHandler);
		stopServer.addActionListener(buttonHandler);
		
		MenuHandler menuHandler = new MenuHandler(this);
		runMenuItem.addActionListener(menuHandler);
		stopMenuItem.addActionListener(menuHandler);
		helpMenuItem.addActionListener(menuHandler);
		aboutMenuItem.addActionListener(menuHandler);
		
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
		
		// 用戶列表
		userList = new JList<>();
		userListPane = new JScrollPane();
		userListPane.getViewport().setView(userList);
		userListPane.setBorder(BorderFactory.createTitledBorder("用戶:")); // 標題
		userListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		userListPane.setOpaque(false);
		userListPane.setPreferredSize(new Dimension(200, 130));
		

		// 信息框
		messageTextArea = new JTextArea();
		messageTextArea.setEditable(false);
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setPreferredSize(new Dimension(400, 200));
		
		messageTextPane = new JScrollPane(messageTextArea);
		messageTextPane.setBorder(BorderFactory.createTitledBorder("信息:"));// 標題
		messageTextPane.setOpaque(false);
		
		// Control Panel
		serverPortLable = new JLabel("端口：");
		serverPortField = new JTextField("65535   ");
		serverPortPanel = new JPanel(new FlowLayout());
		serverPortPanel.add(serverPortLable);
		serverPortPanel.add(serverPortField);
		
		runServer = new JButton("運行");
		runServer.setEnabled(true);
		stopServer = new JButton("停止");
		stopServer.setEnabled(false);
		
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(runServer);
		buttonPanel.add(stopServer);
		
		controlPanel = new JPanel(new GridLayout(2, 1));
		controlPanel.add(serverPortPanel);
		controlPanel.add(buttonPanel);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(userListPane, BorderLayout.WEST);
		getContentPane().add(messageTextPane, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);// 交給 WindowsHandler 處理
		pack(); // 自動適應子組件的首選大小
		setVisible(true);
		setTitle("Online Chat - Server");
		setResizable(true);
	}

	// 關閉服務器
	public void stopServer() {
		
		try {
			server.close();
			// 服務器關閉後 run 按鈕啟用,stop 按鈕禁用
			runServer.setEnabled(true);
			stopServer.setEnabled(false);
			serverStatus = 0;
			messageTextArea.append("INFO: 服務器已停止！\n");
			clearUserList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 運行服務器
	public void runServer() {
		// 檢查端口是否正確			
		String serverPortString = serverPortField.getText().toString().trim();
		int [] serverPort = new int[1]; // 解決 Local variable serverPort defined in an enclosing scope must be final or effectively final
		serverPort[0] = 65535; // 默認端口
		if(!serverPortString.matches("\\d+")) {
			JOptionPane.showMessageDialog(null, "請輸入正確的端口號(0-65535)，建議大於3000", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}else {
			serverPort[0] = Integer.parseInt(serverPortString);
			if(serverPort[0] < 0 && serverPort[0] > 65535) {
				JOptionPane.showMessageDialog(null, "請輸入正確的端口號(0-65535)，建議大於3000", "ERROR", JOptionPane.ERROR_MESSAGE);
				serverPort[0] = 65535;
				return;
			}
			
		}
		
		// 服務器運行後 run 按鈕禁用,stop 按鈕啟用
		runServer.setEnabled(false);
		stopServer.setEnabled(true);
		userNameList = new Vector<>();
		sockets = new ArrayList<>();
		serverStatus = 1;
		messageTextArea.setText("");
		
		// 開啟多線程運行服務器
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server = new ServerSocket(serverPort[0]);
					messageTextArea.append("INFO: 服務器正在 " + serverPort[0] + " 端口上運行！\n");
					while (true) {
						Socket client = server.accept();
						reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
						writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
						sockets.add(client);// 将客户端的socket添加到集合中
						messageTextArea.append("INFO: IP 地址為" + client.getInetAddress().getHostAddress() + "的用戶已連接\n");
						new transferMessage(sockets, client).start();

					}
				} catch (Exception e) {
					

				}
			}
		}).start();
	}
	
	// 轉發信息
	class transferMessage extends Thread {
		private Socket client;
		private ArrayList<Socket> sockets;

		public transferMessage(ArrayList<Socket> sockets, Socket client) {
			this.sockets = sockets;
			this.client = client;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					// 單發
					if (line.contains(GET_MESSAGE)) {
						String ip = line.split(GET_MESSAGE)[2];
						for (Socket s : sockets) {
							if (s.getInetAddress().getHostAddress().equals(ip)) {
								BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
								// 用戶名+指令+信息
								writer.write(line.split(GET_MESSAGE)[0] + SEND_MESSAGE + line.split(GET_MESSAGE)[3]);
								writer.write("\n");
								writer.flush();
							}
						}
						messageTextArea.append(line.split(GET_MESSAGE)[0] + "向"
								+ line.split(GET_MESSAGE)[1] + "說："
								+ line.split(GET_MESSAGE)[3] + "\n");
						// 群發
					} else if (line.contains(GET_GROUP_MESSAGE)) {
						for (Socket s : sockets) {
							if (!s.getInetAddress().getHostAddress().equals(client.getInetAddress().getHostAddress())) {
								BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
								// 用戶名+指令+信息
								writer.write(line.split(GET_GROUP_MESSAGE)[0] + SEND_GROUP_MESSAGE + line.split(GET_GROUP_MESSAGE)[1]);
								writer.write("\n");
								writer.flush();
							}
						}
						messageTextArea.append(line.split(GET_GROUP_MESSAGE)[0] + "向全部人說："
								+ line.split(GET_GROUP_MESSAGE)[1] + "\n");
						// 獲得用戶名
					} else if (line.contains(GET_USERNAME_IP)) {
						if (!userNameList.contains(line.split(GET_USERNAME_IP)[0]
								+ line.split(GET_USERNAME_IP)[1])) {
							userNameList.addElement(line.split(GET_USERNAME_IP)[0]
									+ line.split(GET_USERNAME_IP)[1]);
						}
						userList.setModel(new DefaultComboBoxModel<>(userNameList));
						updateUserList();
					} else if (line.contains(DISCONNECT)) {
						 sockets.remove(client);
						 for(int i=0;i<userNameList.size();i++){
							 if(userNameList.get(i).toString().contains(line.split(DISCONNECT)[0])){
								 userNameList.removeElement(userNameList.get(i));
							 }
						 }
						 userList.setModel(new DefaultComboBoxModel<>(userNameList));
						 updateUserList();
						messageTextArea.append("用戶 " + line.split(DISCONNECT)[0] + " 斷開連接\n");
						for (Socket s : sockets) {
							BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
							// 用戶名+指令+信息
							writer.write(line.split(DISCONNECT)[0] + DISCONNECT);
							writer.write("\n");
							writer.flush();
						}
						 this.stop();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 清空用戶列表 
	public void clearUserList() {
		userNameList.removeAllElements();
		userList.setModel(new DefaultComboBoxModel<>(userNameList));
	}
	
	// 更新客戶端用戶名單
	private void updateUserList() {
		for (Socket s : sockets) {
			StringBuilder buider = new StringBuilder();
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				for (String name_ip : userNameList) {
					buider.append(SEND_USERNAME_IP + name_ip);
				}
				writer.write(buider.toString() + "\n");
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
