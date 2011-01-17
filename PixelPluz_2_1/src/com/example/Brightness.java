/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 *
 * @author Lakmal
 */
public class Brightness {

    private static BufferedImage biSrc, biDest, bi;
    private static  RescaleOp rescale;
    static float scaleFactor = 1.0f;
    static float offset = 10;
    boolean brighten, contrastInc;

    /*This method takes a  */
    public static BufferedImage changeBrightness(BufferedImage image, boolean brighten) {
        biSrc = image;
        if (brighten) {
            if (offset < 255) {
                offset = offset + 5.0f;
            }
        } else {
            if (offset > 0) {
                offset = offset - 5.0f;
            }
        }
       return rescale(biSrc);
    }

    public static BufferedImage rescale(BufferedImage img) {
        rescale = new RescaleOp(scaleFactor, offset, null);
        System.out.println("Scale="+scaleFactor+"offset"+offset);
        rescale.filter(img, img);
        return img;
    }

    public static void setIntialBrightness(){
        offset=10;
        scaleFactor=1.0f;
    }
}
