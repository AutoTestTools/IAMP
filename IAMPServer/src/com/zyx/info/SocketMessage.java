package com.zyx.info;

import com.zyx.server.MyServer.SocketThread;

public class SocketMessage {

	public String to;// imei，指发送给谁
//	public String from;// imei，指谁发送过来的
	public String msg;// 消息内容
	public String time;// 接收时间
	public SocketThread thread;// socketThread
	public SocketInfo info;
	    
}
