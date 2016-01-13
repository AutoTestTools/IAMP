package com.meizu.receiver;

import com.meizu.bluetooth.BluetoothServer;
import com.meizu.info.BrocastAction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothRequestReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		BluetoothServer btServer = new BluetoothServer(context);
		
		String action = intent.getAction();
		switch (action) {
		
		case BrocastAction.START_BT_SERVER:
			btServer.startBtServer();
			break;
			
		case BrocastAction.START_BT_CLIENT:
			btServer.startBtClient();
			break;

		default:
			break;
			
		}
		
	}

}
