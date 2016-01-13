package com.meizu.bluetooth;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class BluetoothHandler extends Handler {
	
	private Context mContext;
	
	public BluetoothHandler(Context context){
		
		this.mContext = context;
		
	}
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
	}

}
