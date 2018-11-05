

public class ParallelSystemEx4{
	
	private int[] array = {0, 1, 2 , 3, 4, 5, 6, 7, 8, 9, 10, 11};
	
	public static void main (String[] args){
		 
		ParallelSystemEx4 ex4class = new ParallelSystemEx4();
		int trobat = ex4class.cercaParallela(11,5);
		System.out.println("\n Final! \n Posicio: "+Integer.toString(trobat));
		
	}
	
	public int cercaParallela (int aBuscar, int NumThreads){
		
		//crea tants threads com li passen per parˆmetre
		SearchThread[] threads = new SearchThread[NumThreads];
		int trobat_final;
		//numero de threads necessaris
		int num= (array.length/NumThreads);
		//en cas que no sigui un divisor exacte
		int rest=(array.length%NumThreads);
		int i=0, max;
		System.out.println("\n numThreads: "+Integer.toString(NumThreads));
		//System.out.println("\n Length: "+Integer.toString(Array.length));
		System.out.println("\n num: "+Integer.toString(num));
		System.out.println("\n rest: "+Integer.toString(rest));
		for (int j=0; j<threads.length; j++){
			if (j==(NumThreads-1) && rest!=0){
				//System.out.println("\n No exacta");
				max = rest;
			}else{
				//System.out.println("\n Exacta");
				max = 0;
			}
			//System.out.println("\nmin "+ i*num);
			//System.out.println("\nmax "+ (((i+1)*num)+max));
			threads[i] = new SearchThread(aBuscar, this, i*num, ((i+1)*num)+max, (i+1));
			i++;
		}
		
		for (int j=0; j<threads.length; j++){
			threads[j].start();
		}

		for (int j=0; j<threads.length; j++){
			try {
				threads[j].join();
				if (threads[j].getValueTrobat() != -1){
					System.out.println("\n Value trobat en la posiciÃ³ "+Integer.toString(threads[j].getValueTrobat())+ " dins del thread " + Integer.toString(threads[j].getNumThread()));	
					trobat_final = threads[j].getValueTrobat();
					return trobat_final;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return -1;
		
	}

	public int[] getArray() {
		return array;
	}
	
	public int getArrayIndex(int i){
		if (i < array.length){
			return array[i];	
		}
		return -1;	
	}

}
