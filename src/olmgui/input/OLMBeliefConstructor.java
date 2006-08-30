/**
 * @file OLMBeliefConstructor.java
 */
package olmgui.input;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.SoftBevelBorder;

import olmgui.OLMMainGUI;
import olmgui.utils.BeliefConstructorButton;
import olmgui.utils.TopicWrapper;
import toulmin.BeliefDesc;

import com.l2fprod.common.swing.plaf.blue.BlueishButtonUI;

import config.OLMTopicConfig;
import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;
import dialogue.DialoguePlanner.DlgMoveAction;
import dialogue.DialoguePlanner.DlgTellMeMore;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.19 $
 *
 */
public class OLMBeliefConstructor extends JPanel 
{
    
//    private String OLMUSER = Messages.getString("OLMConfig.OLMUSER"); //$NON-NLS-1$

//    private final static String TMM_PROMPT = "DlgMove.TellMeMore.Prompt"; //$NON-NLS-1$
//    private final static String TMM_BDESCR = "DlgMove.TellMeMore.DESCRIPTOR"; //$NON-NLS-1$
//    private final static String TMM_BDESCR_NULL = "DlgMove.TellMeMore.DESCRIPTOR.Empty"; //$NON-NLS-1$

    /**
     * 
     */
    private OLMMainGUI sParent=null;
    /**
     * TRUE if the belief descriptor has been modified, FALSE otherwise
     */
    private boolean isModified = false;

    private BeliefConstructorButton jBtnMetacog = null;
    private BeliefConstructorButton jBtnMotiv = null;
    private BeliefConstructorButton jBtnAffect = null;
    private BeliefConstructorButton jBtnCompet = null;
    private BeliefConstructorButton jBtnDomain = null;
    private BeliefConstructorButton jBtnCAPEs = null;

    private JPanel jDescriptor = null;

    private JPanel jCommand = null;

    private JButton jBtnShowMe = null;

    private JButton jBtnTellMe = null;

    private JButton jBtnLost = null;

    private JButton jBtnAgree = null;

    private JButton jBtnDisagree = null;
	private JToolBar jToolBar = null;
	private JToolBar jToolBar1 = null;
    
//    /**
//     * Listener for the "Tell Me More" widget.
//     * 
//     * @todo Implement the explanation!
//     */
//    public class TellMeMoreListener implements ActionListener
//    {
//
//        /* (non-Javadoc)
//         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//         */
//        public void actionPerformed(ActionEvent e)
//        {
//            //String template = MessageFormat.format(TMM_BDESCR,arg);
//
//        	sParent.updateDialogueMove(sParent.getUserName(),Messages.getRandomString(TMM_PROMPT),null);
//            
//            Object[] arg = 
//            {
//            		getBeliefDescriptor()
//            };
//
//            if (isDescriptorValid())
//            	sParent.updateDialogueMove(OLMUSER,Messages.getRandomString(TMM_BDESCR),arg);
//            else
//            	sParent.updateDialogueMove(OLMUSER,Messages.getRandomString(TMM_BDESCR_NULL),null);
//        }
//        
//    }

    /**
     * Local handler for the mouse clicks in the buttons
     */
    private class OLMIconDbClickHandler extends MouseAdapter
    {
        private OLMTopicConfig config = null;
        /**
         * 
         */
        private OLMIconDbClickHandler(OLMTopicConfig attr) {
            super();
            config = attr;
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent evt) {
            super.mouseClicked(evt);
            if (!getButton(config).isEnabled()) return;
            if (evt.getClickCount() == 2)
            {
                //System.out.println("mouseDblClick("+config.toString()+")"); 
                getButton(config).setWrapper(null);
                //checkValidity();
                setModified(true);
            }
        }
        
    }    
    /**
     * 
     */
    public OLMBeliefConstructor() {
        super();

        initialize();
    }

    public void setParent(OLMMainGUI parent)
    {
        sParent = parent;   
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        
        this.setLayout(new BorderLayout());
        this.setSize(new java.awt.Dimension(473,466));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(getJDescriptor(), java.awt.BorderLayout.CENTER);
        this.add(getJCommand(), java.awt.BorderLayout.SOUTH);
    }

    /**
     * This method initializes jButton  
     *  
     * @return javax.swing.JButton  
     */    
    private BeliefConstructorButton getJBtnMetacog() {
        if (jBtnMetacog == null) {
            jBtnMetacog = new BeliefConstructorButton(OLMTopicConfig.METACOG);
            //jBtnMetacog.setPreferredSize(new java.awt.Dimension(100,50));
            jBtnMetacog.setVisible(OLMTopicConfig.METACOG.isVisible());
            jBtnMetacog.addMouseListener(new OLMIconDbClickHandler(OLMTopicConfig.METACOG));
        }
        return jBtnMetacog;
    }

