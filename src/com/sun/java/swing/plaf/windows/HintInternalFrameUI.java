package com.sun.java.swing.plaf.windows;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import com.sun.java.swing.plaf.windows.WindowsGraphicsUtils;
import com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane;
import com.sun.java.swing.plaf.windows.WindowsInternalFrameUI;
import com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane.WindowsTitlePaneLayout;

public class HintInternalFrameUI extends WindowsInternalFrameUI {

 	public HintInternalFrameUI(JInternalFrame b)
	{
		super(b);
//    	UIManager.put("InternalFrame.titleButtonWidth", new Integer(16));
//    	UIManager.put("InternalFrame.titleButtonHeight", new Integer(16));
//    	UIManager.put("InternalFrame.titlePaneHeight", new Integer(16));
	}

	public static ComponentUI createUI(JComponent b) {
	    return new HintInternalFrameUI((JInternalFrame) b);
	  }
	
	public void installDefaults() {
        super.installDefaults();

	    frame.setBorder(UIManager.getBorder("InternalFrame.border"));
    }

	  /* (non-Javadoc)
	 * @see javax.swing.plaf.basic.BasicInternalFrameUI#createNorthPane(javax.swing.JInternalFrame)
	 */
	protected JComponent createNorthPane(JInternalFrame w) {
		    return new HintInternalFrameTitlePane(w);
		  }



}
