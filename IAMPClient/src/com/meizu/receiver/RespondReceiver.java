package com.meizu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.meizu.event.Room;
import com.meizu.event.Telephony;
import com.meizu.info.BrocastAction;

public class RespondReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();

		Log.e("ÊÕµ½¹ã²¥>>>>>>>>", ">>>>>>>" + action);

		if (action.equals(BrocastAction.RESPOND_CREATE_ROOM) || action.equals(BrocastAction.RESPOND_JOIN_ROOM)) {
			
			Room.setRoom_key(intent.getStringExtra("room"));
			
		} else if(action.equals(BrocastAction.RESPOND_CALL)){
			
			String phone = intent.getStringExtra("phone");

			Telephony tp = new Telephony(context);
			tp.makeCall(phone);
			
		}

	}

}
