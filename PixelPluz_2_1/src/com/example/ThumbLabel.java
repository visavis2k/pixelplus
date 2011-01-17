/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Lakmal
 */
public class ThumbLabel extends JLabel implements MouseListener {

    private File file;
    private MainFrame frame;
    private BufferedImage image = null;

    public ThumbLabel(final File file, final MainFrame frame) {
        this.frame = frame;
        this.file = file;
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                frame.confirmSave();
                sendMainImage();
                frame.setMainImageFile(file);
                frame.setIsMainImageSaved(true);
            }
        });
    }
    /*Make a JLabel including a thumbnail image as the Label Icon and returns */

    public JLabel getThumbs() {
        try {
            ImageIO.setUseCache(true);
            image = ImageIO.read(file);
            image.flush();
            BufferedImage resized = image;
            this.setIcon(new ImageIcon(this.resize(resized, 80, 80)));
        } catch (IOException ex) {
            System.out.println("Error reading Image");
        }
        return this;
    }

    /*Send the request to the main frame to change main image on Canvas*/
    public void sendMainImage() {
        frame.setMainImageFile(file);
        frame.setMainImage(image, true);
        System.out.println("Mouse pressed");
    }

    /*
    Resize the loaded image to get the thumbnail
     */
    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
