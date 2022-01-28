/**
 * file: Driver_lab2b.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: lab 2b
 * due date: January 30th, 2022
 * version: 1.0
 *
 * This file contains the declaration of the Driver_lab2b class.
 */

import java.util.Scanner;

/**
 * Driver_lab2b
 *
 * This class walks us through use of the Extended Euclidean algorithm,
 * which finds the greatest common divisor and its connected 'x' and 'y'.
 * The main function takes in 2 integers and sends them to the 
 * 'euclidAlgExt' function, where we apply the algorithm and
 * find the greatest common divisor, 'x' and 'y',
 * and sends them back to the main function.
 */
public class Driver_lab2b {

  /**
   * main
   *
   * This function takes in user input and 
   * sends it to the 'euclidAlgExt' function.
   * The inputs are saved as longs, and the array returned from the
   * 'euclidAlgExt' function will contain 3 longs.
   * Once it receives the array from the 'euclidAlgExt' function it prints out
   * the greatest common divisor, 'x', and 'y' found in the returned array.
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    Scanner input = new Scanner(System.in);

    while (input.hasNextLine()){

      String[] userInputArray = input.nextLine().split(" ");

      long input1 =  Long.parseLong(userInputArray[0]);
      long input2 =  Long.parseLong(userInputArray[1]);

      long[] result = euclidAlgExt(input1, input2);

      System.out.println(result[0] + " " + result[1] + " " + result[2]);
    }

    input.close();
  }

  /**
   * euclidAlgExt
   *
   * This function carries out the Extended Euclidean algorithm.
   * This means that it satisfies the following equation: 
   * d = ax + by, where d is the greatest common divisor, 
   * 'a' is the first input, 'b' is the second input, and
   * 'x' and 'y' are found through the algorithm.
   * We also make sure that 'a' is bigger than 'b', and if it isn't,
   * we swap them around and 
   * swap around the answer to fit the correct output schema.
   * 
   * @param a Long, should be the largest number between the two inputs.
   * @param b Long, should be the smallest number between the two inputs.
   * 
   * @return long[], with gcd or 'd' in position 0, x in position 1,
   * and y in position 2.
   */
  public static long[] euclidAlgExt(long a, long b) {

    boolean switched = false;
    long switcherHolder = 0;

    // Switching the values if 'a' is not larger than 'b'.
    if (!(a >= b)) {

      switched = true;
      switcherHolder = a;
      a = b;
      b = switcherHolder;
    }

    // Following the Extended Euclidean Algorithm.
    // Math and full explanation  is found here: 
    // https://www.youtube.com/watch?v=-uFc7-wOplM

    long quotient = 0;
    long remainder = 0;
    long s1 = 1;
    long s2 = 0;
    long s = 0;
    long t1 = 0;
    long t2 = 1;
    long t = 0;

    // Need to continue through the algorithm until b is 0.
    // Which means we can't go any further.
    while (b != 0)
    {

      remainder = a % b;
      quotient = a / b;

      // This is where we start the actual extended part of the algorithm.
      // We need to 'work' backwards to get 's' and 't',
      // or 'x' and 'y'.
      s = s1 - (s2 * quotient);
      t = t1 - (t2 * quotient);

      // Now we shift everything over.
      a = b;
      b = remainder;
      s1 = s2;
      s2 = s;
      t1 = t2;
      t2 = t;
    }

    // We are told to switch 'a' and 'b' back 
    // if we had to switch them in the beginning.  
    if (switched) {

      switcherHolder = s1;
      s1 = t1;
      t1 = switcherHolder;
    }

    // Returning the gcd, x, and y. In the order.
    return new long[] {a, s1, t1};
  }

}
