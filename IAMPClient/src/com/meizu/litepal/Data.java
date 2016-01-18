package com.meizu.litepal;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.litepal.crud.DataSupport;

import com.meizu.info.BluetoothInfo;

public class Data extends DataSupport {

	private String from_mac = "";
	private String from_name = "";
	private String msg = "";
	private String to_mac = "";
	private String to_name = "";
	private String time = "";
	private boolean isBt = false;

	public Data() {

	}

	public void setTime(String time) {
		this.time = time;
	}

	public Data(boolean b, String f_mac, String f_n, String m, String t_mac, String t_n, Date date) {
		this.isBt = b;
		this.from_mac = f_mac;
		this.from_name = f_n;
		this.msg = m;
		this.to_mac = t_mac;
		this.to_name = t_n;
		setTime(date);
	}

	/** 默认当前时间，蓝牙通讯，发送消息 */
	public void btSend(String m) {
		this.isBt = true;
		this.from_mac = BluetoothInfo.getOneAddress();
		this.from_name = BluetoothInfo.getOneName();
		this.msg = m;
		this.to_mac = BluetoothInfo.getTheOtherAddress();
		this.to_name = BluetoothInfo.getTheOtherName();
		setTime(new Date());
		save();
	}

	/** 默认当前时间，蓝牙通讯，接收消息 */
	public void btReceive(String m) {
		this.isBt = true;
		this.from_mac = BluetoothInfo.getTheOtherAddress();
		this.from_name = BluetoothInfo.getTheOtherName();
		this.msg = m;
		this.to_mac = BluetoothInfo.getOneAddress();
		this.to_name = BluetoothInfo.getOneName();
		setTime(new Date());
		save();
	}

	public boolean isBt() {
		return isBt;
	}

	public void setBt(boolean isBt) {
		this.isBt = isBt;
	}

	public String getFrom_mac() {
		return from_mac;
	}

	public void setFrom_mac(String from_mac) {
		this.from_mac = from_mac;
	}

	public String getFrom_name() {
		return from_name;
	}

	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTo_mac() {
		return to_mac;
	}

	public void setTo_mac(String to_mac) {
		this.to_mac = to_mac;
	}

	public String getTo_name() {
		return to_name;
	}

	public void setTo_name(String to_name) {
		this.to_name = to_name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		this.time = formatter.format(date);
	}

}
