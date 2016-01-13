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

	private BluetoothServerSocket mserverSocket = null;
	private BluetoothSocket socket = null;
	private BluetoothDevice device = null;
	private ReadThread rThread = null;;
	private BluetoothHandler mHandler = null;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	public BluetoothServer(Context context) {
		// TODO Auto-generated constructor stub

		this.mContext = context;

		mHandler = new BluetoothHandler(mContext);
	}

	public void startBtServer() {

		new ServerThread().start();
	}

	public synchronized void startBtClient() {
		
		device = mBluetoothAdapter.getRemoteDevice(BluetoothInfo.getTheOtherAddress());

		new ClientThread().start();

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
				msg.what = 0;
				mHandler.sendMessage(msg2);
				// 启动接受数据
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
				Message msg2 = new Message();
				msg2.obj = "请稍候，正在连接服务器:" + BluetoothInfo.getTheOtherAddress();
				msg2.what = 0;
				mHandler.sendMessage(msg2);

				socket.connect();

				Message msg = new Message();
				msg.obj = "已经连接上服务端！可以发送信息。";
				msg.what = 0;
				mHandler.sendMessage(msg);
				// 启动接受数据
				rThread = new ReadThread();
				rThread.start();
			} catch (IOException e) {
				Log.e("connect", "", e);
				Message msg = new Message();
				msg.obj = "连接服务端异常！断开连接重新试一试。";
				msg.what = 0;
				mHandler.sendMessage(msg);
			}
		}
	};

	// 发送数据
	public void sendMessageHandle(String msg) {
		if (socket == null) {
			Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			OutputStream os = socket.getOutputStream();
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
						String s = new String(buf_data);
						Message msg = new Message();
						msg.obj = s;
						msg.what = 1;
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

}
