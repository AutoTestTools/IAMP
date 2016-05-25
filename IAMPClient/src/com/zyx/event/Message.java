package com.zyx.event;

import com.zyx.info.BrocastAction;
import com.zyx.info.Properties;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

public class Message {

	private Context mContext;

	private boolean iSMms;

	String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

	SmsManager smsManager;

	PendingIntent sentPI;

	PendingIntent deliverPI;

	SmsContent smsContent = null, mmsContent = null;

	public Message(Context context) {

		this.mContext = context;

		smsManager = SmsManager.getDefault();

		// Create the sentIntent parameter
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		sentPI = PendingIntent.getBroadcast(mContext, 0, sentIntent, 0);

		// Create the deliveryIntent parameter
		Intent deliveryIntent = new Intent(DELIVERED_SMS_ACTION);
		deliverPI = PendingIntent.getBroadcast(mContext, 0, deliveryIntent, 0);
	}

	public void sendMessage(String sendTo) {

		String myMessage = "This is a SMS sent automatically !";
		// Send the message
		smsManager.sendTextMessage(sendTo, null, myMessage, sentPI, deliverPI);

	}

	public void registerSMSContentObserver() {
		iSMms = false;
		smsContent = new SmsContent(new Handler());// 这里把两个监听给同一个对象smsContent等于是只要有来信，就会调用smsContent的onchange方法
		mContext.getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsContent);
	}
	
	public void unregisterSMSContentObserver(){
		mContext.getContentResolver().unregisterContentObserver(smsContent);
		smsContent = null;
	}

	public void registerMMSContentObserver() {
		iSMms = true;
		mmsContent = new SmsContent(new Handler());// 这里把两个监听给同一个对象smsContent等于是只要有来信，就会调用smsContent的onchange方法
		mContext.getContentResolver().registerContentObserver(Uri.parse("content://mms"), true, mmsContent);
		mContext.getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, mmsContent);
	}
	
	public void unregisterMMSContentObserver(){
		mContext.getContentResolver().unregisterContentObserver(mmsContent);
		mmsContent = null;
	}

	class SmsContent extends ContentObserver {
		public SmsContent(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			String msg = "";
			if (iSMms) {
				msg = Properties.ALREADY_RECEIVER_MMS + isNewMMS();
				unregisterMMSContentObserver();
			} else {
				msg = Properties.ALREADY_RECEIVER_SMS + isNewSMS();
				unregisterSMSContentObserver();
			}
			Intent newMsg = new Intent(BrocastAction.BT_REPLY_MSG);
			newMsg.putExtra("msg", msg);
			mContext.sendBroadcast(newMsg);
		}
	}

	// 获取是否为指定号码发来短信：
	private String isNewSMS() {
		return isNew(false);
	}

	// 获取是否为指定号码发来彩信：
	private String isNewMMS() {
		return isNew(true);
	}

	private String isNew(boolean isMms) {
//		String mms = "content://mms";
		String sms = "content://sms";
		Uri uri = Uri.parse(sms);
		Cursor c = mContext.getContentResolver().query(uri, null, "read = 0", null, null);
		String phone = null;
		if (c.moveToFirst()) {
			phone = c.getString(c.getColumnIndex("address"));
			Log.e("================", "================" + phone);
		}
		return phone;
	}
}
