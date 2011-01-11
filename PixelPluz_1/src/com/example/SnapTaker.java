/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Lakmal
 */
public class SnapTaker {

     private BufferedImage image;

      public BufferedImage takeScreenShot(int time) {
        try {
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException ex) {
                System.out.println("Sleeping Error!");
            }
            Robot robot = new Robot();
            Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            image = robot.createScreenCapture(captureSize);
            return image;
        } catch (AWTException ex) {
            JOptionPane.showMessageDialog(null, "Failed To Capture Screen!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
