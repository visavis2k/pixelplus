/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.sun.star.uno.XComponentContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Lakmal
 */
public class Controller {

    private MainFrame frame;
    private PrintWriter pw;
    private BufferedReader br;
    private boolean isFirstTime = true;
    private Timer timer;
    private boolean isPictures;
    private File file;
    private boolean isMouseClicked;
    private ImageLoader imageLoader;

    public Controller(MainFrame frame, ImageLoader imageLoader) throws IOException {
        this.imageLoader = imageLoader;
        file = this.imageLoader.getTextFile();
        //file = new File("shortcuts");
        //file.createNewFile();
        this.frame = frame;

    }

    /*This method takes Folder path and all the filenames of that
    folder as parameters and check for image files inside that folder */
    public void checkImageFiles(final String[] children, final String path) throws InterruptedException {
        isPictures = false;
        if (children.length == 0) {
            frame.createBox();
            JOptionPane.showMessageDialog(null, "No Files Found", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            frame.createBox();
            timer = new Timer(1000, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String filename = "";
                    String file = "";
                    //frame.setLoadImage();
                    for (int i = 0; i < children.length; i++) {
                        // Get filename of file or directory String filename = children[i];
                        filename = path + "\\" + children[i];

                        if (checkForImages(filename)) {
                            frame.setThumbImages(filename);
                            isPictures = true;
                            file = filename;
                        }

                    }
                    frame.setTitle("PixelPluz version 1.0");
                    if (isPictures) {
                        try {
                            frame.setLoadedImage(file);
                            frame.setMainImageFile(new File(file));
                        } catch (IOException ex) {
                            System.out.println("Error While parsing first IMAGE to SCRREN" + ex);
                        }
                    }
                    timer.stop();
                    if (!isFirstTime) {
                        if (!isPictures && isMouseClicked) {
                            isMouseClicked = false;
                            JOptionPane.showMessageDialog(null, "No Pictures Found in " + path + " folder", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
            });

            timer.setRepeats(false);
            timer.start();

        }
    }

    /*When a new File Folder is added to the panel that path is written to the
    ShortCut File using this method. */
    public void writeToShortCutFile(ArrayList<String> files) throws IOException {
        FileOutputStream out = null;
        out = new FileOutputStream(file);
        out.write("".getBytes());
        out.flush();
        out.close();
        PrintWriter pwe = new PrintWriter(file);


        for (String s : files) {
            System.out.println(s);
            pwe.println(s);
            pwe.flush();


        }
        pwe.close();


    }

    /*Flag saying there are pictures in the respective folder*/
    public void setIsPictures(boolean flag) {
        isPictures = flag;
    }

    /*Return true if a file path is refering to an image*/
    public boolean checkForImages(String path) {
        return Pattern.matches(".*\\.(jpg|jpeg|gif|png|bmp)", path);
    }

    /*Load Shortcut floders from the shortcut files */
    public ArrayList<String> loadShortcutFiles() throws IOException {
        ArrayList<String> shortcuts = new ArrayList<String>();
        br = new BufferedReader(new FileReader(file));


        try {
            String path = br.readLine();


            while (path != null) {
                shortcuts.add(path);
                path = br.readLine();


            }

        } catch (FileNotFoundException ex) {
        } finally {
            br.close();
            System.gc();


        }
        System.out.println(shortcuts);


        return shortcuts;



    }
    /*Flag if folder loading happens for the first time */

    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;


    }
    /*Flag the mouse clicks*/

    public void setIsMouseClicked(boolean isMouseClicked) {
        this.isMouseClicked = isMouseClicked;

    }
}
