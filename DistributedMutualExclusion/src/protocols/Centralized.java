package protocols;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import communication.CommMatrix;
import baseclasses.Message;
import baseclasses.Message.Type;

public class Centralized extends Thread{
	
	private JTextArea textArea;	
	private String content="";
	private boolean tocken;
	private CommMatrix commMatrix;
	final int leader = 0;
	private int myId;
	ArrayList<Integer> pendingQ = new ArrayList<Integer>();
	
	public Centralized(int myId, CommMatrix commMatrix, int pos1, int pos2){
		super();
		this.commMatrix = commMatrix;
		this.myId = myId;
		tocken = (myId == leader);
		
        JFrame window = new JFrame();
        window.setTitle(this.getClass().toString());
        window.setSize(400, 200);
        window.setLocation(pos1, pos2);
        JTextArea textArea1 = new JTextArea(400, 200);              
        textArea1.setEditable(false);
        textArea1.setText("Hola");
        JScrollPane scrollPane = new JScrollPane(textArea1); 
        window.setContentPane(scrollPane);
        window.setVisible(true);
        
        this.textArea = textArea1;
        
        content = content + "Inicialitzat \n";
        textArea.setText(content);
	}
	
	public synchronized void requestCS(){
		commMatrix.sendMsg(new Message(myId, 0, Type.request));
	}
	
	public synchronized void releaseCS(){
		commMatrix.sendMsg(new Message(myId, 0, Type.release));
		tocken = false;
	}

	public synchronized void handleMsg(Message msg){
		if (msg.getType().equals(Type.request)){
			if (tocken){
				commMatrix.sendMsg(new Message (myId, msg.getFrom(), Type.ack));
				content = content + "Assigna tocken: "+ msg.getFrom() + "\n";
		        textArea.setText(content);
				tocken=false;
			}else{
				pendingQ.add(msg.getFrom());
				content = content + "Afegit pending: "+ msg.getFrom() + "\n";
		        textArea.setText(content);
			}
		}else if (msg.getType().equals(Type.release)){
			if (!pendingQ.isEmpty()){				
				Integer pid = pendingQ.remove(0);
				content = content + "Dona tocken: " + pid +"\n";
		        textArea.setText(content);
				commMatrix.sendMsg(new Message (myId, pid, Type.ack));
			}else{
				tocken = true;
				content = content + "Assigna tocken: "+ myId + "\n";
		        textArea.setText(content);
			}
		}else if (msg.getType().equals(Type.ack)){
			tocken = true;
		}
	}
	
	private List<Message> receivedMessage(){
		List<Message> messages = new ArrayList<Message>();
		for (int i=0; i<commMatrix.getMatrixLength(); i++){
			if (i!=myId && (!commMatrix.getMatrix()[i][myId].isEmpty())){
				messages.add((Message)commMatrix.getMatrix()[i][myId].pop());
			}
		}
		return messages;		
	}
	
	protected Boolean haveTocken(){
		if ((!this.getCommMatrix().getMatrix()[0][this.getMyId()].isEmpty())){
			handleMsg((Message)this.getCommMatrix().getMatrix()[0][this.getMyId()].pop());
			if (this.isTocken()){
				return Boolean.TRUE;
			}			
		}
		return Boolean.FALSE;

	}
	
	@Override
	public void run(){
		while (true){
			List<Message> messages = receivedMessage();
			if (!messages.isEmpty()){
				for (Message message: messages){
					handleMsg(message);				
				}
			}
		}
	}

	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public boolean isTocken() {
		return tocken;
	}

	public void setTocken(boolean tocken) {
		this.tocken = tocken;
	}

	public CommMatrix getCommMatrix() {
		return commMatrix;
	}

	public void setCommMatrix(CommMatrix commMatrix) {
		this.commMatrix = commMatrix;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

}
