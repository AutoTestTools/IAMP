package com.meizu.server;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {

	private static List<String> registeredRoom;
	final static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public RoomManager() {
		registeredRoom = new ArrayList<String>();
	}

	public String bookRoom(String imei) {

		String key = generateKey() + imei;

		while (true) {
			if (!checkRoomKey(key)) {
				registeredRoom.add(key);
				break;
			} else {
				key = generateKey() + key;
			}
		}

		return key;
	}

	public void checkOut(String key) {
		try {
			registeredRoom.remove(key);
		} catch (Exception e) {
			System.out.println("房间" + key + "不存在");
		}
	}

	/**检查房间是否已存在*/
	public boolean checkRoomKey(String key) {
		for (String room : registeredRoom) {
			if (key.equals(room)) {
				return true;
			}
		}
		return false;
	}
	
	public List<String> getAllRoom(){
		return registeredRoom;
	}
	
	private static char generateKey() {
		return chars.charAt((int) (Math.random() * 26));
	}

}
