/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import com.sun.star.beans.XPropertySet;
import com.sun.star.deployment.PackageInformationProvider;
import com.sun.star.deployment.XPackageInformationProvider;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.TextContentAnchorType;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.ucb.XFileIdentifierConverter;
import com.sun.star.ucb.XSimpleFileAccess;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XURLTransformer;
import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author Lakmal
 */
public final class ImageLoader {

    private ImageIcon appIcon, folderIcon, openOfficeIcon;
    private XComponentContext m_xContext;
    private XMultiComponentFactory m_xMCF = null;
    private final static String IMAGE_FOLDER_PATH = "/images";
    private File textFile;
    private static String m_sGraphicFile;
    private static String m_sGraphicFileURL;

    public ImageLoader(XComponentContext m_xContext) {
        this.m_xContext = m_xContext;
        this.getImageIcons();
    }

    /*Load Image files to the user interface of the application*/
    public void getImageIcons() {
        try {
            XPackageInformationProvider xPackageInformationProvider = PackageInformationProvider.get(m_xContext);
            String location = xPackageInformationProvider.getPackageLocation("com.example.PixelPluz");
            Object oTransformer = m_xContext.getServiceManager().createInstanceWithContext("com.sun.star.util.URLTransformer", m_xContext);
            XURLTransformer xTransformer = (XURLTransformer) UnoRuntime.queryInterface(XURLTransformer.class, oTransformer);
            com.sun.star.util.URL[] oURL = new com.sun.star.util.URL[1];
            oURL[0] = new com.sun.star.util.URL();
            oURL[0].Complete = location + IMAGE_FOLDER_PATH;
            System.out.println(oURL[0].Path + oURL[0].Name);
            xTransformer.parseStrict(oURL);
            assignImages(new File(oURL[0].Path + oURL[0].Name).listFiles());
        } catch (com.sun.star.uno.Exception ex) {
            System.out.println(ex);
            System.out.println(ex.getStackTrace());
        }
    }

    public void assignImages(File[] files) {
        int i = 1;
        System.out.println(files);
        folderIcon = new ImageIcon(files[i++].getAbsolutePath());
        appIcon = new ImageIcon(files[i++].getAbsolutePath());
        openOfficeIcon = new ImageIcon(files[i++].getAbsolutePath());
        textFile = files[i];
    }

    public ImageIcon getAppIcon() {
        return appIcon;
    }

    public ImageIcon getFolderIcon() {
        return folderIcon;
    }

    public File getTextFile() {
        return textFile;
    }

    protected void runDemo(String file, int width, int height) throws java.lang.Exception {
        // Check if the graphic file really exist
        // First convert the system path to an URL

        java.io.File sourceFile = new java.io.File(file);
        m_sGraphicFileURL = convertToURL("", sourceFile.getAbsolutePath());
        XTextDocument xTextDoc = createTextDocument();

        // Querying for the interface XMultiServiceFactory on the XTextDocument
        XMultiServiceFactory xMSFDoc = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, xTextDoc);

        // Creating the service GraphicObject
        Object oGraphic = xMSFDoc.createInstance(
                "com.sun.star.text.TextGraphicObject");

        // Querying for the interface XTextContent on the GraphicObject
        XTextContent xTextContent = (XTextContent) UnoRuntime.queryInterface(
                XTextContent.class, oGraphic);

        // Getting the text
        XText xText = xTextDoc.getText();

        // Getting the cursor on the document
        XTextCursor xTextCursor = xText.createTextCursor();

        // Inserting the content
        xText.insertTextContent(xTextCursor, xTextContent, true);

