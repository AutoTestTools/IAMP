package com.meizu.bluetooth;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meizu.client.ConnectService;
import com.meizu.iamp.client.R;
import com.meizu.info.Properties;

public class BluetoothActivity extends Activity implements OnClickListener {

	private FragmentManager manager;
	private TextView[] titles = new TextView[3];

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.bt_main);

		manager = getFragmentManager();

		titles[0] = (TextView) findViewById(R.id.bt_devices);
		titles[1] = (TextView) findViewById(R.id.bt_talk);
		titles[2] = (TextView) findViewById(R.id.myinfo);

		titles[0].setOnClickListener(this);
		titles[1].setOnClickListener(this);
		titles[2].setOnClickListener(this);
		
		setCurrentFragment(R.id.bt_devices);
		
		startConnectService();

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		setCurrentFragment(view.getId());
	}
	
	private void setCurrentFragment(int index){
		Fragment newFragment = null;
		switch (index) {
		
		case R.id.bt_devices:
			titles[0].setTextColor(Properties.GREEN);
			titles[1].setTextColor(Color.BLACK);
			titles[2].setTextColor(Color.BLACK);
			newFragment = new BluetoothDevices();
			break;

		case R.id.bt_talk:
			titles[0].setTextColor(Color.BLACK);
			titles[1].setTextColor(Properties.GREEN);
			titles[2].setTextColor(Color.BLACK);
			newFragment = new TalkHistory();
			break;

		case R.id.myinfo:
			titles[0].setTextColor(Color.BLACK);
			titles[1].setTextColor(Color.BLACK);
			titles[2].setTextColor(Properties.GREEN);
			newFragment = new MyInfomation();
			break;

		default:
			break;
		}
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.frame, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	private void startConnectService() {
		// TODO Auto-generated method stub
		Intent service = new Intent(BluetoothActivity.this,ConnectService.class);
		startService(service);
	}

}
