package com.meizu.info;

public class BluetoothInfo {
	/** ����������ַ */
	private static String oneAddress = null;
	/** ͨѶ��������ַ */
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
