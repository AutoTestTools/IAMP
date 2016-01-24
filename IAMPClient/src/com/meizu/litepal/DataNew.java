package com.meizu.litepal;

import org.litepal.crud.DataSupport;

public class DataNew extends DataSupport {

	public String getFrom_mac() {
		return from_mac;
	}

	public void setFrom_mac(String from_mac) {
		this.from_mac = from_mac;
	}

	public String getFrom_name() {
		return from_name;
	}

	public void setFrom_name(String from_name) {
		this.from_name = from_name;
	}

	public String getMsgRequest() {
		return msgRequest;
	}

	public void setMsgRequest(String msgRequest) {
		this.msgRequest = msgRequest;
	}

	public String getMsgResponse() {
		return msgResponse;
	}

	public void setMsgResponse(String msgResponse) {
		this.msgResponse = msgResponse;
	}

	public String getTo_mac() {
		return to_mac;
	}

	public void setTo_mac(String to_mac) {
		this.to_mac = to_mac;
	}

	public String getTo_name() {
		return to_name;
	}

	public void setTo_name(String to_name) {
		this.to_name = to_name;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public boolean isBt() {
		return isBt;
	}

	public void setBt(boolean isBt) {
		this.isBt = isBt;
	}

	private String from_mac = "";
	private String from_name = "";
	private String msgRequest = "";
	private String msgResponse = "";
	private String to_mac = "";
	private String to_name = "";
	private String timeFrom = "";
	private String timeTo = "";
	private boolean isBt = false;

	public DataNew() {
		// TODO Auto-generated constructor stub
	}

}
