
public class SearchThread extends Thread{
	private int min;
	private int max;
	private int buscat;
	private ParallelSystemEx4 ex4class;
	private volatile int numThread;
	private volatile int trobat;
	
	public SearchThread(int buscat, ParallelSystemEx4 ex4class, int min, int max, int numThread){
		this.buscat = buscat;
		this.trobat = -1;
		this.max = max;
		this.min = min;
		this.ex4class = ex4class;
		this.numThread = numThread;
		/*System.out.println("\n Num: "+Integer.toString(buscat));
		for (int z: array){
			System.out.println(z);
		}*/
	}
	
	public void run(){
		
		for (int i=min; i<max; i++){
			int num = ex4class.getArrayIndex(i);
			System.out.println("Thread: "+ numThread + " i: " + i + " num: " + num );
			if (num == buscat){
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
