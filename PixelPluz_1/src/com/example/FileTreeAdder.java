package com.example;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class FileTreeAdder {

    protected JTree m_tree;
    protected DefaultTreeModel m_model;
    private Controller controller;
    private MainFrame frame;
    private Icon leafIcon;
    private String treePath;

    public FileTreeAdder(Controller controller, MainFrame frame) {
        this.controller = controller;
        this.frame = frame;
        leafIcon = frame.getImageLoader().getFolderIcon();
        UIManager.put("Tree.leafIcon", leafIcon);
        UIManager.put("Tree.openIcon", leafIcon);
        UIManager.put("Tree.closedIcon", leafIcon);
    }

    public JTree getFileTree(String path) {
        treePath = path;
        String names[] = path.split("\\\\");
        System.out.println(names[names.length - 1]);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                new IconData(leafIcon, null, names[names.length - 1]));

        DefaultMutableTreeNode node;
        File[] roots = new File(path).listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (int k = 0; k < roots.length; k++) {
            node = new DefaultMutableTreeNode(new IconData(null,
                    null, new FileNode(roots[k], leafIcon)));
            top.add(node);
            node.add(new DefaultMutableTreeNode(new Boolean(true)));
        }

        m_model = new DefaultTreeModel(top);
        m_tree = new JTree(m_model);
        m_tree.setBorder(BorderFactory.createEtchedBorder());
        m_tree.putClientProperty("JTree.lineStyle", "Angled");

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setBackgroundNonSelectionColor(Color.red);
        m_tree.setCellRenderer(renderer);

        m_tree.addTreeExpansionListener(new DirExpansionListener());

        m_tree.addTreeSelectionListener(new DirSelectionListener(path));

        m_tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        m_tree.setShowsRootHandles(true);
        m_tree.setEditable(false);
        m_tree.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent Me) {
                if (Me.isPopupTrigger()) {
                    new PopupMenu(new String[]{"Close"}, frame, treePath).show(Me.getComponent(), Me.getX(), Me.getY());
                }
            }
        });
        m_tree.setBackground(new Color(191, 219, 255));
        return m_tree;
    }

    DefaultMutableTreeNode getTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) (path.getLastPathComponent());
    }

    FileNode getFileNode(DefaultMutableTreeNode node) {
        if (node == null) {
            return null;
        }
        Object obj = node.getUserObject();
        if (obj instanceof IconData) {
            obj = ((IconData) obj).getObject();
        }
        if (obj instanceof FileNode) {
            return (FileNode) obj;
        } else {
            return null;
        }
    }

    // Make sure expansion is threaded and updating the tree model
    // only occurs within the event dispatching thread.
    class DirExpansionListener implements TreeExpansionListener {

        public void treeExpanded(TreeExpansionEvent event) {
            final DefaultMutableTreeNode node = getTreeNode(
                    event.getPath());
            final FileNode fnode = getFileNode(node);

            Thread runner = new Thread() {

                public void run() {
                    if (fnode != null && fnode.expand(node)) {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                m_model.reload(node);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                }
            };
            runner.start();
        }

        public void treeCollapsed(TreeExpansionEvent event) {
        }
    }

    class DirSelectionListener
            implements TreeSelectionListener {
            String path;
        public DirSelectionListener(String path) {
            this.path=path;
        }

        public void valueChanged(TreeSelectionEvent event) {
            DefaultMutableTreeNode node = getTreeNode(
                    event.getPath());
            FileNode fnode = getFileNode(node);
            if (fnode != null) {
                System.out.println(fnode.getFile().getAbsolutePath());
                try {
                    controller.setIsMouseClicked(true);
                    controller.setIsFirstTime(false);
                    controller.checkImageFiles(fnode.getFile().list(), fnode.getFile().getAbsolutePath());
                } catch (InterruptedException ex) {
                    System.out.println("Interrupted On reading images");
                }
            } else {
                try {
                    controller.checkImageFiles(new File(path).list(), new File(path).getAbsolutePath());
                } catch (InterruptedException ex) {
                    System.out.println("Root Folder");
                }
            }
        }
    }
}

