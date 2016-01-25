package com.meizu.info;

import android.graphics.Color;


public class Properties {

	public static String IP = "172.16.11.36";
	public static int PORT = 2323;
	
	public static String BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	
	public static String WIFI_MODEL = "WIFI_MODEL";
	public static String BLUETOOTH_MODEL = "BLUETOOTH_MODEL";
	
	public static String WHAT_MARK = "#WHAT_MARK#";
	public static String PHONE_MARK = "#PHONENUMBER_MARK#";
	
	/**�����ı�*/
	public static String CALL_ME = "���Ҵ�绰";
	public static String TALK_ME_WHEN_RECEIVER_CALL = "�յ��绰������";
	public static String END_THEN_TALK_WHEN_RECEIVER_CALL = "�Ҷϵ绰�������";
	public static String ANSEWER_THEN_TALK_WHEN_RECEIVER_CALL = "�����绰�������";
	public static String MESSAGE_ME = "���ҷ�����";
	public static String TALK_ME_WHEN_RECEIVER_SMS = "�յ����Ÿ�����";
	public static String TALK_ME_WHEN_RECEIVER_MMS = "�յ����Ÿ�����";
	
	/**�Զ��ظ��ı�*/
	public static String CALL_ALREADY = "�õģ����Ѳ���";
	public static String MESSAGE_ALREADY = "�õģ����ѷ���";
	public static String ALREADY_RECEIVER_CALL = "���յ�������,����:";
	public static String ALREADY_ANSWER_CALL = "�ҽ����˵绰";
	public static String ALREADY_END_CALL = "�ҹҶ��˵绰";
	public static String ALREADY_RECEIVER_SMS = "���յ�������,����:";
	public static String ALREADY_RECEIVER_MMS = "���յ�������,����:";
	
	public final static String INFORM_NUM = "INFORM_NUM";
	public final static String INFORM_STATE = "INFORM_STATE";
	public final static String CREATE_ROOM = "CREATE_ROOM";
	public final static String JOIN_ROOM = "JOIN_ROOM";
	public final static String QUIT_ROOM = "QUIT_ROOM";
	public final static String REQUEST = "REQUEST";
	public final static int SOCKET_OUTTIME = -1;
	
	public final static int BT_CONNECTINT = -1;
	public final static int BT_CONNECTED = 0;
	public final static int BT_DISCONNECT = 1;
	public final static int BT_INFORM_MAC_AND_NAME = 2;
	public final static int BT_REQUEST = 3;
	public final static int BT_RESPOND = 4;
	public final static int BT_REPLY = 5;
	public final static int BT_OPEN_TALKING_TAB = 6;
	public final static int BT_CONNECT_ERROR = 7;
	
	public final static int GREEN = Color.rgb(60, 179, 113);
	public final static int PURPLE = Color.rgb(171, 130, 255);
	public final static int BLUE = Color.rgb(99, 180, 255);
	
}
