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
    new SecureRandom().nextBytes(iv);

    return new IvParameterSpec(iv);
  }

  public static void encryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
    File inputFile, File outputFile) throws IOException, NoSuchPaddingException,
    NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException {
    
    Cipher cipher = Cipher.getInstance(algorithm);
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

  public static void decryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
    File encryptedFile, File decryptedFile) throws IOException, NoSuchPaddingException,
    NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException {

    Cipher cipher = Cipher.getInstance(algorithm);
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

  public static void givenFile_whenEncrypt_thenSuccess() throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException,
  BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

    // Code holder
    SecretKey key = generateKey(256);
    try {

      key = getKeyFromPassword("titan");
    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }

    String algorithm = "AES/CBC/PKCS5Padding";
    IvParameterSpec ivParameterSpec = generateIv();
    File inputFile = Paths.get(
      "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/main/java/com/file_encryption/easyFile.txt")
      .toFile();
    File encryptedFile = new File(
      "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/main/java/com/file_encryption/encryptedFile.txt");
    File decryptedFile = new File(
      "C:/Users/MASTE/Documents/GitHub/mscs630newman/prj/writeup/code/demo/src/main/java/com/file_encryption/decryptedFile.txt");

    encryptFile(algorithm, key, ivParameterSpec, inputFile, encryptedFile);
    decryptFile(algorithm, key, ivParameterSpec, encryptedFile, decryptedFile);

    encryptedFile.delete();

    }

  public static void main(String[] args) {
    System.out.println("Hello from the Java Main Function!");

    try {
      givenFile_whenEncrypt_thenSuccess();

    }
    catch (Exception e) {

      System.out.println("Error: " + e);
    }
    
  }
  
}
