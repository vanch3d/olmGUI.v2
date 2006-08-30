package olmgui.output;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.SoftBevelBorder;

import olmgui.OLMMainGUI;
import olmgui.graph.NodeConfig;
import olmgui.graph.OLMToulminGraph;
import olmgui.graph.ToulminNode;
import olmgui.input.OLMChallengePane;
import olmgui.utils.OutputViewPanel;
import toulmin.BeliefDesc;
import toulmin.Toulmin;
import toulmin.ToulminBacking;
import toulmin.ToulminSubClaim;
import toulmin.ToulminWarrant;

import com.l2fprod.common.swing.plaf.blue.BlueishButtonUI;

import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;
import dialogue.DialoguePlanner.DlgMoveAction;
import dialogue.DialoguePlanner.DlgSwapView;
import dialogue.DialoguePlanner.DlgTellMeMore;

public class OLMArgumentView extends JSplitPane 
{
    /**
     * A reference to the dialogue planner associated with the OLM.
     */
    private DialoguePlanner planner = null;

    private static final long serialVersionUID = 1L;
    private OLMToulminGraph jToulminGraph = null;
    private JPanel jToulminPanel = null;
    private JToolBar jToolBar = null;
    private JButton jAgreeBtn = null;
    private JPanel jViewPanel = null;
    private JButton jBaffledBtn = null;
    private JButton jDisagreeBtn = null;
    private JButton jMoveOnBtn = null;

    private OLMChallengePane jChallengePane = null;
    //private OLMBeliefPane jBeliefPane = null;
    private OLMChartPanel jSummaryPane = null;
    private OLMChartPanel jDistributionPane = null;
    private OLMChartPanel jPartitionPane = null;
    private OutputViewPanel jEvidencePane = null;
    
    
    /**
     * This is the default constructor
     */
    public OLMArgumentView(DialoguePlanner pl) {
        super();
        planner = pl;
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(500, 400);

        this.setEnabled(true);
        this.setDividerSize(10);
        this.setOneTouchExpandable(false);
        this.setContinuousLayout(true);
        this.setRightComponent(getJViewPanel());
        this.setLeftComponent(getJToulminPanel());
        this.setDividerLocation(250);
        this.setDividerLocation(0.99D);
        this.setResizeWeight(0.99);
    }

    /**
     * This method initializes OLMToulminGraph	
     * 	
     * @return olmgui.graph.OLMToulminGraph	
     */
    private OLMToulminGraph getOLMToulminGraph()
    {
        if (jToulminGraph == null)
        {

        	String str = null;//getUserName();
        	if (str==null) str = "";
        	jToulminGraph = new OLMToulminGraph();
        	jToulminGraph.setName("jToulminGraph");
        	//jToulminGraph.setTopNode(str);
        	//jToulminGraph.setImageList(imageList);
        	jToulminGraph.setListeners(planner);
        	jToulminGraph.initialize();        
        }
        return jToulminGraph;
    }

