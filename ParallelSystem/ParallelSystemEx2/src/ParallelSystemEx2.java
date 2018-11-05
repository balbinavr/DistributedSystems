import java.util.ArrayList;
import java.util.List;

public class ParallelSystemEx2 extends Thread{

	private List<Integer> list;
	private int number;
	private Boolean up;
	public static final Integer MAX = 100;
	
	public ParallelSystemEx2(List<Integer> list, int number, Boolean up){
		this.list = list;
		this.up = up;
		this.number = number;
		
	}

	public void run(){
		Boolean trobat=false;
		if (this.up){
			for (int i=0; i<MAX&&(!trobat); i++){
				//System.out.println("\nUp: "+ this.list.get(i));
				if (this.list.get(i)==this.number){
					System.out.println("\nTrobat UP ");
					trobat=true;
				}
			}
		}else{
			for (int i=(MAX-1); i>0&&(!trobat); i--){
				//System.out.println("\nDown: "+ this.list.get(i));
				if (this.list.get(i)==this.number){
					System.out.println("\nTrobat DOWN");
					trobat=true;
				}
			}
		}
	}

	public static void main (String[] args){
		
		List<Integer> linkedlist = new ArrayList<Integer>();
		for (int i=0; i<MAX; i++){
			linkedlist.add(i);
		}
		ParallelSystemEx2 s1 = new ParallelSystemEx2(linkedlist, 5, false);
		ParallelSystemEx2 s2 = new ParallelSystemEx2(linkedlist, 5, true);
		s1.start();
		s2.start();
		try {
			s1.join();
			s2.join();
		} catch (InterruptedException e) {}
		System.out.println("\nFINAL!\n");
	}

}
