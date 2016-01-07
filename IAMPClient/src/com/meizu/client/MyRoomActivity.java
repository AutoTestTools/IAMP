package com.meizu.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MyRoomActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room);
	}
	
	public void quit(View view){
		onBackPressed();
	}
}
