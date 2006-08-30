/**
 * @file OLMChallengePane.java
 */
package olmgui.input;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeListener;
import javax.swing.text.StyledDocument;

import olmgui.i18n.Messages;
import olmgui.utils.JIconComboBox;
import olmgui.utils.OLMFormatTemplate;
import toulmin.BeliefDesc;
import config.OLMQueryResult;
import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;
import dialogue.DialoguePlanner.DlgMoveAction;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.l2fprod.common.swing.plaf.blue.BlueishButtonUI;

/** 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.14 $
 */
public class OLMChallengePane extends JPanel {

    private String 
            LBL_OLMTHINK = Messages.getString("OLMChallengePane.Claim.OLMClaim"), //$NON-NLS-1$
            LBL_USERTHINK = Messages.getString("OLMChallengePane.Claim.UserClaim.name"), //$NON-NLS-1$
            LBL_CONIDENCE = Messages.getString("OLMChallengePane.Claim.Confidence.name"), //$NON-NLS-1$
            LBL_INTRANSIG = Messages.getString("OLMChallengePane.Claim.Intransigence.name"), //$NON-NLS-1$
            BTN_ACCEPT = Messages.getString("OLMMoveSelector.ACCEPT.name"), //$NON-NLS-1$
            BTN_REJECT = Messages.getString("OLMMoveSelector.REJECT.name"), //$NON-NLS-1$
            BTN_MOVEON = Messages.getString("OLMMoveSelector.MOVEON.name"); //$NON-NLS-1$  //  @jve:decl-index=0:
    
    public final static String VIEW_CLAIM = "CHALLENGE_CLAIM";
    public final static String VIEW_EVIDENCE = "CHALLENGE_EVIDENCE";
    public final static String VIEW_EVENT = "CHALLENGE_EVENT";
    
    private class JSliderLabel extends JLabel
    {
        /**
         * @param text
         */
        private JSliderLabel(String text) {
            super(text);
            Font ft = getFont();
            Font ftnew = ft.deriveFont(Font.BOLD);
            setFont(ftnew);
        }
        
    }
       
    private JPanel jClaimPanel = null;
    private JLabel jLabel3 = null;
    private JLabel jConfidenceLbl = null;
    private JSlider jConfidenceSlider = null;
    private JLabel jIntransigenceLbl = null;
    
    //ButtonGroup adamantGroup = null;

    private JPanel jEvidencePanel = null;

    private JPanel jEventPanel = null;

    private JPanel jCardPanel = null;

    private JToolBar jToolBar = null; 

    private JButton jAcceptBtn = null;
    private JButton jMoveOnBtn = null;
    private JButton jRejectBtn = null;
    
    private String selectView = null;

    private JLabel jLabel2 = null;

    private JIconComboBox jAttributeComboBox = null;

    private JLabel jLabel6 = null;

    private JLabel jValueLabel = null;

    private JLabel jLabel8 = null;

    private JPanel jCommonPanel = null;

    private JPanel jConfidencePanel = null;

    private JIconComboBox jClaimComboBox = null;

    private JComboBox jValueComboBox = null;

    private JSlider jIntransigenceSlider = null;

    private JLabel jLabel1 = null;

    private JLabel jLabel4 = null;

	private JTextPane jTextPane = null;
           
