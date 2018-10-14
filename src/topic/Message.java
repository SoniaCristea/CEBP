package topic;

public class Message {

	private Object content;
	private Object type;
	private int timeout;
	
	public Message(Object content, Object type, int time) {
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