    /**
     * This method initializes jButton1 
     *      
     * @return javax.swing.JButton  
     */    
    private BeliefConstructorButton getJBtnMotiv() {
        if (jBtnMotiv == null) {
            jBtnMotiv = new BeliefConstructorButton(OLMTopicConfig.MOTIV);
            //jBtnMotiv.setText(OLMTopicConfig.MOTIV.toString());
            //jBtnMotiv.setIcon(new TopicIcon(OLMTopicConfig.MOTIV));
            //jBtnMotiv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            //jBtnMotiv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            //jBtnMotiv.setEnabled(false);
            //jBtnMotiv.setPreferredSize(new java.awt.Dimension(100,50));
            jBtnMotiv.setVisible(OLMTopicConfig.MOTIV.isVisible());
            //jBtnMotiv.setRolloverEnabled(false);
            jBtnMotiv.addMouseListener(new OLMIconDbClickHandler(OLMTopicConfig.MOTIV)); 
        }
        return jBtnMotiv;
    }

    /**
     * This method initializes jButton2 
     *  
     * @return javax.swing.JButton  
     */    
    private BeliefConstructorButton getJBtnAffect() {
        if (jBtnAffect == null) {
            jBtnAffect = new BeliefConstructorButton(OLMTopicConfig.AFFECT);
            //jBtnAffect.setText(OLMTopicConfig.AFFECT.toString());
            //jBtnAffect.setIcon(new TopicIcon(OLMTopicConfig.AFFECT));
            //jBtnAffect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            //jBtnAffect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            //jBtnAffect.setEnabled(false);
            //jBtnAffect.setPreferredSize(new java.awt.Dimension(100,50));
            jBtnAffect.setVisible(OLMTopicConfig.AFFECT.isVisible());
            jBtnAffect.addMouseListener(new OLMIconDbClickHandler(OLMTopicConfig.AFFECT)); 
    }
        return jBtnAffect;
    }

    /**
     * This method initializes jButton3 
     *  
     * @return javax.swing.JButton  
     */    
    private BeliefConstructorButton getJBtnCompet() {
        if (jBtnCompet == null) {
            jBtnCompet = new BeliefConstructorButton(OLMTopicConfig.COMPET);
            //jBtnCompet.setText(OLMTopicConfig.COMPET.toString());
            //jBtnCompet.setIcon(new TopicIcon(OLMTopicConfig.COMPET));
            //jBtnCompet.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            //jBtnCompet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            //jBtnCompet.setEnabled(false);
            //jBtnCompet.setPreferredSize(new java.awt.Dimension(100,50));
            jBtnCompet.setVisible(OLMTopicConfig.COMPET.isVisible());
            jBtnCompet.addMouseListener(new OLMIconDbClickHandler(OLMTopicConfig.COMPET)); 
}
        return jBtnCompet;
    }

    /**
     * This method initializes jButton4 
     *  
     * @return javax.swing.JButton  
     */    
    private BeliefConstructorButton getJBtnDomain() {
        if (jBtnDomain == null) {
            jBtnDomain = new BeliefConstructorButton(OLMTopicConfig.DOMAIN);
            //jBtnDomain.setText(OLMTopicConfig.DOMAIN.toString());
            //jBtnDomain.setIcon(new TopicIcon(OLMTopicConfig.DOMAIN));
            //jBtnDomain.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            //jBtnDomain.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            //jBtnDomain.setEnabled(false);
            //jBtnDomain.setPreferredSize(new java.awt.Dimension(50,50));
            jBtnDomain.setVisible(OLMTopicConfig.DOMAIN.isVisible());
            jBtnDomain.addMouseListener(new OLMIconDbClickHandler(OLMTopicConfig.DOMAIN)); 
    }
        return jBtnDomain;
    }

