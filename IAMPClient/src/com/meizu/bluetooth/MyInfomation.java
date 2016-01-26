package com.meizu.bluetooth;

import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meizu.iamp.client.R;
import com.meizu.info.BluetoothInfo;
import com.meizu.info.PhoneNumber;
import com.meizu.info.Properties;

public class MyInfomation extends Fragment implements OnClickListener {

	private Context mContext;
	private TextView tv_title, tv_name, tv_mac, tv_phone;
	private ImageButton changeBtn, reNameBtn, rePhoneBtn;
	BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
	int ME = 0;
	int PAIR = 1;
	int CURRENT = ME;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.bt_myinfo, null);

		initView(view);

		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		tv_title = (TextView) view.findViewById(R.id.info_title);
		tv_name = (TextView) view.findViewById(R.id.info_name);
		tv_mac = (TextView) view.findViewById(R.id.info_mac);
		tv_phone = (TextView) view.findViewById(R.id.info_phone);
		changeBtn = (ImageButton) view.findViewById(R.id.change_title);
		reNameBtn = (ImageButton) view.findViewById(R.id.re_name);
		rePhoneBtn = (ImageButton) view.findViewById(R.id.re_phone);

		changeBtn.setOnClickListener(this);
		reNameBtn.setOnClickListener(this);
		rePhoneBtn.setOnClickListener(this);
		
		setItmeText();

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
		case R.id.change_title:
			if (CURRENT == ME) {
				CURRENT = PAIR;
			} else {
				CURRENT = ME;
			}
			setItmeText();
			break;
		case R.id.re_name:
			editName("重命名");
			break;
		case R.id.re_phone:
			editName("修改号码");
			break;

		default:
			break;
		}
	}

	/** 设置item颜色及文本 */
	protected void setItmeText() {
		// TODO Auto-generated method stub
		if (CURRENT == ME) {
			reNameBtn.setVisibility(View.VISIBLE);
			rePhoneBtn.setVisibility(View.VISIBLE);
			tv_title.setTextColor(Properties.BLUE);
			tv_title.setText("本机");
			BluetoothInfo.setOneAddress(mBTAdapter.getAddress());
			BluetoothInfo.setOneName(mBTAdapter.getName());
			if (PhoneNumber.getOneNumber().equals("null")) {
				new PhoneNumber(mContext);
			}
			tv_name.setText(BluetoothInfo.getOneName());
			tv_mac.setText(BluetoothInfo.getOneAddress());
			tv_phone.setText(PhoneNumber.getOneNumber());
		} else {
			reNameBtn.setVisibility(View.GONE);
			rePhoneBtn.setVisibility(View.GONE);
			tv_title.setTextColor(Properties.GREEN);
			tv_title.setText("对方");
			if (TextUtils.isEmpty(BluetoothInfo.getTheOtherName())) {
				BluetoothInfo.setTheOtherAddress("null");
				BluetoothInfo.setTheOtherName("null");
			}
			tv_name.setText(BluetoothInfo.getTheOtherName());
			tv_mac.setText(BluetoothInfo.getTheOtherAddress());
			tv_phone.setText(PhoneNumber.getTheOtherNumber());
		}
	}

	protected void editName(final String type) {
		AlertDialog.Builder renameDialog = new AlertDialog.Builder(mContext);
		renameDialog.setTitle(type);
		final EditText edit = new EditText(mContext);
		renameDialog.setView(edit);
		renameDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newText = edit.getText().toString();
				String toast = type + "成功";
				if (type.equals("重命名")) {
					tv_name.setText(newText);
					mBTAdapter.setName(newText);
					BluetoothInfo.setOneName(newText);
				} else if (type.equals("修改号码")) {
					tv_phone.setText(newText);
					PhoneNumber.setOneNumber(newText);
					toast += ",只是修改通讯过程中发送给对方的号码，实际上并未真正修改SIM卡号码";
				}
				Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
			}
		});
		renameDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(mContext, "取消" + type, Toast.LENGTH_SHORT).show();
			}
		});
		renameDialog.setCancelable(true); // 设置按钮是否可以按返回键取消,false则不可以取消
		AlertDialog dialog = renameDialog.create(); // 创建对话框
		dialog.setCanceledOnTouchOutside(true); // 设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
		dialog.show();
	}

}
