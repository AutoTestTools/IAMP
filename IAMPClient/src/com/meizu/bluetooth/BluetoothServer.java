package com.meizu.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.meizu.info.BluetoothInfo;
import com.meizu.info.Properties;

public class BluetoothServer {

	private Context mContext;

	private BluetoothDevice device = null;
	private ReadThread rThread = null;;
	private BluetoothHandler mHandler = null;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	static private BluetoothServerSocket mserverSocket = null;
	static private BluetoothSocket socket = null;
	static private ServerThread serverThread = null;
	static private ClientThread clientThread = null;

	public BluetoothServer(Context context) {
		// TODO Auto-generated constructor stub

		this.mContext = context;

		mHandler = new BluetoothHandler(mContext);
	}

	public void startBtServer() {
		if (serverThread == null) {
			
			serverThread = new ServerThread();
			serverThread.start();
		}
	}

	public synchronized void startBtClient() {

		if (clientThread == null) {

			device = mBluetoothAdapter.getRemoteDevice(BluetoothInfo.getTheOtherAddress());

			clientThread = new ClientThread();
			clientThread.start();

		}
	}

	// 开启服务器
	private class ServerThread extends Thread {
		public void run() {

			try {
				/*
				 * 创建一个蓝牙服务器 参数分别：服务器名称、UUID
				 */
				mserverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BluetoothInfo.getOneAddress(), UUID.fromString(Properties.BLUETOOTH_UUID));

				Message msg = new Message();
				msg.obj = "请稍候，正在等待客户端的连接...";
				msg.what = 0;
				mHandler.sendMessage(msg);

				/* 接受客户端的连接请求 */
				socket = mserverSocket.accept();

				Message msg2 = new Message();
				String info = "客户端已经连接上！可以发送信息。";
				msg2.obj = info;
				msg2.what = 0;
				mHandler.sendMessage(msg2);

				// 启动接受数据
				if (rThread != null)
					rThread.interrupt();
				rThread = new ReadThread();
				rThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	// 开启客户端
	private class ClientThread extends Thread {
		public void run() {
			try {
				// 创建一个Socket连接：只需要服务器在注册时的UUID号
				socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Properties.BLUETOOTH_UUID));
				// 连接
				Message msg = new Message();
				msg.obj = "请稍候，正在连接服务器:" + BluetoothInfo.getTheOtherAddress();
				msg.what = Properties.BT_CONNECTINT;
				mHandler.sendMessage(msg);

				socket.connect();

				Message msg1 = new Message();
				msg1.obj = "已经连接上服务端！可以发送信息。";
				msg1.what = Properties.BT_CONNECTED;
				mHandler.sendMessage(msg1);

				sendMessageHandle(Properties.BT_INFORM_MAC_AND_NAME, BluetoothInfo.getOneAddress() + "_" + BluetoothInfo.getOneName());// 发送我的mac_name

				Message msg2 = new Message();
				msg2.obj = "开启对话模式";
				msg2.what = Properties.BT_OPEN_TALKING_TAB;
				mHandler.sendMessage(msg2);

				// 启动接受数据
				if (rThread != null)
					rThread.interrupt();
				rThread = new ReadThread();
				rThread.start();
			} catch (IOException e) {
				Message msg = new Message();
				msg.obj = "连接服务端异常！断开连接重新试一试。";
				msg.what = Properties.BT_CONNECT_ERROR;
				mHandler.sendMessage(msg);
			}
		}
	};

	// 发送数据
	public void sendMessageHandle(int what, String m) {
		if (socket == null) {
			Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			OutputStream os = socket.getOutputStream();
			String msg = what + Properties.WHAT_MARK + m;
			os.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 读取数据
	private class ReadThread extends Thread {
		public void run() {

			byte[] buffer = new byte[1024];
			int bytes;
			InputStream mmInStream = null;

			try {
				mmInStream = socket.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (true) {
				try {
					// Read from the InputStream
					if ((bytes = mmInStream.read(buffer)) > 0) {
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						String[] str = new String(buf_data).split(Properties.WHAT_MARK);
						Message msg = new Message();
						msg.obj = str[1];
						msg.what = Integer.parseInt(str[0]);
						mHandler.sendMessage(msg);
					}
				} catch (IOException e) {
					try {
						mmInStream.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			}
		}
	}

	/* 停止服务器 */
	public void shutdownServer() {
		new Thread() {
			public void run() {
				if (serverThread != null) {
					serverThread.interrupt();
					serverThread = null;
				}
				if (rThread != null) {
					rThread.interrupt();
					rThread = null;
				}
				try {
					if (socket != null) {
						socket.close();
						socket = null;
					}
					if (mserverSocket != null) {
						mserverSocket.close();/* 关闭服务器 */
						mserverSocket = null;
					}
				} catch (IOException e) {
					Log.e("server", "mserverSocket.close()", e);
				}
			};
		}.start();
	}

	/* 停止客户端连接 */
	public void shutdownClient() {
		new Thread() {
			public void run() {
				if (clientThread != null) {
					clientThread.interrupt();
					clientThread = null;
				}
				if (rThread != null) {
					rThread.interrupt();
					rThread = null;
				}
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					socket = null;
				}
			};
		}.start();
	}
}