    /**
     * This method initializes jToulminPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJToulminPanel()
    {
        if (jToulminPanel == null) 
        {
            jToulminPanel = new JPanel();
            jToulminPanel.setLayout(new BorderLayout());
            jToulminPanel.add(getJToolBar(), BorderLayout.SOUTH);
            jToulminPanel.add(getOLMToulminGraph(), BorderLayout.CENTER);
        }
        return jToulminPanel;
    }

    /**
     * This method initializes jToolBar	
     * 	
     * @return javax.swing.JToolBar	
     */
    private JToolBar getJToolBar()
    {
        if (jToolBar == null)
        {
            jToolBar = new JToolBar();
            jToolBar.setFloatable(false);
            jToolBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0),  new SoftBevelBorder(SoftBevelBorder.RAISED)));

            jToolBar.add(getJBaffledBtn());
            jToolBar.addSeparator();
            jToolBar.add(getJAgreeBtn());
            jToolBar.add(getJDisagreeBtn());
            jToolBar.add(getJMoveOnBtn());
        }
        return jToolBar;
    }

    /**
     * This method initializes jAgreeBtn	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJAgreeBtn() {
        if (jAgreeBtn == null) {
            jAgreeBtn = new JButton();
            jAgreeBtn.setUI(new BlueishButtonUI());
            jAgreeBtn.setIcon(new ImageIcon(getClass().getResource("/res/agree.gif")));
            DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.AGREE);
            jAgreeBtn.setAction(act);
            jAgreeBtn.addMouseListener(act);
            //jButton.setToolTipText(null);
            //jAgreeBtn.setText(null);

        }
        return jAgreeBtn;
    }

    /**
     * This method initializes jViewPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJViewPanel() {
        if (jViewPanel == null) {
            jViewPanel = new JPanel();
            jViewPanel.setLayout(new CardLayout());
            jViewPanel.add(getOLMEvidencePane(), OLMMainGUI.VIEW_EVIDENCE);
            jViewPanel.add(getOLMSummaryPane(), OLMMainGUI.VIEW_BELIEF);
            jViewPanel.add(getOLMDistributionPane(), OLMMainGUI.VIEW_DISTRIB);
            jViewPanel.add(getOLMPartitionPane(), OLMMainGUI.VIEW_PARTITION);
            jViewPanel.add(getOLMChallengePane(), OLMMainGUI.VIEW_DISAGREE);
        }
        return jViewPanel;
    }

    /**
     * This method initializes jBaffledBtn	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJBaffledBtn() {
        if (jBaffledBtn == null) {
            jBaffledBtn = new JButton();
            jBaffledBtn.setUI(new BlueishButtonUI());
            jBaffledBtn.setIcon(new ImageIcon(getClass().getResource("/res/baffled.gif")));
            DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.BAFFLED);
            jBaffledBtn.setAction(act);
            jBaffledBtn.addMouseListener(act);
            //jBaffledBtn.setToolTipText(null);
            //jBaffledBtn.setText(null);
        }
        return jBaffledBtn;
    }

    /**
     * This method initializes jDisagreeBtn	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJDisagreeBtn() {
        if (jDisagreeBtn == null) {
            jDisagreeBtn = new JButton();
            jDisagreeBtn.setUI(new BlueishButtonUI());
            jDisagreeBtn.setIcon(new ImageIcon(getClass().getResource("/res/disagree.gif")));
            DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.DISAGREE);
            jDisagreeBtn.setAction(act);
            jDisagreeBtn.addMouseListener(act);
            //jButton2.setToolTipText(null);
            //jDisagreeBtn.setText(null);

        }
        return jDisagreeBtn;
    }

    /**
     * This method initializes jMoveOnBtn	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJMoveOnBtn() {
        if (jMoveOnBtn == null) {
            jMoveOnBtn = new JButton();
            jMoveOnBtn.setUI(new BlueishButtonUI());
            jMoveOnBtn.setIcon(new ImageIcon(getClass().getResource("/res/moveon.gif")));
            DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.MOVEON);
            jMoveOnBtn.setAction(act);
            jMoveOnBtn.addMouseListener(act);
            //jButton3.setToolTipText(null);
            //jMoveOnBtn.setText(null);
       }
        return jMoveOnBtn;
    }

    /**
     * This method initializes OLMDistributionPane	
     * 	
     * @return olmgui.output.OLMDistributionPane	
     */
    private OLMChartPanel getOLMDistributionPane() {
        if (jDistributionPane == null) {
            //jDistributionPane = new OLMDistributionPane();
            jDistributionPane = new ChartDistribution();
            jDistributionPane.addSwap(ChartDistribution.PIGNISTIC);
            jDistributionPane.addSwap(ChartDistribution.MASSDISTRIB);
            
            DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.TELLMORE);
            jDistributionPane.addTellMeListener((MouseListener)act);
            jDistributionPane.addTellMeListener(planner.setTellMeMoreListener(DlgTellMeMore.BELIEF));

            act = (DlgMoveAction) planner.getAction(DlgMoveID.SWAP);
            jDistributionPane.addSwapListener((MouseListener)act);
            jDistributionPane.addSwapListener(planner.setSwapViewListerner(DlgSwapView.BELIEF,jDistributionPane));
        }
        return jDistributionPane;
    }

    private OutputViewPanel getOLMEvidencePane()
    {
    	if (jEvidencePane==null)
    	{
    		jEvidencePane = new ChartEvidence();
    		jEvidencePane.addSwap(ChartEvidence.EVIDENCE);
    		jEvidencePane.addSwap(ChartEvidence.ATTRIBUTE);

    		DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.TELLMORE);
    		jEvidencePane.addTellMeListener((MouseListener)act);
    		jEvidencePane.addTellMeListener(planner.setTellMeMoreListener(DlgTellMeMore.EVIDENCE));

            act = (DlgMoveAction) planner.getAction(DlgMoveID.SWAP);
            jEvidencePane.addSwapListener((MouseListener)act);
            jEvidencePane.addSwapListener(planner.setSwapViewListerner(DlgSwapView.EVIDENCE,jEvidencePane));
            jEvidencePane.setListeners(planner);
    	}
    	return jEvidencePane;
    }

    private OLMChartPanel getOLMPartitionPane()
    {
    	if (jPartitionPane==null)
    	{
    		jPartitionPane = new ChartPartition();
    		//jPartitionPane = new ChartHistory();
    		//jPartitionPane = new ChartBubble();
    		
    		jPartitionPane.addSwap(ChartPartition.PARTITION);
    		jPartitionPane.addSwap(ChartPartition.SCATTER);

    		DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.TELLMORE);
    		jPartitionPane.addTellMeListener((MouseListener)act);
    		jPartitionPane.addTellMeListener(planner.setTellMeMoreListener(DlgTellMeMore.PARTITION));

    		act = (DlgMoveAction) planner.getAction(DlgMoveID.SWAP);
    		jPartitionPane.addSwapListener((MouseListener)act);
    		jPartitionPane.addSwapListener(planner.setSwapViewListerner(DlgSwapView.PARTITION,jPartitionPane));
    		jPartitionPane.setListeners(planner);

    	}
    	return jPartitionPane;
    }
    
    private OLMChartPanel getOLMSummaryPane()
    {
    	if (jSummaryPane==null)
    	{
    		jSummaryPane = new ChartSummary();
    		DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.TELLMORE);
    		jSummaryPane.addTellMeListener((MouseListener)act);
    		jSummaryPane.addTellMeListener(planner.setTellMeMoreListener(DlgTellMeMore.SUMMARY));
    	}
    	return jSummaryPane;
    }
    /**
     * This method initializes OLMChallengePane	
     * 	
     * @return olmgui.input.OLMChallengePane	
     */
    private OLMChallengePane getOLMChallengePane() {
        if (jChallengePane == null) {
            jChallengePane = new OLMChallengePane();
            jChallengePane.setListeners(planner);

        }
        return jChallengePane;
    }

    public void setEvidence(String target, ToulminWarrant warrant)
    {
        //getOLMWarrantPane().setEvidence(target, warrant);
    }
    
    public void setPartition(ToulminNode node)
    {
    	getOLMPartitionPane().setSelectedNode(node);
    }
    
