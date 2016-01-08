package com.meizu.client;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.meizu.info.BrocastAction;

public class MyRoomActivity extends Activity {

	List<String> title;
	ListView lv;
	static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room);
		mContext = getApplicationContext();

		initListView();

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

}
