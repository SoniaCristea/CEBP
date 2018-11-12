package topic;

import java.io.Serializable;

public class Topic implements Serializable{
	private String content;
	private String header;
	private int timeout;
	//queue
	//listaclienti
	
	

	public Topic(String header, String content,int time) {
		this.content = content;
		this.header = header;
		this.timeout = time;
		
		
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	
}
