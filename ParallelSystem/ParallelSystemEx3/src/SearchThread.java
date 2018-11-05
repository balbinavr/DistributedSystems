
public class SearchThread extends Thread{
	private int[] array;
	private int buscat;
	private volatile int numThread;
	private volatile int trobat;
	
	public SearchThread(int buscat, int[] array, int numThread){
		this.buscat = buscat;
		this.array = array;
		this.trobat = -1;
		this.numThread = numThread;
		/*System.out.println("\n Num: "+Integer.toString(buscat));
		for (int z: array){
			System.out.println(z);
		}*/
	}
	
	public void run(){
		
		for (int i=0; i<this.array.length; i++){
			if (this.array[i] == buscat){
				trobat = i;
				break;
			}
		}
	}
	
	public int getValueTrobat(){
		return this.trobat;
	}
	
	public int getNumThread(){
		return this.numThread;
	}
}
