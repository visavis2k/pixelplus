/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

/**
 *
 * @author Lakmal
 */
import java.io.File;
import javax.swing.filechooser.*;



public class ImageFilter extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = FilterUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(FilterUtils.tiff) ||
                extension.equals(FilterUtils.tif) ||
                extension.equals(FilterUtils.gif) ||
                extension.equals(FilterUtils.jpeg) ||
                extension.equals(FilterUtils.jpg) ||
                extension.equals(FilterUtils.png)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Just Images";
    }
}
