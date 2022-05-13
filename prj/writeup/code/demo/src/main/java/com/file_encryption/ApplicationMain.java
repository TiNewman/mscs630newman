/**
 * file: ApplicationMain.java
 * author: Titan Newman 
 * course: MSCS 630
 * assignment: Final Project
 * due date: May 15th, 2022
 * version: 1.0
 *
 * This file contains the UI and calls to the Encryption.java file.
 */
package com.file_encryption;

import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
 
/**
 * ApplicationMain
 *
 * This class contains the code for the UI of the project. 
 * In here we go through different steps to ask the user what they want to do.
 * We start with asking if they want to Encrypt or Decrypt a file,
 * then ask what file they would like to use, then we as for a password to be
 * used on the file.
 * While using Java's UI tools, we have to make sure that users' don't cancel
 * or quit.
 * If the they do, we take check of it and close the application without any errors.
 * After we go through the process (stated above), we call Encryption.java to 
 * do the actual encryption and decryption.
 * We do not save passwords or read the files once finished.
 */
public class ApplicationMain extends JDialog {

  /**
   * main
   *
   * This function starts the actual application.
   * 
   * @param args
   */
  public static void main(final String[] args) {

    new ApplicationMain();
  }

  /**
   * ApplicationMain
   *
   * This function is where everything is called and user's interact.
   * We get the different user inputs in here and use Java's UI.
   * We also call 'encrypt()' or 'decrypt()' as per the user's request.
   */
  public ApplicationMain() {

    // Take the user's input.
    Object[] choices = {"Encrypt a file", "Decrypt a file"};
    Object defaultChoice = choices[0];
    int optionTaken = JOptionPane.showOptionDialog(this, 
      "What would you like to do?",
      "Decision Time",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      null,
      choices,
      defaultChoice);

    if (optionTaken == -1) {

      closeApplication();
    }

    // Take the user's input for encryption/decryption type.
    Object[] choices1 = {"AES GCM Mode 256", "AES CBC 128"};
    Object defaultChoice1 = choices1[0];
    int optionTaken1 = JOptionPane.showOptionDialog(this, 
      "What Encryption or Decryption type would you like to use?",
      "Decision Time",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      null,
      choices1,
      defaultChoice1);

    String toSend = "";

    if (optionTaken1 == -1) {

      closeApplication();
    }
    else {

      toSend =  (optionTaken1 == 1 ? "CBC" : "GCM");
    }

    // Get what file the user wants.
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

    int result = fileChooser.showOpenDialog(this);

    File fileToRead = new File("holderFile.txt");

    if (result == JFileChooser.APPROVE_OPTION) {

      fileToRead = fileChooser.getSelectedFile();
    }
    else if (result == JFileChooser.CANCEL_OPTION) {

      // Did the user quit/cancel.
      closeApplication();
    }

    // Get the password from the user.
    String password = "";

    // Encrypt or Decrypt depending on the user's input.
    if (optionTaken == 0) { 

      password = passwordIntake(true);

      // Encrypt here
      try {

        Encryption.encrypt(fileToRead, password, true, toSend);
      }
      catch (Exception e) {

        System.out.println("Error: " + e);
      }
    }
    else {

      password = passwordIntake(false);

      // Decrypt here
      try {

        Encryption.decrypt(fileToRead, password, true, false, toSend);
      }
      catch (Exception e) {

        System.out.println("Error: " + e);
        JOptionPane.showMessageDialog(null, "You entered the wrong password!\n");
      }
    }

    closeApplication();
  }

  /**
   * closeApplication
   *
   * This function is used to close the application safely (without errors).
   */
  public static void closeApplication() {
   
    System.exit(0);
  }

  /**
   * passwordIntake
   *
   * This function is used to take in the user's password.
   * Depending if the password is for encrypting or decrypting,
   * the prompts will be different.
   * 
   * @param isNewPassword boolean, states if we are encrypting and this is 
   * a new password. 
   *  
   * @return String, is the user inputted password.
   */
  public static String passwordIntake(boolean isNewPassword) {

    String password = "";

    while (password.length() == 0) {

      // Get password
      password = JOptionPane.showInputDialog("Enter your password please (can't be null)?");

      if (password == null) {

        // Did the user quit/cancel.
        closeApplication();
      }
    }

    // This is a new password, so display it and show the user.
    if (isNewPassword) {

      JOptionPane.showMessageDialog(null, "Your Password is: '" + password + "'\n Don't forget it!");
    }

    return password;
  }

}
