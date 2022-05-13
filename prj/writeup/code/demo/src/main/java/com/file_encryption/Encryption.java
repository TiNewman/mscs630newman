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
   * This function encrypts a file into a separate file using
   * AES and its CBC variation.
   * The generalized encryption works with a plain text and a secret key which,
   * when used together in the cipher, give you a corresponding cipher text.
   * If the data that we are encrypting isn't 128 bits, we use PKCS 5 as the 
   * padding method (which fills the remaining data to 128 bits).
   * We are using the CBC variation in this method which stands for 
   * Cipher Block Chaining.
   * It uses an IV and XORs the plaintext together
   * (in the case for this function we converted directly to bytes).
   * Which gives the first round of AES encrypted data.
   * The algorithm then moves to the next block and uses the encryption data 
   * (from earlier) to XOR with the next block of bytes (data given in) and 
   * continues until it reaches the end of the data (or the padded data).
   * This can not be parallelized.
   * This variation is the step above the ECB variation as it is randomizing the
   * plaintext (bytes) used in the previous block, which left ECB with a 
   * vulnerability.
   * CBC's equation:
   *  Ci = E(K, Pi XOR Ci-1) for i = 1,....k
   * The function implements use of Java's javax.crypto, which holds the actual
   * AES encryption and functions.
   * All that we do here is construct the cipher(algorithm) with settings for 
   * how and what we want the cipher to do.
   * The cipher takes in different parameters depending on the encryption,
   * and as we are using AES CBC with padding, it takes in the the 
   * mode (are we encrypting or decrypting), the secret key, and the IV. 
   * We taken two files, where the first has it's data converted to byte[] and
   * encrypted, and then the encrypted data is saved into the second file.
   * 
   * @param key SecretKey, secret key which acts as the key to lock the encryption.
   * @param iv IvParameterSpec, the initialization vector. 
   * @param inputFile File, is the file that contains the data to be encrypted.
   * @param outputFile File, where the encrypted data goes.
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

  /**
   * decryptAESCBC
   *
   * This function decrypts a file into a separate file using
   * AES and its CBC variation.
   * The 'encryptAESCBC' already discussed the implementation for encrypting,
   * and as for decrypting, it is simply doing the inverse encryption.
   * As AES uses symmetric cryptography, the same key is used for decryption.
   * So we take in the user's secret key and if it is correct, it will 
   * decrypt the data.
   * It first starts with getting the inverse round key 
   * (inverse of IV and XOR encrypted data),
   * then reverses the encryption process till completed.
   * The data is taken from the first file and decrypted, where the 
   * decrypted data is then saved into the second file.
   * 
   * @param key SecretKey, secret key which acts as the key to unlock the encryption.
   * @param iv IvParameterSpec, the initialization vector. 
   * @param inputFile File, is the file that contains the encrypted data.
   * @param outputFile File, where the decrypted data goes.
   * 
   */
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

  /**
   * encryptAES256GCM
   *  
   * This function encrypts a file into a separate file using
   * AES and its GCM variation (which is at 256 bits, processes at each 128 bit).
   * Key size's relation is 256 bits.
   * The generalized encryption works with a plain text and a secret key which,
   * when used together in the cipher, give you a corresponding cipher text.
   * Before explaining the GCM variation, I need to explain its precursor, CTR.
   * CTR where it first encrypts the IV and uses it as a counter, where the after
   * each round it encrypts the counter to help add block noise.
   * It is also a stream cipher which encrypts each binary bit in the stream.
   * CTR does need a nonce, which is used to create the actual key stream.
   * The textbook (as its a little old), stated that CTR was the best choice,
   * however GCM is the newest in the line of AES encryptions to be be the 
   * top choice.
   * GCM (Galois Counter Mode) is an extension of CTR and also uses a counter 
   * for each block it encrypts, where after each round, the cipher gets 
   * the new counter and is XOR'ed with the plaintext.
   * This allows block ciphers to be turned into stream ciphers.
   * Java's crypto cipher allows the encryption cycle to be run in parallel,
   * as each block can be encrypted by itself.
   * Another notable factor of GCM is it contains an authentication tag.
   * As this is a 128 encryption standard, it uses a key length of 8 
   * 32-bit integers and goes through 14 rounds.
   * These round are similar to CTR, where the last round only computes 
   * the substitution, shift rows and adds the round key with counter.
   * The authentication tag is computed through using the counter and
   * hash function on the first block which then goes through the 
   * AES encryption process, and ties into the first and second AES rollovers 
   * (where you go through each 256 bit encryption processes, and each time
   * the counter is increased as well).
   * This allows us to authenticate each cipher text (256 bit portion) to make
   * sure it was actually computed correctly.
   * The main function used in this variation is the GHASH function:
   *  GHASH(H, A, C) = X m+n+1
   *    Where
   *        H:  E k(0^128)
   *        A: data that is not encrypted (not encrypted and authenticated)
   *        C: is the cipher text of size 128 blocks
   * To see the full  counter function go to: https://en.wikipedia.org/wiki/Galois/Counter_Mode .
   * 
   * The function implements use of Java's javax.crypto, which holds the actual
   * AES encryption and functions.
   * All that we do here is construct the cipher(algorithm) with settings for 
   * how and what we want the cipher to do.
   * The cipher takes in different parameters depending on the encryption,
   * and as we are using AES GCM, it takes in the the 
   * mode (are we encrypting or decrypting), the secret key, and the IV.
   * 
   * @param key SecretKey, secret key which acts as the key to lock the encryption.
   * @param iv bye[], the initialization vector. 
   * @param inputFile File, is the file that contains the data to be encrypted.
   * @param outputFile File, where the encrypted data goes.
   *  
   */
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

  /**
   * decryptAES256GCM
   *
   * This function decrypts a file into a separate file using
   * AES and its GCM variation.
   * The 'encryptAES256GCM' already discussed the implementation for encrypting,
   * and as for decrypting, it is simply doing the inverse encryption.
   * As AES uses symmetric cryptography, the same key is used for decryption.
   * So we take in the user's secret key and if it is correct, it will 
   * decrypt the data.
   * It first starts with getting the inverse round key 
   * (inverse of IV and XOR encrypted data),
   * then reverses the encryption process till completed.
   * The data is taken from the first file and decrypted, where the 
   * decrypted data is then saved into the second file.
   * As this is a stream cipher, each block is pushed to 128 bits.
   * 
   * @param key SecretKey, secret key which acts as the key to unlock the encryption.
   * @param iv IvParameterSpec, the initialization vector. 
   * @param inputFile File, is the file that contains the encrypted data.
   * @param outputFile File, where the decrypted data goes.
   * 
   */
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

  /**
   * encrypt
   *
   * This function is used to process the files in which the application passes,
   * and to call the necessary encryption functions.
   * This function is also called by the test class, but declares that the files
   * shouldn't be deleted.
   * The user's password sent to the secrete key generator function as well.
   * 
   * @param fileToEncrypt File, the file that will be encrypted.
   * @param userPassword String, user's password.
   * @param deleteFile boolean, must the files be deleted or not.
   * @param modeType String, what encryption variation will be used.
   * 
   */
  public static boolean encrypt(File fileToEncrypt, String userPassword, 
  boolean deleteFile, String modeType)
  throws Exception {

    // Creating the IV.
    byte[] IV = new byte[12];
    IvParameterSpec ivParameterSpec = generateIv();

    // Getting the user's secret key.
    SecretKey key = generateKey(256);
    try {

      key = getKeyFromPassword(userPassword);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
      return false;
    }
  
    // Getting the file name and creating the correct files based off of it.
    String holder = fileToEncrypt.getName();
    String[] fileNameCut = holder.split(".txt");

    String[] filePath = fileToEncrypt.getPath().split(fileNameCut[0]);

    File encryptedFile = new File(
    filePath[0] + fileNameCut[0] + "ENCRYPTED.txt");

    // What encryption variation are we using.
    if (modeType.equals("GCM")) {

      encryptAES256GCM(key, IV, fileToEncrypt, encryptedFile);
    }
    else {

      encryptAESCBC(key, ivParameterSpec, fileToEncrypt, encryptedFile);
    }
    
    // Deleting the file or not, depending on testing or not.
    if (deleteFile) {

      boolean deleted = fileToEncrypt.delete();

      if (!deleted) { 

        System.out.println("Error with deleting input file -> encrypt().");
        return false;
      } 
    }

    return true;
  }

  /**
   * decrypt
   *
   * This function is used to process the files in which the application passes,
   * and to call the necessary decryption functions.
   * This function is also called by the test class, but declares that the files
   * shouldn't be deleted.
   * The user's password sent to the secrete key generator function as well.
   * 
   * @param encryptedFile File, the file that will be decrypted.
   * @param userPassword String, user's password.
   * @param deleteFile boolean, must the files be deleted or not.
   * @param modeType String, what decryption variation will be used.
   * 
   */
  public static boolean decrypt(File encryptedFile, String userPassword, boolean deleteFile,
  boolean renameForTest, String modeType) 
  throws Exception {

    // Creating the IV.
    byte[] IV = new byte[12];
    IvParameterSpec ivParameterSpec = generateIv();

    // Getting the user's secret key.
    SecretKey key = generateKey(256);
    try {

      key = getKeyFromPassword(userPassword);
    }
    catch (Exception e) {

      System.out.println("Error12: " + e);
    }
    
    // Getting the file name and creating the correct files based off of it.
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

    // Running the decryption variation and saving test files.
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

    // Deleting the file or not, depending on testing or not.
    if (deleteFile) {

      boolean deleted = encryptedFile.delete();

      if (!deleted) { 

        System.out.println("Error with deleting input file -> decrypt().");
        return false;
      }
   }

    return true;
  }
  
}
