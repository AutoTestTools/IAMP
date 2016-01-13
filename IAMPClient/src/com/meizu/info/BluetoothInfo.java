package com.meizu.info;

public class BluetoothInfo {
	/** 本机蓝牙地址 */
	private static String oneAddress = null;
	/** 通讯者蓝牙地址 */
	private static String theOtherAddress = null;

	public static String getOneAddress() {
		return oneAddress;
	}

	public static void setOneAddress(String oneAddress) {
		BluetoothInfo.oneAddress = oneAddress;
	}

	public static String getTheOtherAddress() {
		return theOtherAddress;
	}

	public static void setTheOtherAddress(String theOtherAddress) {
		BluetoothInfo.theOtherAddress = theOtherAddress;
	}

}
