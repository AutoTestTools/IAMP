package com.meizu.event;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class Telephony {
	
	Context mContext;

	public Telephony(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}
	
	/**����绰*/
	public void makeCall(String number) {
//		���·���ֻ����ת�����Ž��棬�����Զ�����绰
//		TelephonyManager telMag = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//		Class<TelephonyManager> c = TelephonyManager.class;
//		Method mthMakeCall = null;
//		if (number != "" && number != null) {
//			try {
//				mthMakeCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
//				mthMakeCall.setAccessible(true);
//				ITelephony iTel = (ITelephony) mthMakeCall.invoke(telMag, (Object[]) null);
//				iTel.dial(number);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		Intent call = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));
		call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(call);
	}
	
	/**�����绰*/
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

	/**�Ҷϵ绰*/
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

}
