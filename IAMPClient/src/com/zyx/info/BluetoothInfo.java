package com.zyx.info;

public class BluetoothInfo {
	/** ����������ַ */
	private static String oneAddress = null;
	/** ͨѶ��������ַ */
	private static String theOtherAddress = null;
	/** ������������ */
	private static String oneName = null;
	/** ͨѶ���������� */
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
