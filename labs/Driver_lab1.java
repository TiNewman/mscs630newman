/**
 * file: Driver_lab1.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: lab 1
 * due date: January 23rd, 2022
 * version: 1.0
 *
 * This file contains the declaration of the Driver_lab1 class.
 */

import java.util.HashMap;
import java.util.Scanner;

/**
 * Driver_lab1
 *
 * This class shows the basics of data encryption by taking
 * in strings and converting them to integers. The main function
 * takes in strings and sends them to the 'str2int' function,
 * where each character is converted to an integer and saved in 
 * an array.
 */
public class Driver_lab1 {

  /**
   * main
   *
   * This function takes in user input and 
   * sends it to the 'str2int' function.
   * It also prints out the integers that it receives back from 
   * the 'str2int' function.
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    Scanner input = new Scanner(System.in);

    while (input.hasNextLine()){

      int[] result = str2int(input.nextLine());

      // Printing out the integers that were converted
      // from the user inputted text.
      for (int i = 0; i < result.length; i++) {
 
        System.out.print(result[i] + " ");
      }
      
      System.out.println();

    }

    input.close();
  }

  /**
   * str2int
   *
   * This function takes in text, and based on each character,
   * it converts it to an integer and saves it in an array.
   * It uses a hashmap, which contains the alphabet and a space,
   * to convert each character in the string to an integer.
   * 
   * 
   * @param plainText String which will be converted to integers.
   * @return Returns an int[], which holds the converted string in integers.
   */
  public static int[] str2int(String plainText) {

    String lowerPlainText = plainText.toLowerCase();
    HashMap<Character, Integer> charToInt = new HashMap<>();
    int[] convertedChars = new int[plainText.length()];
    
    // Inserting the alphabet into the hashmap with 
    // its corresponding integer.
    int mapCounter = 0;
    for(char letter = 'a'; letter <='z'; letter++ )
    {
      charToInt.put(letter, mapCounter);
      mapCounter++;
    }

    // A space (" ") is a special case outside the alphabet, 
    // that needs to be in the hashmap.
    charToInt.put(' ', 26);

    // Going through each character in the string, 
    // and converting it to its corresponding integer.
    for (int i = 0; i < lowerPlainText.length(); i++) {
 
      convertedChars[i] = charToInt.get(lowerPlainText.charAt(i));
    }

    return convertedChars;
  }
}
