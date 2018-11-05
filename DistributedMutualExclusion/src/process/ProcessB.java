package process;
import java.util.ArrayList;
import java.util.List;

import baseclasses.Message;
import baseclasses.Message.Type;
import communication.CommMatrix;
import protocols.Centralized;


public class ProcessB extends Centralized {

	public final int ProcessLWB_length = 2;
	private List<ProcessLWB> children;
	private CommMatrix commMatrixChildren;
	private boolean q[];
	
	public ProcessB(int myId, CommMatrix commMatrix){
		
		super(myId, commMatrix, 450, 250);
        this.commMatrixChildren = new CommMatrix(ProcessLWB_length+1); 
        this.children = new ArrayList<ProcessLWB>();
        this.children.add(new ProcessLWB(1, ProcessLWB_length+1, this.commMatrixChildren, 0, 500));
        this.children.add(new ProcessLWB(2, ProcessLWB_length+1, this.commMatrixChildren, 450, 500));
        //this.children.add(new ProcessLWB(3, ProcessLWB_length+1, this.commMatrixChildren, 450, 500));
        this.q = new boolean[ProcessLWB_length];
        for (int i=0; i<ProcessLWB_length;i++){
        	this.q[i] = false;
        }
        
        for (ProcessLWB process: this.children){
			process.start();
		}  
	}
	
	public boolean childrenFinished(){
		int j=0;
		for (int i=0; i< ProcessLWB_length; i++){
			if ((!commMatrixChildren.getMatrix()[i+1][0].isEmpty())){
				Message message = (Message)commMatrixChildren.getMatrix()[i+1][0].pop();
				if (message.getType().equals(Type.release)){
					this.setContent(this.getContent() + "Tinc release: "+ (i+1) +"\n");
					this.getTextArea().setText(this.getContent());
					q[i] = true;
				}	
			}
			if (q[i]) j++;
		}
		if (j == ProcessLWB_length){
			return true;
		}
		return false;
	}
	
	@Override
	public void run(){
		while (true){
	        for (int i=0; i<ProcessLWB_length;i++){
	        	this.q[i] = false;
	        }
			this.setContent(this.getContent() + "Envia B \n");
	        this.getTextArea().setText(this.getContent());
			requestCS();
			while (!haveTocken()){}	
			this.setContent(this.getContent() + "Tinc tocken: "+ this.getMyId() +"\n");
			this.getTextArea().setText(this.getContent());
			for (ProcessLWB process: this.children){
				this.setContent(this.getContent() + "Envia tocken als fills: "+ process.getMyId() + "\n");
		        this.getTextArea().setText(this.getContent());
				process.tocken(process.getMyId());
			}
			while (!childrenFinished()){}
			this.setContent(this.getContent() + "Children B acabat!! \n");
			this.getTextArea().setText(this.getContent());
			releaseCS();	
			try {
			    Thread.sleep(1000);                
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			//while(true){}
		}
	}
}
