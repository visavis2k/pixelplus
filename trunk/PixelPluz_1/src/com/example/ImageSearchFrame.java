/*
 * MainFrame.java
 *
 * Copyright 2006 Sun Microsystems, Inc. ALL RIGHTS RESERVED Use of
 * this software is authorized pursuant to the terms of the license
 * found at http://developers.sun.com/berkeley_license.html.
 *
 */
package com.example;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class ImageSearchFrame extends javax.swing.JFrame {

    private MainFrame frame;
    private ImageRetriever imgRetriever;
    private BufferedImage image;

    public ImageSearchFrame(MainFrame frame) {
        pack();
        setLocationRelativeTo(null);
       //setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        this.frame = frame;
        API_KEY = "339db1433e5f6f11f3ad54135e6c07a9";
        System.out.println(API_KEY);
        if (API_KEY == null) {
            System.err.println("You must create a FlickrKey.properties file.");
            System.exit(-1);
        }

        initComponents();
         setLocationRelativeTo(null);

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSearch = new javax.swing.JTextField();
        lblImageList = new javax.swing.JLabel();
        scrollImageList = new javax.swing.JScrollPane();
        listModel = new DefaultListModel();
        renderer = new ImageInfoRenderer();
        listImages = new JList(listModel);
        listImages.setCellRenderer(renderer);
        lblSelectedImage = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        progressMatchedImages = new javax.swing.JProgressBar();
        listenerMatchedImages = new ProgressListener(progressMatchedImages);
        progressSelectedImage = new javax.swing.JProgressBar();
        listenerSelectedImage = new ProgressListener(progressSelectedImage);
        searchButton = new javax.swing.JButton();
        placeDbutton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Flickr Search");
        setBounds(new java.awt.Rectangle(200, 50, 0, 0));
        setResizable(false);

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });

        lblImageList.setText("Matched Images");

        listImages.setModel(listModel);
        listImages.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listImages.setCellRenderer(renderer);
        listImages.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listImagesValueChanged(evt);
            }
        });
        scrollImageList.setViewportView(listImages);

        lblSelectedImage.setText("Selected Image");

        lblImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        lblImage.setFocusable(false);
        lblImage.setMaximumSize(new java.awt.Dimension(500, 500));
        lblImage.setMinimumSize(new java.awt.Dimension(250, 250));
        lblImage.setOpaque(true);
        lblImage.setPreferredSize(new java.awt.Dimension(500, 250));

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        placeDbutton.setText("Save & Place ");
        placeDbutton.setEnabled(false);
        placeDbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placeDbuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addComponent(scrollImageList, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblImageList)
                            .addComponent(lblSelectedImage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressMatchedImages, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                            .addComponent(progressSelectedImage, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                    .addComponent(placeDbutton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblImageList)
                    .addComponent(progressMatchedImages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollImageList, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedImage)
                    .addComponent(progressSelectedImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(placeDbutton)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("Flickr Search");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void retrieveImage(BufferedImage img) {
        image = img;
        placeDbutton.setEnabled(true);
    }
    private void listImagesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listImagesValueChanged
        // don't do anything if this is just a changing list
        // being populated by thumbnail images
        if (evt.getValueIsAdjusting() || listImages.isSelectionEmpty()) {
            return;
        }
        // handle selection made by the user
        int selectedIndex = listImages.getSelectedIndex();
        if (selectedIndex >= 0) {
            ImageInfo info = (ImageInfo) listImages.getSelectedValue();
            String id = info.getId();
            String server = info.getServer();
            String secret = info.getSecret();
            // no need to search an invalid thumbnail image
            if (id == null || server == null || secret == null) {
                return;
            }
            String strImageUrl = String.format(IMAGE_URL_FORMAT,
                    server, id, secret);
            retrieveImage(strImageUrl);
        }
    }//GEN-LAST:event_listImagesValueChanged

    private void retrieveImage(String imageUrl) {
        // SwingWorker objects can't be reused, so
        // create a new one as needed.
        imgRetriever = new ImageRetriever(lblImage, imageUrl, this);
        progressSelectedImage.setValue(0);
        // listen for changes in the "progress" property
        // we can reuse the listener even though the worker thread
        // will be a new SwingWorker
        imgRetriever.addPropertyChangeListener(listenerSelectedImage);

        progressSelectedImage.setIndeterminate(true);

        // tell the worker thread to begin with this asynchronous method
        imgRetriever.execute();
        // this event thread continues immediately here without blocking

    }

    /**
     * Begin searching for images with search text in either the
     * title or description.
     */
    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String strSearchText = txtSearch.getText();
            if (strSearchText != null && strSearchText.length() > 0) {
                searchImages(strSearchText, 1);
            }
        }
    }//GEN-LAST:event_txtSearchKeyPressed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:`
        try{
        String strSearchText = txtSearch.getText();
        if (strSearchText != null && strSearchText.length() > 0) {
            searchImages(strSearchText, 1);
        }
        }catch(Throwable e){
            JOptionPane.showMessageDialog(null, "Cannot Find a working Internet Connection.Check your internet connectivity and Try Again ", "No Connection", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void placeDbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placeDbuttonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

            String description = "Image File(*.jpg)";//the filter you see
            String extension = "jpg";//the filter passed to program

            @Override
            public boolean accept(File f) {
                if (f == null) {
                    return false;
                }
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().toLowerCase().endsWith(extension);
            }
            public String getExtension(){
                return extension;
            }
            @Override
            public String getDescription() {
                return extension;
            }
        });
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

            String description = "Image File(*.png)";//the filter you see
            String extension = "png";//the filter passed to program

            @Override
            public boolean accept(File f) {
                if (f == null) {
                    return false;
                }
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().toLowerCase().endsWith(extension);

            }

            public String getExtension() {
                return extension;
            }

            @Override
            public String getDescription() {
                return extension;
            }
        });
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

            String description = "Image File(*.gif)";//the filter you see
            String extension = "gif";//the filter passed to program

            @Override
            public boolean accept(File f) {
                if (f == null) {
                    return false;
                }
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().toLowerCase().endsWith(extension);
            }

            @Override
            public String getDescription() {
                return extension;
            }

            public String getExtension() {
                return extension;
            }
        });
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {

                ImageIO.write(image, fileChooser.getFileFilter().getDescription(), fileChooser.getSelectedFile());
                frame.setMainImage(image, true);
                frame.setMainImageFile(fileChooser.getSelectedFile());
                frame.setIsMainImageSaved(true);
                dispose();
            } catch (IOException ex) {
                System.out.println("Error in Writing to File " + ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Save it before Editing !", "Save", JOptionPane.INFORMATION_MESSAGE);
        }


    }//GEN-LAST:event_placeDbuttonActionPerformed

    /**
     * searchImages
     * Initiate a search to retrieve a page of matched images.
     */
    private void searchImages(String strSearchText, int page) {
        if (searcher != null && !searcher.isDone()) {
            // cancel current search to begin a new one
            // only want one image search at a time.
            searcher.cancel(true);
            searcher = null;
        }

        // any parameters on the url GET command needs to URL-encoded
        String strEncodedText = null;
        try {
            strEncodedText = URLEncoder.encode(strSearchText, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            strEncodedText = strSearchText;
        }

        // remove matched images from any previous search
        listModel = new DefaultListModel();
        listImages.setModel(listModel);
        // clear up any displayed images in preparation to show another
        // selection from the image list
        lblImage.setIcon(null);
        lblImage.setText(null);
        // provide the list model so that the ImageSearcher can publish
        // images to the list immediately as they are available.
        searcher = new ImageSearcher(listModel, API_KEY, strEncodedText, page);
        searcher.addPropertyChangeListener(listenerMatchedImages);

        progressMatchedImages.setIndeterminate(true);

        // start the search!
        searcher.execute();
        // this event thread continues immediately here without blocking
    }

    class ProgressListener implements PropertyChangeListener {
        // prevent creation without providing a progress bar

        private ProgressListener() {
        }

        ProgressListener(JProgressBar progressBar) {
            this.progressBar = progressBar;
            this.progressBar.setValue(0);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String strPropertyName = evt.getPropertyName();
            if ("progress".equals(strPropertyName)) {
                progressBar.setIndeterminate(false);
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
            }
        }
        private JProgressBar progressBar;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblImageList;
    private javax.swing.JLabel lblSelectedImage;
    private javax.swing.JList listImages;
    private javax.swing.JButton placeDbutton;
    private javax.swing.JProgressBar progressMatchedImages;
    private javax.swing.JProgressBar progressSelectedImage;
    private javax.swing.JScrollPane scrollImageList;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
    private DefaultListModel listModel;
    private ImageInfoRenderer renderer;
    private SwingWorker searcher;
    private ProgressListener listenerMatchedImages;
    private ProgressListener listenerSelectedImage;
    final private String IMAGE_URL_FORMAT = "http://static.flickr.com/%s/%s_%s.jpg";
    static private String API_KEY;
}
