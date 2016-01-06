package com.meizu.info;

public class SocketInfo {
	
	public int id;
	public String imei;
	public String phone;
	public String room;
	public boolean isBusy;
	
	public SocketInfo(int id){
		this.id = id;
		this.imei = null;
		this.phone = null;
		this.room = null;
		this.isBusy = false;
	}

}