    /**
     * 
     */
    public OLMChallengePane() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 350);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.setPreferredSize(new Dimension(300, 350));
        this.add(getJCardPanel(), java.awt.BorderLayout.CENTER);
        this.add(getJCommonPanel(), java.awt.BorderLayout.SOUTH);
        
    }

    /**
     * This method initializes jPanel2	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJClaimPanel() {
        if (jClaimPanel == null) {
            
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.gridy = 0;
            gridBagConstraints15.weightx = 1.0;
            gridBagConstraints15.weighty = 0.0;
            gridBagConstraints15.gridwidth = 1;
            gridBagConstraints15.insets = new Insets(0, 0, 10, 0);
            gridBagConstraints15.gridx = 0;
            jLabel3 = new JLabel();
            jLabel3.setText(LBL_USERTHINK);
            
            
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints21.gridy = 2;
            gridBagConstraints21.weightx = 0.0;
            gridBagConstraints21.anchor = GridBagConstraints.WEST;
            gridBagConstraints21.ipadx = 75;
            gridBagConstraints21.insets = new Insets(5, 20, 0, 0);
            gridBagConstraints21.gridwidth = 1;
            gridBagConstraints21.gridx = 0;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.gridwidth = 3;
            gridBagConstraints8.insets = new java.awt.Insets(10,10,10,10);
            gridBagConstraints8.gridy = 9;

        
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 1;
            
            jClaimPanel = new JPanel();
            jClaimPanel.setLayout(new GridBagLayout());
            jClaimPanel.add(jLabel3, gridBagConstraints);
            jClaimPanel.add(getJClaimComboBox(), gridBagConstraints21);
            jClaimPanel.add(getJTextPane(), gridBagConstraints15);
        }
        return jClaimPanel;
    }

    /**
     * This method initializes jSlider	
     * 	
     * @return javax.swing.JSlider	
     */
    private JSlider getJConfidenceSlider() {
        if (jConfidenceSlider == null) {
            jConfidenceSlider = new JSlider();
            jConfidenceSlider.setName("OLMChallengePane.Claim.Confidence");
            jConfidenceSlider.setMinimum(10);
            jConfidenceSlider.setMaximum(90);
            jConfidenceSlider.setValue(50);

            // Show tick marks
            jConfidenceSlider.setPaintTicks(true);
            jConfidenceSlider.setMajorTickSpacing(20);
            jConfidenceSlider.setMinorTickSpacing(5);
            jConfidenceSlider.setPaintLabels(true);
            jConfidenceSlider.setValueIsAdjusting(true);
            jConfidenceSlider.setSnapToTicks(true);
        }
        return jConfidenceSlider;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJAcceptBtn() {
        if (jAcceptBtn == null) {
            jAcceptBtn = new JButton();
            jAcceptBtn.setText(BTN_ACCEPT);
            jAcceptBtn.setUI(new BlueishButtonUI());
            //jAcceptBtn.setIcon(new ImageIcon(getClass().getResource("/res/agree.gif")));
            //jAcceptBtn.setActionCommand(DlgMoveID.ACCEPT.toString());            
        }
        return jAcceptBtn;
    }

    public void setListeners(DialoguePlanner planner)
    {
        if (planner==null) return;
        
        DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.ACCEPT);
        getJAcceptBtn().setAction(act);
        getJAcceptBtn().setToolTipText(null);
        getJAcceptBtn().addMouseListener(act);

        act = (DlgMoveAction) planner.getAction(DlgMoveID.REJECT);
        getJRejectBtn().setAction(act);
        getJRejectBtn().setToolTipText(null);
        getJRejectBtn().addMouseListener(act);

        act = (DlgMoveAction) planner.getAction(DlgMoveID.MOVEON);
        getJMoveOnBtn().setAction(act);
        getJMoveOnBtn().setToolTipText(null);
        getJMoveOnBtn().addMouseListener(act);
        
//        getJAcceptBtn().addActionListener((ActionListener) planner.setMoveSelectorListener());
//        getJAcceptBtn().addMouseListener((MouseListener) planner.setMoveSelectorListener());
//        getJRejectBtn().addActionListener((ActionListener) planner.setMoveSelectorListener());
//        getJRejectBtn().addMouseListener((MouseListener) planner.setMoveSelectorListener());
//        getJMoveOnBtn().addActionListener((ActionListener) planner.setMoveSelectorListener());
//        getJMoveOnBtn().addMouseListener((MouseListener) planner.setMoveSelectorListener());
        
        getJConfidenceSlider().addChangeListener((ChangeListener) planner.setChallengeListener());
        getJConfidenceSlider().addMouseListener((MouseListener) planner.setChallengeListener());
        getJIntransigenceSlider().addMouseListener((MouseListener) planner.setChallengeListener());
        getJClaimComboBox().addMouseListener((MouseListener) planner.setChallengeListener());
    }
    
    private void activateView(String viewName)
    {
        CardLayout cl = (CardLayout)(getJCardPanel().getLayout());
        cl.show(getJCardPanel(),viewName);
        selectView = viewName;
    }

    
    public void setClaim(String viewName,BeliefDesc bdesc,int lvl)
    {
        activateView(viewName);
        
        String lbl = Messages.getJudgementOn(bdesc,lvl);
        
        Hashtable dic = new Hashtable();
        dic.put(new Integer(10),new JSliderLabel(Messages.getString(jConfidenceSlider.getName() + ".low")));
        dic.put(new Integer(90),new JSliderLabel(Messages.getString(jConfidenceSlider.getName() + ".high")));
        dic.put(new Integer(50),new JSliderLabel(Messages.getString(jConfidenceSlider.getName() + ".medium")));
        jConfidenceSlider.setLabelTable(dic);
        
        dic = new Hashtable();
        dic.put(new Integer(10),new JSliderLabel(Messages.getString(jConfidenceSlider.getName() + ".low")));
        dic.put(new Integer(90),new JSliderLabel(Messages.getString(jConfidenceSlider.getName() + ".high")));
        dic.put(new Integer(50),new JSliderLabel(Messages.getString(jConfidenceSlider.getName() + ".medium")));
        jIntransigenceSlider.setLabelTable(dic);

        String[] strings={
                Messages.getJudgementOn(bdesc,1),
                Messages.getJudgementOn(bdesc,2),
                Messages.getJudgementOn(bdesc,3),
                Messages.getJudgementOn(bdesc,4)};
            
        ImageIcon[] imagesLevel = {
                new ImageIcon(getClass().getResource("/res/level1.gif")),
                new ImageIcon(getClass().getResource("/res/level2.gif")),
                new ImageIcon(getClass().getResource("/res/level3.gif")),
                new ImageIcon(getClass().getResource("/res/level4.gif"))
        };
        
        StyledDocument doc = (StyledDocument)getJTextPane().getDocument();
        getJTextPane().setText(null);
        Object param[] = {
        		lbl,
        		bdesc
        };
        OLMFormatTemplate.formatTemplate(doc, "user", LBL_OLMTHINK, param);

        jClaimComboBox.setData(strings,imagesLevel);
        jClaimComboBox.setSelectedIndex(lvl-1);
        jIntransigenceSlider.setValue(50);
        jConfidenceSlider.setValue(50);
        updateUI();
    }
    
    public void setEvidence(int nEvidence)
    {
        
    }
    
    public void setAttribute()
    {
        
    }
    
    public Hashtable getOutcome()
    {
        Hashtable hash = new Hashtable();
        if (selectView.equals(VIEW_CLAIM))
        {
            hash.put(OLMQueryResult.CHALLENGE_TYPE,OLMQueryResult.CHALLENGE_CLAIM);
            Integer lvl = (Integer)jClaimComboBox.getSelectedItem();
            
            //lvl = new Integer(lvl.intValue()+1);
            Double summary = new Double(lvl.intValue()*.33);
            //String lvl = levelGroup.getSelection().getActionCommand();
            int iconf = getJConfidenceSlider().getValue();
            int iintr = getJIntransigenceSlider().getValue();
            Double conf = new Double((double)iconf/100.);
            //Integer intr = new Integer(Integer.parseInt(adamantGroup.getSelection().getActionCommand()));
            Double intr = new Double((double)iintr/100.);
            hash.put(OLMQueryResult.CHALLENGE_LEVEL,summary);
            if (conf!=null) hash.put(OLMQueryResult.CHALLENGE_CONFIDENCE,conf);
            if (intr!=null) hash.put(OLMQueryResult.CHALLENGE_INTRANSIG,intr);
            
            
        }
        return hash;
    }
    
