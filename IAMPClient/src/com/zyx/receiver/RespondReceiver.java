package com.zyx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zyx.event.Message;
import com.zyx.event.Model;
import com.zyx.event.Room;
import com.zyx.event.Telephony;
import com.zyx.info.BrocastAction;
import com.zyx.info.PhoneNumber;
import com.zyx.info.Properties;
import com.zyx.litepal.Data;

public class RespondReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();

		Log.e("收到广播>>>>>>>>", ">>>>>>>" + action);

		if (action.equals(BrocastAction.RESPOND_CREATE_ROOM) || action.equals(BrocastAction.RESPOND_JOIN_ROOM)) {

			Room.setRoom_key(intent.getStringExtra("room"));

		} else if (action.equals(BrocastAction.RESPOND_CALL)) {

			String phone = intent.getStringExtra("phone");
			String msg = intent.getStringExtra("msg");

			addReceiveData(context, msg);
			
			PhoneNumber.setTheOtherNumber(phone);//设置对方号码

			//打电话操作
			Telephony tp = new Telephony(context);
			tp.makeCall(phone);

			Intent send = new Intent(BrocastAction.BT_REPLY_MSG);
			send.putExtra("msg", Properties.CALL_ALREADY);
			context.sendBroadcast(send);

		}  else if (action.equals(BrocastAction.RESPOND_ANSWER_CALL)) {

			String msg = intent.getStringExtra("msg");

			addReceiveData(context, msg);

			//接听电话操作
			Telephony tp = new Telephony(context);
			tp.answerCall();

			Intent send = new Intent(BrocastAction.BT_REPLY_MSG);
			send.putExtra("msg", Properties.ALREADY_ANSWER_CALL);
			context.sendBroadcast(send);

		}  else if (action.equals(BrocastAction.RESPOND_END_CALL)) {

			String msg = intent.getStringExtra("msg");

			addReceiveData(context, msg);

			//挂断电话操作
			Telephony tp = new Telephony(context);
			tp.endCall();

			Intent send = new Intent(BrocastAction.BT_REPLY_MSG);
			send.putExtra("msg", Properties.ALREADY_END_CALL);
			context.sendBroadcast(send);

		}  else if (action.equals(BrocastAction.RESPOND_REVEIVER_CALL)) {

			String msg = intent.getStringExtra("msg");

			addReceiveData(context, msg);

			//监听来电操作
			Telephony tp = new Telephony(context);
			tp.registerCallReceiver();

		} else if (action.equals(BrocastAction.RESPOND_MESSAGE)) {
			
			String phone = intent.getStringExtra("phone");
			String msg = intent.getStringExtra("msg");

			addReceiveData(context, msg);
			
			PhoneNumber.setTheOtherNumber(phone);//设置对方号码
			
			//发短信操作
			Message m = new Message(context);
			m.sendMessage(phone);

			Intent send = new Intent(BrocastAction.BT_REPLY_MSG);
			send.putExtra("msg", Properties.MESSAGE_ALREADY);
			context.sendBroadcast(send);
			
		} else if(action.equals(BrocastAction.RESPOND_RECEIVE_SMS)){
			
			String msg = intent.getStringExtra("msg");
			
			addReceiveData(context, msg);
			
			Message m = new Message(context);
			m.registerSMSContentObserver();
			
		} else if(action.equals(BrocastAction.RESPOND_RECEIVE_MMS)){
			
			String msg = intent.getStringExtra("msg");
			
			addReceiveData(context, msg);
			
			Message m = new Message(context);
			m.registerMMSContentObserver();
			
		}else if (action.equals(BrocastAction.RESPOND_NOTHING)) {

			String msg = intent.getStringExtra("msg");

			addReceiveData(context, msg);
			
		}

	}

	private void addReceiveData(Context context, String msg) {
		if (Model.getCurModel().equals(Properties.BLUETOOTH_MODEL)) {
			new Data().btReceive(msg);
			Intent change = new Intent(BrocastAction.LITEPAL_DATA_CHANGE);
			context.sendBroadcast(change);
		}
	}

}
