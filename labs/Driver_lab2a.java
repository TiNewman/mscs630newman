/**
 * file: Driver_lab2a.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: lab 2a
 * due date: January 30th, 2022
 * version: 1.0
 *
 * This file contains the declaration of the Driver_lab2a class.
 */

import java.util.Scanner;

/**
 * Driver_lab2a
 *
 * This class walks us through use of the Euclidean algorithm,
 * which finds the greatest common divisor.
 * The main function takes in 2 integers and sends them to the 
 * 'euclidAlg' function, where we apply the algorithm and
 * find the greatest common divisor and send it back to the main function.
 */
public class Driver_lab2a {

  /**
   * main
   *
   * This function takes in user input and 
   * sends it to the 'euclidAlg' function.
   * It also prints out the greatest common divisor that it 
   * receives back from the 'euclidAlg' function.
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    Scanner input = new Scanner(System.in);

    while (input.hasNextLine()){

      String[] userInputArray = input.nextLine().split(" ");

      long input1 =  Long.parseLong(userInputArray[0]);
      long input2 =  Long.parseLong(userInputArray[1]);

      long result = euclidAlg(input1, input2);

      System.out.println(result);
    }

    input.close();
  }

  /**
   * euclidAlg
   *
   * This function applies the Euclidean algorithm, where we find the 
   * greatest common divisor between two numbers.
   * We have to also make sure 'a' is the larger number between the two
   * given inputs, and if it isn't, we have to swap them around.
   * We then return the greatest common divisor as a Long.
   * 
   * @param a Long, should be the largest number between the two inputs.
   * @param b Long, should be the smallest number between the two inputs.
   * 
   * @return Long, which is the greatest common divisor between 'a' and 'b'.
   */
  public static long euclidAlg(long a, long b) {

    boolean switched = false;
    long switcherHolder = 0;
    long answer = 0;

    // Switching the values if 'a' is not larger than 'b'.
    if (!(a >= b)) {

      switched = true;
      switcherHolder = a;
      a = b;
      b = switcherHolder;

    }

    boolean computing = true;
    long remainder = 0; 

    // This is where we actually apply the Euclidean algorithm.
    // Once we get to a remainder of 0, then we are finished.
    while (computing) {

      remainder = a % b; 
    
      if (remainder == 0){
  
        answer = b;
        computing = false;
      }
      else {

        a = b;
        b = remainder;
      }

    }

    // We are told to switch 'a' and 'b' back 
    // if we had to switch them in the beginning.
    if (switched) {

      switcherHolder = a;
      a = b;
      b = switcherHolder;
    }

    // Returning the greatest common divisor.
    return answer;
  }

}
