package com.meizu.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import com.meizu.info.Properties;
import com.meizu.info.SocketInfo;
import com.meizu.info.SocketMessage;

public class MyServer {

	private boolean isStartServer;
	
	private ServerSocket mServer;
	/**
	 * 消息队列，用于保存SocketServer接收来自于客户机（手机端）的消息
	 */
	private ArrayList<SocketMessage> mMsgList = new ArrayList<SocketMessage>();
	/**
	 * 线程队列，用于接收消息。每个客户机拥有一个线程，每个线程只接收发送给自己的消息
	 */
	private ArrayList<SocketThread> mThreadList = new ArrayList<SocketThread>();

	public static void main(String[] args) throws IOException {
		MyServer server = new MyServer();
		server.startSocket();
	}

	/**
	 * 开启SocketServer
	 */
	private void startSocket() {
		try {
			isStartServer = true;
			int prot = Properties.PORT;// 端口可以自己设置，但要和Client端的端口保持一致
			mServer = new ServerSocket(prot);// 创建一个ServerSocket
			System.out.println("启动server,端口：" + prot);
			
			Socket socket = null;
			
			// Android（SocketClient）客户机的唯一标志，每个socketID表示一个Android客户机
			int socketID = 0;
			
			// 开启发送消息线程
			startSendMessageThread();
			
			// 用一个循环来检测是否有新的客户机加入
			while (isStartServer) {
				// accept()方法是一个阻塞的方法，调用该方法后，
				// 该线程会一直阻塞，直到有新的客户机加入，代码才会继续往下走
				socket = mServer.accept();
				// 有新的客户机加入后，则创建一个新的SocketThread线程对象
				SocketThread thread = new SocketThread(socket, socketID++);
				thread.start();
				// 将该线程添加到线程队列
				mThreadList.add(thread);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开启推送消息线程，如果mMsgList中有SocketMessage，则把该消息推送到Android客户机
	 */
	public void startSendMessageThread() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					/*
					 * 如果isStartServer=true，则说明SocketServer已启动，
					 * 用一个循环来检测消息队列中是否有消息，如果有，则推送消息到相应的客户机
					 */
					while (isStartServer) {
						// 判断消息队列中的长度是否大于0，大于0则说明消息队列不为空
						if (mMsgList.size() > 0) {
							// 读取消息队列中的第一个消息
							SocketMessage from = mMsgList.get(0);
							for (SocketThread to : mThreadList) {
								if (to.info.room!=null && !to.info.imei.equals(from.info.imei) && to.info.room.equals(from.info.room)) {
									System.out.println("收到消息：>>>>>" + to.info.imei + "from<<<<<"+from.info.imei );
									BufferedWriter writer = to.writer;
									JSONObject json = new JSONObject();
									json.put("title", Properties.REQUEST);
									json.put("msg", "call me");
									json.put("from", from.info.imei);
									json.put("phone", from.info.phone);
									// writer写进json中的字符串数据，末尾记得加换行符："\n"，否则在客户机端无法识别
									// 因为BufferedReader.readLine()方法是根据换行符来读取一行的
									writer.write(json.toString() + "\n");
									// 调用flush()方法，刷新流缓冲，把消息推送到手机端
									writer.flush();
									System.out.println("推送消息成功：" + from.msg + ">> to " + to.info.imei);
								}
							}
							// 每推送一条消息之后，就要在消息队列中移除该消息
							mMsgList.remove(0);
						}
						Thread.sleep(200);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public class SocketThread extends Thread {

		public int socketID;
		public SocketInfo info;
		public Socket socket;// Socket用于获取输入流、输出流
		public BufferedWriter writer;// BufferedWriter 用于推送消息
		public BufferedReader reader;// BufferedReader 用于接收消息

		public SocketThread(Socket socket, int count) {
			socketID = count;
			this.socket = socket;
			this.info = new SocketInfo(socketID);
			System.out.println("新增一台客户机，socketID：" + socketID);
		}

		@Override
		public void run() {
			super.run();
			try {
				// 初始化BufferedReader
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				// 初始化BufferedWriter
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
				// 如果isStartServer=true，则说明SocketServer已经启动，
				// 现在需要用一个循环来不断接收来自客户机的消息，并作其他处理
				while (isStartServer) {
					// 先判断reader是否已经准备好
					if (reader.ready()) {
						/*
						 * 读取一行字符串，读取的内容来自于客户机 reader.readLine()方法是一个阻塞方法，
						 * 从调用这个方法开始，该线程会一直处于阻塞状态， 直到接收到新的消息，代码才会往下走
						 */
						String data = reader.readLine();
						System.out.println("收到一条消息>>>>>>>" + data);
						// 将data作为json对象的内容，创建一个json对象
						JSONObject json = new JSONObject(data);
						processMsg(json);
						
					}
					// 睡眠100ms，每100ms检测一次是否有接收到消息
					Thread.sleep(100);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		private void processMsg(JSONObject json){

			int title = json.getInt("title");
			
			System.out.println("请求ID>>>>>>>>"+title);

			switch (title) {
			
			case Properties.INFORM_NUM:
				this.info.imei = json.getString("imei");
				this.info.phone = json.getString("phone");
				System.out.println("imei>>>>>>>>"+this.info.imei + "  phone>>>>>>"+this.info.phone);
				break;

			case Properties.INFORM_STATE:
				this.info.isBusy = json.getBoolean("state");
				System.out.println("state is busy>>>>>>>>"+this.info.isBusy);
				break;
				
			case Properties.CREATE_ROOM:
				this.info.room = "A01";
				System.out.println("room >>>>>>>>"+this.info.room);
				break;
				
			case Properties.JOIN_ROOM:
				this.info.room = "A01";
				System.out.println("room >>>>>>>>"+this.info.room);
				break;
				
			case Properties.REQUEST:
				// 创建一个SocketMessage对象，用于接收json中的数据
				SocketMessage msg = new SocketMessage();
				msg.msg = json.getString("msg");
				msg.time = getTime(System.currentTimeMillis());
				msg.info = this.info;
				// 接收到一条消息后，将该消息添加到消息队列mMsgList
				mMsgList.add(msg);
				System.out.println("消息保存成功>>>>>>>");
				break;
				
			case Properties.QUIT_ROOM:
				this.info.room = null;
				System.out.println("room >>>>>>>>"+this.info.room);
				break;

			default:
				break;
			}
			
		}

		/**
		 * 获取指定格式的时间字符串，通过毫秒转换日期
		 * 
		 * @param millTime
		 */
		private String getTime(long millTime) {
			Date d = new Date(millTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(d);
		}
	}
	

}
