package com.zyx.bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.litepal.crud.DataSupport;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyx.iamp.client.R;
import com.zyx.info.BluetoothInfo;
import com.zyx.info.BrocastAction;
import com.zyx.info.Properties;
import com.zyx.litepal.Data;

public class Talking extends Fragment implements OnClickListener {

	private Context mContext;
	private ListView mList;
	private TextView ip;
	private ImageView img;
	private EditText et;
	private Button send, choose;
	private ImageButton close;

	private List<Map<String, Object>> mData;
	private List<Data> dataList;

	private MyAdapter adapter;

	final String[] reqType = new String[] { Properties.CALL_ME, Properties.TALK_ME_WHEN_RECEIVER_CALL, Properties.ANSEWER_THEN_TALK_WHEN_RECEIVER_CALL,
			Properties.END_THEN_TALK_WHEN_RECEIVER_CALL, Properties.MESSAGE_ME, Properties.TALK_ME_WHEN_RECEIVER_SMS, Properties.TALK_ME_WHEN_RECEIVER_MMS };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = getActivity();

		registerReceiver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.bt_talking, null);

		mList = (ListView) view.findViewById(R.id.msg_content_listview);
		ip = (TextView) view.findViewById(R.id.talker_ip);
		et = (EditText) view.findViewById(R.id.msg);
		img = (ImageView) view.findViewById(R.id.back_to_history);// 返回按钮
		send = (Button) view.findViewById(R.id.send);// 发送
		choose = (Button) view.findViewById(R.id.choose);
		close = (ImageButton) view.findViewById(R.id.close);

		mList.setDividerHeight(0);// 设为无分割线模式
		ip.setText(CurReqPage.getTalker_mac());
		img.setOnClickListener(this);
		send.setOnClickListener(this);
		choose.setOnClickListener(this);
		close.setOnClickListener(this);

		if (CurReqPage.getTalker_mac().equals(BluetoothInfo.getTheOtherAddress())) {// 如果当前聊天界面是当前的对话界面，则添加按钮及发送按钮可以点击
			send.setEnabled(true);
			choose.setEnabled(true);
			close.setEnabled(true);
		} else {// 否则不可点击
			send.setEnabled(false);
			choose.setEnabled(false);
			close.setEnabled(false);
		}

		adapter = new MyAdapter(mContext);

		myHandler.sendEmptyMessage(0);

		return view;
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		setDataList();
		for (int i = 0; i < dataList.size(); i++) {
			String name = dataList.get(i).getFrom_name();
			String msg = dataList.get(i).getMsg();
			String time = dataList.get(i).getTime();
			boolean byMe = dataList.get(i).getFrom_mac().equals(BluetoothInfo.getOneAddress());

			map = new HashMap<String, Object>();
			map.put("name", name != null ? name : "");
			map.put("msg", msg != null ? msg : "");
			map.put("time", time != null ? time : "");
			map.put("byMe", byMe);
			list.add(map);
		}
		return list;
	}

	public final class ViewHolder {
		public LinearLayout layout;
		public TextView myname;
		public TextView yourname;
		public TextView msg;
		public TextView time;
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

			String name = (String) mData.get(position).get("name");
			String msg = (String) mData.get(position).get("msg");
			String time = (String) mData.get(position).get("time");
			boolean byMe = (boolean) mData.get(position).get("byMe");

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.talking_list_item, null);
				holder.layout = (LinearLayout) convertView.findViewById(R.id.msg_content_layout);
				holder.time = (TextView) convertView.findViewById(R.id.msg_time);
				holder.myname = (TextView) convertView.findViewById(R.id.msg_my_name);
				holder.yourname = (TextView) convertView.findViewById(R.id.msg_your_name);
				holder.msg = (TextView) convertView.findViewById(R.id.msg_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.time.setVisibility(View.INVISIBLE);
			if (byMe) {
//				holder.myname.setText(name);
//				holder.myname.setVisibility(View.GONE);
				holder.yourname.setText("");
				holder.myname.setVisibility(View.VISIBLE);
				holder.yourname.setVisibility(View.INVISIBLE);
				holder.msg.setBackground(mContext.getDrawable(R.drawable.my));
				holder.layout.setGravity(Gravity.RIGHT);
			} else {
				holder.myname.setText("");
				holder.yourname.setText(name);
				holder.myname.setVisibility(View.INVISIBLE);
				holder.yourname.setVisibility(View.VISIBLE);
				holder.msg.setBackground(mContext.getDrawable(R.drawable.your));
				holder.layout.setGravity(Gravity.LEFT);
			}
			holder.time.setText(time);
			holder.msg.setText(msg);
			return convertView;
		}

	}

	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			mData = getData();

			mList.setAdapter(adapter);

			mList.setSelection(adapter.getCount() - 1);// 设置更新数据后，滚动到最后一行

			super.handleMessage(msg);
		};
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.close:
			Intent closeIntent = new Intent(BrocastAction.BT_CLOSE);
			mContext.sendBroadcast(closeIntent);
			BluetoothInfo.setTheOtherAddress("null");
			BluetoothInfo.setTheOtherName("null");
			closeTalking();
			break;
		case R.id.back_to_history:
			closeTalking();
			break;
		case R.id.send:
			String msg = et.getText().toString();
			if (msg != null && !msg.equals("")) {
				Intent send = new Intent(BrocastAction.BT_SEND_MSG);
				send.putExtra("msg", msg);
				mContext.sendBroadcast(send);
				et.setText("");
			} else {
				Toast.makeText(mContext, "发送消息不能为空", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.choose:

			AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择请求")

			.setSingleChoiceItems(reqType, -1, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					et.setText(reqType[which]);

				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					et.setText("");
				}

			}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Intent send = new Intent(BrocastAction.BT_SEND_MSG);
					String msg = et.getText().toString();
					if(!msg.equals("")){
						send.putExtra("msg", msg);
						mContext.sendBroadcast(send);
						et.setText("");
					}
				}

			}).create();
			dialog.show();
			break;

		default:
			break;
		}
	}

	public void setDataList() {
		dataList = DataSupport.where("from_mac = ? or to_mac = ?", CurReqPage.getTalker_mac(), CurReqPage.getTalker_mac()).order("time asc").find(Data.class);
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BrocastAction.LITEPAL_DATA_CHANGE);
		mContext.registerReceiver(dReceiver, filter);
	}

	private BroadcastReceiver dReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(BrocastAction.LITEPAL_DATA_CHANGE)) {
				myHandler.sendEmptyMessage(0);
			}
		}
	};

	private void unregisterReceiver() {
		mContext.unregisterReceiver(dReceiver);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver();
	}
	
	private void closeTalking(){
		CurReqPage.setTalking(false);
		Intent tabIntent = new Intent(BrocastAction.BT_TAB_CHANGE);
		tabIntent.putExtra("tab", R.id.bt_talk);
		mContext.sendBroadcast(tabIntent);
	}

}
