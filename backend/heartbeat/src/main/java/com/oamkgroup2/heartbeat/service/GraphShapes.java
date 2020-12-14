package com.oamkgroup2.heartbeat.service;
import java.util.ArrayList; 

public class GraphShapes {
   
     //Declaring array list
     private static  ArrayList<Integer> AverageArray = new ArrayList<Integer>();

    // Taking average of the values
    public int AverageData(int[] a) {
        int total = 0;
        int av = 0;
        // int[] avergevaluesArray =
        for (int i = 0; i < 10; i++) {

            total = total + a[i];
            // System.out.println("total No : " + total);
            av = total / 10;
            // System.out.println("average: " + total);
        }

        return av;
    }

    // just checking the type of the array
    public static int checkType(int AveragArray[], int n) {
        int range1 = 0;
        int range2 = 10;
        int ArrayCounter = 0;
        int index = 0;
        int AveData = 0;
        int[] dataarray = new int[10];
        System.out.println(" arr.length: " + AveragArray.length / 10);

        // Storing the data in array after each 10 valuesevery time
        while (ArrayCounter < AveragArray.length / 10) {

            for (int i = 0; i < (AveragArray.length / 10) - 1; i++) {
                for (int j = range1; j < range2; j++) {
                    dataarray[index] = AveragArray[j];
                    //System.out.println(" dataarray[i]: " + dataarray[index]);
                    index++;
                    // Data is storing into temporary array for averaging.

                }
                index = 0;
            }

            // Taking Average after each 10 values
            GraphShapes av = new GraphShapes();
            AveData = av.AverageData(dataarray);
            
            //Storing in Arraylist, this for dynamics storing in array. 
            AverageArray.add(AveData);

            // for Next data values
            range1 = range1+10;
            range2 = range2+10;
            ArrayCounter = ArrayCounter+1;
        }

	//retrieving the Averagevalues from arraylist
       // for (int i = 0; i < AverageArray.size(); i++) {
          //  System.out.println("Average No : " + AverageArray.get(i));
       // }
        
	//Graph identifiction

        // If the first two and the last two elements of the array are in decreasing order  
        //(Graph is Decreasing)

        if (AveragArray[0] >= AveragArray[1] &&  
                 AveragArray[n - 2] >= AveragArray[n - 1])  
            return 0;  //downward slope
      
        // If the first two elements of the array are in increasing order and the last two elements of the array are in decreasing order
        //(Graph is Increasing then Decreasing)  

        else if (AveragArray[0] <= AveragArray[1] && 
               AveragArray[n - 2] >= AveragArray[n - 1])  
            //return hill
            return 1; 
  
        // If the first two elements of the array are in decreasing order and the last two elements of the array are in increasing order 
        //(graph is decreasing and then increasing)
 
        else
            return 2; 
    } 
    


public static void main(String[] args)  
    {  
        int[] arr = new int[]{ 40, 45, 50, 55, 60, 65, 70 , 75, 80, 85,90, 95, 100, 105, 102, 103, 107, 100, 98, 96, 99, 95, 94, 97, 99, 94, 95, 80, 81, 82, 83, 84, 89, 70, 75, 72, 73, 72, 71, 60, 69, 68, 67, 66, 65, 64, 63, 62, 61, 60}; 
        int n = arr.length; 
        System.out.println(checkType(arr, n)); 

    } 
}
