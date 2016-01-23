package com.meizu.event;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.meizu.info.BrocastAction;
import com.meizu.info.Properties;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Telephony {

	Context mContext;

	public Telephony(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	/** 拨打电话 */
	public void makeCall(String number) {
		// 以下方法只会跳转到拨号界面，不会自动拨打电话
		// TelephonyManager telMag = (TelephonyManager)
		// mContext.getSystemService(Context.TELEPHONY_SERVICE);
		// Class<TelephonyManager> c = TelephonyManager.class;
		// Method mthMakeCall = null;
		// if (number != "" && number != null) {
		// try {
		// mthMakeCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
		// mthMakeCall.setAccessible(true);
		// ITelephony iTel = (ITelephony) mthMakeCall.invoke(telMag, (Object[])
		// null);
		// iTel.dial(number);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
		call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(call);
	}

	/** 接听电话 */
	public void answerCall() {
		TelephonyManager telMag = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		Class<TelephonyManager> c = TelephonyManager.class;
		Method mthEndCall = null;
		try {
			mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
			mthEndCall.setAccessible(true);
			ITelephony iTel = (ITelephony) mthEndCall.invoke(telMag, (Object[]) null);
			iTel.answerRingingCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 挂断电话 */
	public void endCall() {
		TelephonyManager telMag = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		Class<TelephonyManager> c = TelephonyManager.class;
		Method mthEndCall = null;
		try {
			mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
			mthEndCall.setAccessible(true);
			ITelephony iTel = (ITelephony) mthEndCall.invoke(telMag, (Object[]) null);
			iTel.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 监听来电 */
	public void registerCallReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		mContext.getApplicationContext().registerReceiver(phoneCallReceiver, filter);
	}

	BroadcastReceiver phoneCallReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 如果是拨打电话
			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				Log.e("PhoneStatReceiver", "正在拨打电话：" + phoneNumber);
			} else {
				// 如果是来电
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

				if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
					String incoming_number = intent.getStringExtra("incoming_number");

					Intent incoming = new Intent(BrocastAction.BT_REPLY_MSG);
					incoming.putExtra("msg", Properties.ALREADY_RECEIVER_CALL + (incoming_number == null ? "" : incoming_number));
					mContext.sendBroadcast(incoming);

					unregisterCallReceiver();
				}
			}
		}

	};

	/** 解除监听来电 */
	public void unregisterCallReceiver() {
		mContext.getApplicationContext().unregisterReceiver(phoneCallReceiver);
	}

}
