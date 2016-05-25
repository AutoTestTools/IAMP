package com.zyx.info;

public class BrocastAction {
	
	
	public final static String BT_TAB_CHANGE = "com.zyx.bt.tab_change";
	public final static String BT_CLOSE = "com.zyx.bt.close";
	public final static String START_BT_SERVER = "com.zyx.bt.start_server";
	public final static String START_BT_CLIENT = "com.zyx.bt.start_client";
	public final static String STOP_BT_SERVER = "com.zyx.bt.stop_server";
	public final static String STOP_BT_CLIENT = "com.zyx.bt.stop_client";
	public final static String BT_SEND_MSG = "com.zyx.bt.send_msg";
	public final static String BT_REPLY_MSG = "com.zyx.bt.reply_msg";
	public final static String BT_RECEIVER_OTHER_MSG = "com.zyx.bt.receiver_other_msg";
	
	public final static String INIT_SOCKET = "com.zyx.socket.init_socket";
	public final static String INFORM_STATE = "com.zyx.socket.inform_state";
	public final static String CREATE_ROOM ="com.zyx.socket.create_room";
	public final static String JOIN_ROOM = "com.zyx.socket.join_room";
	public final static String QUIT_ROOM = "com.zyx.socket.quit_room";
	public final static String REQUEST_CALL ="com.zyx.socket.request.call";
	public final static String REQUEST_MESSAGE = "com.zyx.socket.request.message";
	
	public final static String RESPOND_CREATE_ROOM = "com.zyx.socket.respond.create_room";
	public final static String RESPOND_JOIN_ROOM = "com.zyx.socket.respond.join_room";
	public final static String RESPOND_CALL = "com.zyx.socket.respond.call";
	public final static String RESPOND_REVEIVER_CALL = "com.zyx.socket.respond.receiver.call";
	public final static String RESPOND_ANSWER_CALL = "com.zyx.socket.respond.answer.call";
	public final static String RESPOND_END_CALL = "com.zyx.socket.respond.end.call";
	public final static String RESPOND_MESSAGE = "com.zyx.socket.respond.message";
	public final static String RESPOND_RECEIVE_SMS = "com.zyx.socket.respond.receive.sms";
	public final static String RESPOND_RECEIVE_MMS = "com.zyx.socket.respond.receive.mms";
	public final static String RESPOND_NOTHING = "com.zyx.socket.respond.nothing";
	
	public final static String LITEPAL_DATA_CHANGE = "com.zyx.litepal.data.change";

}
