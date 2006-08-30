package olmgui.input;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.ImageIcon;

import olmgui.i18n.Messages;


import config.OLMPrefs;

import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;
import dialogue.DialoguePlanner.DlgMoveAction;

public class OLMSwitchBar extends JToolBar {

    /**
     * Shortcuts for the various views used in the OLM.
     * The shortcuts are used both for referencing the view internally and 
     * for accessing the internationalized labels
     */
    final public static String  VIEW_BELIEF = "VIEW_BELIEF",            ///< Reference for the Summary Belief view 
                                VIEW_DISTRIB = "VIEW_DISTRIB",          ///< Reference for the Distribution view
                                VIEW_GRAPH = "VIEW_GRAPH",              ///< Reference for Topic Map view
                                VIEW_EVIDENCE = "VIEW_EVIDENCE",        ///< Reference for the Warrant/Backing view 
                                VIEW_DESCRIPTOR = "VIEW_DESCRIPTOR",    ///< Reference for the Belief Descriptor view
                                VIEW_DISAGREE = "VIEW_DISAGREE",        ///< Reference for the Challenge view
                                VIEW_TOULMIN = "VIEW_TOULMIN";          ///< Reference for the Toulmin view
    final public static String  VIEW_HISTORY = "VIEW_HISTORY";          ///< Reference for the Summary Belief view 
    final public static String  VIEW_PARTITION = "VIEW_PARTITION";      ///< Reference for the Partition view 

    
    private ButtonGroup   jToogleGroup = null;
    private JButton       jHelpButton = null;
    private JToggleButton jToggleDescriptor = null;
    private JToggleButton jToggleTopics = null;
    private JToggleButton jToggleToulmin = null;
    private JToggleButton jToggleHistory = null;

    /**
     * This method initializes 
     * 
     */
    public OLMSwitchBar() {
    	super();
    	initialize();
        
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setFloatable(false);
        this.setSize(new Dimension(31, 223));
        this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.control,5));
        this.setOrientation(javax.swing.JToolBar.VERTICAL);
        this.setPreferredSize(new Dimension(40, 110));
        this.add(getJHelpButton());
        this.addSeparator();
        this.add(getJToggleDescriptor());
        this.add(getJToggleToulmin());
        this.addSeparator();
        if (OLMPrefs.SHOWHISTORY)
        {
            this.add(getJToggleHistory());
            this.addSeparator();
        }
        this.add(getJToggleTopics());
        if (false)
        {
            this.addSeparator();
        }
        
        jToogleGroup = new ButtonGroup();
        jToogleGroup.add(getJToggleDescriptor());
        jToogleGroup.add(getJToggleToulmin());
        jToogleGroup.add(getJToggleHistory());
        jToogleGroup.add(getJToggleTopics());    		
    }
    
    /**
     * Helper used to specify the command, name and attributes (such as tooltip, 
     * icon) of the toogle buttons in the switch bar.
     * @param btn   The button to define
     * @param id    The command of the button
     */
    private void setViewAttributes(JToggleButton btn,String id)
    {
        btn.setName(id);
        btn.setActionCommand(id);
        btn.setToolTipText(Messages.getString("OLMMainGUI." + id + ".name"));
        //btn.setIcon(new Icon());
    }


    /**
     * This method initializes jToggleButton    
     *  
     * @return javax.swing.JToggleButton    
     */
    private JToggleButton getJToggleHistory() {
        if (jToggleHistory == null) {
            jToggleHistory = new JToggleButton();
            setViewAttributes(jToggleHistory,VIEW_HISTORY);

            jToggleHistory.setIcon(new ImageIcon(getClass().getResource("/res/s_history.gif")));
//            ToggleViewListener tt = new ToggleViewListener();
//            jToggleHistory.addActionListener(tt);
//            jToggleHistory.addMouseListener(tt);
            jToggleHistory.setEnabled(OLMPrefs.SHOWHISTORY);
            jToggleHistory.setVisible(OLMPrefs.SHOWHISTORY);
        }
        return jToggleHistory;
    }

    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */    
    private JToggleButton getJToggleTopics() {
        if (jToggleTopics == null) {
            jToggleTopics = new JToggleButton();
            //jToggleGraph.setName(VIEW_GRAPH);
            //jToggleGraph.setToolTipText(TOOLTIP_GRAPH);
            setViewAttributes(jToggleTopics,VIEW_GRAPH);
            jToggleTopics.setIcon(new ImageIcon(getClass().getResource("/res/s_topics.gif")));
            jToggleTopics.setEnabled(true);
//            ToggleViewListener tt = new ToggleViewListener();
//            jToggleTopics.addActionListener(tt);
//            jToggleTopics.addMouseListener(tt);
        }
        return jToggleTopics;
    }
  
    
    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJHelpButton() {
        if (jHelpButton == null) {
            jHelpButton = new JButton();
            jHelpButton.setIcon(new ImageIcon(getClass().getResource("/res/helpOLM.gif")));
        }
        return jHelpButton;
    }

    /**
     * This method initializes jToggleButton    
     *  
     * @return javax.swing.JToggleButton    
     */    
    private JToggleButton getJToggleDescriptor() {
        if (jToggleDescriptor == null) {
            jToggleDescriptor = new JToggleButton();
            //jToggleDescriptor.setName(VIEW_DESCRIPTOR);
            //jToggleDescriptor.setToolTipText(TOOLTIP_BDESCRIPTOR);
            setViewAttributes(jToggleDescriptor,VIEW_DESCRIPTOR);
            jToggleDescriptor.setIcon(new ImageIcon(getClass().getResource("/res/s_descriptor.gif")));
//            ToggleViewListener tt = new ToggleViewListener();
//            jToggleDescriptor.addActionListener(tt);
//            jToggleDescriptor.addMouseListener(tt);
            jToggleDescriptor.setSelected(true);
        }
        return jToggleDescriptor;
    }

    
    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */    
    private JToggleButton getJToggleToulmin() {
        if (jToggleToulmin == null) {
            jToggleToulmin = new JToggleButton();
            //jToggleGraph.setName(VIEW_GRAPH);
            //jToggleGraph.setToolTipText(TOOLTIP_GRAPH);
            setViewAttributes(jToggleToulmin,VIEW_TOULMIN);
            jToggleToulmin.setIcon(new ImageIcon(getClass().getResource("/res/s_toulmin.gif")));
            jToggleToulmin.setEnabled(false);
//            ToggleViewListener tt = new ToggleViewListener();
//            jToggleToulmin.addActionListener(tt);
//            jToggleToulmin.addMouseListener(tt);

        }
        return jToggleToulmin;
    }    
    
    public void initActions(DialoguePlanner planner)
    {
        if (planner==null) return;
        
        DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.ABOUT);
        act.setPermanent(true);
        getJHelpButton().setAction(act);
        getJHelpButton().addMouseListener(act);//((MouseListener) planner.setMoveSelectorListener());
        getJHelpButton().setText("");
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
