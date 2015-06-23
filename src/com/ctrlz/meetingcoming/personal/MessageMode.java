package com.ctrlz.meetingcoming.personal;

public class MessageMode {
	
	//消息发送者
	private String sender;
	//消息内容
	private String content;
	//消息时间
	private String time;
	//消息标题
	private String title;
	//类型 0广播 1个人
	private String type;
	//消息ID
	private String messageId;
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
