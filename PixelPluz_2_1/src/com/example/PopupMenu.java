/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Lakmal
 */
public class PopupMenu extends JPopupMenu {

    private String[] menuItems;
    private MainFrame frame;
    private String path;

 /*
 Creates the Popup menu which is placed on the Shortcut Panel
 */
    public PopupMenu(String[] menuItems, MainFrame frame, String path) {
        this.frame = frame;
        this.menuItems = menuItems;
        this.path = path;
        intializePopupMenu();
    }

    private void intializePopupMenu() {
        for (String s : menuItems) {
            add(new MenuItem(s,this));
        }
    }

    public MainFrame getFrame() {
        return frame;
    }

    public String getPath() {
        return path;
    }
}

class MenuItem extends JMenuItem {

    private PopupMenu pMenu;

    public MenuItem(String text, final PopupMenu pMenu) {
        super(text);
        this.pMenu = pMenu;
        ActionListener menuListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
               if(event.getActionCommand().equals("Close")){
                    try {
                        pMenu.getFrame().ShortCutPanelChange(pMenu.getPath(), true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
               }
            }
        };
        addActionListener(menuListener);
    }
}
