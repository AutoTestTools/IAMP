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
			
		case Properties.BT_INFORM_MAC_AND_NAME://服务端收到对方mac和name时，启动对话模式
			String m[] = str.split("_");
			BluetoothInfo.setTheOtherAddress(m[0]);
			BluetoothInfo.setTheOtherName(m[1]);
			setCurTalking(m[0]);
			Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
			break;
			
		case Properties.BT_REQUEST://收到请求
			String action = null;
			if(str.equals(Properties.CALL_ME)){
				action = BrocastAction.RESPOND_CALL;
			}else if(str.equals(Properties.MESSAGE_ME)){
				action = BrocastAction.RESPOND_CALL;
			}else{
				action = BrocastAction.RESPOND_NOTHING;
			}
			Intent request = new Intent(action);
			request.putExtra("phone", "123456");
			request.putExtra("msg", str);
			mContext.sendBroadcast(request);
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
