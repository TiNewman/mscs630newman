/**
 * file: Driver_lab3a.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: lab 3a
 * due date: February 6th, 2022
 * version: 1.0
 *
 * This file contains the declaration of the Driver_lab3a class.
 */

import java.util.Scanner;

/**
 * Driver_lab3a
 *
 * This class walks us through using an algorithm of cofactor expansion along
 * the first row of a matrix to find the determinant based off of a modulo.
 */
public class Driver_lab3a {

  /**
   * main
   *
   * This function takes in user input of a matrix and
   * sends it (and the first element which is the modulo 'm') to the
   * 'cofModDet' function where the actual algorithm of cofactor expansion
   * is done, and we find our determinant. This function then gets an integer
   * back from the 'cofModDet' function, which is the determinant and 
   * prints it.
   *
   * @param args
   */
  public static void main(String[] args) {

    Scanner input = new Scanner(System.in);

    int m = input.nextInt();
    int size = input.nextInt();
    int userInput[][] = new int[size][size];

    // Saving the user input into a matrix.
    for(int i= 0; i < size ; i++) {

      for (int j = 0; j < size; j++) {

        userInput[i][j] = input.nextInt();
      }
    }

    input.close();

    int def = cofModDet(m, userInput);
    System.out.println(def);
  }

  /**
   * cofModDet
   *
   * This function uses the algorithm of cofactor expansion 
   * along the first row of a matrix, and with a given modulo, 
   * gives us a determinant.
   * There are three parts to this, 
   * where the first is if the matrix is of size 1,
   * we get back that number as the determinant,
   * the second is if the matrix is of size 2,
   * and we do [0][0] * [1][1] - [0][1] * [1][0] as the determinant,
   * and finally if the matrix is of size 3 or larger,
   * we have to continually cut the matrix into sub-matrices and recursively
   * send them back through the algorithm until we find the sub-matrix's 
   * determinant, which all gets added up to become the final determinant.
   * We also swap between adding and subtracting each sub-matrix's 
   * determinant and make sure it is returned as type int.
   * 
   * @param m Integer, which is the modulo given by the user.
   * @param A Integer Matrix, which is the original matrix given by the user.
   * 
   * @return int, which is the determinant of the user given matrix.
   */
  public static int cofModDet(int m, int[][] A){
    long determinant = 0;
    int matrixSize = A.length;
    // As the test cases seem to use extremely large numbers, 
    // I had to convert the int matrix into a long matrix to allow the math 
    // to be computed correctly.
    long[][] longMatrix = convertToLong(A);

    if (matrixSize == 1) {

      determinant = longMatrix[0][0];
    }
    else if (matrixSize == 2) {

      determinant = longMatrix[0][0] * longMatrix[1][1] -
      longMatrix[0][1] * longMatrix[1][0];
    }
    else if (matrixSize >= 3) {

      // Swap between adding and subtracting the sectional determinants.
      int sign = 1;

      for (int i = 0; i < matrixSize; i++){

        int[][] subMatrix = new int[matrixSize - 1][matrixSize - 1];

        // Create a sub-matrix so we can calculate it's determinant.
        for (int largeRow = 1; largeRow < matrixSize; largeRow++) {

          int subColumn = 0;

          for (int j = 0; j < matrixSize; j++) {

            if (j != i) {

              subMatrix[largeRow - 1][subColumn] = A[largeRow][j];
              subColumn++;
            }
          }
        }

        // Cycle through each element in the row and 
        // find its determinant through its sub-matrix.
        determinant += sign * (longMatrix[0][i] * cofModDet(m, subMatrix));
        // We need to switch the sign as we move to the next recursive call.
        sign = -1 * sign;
      }
    }

    if (determinant % m < 0) {

      return (int) (determinant % m + m);
    }

    return (int) (determinant % m);
  }

  /**
   * convertToLong
   *
   * This function takes in a matrix that is of type int, 
   * and converts it to a matrix of type long.
   * This allows the math in the 'cofModDet' function to be done correctly.
   * 
   * @param A Integer Matrix, which is the original matrix given by the user.
   * 
   * @return Long Matrix, which is the same as 'A' but just in type long.
   */
  private static long[][] convertToLong(int A[][]){
    long[][] holder1 = new long[A.length][A.length];

    for (int i = 0; i < A.length; i++){

      for (int j = 0; j < A[i].length; j++){

        holder1[i][j] = (long) A[i][j];
      }
    }

    return holder1;
  }
}
