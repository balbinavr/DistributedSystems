package protocols;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Layers.CoreLayer;
import Layers.Layer;
import baseclasses.Action;
import baseclasses.LamportClock;
import baseclasses.LamportMessage;
import baseclasses.Message;
import baseclasses.Message.Type;
import communication.CommSockets;

public class RicartAndAgrawala extends Thread{
	private JTextArea textArea, textArea2;	
	private String content="", content2="";
	private int myts, numProc, myId;
	private LamportClock c;
	LinkedList<Integer> pendingQ = new LinkedList<Integer>();
	private Integer numOkay = 0;
	private CommSockets commSockets;
	private boolean tocken = false;
	private Action action;
	private Layer layer;
	
	public RicartAndAgrawala(int myId, int numProc, CommSockets commSockets, int pos1, int pos2, Layer layer){
		this.myId = myId;
		this.numProc = numProc;
		this.commSockets = commSockets;
		this.c = new LamportClock();
		this.pendingQ = new LinkedList<Integer>();
		this.myts = Integer.MAX_VALUE;
		this.numOkay = 0;
		this.layer = layer;
		
		JFrame window = new JFrame();
        window.setTitle(this.getClass().toString());
        window.setSize(200, 200);
        window.setLocation(pos1, pos2);
        
        JFrame window1 = new JFrame();
        window1.setTitle(this.getClass().toString());
        window1.setSize(200, 200);
        window1.setLocation(pos1+202, pos2);
        
        JTextArea textArea1 = new JTextArea(200, 300);
        textArea1.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea1);
        
        JTextArea textArea3 = new JTextArea(200, 300); 
        textArea3.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(textArea3); 
        
        window.setContentPane(scrollPane);
        window.setVisible(true);
        this.textArea = textArea1;
        window1.setContentPane(scrollPane1);
        window1.setVisible(true);
        this.textArea2 = textArea3;
        
