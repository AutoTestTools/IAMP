package com.zyx.info;

public class BluetoothInfo {
	/** 本机蓝牙地址 */
	private static String oneAddress = null;
	/** 通讯者蓝牙地址 */
	private static String theOtherAddress = null;
	/** 本机蓝牙名称 */
	private static String oneName = null;
	/** 通讯者蓝牙名称 */
	private static String theOtherName = null;

	public static String getOneAddress() {
		return oneAddress;
	}

	public static void setOneAddress(String oneAddress) {
		BluetoothInfo.oneAddress = oneAddress;
	}

	public static String getOneName() {
		return oneName;
	}

	public static void setOneName(String oneName) {
		BluetoothInfo.oneName = oneName;
	}

	public static String getTheOtherName() {
		return theOtherName;
	}

	public static void setTheOtherName(String theOtherName) {
		BluetoothInfo.theOtherName = theOtherName;
	}

	public static String getTheOtherAddress() {
		return theOtherAddress;
	}

	public static void setTheOtherAddress(String theOtherAddress) {
		BluetoothInfo.theOtherAddress = theOtherAddress;
	}

}
