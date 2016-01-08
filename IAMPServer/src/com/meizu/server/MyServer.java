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
	
	private static ServerSocket mServer;
	/**
	 * ��Ϣ���У����ڱ���SocketServer���������ڿͻ������ֻ��ˣ�����Ϣ
	 */
	private ArrayList<SocketMessage> mMsgList = new ArrayList<SocketMessage>();
	/**
	 * �̶߳��У����ڽ�����Ϣ��ÿ���ͻ���ӵ��һ���̣߳�ÿ���߳�ֻ���շ��͸��Լ�����Ϣ
	 */
	private ArrayList<SocketThread> mThreadList = new ArrayList<SocketThread>();

	public static void main(String[] args) throws IOException {
		MyServer server = new MyServer();
		server.startSocket();
	}

	/**
	 * ����SocketServer
	 */
	private void startSocket() {
		try {
			isStartServer = true;
			int prot = Properties.PORT;// �˿ڿ����Լ����ã���Ҫ��Client�˵Ķ˿ڱ���һ��
			mServer = new ServerSocket(prot);// ����һ��ServerSocket
			System.out.println("����server,�˿ڣ�" + prot);
			
			Socket socket = null;
			
			// Android��SocketClient���ͻ�����Ψһ��־��ÿ��socketID��ʾһ��Android�ͻ���
			int socketID = 0;
			
			// ����������Ϣ�߳�
			startSendMessageThread();
			
			// ��һ��ѭ��������Ƿ����µĿͻ�������
			while (isStartServer) {
				// accept()������һ�������ķ��������ø÷�����
				// ���̻߳�һֱ������ֱ�����µĿͻ������룬����Ż����������
				socket = mServer.accept();
				// ���µĿͻ���������򴴽�һ���µ�SocketThread�̶߳���
				SocketThread thread = new SocketThread(socket, socketID++);
				thread.start();
				// �����߳���ӵ��̶߳���
				mThreadList.add(thread);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����������Ϣ�̣߳����mMsgList����SocketMessage����Ѹ���Ϣ���͵�Android�ͻ���
	 */
	public void startSendMessageThread() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					/*
					 * ���isStartServer=true����˵��SocketServer��������
					 * ��һ��ѭ���������Ϣ�������Ƿ�����Ϣ������У���������Ϣ����Ӧ�Ŀͻ���
					 */
					while (isStartServer) {
						// �ж���Ϣ�����еĳ����Ƿ����0������0��˵����Ϣ���в�Ϊ��
						if (mMsgList.size() > 0) {
							// ��ȡ��Ϣ�����еĵ�һ����Ϣ
							SocketMessage from = mMsgList.get(0);
							for (SocketThread to : mThreadList) {
								if (to.info.room!=null && !to.info.imei.equals(from.info.imei) && to.info.room.equals(from.info.room)) {
									System.out.println("�յ���Ϣ��>>>>>" + to.info.imei + "from<<<<<"+from.info.imei );
									BufferedWriter writer = to.writer;
									JSONObject json = new JSONObject();
									json.put("title", Properties.REQUEST);
									json.put("msg", "call me");
									json.put("from", from.info.imei);
									json.put("phone", from.info.phone);
									// writerд��json�е��ַ������ݣ�ĩβ�ǵüӻ��з���"\n"�������ڿͻ������޷�ʶ��
									// ��ΪBufferedReader.readLine()�����Ǹ��ݻ��з�����ȡһ�е�
									writer.write(json.toString() + "\n");
									// ����flush()������ˢ�������壬����Ϣ���͵��ֻ���
									writer.flush();
									System.out.println("������Ϣ�ɹ���" + from.msg + ">> to " + to.info.imei);
								}
							}
							// ÿ����һ����Ϣ֮�󣬾�Ҫ����Ϣ�������Ƴ�����Ϣ
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
		public Socket socket;// Socket���ڻ�ȡ�������������
		public BufferedWriter writer;// BufferedWriter ����������Ϣ
		public BufferedReader reader;// BufferedReader ���ڽ�����Ϣ

		public SocketThread(Socket socket, int count) {
			socketID = count;
			this.socket = socket;
			this.info = new SocketInfo(socketID);
			System.out.println("����һ̨�ͻ�����socketID��" + socketID);
		}

		@Override
		public void run() {
			super.run();
			try {
				// ��ʼ��BufferedReader
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				// ��ʼ��BufferedWriter
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
				// ���isStartServer=true����˵��SocketServer�Ѿ�������
				// ������Ҫ��һ��ѭ�������Ͻ������Կͻ�������Ϣ��������������
				while (isStartServer) {
					// ���ж�reader�Ƿ��Ѿ�׼����
					if (reader.ready()) {
						/*
						 * ��ȡһ���ַ�������ȡ�����������ڿͻ��� reader.readLine()������һ������������
						 * �ӵ������������ʼ�����̻߳�һֱ��������״̬�� ֱ�����յ��µ���Ϣ������Ż�������
						 */
						String data = reader.readLine();
						System.out.println("�յ�һ����Ϣ>>>>>>>" + data);
						// ��data��Ϊjson��������ݣ�����һ��json����
						JSONObject json = new JSONObject(data);
						processMsg(json);
						
					}
					// ˯��100ms��ÿ100ms���һ���Ƿ��н��յ���Ϣ
					Thread.sleep(100);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		private void processMsg(JSONObject json){

			int title = json.getInt("title");
			
			System.out.println("����ID>>>>>>>>"+title);

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
				// ����һ��SocketMessage�������ڽ���json�е�����
				SocketMessage msg = new SocketMessage();
				msg.msg = json.getString("msg");
				msg.time = getTime(System.currentTimeMillis());
				msg.info = this.info;
				// ���յ�һ����Ϣ�󣬽�����Ϣ��ӵ���Ϣ����mMsgList
				mMsgList.add(msg);
				System.out.println("��Ϣ����ɹ�>>>>>>>");
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
		 * ��ȡָ����ʽ��ʱ���ַ�����ͨ������ת������
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
