package com.zyx.bluetooth;

public class CurReqPage {

	private static boolean isTalking = false;
	
	private static String talker_mac;
	
	private static String talker_name;

	public static boolean isTalking() {
		return isTalking;
	}

	public static void setTalking(boolean isTalking) {
		CurReqPage.isTalking = isTalking;
	}

	public static String getTalker_mac() {
		return talker_mac;
	}

	public static void setTalker_mac(String talker_mac) {
		CurReqPage.talker_mac = talker_mac;
	}

	public static String getTalker_name() {
		return talker_name;
	}

	public static void setTalker_name(String talker_name) {
		CurReqPage.talker_name = talker_name;
	}

}
