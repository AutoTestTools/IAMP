package com.meizu.info;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneNumber {

	private Context mContext;
	private TelephonyManager telephonyMgr;

	/** ±¾»úÄ¬ÈÏSIMºÅÂë */
	private static String oneNumber = "";

	public PhoneNumber(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		try {
			telephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			String phone = telephonyMgr.getLine1Number();
			if (phone != null)
				setOneNumber(phone);
			else
				setOneNumber(" ");
		} catch (Exception e) {
			setOneNumber(" ");
		}
	}

	public static String getOneNumber() {
		return oneNumber;
	}

	private static void setOneNumber(String oneNumber) {
		PhoneNumber.oneNumber = oneNumber;
	}

}
