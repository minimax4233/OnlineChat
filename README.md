# Onlice Chat

OnlineChat_Client 為客戶端，OnlineChat_Server 為服務器端，在2020年9月-2021年1月編寫

## 功能演示

#### 界面介绍

1. Server

   主界面

   ![image-20201227233842361](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227233842361.png)

   菜單

   ![image-20201227234110854](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234110854.png)

   ![image-20201227234125681](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234125681.png)

   

2. Client

   主界面

   ![image-20201227233850804](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227233850804.png)

   菜單

   ![image-20201227234146451](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234146451.png)

   ![image-20201227234154069](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234154069.png)



#### 使用说明

* Server

  輸入端口後按運行即可启動服務器，按停止就關閉服務器。默认端口65535

  ![image-20201227234328521](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234328521.png)

  server 左側用戶框可以看到當前連接用戶，右側信息框可以看到一些資訊和用戶之間的消息。

  ![image-20201227234524476](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234524476.png)

  在結束程序時會有二次確認。

  ![image-20201227234924966](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234924966.png)

  端口不合法会有提示。

  ![image-20201227235039576](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227235039576.png)

* Client

  输入用戶名、IP、端口后按連接即可連接到服務器

  ![image-20201227234615169](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234615169.png)

  選擇用戶后按發送表示單發給該用戶，按群發則是群發信息給所有用戶。

  ![image-20201227234733323](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234733323.png)

  ![image-20201227234741670](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234741670.png)

  當甚麼都不输入時按下連接、服务器不存在等會有報錯。

  ![image-20201227234815035](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234815035.png)

  ![image-20201227234856402](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234856402.png)

  在結束程序時會有二次確認。

  ![image-20201227234924966](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227234924966.png)

## 实现方式

#### 整体架构

1. server

   Main 为程序入口，ChatServer 为程序主界面和核心，ButtonHandler、MenuHandler、WindowsHandler分別为按鈕、菜單欄、窗口的控制器，監聽用戶事件。

![image-20201227235109366](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227235109366.png)

2. client

![image-20201227235117276](https://raw.githubusercontent.com/minimax4233/OnlineChat/main/README.assets/image-20201227235117276.png)

Main 为程序入口，Chatclient 为程序主界面和核心，ButtonHandler、MenuHandler、WindowsHandler分別为按鈕、菜單欄、窗口的控制器，監聽用戶事件。

#### 模块实现

1. server

   程序主界面是由 ChatServer 類續承 JFrame 得到，并包含了  Menu Bar、Tool Button、Windows，它们的事件監聽在各自的 Handler 中。

   ```java
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
   ```

   運行服務器

   ```java
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
   ```

   轉發信息

   ```java
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
   ```

   

   用戶列表

   ```java
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
   ```

   

   

   

2. client

   程序主界面是由 ChatClient 類續承 JFrame 得到，并包含了  Menu Bar、Tool Button、Windows，它们的事件監聽在各自的 Handler 中。

   ```java
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
   ```

   發信息

   ```java
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
   ```

   

