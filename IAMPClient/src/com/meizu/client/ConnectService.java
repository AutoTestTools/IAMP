package com.meizu.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.meizu.info.Properties;
import com.meizu.info.SocketInfo;

public class ConnectService extends Service {

	protected boolean isStartRecieveMsg = false;

	protected static Context mContext;

	protected Socket mSocket = null;
	protected SocketInfo socketInfo;

	protected SocketHandler mHandler;
	protected BufferedReader mReader;// BufferedWriter 用于推送消息
	protected BufferedWriter mWriter;// BufferedReader 用于接收消息

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		mContext = getApplicationContext();

		createNotification();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		initSocket();
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	/**
	 * 初始化socket
	 */
	private void initSocket() {
		// 新建一个线程，用于初始化socket和检测是否有接收到新的消息
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					isStartRecieveMsg = true;
					mSocket = new Socket(Properties.IP, Properties.PORT);
					mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "utf-8"));
					mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "utf-8"));

					informNum();

					while (isStartRecieveMsg) {
						String data = null;
						if ((data = mReader.readLine()) != null) {
							/*
							 * 读取一行字符串，读取的内容来自于客户机 reader.readLine()方法是一个阻塞方法，
							 * 从调用这个方法开始，该线程会一直处于阻塞状态， 直到接收到新的消息，代码才会往下走
							 */
							Log.e(">>>>>>>>>>>", ">>>>>>>>>>>>>>>>>>>收到推送");
							// String data = mReader.readLine();
							Log.e(">>>>>>>>>>>", ">>>>>>>>>>>>>>>>>>>" + data);
							// handler发送消息，在handleMessage()方法中接收
							// tv.append(data+"\n");
							mHandler.obtainMessage(0, data).sendToTarget();
						}
						Thread.sleep(200);
					}
					mReader.close();
					mWriter.close();
					mSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	static class SocketHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				try {
					JSONObject json = new JSONObject((String) msg.obj);
					String phone = json.getString("phone");
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;

			default:
				break;
			}
		}
	}

	private void informNum() {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("title", Properties.INFORM_NUM);
					json.put("imei", socketInfo.IMEI);
					json.put("phone", socketInfo.phone);
					mWriter.write(json.toString() + "\n");
					mWriter.flush();
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	private void informState() {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("title", Properties.INFORM_STATE);
					json.put("state", false);
					mWriter.write(json.toString() + "\n");
					mWriter.flush();
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	private void createRoom() {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("title", Properties.CREATE_ROOM);
					json.put("msg", "SSSS");
					mWriter.write(json.toString() + "\n");
					mWriter.flush();
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	private void joinRoom() {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("title", Properties.JOIN_ROOM);
					json.put("msg", "SSSS");
					mWriter.write(json.toString() + "\n");
					mWriter.flush();
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	private void sendRequest() {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("title", Properties.REQUEST);
					json.put("msg", "call me");
					mWriter.write(json.toString() + "\n");
					mWriter.flush();
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	private void quitRoom() {
		try {
			JSONObject json = new JSONObject();
			json.put("title", Properties.QUIT_ROOM);
			json.put("msg", "SSSS");
			mWriter.write(json.toString() + "\n");
			mWriter.flush();
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createNotification() {

		NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		PendingIntent intent = PendingIntent.getActivity(mContext, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(intent);
		builder.setContentTitle("IAMP");
		builder.setContentText("IAMP Service is Running");
		builder.setTicker("IAMP Service is Running");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setAutoCancel(false);
		Notification notification = builder.build();
		notification.flags |= Notification.FLAG_NO_CLEAR;
		manager.notify((int) System.currentTimeMillis(), notification);

	}

}
