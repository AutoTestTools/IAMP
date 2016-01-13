package com.meizu.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meizu.info.Properties;
import com.meizu.info.SocketInfo;

public class SocketServer {

	public boolean isStartRecieveMsg = false;

	public static Context mContext;

	public static Socket mSocket = null;
	public static SocketInfo socketInfo;

	public static SocketHandler mHandler;
	public static BufferedReader mReader;// BufferedWriter 用于推送消息
	public static BufferedWriter mWriter;// BufferedReader 用于接收消息

	public SocketServer(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		socketInfo = new SocketInfo(mContext);
		mHandler = new SocketHandler(mContext);
	}

	/**
	 * 初始化socket
	 */
	public void initSocket() {
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
							Log.e(">>>>>>>>>>>", "收到推送>>>>>>>>>>>>>>>>>>>" + data);
							// handler发送消息，在handleMessage()方法中接收
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

	public void informNum() {
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
					initSocket();
					
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	public void informState() {
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
					initSocket();
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	public void createRoom() {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("title", Properties.CREATE_ROOM);
					json.put("msg", "create room ");
					mWriter.write(json.toString() + "\n");
					mWriter.flush();
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					initSocket();
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	public void joinRoom(final String room) {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("title", Properties.JOIN_ROOM);
					json.put("room", room);
					json.put("msg", "join room " + room);
					mWriter.write(json.toString() + "\n");
					mWriter.flush();
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					initSocket();
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	public void requestCall() {
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
					initSocket();
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}
	
	public void requestMessage() {
		new AsyncTask<String, Integer, String>() {
			
			@Override
			protected String doInBackground(String... params) {
				try {
					JSONObject json = new JSONObject();
					json.put("title", Properties.REQUEST);
					json.put("msg", "message me");
					mWriter.write(json.toString() + "\n");
					mWriter.flush();
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					initSocket();
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	public void quitRoom() {
		try {
			JSONObject json = new JSONObject();
			json.put("title", Properties.QUIT_ROOM);
			json.put("msg", "quit room");
			mWriter.write(json.toString() + "\n");
			mWriter.flush();
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			initSocket();
			e.printStackTrace();
		}
	}

}
