
import java.util.*;
 
public class MergeSort{
	
    public static void main(String[] args){
    	
        //Array a ordenar
        Integer[] a = { 2, 6, 3, 5, 1 };   
        //Crida el merge sort
        mergeSort(a); 
        //Aconseguir array ordenat
        System.out.println(Arrays.toString(a));
    }
 
    public static Integer[] mergeSort(Integer[] list){
        //si la llista esta buida o nomes te un valor no cal fer res
        if (list.length <= 1) {
            return list;
        }
         
        //divideix l'array en dues parts
        Integer[] first = new Integer[list.length / 2];
        Integer[] second = new Integer[list.length - first.length];
        System.arraycopy(list, 0, first, 0, first.length);
        System.arraycopy(list, first.length, second, 0, second.length);
         
        //ordena les dues parts per separat ( de manera recursiva )
        mergeSort(first);
        mergeSort(second);
         
        //Merge both halves together, overwriting to original array
        merge(first, second, list);
        return list;
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
