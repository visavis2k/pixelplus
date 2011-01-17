/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

/**
 *
 * @author Lakmal
 */
//import com.sun.media.ui.ToolTip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;


/* ResizableComponent.java */
class CropComponent extends JFrame {

    private JScrollPane panel;
    private Resizable resizer;
    private BufferedImage image;
    

    public CropComponent(BufferedImage image) {
        setBounds(100, 100, 400, 400);
        this.image = image;
        panel = new ImagePanel(image);
        panel.setSize(image.getWidth(), image.getHeight());
        JLabel l = new JLabel();
        resizer = new Resizable(l);
        resizer.setBounds(50, 50, 200, 150);
        resizer.setOpaque(false);
        panel.add(resizer);
        getContentPane().add(panel);
//        size = new Dimension(image.getWidth(), image.getHeight());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(350, 300));
        setTitle("Cropper");
        setLocationRelativeTo(null);
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent me) {
                requestFocus();
                resizer.repaint();
                resizer.revalidate();
                panel.repaint();
                panel.revalidate();
                getContentPane().repaint();




            }
        });

        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                requestFocus();
                resizer.repaint();
                resizer.revalidate();
                panel.repaint();
                panel.revalidate();
                getContentPane().repaint();
            }
        });

        addMouseListener(new MouseAdapter() {

            public void mouseMoved(MouseEvent me) {
                requestFocus();
                resizer.repaint();
                resizer.revalidate();
                panel.repaint();
                panel.revalidate();
                getContentPane().repaint();
            }
        });



        // Netralize the effect of maximize button since it can't be disabled.
        addWindowStateListener(new WindowAdapter() {

            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == JFrame.MAXIMIZED_BOTH) // If maximize
                {
                    setExtendedState(JFrame.NORMAL); // then revert back to original size. will create flicker.
                    JOptionPane.showMessageDialog(null, "Not Maximizable! Chnage the size of the window manually to a prefered size");
                }
            }
        });

    }

    public BufferedImage clipImage() {
        BufferedImage clipped = null;
        try {
            int w = resizer.getWidth();
            int h = resizer.getHeight();
            //int x0 = (getWidth() - size.width) / 2;
            //int y0 = (getHeight() - size.height) / 2;
            int x = resizer.getX();// - x0;
            int y = resizer.getY(); //- y0;
            clipped = image.getSubimage(x, y, w, h);


        } catch (RasterFormatException rfe) {
            System.out.println("raster format error: " + rfe.getMessage());
            JOptionPane.showMessageDialog(null, "Selection is out of the Image area", "Selection Error", JOptionPane.INFORMATION_MESSAGE);
            return null;


        }
        return clipped;
    }
}

class Resizable extends JComponent {

    public Resizable(Component comp) {
        this(comp, new ResizableBorder(8));
    }

    public Resizable(Component comp, ResizableBorder border) {
        setLayout(new BorderLayout());
        add(comp);
        addMouseListener(resizeListener);
        addMouseMotionListener(resizeListener);
        setBorder(border);
    }

