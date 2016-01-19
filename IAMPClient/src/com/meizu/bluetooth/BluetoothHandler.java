package com.meizu.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.meizu.iamp.client.R;
import com.meizu.info.BluetoothInfo;
import com.meizu.info.BrocastAction;
import com.meizu.info.Properties;

public class BluetoothHandler extends Handler {

	private Context mContext;

	public BluetoothHandler(Context context) {

		this.mContext = context;

	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		int what = msg.what;
		String str = msg.obj.toString();
		switch (what) {

		case Properties.BT_OPEN_TALKING_TAB:// 客户端自启对话模式
			setCurTalking(BluetoothInfo.getTheOtherAddress());
			break;

		case Properties.BT_INFORM_MAC_AND_NAME:// 服务端收到对方mac和name时，启动对话模式
			String m[] = str.split(Properties.PHONE_MARK)[0].split("_");
			BluetoothInfo.setTheOtherAddress(m[0]);
			BluetoothInfo.setTheOtherName(m[1]);
			setCurTalking(m[0]);
			Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
			break;

		case Properties.BT_REQUEST:// 收到请求
			String action = null;
			String[] message = str.split(Properties.PHONE_MARK);
			if (message[0].equals(Properties.CALL_ME)) {
				action = BrocastAction.RESPOND_CALL;
			} else if (message[0].equals(Properties.TALK_ME_WHEN_RECEIVER_CALL)) {
				action = BrocastAction.RESPOND_REVEIVER_CALL;
			} else if (message[0].equals(Properties.END_THEN_TALK_WHEN_RECEIVER_CALL)) {
				action = BrocastAction.RESPOND_END_CALL;
			} else if (message[0].equals(Properties.ANSEWER_THEN_TALK_WHEN_RECEIVER_CALL)) {
				action = BrocastAction.RESPOND_ANSWER_CALL;
			} else if (message[0].equals(Properties.MESSAGE_ME)) {
				action = BrocastAction.RESPOND_MESSAGE;
			} else if (message[0].equals(Properties.TALK_ME_WHEN_RECEIVER_SMS)) {
				action = BrocastAction.RESPOND_RECEIVE_SMS;
			} else if (message[0].equals(Properties.TALK_ME_WHEN_RECEIVER_MMS)) {
				action = BrocastAction.RESPOND_RECEIVE_MMS;
			} else {
				action = BrocastAction.RESPOND_NOTHING;
			}
			Intent request = new Intent(action);
			if(message.length >1)
				request.putExtra("phone", message[1]);
			request.putExtra("msg", message[0]);
			mContext.sendBroadcast(request);
			break;

		case Properties.BT_REPLY:// 收到回复
			Intent nothing = new Intent(BrocastAction.RESPOND_NOTHING);
			nothing.putExtra("msg", str);
			mContext.sendBroadcast(nothing);
			break;

		case Properties.BT_RESPOND:

			break;

		default:
			Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void setCurTalking(String mac) {
		// TODO Auto-generated method stub
		CurReqPage.setTalking(true);
		CurReqPage.setTalker_mac(mac);
		Intent tabIntent = new Intent(BrocastAction.BT_TAB_CHANGE);
		tabIntent.putExtra("tab", R.id.bt_talk);
		mContext.sendBroadcast(tabIntent);
	}

}
