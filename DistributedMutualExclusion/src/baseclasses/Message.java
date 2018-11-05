package baseclasses;

public class Message {

	public enum Type { request, release, ack, tocken, finishedCS }
	private Type type;
	private int from;
	private int to;
	private int timestamp;
	
	public Message(int from, int to, Type type){
		this.type = type;	
		this.from = from;
		this.to = to;
	}

	public Type getType() {
		return type;
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
	
}