    /**
     * This method initializes jButton5 
     *  
     * @return javax.swing.JButton  
     */    
    private BeliefConstructorButton getJBtnCAPEs() {
        if (jBtnCAPEs == null) {
            jBtnCAPEs = new BeliefConstructorButton(OLMTopicConfig.CAPES);
            //jBtnCAPEs.setText(OLMTopicConfig.CAPES.toString());
            //jBtnCAPEs.setIcon(new TopicIcon(OLMTopicConfig.CAPES));
            //jBtnCAPEs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            //jBtnCAPEs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            //jBtnCAPEs.setEnabled(false);
            //jBtnCAPEs.setPreferredSize(new java.awt.Dimension(150,150));
            jBtnCAPEs.setVisible(OLMTopicConfig.CAPES.isVisible());
            
            jBtnCAPEs.addMouseListener(new OLMIconDbClickHandler(OLMTopicConfig.CAPES)); 
    }
        return jBtnCAPEs;
    }
    private BeliefConstructorButton getButton(OLMTopicConfig attr)
    {   
        if (attr == OLMTopicConfig.METACOG) return jBtnMetacog;
        if (attr == OLMTopicConfig.MOTIV) return jBtnMotiv;
        if (attr == OLMTopicConfig.AFFECT) return jBtnAffect;
        if (attr == OLMTopicConfig.COMPET) return jBtnCompet;
        if (attr == OLMTopicConfig.DOMAIN) return jBtnDomain;
        if (attr == OLMTopicConfig.CAPES) return jBtnCAPEs;
        return null;
    }
    
    
    /**
     * @return <code>true</code> if the belief constructor has been modified,
     *         <code>true</code> false otherwise.
     */
    public boolean isModified() {
        return this.isModified;
    }

    /**
     * @param modified <code>true</code> to indicate that the belief descriptor has 
     *                 been modified, <code>false</code> otherwise. 
     */
    public void setModified(boolean modified) {
        this.isModified = modified;
        sParent.enableMove(DlgMoveID.SHOWME,modified);
    }

    public boolean isDescriptorValid()
    {
        boolean valide = false;
        
        // Check the placeholders
        valide |= getButton(OLMTopicConfig.CAPES).isEnabled();
        valide |= getButton(OLMTopicConfig.METACOG).isEnabled();
        valide |= getButton(OLMTopicConfig.AFFECT).isEnabled();
        valide |= getButton(OLMTopicConfig.COMPET).isEnabled();
        valide |= getButton(OLMTopicConfig.DOMAIN).isEnabled();
        valide |= getButton(OLMTopicConfig.MOTIV).isEnabled();
        
        // Has the belief descriptor been modified?
        valide &= isModified();
        return valide;
    }
    
    public void setLayerAttribute(OLMTopicConfig attr,TopicWrapper id)
    {
        getButton(attr).setWrapper(id);
        //getButton(attr).setEnabled(true);
        
        if (attr==OLMTopicConfig.METACOG)
        {
            getButton(OLMTopicConfig.CAPES).setWrapper(null);
        }
        if (attr==OLMTopicConfig.COMPET)
        {
            getButton(OLMTopicConfig.CAPES).setWrapper(null);
        }
        if (attr==OLMTopicConfig.AFFECT)
        {
            getButton(OLMTopicConfig.MOTIV).setWrapper(null);
            getButton(OLMTopicConfig.CAPES).setWrapper(null);
       }
        if (attr==OLMTopicConfig.MOTIV)
        {
            getButton(OLMTopicConfig.AFFECT).setWrapper(null);
            getButton(OLMTopicConfig.CAPES).setWrapper(null);
       }
        if (attr==OLMTopicConfig.CAPES)
        {
            getButton(OLMTopicConfig.METACOG).setWrapper(null);
            getButton(OLMTopicConfig.MOTIV).setWrapper(null);
            getButton(OLMTopicConfig.AFFECT).setWrapper(null);
            getButton(OLMTopicConfig.COMPET).setWrapper(null);
       }
        //checkValidity();
        setModified(true);
    }
    
    private String getIdFrom(OLMTopicConfig attr)
    {
        String str = null;
        if (getButton(attr).isEnabled())
            str = getButton(attr).getWrapper().getID();
        return str;
    }

    private String getTitleFrom(OLMTopicConfig attr)
    {
        String str = null;
        if (getButton(attr).isEnabled())
        {
            str = getButton(attr).getWrapper().getID();
            TopicWrapper wp = (TopicWrapper)attr.getModel().findElement(str);
            if (wp!=null)
                str = wp.getTitle();
            
        }
        return str;
    }
    
    /**
     * This method is called to get a complete description of the belief 
     * defined in the constructor.
     * @return a 6-slots vector containing the topic Ids (could be null) for each dimension   
     */
    public BeliefDesc getBeliefDescriptor()
    {
        //boolean tt = getJBtnMotiv().isVisible();
        
    	BeliefDesc vec = new BeliefDesc();
        //vec.add(null);
        vec.add(getIdFrom(OLMTopicConfig.DOMAIN));
        vec.add(getIdFrom(OLMTopicConfig.CAPES));
        vec.add(getIdFrom(OLMTopicConfig.COMPET));
        vec.add(getIdFrom(OLMTopicConfig.MOTIV));
        vec.add(getIdFrom(OLMTopicConfig.AFFECT));
        vec.add(getIdFrom(OLMTopicConfig.METACOG));
        
        for (int i=0;i<vec.size();i++)
        {
            if (vec.get(i)==null)
                vec.set(i,""); //$NON-NLS-1$
        }
        
        return vec;
    }

