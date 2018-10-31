package topic;

public enum MessageTypes {
	
	STRING(String.class),INT(Integer.class),DOUBLE(Double.class),CHAR(Character.class);
	
	private Class classType;

	private MessageTypes(Class classType) {
		this.classType = classType;
	}
	
	

}
