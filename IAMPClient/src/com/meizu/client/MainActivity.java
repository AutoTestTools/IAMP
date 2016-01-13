package com.meizu.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.meizu.bluetooth.BluetoothActivity;
import com.meizu.event.Model;
import com.meizu.iamp.client.R;
import com.meizu.info.Properties;
import com.meizu.socket.NewRoomActivity;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void model(View view){
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.wifi_model:
			intent.setClass(MainActivity.this, NewRoomActivity.class);
			Model.setCurModel(Properties.WIFI_MODEL);
			break;
		case R.id.buletooth_model:
			intent.setClass(MainActivity.this, BluetoothActivity.class);
			Model.setCurModel(Properties.BLUETOOTH_MODEL);
			break;
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
