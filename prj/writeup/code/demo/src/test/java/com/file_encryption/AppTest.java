/**
 * file: AppTest.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: Final Project
 * due date: May 15th, 2022
 * version: 1.0
 *
 * This file contains the all the tests for the actual encryption/decryption
 * functions.
 */
package com.file_encryption;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * AppTest
 *
 * This class contains the test for the Encryption.java class. 
 * In here we test encryption and decryption on three different 'test'
 * files which are in plaintext.
 * They go through encryption and decryption process.
 * The files are not deleted as I also double check manually.
 * Each test case should pass successfully, and without error.
 * Test case 1 is only text (upper and lower) and a single digit.
 * Test case 2 is focused on digits and special characters digit.
 * Test case 3 is a large file of text.
 * All test cases are the same so no individual comments, except
 * for the first one.
 */
public class AppTest {

  /**
   * is
   *
   * This function tests 2 files to make sure they are the same.
   * 
   * @param inputFile File, is the input file (starting file) that
   * is encrypted. 
   * @param decryptedFile File, is the decrypted file that
   * went through the entire encryption process.
   *  
   * @return boolean, which is true if the files where the same and
   * false if they weren't.
   */
  private boolean is(File inputFile, File decryptedFile) {

    try {

      return FileUtils.contentEquals(inputFile, decryptedFile);
    }
    catch (IOException e) {

      e.printStackTrace();
      return false;
   }
  }


  @Test
  public void testCase1() throws InterruptedException {

    // Gets the first file (input file, to be encrypted).
    File inputFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase1.txt")
    .toFile();

    // Encrypt the file, but don't delete the original.
    try {
      
      Encryption.encrypt(inputFile, "password1", false);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File encryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase1ENCRYPTED.txt")
    .toFile();

    // Decrypt te file, don't delete the file, and make sure the name is different.
    try {

      Encryption.decrypt(encryptedFile, "password1", false, true);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File decryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase1DECRYPTED.txt")
    .toFile();

    // Check if the beginning file (inputFile) is the same as the decrypted file.
    assertEquals(true, is(inputFile, decryptedFile));
  }


  @Test
  public void testCase2() throws InterruptedException {

    File inputFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase2.txt")
    .toFile();

    try {
      
      Encryption.encrypt(inputFile, "password1", false);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File encryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase2ENCRYPTED.txt")
    .toFile();

    try {

      Encryption.decrypt(encryptedFile, "password1", false, true);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File decryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase2DECRYPTED.txt")
    .toFile();

    assertEquals(true, is(inputFile, decryptedFile));
  }


  @Test
  public void testCase3() throws InterruptedException {

    File inputFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase3.txt")
    .toFile();

    try {
      
      Encryption.encrypt(inputFile, "password1", false);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File encryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase3ENCRYPTED.txt")
    .toFile();

    try {

      Encryption.decrypt(encryptedFile, "password1", false, true);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File decryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase3DECRYPTED.txt")
    .toFile();

    assertEquals(true, is(inputFile, decryptedFile));
  }

}