//  This method returns the selected radio button in a button group
    public static JRadioButton getSelection(ButtonGroup group) {
        for (Enumeration e=group.getElements(); e.hasMoreElements(); ) {
            JRadioButton b = (JRadioButton)e.nextElement();
            if (b.getModel() == group.getSelection()) {
                return b;
            }
        }
        return null;
    }
    
    /**
     * This method initializes jEvidencePanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJEvidencePanel() {
        if (jEvidencePanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.insets = new java.awt.Insets(5,0,5,0);
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints1.ipadx = 15;
            gridBagConstraints1.gridx = 2;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridwidth = 2;
            gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints13.insets = new java.awt.Insets(5,5,5,5);
            gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.gridy = 1;
            jLabel8 = new JLabel();
            jLabel8.setText("So what do you think it was?");
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 3;
            gridBagConstraints12.weightx = 0.5;
            gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints12.insets = new java.awt.Insets(5,0,5,5);
            gridBagConstraints12.gridy = 0;
            jValueLabel = new JLabel();
            jValueLabel.setText("fdfdf");
            Font ft = jValueLabel.getFont();
            Font nft = ft.deriveFont(Font.BOLD);
            jValueLabel.setFont(nft);

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 2;
            gridBagConstraints11.insets = new java.awt.Insets(0,5,0,5);
            gridBagConstraints11.gridy = 0;
            jLabel6 = new JLabel();
            jLabel6.setText("was");
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridy = 0;
            gridBagConstraints10.weightx = 0.0;
            gridBagConstraints10.insets = new java.awt.Insets(5,0,5,0);
            gridBagConstraints10.ipadx = 15;
            gridBagConstraints10.gridx = 1;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.insets = new java.awt.Insets(0,5,0,5);
            gridBagConstraints9.gridy = 0;
            jLabel2 = new JLabel();
            jLabel2.setText("I think that");
            jEvidencePanel = new JPanel();
            jEvidencePanel.setLayout(new GridBagLayout());
            jEvidencePanel.setName("jEvidencePanel");
            jEvidencePanel.add(jLabel2, gridBagConstraints9);
            jEvidencePanel.add(getJAttributeComboBox(), gridBagConstraints10);
            jEvidencePanel.add(jLabel6, gridBagConstraints11);
            jEvidencePanel.add(jValueLabel, gridBagConstraints12);
            jEvidencePanel.add(jLabel8, gridBagConstraints13);
            jEvidencePanel.add(getJValueComboBox(), gridBagConstraints1);
        }
        return jEvidencePanel;
    }

    /**
     * This method initializes jEventPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJEventPanel() {
        if (jEventPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints4.insets = new java.awt.Insets(5,5,5,5);
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.gridy = 1;
            jLabel4 = new JLabel();
            jLabel4.setText("But you obviously don't and are willing to get rid of it.");
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints3.insets = new java.awt.Insets(5,5,5,5);
            gridBagConstraints3.gridy = 0;
            jLabel1 = new JLabel();
            jLabel1.setText("I Think this piece of evidence is important.");
            jEventPanel = new JPanel();
            jEventPanel.setLayout(new GridBagLayout());
            jEventPanel.setName("jEventPanel");
            jEventPanel.add(jLabel1, gridBagConstraints3);
            jEventPanel.add(jLabel4, gridBagConstraints4);
        }
        return jEventPanel;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJCardPanel() {
        if (jCardPanel == null) {
            jCardPanel = new JPanel();
            jCardPanel.setBorder(BorderFactory.createLineBorder(SystemColor.control, 10));
            jCardPanel.setLayout(new CardLayout());
            jCardPanel.add(getJClaimPanel(), VIEW_CLAIM);
            jCardPanel.add(getJEvidencePanel(), VIEW_EVIDENCE);
            jCardPanel.add(getJEventPanel(), VIEW_EVENT);
        }
        return jCardPanel;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JToolBar getJToolBar() 
    {
        if (jToolBar == null) 
        {
            jToolBar = new JToolBar();
            //jToolBar.setFloatable(false);
            jToolBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),  new SoftBevelBorder(SoftBevelBorder.RAISED)));
            jToolBar.setFloatable(false);
            jToolBar.add(getJAcceptBtn());
            jToolBar.add(getJRejectBtn());
            jToolBar.add(getJMoveOnBtn());
        }
        return jToolBar;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJMoveOnBtn() {
        if (jMoveOnBtn == null) {
            jMoveOnBtn = new JButton();
            jMoveOnBtn.setUI(new BlueishButtonUI());
            jMoveOnBtn.setText(BTN_MOVEON);
            //jMoveOnBtn.setIcon(new ImageIcon(getClass().getResource("/res/moveon.gif")));
            //jMoveOnBtn.setActionCommand(DlgMoveID.MOVEON.toString());            
       }
        return jMoveOnBtn;
    }

    /**
     * This method initializes jButton1	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJRejectBtn() {
        if (jRejectBtn == null) {
            jRejectBtn = new JButton();
            jRejectBtn.setText(BTN_REJECT);
            jRejectBtn.setUI(new BlueishButtonUI());
            //jRejectBtn.setIcon(new ImageIcon(getClass().getResource("/res/disagree.gif"))); //$NON-NLS-1$
            jRejectBtn.setEnabled(false);
            jRejectBtn.setVisible(false);
            //jRejectBtn.setActionCommand(DlgMoveID.REJECT.toString());            
        }
        return jRejectBtn;
    }

    /**
     * This method initializes jComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJAttributeComboBox() {
        if (jAttributeComboBox == null) {
            String[] strings = {
                    Messages.getString("ATTRIBUTE." + OLMQueryResult.EVIDENCE_DIFF),
                    Messages.getString("ATTRIBUTE." + OLMQueryResult.EVIDENCE_PERF)
                    };
            
            jAttributeComboBox = new JIconComboBox();
            jAttributeComboBox.setData(strings,null);
            //jComboBox.addItem(strings[0]);
            //jComboBox.addItem(strings[1]);
            Font ft = jAttributeComboBox.getFont();
            Font nft = ft.deriveFont(Font.BOLD);
            jAttributeComboBox.setFont(nft);
            jAttributeComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    //System.out.println("itemStateChanged()"); 
                }
            });
        }
        return jAttributeComboBox;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJCommonPanel() {
        if (jCommonPanel == null) {
            jCommonPanel = new JPanel();
            jCommonPanel.setLayout(new BorderLayout());
            jCommonPanel.add(getJToolBar(), java.awt.BorderLayout.SOUTH);
            jCommonPanel.add(getJConfidencePanel(), java.awt.BorderLayout.CENTER);
        }
        return jCommonPanel;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJConfidencePanel() {
        if (jConfidencePanel == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 3;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new Insets(5, 0, 5, 0);
            gridBagConstraints2.gridx = 0;
            jConfidencePanel = new JPanel();
            jConfidencePanel.setLayout(new GridBagLayout());
            
            GridBagConstraints gridBagIntransigLbl = new GridBagConstraints();
            gridBagIntransigLbl.gridx = 0;
            gridBagIntransigLbl.weightx = 0.0;
            gridBagIntransigLbl.anchor = java.awt.GridBagConstraints.WEST;
            gridBagIntransigLbl.insets = new Insets(10, 0, 0, 0);
            gridBagIntransigLbl.gridy = 2;
            jIntransigenceLbl = new JLabel();
            jIntransigenceLbl.setText(LBL_INTRANSIG);
            GridBagConstraints gridBagConfidence = new GridBagConstraints();
            gridBagConfidence.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConfidence.gridy = 1;
            gridBagConfidence.weightx = 1.0;
            gridBagConfidence.insets = new Insets(5, 0, 5, 0);
            gridBagConfidence.gridwidth = 1;
            gridBagConfidence.gridx = 0;
            GridBagConstraints gridBagConfLbl = new GridBagConstraints();
            gridBagConfLbl.gridx = 0;
            gridBagConfLbl.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConfLbl.insets = new Insets(10, 0, 0, 0);
            gridBagConfLbl.gridwidth = 1;
            gridBagConfLbl.gridy = 0;
            jConfidenceLbl = new JLabel();
            jConfidenceLbl.setBorder(BorderFactory.createLineBorder(SystemColor.control, 10));
            jConfidenceLbl.setText(LBL_CONIDENCE);
            jConfidencePanel.add(jConfidenceLbl, gridBagConfLbl);
            jConfidencePanel.add(getJConfidenceSlider(), gridBagConfidence);
            jConfidencePanel.add(jIntransigenceLbl, gridBagIntransigLbl);
            jConfidencePanel.add(getJIntransigenceSlider(), gridBagConstraints2);
            
        }
        return jConfidencePanel;
    }

    /**
     * This method initializes jComboBox1	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JIconComboBox getJClaimComboBox() {
        if (jClaimComboBox == null) {
            jClaimComboBox = new JIconComboBox();
            jClaimComboBox.setName("OLMChallengePane.Claim.UserClaim");

            //jComboBox1.setSelectedIndex(-1);
            Font ft = jClaimComboBox.getFont();
            Font nft = ft.deriveFont(Font.BOLD);
            jClaimComboBox.setFont(nft);

            ImageIcon[] imagesLevel = {
                    new ImageIcon(getClass().getResource("/res/level1.gif")),
                    new ImageIcon(getClass().getResource("/res/level2.gif")),
                    new ImageIcon(getClass().getResource("/res/level3.gif")),
                    new ImageIcon(getClass().getResource("/res/level4.gif"))
            };
            
            String[] strings={"","","",""};
            jClaimComboBox.setData(strings,imagesLevel);
            
            
        }
        return jClaimComboBox;
    }

    /**
     * This method initializes jComboBox2	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJValueComboBox() {
        if (jValueComboBox == null) {
            jValueComboBox = new JComboBox();
            Font ft = jValueComboBox.getFont();
            Font nft = ft.deriveFont(Font.BOLD);
            jValueComboBox.setFont(nft);
        }
        return jValueComboBox;
    }

    /**
     * This method initializes jSlider	
     * 	
     * @return javax.swing.JSlider	
     */
    private JSlider getJIntransigenceSlider() {
        if (jIntransigenceSlider == null) {
            jIntransigenceSlider = new JSlider();
            jIntransigenceSlider.setName("OLMChallengePane.Claim.Intransigence");
            jIntransigenceSlider.setMinimum(10);
            jIntransigenceSlider.setMaximum(90);
            jIntransigenceSlider.setValue(50);
                       
            // Show tick marks
            jIntransigenceSlider.setPaintTicks(true);
            jIntransigenceSlider.setMajorTickSpacing(20);
            jIntransigenceSlider.setMinorTickSpacing(5);
            jIntransigenceSlider.setPaintLabels(true);
            jIntransigenceSlider.setValueIsAdjusting(true);
            jIntransigenceSlider.setSnapToTicks(true);

        }
        return jIntransigenceSlider;
    }

	/**
	 * This method initializes jTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getJTextPane() {
		if (jTextPane == null) {
			jTextPane = new JTextPane();
			jTextPane.setText("I think ...");
			jTextPane.setBackground(SystemColor.control);
		}
		return jTextPane;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"  
