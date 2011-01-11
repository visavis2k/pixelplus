/*
 * FlickrSearch.java
 *
 * Copyright 2007 Sun Microsystems, Inc. ALL RIGHTS RESERVED Use of
 * this software is authorized pursuant to the terms of the license
 * found at http://developers.sun.com/berkeley_license.html.
 */
package com.example;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import java.net.URL;

/**
 * @author John O'Conner
 */
public class ImageRetriever extends SwingWorker<Icon, Void> {

    private Icon icon;
    private BufferedImage downloadedImage;
    private ImageSearchFrame frame;
    

    public ImageRetriever(JLabel lblImage, String strImageUrl,ImageSearchFrame frame) {
        this.strImageUrl = strImageUrl;
        this.frame=frame;
        this.lblImage = lblImage;
    }

    public Icon getIcon() {
        return icon;
    }

    @Override
    protected Icon doInBackground() throws Exception {
        Icon icon = retrieveImage(strImageUrl);
        return icon;
    }

    private Icon retrieveImage(String strImageUrl) throws MalformedURLException, IOException {

        InputStream is = null;
        URL imgUrl = null;
        imgUrl = new URL(strImageUrl);
        is = imgUrl.openStream();
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix("jpg");

        ImageReader reader = it.next();
        reader.setInput(iis);
        reader.addIIOReadProgressListener(new IIOReadProgressListener() {

            public void sequenceStarted(ImageReader source, int minIndex) {
            }

            public void sequenceComplete(ImageReader source) {
            }

            public void imageStarted(ImageReader source, int imageIndex) {
            }

            public void imageProgress(ImageReader source, float percentageDone) {
                setProgress((int) percentageDone);

            }

            public void imageComplete(ImageReader source) {
                setProgress(100);
            }

            public void thumbnailStarted(ImageReader source, int imageIndex, int thumbnailIndex) {
            }

            public void thumbnailProgress(ImageReader source, float percentageDone) {
            }

            public void thumbnailComplete(ImageReader source) {
            }

            public void readAborted(ImageReader source) {
            }
        });
        Image image = reader.read(0);
        downloadedImage=toBufferedImage(image);
        icon = new ImageIcon(image);
        frame.retrieveImage(downloadedImage);
        return icon;
    }

    @Override
    protected void done() {
        icon = null;
        String text = null;
        try {
            icon = get();
        } catch (Exception ignore) {
            ignore.printStackTrace();
            text = "Image unavailalbe";
        }
        lblImage.setIcon(icon);
        lblImage.setText(text);
    }
    private String strImageUrl;
    private JLabel lblImage;

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha == true) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        } //No screen

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha == true) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            return ((BufferedImage) image).getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        return pg.getColorModel().hasAlpha();
    }
}
