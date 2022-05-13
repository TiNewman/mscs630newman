/**
 * file: Encryption.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: Final Project
 * due date: May 15th, 2022
 * version: 1.0
 *
 * This file contains the actual encryption and decryption for the application.
 * Help from : https://github.com/eugenp/tutorial.
 * All modifications completed by myself.
 */
package com.file_encryption;


import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.BadPaddingException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.IllegalBlockSizeException;
 
/**
 * Encryption
 *
 * This class contains the code that uses Java's encryption standards.
 * For this application, we are focusing on an AES implementation with 
 * a symmetric-key block cipher (256 bit encryption with stream encryption). 
 * We do this as we use the same key for both encrypting and decrypting files.
 * Right now this encryption class can only encrypt or decrypt single files
 * at a time, however it can be called multiple times.
 * 
 * 
 */
public class Encryption {

  public static final String SALT = "thisIsADemo";

  public Encryption() {}

  /**
   * generateKey
   *
   * This function is used to create completely random
   * key based off of a key generator given by javax.crypto.
   * It creates a symmetric secret key used for encryption/decryption.
   * Whatever we pass is the actual key size:
   *  AES (128)
   *  DES (56)
   *  DESede (168)
   *  HmacSHA1
   *  HmacSHA256
   * 
   * @param n key size.
   *  
   * @return SecretKey, is the secret key to be used in the encryption/decryption.
   */
  public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {

    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(n);

    SecretKey key = keyGenerator.generateKey();

    return key;
  }

