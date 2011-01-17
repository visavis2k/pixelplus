/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.awt.Frame;
import java.io.File;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.swing.JFrame;

/**
 *
 * @author acer
 */
public class WebCamViewer extends JFrame {

    boolean saved = false;
    File file = null;
    public Player _player = null;

    WebCamViewer() {
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        String mediaFile = "vfw:Micrsoft WDM Image Capture (Win32):0";
        try {
            MediaLocator mlr = new MediaLocator(mediaFile);

            _player = Manager.createRealizedPlayer(mlr);

            if (_player.getVisualComponent() != null) {
                this.add(_player.getVisualComponent());
                _player.start();
                setExtendedState(Frame.MAXIMIZED_BOTH);
                setUndecorated(true);
            }
        } catch (Exception e) {
            System.err.println("Got exception " + e);
        }

    }

    public void release() {
        _player.stop();
        _player.deallocate();
        _player.close();
        dispose();
    }

    public static void main(String[] args) {
        new WebCamViewer().show();
    }
}
