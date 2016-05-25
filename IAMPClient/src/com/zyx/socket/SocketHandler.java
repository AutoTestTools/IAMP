package com.zyx.socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.zyx.info.BrocastAction;
import com.zyx.info.Properties;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SocketHandler extends Handler {

	public Context mContext;

	public SocketHandler(Context context) {
		
		this.mContext = context;
		
	}

	@Override
	public void handleMessage(Message msgStr) {
		// TODO Auto-generated method stub
		super.handleMessage(msgStr);
		if(msgStr.what == Properties.SOCKET_OUTTIME){
			Toast.makeText(mContext, "wifi服务未开启，请联系开发人员", Toast.LENGTH_SHORT).show();
			return;
		}
		JSONObject json = null;
		String title = "";
		String msg = "";
		try {
			json = new JSONObject((String) msgStr.obj);
			title = json.getString("title");
			msg = json.getString("msg");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("收到推送>>>>>>>", title +">>>>>>>>>"+ msg);
		switch (title) {
		case Properties.CREATE_ROOM:
			Intent roomKey = new Intent(BrocastAction.RESPOND_CREATE_ROOM);
			roomKey.putExtra("room", msg.split("_")[1]);
			mContext.sendBroadcast(roomKey);
			Log.e("创建房间>>>>>>>", title +">>>>>>>>>"+ msg.split("_")[1]);
			break;
		case Properties.JOIN_ROOM:
			Intent joinRes = new Intent(BrocastAction.RESPOND_JOIN_ROOM);
			String room = null;
			if(msg.contains("success")){
				room = msg.split("_")[1];
			}
			joinRes.putExtra("room", room);
			mContext.sendBroadcast(joinRes);
			break;
		case Properties.REQUEST:
			if(msg.contains("call")){
				Intent call = new Intent(BrocastAction.RESPOND_CALL);
				String phone = "";
				try {
					phone = json.getString("phone");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				call.putExtra("phone", phone);
				mContext.sendBroadcast(call);
			}else if(msg.contains("message")){
				
			}
			break;
		default:
			break;
		}
//		
//		switch (title) {
//		case "call me":
//			
//			try {
//				JSONObject json = new JSONObject((String) msg.obj);
//				String phone = json.getString("phone");
//				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				mContext.startActivity(intent);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			break;
//
//		default:
//			break;
//		}
	}
	
}
