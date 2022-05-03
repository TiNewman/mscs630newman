package com.file_encryption;

import static org.junit.Assert.assertTrue;

import java.security.spec.ECFieldF2m;

import org.junit.Test;

import main.java.com.file_encryption.*;

/**
 * Unit test for simple App.
 */
public class AppTest {

  @Test
  public void testCase1() {

    File inputFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase1.txt")
    .toFile();

    try {
      
      Encryption.encrypt(inputFile, 'password1', false);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File encryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase1ENCRYPTED.txt")
    .toFile();

    try {

      Encryption.decrypt(encryptedFile, 'password1', false, true);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File decryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase1DECRYPTED.txt")
    .toFile();

    assertThat(inputFile).hasSameTextualContentAs(decryptedFile);
  }

  @Test
  public void testCase2() {

    File inputFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase2.txt")
    .toFile();

    try {
      
      Encryption.encrypt(inputFile, 'password1', false);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File encryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase2ENCRYPTED.txt")
    .toFile();

    try {

      Encryption.decrypt(encryptedFile, 'password1', false, true);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File decryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase2DECRYPTED.txt")
    .toFile();

    assertThat(inputFile).hasSameTextualContentAs(decryptedFile);
  }

  @Test
  public void testCase3() {

    File inputFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase3.txt")
    .toFile();

    try {
      
      Encryption.encrypt(inputFile, 'password1', false);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File encryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase3ENCRYPTED.txt")
    .toFile();

    try {

      Encryption.decrypt(encryptedFile, 'password1', false, true);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    File decryptedFile = Paths.get(
    "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/test/java/com/file_encryption/testCaseFiles/testCase3DECRYPTED.txt")
    .toFile();

    assertThat(inputFile).hasSameTextualContentAs(decryptedFile);
  }

}
