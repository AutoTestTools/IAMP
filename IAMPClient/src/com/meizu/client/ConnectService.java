package com.meizu.client;

import com.meizu.info.BrocastAction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class ConnectService extends Service {

	protected static Context mContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		mContext = getApplicationContext();
		
		createNotification();
		
		initSocket();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
//		return super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	private void createNotification() {

		NotificationManager manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		PendingIntent intent = PendingIntent.getActivity(mContext, 0, new Intent(this, MyRoomActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
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
	
	private void initSocket(){
		Intent initSocket = new Intent(BrocastAction.INIT_SOCKET);
		sendBroadcast(initSocket);
	}
	
}
