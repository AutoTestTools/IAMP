package com.meizu.client;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.meizu.event.Model;
import com.meizu.info.BrocastAction;
import com.meizu.info.Properties;

public class ConnectService extends Service {

	protected static Context mContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		mContext = getApplicationContext();
		
		if(Model.getCurModel().equals(Properties.WIFI_MODEL))
			initSocket();
		else if(Model.getCurModel().equals(Properties.BLUETOOTH_MODEL))
			startBtServer();
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
	
	
	private void initSocket(){
		Intent initSocket = new Intent(BrocastAction.INIT_SOCKET);
		sendBroadcast(initSocket);
	}
	
	private void startBtServer(){
		Intent startBtServer = new Intent(BrocastAction.START_BT_SERVER);
		sendBroadcast(startBtServer);
	}
	
}
