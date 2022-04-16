package com.file_encryption;

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

    String filePath = "";
    File fileToRead = new File("holderFile.txt");

    if (result == JFileChooser.APPROVE_OPTION) {
      fileToRead = fileChooser.getSelectedFile();
      filePath = fileChooser.getSelectedFile().getAbsolutePath();
    }


    // Get the password from the user.
    String password = "";

    if (optionTaken == 0) { 

      password = passwordIntake(true);
      // Encrypt here
    }
    else {

      password = passwordIntake(false);
      // Decrypt here
    }


    // Get everything inside the file.
    ArrayList<String> textInsideFile = getFileInsides(fileToRead);

    /*for (String singleLine : textInsideFile) {
      
      System.out.println(singleLine);
    }
    */

    closeApplication();
  }



  public void closeApplication() {
   
    System.exit(0);
  }

  public static String passwordIntake(boolean isNewPassword) {

    String password = "";

    while (password.length() == 0) {

      // Get password
      password = JOptionPane.showInputDialog("Enter your password please (can't be null)?");
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
