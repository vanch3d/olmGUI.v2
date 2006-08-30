package olmgui;


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;

import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import olmgui.output.OLMArgumentView;

import config.OLMPrefs;
import dialogue.DialoguePlanner;

import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

/**
 * A demo that shows all of the Swing components.
 *
 * @version 1.50 07/26/04
 * @author Jeff Dinkins
 */
public class OLMGUI extends JPanel {

 



    // Used only if swingset is an application 
    private JFrame frame = null;
    private JWindow splashScreen = null;

    // Used only if swingset is an applet 
    private OLMApplet applet = null;

    // contentPane cache, saved from the applet or application frame
    Container contentPane = null;
	private OLMPleaseWait jProcessToolBar;
	private JToolBar jToolBar;

	private OLMArgumentView jArgumentView;
  
    /**
     * SwingSet2 Constructor
     */
    public OLMGUI(OLMApplet applet) 
    {

    // Note that the applet may null if this is started as an application
    this.applet = applet;

    // Create Frame here for app-mode so the splash screen can get the
    // GraphicsConfiguration from it in createSplashScreen()
    if (!isApplet()) {
        frame = createFrame();
    }

    // Create and throw the splash screen up. Since this will
    // physically throw bits on the screen, we need to do this
    // on the GUI thread using invokeLater.
    //createSplashScreen();
    initialize();
    
    // do the following on the gui thread
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
        //showSplashScreen();
        }
    });
        
    // Show the demo and take down the splash screen. Note that
    // we again must do this on the GUI thread using invokeLater.
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
        showSwingSet2();
        }
    });

    }

	private void initialize() {
	    // setLayout(new BorderLayout());
	    setLayout(new BorderLayout());
	    JLabel top = new JLabel("dfdfdfd");
        add(getJProcessToolBar(), java.awt.BorderLayout.SOUTH);
        add(getJToolBar(), java.awt.BorderLayout.WEST);
        add(getOLMArgumentView(), java.awt.BorderLayout.CENTER);

	    // set the preferred size of the demo
	    setPreferredSize(new Dimension(800,600));

	}
    /**
     * This method initializes OLMArgumentView	
     * 	
     * @return olmgui.output.OLMArgumentView	
     */
    private OLMArgumentView getOLMArgumentView() {
        if (jArgumentView == null) {
            jArgumentView = new OLMArgumentView(new DialoguePlanner((JApplet)null));
            jArgumentView.setName("OLMArgumentView");
        }
        return jArgumentView;
    }
    /**
     * This method initializes the progress bar of the applet 
     *
     * @return javax.swing.JToolBar   A reference to the pane
     */
    private OLMPleaseWait getJProcessToolBar()
    {
        if (jProcessToolBar == null) {
            jProcessToolBar = new OLMPleaseWait();
        }

        return jProcessToolBar;
    }

    /**
     * This method initializes the toolbar used to switch between the various display modes.	
     * 	
     * @return javax.swing.JToolBar	   A reference to the widget
     */    
    private JToolBar getJToolBar() {
    	if (jToolBar == null) {
    		jToolBar = new JToolBar();
    		jToolBar.setFloatable(false);
            jToolBar.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.control,5));
    		jToolBar.setOrientation(javax.swing.JToolBar.VERTICAL);
    		//jToolBar.setPreferredSize(new Dimension(40, 110));
  
    	}
    	return jToolBar;
    }
    /**
     * SwingSet2 Main. Called only if we're an application, not an applet.
     */
    public static void main(String[] args) {
    // Create SwingSet on the default monitor
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        UIManager.put("swing.boldMetal", Boolean.FALSE);
        OLMGUI swingset = new OLMGUI(null);
    }



    /**
     * Bring up the SwingSet2 demo by showing the frame (only
     * applicable if coming up as an application, not an applet);
     */
    public void showSwingSet2() {
    if(!isApplet() && getFrame() != null) {
        // put swingset in a frame and show it
        JFrame f = getFrame();
        f.setTitle("Frame.title");
        f.getContentPane().add(this, BorderLayout.CENTER);
        f.pack();

        Rectangle screenRect = f.getGraphicsConfiguration().getBounds();
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                    f.getGraphicsConfiguration());

            // Make sure we don't place the demo off the screen.
            int centerWidth = screenRect.width < f.getSize().width ?
                    screenRect.x :
                    screenRect.x + screenRect.width/2 - f.getSize().width/2;
            int centerHeight = screenRect.height < f.getSize().height ?
                    screenRect.y :
                    screenRect.y + screenRect.height/2 - f.getSize().height/2;

            centerHeight = centerHeight < screenInsets.top ?
                    screenInsets.top : centerHeight;

            f.setLocation(centerWidth, centerHeight);
        f.setVisible(true);
    } 
    }


    /**
     * Determines if this is an applet or application
     */
    public boolean isApplet() {
    return (applet != null);
    }

    /**
     * Returns the applet instance
     */
    public OLMApplet getApplet() {
    return applet;
    }


    /**
     * Returns the frame instance
     */
    public JFrame getFrame() {
    return frame;
    }

    /**
     * Returns the content pane wether we're in an applet
     * or application
     */
    public Container getContentPane() {
    if(contentPane == null) {
        if(getFrame() != null) {
        contentPane = getFrame().getContentPane();
        } else if (getApplet() != null) {
        contentPane = getApplet().getContentPane();
        }
    }
    return contentPane;
    }

    /**
     * Create a frame for SwingSet2 to reside in if brought up
     * as an application.
     */
    public static JFrame createFrame() {
    JFrame frame = new JFrame();
//        if (numSSs == 0) {
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        } else {
//        WindowListener l = new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                    numSSs--;
//                    swingSets.remove(this);
//            }
//        };
//        frame.addWindowListener(l);
//        }
    return frame;
    }
    
 }

