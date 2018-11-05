package baseclasses;

public class Message {
	
	public enum Type { request, release, action }
	private Type type;
	private int from;
	private int to;
	private int timestamp;
	private String message;
	
	public Message(){
		
	}
	
	public Message(int from, int to, Type type, String message){
		this.type = type;	
		this.from = from;
		this.to = to;
		this.message = message;
	}

	public Type getType() {
		return type;
	}
	
	public Type getTypeFromString(String type){
		if ("request".equals(type)){
			return Type.request;
		}else if("release".equals(type)){
			return Type.release;
		}else if("action".equals(type)){
			return Type.action;
		}
		return null;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

}