  /**
   * getKeyFromPassword
   *
   * This function is used to create a secret key
   * based off of a set string.
   * Here we actually set the parameters of the generator to use PBKDF2
   * with SHA1 HMAC (which  mixes a secret key,
   * hashes the result with the hash function, 
   * mixes that hash value with the secret key,
   * finally applies the hash function a second time).
   * This allows us to then create a cryptographic key with the password and the
   * secrete key, which all comes together to create the finished secrete key.
   * 
   * @param password user password.
   *  
   * @return SecretKey, is the secret key to be used in the encryption/decryption.
   */
  public static SecretKey getKeyFromPassword(String password)
    throws NoSuchAlgorithmException, InvalidKeySpecException {

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), 65536, 256);
    SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
      .getEncoded(), "AES");

    return secret;
  }

  /**
   * IvParameterSpec
   *
   * This function is used to create an IV or initialization vector.
   *  
   * @return IvParameterSpec, is the IV to be used in the encryption/decryption.
   */
  public static IvParameterSpec generateIv() {

    byte[] iv = new byte[16];
    return new IvParameterSpec(iv);
  }

  /**
   * fileToByte
   *
   * This function is used to read everything from a file,
   * convert and save it into a byte array that will then be 
   * encrypted/decrypted based off each byte.
   * 
   * @param file file to convert to byte[].
   *  
   * @return byte[], is the file in form of byte[].
   */
  public static byte[] fileToByte(File file) throws IOException {

    byte [] byteData = new byte[(int) file.length()];

    try(FileInputStream fileInputStream = new FileInputStream(file)) {

      fileInputStream.read(byteData);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    return byteData;
  }

  /**
   * encryptAESCBC
   *
   * This function is 
   * 
   * @param key 
   * @param iv 
   * @param inputFile 
   * @param outputFile 
   *  
   */
  public static void encryptAESCBC(SecretKey key, IvParameterSpec iv,
    File inputFile, File outputFile) throws IOException, NoSuchPaddingException,
    NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException {

    // Need the file to be chars.
    byte[] inputFileAsBytes = fileToByte(inputFile);
    
    // Creating the instance of the AES encryption of type CBC with PKCS5 Padding
    // and using the key (password) and Iv.
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    // Actually encrypting it.
    byte[] cipherText = cipher.doFinal(inputFileAsBytes);

    // Writing to the file.
    FileOutputStream outputStream = new FileOutputStream(outputFile);
    outputStream.write(cipherText);
    outputStream.close();
  }


  public static void decryptAESCBC(SecretKey key, IvParameterSpec iv,
    File encryptedFile, File decryptedFile) throws IOException, NoSuchPaddingException,
    NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException {

    // Need the file to be chars.
    byte[] encryptedFileAsBytes = fileToByte(encryptedFile);

    // Start the  decryption cipher as using CBC with PKCS5 padding.
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, iv);

    // Actually encrypting it.
    byte[] decryptedText = cipher.doFinal(encryptedFileAsBytes);

    // Writing to the file.
    FileOutputStream outputStream = new FileOutputStream(decryptedFile);
    outputStream.write(decryptedText);
    outputStream.close();
  }


  public static void encryptAES256GCM(SecretKey key, byte[] iv,
  File inputFile, File encryptedFile) throws Exception {

    // Need the file to be chars.
    byte[] inputFileAsBytes = fileToByte(inputFile);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
    
    // Creating the actual algorithm with set parameters for encryption.
    cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
    
    // Actually encrypting it.
    byte[] cipherText = cipher.doFinal(inputFileAsBytes);

    // Writing to the file.
    FileOutputStream outputStream = new FileOutputStream(encryptedFile);
    outputStream.write(cipherText);
    outputStream.close();
  }

  

  public static void decryptAES256GCM(SecretKey key, byte[] iv,
  File encryptedFile, File decryptedFile) throws Exception {
    // Open the file and change it to bytes.
    byte[] holder1 = fileToByte(encryptedFile);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
    
    // Creating the actual algorithm with set parameters for decryption.
    cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
    
    // Actually decrypting it.
    byte[] decryptedText = cipher.doFinal(holder1);

    // Write to the file now that the decryption is done.
    FileOutputStream outputStream = new FileOutputStream(decryptedFile);
    outputStream.write(decryptedText);
    outputStream.close();
  }


  public static boolean encrypt(File fileToEncrypt, String userPassword, 
  boolean deleteFile, String modeType)
  throws Exception {

    IvParameterSpec ivParameterSpec = generateIv();
    byte[] IV = new byte[12];

    SecretKey key = generateKey(256);
    try {

      key = getKeyFromPassword(userPassword);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
      return false;
    }

    String holder = fileToEncrypt.getName();
    String[] fileNameCut = holder.split(".txt");

    String[] filePath = fileToEncrypt.getPath().split(fileNameCut[0]);

    File encryptedFile = new File(
    filePath[0] + fileNameCut[0] + "ENCRYPTED.txt");


    if (modeType.equals("GCM")) {

      encryptAES256GCM(key, IV, fileToEncrypt, encryptedFile);
    }
    else {

      encryptAESCBC(key, ivParameterSpec, fileToEncrypt, encryptedFile);
    }
    

    if (deleteFile) {

      boolean deleted = fileToEncrypt.delete();

      if (!deleted) { 

        System.out.println("Error with deleting input file -> encrypt().");
        return false;
      } 
    }

    return true;
  }

  public static boolean decrypt(File encryptedFile, String userPassword, boolean deleteFile,
  boolean renameForTest, String modeType) 
  throws Exception {

    SecretKey key = generateKey(256);

    try {

      key = getKeyFromPassword(userPassword);
    }
    catch (Exception e) {

      System.out.println("Error12: " + e);
    }

    IvParameterSpec ivParameterSpec = generateIv();
    byte[] IV = new byte[12];

    String holder = encryptedFile.getName();
    String[] fileNameCut = new String[] {" ", ""};
    
    if (holder.contains("ENCRYPTED")){

      fileNameCut = holder.split("ENCRYPTED");
    }
    else {

      fileNameCut = holder.split(".tx");
      fileNameCut[1] = "..txt";
    }

    String[] filePath = encryptedFile.getPath().split(fileNameCut[0]);

    if (renameForTest) {

      File decryptedFile = new File(
      filePath[0] + fileNameCut[0] + "DECRYPTED" + fileNameCut[1]);

      if (modeType.equals("GCM")) {

        decryptAES256GCM(key, IV, encryptedFile, decryptedFile);
      }
      else {

        decryptAESCBC(key, ivParameterSpec, encryptedFile, decryptedFile);
      }
    }
    else {

      File decryptedFile = new File(
      filePath[0] + fileNameCut[0] + fileNameCut[1]);

      if (modeType.equals("GCM")) {

        decryptAES256GCM(key, IV, encryptedFile, decryptedFile);
      }
      else {

        decryptAESCBC(key, ivParameterSpec, encryptedFile, decryptedFile);
      }
    }

    if (deleteFile) {

      boolean deleted = encryptedFile.delete();

      if (!deleted) { 

        System.out.println("Error with deleting input file -> decrypt().");
        return false;
      }
   }

    return true;
  }


  public static void main(String[] args) {}
  
}