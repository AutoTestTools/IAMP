package com.meizu.client;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meizu.event.Room;
import com.meizu.info.BrocastAction;
import com.meizu.socket.client.R;

public class MyRoomActivity extends Activity {

	List<String> title;
	ListView lv;
	TextView key,count;
	static Context mContext;
	Notification notification;
	NotificationManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room);
		
		mContext = getApplicationContext();
		manager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

		initTextView();
		
		initListView();
		
		createNotification();

	}

	private void initTextView() {
		// TODO Auto-generated method stub
		key = (TextView) findViewById(R.id.room_number);
		count = (TextView) findViewById(R.id.devices_count);
		
		key.setText("当前房间号:" + Room.getRoom_key());
	}

	private void initListView() {
		title = Arrays.asList("来电", "短信");
		lv = (ListView) findViewById(R.id.requestlistview);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, title));
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> contentView, View view, int index, long arg3) {
				// TODO Auto-generated method stub
				final int i = index;
				new AlertDialog.Builder(MyRoomActivity.this).setTitle("请求确认").setMessage(title.get(i) + "?")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								request(title.get(i));
							}
						}).setNegativeButton("取消", null).show();
			}
		});
	}
	
	public void quit(View view){
		
		Intent quit = new Intent(BrocastAction.QUIT_ROOM);
		sendBroadcast(quit);
		
		//设置本地房间号为空
		Room.setRoom_key(null);
		
		//取消通知栏通知
		manager.cancel(0);
		
		//返回上一Activity
		onBackPressed();
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK)
			return true;
		
		return super.onKeyDown(keyCode, event);
	}

	private void request(String str) {
		String action = "";
		switch (str) {

		case "来电":
			action = BrocastAction.REQUEST_CALL;
			break;

		case "短信":
			action = BrocastAction.REQUEST_MESSAGE;
			break;

		default:
			break;
		}
		Intent request = new Intent(action);
		sendBroadcast(request);
	}
	
	private void createNotification() {

		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        Intent intent = new Intent(this, MyRoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(pendingIntent);
		builder.setContentTitle("IAMP");
		builder.setContentText("IAMP Service is Running");
		builder.setTicker("IAMP Service is Running");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setAutoCancel(false);
		notification = builder.build();
		notification.flags |= Notification.FLAG_NO_CLEAR;
		manager.notify(0, notification);

	}
	
}
