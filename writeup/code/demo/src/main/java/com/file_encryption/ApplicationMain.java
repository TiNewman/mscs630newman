package com.file_encryption;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.plaf.FileChooserUI;
 
public class ApplicationMain extends JDialog {

  public ApplicationMain() {

    //Create a frame
    //Frame f = new Frame();
    //f.setSize(500, 300);
     
    //Prepare font
    //Font font = new Font( "Times New Roman", Font.PLAIN, 22 );
     
    //Write something
    //Label label = new Label("Starting Title");
    //label.setForeground(Color.RED);
    //label.setFont(font);
    //f.add(label);

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    int result = fileChooser.showOpenDialog(this);

    String filePath = "";

    if (result == JFileChooser.APPROVE_OPTION) {

      filePath = fileChooser.getSelectedFile().getAbsolutePath();
    }

    System.out.println("Path+File: " + filePath);
 
    //Make visible
    /*f.setVisible(true);
    f.addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e) {

        System.exit(0);
      }
    });
    */

  }

  public static void main(final String[] args) {

    new ApplicationMain();
  }
}