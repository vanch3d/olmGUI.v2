/**
 * @file OLMPleaseWait.java
 */
package olmgui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;

import olmgui.i18n.Messages;

/**
 * Progress Bar widget used for visualising connection attempts with the server and, 
 * if relevant, displaying the cause of connection errors.
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.9 $
 *
 */
public class OLMPleaseWait extends JToolBar {

    private final static String MSG_CONNECTING = "Connecting to LeActiveMath server...";
    
    private JProgressBar jProgressBar = null;
    private JButton jBtnStop = null;

    /**
     * This method initializes 
     * 
     */
    public OLMPleaseWait() {
    	super();
    	initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.add(getJProgressBar());
        this.add(getJBtnStop());
        this.setFloatable(false);
        this.setSize(300, 26);
    		
    }

    /**
     * This method initializes jProgressBar	
     * 	
     * @return javax.swing.JProgressBar	
     */    
    private JProgressBar getJProgressBar() {
    	if (jProgressBar == null) {
    		jProgressBar = new JProgressBar(0,2000);
            //jProgressBar.setString(MSG_CONNECTING);
            jProgressBar.setString("");
            jProgressBar.setValue(0);
            jProgressBar.setStringPainted(true);
            jProgressBar.setIndeterminate(false);
    	}
    	return jProgressBar;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */    
    private JButton getJBtnStop() {
    	if (jBtnStop == null) {
    		jBtnStop = new JButton();
    		jBtnStop.setIcon(new ImageIcon(getClass().getResource("/res/stop.gif")));
    		jBtnStop.setPreferredSize(new java.awt.Dimension(24,24));
    		jBtnStop.setEnabled(false);
    		jBtnStop.addActionListener(new java.awt.event.ActionListener() { 
    			public void actionPerformed(java.awt.event.ActionEvent e) {    
    				System.out.println("Trying to interrupt the request? Poor fool!"); 
    			}
    		});
    	}
    	return jBtnStop;
    }
    
    /**
     * Set the text of the progress bar.
     * @param str   The string to display. If null, then them empty string is set.
     */
    public void setProgressText(String str)
    {
        jProgressBar.setString((str==null)?"":Messages.getString(str));
    }

    public void setProgressString(String str)
    {
        jProgressBar.setString((str==null)?"":str);
    }
    
    public void enableProgress(boolean show)
    {
        jProgressBar.setIndeterminate(show);
        jBtnStop.setEnabled(show);
        if (show==true)
            jProgressBar.setString(MSG_CONNECTING);
    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
