/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.swing.JPanel;

/**
 *
 * @author Lakmal
 */
public class Brightness {

    private  Image displayImage;
    private  BufferedImage biSrc, biDest, bi;
    private  Graphics2D big;
    private  RescaleOp rescale;
    float scaleFactor = 1.0f;
    float offset = 10;
    boolean brighten, contrastInc;

/*This method takes a  */
    public void createBufferedImages() {
        biSrc = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        big = biSrc.createGraphics();
        big.drawImage(displayImage, 0, 0, new JPanel());

        biDest = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_RGB);
        bi = biSrc;
    }
}
