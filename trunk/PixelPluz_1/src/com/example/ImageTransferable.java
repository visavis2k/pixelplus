/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 *
 * @author Lakmal
 */
public class ImageTransferable implements Transferable, ClipboardOwner {

    private Object data;
    private DataFlavor flavor;

    public ImageTransferable(Object data, DataFlavor flavor) {
        this.data = data;
        this.flavor = flavor;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{flavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return this.flavor.equals(flavor);
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (this.flavor.equals(flavor)) {
            return data;
        }
        return null;
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