//    public void setBeliefData(BeliefDesc belief,Double summary,ArrayList list)
//    {
//        //jBeliefPane.setData(belief, summary, list);
//        //jBeliefPane.setData(belief, summary, list);
//    }
    
//    public void setDistributionData(ArrayList distribution,ArrayList confidence,ArrayList pignistic,ArrayList discount)
//    {
//        //getOLMDistributionPane().setData(distribution, confidence, pignistic, discount);
//    }
    public void setDistributionData(Toulmin toulmin)
    {
        getOLMDistributionPane().setToulmin(toulmin);
    }
    
    public void setPartitionData(Toulmin toulmin)
    {
    	getOLMPartitionPane().setToulmin(toulmin);
    }
    
    public void setSummaryData(Toulmin toulmin)
    {
        getOLMSummaryPane().setToulmin(toulmin);
    }
    
    public void setChallengeData(String viewName,BeliefDesc bdesc,int lvl)
    {
        getOLMChallengePane().setClaim(viewName, bdesc, lvl);
    }
    
    public void switchToView(String view)
    {
        CardLayout cl2 = (CardLayout)(getJViewPanel().getLayout());
        cl2.show(getJViewPanel(),view);
    }
    
    public ToulminNode getToulminSelectedNode()
    {
        ToulminNode node = null;
        if (getOLMToulminGraph()!=null)
            node = getOLMToulminGraph().getSelectedNode();
        return node;
        
    }
    
    public void setSelectedToulminNode(ToulminNode node)
    {
        if (getOLMToulminGraph()!=null)
            getOLMToulminGraph().getTGPanel().setSelect(node);
        
        //setEvidence(null,null);
        if (node.getNodeConfig()==NodeConfig.BACKING)
        {
            ToulminBacking backing = (ToulminBacking)node.getData();
            ToulminWarrant warrant = backing.getWarrant();
            getOLMEvidencePane().setSelectedNode(node);
            //setEvidence(Toulmin.BACKING,warrant);
            switchToView(OLMMainGUI.VIEW_EVIDENCE);
        }
        else if (node.getNodeConfig()==NodeConfig.WARRANT)
        {
            //setEvidence(Toulmin.WARRANT,(ToulminWarrant)node.getData());
            switchToView(OLMMainGUI.VIEW_EVIDENCE);
        }
        else if (node.getNodeConfig()==NodeConfig.CLAIM)
        {
            if (node.getToulminType().equals(Toulmin.SUBCLAIM))
            {
                switchToView(OLMMainGUI.VIEW_PARTITION);
                if (node.getData() instanceof ToulminSubClaim) 
                {
                    ToulminSubClaim claim = (ToulminSubClaim) node.getData();
                    //String val = claim.getValue();
                    setPartition(node);
                    //getJPartitionPane().setSelectedNode(node);
                    
                }
            }
            else
                switchToView(OLMMainGUI.VIEW_BELIEF);
        }
        else if (node.getNodeConfig()==NodeConfig.DATA)
        {
            switchToView(OLMMainGUI.VIEW_DISTRIB);
        }        
    }
    
    public ToulminNode expandToulminNode(ToulminNode node)
    {
        if (getOLMToulminGraph()==null) return null;
        ToulminNode ret = getOLMToulminGraph().expandToulminNode(node.getID());
        if (ret!=null)
        {
            getOLMToulminGraph().centerOnNode(ret);
        }
        return ret;
    }

    public void setToulmin(Toulmin toulmin)
    {
        if (getOLMToulminGraph()==null) return;
        getOLMToulminGraph().setToulmin(toulmin);
        Double score = new Double(toulmin.getClaim().getClaimSummary());
        if (!score.equals(new Double(-1)))
        {
            //setBeliefData(toulmin.getBeliefDesc(),score,null);
        	setSummaryData(toulmin);
            
            //ToulminData data = toulmin.getData();
//            setDistributionData(data.getDistribution(),
//                data.getCertainty(),data.getPignistic(),null);
//           
            getOLMEvidencePane().setToulmin(toulmin); 
            setDistributionData(toulmin);
            setEvidence(null,null);
            setPartitionData(toulmin);
        }
    }
    
    public void setToulminUser(String user)
    {
        if (getOLMToulminGraph()!=null)
            getOLMToulminGraph().setTopNode(user);
        
    }

    public void enableToulmin(boolean show)
    {
        getOLMToulminGraph().setEnabled(show);
    }
    
    public Hashtable getChallengeOutcome()
    {
        Hashtable hash = getOLMChallengePane().getOutcome();
        return hash;
    }
     
    

}  
