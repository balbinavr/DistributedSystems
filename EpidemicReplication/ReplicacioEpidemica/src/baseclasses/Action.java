package baseclasses;

public class Action {
	
	private String action;
	private Runnable thread;
	
	public Action(String action, Runnable thread){
		this.action = action;
		this.thread = thread;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Runnable getThread() {
		return thread;
	}
	public void setThread(Runnable thread) {
		this.thread = thread;
	}
	
	

}
