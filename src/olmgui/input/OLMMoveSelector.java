/**
 * @file OLMMoveSelector.java
 */
package olmgui.input;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import olmgui.i18n.Messages;
import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;
import dialogue.DialoguePlanner.DlgMoveAction;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.11 $
 *
 */
public class OLMMoveSelector extends JPanel
{

    /**
     * Shortcut for the internationalized strings used in the GUI
     */
    private String BTN_QUIT = Messages.getString("OLMMoveSelector.QUIT.name"); //$NON-NLS-1$

    private JButton jBtnQuit = null;

    /**
     * 
     */
    public OLMMoveSelector()
    {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.gridy = 0;
        gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.weighty = 0.0;
        gridBagConstraints11.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints11.insets = new java.awt.Insets(5,5,5,5);
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(279,202));
        this.add(getJBtnQuit(), gridBagConstraints11);
    }
    
    /**
     * This method initializes jButton4	
     * 	
     * @return javax.swing.JButton	
     */    
    private JButton getJBtnQuit() {
    	if (jBtnQuit == null) {
    		jBtnQuit = new JButton();
            //jBtnQuit.setIcon(new ImageIcon(getClass().getResource("/res/home.gif"))); //$NON-NLS-1$
            jBtnQuit.setText(BTN_QUIT);
            //jBtnQuit.setActionCommand(DlgMoveID.QUIT.toString());            
            //moveBtn.put(DlgMoveID.QUIT,jBtnQuit);
   	}
    	return jBtnQuit;
    }

    public void setListeners(DialoguePlanner planner)
    {
        if (planner==null) return;
        
        DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.QUIT);
        getJBtnQuit().setAction(act);
        getJBtnQuit().addMouseListener(act);
        getJBtnQuit().setToolTipText(null);
    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
