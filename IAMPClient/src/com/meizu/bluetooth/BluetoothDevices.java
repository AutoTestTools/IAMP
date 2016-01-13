package com.meizu.bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meizu.client.ClearEditText;
import com.meizu.iamp.client.R;
import com.meizu.info.BluetoothInfo;
import com.meizu.info.BrocastAction;
import com.meizu.info.Properties;

public class BluetoothDevices extends Fragment {

	private Context mContext;
	private ListView mList;
	private ClearEditText mClearEditText;
	private ImageButton bt;

	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private List<ScanBluetoothInfo> bList = new ArrayList<ScanBluetoothInfo>();

	private MyAdapter adapter;

	/* 取得默认的蓝牙适配器 */
	private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = getActivity();
		
		BluetoothInfo.setOneAddress(mBtAdapter.getAddress());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.bt_list, null);

		initView(view);

		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// If BT is not on, request that it be enabled.
		if (!mBtAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, 3);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
		// Unregister broadcast listeners
		mContext.unregisterReceiver(mReceiver);
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					String name = device.getName();
					String address = device.getAddress();
					bList.add(new ScanBluetoothInfo(name == null ? "Unkwon Device" : name, address == null ? "Unkwon Address" : address, false));
					myHandler.sendEmptyMessage(0);
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				if (bList.size() == 0) {
					Toast.makeText(mContext, "没有发现蓝牙设备", Toast.LENGTH_SHORT).show();
				}
				bt.setImageDrawable(mContext.getDrawable(R.drawable.refresh));
			}
		}
	};

	private List<Map<String, Object>> getData(String filter) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		for (ScanBluetoothInfo b : bList) {
			String name = b.name;
			String address = b.address;
			if (!filter.equals("") && !name.contains(filter) && !address.contains(filter))
				continue;
			map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("address", address);
			map.put("isPaired", b.isPaired);
			list.add(map);
		}

		return list;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mList = (ListView) view.findViewById(R.id.bt_devices_list);
		mClearEditText = (ClearEditText) view.findViewById(R.id.devices_filter_edit);
		bt = (ImageButton) view.findViewById(R.id.imagebt);

		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		mContext.registerReceiver(mReceiver, filter);

		adapter = new MyAdapter(mContext);

		myHandler.sendEmptyMessage(0);

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mBtAdapter.isDiscovering()) {

					mBtAdapter.cancelDiscovery();
					bt.setImageDrawable(mContext.getDrawable(R.drawable.stop));

				} else {

					mBtAdapter.startDiscovery();
					bt.setImageDrawable(mContext.getDrawable(R.drawable.refresh));

					bList.clear();

					Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
					if (pairedDevices.size() > 0) {
						for (BluetoothDevice device : pairedDevices) {
							String name = device.getName();
							String address = device.getAddress();
							bList.add(new ScanBluetoothInfo(name == null ? "Unkwon Device" : name, address == null ? "Unkwon Address" : address, true));
							myHandler.sendEmptyMessage(0);
						}
					} else {
						Toast.makeText(mContext, "没有已配对设备", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View convertView, int position, long arg3) {
				// TODO Auto-generated method stub
				BluetoothInfo.setTheOtherAddress(bList.get(position).address);
				Toast.makeText(mContext, bList.get(position).address, Toast.LENGTH_SHORT).show();
				Intent client = new Intent(BrocastAction.START_BT_CLIENT);
				mContext.sendBroadcast(client);
			}
		});

		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				mData = getData(s.toString());
				myHandler.sendEmptyMessage(0);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public final class ViewHolder {
		public TextView name;
		public TextView address;
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

				convertView = mInflater.inflate(R.layout.bt_list_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.address = (TextView) convertView.findViewById(R.id.tv_address);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String text = mClearEditText.getText().toString();
			holder.name.setText((highlight((String) mData.get(position).get("name"), text)));
			holder.address.setText((highlight((String) mData.get(position).get("address"), text)));
			if((boolean) mData.get(position).get("isPaired")){
				holder.name.setTextColor(Properties.PURPLE);
				holder.address.setTextColor(Properties.PURPLE);
			}
			return convertView;
		}

	}

	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			mData = getData(mClearEditText.getText().toString());

			mList.setAdapter(adapter);

			super.handleMessage(msg);
		};
	};

	/** 高亮关键字 */
	public SpannableStringBuilder highlight(String text, String target) {
		SpannableStringBuilder spannable = new SpannableStringBuilder(text);
		CharacterStyle span = null;

		if (!target.equals("")) {
			Pattern p = Pattern.compile(target);
			Matcher m = p.matcher(text);
			while (m.find()) {
				span = new ForegroundColorSpan(Properties.GREEN);// 需要重复！
				spannable.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}

	public final class ScanBluetoothInfo {

		String name;
		String address;
		boolean isPaired;

		public ScanBluetoothInfo(String n, String a, boolean p) {
			this.name = n;
			this.address = a;
			this.isPaired = p;
		}

	}

}
