package com.zyx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zyx.info.BrocastAction;
import com.zyx.socket.SocketServer;

public class SocketRequestReceiver extends BroadcastReceiver {

	static SocketServer socket;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();

		socket = new SocketServer(context);

		if (BrocastAction.INIT_SOCKET.equals(action)) {

			socket.initSocket();

		} else if (BrocastAction.INFORM_STATE.equals(action)) {

			socket.informState();

		} else if (BrocastAction.CREATE_ROOM.equals(action)) {

			socket.createRoom();

		} else if (BrocastAction.JOIN_ROOM.equals(action)) {

			socket.joinRoom(intent.getStringExtra("room"));

		} else if (BrocastAction.QUIT_ROOM.equals(action)) {

			socket.quitRoom();

		} else if (BrocastAction.REQUEST_CALL.equals(action)) {

			socket.requestCall();

		} else if (BrocastAction.REQUEST_MESSAGE.equals(action)) {

			socket.requestMessage();

		}

	}

}
