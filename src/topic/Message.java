package topic;

import java.awt.TrayIcon.MessageType;
import java.io.Serializable;

public class Message implements Serializable{

	private Object content;
	private MessageTypes type;
	private int timeout;
	
	public Object getContent() {
		return content;
	}


	public void setContent(Object content) {
		this.content = content;
	}

	
	
	public Message(Object content, MessageTypes type, int time) {
		super();
		this.content = content;
		this.type = type;
		this.timeout = time;
	}
	
	
	public Object getMessageType(){
		return this.type;
	}
	
	public int getTimeout(){
		return this.timeout;
	}
	
	
}
