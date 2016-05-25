package com.zyx.event;

public class Room {

	private static String room_key = null;
	private static int room_num = 0;

	public static String getRoom_key() {
		return room_key;
	}

	public static void setRoom_key(String room_key) {
		Room.room_key = room_key;
	}

	public static int getRoom_num() {
		return room_num;
	}

	public static void setRoom_num(int room_num) {
		Room.room_num = room_num;
	}

}
