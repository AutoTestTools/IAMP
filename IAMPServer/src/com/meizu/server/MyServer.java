package com.meizu.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import com.meizu.info.Properties;
import com.meizu.info.SocketInfo;
import com.meizu.info.SocketMessage;

public class MyServer {

	private boolean isStartServer;

	private static ServerSocket mServer;
	/*** ��Ϣ���У����ڱ���SocketServer���������ڿͻ������ֻ��ˣ�����Ϣ */
	private ArrayList<SocketMessage> mMsgList = new ArrayList<SocketMessage>();
	/*** �̶߳��У����ڽ�����Ϣ��ÿ���ͻ���ӵ��һ���̣߳�ÿ���߳�ֻ���շ��͸��Լ�����Ϣ */
	private ArrayList<SocketThread> mThreadList = new ArrayList<SocketThread>();
	/*** ���������� */
	private static RoomManager rm = new RoomManager();

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

			// ����ɾ����Чsocket�߳�
			startDelInvalidThread();

			// ����ɾ����Ч�����߳�
			startDelRoomThread();

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

	private void startDelRoomThread() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				while (isStartServer) {
					Set<String> curRoom = new HashSet<String>();
					if (mThreadList.size() > 0) {
						for (SocketThread socketThread : mThreadList) {
							if (socketThread.info.room != null)
								curRoom.add(socketThread.info.room);
						}
						List<String> roomList = rm.getAllRoom();
						if (roomList.size() > 0) {
							for (String room : roomList) {
								if (!curRoom.contains(room)) {
									rm.checkOut(room);
									break;
								}
							}
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/** ����ɾ����Чsocket���߳� */
	private void startDelInvalidThread() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (isStartServer) {
					if (mThreadList.size() > 0) {
						for (SocketThread socketThread : mThreadList) {
							try {
								BufferedWriter writer = socketThread.writer;
								JSONObject json = new JSONObject();
								json.put("title", "test connecting");
								json.put("msg", "test connecting");
								writer.write(json.toString() + "\n");
								writer.flush();
							} catch (Exception e) {
								// TODO: handle exception
								socketThread.destory();
								mThreadList.remove(socketThread);
								System.out.println("ɾ����Чsocket��" + socketThread.socketID);
								System.out.println("��ǰ�豸����>>>>>" + mThreadList.size());
								break;
							}
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
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
							String msg = from.msg;
							System.out.println("��ǰ�豸����>>>>>" + mThreadList.size());

							for (SocketThread to : mThreadList) {
								if (msg.equals(Properties.CREATE_ROOM)) {
									if (to.info.imei.equals(from.info.imei)) {
										BufferedWriter writer = to.writer;
										JSONObject json = new JSONObject();
										json.put("title", Properties.CREATE_ROOM);
										json.put("msg", "room key is _" + from.info.room);
										writer.write(json.toString() + "\n");
										writer.flush();
										System.out.println("������Ϣ�ɹ���" + from.msg + ">> to " + to.info.imei);
										break;
									}
								} else if (msg.equals(Properties.JOIN_ROOM)) {
									if (to.info.imei.equals(from.info.imei)) {
										BufferedWriter writer = to.writer;
										JSONObject json = new JSONObject();
										json.put("title", Properties.JOIN_ROOM);
										if (from.info.room != null) {
											json.put("msg", "success_" + from.info.room);
										} else {
											json.put("msg", "fail!");
										}
										writer.write(json.toString() + "\n");
										writer.flush();
										System.out.println("������Ϣ�ɹ���" + from.msg + ">> to " + to.info.imei);
										break;
									}
								} else {
									if (to.info.room != null && !to.info.imei.equals(from.info.imei) && to.info.room.equals(from.info.room)) {
										System.out.println("�յ���Ϣ��>>>>>" + to.info.imei + "from<<<<<" + from.info.imei);
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
		public boolean isRunning;

		public SocketThread(Socket socket, int count) {
			socketID = count;
			this.socket = socket;
			this.info = new SocketInfo(socketID);
			System.out.println("����һ̨�ͻ�����socketID��" + socketID);
		}

		@Override
		public void run() {
			super.run();
			isRunning = true;
			try {
				// ��ʼ��BufferedReader
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				// ��ʼ��BufferedWriter
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
				// ���isStartServer=true����˵��SocketServer�Ѿ�������
				// ������Ҫ��һ��ѭ�������Ͻ������Կͻ�������Ϣ��������������
				while (isRunning) {
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

		private void processMsg(JSONObject json) {

			String title = json.getString("title");

			// ����һ��SocketMessage�������ڽ���json�е�����
			SocketMessage msg = new SocketMessage();
			msg.msg = title;

			switch (title) {

			case Properties.INFORM_NUM:
				this.info.imei = json.getString("imei");
				this.info.phone = json.getString("phone");
				System.out.println("imei>>>>>>>>" + this.info.imei + "  phone>>>>>>" + this.info.phone);
				break;

			case Properties.INFORM_STATE:
				this.info.isBusy = json.getBoolean("state");
				System.out.println("state is busy>>>>>>>>" + this.info.isBusy);
				break;

			case Properties.CREATE_ROOM:
				this.info.room = rm.bookRoom(this.info.imei.substring(this.info.imei.length() - 4, this.info.imei.length()));
				msg.info = this.info;
				mMsgList.add(msg);
				System.out.println("room >>>>>>>>" + this.info.room);
				break;

			case Properties.JOIN_ROOM:
				if (rm.checkRoomKey(json.getString("room"))) {
					this.info.room = json.getString("room");
				}
				msg.info = this.info;
				mMsgList.add(msg);
				System.out.println("room >>>>>>>>" + this.info.room);
				break;

			case Properties.REQUEST:

				msg.info = this.info;
				// ���յ�һ����Ϣ�󣬽�����Ϣ��ӵ���Ϣ����mMsgList
				mMsgList.add(msg);
				System.out.println("���󱣴�ɹ�>>>>>>>");
				break;

			case Properties.QUIT_ROOM:
				this.info.room = null;
				System.out.println("room >>>>>>>>" + this.info.room);
				break;

			default:
				break;
			}

		}

		public void destory() {
			isRunning = false;
		}

	}

}
