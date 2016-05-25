package com.zyx.info;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SocketInfo {

	public String IMEI;
	public String phone;
	public String room;
	public boolean isBusy;

	private Context mContext;
	private TelephonyManager TelephonyMgr;
	
	public SocketInfo(Context context){
		this.mContext = context;
		TelephonyMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE); 
		this.IMEI = getIMEI();
		this.phone = getPhoneNum();
		this.room = null;
		this.isBusy = false;
	}

	private String getIMEI() {

		return TelephonyMgr.getDeviceId();
		
	}

	private String getPhoneNum() {
		
		String phone = TelephonyMgr.getLine1Number();
		
		if(phone!=null){
			return phone;
		}
		
		return "";
		
	}

}
