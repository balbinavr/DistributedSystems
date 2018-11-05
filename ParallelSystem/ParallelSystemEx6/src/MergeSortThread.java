
import java.util.*;
 
public class MergeSortThread extends Thread
{
	private Integer[] list;
	private int min;
	private int max;
	
	public MergeSortThread(Integer[] list, int min, int max){
		this.list = list;
		this.min = min;
		this.max = max;
	}

    public static void main(String[] args) 
    {
        //Array a ordenar
        Integer[] a = { 2, 6, 5, 1, 4 };       
        //Crida el merge sort
        MergeSortThread th1 = new MergeSortThread(a, 0, 5);
        th1.start();
        try {
			th1.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted Exception");
		}
        
        //Aconseguir array ordenat
        System.out.println(Arrays.toString(a));
    }
 
    @Override
    public void run() 
    {
    	System.out.println("Max: "+max+" Min: "+ min);
    	int size = this.max - this.min;
    	System.out.println("Size: " + size);
    	//si la llista està buida o nomes te un valor no cal fer res
    	if (size > 2) {

            try {
    			Thread.sleep(1000);
    		} catch (InterruptedException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    		MergeSortThread th1 = new MergeSortThread(this.list, min, (min+(size/2)));
    		MergeSortThread th2 = new MergeSortThread(this.list, (min+(size/2)), max);
    		// Lanzamos los threads para seguir con el particionado
    		th1.start();	 
    		th2.start();
    		try {
    			th1.join();
    			th2.join();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}

    		Integer[] first = new Integer[list.length / 2];
    		Integer[] second = new Integer[list.length - first.length];
    		System.arraycopy(list, 0, first, 0, first.length);
    		System.arraycopy(list, first.length, second, 0, second.length);
    		
    		merge(first, second, this.list);
    	
    	}else{
    		merge(this.list);
    	}
 
    }
    
	private void merge(Integer[] list) {
		if ( this.list[min] > this.list[(max-1)]) {
			int value = this.list[min];
			this.list[min] = this.list[max-1];
			this.list[max-1] = value;
		}		
	}
     
    private static void merge(Integer[] first, Integer[] second, Integer[] result){
    	
        int iFirst = 0;
        int iSecond = 0;
        int iMerged = 0;
         
        while (iFirst < first.length && iSecond < second.length){    	
            if (first[iFirst].compareTo(second[iSecond]) < 0){
                result[iMerged] = first[iFirst];
                iFirst++;
            } 
            else{
                result[iMerged] = second[iSecond];
                iSecond++;
            }
            iMerged++;
        }
        System.arraycopy(first, iFirst, result, iMerged, first.length - iFirst);
        System.arraycopy(second, iSecond, result, iMerged, second.length - iSecond);
    }
}
