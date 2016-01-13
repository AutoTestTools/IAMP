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

	// ����������
	private class ServerThread extends Thread {
		public void run() {

			try {
				/*
				 * ����һ������������ �����ֱ𣺷��������ơ�UUID
				 */
				mserverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(BluetoothInfo.getOneAddress(), UUID.fromString(Properties.BLUETOOTH_UUID));

				Message msg = new Message();
				msg.obj = "���Ժ����ڵȴ��ͻ��˵�����...";
				msg.what = 0;
				mHandler.sendMessage(msg);

				/* ���ܿͻ��˵��������� */
				socket = mserverSocket.accept();

				Message msg2 = new Message();
				String info = "�ͻ����Ѿ������ϣ����Է�����Ϣ��";
				msg2.obj = info;
				msg.what = 0;
				mHandler.sendMessage(msg2);
				// ������������
				rThread = new ReadThread();
				rThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	// �����ͻ���
	private class ClientThread extends Thread {
		public void run() {
			try {
				// ����һ��Socket���ӣ�ֻ��Ҫ��������ע��ʱ��UUID��
				socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Properties.BLUETOOTH_UUID));
				// ����
				Message msg2 = new Message();
				msg2.obj = "���Ժ��������ӷ�����:" + BluetoothInfo.getTheOtherAddress();
				msg2.what = 0;
				mHandler.sendMessage(msg2);

				socket.connect();

				Message msg = new Message();
				msg.obj = "�Ѿ������Ϸ���ˣ����Է�����Ϣ��";
				msg.what = 0;
				mHandler.sendMessage(msg);
				// ������������
				rThread = new ReadThread();
				rThread.start();
			} catch (IOException e) {
				Log.e("connect", "", e);
				Message msg = new Message();
				msg.obj = "���ӷ�����쳣���Ͽ�����������һ�ԡ�";
				msg.what = 0;
				mHandler.sendMessage(msg);
			}
		}
	};

	// ��������
	public void sendMessageHandle(String msg) {
		if (socket == null) {
			Toast.makeText(mContext, "û������", Toast.LENGTH_SHORT).show();
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

	// ��ȡ����
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
