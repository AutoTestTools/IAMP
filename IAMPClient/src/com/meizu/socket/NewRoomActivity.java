package com.meizu.socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meizu.client.ConnectService;
import com.meizu.event.Room;
import com.meizu.iamp.client.R;
import com.meizu.info.BrocastAction;

public class NewRoomActivity extends Activity {

	Button create_btn, join_btn;
	static Context mContext;
	static boolean isRoom = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_room);

		initView();

		mContext = getApplicationContext();

		startConnectService();
		
	}

	private void getRoomKey() {
		// TODO Auto-generated method stub

		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Room.setRoom_key(null);
				boolean flag = false;
				long curTime = System.currentTimeMillis();
				while (System.currentTimeMillis() - curTime < 20 * 1000) {
					
					Log.e(">>>>>>>>>>>>", ">>>>>>>>>>>>>>"+ Room.getRoom_key());
					if (Room.getRoom_key() != null) {
						flag = true;
						mHandler.sendEmptyMessage(0);
						break;
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(!flag){
					mHandler.sendEmptyMessage(1);
				}
			}
		}).start();
	}

	private void initView() {
		// TODO Auto-generated method stub
		create_btn = (Button) findViewById(R.id.crate_room);
		join_btn = (Button) findViewById(R.id.join_room);

	}

	public void room(View view) {
		
		getRoomKey();
		
		switch (view.getId()) {

		case R.id.crate_room:
			Toast.makeText(mContext, "正在创建房间，请等待……", Toast.LENGTH_LONG).show();
			createRoom();
			break;
		case R.id.join_room:
			joinRoom();
			break;

		default:
			break;
		}
		
	}

	private void joinRoom() {
		// TODO Auto-generated method stub
		final EditText et = new EditText(this);
		et.setHint("房间号");
		new AlertDialog.Builder(this).setTitle("请输入房间号").setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Toast.makeText(mContext, "正在进入房间，请等待……", Toast.LENGTH_LONG).show();
				Intent join = new Intent(BrocastAction.JOIN_ROOM);
				join.putExtra("room", et.getText().toString());
				sendBroadcast(join);
			}
		}).setNegativeButton("取消", null).show();
	}

	private void createRoom() {
		// TODO Auto-generated method stub
		Intent create = new Intent(BrocastAction.CREATE_ROOM);
		sendBroadcast(create);
	}

	private void startConnectService() {
		Intent service = new Intent(this, ConnectService.class);
		startService(service);
	}
	
	Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			case 0:
				Toast.makeText(mContext, "sucess!", Toast.LENGTH_SHORT).show();
				Intent rIntent = new Intent(NewRoomActivity.this, MyRoomActivity.class);
				startActivity(rIntent);
				break;
				
			case 1:
				Toast.makeText(mContext, "fail T^T!", Toast.LENGTH_SHORT).show();
				break;
				
			default:
				break;
			}
		}
		
	};

}
