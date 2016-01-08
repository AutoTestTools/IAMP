package com.meizu.client;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class SocketHandler extends Handler {

	public Context mContext;

	public SocketHandler(Context context) {
		
		this.mContext = context;
		
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		switch (msg.what) {
		case 0:
			try {
				JSONObject json = new JSONObject((String) msg.obj);
				String phone = json.getString("phone");
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		default:
			break;
		}
	}
	
}
