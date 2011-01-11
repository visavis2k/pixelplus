/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *
 * @author Lakmal
 */
public class Rotation {

    /*
    Get a BufferedImage and a double value as parameters and returns a
    bufferedImage which is rotated in the given angle(+ angles--->right rotation)
     */
    public static BufferedImage tilt(BufferedImage image, double angle) {
        GraphicsConfiguration gc = getDefaultConfiguration();
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
        int transparency = image.getColorModel().getTransparency();
        BufferedImage result = gc.createCompatibleImage(neww, newh, transparency);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        MainFrame.setTempMainImage(result);
        return result;
    }

    public static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    /*
    Get a BufferedImage and a double value as parameters and returns a
    bufferedImage which is rotated in the given angle(+ angles--->right rotation)
     */
    public static BufferedImage rotateRadians(BufferedImage img, double radians) {
        int iw = img.getWidth();
        int ih = img.getHeight();
        Dimension dim = new Dimension(iw + (int) (iw / 3), ih + (int) (ih / 3));

        int w = dim.width;
        int h = dim.height;
        int x = (w / 2) - (iw / 2);
        int y = (h / 2) - (ih / 2);

        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        // set some rendering hints for better looking images
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform xform = new AffineTransform();
        xform.rotate(radians, w / 2, h / 2);
        xform.translate(x, y);
        g2d.drawImage(img, xform, null);
        g2d.dispose();
        MainFrame.setTempMainImage(bi);
        return bi;
    }

    public static BufferedImage flip(BufferedImage image, boolean vert) {
        AffineTransform tx = null;
        AffineTransformOp op = null;
        if (vert) {
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -image.getHeight(null));
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            return image;
        } else {
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(null), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
            return image;
        }
    }
}
