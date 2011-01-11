/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

/**
 *
 * @author Lakmal
 */
import java.awt.Dimension;
import java.io.File;
import java.util.Collections;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class FileTree {
    private JScrollPane scrollpane;
    private JTree tree;
    private final static String ICON_PATH="image/folder.gif";
    private static Icon leafIcon = new ImageIcon(ICON_PATH);

    public FileTree(final Controller controller, MainFrame frame, File dir) {
        tree = new JTree(addNodes(null, dir));
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setLeafIcon(leafIcon);
        renderer.setClosedIcon(leafIcon);
        renderer.setOpenIcon(leafIcon);

        // Add a listener
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                try {
                    controller.checkImageFiles(new File(node.toString()).list(), node.toString());
                } catch (InterruptedException ex) {
                    System.out.println("Error Getting slection Jtree");
                }
            }
        });

        // Lastly, put the JTree into a JScrollPane.
        scrollpane = new JScrollPane();
        scrollpane.getViewport().add(tree);

    }

    public JScrollPane getScrollPane() {
        return scrollpane;
    }

    /** Add nodes from under "dir" into curTop. Highly recursive. */
    DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
        String curPath = dir.getPath();
        DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
        if (curTop != null) { // should only be null at root
            curTop.add(curDir);
        }
        Vector ol = new Vector();
        String[] tmp = dir.list();
        for (int i = 0; i < tmp.length; i++) {
            ol.addElement(tmp[i]);
        }
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        Vector files = new Vector();
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < ol.size(); i++) {
            String thisObject = (String) ol.elementAt(i);
            String newPath;
            if (curPath.equals(".")) {
                newPath = thisObject;
            } else {
                newPath = curPath + File.separator + thisObject;
            }
            if ((f = new File(newPath)).isDirectory()) {
                addNodes(curDir, f);
            } else {
                // files.addElement(thisObject);
            }
        }

        for (int fnum = 0; fnum < files.size(); fnum++) {
            curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
        }
        return curDir;
    }

    public Dimension getMinimumSize() {
        return new Dimension(200, 400);
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 400);
    }
}
