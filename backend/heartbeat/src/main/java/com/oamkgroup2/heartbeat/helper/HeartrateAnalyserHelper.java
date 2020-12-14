package com.oamkgroup2.heartbeat.helper;

import com.oamkgroup2.heartbeat.model.ShapeResult;
import com.oamkgroup2.heartbeat.model.Log;

public class HeartrateAnalyserHelper {

    private HeartrateAnalyserHelper() {
    };

    // Taking average of the values
    private static int averageData(int[] a) {
        int total = 0;

        for (int i = 0; i < a.length; i++) {
            total = total + a[i];
        }

        double avg = ((double) total) / a.length;
        return (int) Math.round(avg);
    }

    // just checking the type of the array
    public static ShapeResult checkShape(Log inputArray[]) {
        int arrayLength = inputArray.length;

        int firstThird = 0;
        int secondThird = 0;
        int thirdThird = 0;
        int remainder = arrayLength % 3;
        if (remainder == 0) {
            firstThird = arrayLength / 3;
            secondThird = firstThird * 2;
            thirdThird = arrayLength;
        } else if (remainder == 1) {
            firstThird = (arrayLength / 3);
            secondThird = firstThird * 2;
            thirdThird = arrayLength;
            firstThird++;
        } else {
            firstThird = (arrayLength / 3);
            secondThird = firstThird * 2;
            thirdThird = arrayLength;
            firstThird++;
            secondThird++;
        }

        int[] firstThirdArray = new int[firstThird];
        int[] secondThirdArray = new int[secondThird - firstThird];
        int[] thirdThirdArray = new int[thirdThird - secondThird];

        for (int i = 0; i < inputArray.length; i++) {
            if (i < firstThird) {
                firstThirdArray[i] = inputArray[i].getHeartRate();
            } else if (i < secondThird) {
                secondThirdArray[i - firstThird] = inputArray[i].getHeartRate();
            } else {
                thirdThirdArray[i - secondThird] = inputArray[i].getHeartRate();
            }
        }

        int firstAvg = HeartrateAnalyserHelper.averageData(firstThirdArray);
        int secondAvg = HeartrateAnalyserHelper.averageData(secondThirdArray);
        int thirdAvg = HeartrateAnalyserHelper.averageData(thirdThirdArray);

        // Graph identifiction

        // If the first two and the last two elements of the array are in decreasing
        // order
        // (Graph is Decreasing)

        if (firstAvg > secondAvg && secondAvg > thirdAvg) {
            return ShapeResult.SLOPE; // downward slope
        }
        // If the first two elements of the array are in increasing order and the last
        // two elements of the array are in decreasing order
        // (Graph is Increasing then Decreasing)

        else if (firstAvg < secondAvg && secondAvg > thirdAvg) {
            // return hill
            return ShapeResult.HILL;
        }
        // If the first two elements of the array are in decreasing order and the last
        // two elements of the array are in increasing order
        // (graph is decreasing and then increasing)

        else if (firstAvg > secondAvg && secondAvg < thirdAvg) {
            return ShapeResult.HAMMOCK;
        } else {
            return ShapeResult.UNDEFINED;
        }
    }
}
