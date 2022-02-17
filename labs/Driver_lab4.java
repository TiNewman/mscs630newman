/**
 * file: Driver_lab4.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: lab 4
 * due date: February 21st, 2022
 * version: 1.0
 *
 * This file contains the declaration of the Driver_lab4 class.
 */

import java.util.Scanner;

/**
 * Driver_lab4
 *
 * This class contains the driver for lab4.
 * This class takes in a user input of a single system key, 
 * and passes it to the "AESCipher.java" file through the 
 * "AESRoundKeys" function.
 * It then prints out the 11 round keys given back from AESRoundKeys.
 */
public class Driver_lab4 {
  
  /**
   * main
   *
   * This function takes in user input of string which is a system key.
   * It then send it to "AESCipher.java" file, and into the "AESRoundKeys"
   * function which returns 11 round keys in a string array.
   * It then prints out all 11 round keys.
   *
   * @param args
   */
  public static void main(String args[]){

    Scanner input = new Scanner(System.in);
    String hexKey = input.nextLine();
    input.close();

    String[] roundKeys = AESCipher.AESRoundKeys(hexKey);

    for (int i = 0; i < roundKeys.length; i++) {

      System.out.println(roundKeys[i]);
    }

  }

}
