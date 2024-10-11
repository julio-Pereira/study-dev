import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MiniMaxSum {

    /*
     * Complete the 'miniMaxSum' function below.
     *
     * The function accepts INTEGER_ARRAY arr as parameter.
     */

    public static void miniMaxSum(List<Integer> arr) {
        // Write your code here
        long min = arr.get(0);
        long max = arr.get(0);
        long sumMinimum = 0;
        long sumMaximum = 0;

        for (int i = 0; i < arr.size(); i++) {

            if (arr.get(i) > max) {
                max = arr.get(i);
            }

            if (arr.get(i) < min) {
                min = arr.get(i);
            }
        }

        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) != max) {
                sumMinimum += arr.get(i);
            }

            if (arr.get(i) != min) {
                sumMaximum += arr.get(i);
            }

            if (arr.get(i) == min && arr.get(i) == max) {
                sumMinimum += arr.get(i);
                sumMaximum += arr.get(i);
            }
        }

        if (sumMinimum == sumMaximum) {
            sumMinimum -= arr.getLast();
            sumMaximum -= arr.getLast();
        }
        System.out.print(sumMinimum + " " + sumMaximum);
    }
    public static void main(String[] args) throws IOException {


        List<Integer> arr = new ArrayList<>();

        arr.add(5);
        arr.add(5);
        arr.add(5);
        arr.add(5);
        arr.add(5);
        miniMaxSum(arr);

    }
}