        content = content + "Inicialitzat " + myId + "\n";
        textArea.setText(content);
        
	}
	
	public synchronized void requestCS(){
		c.tick();
		myts = c.getValue();
		for (int j=1; j<this.numProc; j++){
			if (j != this.myId){
				content = content + "Request enviat a: " + j + "de " + myId + "\n";
		        textArea.setText(content);
				try {
					commSockets.sendMsg(new LamportMessage(myId, j, Type.request, myts));
				} catch (IOException e) {
					System.out.println("Error sending message");
				}
			}
		}
	}
	
	public synchronized void releaseCS(){
		myts = Integer.MAX_VALUE;
		try {
			while (!pendingQ.isEmpty()){
				int pid = pendingQ.removeFirst();
				content = content + "Release enviat a: " + pid + "de " + myId + "\n";
		        textArea.setText(content);
				commSockets.sendMsg(new LamportMessage(myId, pid, Type.release, c.getValue()));
	
			}

		} catch (IOException e) {
			System.out.println("Error sending message");
		}
	}
	
	public synchronized void handleMsg(Message m){
		try {
			int timeStamp = ((LamportMessage)m).getLogicalClock();
			this.c.receiveAction(m.getFrom(), timeStamp);
			if (m.getType().equals(Type.request)){
				content2 = content2 + myId + " " + myts + " " +timeStamp + "\n";
				textArea2.setText(content2);
				if ((myts == Integer.MAX_VALUE) || (timeStamp < myts) || (timeStamp == myts) && (m.getFrom() < myId)){
					content2 = content2 + "Es compleix!! Release enviat de: " + myId + "a " + m.getFrom() + "\n";
					textArea2.setText(content2);
					commSockets.sendMsg(new LamportMessage(myId, m.getFrom(), Type.release, c.getValue()));
				}else{
					content2 = content2 + m.getFrom() +" Afegit al pending"+ "\n";
					textArea2.setText(content2);
					pendingQ.add(m.getFrom());
				}
			}else if (m.getType().equals(Type.release)){
				content2 = content2 + "Release rebut de: " + m.getFrom() + "a " + myId + "incrementa \n";
				textArea2.setText(content2);
				this.numOkay = this.numOkay+1;
			}else if (m.getType().equals(Type.action)){
				content2 = content2 + "Action rebut de: " + m.getFrom() + "a " + myId + "\n";
				textArea2.setText(content2);
				actualitzaValor(((LamportMessage)m).getAction());
			}
		} catch (IOException e) {
			System.out.println("Error sending message");
		}


	}
	
	private boolean okayCS(){
		int numFinal = this.numProc-2;
		if (numOkay.equals(numFinal)){
			return true;
		}else{
			return false;
		}
	}
	
	private void MessagesManagement(){
		Thread t1 = new Thread(new Runnable() {           
			public void run() {
				try {
					content2 = content2 + "LamportManagement Inicialitzat" + "\n";
					textArea2.setText(content2);
					content2 = content2 + "Length: "+ commSockets.getSocketsLength() + "\n";
					textArea2.setText(content2);
					while (true){
						for (int i=1; i<commSockets.getSocketsLength(); i++){
							if ((i!=myId) && (commSockets.getSockets().get(i).getInputStream().available() > 0)){
								content2 = content2 + "Trobaaa" + i + "\n";
								textArea2.setText(content2);
		
								BufferedReader in = new BufferedReader(new InputStreamReader(commSockets.getSockets().get(i).getInputStream()));
								String message = in.readLine();

								content2 = content2 + "Llegeixo "+ message + " a "+ myId+ " de " + i + "\n";
								textArea2.setText(content2);
								Message msg = convertStringToMessage(message, myId, i);							
								handleMsg(msg);
								
							}
							
						}
					}
				} catch (IOException e) {
					System.out.println("IOException thread");
				}
			} 
		});
		t1.start();
	}

	private Message convertStringToMessage(String message, int to, int from){
		LamportMessage msg = new LamportMessage();
		//System.out.println(message);
		String[] split = message.split(" ");
		msg.setType(msg.getTypeFromString(split[0]));
		if (!msg.getTypeFromString(split[0]).equals(Type.action)){
			msg.setLogicalClock(Integer.parseInt(split[1]));
		}else{
			msg.setAction(split[1]);
		}
		msg.setFrom(from);
		msg.setTo(to);
		return msg;
	}
	
	private void RicartAndAgrawalaFlow(){
	    Thread t = new Thread(new Runnable() {           
	        public void run() { 
	        	content = content + "LamportFlow Inicialitzat" + "\n";
		        textArea.setText(content);
				while (true){
					myts = Integer.MAX_VALUE;
					c.reset();
					while(getAction() == null){}
					content = content + "Action! \n";
			        textArea.setText(content);
					requestCS();
					numOkay = 0;
					//Detecta que te CS
					while(!okayCS()){}
					content = content + "DINS! \n";
			        textArea.setText(content);
					//Envia la trama de canvis a la resta de nodes (només ecriptura)
					try {
						commSockets.broadcastAction(action.getAction(), myId);
					} catch (IOException e) {
						System.out.println("Error sending action");
					}
					content = content + "Broadcast \n";
			        textArea.setText(content);
					//Actualitza el el valor
					actualitzaValor(action.getAction());
					//Guarda versio en el fitxer
					Runnable actionRun = action.getThread();
					action = null;
					synchronized(actionRun){
						actionRun.notify();
					}
					content = content + "NOTIFY! \n";
					textArea.setText(content);
					releaseCS();
				}
	        } 
	    });
	    t.start();
		
	}
	
	@Override
	public void run(){
		MessagesManagement();
		RicartAndAgrawalaFlow();
	}

	public void actualitzaValor(String action){
		String[] frames = action.split(":");
		layer.getValors()[Integer.parseInt(frames[0])] = Integer.parseInt(frames[1]); 
		try {
			layer.getLockVersion().acquire();
			content = content + "Incrementa versio: "+ layer.getVersio() +"\n";
	        textArea.setText(content);
			((CoreLayer)layer).incrementaVersio(action + " ");
			content = content + "Incrementat versio: "+ layer.getVersio() +"\n";
	        textArea.setText(content);
			layer.getLockVersion().release();
		} catch (InterruptedException e) {
			System.out.println("LockVersion Error");
		}
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

	public int getMyts() {
		return myts;
	}

	public void setMyts(int myts) {
		this.myts = myts;
	}

	public int getNumProc() {
		return numProc;
	}

	public void setNumProc(int numProc) {
		this.numProc = numProc;
	}

	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public LamportClock getC() {
		return c;
	}

	public void setC(LamportClock c) {
		this.c = c;
	}

	public LinkedList<Integer> getPendingQ() {
		return pendingQ;
	}

	public void setPendingQ(LinkedList<Integer> pendingQ) {
		this.pendingQ = pendingQ;
	}

	public Integer getNumOkay() {
		return numOkay;
	}

	public void setNumOkay(Integer numOkay) {
		this.numOkay = numOkay;
	}

	public CommSockets getCommSockets() {
		return commSockets;
	}

	public void setCommSockets(CommSockets commMatrix) {
		this.commSockets = commMatrix;
	}

	public boolean isTocken() {
		return tocken;
	}

	public void setTocken(boolean tocken) {
		this.tocken = tocken;
	}

	public synchronized Action getAction() {
		return action;
	}

	public synchronized void setAction(Action action) {
		this.action = action;
	}
	
}