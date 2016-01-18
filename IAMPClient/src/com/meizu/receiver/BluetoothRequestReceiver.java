package com.meizu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meizu.bluetooth.BluetoothServer;
import com.meizu.info.BrocastAction;
import com.meizu.info.PhoneNumber;
import com.meizu.info.Properties;
import com.meizu.litepal.Data;

public class BluetoothRequestReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		BluetoothServer btServer = new BluetoothServer(context);
		String msg = "";
		boolean isSend = false;
		String action = intent.getAction();
		switch (action) {

		case BrocastAction.START_BT_SERVER:
			btServer.startBtServer();
			break;

		case BrocastAction.START_BT_CLIENT:
			btServer.startBtClient();
			break;

		case BrocastAction.STOP_BT_SERVER:
			btServer.shutdownServer();

		case BrocastAction.STOP_BT_CLIENT:
			btServer.shutdownClient();
			break;

		case BrocastAction.BT_SEND_MSG:
			msg = intent.getStringExtra("msg");
			isSend = btServer.sendMessageHandle(Properties.BT_REQUEST, msg+ Properties.PHONE_MARK + PhoneNumber.getOneNumber());
			if (isSend) {
				new Data().btSend(msg);
				sendDataChangeBrocast(context);
			}
			break;
		case BrocastAction.BT_REPLY_MSG:
			msg = intent.getStringExtra("msg");
			isSend = btServer.sendMessageHandle(Properties.BT_REPLY, msg);
			if (isSend) {
				new Data().btSend(msg);
				sendDataChangeBrocast(context);
			}
			break;

		default:
			break;

		}

	}

	private void sendDataChangeBrocast(Context context) {
		Intent data = new Intent(BrocastAction.LITEPAL_DATA_CHANGE);
		context.sendBroadcast(data);
	}

}