    public void setListeners(DialoguePlanner planner)
    {
        if (planner==null) return;
        
        getJBtnMetacog().addMouseListener(planner.setBeliefConstructorListener());
        getJBtnMotiv().addMouseListener(planner.setBeliefConstructorListener());
        getJBtnAffect().addMouseListener(planner.setBeliefConstructorListener());
        getJBtnCompet().addMouseListener(planner.setBeliefConstructorListener());
        getJBtnCAPEs().addMouseListener(planner.setBeliefConstructorListener());
        getJBtnDomain().addMouseListener(planner.setBeliefConstructorListener());
        
        DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.SHOWME);
        getJBtnShowMe().setAction(act);
        getJBtnShowMe().addMouseListener(act);
        getJBtnShowMe().setToolTipText(null);
        act = (DlgMoveAction) planner.getAction(DlgMoveID.AGREE);
        getJBtnAgree().setAction(act);
        getJBtnAgree().addMouseListener(act);
        getJBtnAgree().setToolTipText(null);
        act = (DlgMoveAction) planner.getAction(DlgMoveID.DISAGREE);
        getJBtnDisagree().setAction(act);
        getJBtnDisagree().addMouseListener(act);
        getJBtnDisagree().setToolTipText(null);
        act = (DlgMoveAction) planner.getAction(DlgMoveID.LOST);
        getJBtnLost().setAction(act);
        getJBtnLost().addMouseListener(act);
        getJBtnLost().setToolTipText(null);
        act = (DlgMoveAction) planner.getAction(DlgMoveID.TELLMORE);
        getJBtnTellMe().setAction(act);
        getJBtnTellMe().addMouseListener(act);
        //getJBtnTellMe().addActionListener(new TellMeMoreListener());
        getJBtnTellMe().addActionListener(planner.setTellMeMoreListener(DlgTellMeMore.DESCRIPTOR));
        getJBtnTellMe().setToolTipText(null);

    }

