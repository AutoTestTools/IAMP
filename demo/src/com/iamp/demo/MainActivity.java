package com.iamp.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String LINE = "-----------------------------";

	private ListView list;

	private TextView send, receiver;

	private Context mContext;

	// 计算点击的次数
	private int count;
	// 第一次点击的时间 long型
	private long firstClick;
	// 最后一次点击的时间
	private long lastClick;
	// 第一次点击的button的id
	private int firstId;

	private ButtonOnTouchListener doubleTouchListner = new ButtonOnTouchListener();

	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

	final String[] reqType = new String[] { CALL_ME,
			TALK_ME_WHEN_RECEIVER_CALL, ANSEWER_THEN_TALK_WHEN_RECEIVER_CALL,
			END_THEN_TALK_WHEN_RECEIVER_CALL, MESSAGE_ME,
			TALK_ME_WHEN_RECEIVER_SMS, TALK_ME_WHEN_RECEIVER_MMS, MESSAGE1,
			MESSAGE2, MESSAGE3 };

	/** 请求文本 */
	public static String CALL_ME = "给我打电话";
	public static String TALK_ME_WHEN_RECEIVER_CALL = "收到电话告诉我";
	public static String END_THEN_TALK_WHEN_RECEIVER_CALL = "挂断电话告诉我";
	public static String ANSEWER_THEN_TALK_WHEN_RECEIVER_CALL = "接听电话告诉我";
	public static String MESSAGE_ME = "给我发短信";
	public static String TALK_ME_WHEN_RECEIVER_SMS = "收到短信告诉我";
	public static String TALK_ME_WHEN_RECEIVER_MMS = "收到彩信告诉我";
	public static String MESSAGE1 = "自定义";
	public static String MESSAGE2 = "对方无响应事件";
	public static String MESSAGE3 = "需要添加请联系开发人员";

	/** 广播 */
	public static String RESPOND_NOTHING = "com.meizu.socket.respond.nothing";
	public static String SEND_MESSAGE = "com.meizu.bt.send_msg";
	
	public static String permission = "发送广播(需要添加权限:):<uses-permission android:name=\"com.meizu.iamp.client.permission.SEND_MESSAGE\" />\n"+LINE;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mContext = getApplicationContext();

		send = (TextView) findViewById(R.id.send);
		// send.setOnTouchListener(doubleTouchListner);
		send.setMovementMethod(ScrollingMovementMethod.getInstance());
		send.setText(permission);

		receiver = (TextView) findViewById(R.id.receiver);
		receiver.setOnTouchListener(doubleTouchListner);
		// receiver.setMovementMethod(ScrollingMovementMethod.getInstance());

		list = (ListView) findViewById(R.id.list);
		list.setDividerHeight(0);

		MyAdapter adapter = new MyAdapter(mContext);

		mData = getData();

		list.setAdapter(adapter);

		registerReceiver();
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		int num = reqType.length;

		for (int i = 0; i < num; i = i + 2) {
			map = new HashMap<String, Object>();
			map.put("btn1", reqType[i]);
			if (i + 1 < num) {
				map.put("btn2", reqType[i + 1]);
			}
			list.add(map);
		}

		return list;
	}

	public final class ViewHolder {
		public Button btn1;
		public Button btn2;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.item, null);
				holder.btn1 = (Button) convertView.findViewById(R.id.send_btn1);
				holder.btn2 = (Button) convertView.findViewById(R.id.send_btn2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.btn1.setText((String) mData.get(position).get("btn1"));
			holder.btn2.setText((String) mData.get(position).get("btn2"));

			final int index = position;

			holder.btn1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					send.append("\n" + sendBrocast((String) mData.get(index).get("btn1")));
				}
			});

			holder.btn2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					send.append("\n" + sendBrocast((String) mData.get(index).get("btn2")));
				}
			});
			return convertView;
		}

	}

	private String sendBrocast(String msg) {
		Intent send = new Intent(SEND_MESSAGE);
		send.putExtra("msg", msg);
		mContext.sendBroadcast(send);

		return "Intent send = new Intent(\"com.meizu.bt.send_msg\");\n"
				+ "send.putExtra(\"msg\", \"" + msg +"\");\n"
				+ "mContext.sendBroadcast(send);\n"
				+ LINE;
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(RESPOND_NOTHING);
		mContext.registerReceiver(downloadReceiver, filter);
	}

	private void unregisterReceiver() {
		mContext.unregisterReceiver(downloadReceiver);
	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(RESPOND_NOTHING)) {
				receiver.setText("收到广播:\nintent.getAction():\""
						+ RESPOND_NOTHING
						+ "\";\nintent.getStringExtra(\"msg\"):"
						+ intent.getStringExtra("msg"));
			}
		}
	};

	private class ButtonOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
				if (firstClick != 0 && firstId != 0
						&& System.currentTimeMillis() - firstClick > 300) {
					count = 0;
					firstId = 0;
				}
				count++;
				if (count == 1) {
					firstClick = System.currentTimeMillis();
					// 记录第一次点得按钮的id
					firstId = v.getId();
				} else if (count == 2) {
					lastClick = System.currentTimeMillis();
					// 两次点击小于300ms 也就是连续点击
					if (lastClick - firstClick < 300) {
						// 第二次点击的button的id
						int id = v.getId();
						// 判断两次点击的button是否是同一个button
						if (id == firstId) {
							// if (id == R.id.send) {
							send.setText(permission);
							// } else if (id == R.id.receiver) {
							receiver.setText("接收广播:");
							// }
						}
					}
				}
			}

			return true;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver();
	}

}
