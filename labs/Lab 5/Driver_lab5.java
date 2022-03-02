/**
 * file: Driver_lab5.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: lab 5
 * due date: March 7th, 2022
 * version: 1.0
 *
 * This file contains the declaration of the Driver_lab5 class.
 */

import java.util.Scanner;

/**
 * Driver_lab5
 *
 * This class contains the driver for lab5.
 * This class takes in a user input of a single system key, 
 * and a plain text in Hex form, and passes it to the "AESCipher.java" file
 * which will go through the actual AES 10 round encryption.
 * It used the "AES" function.
 * It then prints out the cipher-text given back from the 'AES' function.
 */
public class Driver_lab5 {
  
  /**
   * main
   *
   * This function takes in a user input of a single system key, 
   * and a plain text in Hex form, and passes it to the "AESCipher.java" file
   * which will go through the actual AES 10 round encryption.
   * This function retrieves a String[], with only a single String.
   * The single string is the cipher-text and prints it out.
   *
   * @param args
   */
  public static void main(String args[]){

    Scanner input = new Scanner(System.in);
    String systemKey = input.nextLine();
    String plainText = input.nextLine();
    input.close();

    String[] answer = AESCipher.AES(plainText, systemKey);

    System.out.println(answer[0]);

  }

}
