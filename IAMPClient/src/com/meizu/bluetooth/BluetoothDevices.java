package com.meizu.bluetooth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import android.graphics.Color;
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
import android.widget.ProgressBar;
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
	private ProgressBar bar;

	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private List<ScanBluetoothInfo> bList = new ArrayList<ScanBluetoothInfo>();
	static private List<ScanBluetoothInfo> tList = new ArrayList<ScanBluetoothInfo>();

	private MyAdapter adapter;

	private BluetoothDevice pair;
	private int pairIndex;

	/* 取得默认的蓝牙适配器 */
	private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = getActivity();

		BluetoothInfo.setOneAddress(mBtAdapter.getAddress());
		BluetoothInfo.setOneName(mBtAdapter.getName());
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

		if (tList.size() == 0) {
			mBtAdapter.startDiscovery();
			mHandler.sendEmptyMessage(5);
		} else {
			mHandler.sendEmptyMessage(0);
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
		try {
			mContext.unregisterReceiver(mReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
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
					mHandler.sendEmptyMessage(0);
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				if (tList.size() == 0) {
					Toast.makeText(mContext, "没有发现蓝牙设备", Toast.LENGTH_SHORT).show();
				}
				mHandler.sendEmptyMessage(4);
			}
		}
	};

	private List<Map<String, Object>> getData(String filter) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		for (ScanBluetoothInfo b : tList) {
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
		bar = (ProgressBar) view.findViewById(R.id.scaning);

		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		mContext.registerReceiver(mReceiver, filter);

		adapter = new MyAdapter(mContext);

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mBtAdapter.isDiscovering()) {
					mBtAdapter.cancelDiscovery();
					mHandler.sendEmptyMessage(4);
				} else {
					bList.clear();
					mBtAdapter.cancelDiscovery();
					mBtAdapter.startDiscovery();
					mHandler.sendEmptyMessage(5);
					
				}
			}
		});

		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View convertView, int position, long arg3) {
				// TODO Auto-generated method stub
				BluetoothInfo.setTheOtherAddress((String) mData.get(position).get("address"));
				BluetoothInfo.setTheOtherName((String) mData.get(position).get("name"));
				if (!bList.get(position).isPaired) {
					pair = mBtAdapter.getRemoteDevice(BluetoothInfo.getTheOtherAddress());
					pairIndex = position;
					pairAnotherDevice(pair);// 请求配对设备
					mHandler.sendEmptyMessage(1);// 显示progressBar
					pairThread.start();// 检测配对结果
				} else {
					mHandler.sendEmptyMessage(2);
				}
			}
		});

		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				mData = getData(s.toString());
				mHandler.sendEmptyMessage(0);
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

	public void pairAnotherDevice(BluetoothDevice btDev) {
		Method createBondMethod;
		try {
			createBondMethod = BluetoothDevice.class.getMethod("createBond");
			createBondMethod.invoke(btDev);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			if ((boolean) mData.get(position).get("isPaired")) {
				holder.name.setTextColor(Properties.BLUE);
				holder.address.setTextColor(Properties.BLUE);
			} else {
				holder.name.setTextColor(Color.BLACK);
				holder.address.setTextColor(Color.BLACK);
			}
			return convertView;
		}

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				if (bList.size() != 0) {
					tList = bList;
				}
				mData = getData(mClearEditText.getText().toString());
				mList.setAdapter(adapter);
				mList.setClickable(true);
				break;

			case 1:
				bar.setVisibility(View.VISIBLE);
				Toast.makeText(mContext, "需要对方确认配对", Toast.LENGTH_SHORT).show();
				mList.setClickable(false);
				break;
			case 2:// 配对成功
				bar.setVisibility(View.GONE);
				Intent client = new Intent(BrocastAction.START_BT_CLIENT);
				mContext.sendBroadcast(client);
				break;
			case 3:// 超时，配对不成功
				mList.setClickable(true);
				bar.setVisibility(View.GONE);
				Toast.makeText(mContext, "配对不成功，请重新配对", Toast.LENGTH_SHORT).show();
				mData = getData(mClearEditText.getText().toString());
				mList.setAdapter(adapter);
				break;
			case 4:// 修改为刷新图标
				bt.setImageDrawable(mContext.getDrawable(R.drawable.refresh));
				break;
			case 5:// 修改为暂停图标
				bt.setImageDrawable(mContext.getDrawable(R.drawable.stop));
				Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();// 获取已配对设备
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						String name = device.getName();
						String address = device.getAddress();
						bList.add(new ScanBluetoothInfo(name == null ? "Unkwon Device" : name, address == null ? "Unkwon Address" : address, true));
						mHandler.sendEmptyMessage(0);
					}
				} else {
					Toast.makeText(mContext, "没有已配对设备", Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		};
	};

	Thread pairThread = new Thread(new Runnable() {// 等待配对

				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean flag = false;
					long curTime = System.currentTimeMillis();
					while (System.currentTimeMillis() - curTime <= 60 * 1000) {
						if (pair.getBondState() == BluetoothDevice.BOND_BONDED) {
							bList.set(pairIndex, new ScanBluetoothInfo(pair.getName(), pair.getAddress(), true));
							mHandler.sendEmptyMessage(2);// 配对成功
							flag = true;
							break;
						} else {
							try {
								Thread.sleep(200);
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					if (!flag)
						mHandler.sendEmptyMessage(3);// 超时，配对不成功
				}
			});

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
