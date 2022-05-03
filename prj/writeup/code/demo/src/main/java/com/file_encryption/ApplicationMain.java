package com.file_encryption;


//import com.file_encryption.Encryption;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
 
public class ApplicationMain extends JDialog {

  
  public static void main(final String[] args) {

    new ApplicationMain();
  }

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

    
    // Get what file the user wants.
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    int result = fileChooser.showOpenDialog(this);

    File fileToRead = new File("holderFile.txt");

    if (result == JFileChooser.APPROVE_OPTION) {

      fileToRead = fileChooser.getSelectedFile();
    }
    else if (result == JFileChooser.CANCEL_OPTION) {

      closeApplication();
    }


    // Get the password from the user.
    String password = "";

    Encryption encryptionClass = new Encryption();

    if (optionTaken == 0) { 

      password = passwordIntake(true);

      System.out.println("To Path: " + fileToRead.toPath());
      System.out.println("Absolute: " + fileToRead.getAbsolutePath());
      // Encrypt here
      try {

        encryptionClass.encrypt(fileToRead, password, true);
      }
      catch (Exception e) {

        System.out.println("Error: " + e);
      }
    }
    else {

      password = passwordIntake(false);
      // Decrypt here
      try {

        encryptionClass.decrypt(fileToRead, password, true, false);
      }
      catch (Exception e) {

        System.out.println("Error: " + e);
      }
    }


    // Get everything inside the file.
    // ArrayList<String> textInsideFile = getFileInsides(fileToRead);

    /*for (String singleLine : textInsideFile) {
      
      System.out.println(singleLine);
    }
    */

    closeApplication();
  }



  public static void closeApplication() {
   
    System.exit(0);
  }

  public static String passwordIntake(boolean isNewPassword) {

    String password = "";

    while (password.length() == 0) {

      // Get password
      password = JOptionPane.showInputDialog("Enter your password please (can't be null)?");

      if (password == null) {

        closeApplication();
      }
    }

    if (isNewPassword) {

      JOptionPane.showMessageDialog(null, "Your Password is: '" + password + "'\n Don't forget it!");
    }

    return password;
  }

  public static ArrayList<String> getFileInsides(File fileToRead) {

    ArrayList<String> textInsideFile = new ArrayList<String>();

    try {

      Scanner myReader = new Scanner(fileToRead);

      while (myReader.hasNextLine()) {

        textInsideFile.add(myReader.nextLine());
      }

      myReader.close();
    }
    catch (FileNotFoundException e) {}

    return textInsideFile;
  }

}