class IconCellRenderer
        extends JLabel
        implements TreeCellRenderer {

    protected Color m_textSelectionColor;
    protected Color m_textNonSelectionColor;
    protected Color m_bkSelectionColor;
    protected Color m_bkNonSelectionColor;
    protected Color m_borderSelectionColor;
    protected boolean m_selected;

    public IconCellRenderer() {
        super();
        m_textSelectionColor = UIManager.getColor(
                "Tree.selectionForeground");
        m_textNonSelectionColor = UIManager.getColor(
                "Tree.textForeground");
        m_bkSelectionColor = UIManager.getColor(
                "Tree.selectionBackground");
        m_bkNonSelectionColor = UIManager.getColor(
                "Tree.textBackground");
        m_borderSelectionColor = UIManager.getColor(
                "Tree.selectionBorderColor");
        setOpaque(false);
    }

    public Component getTreeCellRendererComponent(JTree tree,
            Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        setText(obj.toString());

        if (obj instanceof Boolean) {
            setText("Retrieving data...");
        }

        if (obj instanceof IconData) {
            IconData idata = (IconData) obj;
            if (expanded) {
                setIcon(idata.getExpandedIcon());
            } else {
                setIcon(idata.getIcon());
            }
        } else {
            setIcon(null);
        }

        setFont(tree.getFont());
        setForeground(sel ? m_textSelectionColor
                : m_textNonSelectionColor);
        setBackground(sel ? m_bkSelectionColor
                : m_bkNonSelectionColor);
        m_selected = sel;
        return this;
    }

    public void paintComponent(Graphics g) {
        Color bColor = getBackground();
        Icon icon = getIcon();

        g.setColor(bColor);
        int offset = 0;
        if (icon != null && getText() != null) {
            offset = (icon.getIconWidth() + getIconTextGap());
        }
        g.fillRect(offset, 0, getWidth() - 1 - offset,
                getHeight() - 1);

        if (m_selected) {
            g.setColor(m_borderSelectionColor);
            g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        }
        super.paintComponent(g);
    }
}

class IconData {

    protected Icon m_icon;
    protected Icon m_expandedIcon;
    protected Object m_data;

    public IconData(Icon icon, Object data) {
        m_icon = icon;
        m_expandedIcon = null;
        m_data = data;
    }

    public IconData(Icon icon, Icon expandedIcon, Object data) {
        m_icon = icon;
        m_expandedIcon = expandedIcon;
        m_data = data;
    }

    public Icon getIcon() {
        return m_icon;
    }

    public Icon getExpandedIcon() {
        return m_expandedIcon != null ? m_expandedIcon : m_icon;
    }

    public Object getObject() {
        return m_data;
    }

    public String toString() {
        return m_data.toString();
    }
}

class FileNode {

    protected File m_file;
    private Icon leafIcon;

    public FileNode(File file, Icon leafIcon) {
        m_file = file;
        this.leafIcon = leafIcon;

    }

    public File getFile() {
        return m_file;
    }

    public String toString() {
        return m_file.getName().length() > 0 ? m_file.getName()
                : m_file.getPath();
    }

    public boolean expand(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode flag =
                (DefaultMutableTreeNode) parent.getFirstChild();
        if (flag == null) // No flag
        {
            return false;
        }
        Object obj = flag.getUserObject();
        if (!(obj instanceof Boolean)) {
            return false;      // Already expanded
        }
        parent.removeAllChildren();  // Remove Flag

        File[] files = listFiles();
        if (files == null) {
            return true;
        }

        Vector v = new Vector();

        for (int k = 0; k < files.length; k++) {
            File f = files[k];


            FileNode newNode = new FileNode(f, leafIcon);
            if (!newNode.hasSubDirs()) {
                continue;
            }

            boolean isAdded = false;
            for (int i = 0; i < v.size(); i++) {
                FileNode nd = (FileNode) v.elementAt(i);
                if (newNode.compareTo(nd) < 0) {
                    v.insertElementAt(newNode, i);
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                v.addElement(newNode);
            }
        }

        for (int i = 0; i < v.size(); i++) {
            FileNode nd = (FileNode) v.elementAt(i);
            IconData idata = new IconData(leafIcon,
                    leafIcon, nd);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(idata);
            parent.add(node);

            if (nd.hasSubDirs()) {
                node.add(new DefaultMutableTreeNode(
                        new Boolean(true)));
            }
        }

        return true;
    }

    public boolean hasSubDirs() {
        File[] files = listFiles();
        if (files == null) {
            return false;
        }
        for (int k = 0; k < files.length; k++) {
            if (files[k].isDirectory()) {
                return true;
            }
        }
        return false;
    }

    public int compareTo(FileNode toCompare) {
        return m_file.getName().compareToIgnoreCase(
                toCompare.m_file.getName());
    }

    protected File[] listFiles() {

        try {
            return m_file.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error reading directory " + m_file.getAbsolutePath(),
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}
