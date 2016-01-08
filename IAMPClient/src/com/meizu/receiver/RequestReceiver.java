package com.meizu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meizu.client.SocketServer;
import com.meizu.info.BrocastAction;

public class RequestReceiver extends BroadcastReceiver {

	static SocketServer socket;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		socket = new SocketServer(context);

		String action = intent.getAction();
		
		if(BrocastAction.INIT_SOCKET.equals(action)){
			
			socket.initSocket();
			
		}else if(BrocastAction.INFORM_STATE.equals(action)){
			
			socket.informState();
			
		}else if(BrocastAction.CREATE_ROOM.equals(action)){
			
			socket.createRoom();
			
		}else if(BrocastAction.JOIN_ROOM.equals(action)){
			
			socket.joinRoom();
			
		}else if(BrocastAction.QUIT_ROOM.equals(action)){
			
			socket.quitRoom();
			
		}else if(BrocastAction.REQUEST_CALL.equals(action)){
			
			socket.requestCall();
						
		}else if(BrocastAction.REQUEST_MESSAGE.equals(action)){
			
			socket.requestMessage();
			
		}
			
	}

}
