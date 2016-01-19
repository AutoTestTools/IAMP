package com.meizu.bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meizu.iamp.client.R;
import com.meizu.info.BluetoothInfo;

public class MyInfomation extends Fragment implements OnClickListener{

	private Context mContext;
	private ListView mList;
	private MyAdapter adapter;
	BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter(); 
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		BluetoothInfo.setOneAddress(mBTAdapter.getAddress());
		BluetoothInfo.setOneName(mBTAdapter.getName());
		
		mData = getData();
		adapter = new MyAdapter(mContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bt_myinfo, null);
		mList = (ListView) view.findViewById(R.id.lv_myinfo_list);
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
		mContext.registerReceiver(mReceiver, filter);
		
		return view;
	}

	
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mData = getData();
			mList.setAdapter(adapter);
		}
	};
	
	public List<Map<String, Object>> getData(){
		String bondedBTAdress = null;
		String bondedBTName = null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//		Set<BluetoothDevice> bondedBT = mBTAdapter.getBondedDevices();
//		if (bondedBT.size() > 0) {
//			for (BluetoothDevice bd : bondedBT) {
//				bondedBTAdress = bd.getAddress();
//				bondedBTName = bd.getName();
//				break;//获取的第一个值就是当前我已经连接的设备
//			}
//		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("image", R.drawable.ic_launcher);
		map.put("title", "设备名称");
		map.put("data", mBTAdapter.getName());
		list.add(map);
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("image", R.drawable.ic_launcher);
		map1.put("title", "设备地址");
		map1.put("data", mBTAdapter.getAddress());
		list.add(map1);
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("image", R.drawable.ic_launcher);
		map2.put("title", "已连接设备名称");
		if (!TextUtils.isEmpty(bondedBTName)) {
			map2.put("data",bondedBTName);
		}else{
			map2.put("data","未连接设备");
		}
		list.add(map2);
		
		
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("image", R.drawable.ic_launcher);
		map3.put("title", "已连接设备地址");
		if (!TextUtils.isEmpty(bondedBTAdress)) {
			map3.put("data",bondedBTAdress);
		}else{
			map3.put("data","未连接设备");
		}
		list.add(map3);
		
		
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("image", R.drawable.ic_launcher);
		map4.put("title", "名称");
		map4.put("data", mBTAdapter.getAddress());
		list.add(map4);
		
		
		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("image", R.drawable.ic_launcher);
		map5.put("title", "名称");
		map5.put("data", mBTAdapter.getAddress());
		list.add(map5);
		return list;
	}
	
	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			int[] resID = new int[]{R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.bt_myinfo_item, null);
				holder.image = (ImageView) convertView.findViewById(R.id.iv_myinfo_image);
				holder.title = (TextView) convertView.findViewById(R.id.tv_myinfo_title);
				holder.data = (TextView) convertView.findViewById(R.id.tv_myinfo_data);
				holder.imageButton = (ImageButton) convertView.findViewById(R.id.ib_myinfo_rename);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String title = mData.get(position).get("title").toString();
			if(title.equals("设备名称")){
				holder.imageButton.setVisibility(View.VISIBLE);
				holder.imageButton.setOnClickListener(MyInfomation.this);
			}
//			holder.image.setBackgroundResource(resID[position]);
			holder.title.setText(title);
			holder.data.setText(mData.get(position).get("data").toString());
			return convertView;
		}

	}
	
	public final class ViewHolder {
		public ImageView image;
		public TextView title;
		public TextView data;
		public ImageButton imageButton;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_myinfo_rename:
	        AlertDialog.Builder renameDialog = new AlertDialog.Builder(mContext);  
	        renameDialog.setTitle("修改名称");
	        final EditText edit = new EditText(mContext);
	        renameDialog.setView(edit);  
	        renameDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	            	String newName = edit.getText().toString();
	            	BluetoothInfo.setOneName(newName);
	            	mBTAdapter.setName(newName);
	                Toast.makeText(mContext, "重命名成功", Toast.LENGTH_SHORT).show();
	            }  
	        });  
	        renameDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	        	@Override
	        	public void onClick(DialogInterface dialog, int which) {
	        		Toast.makeText(mContext, "取消重命名", Toast.LENGTH_SHORT).show();
	        	}
	    	});  
	        renameDialog.setCancelable(true);	//设置按钮是否可以按返回键取消,false则不可以取消
	        AlertDialog dialog = renameDialog.create();	//创建对话框
	        dialog.setCanceledOnTouchOutside(true);	//设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
	        dialog.show();
		
			break;

		default:
			break;
		}
	}



	
}
