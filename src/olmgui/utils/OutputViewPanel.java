/**
 * @file OutputViewPanel.java
 */
package olmgui.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import javax.swing.BorderFactory;
import javax.swing.border.SoftBevelBorder;

import toulmin.Toulmin;

import com.l2fprod.common.swing.plaf.blue.BlueishButtonUI;

import dialogue.DialoguePlanner;

import java.awt.ComponentOrientation;
import java.util.ArrayList;

/**
 * This is a generic panel used in the main GUI to hold the various
 * External Representations used in the OLM.
 * It optionally adds a "tell me more" button below the view which can be 
 * used to browse between different views of the ER - if any.
 * It is up to the External Representation to deal with the message.
 *
 * @author Nicolas Van Labeke
 * @version $Revision: 1.16 $
 */
public abstract class OutputViewPanel extends JPanel {

    /**
     * Action message used by the "tell me more" button.
     */
    final public static String  VIEW_TELLMORE = "TELLMORE";
    final public static String  VIEW_SWAP = "SWAP";
    
    private String BTN_TELLMORE = Messages.getString("OLMMoveSelector.TELLMORE.name"); //$NON-NLS-1$
    
    /**
     * A reference to the panel containing the "tell me more" button.
     */
    private JToolBar jMorePanel = null;

    /**
     * A reference to the "tell me more" button.
     */
    private JButton jTellBtn = null;

	private JButton jSwapBtn = null;
    private JPanel jPanel = null;
    private JToolBar jToolBar = null;
    
    private ArrayList views = new ArrayList();  //  @jve:decl-index=0:

    /**
     * Default constructor.
     */
    public OutputViewPanel() 
    {
        super();
        initialize();
    }

    /**
     * Initialise the GUI of the panel.
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(386, 289);
        //this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        //this.add(getJButton(), BorderLayout.CENTER);
        this.add(getJPanel(), BorderLayout.SOUTH);
    }

    /**
     * Add the view at the centre of the panel.
     * @param cpnt  A reference to the view to add to the panel.
     */
    public void addMainView(Component cpnt)
    {
        this.add(cpnt,java.awt.BorderLayout.CENTER);
    }
    
    /**
     * Enable or disable the "tell me more" button.
     * @param show  <code>true</code> to enable the button, 
     *              <code>false</code> to disable it.
     */
    public void enableTellMeMore(boolean show)
    {
        jMorePanel.setVisible(show);
        jTellBtn.setEnabled(show);
    }

    public void enableSwap(boolean show)
    {
        jMorePanel.setVisible(show);
        jSwapBtn.setVisible(show);
        jSwapBtn.setEnabled(show);
    }

    /**
     * Add a listener to the "tell me more" actions and enable it.
     * @param listen    A reference to the listener intercepting the action.
     */
    public void addSwapListener(ActionListener listen)
    {
        enableSwap(true);
        getJSwapBtn().addActionListener(listen);
    }

    /**
     * Add a listener to the mouse actions in the "tell me more" button.
     * @param listen
     */
    public void addSwapListener(MouseListener listen)
    {
    	enableSwap(true);
    	getJSwapBtn().addMouseListener(listen);
   }    
    
    public void addTellMeListener(ActionListener listen)
    {
        enableTellMeMore(true);
        getJTellBtn().addActionListener(listen);
    }

    /**
     * Add a listener to the mouse actions in the "tell me more" button.
     * @param listen
     */
    public void addTellMeListener(MouseListener listen)
    {
        enableTellMeMore(true);
        getJTellBtn().addMouseListener(listen);
   }    
    
    public String getCurrentView()
    {
    	if (views.isEmpty()) 
    		return null;
    	else 
    		return (String)views.get(0);
    }
    
    public void addSwap(String view)
    {
    	views.add(view);
    }
    
    public void swapView()
    {
     	if (views.isEmpty()) return;
     	Object obj = views.remove(0);
     	views.add(obj);
    }

    abstract public void swapToNextView();
    abstract public void setListeners(DialoguePlanner planner);

    
    /**
     * Initialise the "tell me more" panel.
     * @return  A reference to the panel.
     */
    protected JToolBar getJMorePanel() {
        if (jMorePanel == null) {
//            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
//            gridBagConstraints1.gridx = 1;
//            gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
//            gridBagConstraints1.insets = new java.awt.Insets(2,0,2,2);
//            gridBagConstraints1.gridy = 0;
//            GridBagConstraints gridBagConstraints = new GridBagConstraints();
//            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
//            gridBagConstraints.gridy = 0;
//            gridBagConstraints.weightx = 1.0;
//            gridBagConstraints.insets = new java.awt.Insets(2,0,2,2);
//            gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
//            gridBagConstraints.gridx = 0;
//            jMorePanel = new JPanel();
//            jMorePanel.setVisible(true);
//            jMorePanel.setLayout(new GridBagLayout());
//            jMorePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0), new SoftBevelBorder(SoftBevelBorder.RAISED)));
//
//            jMorePanel.add(getJTellBtn(), gridBagConstraints);
//            jMorePanel.add(getJSwapBtn(), gridBagConstraints1);
            
            jMorePanel = new JToolBar();
            //jMorePanel.setFloatable(false);
            jMorePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),  new SoftBevelBorder(SoftBevelBorder.RAISED)));

            jMorePanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            jMorePanel.setFloatable(false);
            jMorePanel.add(getJTellBtn());
        }
        return jMorePanel;
    }

    /**
     * Initialise the "tell me more" button.	
     * @return A reference to the button.	
     */
    private JButton getJTellBtn() {
        if (jTellBtn == null) {
            jTellBtn = new JButton();
            jTellBtn.setUI(new BlueishButtonUI());
            jTellBtn.setEnabled(false);

            jTellBtn.setText(BTN_TELLMORE);
            jTellBtn.setActionCommand(VIEW_TELLMORE);
            jTellBtn.setIcon(new ImageIcon(getClass().getResource("/res/tellme.gif"))); //$NON-NLS-1$
            //DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.TELLMORE);
            //jTellBtn.setAction(act);
            //jTellBtn.addMouseListener(act);
            //jTellBtn.addActionListener(new TellMeMoreListener());
            //jTellBtn.setToolTipText(null);

        }
        return jTellBtn;
    }

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSwapBtn() {
		if (jSwapBtn == null) {
			jSwapBtn = new JButton();
			jSwapBtn.setIcon(new ImageIcon(getClass().getResource("/res/swap.gif")));
			jSwapBtn.setActionCommand(VIEW_SWAP);
			jSwapBtn.setVisible(true);
			jSwapBtn.setEnabled(false);
            jSwapBtn.setUI(new BlueishButtonUI());

		}
		return jSwapBtn;
	}

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.add(getJToolBar(), BorderLayout.WEST);
            jPanel.add(getJMorePanel(), BorderLayout.CENTER);
        }
        return jPanel;
    }

    /**
     * This method initializes jToolBar	
     * 	
     * @return javax.swing.JToolBar	
     */
    private JToolBar getJToolBar() {
        if (jToolBar == null) {
            jToolBar = new JToolBar();
            //jToolBar.setFloatable(false);
            jToolBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),  new SoftBevelBorder(SoftBevelBorder.RAISED)));
            jToolBar.setFloatable(false);
            jToolBar.add(getJSwapBtn());
        }
        return jToolBar;
    }
    
    abstract public void setToulmin(Toulmin toulmin);
    abstract public void setSelectedNode(ToulminNode node);


}  //  @jve:decl-index=0:visual-constraint="10,10"  
