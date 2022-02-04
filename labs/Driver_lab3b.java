/**
 * file: Driver_lab3b.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: lab 3b
 * due date: February 6th, 2022
 * version: 1.0
 *
 * This file contains the declaration of the Driver_lab3b class.
 */

import java.util.Scanner;

/**
 * Driver_lab3b
 *
 * This class walks us through converting a a plaintext string and 
 * converting it to a Hexadecimal matrix with the help of a padding element.
 * First we convert the string into ASCII in a 4x4 matrix, then we return
 * the matrix and we print each 4x4 matrix in Hexadecimal format to the user.
 * The padding element (character) is used to fill any 
 * holes left in a 4x4 matrix.
 */
public class Driver_lab3b {

  /**
   * main
   *
   * This function takes in user input of the first line being 
   * the padding character, and the second line being a string 
   * that we will convert into ASCII and  print out as 
   * a 4x4 hexadecimal matrix.
   * This function sends a string of length 1 to 16 and a padding element 
   * to the 'getHexMatP' function, and continues to do so until it 
   * reaches the end of the plaintext given by the user.
   * The 'getHexMatP' function then returns a 4x4 matrix as an int with ASCII.
   * This function then prints out each element in the matrix as a hexadecimal.
   * 
   * @param args
   */
  public static void main(String[] args) {

    Scanner input = new Scanner(System.in);

    char substitute = input.nextLine().charAt(0);
    String plainText = input.nextLine();

    input.close();

    int startingIndex = 0;
    int endIndex = 16;
    int counter = 0;
    int evenSixteenSplit = plainText.length() / 16;
    int unevenSixteenSplit = plainText.length() % 16;

    // This is how many times we need to cycle through the plaintext to 
    // make sure we have enough 4x4 matrices.
    int timesToSend = evenSixteenSplit + 1;

    while (timesToSend != 0) {

      // Here we are setting the start and end indexes for getting a 
      // substring from the plaintext.
      if (counter < evenSixteenSplit){

        endIndex = startingIndex + 16;
        counter++;
      }
      else if (unevenSixteenSplit > 0) {

        startingIndex = plainText.length() - unevenSixteenSplit;
        endIndex = plainText.length();
      }

      int[][] asciiMatrix = 
      getHexMatP(substitute, plainText.substring(startingIndex, endIndex));
      
      // Here we are cycling through the matrix returned by the 'getHexMatP'
      // function so we can print each item out and 
      // have it converted to hexadecimal.
      for (int i = 0; i < asciiMatrix.length; i++){

        for (int j = 0; j < asciiMatrix[0].length; j++){

          System.out.print(
            Integer.toHexString(asciiMatrix[i][j]).toUpperCase() + " "
            );
        }

        System.out.println();
      }

      System.out.println();

      // Every time we move to the next 4x4 matrix, so we do it one less time,
      // and we make sure we move to the next index in the plaintext.
      startingIndex = endIndex;
      timesToSend -= 1;
    }

  }

  /**
   * getHexMatP
   *
   * This function takes in a padding character ('s') and a string ('p') of
   * length 1 to 16 and puts it into a 4x4 matrix where each 
   * character is converted to it's relative ASCII value.
   * If the plaintext is not of size 16, it uses the padding character to
   * fill the rest of the 4x4 matrix.
   * 
   * @param s Character, where it is a single the padding/substitute character.
   * @param p String, where it is a string of length 1 to 16 that will be 
   * converted to ASCII.
   * 
   * @return Integer[][], which holds the converted string 'p' and 
   * 's' if there are empty spaces in the 4x4 ASCII matrix.
   */
  public static int[][] getHexMatP(char s, String p){

    int[][] asciiMatrix = new int[4][4];
    char[] textInChar = p.toCharArray();
    int counter = 0;

    for (int j = 0; j < 4; j++) {

      for (int i = 0; i < 4; i++) {

        if (counter < p.length()){

          // Inserting with 'i' and then 'j' to fill the matrix column first,
          // not rows first. 
          // This is also where we convert the character to INT (or ASCII).
          asciiMatrix[i][j] = textInChar[counter];
          counter++;
        }
        else {
          // This is where we use the padding character to 
          // fill the rest of the matrix.
          asciiMatrix[i][j] = s;
        }

      }

    }

    return asciiMatrix;
  } 

}
