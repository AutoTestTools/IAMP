package com.meizu.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;
import com.meizu.info.Properties;
import com.meizu.info.SocketInfo;

public class MainActivity extends Activity {

	String buffer = "";
	static TextView tv;
	EditText et;
	String getEd;

	public static Context mContext;

	private Socket mSocket = null;
	private boolean isStartRecieveMsg;
	private SocketInfo socketInfo;

	private SocketHandler mHandler;
	protected BufferedReader mReader;// BufferedWriter 用于推送消息
	protected BufferedWriter mWriter;// BufferedReader 用于接收消息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv = (TextView) findViewById(R.id.tv);
		et = (EditText) findViewById(R.id.et);
		mHandler = new SocketHandler();

		mContext = getApplicationContext();
		socketInfo = new SocketInfo(mContext);

		initSocket();
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
					tv.append(json.getString("from") + ":" + json.getString("msg") + "\n");
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

	private static String getTime(long millTime) {
		Date d = new Date(millTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}

	public void send(View view) {
		switch (view.getId()) {
		case R.id.create:
			createRoom();
			break;
		case R.id.join:
			joinRoom();
			break;
		case R.id.request:
			sendRequest();
			break;
		case R.id.quit:
			quitRoom();
			break;

		default:
			break;
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

}
