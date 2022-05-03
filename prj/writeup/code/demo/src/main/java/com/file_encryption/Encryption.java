// https://github.com/eugenp/tutorials

// With modifications by myself.
package com.file_encryption;


import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.BadPaddingException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.IllegalBlockSizeException;
import java.nio.file.Paths;
 

public class Encryption {

  public static final String SALT = "thisIsADemo";
  public static final String Algorithm = "AES/CBC/PKCS5Padding";

  public Encryption() {}


  public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {

    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(n);

    SecretKey key = keyGenerator.generateKey();

    return key;
  }

public static SecretKey getKeyFromPassword(String password)
    throws NoSuchAlgorithmException, InvalidKeySpecException {

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), 65536, 256);
    SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
      .getEncoded(), "AES");

    return secret;
  }

  public static IvParameterSpec generateIv() {

    byte[] iv = new byte[16];
    return new IvParameterSpec(iv);
  }

  public static void encryptFileProcess(SecretKey key, IvParameterSpec iv,
    File inputFile, File outputFile) throws IOException, NoSuchPaddingException,
    NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException {
    
    Cipher cipher = Cipher.getInstance(Algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    FileInputStream inputStream = new FileInputStream(inputFile);
    FileOutputStream outputStream = new FileOutputStream(outputFile);

    byte[] buffer = new byte[64];
    int bytesRead;

    while ((bytesRead = inputStream.read(buffer)) != -1) {

      byte[] output = cipher.update(buffer, 0, bytesRead);

      if (output != null) {

        outputStream.write(output);
      }
    }

    byte[] outputBytes = cipher.doFinal();

    if (outputBytes != null) {

      outputStream.write(outputBytes);
    }

    inputStream.close();
    outputStream.close();
  }

  public static void decryptFileProcess(SecretKey key, IvParameterSpec iv,
    File encryptedFile, File decryptedFile) throws IOException, NoSuchPaddingException,
    NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException {

    Cipher cipher = Cipher.getInstance(Algorithm);
    cipher.init(Cipher.DECRYPT_MODE, key, iv);

    FileInputStream inputStream = new FileInputStream(encryptedFile);
    FileOutputStream outputStream = new FileOutputStream(decryptedFile);

    byte[] buffer = new byte[64];
    int bytesRead;

    while ((bytesRead = inputStream.read(buffer)) != -1) {

      byte[] output = cipher.update(buffer, 0, bytesRead);

      if (output != null) {

        outputStream.write(output);
      }
    }

    byte[] output = cipher.doFinal();

    if (output != null) {

      outputStream.write(output);
    }

    inputStream.close();
    outputStream.close();
  }


  public static boolean encrypt(File fileToEncrypt, String userPassword, boolean deleteFile)
  throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, 
  InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, 
  NoSuchPaddingException {

    IvParameterSpec ivParameterSpec = generateIv();

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
    
    encryptFileProcess(key, ivParameterSpec, fileToEncrypt, encryptedFile);

    if (deleteFile) {

      boolean deleted = fileToEncrypt.delete();

      if (!deleted) { 

        System.out.println("Error with deleting input file -> encrypt().");
        return false;
      } 
    }

    return true;
  }

  public static boolean decrypt(File encryptedFile, String userPassword, boolean deleteFile, boolean renameForTest) 
  throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, 
  InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, 
  NoSuchPaddingException {

    // Code holder
    SecretKey key = generateKey(256);
    try {

      key = getKeyFromPassword(userPassword);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    IvParameterSpec ivParameterSpec = generateIv();

    String holder = encryptedFile.getName();
    String[] fileNameCut = holder.split("ENCRYPTED");

    String[] filePath = encryptedFile.getPath().split(fileNameCut[0]);

    if (renameForTest) {

      File decryptedFile = new File(
      filePath[0] + fileNameCut[0] + "DECRYPTED" + fileNameCut[1]);

      decryptFileProcess(key, ivParameterSpec, encryptedFile, decryptedFile);
    }
    else {

      File decryptedFile = new File(
      filePath[0] + fileNameCut[0] + fileNameCut[1]);

      decryptFileProcess(key, ivParameterSpec, encryptedFile, decryptedFile);
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


  public static void main(String[] args) {

    System.out.println("Hello");
    /*try {
      encrypt("", "password1", true);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }
    
    try {
      decrypt("", "password1", true, false);
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }
    */
    
    
  }
  
}