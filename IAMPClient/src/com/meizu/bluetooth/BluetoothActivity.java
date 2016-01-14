package com.meizu.bluetooth;

import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meizu.client.ConnectService;
import com.meizu.iamp.client.R;
import com.meizu.info.BrocastAction;
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
		
		registerReceiver();
		
		startConnectService();
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent tabIntent = new Intent(BrocastAction.BT_TAB_CHANGE);
		tabIntent.putExtra("tab", view.getId());
		sendBroadcast(tabIntent);
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
			newFragment = getTalkPage();
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
	
	private Fragment getTalkPage(){
		if(CurReqPage.isTalking())
			return new Talking();
		else
			return new TalkHistory();
	}
	
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BrocastAction.BT_TAB_CHANGE);
		registerReceiver(tabReceiver, filter);
	}

	private BroadcastReceiver tabReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(BrocastAction.BT_TAB_CHANGE)){
				setCurrentFragment(intent.getIntExtra("tab", R.id.bt_devices));
			}
				
		}
	};

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(tabReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