//    public ActionListener getTellMeMoreListener()
//    {
//        return new TellMeMoreListener();
//    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJDescriptor() {
        if (jDescriptor == null) {
            jDescriptor = new JPanel();
             
            jDescriptor.setLayout(new GridBagLayout());
            jDescriptor.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            
                GridBagConstraints gridBagCAPES = new GridBagConstraints();
                gridBagCAPES.gridx = 3;
                gridBagCAPES.gridy = 0;
                gridBagCAPES.gridheight = 3;
                gridBagCAPES.fill = java.awt.GridBagConstraints.BOTH;
                gridBagCAPES.weightx = 1.0;
                gridBagCAPES.weighty = 0.5;
                gridBagCAPES.insets = new java.awt.Insets(5,5,5,5);
                GridBagConstraints gridBagDomain = new GridBagConstraints();
                gridBagDomain.gridx = 0;
                gridBagDomain.gridy = 4;
                gridBagDomain.fill = java.awt.GridBagConstraints.BOTH;
                gridBagDomain.gridwidth = 4;
                gridBagDomain.weightx = 1.0;
                gridBagDomain.weighty = 0.5;
                gridBagDomain.ipadx = 0;
                gridBagDomain.ipady = 0;
                gridBagDomain.insets = new java.awt.Insets(5,5,5,5);
                GridBagConstraints gridBagCompet = new GridBagConstraints();
                gridBagCompet.gridx = 0;
                gridBagCompet.gridy = 2;
                gridBagCompet.gridwidth = 3;
                gridBagCompet.insets = new java.awt.Insets(5,5,5,5);
                gridBagCompet.weightx = 0.5;
                gridBagCompet.weighty = 0.5;
                gridBagCompet.fill = java.awt.GridBagConstraints.BOTH;
                GridBagConstraints gridBagAffect = new GridBagConstraints();
                gridBagAffect.gridx = 2;
                gridBagAffect.gridy = 1;
                gridBagAffect.insets = new java.awt.Insets(5,5,5,5);
                gridBagAffect.weightx = 0.5;
                gridBagAffect.weighty = 0.5;
                gridBagAffect.fill = java.awt.GridBagConstraints.BOTH;
                GridBagConstraints gridBagMotiv = new GridBagConstraints();
                gridBagMotiv.gridx = 0;
                gridBagMotiv.gridy = 1;
                gridBagMotiv.insets = new java.awt.Insets(5,5,5,5);
                gridBagMotiv.weightx = 0.5;
                gridBagMotiv.weighty = 0.5;
                gridBagMotiv.fill = java.awt.GridBagConstraints.BOTH;
                GridBagConstraints gridBagMetacog = new GridBagConstraints();
                gridBagMetacog.gridx = 0;
                gridBagMetacog.gridy = 0;
                gridBagMetacog.gridwidth = 3;
                gridBagMetacog.insets = new java.awt.Insets(5,5,5,5);
                gridBagMetacog.weightx = 0.5;
                gridBagMetacog.weighty = 0.5;
                gridBagMetacog.fill = java.awt.GridBagConstraints.BOTH;
                jDescriptor.add(getJBtnMetacog(), gridBagMetacog);
                jDescriptor.add(getJBtnMotiv(), gridBagMotiv);
                jDescriptor.add(getJBtnAffect(), gridBagAffect);
                jDescriptor.add(getJBtnDomain(), gridBagDomain);
                jDescriptor.add(getJBtnCompet(), gridBagCompet);
                jDescriptor.add(getJBtnCAPEs(), gridBagCAPES);
                        
        }
        return jDescriptor;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJCommand() {
        if (jCommand == null) {
            jCommand = new JPanel();
            jCommand.setLayout(new BorderLayout());
            jCommand.add(getJToolBar(), BorderLayout.CENTER);
            jCommand.add(getJToolBar1(), BorderLayout.EAST);
        }
        return jCommand;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJBtnShowMe() {
        if (jBtnShowMe == null) {
            jBtnShowMe = new JButton();
            jBtnShowMe.setText(DlgMoveID.SHOWME.toString());
            jBtnShowMe.setIcon(new ImageIcon(getClass().getResource("/res/showme.gif")));
            jBtnShowMe.setUI(new BlueishButtonUI());
        }
        return jBtnShowMe;
    }

    /**
     * This method initializes jButton1	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJBtnTellMe() {
        if (jBtnTellMe == null) {
            jBtnTellMe = new JButton();
            jBtnTellMe.setText(DlgMoveID.TELLMORE.toString());
            jBtnTellMe.setIcon(new ImageIcon(getClass().getResource("/res/tellme.gif")));
            jBtnTellMe.setUI(new BlueishButtonUI());
        }
        return jBtnTellMe;
    }

    /**
     * This method initializes jButton2	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJBtnLost() {
        if (jBtnLost == null) {
            jBtnLost = new JButton();
            jBtnLost.setText(DlgMoveID.LOST.toString());
            jBtnLost.setIcon(new ImageIcon(getClass().getResource("/res/lost.gif")));
            jBtnLost.setUI(new BlueishButtonUI());
        }
        return jBtnLost;
    }

    /**
     * This method initializes jButton3	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJBtnAgree() {
        if (jBtnAgree == null) {
            jBtnAgree = new JButton();
            jBtnAgree.setText(DlgMoveID.AGREE.toString());
            jBtnAgree.setIcon(new ImageIcon(getClass().getResource("/res/agree.gif")));
            jBtnAgree.setUI(new BlueishButtonUI());
       }
        return jBtnAgree;
    }

    /**
     * This method initializes jButton4	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJBtnDisagree() {
        if (jBtnDisagree == null) {
            jBtnDisagree = new JButton();
             jBtnDisagree.setText(DlgMoveID.DISAGREE.toString());
             jBtnDisagree.setIcon(new ImageIcon(getClass().getResource("/res/disagree.gif")));
             jBtnDisagree.setUI(new BlueishButtonUI());
        }
        return jBtnDisagree;
    }

	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			jToolBar = new JToolBar();
            jToolBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),  new SoftBevelBorder(SoftBevelBorder.RAISED)));
            jToolBar.setFloatable(false);
            jToolBar.add(getJBtnShowMe());
            jToolBar.addSeparator();
            jToolBar.add(getJBtnLost());
            jToolBar.add(getJBtnAgree());
            jToolBar.add(getJBtnDisagree());
		}
		return jToolBar;
	}

	/**
	 * This method initializes jToolBar1	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBar1() {
		if (jToolBar1 == null) {
			jToolBar1 = new JToolBar();
			jToolBar1.add(getJBtnTellMe());
            jToolBar1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),  new SoftBevelBorder(SoftBevelBorder.RAISED)));
            jToolBar1.setFloatable(false);
		}
		return jToolBar1;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"  
