

public class ParallelSystemEx3{
	
	public static void main (String[] args){
		int[] array = {0, 1, 2 , 3, 4, 5, 6, 7, 8, 9, 10, 11};
		int trobat = cercaParallela(11, array, 5);
		System.out.println("\n Final! \n Posicio: "+Integer.toString(trobat));
		
	}
	
	public static int cercaParallela (int aBuscar, int[] Array, int NumThreads){
		SearchThread[] threads = new SearchThread[NumThreads];
		int trobat_final;
		int num=(Array.length/NumThreads);
		int rest=(Array.length%NumThreads);
		int i=0, max;
		/*System.out.println("\n numThreads: "+Integer.toString(NumThreads));
		System.out.println("\n Length: "+Integer.toString(Array.length));
		System.out.println("\n num: "+Integer.toString(num));
		System.out.println("\n rest: "+Integer.toString(rest));*/
		for (int j=0; j<threads.length; j++){
			if (j==(NumThreads-1) && rest!=0){
				//System.out.println("\n No exacta");
				max = rest;
			}else{
				//System.out.println("\n Exacta");
				max = 0;
			}
			//System.out.println("\n max: "+Integer.toString(max));
			int[] array1 = new int[num+max];
			int y=0;
			//System.out.println("\n i: "+Integer.toString(i));
			for (int z=i*num; z<(num*(i+1)+max);z++){
				array1[y]=Array[z];
				y++;
			}
			threads[i] = new SearchThread(aBuscar, array1, (i+1));
			i++;
		}
		
		for (int j=0; j<threads.length; j++){
			threads[j].start();
		}

		for (int j=0; j<threads.length; j++){
			try {
				threads[j].join();
				if (threads[j].getValueTrobat() != -1){
					System.out.println("\n Value trobat en la posició "+Integer.toString(threads[j].getValueTrobat())+ " dins del thread " + Integer.toString(threads[j].getNumThread()));	
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

}