    private void resize() {
        if (getParent() != null) {
            ((JComponent) getParent()).revalidate();
        }
    }
    MouseInputListener resizeListener = new MouseInputAdapter() {

        public void mouseMoved(MouseEvent me) {
            if (hasFocus()) {
                ResizableBorder border = (ResizableBorder) getBorder();
                setCursor(Cursor.getPredefinedCursor(border.getCursor(me)));
                repaint();
                revalidate();
            }
        }

        public void mouseExited(MouseEvent mouseEvent) {
            setCursor(Cursor.getDefaultCursor());

        }
        private int cursor;
        private Point startPos = null;

        public void mousePressed(MouseEvent me) {
            ResizableBorder border = (ResizableBorder) getBorder();
            cursor = border.getCursor(me);
            startPos = me.getPoint();
            requestFocus();
            repaint();
            revalidate();
        }

        public void mouseDragged(MouseEvent me) {

            if (startPos != null) {

                int x = getX();
                int y = getY();
                int w = getWidth();
                int h = getHeight();

                int dx = me.getX() - startPos.x;
                int dy = me.getY() - startPos.y;

                switch (cursor) {
                    case Cursor.N_RESIZE_CURSOR:
                        if (!(h - dy < 50)) {
                            setBounds(x, y + dy, w, h - dy);
                            resize();
                        }
                        break;

                    case Cursor.S_RESIZE_CURSOR:
                        if (!(h + dy < 50)) {
                            setBounds(x, y, w, h + dy);
                            startPos = me.getPoint();
                            resize();
                        }
                        break;

                    case Cursor.W_RESIZE_CURSOR:
                        if (!(w - dx < 50)) {
                            setBounds(x + dx, y, w - dx, h);
                            resize();
                        }
                        break;

                    case Cursor.E_RESIZE_CURSOR:
                        if (!(w + dx < 50)) {
                            setBounds(x, y, w + dx, h);
                            startPos = me.getPoint();
                            resize();
                        }
                        break;

                    case Cursor.NW_RESIZE_CURSOR:
                        if (!(w - dx < 50) && !(h - dy < 50)) {
                            setBounds(x + dx, y + dy, w - dx, h - dy);
                            resize();
                        }
                        break;

                    case Cursor.NE_RESIZE_CURSOR:
                        if (!(w + dx < 50) && !(h - dy < 50)) {
                            setBounds(x, y + dy, w + dx, h - dy);
                            startPos = new Point(me.getX(), startPos.y);
                            resize();
                        }
                        break;

                    case Cursor.SW_RESIZE_CURSOR:
                        if (!(w - dx < 50) && !(h + dy < 50)) {
                            setBounds(x + dx, y, w - dx, h + dy);
                            startPos = new Point(startPos.x, me.getY());
                            resize();
                        }
                        break;

                    case Cursor.SE_RESIZE_CURSOR:
                        if (!(w + dx < 50) && !(h + dy < 50)) {
                            setBounds(x, y, w + dx, h + dy);
                            startPos = me.getPoint();
                            resize();
                        }
                        break;

                    case Cursor.MOVE_CURSOR:
                        Rectangle bounds = getBounds();
                        bounds.translate(dx, dy);
                        setBounds(bounds);
                        resize();
                }


                setCursor(Cursor.getPredefinedCursor(cursor));
            }
            repaint();
            revalidate();
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            startPos = null;
            repaint();
            revalidate();
        }
    };
}

class ResizableBorder implements Border {

    private int dist = 8;
    int locations[] = {
        SwingConstants.NORTH, SwingConstants.SOUTH, SwingConstants.WEST,
        SwingConstants.EAST, SwingConstants.NORTH_WEST,
        SwingConstants.NORTH_EAST, SwingConstants.SOUTH_WEST,
        SwingConstants.SOUTH_EAST
    };
    int cursors[] = {
        Cursor.N_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR,
        Cursor.E_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
        Cursor.SW_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
    };

    public ResizableBorder(int dist) {
        this.dist = dist;
    }

    public Insets getBorderInsets(Component component) {
        return new Insets(dist, dist, dist, dist);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component component, Graphics g, int x, int y,
            int w, int h) {
        g.setColor(Color.black);
        g.drawRect(x + dist / 2, y + dist / 2, w - dist, h - dist);

        if (component.hasFocus()) {


            for (int i = 0; i < locations.length; i++) {
                Rectangle rect = getRectangle(x, y, w, h, locations[i]);
                g.setColor(Color.WHITE);
                g.fillRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
                g.setColor(Color.BLACK);
                g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
            }
        }
    }

    private Rectangle getRectangle(int x, int y, int w, int h, int location) {
        switch (location) {
            case SwingConstants.NORTH:
                return new Rectangle(x + w / 2 - dist / 2, y, dist, dist);
            case SwingConstants.SOUTH:
                return new Rectangle(x + w / 2 - dist / 2, y + h - dist, dist,
                        dist);
            case SwingConstants.WEST:
                return new Rectangle(x, y + h / 2 - dist / 2, dist, dist);
            case SwingConstants.EAST:
                return new Rectangle(x + w - dist, y + h / 2 - dist / 2, dist,
                        dist);
            case SwingConstants.NORTH_WEST:
                return new Rectangle(x, y, dist, dist);
            case SwingConstants.NORTH_EAST:
                return new Rectangle(x + w - dist, y, dist, dist);
            case SwingConstants.SOUTH_WEST:
                return new Rectangle(x, y + h - dist, dist, dist);
            case SwingConstants.SOUTH_EAST:
                return new Rectangle(x + w - dist, y + h - dist, dist, dist);
        }
        return null;
    }

    public int getCursor(MouseEvent me) {
        Component c = me.getComponent();
        int w = c.getWidth();
        int h = c.getHeight();

        for (int i = 0; i < locations.length; i++) {
            Rectangle rect = getRectangle(0, 0, w, h, locations[i]);
            if (rect.contains(me.getPoint())) {
                return cursors[i];
            }
        }

        return Cursor.MOVE_CURSOR;
    }
}

class ImagePanel extends JScrollPane {

    private Image img;

    public ImagePanel(String img) {

        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img) {
        this.img = img;
        Dimension size = new Dimension(500 , 500);
        setPreferredSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
