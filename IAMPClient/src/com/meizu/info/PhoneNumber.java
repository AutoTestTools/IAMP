package com.meizu.info;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneNumber {

	private Context mContext;
	private TelephonyManager telephonyMgr;

	/** ����Ĭ��SIM���� */
	private static String oneNumber = "null";
	/** ���һ�η��Ͷ��Ż򲦴�绰�ĶԷ�SIM���� */
	private static String theOtherNumber = "null";

	public static String getTheOtherNumber() {
		return theOtherNumber;
	}

	public static void setTheOtherNumber(String theOtherNumber) {
		PhoneNumber.theOtherNumber = theOtherNumber;
	}

	public PhoneNumber(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		try {
			telephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			String phone = telephonyMgr.getLine1Number();
			
			if (phone != null)
				setOneNumber(phone);
			else
				setOneNumber("null");
		} catch (Exception e) {
			setOneNumber("null");
		}
	}

	public static String getOneNumber() {
		return oneNumber;
	}
	
	public static void setOneNumber(String oneNumber) {
		PhoneNumber.oneNumber = oneNumber;
	}

}
