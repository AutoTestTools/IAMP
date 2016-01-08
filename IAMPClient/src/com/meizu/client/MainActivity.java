package com.meizu.client;

import com.meizu.info.BrocastAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	Button create_btn, join_btn;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		mContext = getApplicationContext();
		
		startConnectService();
	}

	private void initView() {
		// TODO Auto-generated method stub
		create_btn = (Button) findViewById(R.id.crate_room);
		join_btn = (Button) findViewById(R.id.join_room);
		
	}
	
	public void room(View view){
		switch (view.getId()) {
		
		case R.id.crate_room:
			createRoom();
			break;
		case R.id.join_room:
			joinRoom();
			break;

		default:
			break;
		}
		
		Intent request = new Intent(this, MyRoomActivity.class);
		startActivity(request);
	}

	private void joinRoom() {
		// TODO Auto-generated method stub
		Intent join = new Intent(BrocastAction.JOIN_ROOM);
		sendBroadcast(join);
	}

	private void createRoom() {
		// TODO Auto-generated method stub
		Intent create = new Intent(BrocastAction.CREATE_ROOM);
		sendBroadcast(create);
	}
	
	private void startConnectService(){
		Intent service = new Intent(this, ConnectService.class);
		startService(service);
	}

}