        // Querying for the interface XPropertySet on the graphic object
        XPropertySet xPropSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, oGraphic);

        // Setting the anchor type
        xPropSet.setPropertyValue("AnchorType", TextContentAnchorType.AT_PARAGRAPH);

        // Setting the graphic url
        xPropSet.setPropertyValue("GraphicURL", m_sGraphicFileURL);

        // Setting the horizontal position
        xPropSet.setPropertyValue("HoriOrientPosition", new Integer(5500));

        // Setting the vertical position
        xPropSet.setPropertyValue("VertOrientPosition", new Integer(4200));

        // Setting the width
        xPropSet.setPropertyValue("Width", (int) width * 25);

        // Setting the height
        xPropSet.setPropertyValue("Height", (int) height * 25);
    }

    private XMultiComponentFactory getMultiComponentFactory()
            throws java.lang.Exception {

        m_xMCF = m_xContext.getServiceManager();

        return m_xMCF;
    }

    private XComponent newDocComponent(String docType)
            throws java.lang.Exception {
        String loadUrl = "private:factory/" + docType;
        m_xMCF = this.getMultiComponentFactory();
        Object desktop = m_xMCF.createInstanceWithContext(
                "com.sun.star.frame.Desktop", m_xContext);
        XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(com.sun.star.frame.XDesktop.class, desktop);
        XComponent document = xDesktop.getCurrentComponent();
        /*XComponentLoader xComponentLoader =
        (XComponentLoader) UnoRuntime.queryInterface(
        XComponentLoader.class, desktop);
        PropertyValue[] loadProps = new PropertyValue[0];
        return xComponentLoader.loadComponentFromURL(loadUrl, "_blank",
        0, loadProps);*/
        return document;
    }

    private XTextDocument createTextDocument() {
        XTextDocument aTextDocument = null;

        XComponent xComponent;
        try {
            xComponent = newDocComponent("swriter");
            aTextDocument = (XTextDocument) UnoRuntime.queryInterface(
                    XTextDocument.class, xComponent);
        } catch (java.lang.Exception ex) {
            ex.printStackTrace(System.err);
        }


        return aTextDocument;
    }

    private boolean checkFile(String aURL) {
        boolean bExists = false;
        try {
            System.out.println(m_xContext);
            XSimpleFileAccess xSFA = (XSimpleFileAccess) UnoRuntime.queryInterface(
                    XSimpleFileAccess.class,
                    this.getMultiComponentFactory().createInstanceWithContext(
                    "com.sun.star.ucb.SimpleFileAccess", m_xContext));
            bExists = xSFA.exists(aURL) && !xSFA.isFolder(aURL);
        } catch (com.sun.star.ucb.CommandAbortedException ex) {
            ex.printStackTrace();
        } catch (com.sun.star.uno.Exception ex) {
            ex.printStackTrace();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        return bExists;
    }

    /**
     * Converts an URL into a system path using OOo API
     * @param sURLPath
     * @return
     */
    private String convertFromURL(String sURLPath) {
        String sSystemPath = null;
        try {
            m_xMCF = getMultiComponentFactory();
            XFileIdentifierConverter xFileConverter =
                    (XFileIdentifierConverter) UnoRuntime.queryInterface(
                    XFileIdentifierConverter.class,
                    m_xMCF.createInstanceWithContext(
                    "com.sun.star.ucb.FileContentProvider", m_xContext));
            sSystemPath = xFileConverter.getSystemPathFromFileURL(sURLPath);

        } catch (com.sun.star.uno.Exception e) {
            e.printStackTrace(System.err);
        } finally {
            return sSystemPath;
        }
    }

    /**
     * Converts a system path into an URL using OOo API
     * @param sBase
     * @param sSystemPath
     * @return
     */
    private String convertToURL(String sBase, String sSystemPath) {
        String sURL = null;
        try {
            m_xMCF = getMultiComponentFactory();
            XFileIdentifierConverter xFileConverter =
                    (XFileIdentifierConverter) UnoRuntime.queryInterface(
                    XFileIdentifierConverter.class,
                    m_xMCF.createInstanceWithContext(
                    "com.sun.star.ucb.FileContentProvider", m_xContext));
            sURL = xFileConverter.getFileURLFromSystemPath(
                    sBase, sSystemPath);
        } catch (com.sun.star.uno.Exception e) {
            e.printStackTrace();
        } finally {
            return sURL;
        }
    }
}
