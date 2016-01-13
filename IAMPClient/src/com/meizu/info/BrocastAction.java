package com.meizu.info;

public class BrocastAction {
	
	
	public final static String START_BT_SERVER = "com.meizu.bt.start_server";
	public final static String START_BT_CLIENT = "com.meizu.bt.start_client";
	
	public final static String INIT_SOCKET = "com.meizu.socket.init_socket";
	public final static String INFORM_STATE = "com.meizu.socket.inform_state";
	public final static String CREATE_ROOM ="com.meizu.socket.create_room";
	public final static String JOIN_ROOM = "com.meizu.socket.join_room";
	public final static String QUIT_ROOM = "com.meizu.socket.quit_room";
	public final static String REQUEST_CALL ="com.meizu.socket.request.call";
	public final static String REQUEST_MESSAGE = "com.meizu.socket.request.message";
	
	public final static String RESPOND_CREATE_ROOM = "com.meizu.socket.respond.create_room";
	public final static String RESPOND_JOIN_ROOM = "com.meizu.socket.respond.join_room";
	public final static String RESPOND_CALL = "com.meizu.socket.respond.call";
	public final static String RESPOND_MESSAGE = "com.meizu.socket.respond.message";

}
