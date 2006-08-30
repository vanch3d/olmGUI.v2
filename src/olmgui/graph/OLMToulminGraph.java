/**
 * @file OLMToulminGraph.java
 */
package olmgui.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;

import olmgui.i18n.Messages;
import toulmin.Toulmin;
import toulmin.ToulminBacking;
import toulmin.ToulminClaim;
import toulmin.ToulminData;
import toulmin.ToulminSubClaim;
import toulmin.ToulminWarrant;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGLensSet;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.graphelements.GraphEltSet;
import com.touchgraph.graphlayout.interaction.HVScroll;
import com.touchgraph.graphlayout.interaction.HyperScroll;
import com.touchgraph.graphlayout.interaction.TGUIManager;
import com.touchgraph.graphlayout.interaction.ZoomScroll;

import config.OLMPrefs;
import config.OLMQueryResult;
import dialogue.DialoguePlanner;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.34 $
 */
public class OLMToulminGraph extends OLMGraphBrowser
{
    
    protected String STR_ABOUT = "OLMGraphBrowser.Toulmin.About"; //$NON-NLS-1$
    protected String STR_GIVEN = "OLMGraphBrowser.Toulmin.Given"; //$NON-NLS-1$
    protected String STR_SINCE = "OLMGraphBrowser.Toulmin.Since"; //$NON-NLS-1$
    protected String STR_BECAUSE = "OLMGraphBrowser.Toulmin.Because"; //$NON-NLS-1$

    protected String STR_DESCRIPTOR = "OLMGraphBrowser.Toulmin.Descriptor"; //$NON-NLS-1$

    protected String STR_CLAIM = "OLMGraphBrowser.Toulmin.Claim"; //$NON-NLS-1$
    protected String STR_SUBCLAIM = "OLMGraphBrowser.Toulmin.SubClaim"; //$NON-NLS-1$
    protected String STR_SUBCLAIM_OTHER = "OLMGraphBrowser.Toulmin.SubClaim.Other"; //$NON-NLS-1$

    protected String STR_DATA = "OLMGraphBrowser.Toulmin.Data"; //$NON-NLS-1$
    protected String STR_BACKING = "OLMGraphBrowser.Toulmin.Backing"; //$NON-NLS-1$
    protected String STR_WARRANT = Messages.getString("OLMGraphBrowser.Toulmin.Warrant"); //$NON-NLS-1$

    //protected String STR_EVIDENCE = Messages.getString("OLMGraphBrowser.Menu.Evidence"); // label for locality menu item //$NON-NLS-1$

    protected String STR_LEVEL = Messages.getString("OLMGraphBrowser.Toulmin.Level"); //$NON-NLS-1$
    
    private int index =0;
    
//    /**
//      * @deprecated NOT IN USE ANYMORE
//     */
//    protected EvidenceScroll evidenceScroll;
    
    /** 
     * Default constructor.
     * Make sure to explicitely call {@link #initialize()} after calling the constructor
     */
     public OLMToulminGraph() 
     {
         topPanel = new JPanel();
         scrollBarHash = new Hashtable();
         tgLensSet = new TGLensSet();
         tgPanel = new TGPanel();
         hvScroll = new HVScroll(tgPanel, tgLensSet);
         zoomScroll = new ZoomScroll(tgPanel);
         hyperScroll = new HyperScroll(tgPanel);
         //evidenceScroll = new EvidenceScroll(tgPanel);

         completeEltSet = new GraphEltSet();
         tgPanel.setGraphEltSet(completeEltSet);

         setTopNode(STR_USER);
         enableHints();
         //tgPanel.setBackColor(java.awt.SystemColor.control);
         //super.defaultColor = java.awt.SystemColor.control;
     }
     
     public void setListeners(DialoguePlanner planner)
     {
         tgPanel.addMouseListener(planner.setToulminMouseListener());
     }     
     /** 
      * Initialize panel, lens, and establish the initial graph.
      */
     public void initialize() 
     {
         setImageList();
         buildPanel();
         buildLens();
         tgPanel.setLensSet(tgLensSet);
         addUIs();
         //tgPanel.addMouseListener(new GLToulminNavigateMouseListener());
         setVisible(true);
         Node node = new Node();
         node.setNodeBorderInactiveColor(java.awt.SystemColor.control);
     }
     
     public void addUIs() 
     {
         tgUIManager = new TGUIManager();
         ToulminNavigateUI navigateUI = new ToulminNavigateUI(this);
         tgUIManager.addUI(navigateUI,STR_NAVIGATE);
         tgUIManager.activate(STR_NAVIGATE);
     }
     
     public void buildLens() {
         tgLensSet.addLens(hvScroll.getLens());
         tgLensSet.addLens(zoomScroll.getLens());
         tgLensSet.addLens(hyperScroll.getLens());
         tgLensSet.addLens(tgPanel.getAdjustOriginLens());
     }

     
     public void buildPanel() {
         final JScrollBar horizontalSB = hvScroll.getHorizontalSB();
         final JScrollBar verticalSB = hvScroll.getVerticalSB();
         final JSlider zoomSB = zoomScroll.getZoomSB();
         final JSlider hyperSB = hyperScroll.getHyperSB();
         //final JScrollBar evidSB = evidenceScroll.getEvidenceSB();
         
         setLayout(new BorderLayout());

         JPanel scrollPanel = new JPanel();
         scrollPanel.setBackground(defaultColor);
         scrollPanel.setLayout(new GridBagLayout());
         GridBagConstraints c = new GridBagConstraints();

//         final JPanel topPanel = new JPanel();
         topPanel.setBackground(defaultColor);
         topPanel.setLayout(new GridBagLayout());
         c.gridy=0; c.fill=GridBagConstraints.HORIZONTAL;

         c.insets=new Insets(0,0,0,0);
         c.gridx=1;c.weightx=1;

         scrollBarHash.put(STR_ZOOM, zoomSB);
         scrollBarHash.put(STR_HYPER, hyperSB);
         //scrollBarHash.put(STR_EVIDENCE, evidSB);

         //JPanel scrollselect = scrollSelectPanel(new String[] {STR_ZOOM, STR_HYPER,STR_EVIDENCE});
         JPanel scrollselect = scrollSelectPanel(new String[] {STR_ZOOM, STR_HYPER});
         scrollselect.setBackground(defaultColor);
         topPanel.add(scrollselect,c);

         add(topPanel, BorderLayout.NORTH);

         c.fill = GridBagConstraints.BOTH;
         c.gridwidth = 1;
         c.gridx = 0; c.gridy = 1; c.weightx = 1; c.weighty = 1;
         scrollPanel.add(tgPanel,c);

         c.gridx = 1; c.gridy = 1; c.weightx = 0; c.weighty = 0;
         scrollPanel.add(verticalSB,c);

         c.gridx = 0; c.gridy = 2;
         scrollPanel.add(horizontalSB,c);

         add(scrollPanel,BorderLayout.CENTER);

//         glPopup = new JPopupMenu();
//         glPopup.setBackground(defaultColor);
//
//         JMenuItem menuItem = new JMenuItem(STR_TOGGLE);
//         ActionListener toggleControlsAction = new ActionListener() {
//                 boolean controlsVisible = true;
//
//                 public void actionPerformed(ActionEvent e) {
//                     controlsVisible = !controlsVisible;
//                     horizontalSB.setVisible(controlsVisible);
//                     verticalSB.setVisible(controlsVisible);
//                     topPanel.setVisible(controlsVisible);
//                 }
//             };
//         menuItem.addActionListener(toggleControlsAction);
//         glPopup.add(menuItem);
     }
     
     public ToulminNode getSelectedNode()
     {
         Node node = tgPanel.getSelect();
         if (node instanceof ToulminNode)
         {
            ToulminNode tnode = (ToulminNode) node;
            return tnode;
         }
         else return null;
     }
     
     
     
     public void setToulmin(Toulmin toulmin)
     {
         index =0;
         tgPanel.clearSelect();
         tgPanel.clearAll();
         if (toulmin == null) return;

         try 
         {
             OLMNode topnode = new OLMNode(STR_USER,topNodeUser);
             NodeConfig.TOPNODE.setConfig(topnode);
             topnode.setImage(getUserImage());
             topnode.setHint("That's you, Geezer!");
             topnode.setVisible(true);
             completeEltSet.addNode(topnode);

             OLMNode are = new OLMNode(null);
             setNodeAttribute(are,STR_ABOUT);
             NodeConfig.BELIEFASSOC.setConfig(are);
             OLMEdge edge = new OLMEdge(topnode,are,Edge.DEFAULT_LENGTH);
             are.setVisible(true);
             edge.setVisible(true);
             completeEltSet.addNode(are);
             completeEltSet.addEdge(edge);

             OLMNode belief = getDescriptorNode(toulmin.getBeliefDesc(),STR_DESCRIPTOR);
             NodeConfig.BELIEF.setConfig(belief);
             belief.setVisible(true);
             completeEltSet.addNode(belief);
             completeEltSet.addEdge(edge = new OLMEdge(are,belief,Edge.DEFAULT_LENGTH));
             edge.setVisible(true);
             
             OLMNode is = new OLMNode(null,"is");
             NodeConfig.BELIEFASSOC.setConfig(is);
             is.setVisible(true);
             completeEltSet.addNode(is);
             completeEltSet.addEdge(edge = new OLMEdge(belief,is,Edge.DEFAULT_LENGTH));
             edge.setVisible(true);
             
             Node claim = addClaim(is,toulmin,true);

             tgPanel.updateLocalityFromVisibility();
             tgPanel.resetDamper();
             tgPanel.setSelect(claim);
             
         } 
         catch (TGException e1) 
         {
        	 e1.printStackTrace();
         }
     }
     
     private ToulminNode addNewClaim(int index,Toulmin toulmin,boolean isTop) throws TGException
     {
         String template = (isTop)? STR_CLAIM : STR_SUBCLAIM;

         ToulminClaim data = toulmin.getClaim();

         String label = null;
         if (data instanceof ToulminSubClaim) 
         {
            ToulminSubClaim sub = (ToulminSubClaim) data;
            List warrants  = toulmin.getList();
            //System.out.println("warrants: " + warrants.size());
            Object[] arg = {
                    Messages.getString(sub.getDimension()),
                    Messages.getString(sub.getValue()),
                    new Integer(warrants.size())
            };
            if (sub.getValue().equals("ATTRIBUTE.OTHERS"))
            {
                template = STR_SUBCLAIM_OTHER;
                arg[0] = new Integer(warrants.size());
            }
            
            label = MessageFormat.format(Messages.getSafeString(template),arg);
          
         }
         else
         {
             int lvl = toulmin.getClaim().getClaimLevel();
             label = Messages.getJudgementOn(toulmin.getBeliefDesc(),lvl);
             Object[] arg = {label};
             label = MessageFormat.format(Messages.getSafeString(template),arg);
         }
         
         ToulminNode node = null;
         if (isTop)
         {
             node = new ToulminNode(Toulmin.CLAIM,Toulmin.CLAIM + index,label);
             setNodeAttribute(node,label,template);
             node.setToulmin(toulmin);
         }
         else
         {
             node = new ToulminNode(Toulmin.SUBCLAIM,Toulmin.SUBCLAIM + index,label);
             setNodeAttribute(node,label,template);
             node.setToulmin(toulmin);
             //node.setToulmin(toulmin.getTopToulmin());
         }
         node.setData(data);
         
         List arr = new ArrayList();
         if (isTop)
             arr.add(Toulmin.DATA);
         else
             arr.add(Toulmin.WARRANT);
         
         node.setNextNode(arr);
         node.setNodeConfig(NodeConfig.CLAIM,null);
         node.setVisible(true);
         completeEltSet.addNode(node);
         return node;
     }
     
     public ToulminNode expandToulminNode(String id)
     {
    	 ToulminNode returnNode = null;
         Node node= tgPanel.findNode(id);
         if (node==null) return null;
         if (node instanceof ToulminNode)
         {
             ToulminNode toulmin = (ToulminNode) node;
             try 
             {
                 List ls = toulmin.getNextNode();
                 if (ls.size()>0)
                 {
                     returnNode = expandToulminNode(toulmin);
                     tgPanel.updateLocalityFromVisibility();
                     tgPanel.resetDamper();
                 }
                 
             } catch (TGException e) 
             {
                e.printStackTrace();
             }
         }
         return returnNode;
     }
     
     public void centerOnNode(ToulminNode node)
     {
         if (node!=null)
             getHVScroll().slowScrollToCenter(node);
     }
     
     private ToulminNode expandToulminNode(ToulminNode top) throws TGException
     {
    	 ToulminNode returnNode = null;
         Node XXnode= tgPanel.findNode(top.getToulmin().getBeliefDesc().toString());

         if (top.getNextNode()==null || top.getNextNode().isEmpty()) return null;
         String node = (String)top.getNextNode().remove(0);
         if (node==null || node.equals("")) return null;
         
         OLMEdge edge = null;
         if (node.equals("DATA"))
         {
             OLMNode command = new OLMNode("GIVEN_" + index,STR_GIVEN);
             setNodeAttribute(command,STR_GIVEN);
             command.setVisible(true);
             NodeConfig.BELIEFASSOC.setConfig(command);
             completeEltSet.addNode(command);
             completeEltSet.addEdge(edge = new OLMEdge(XXnode,command,Edge.DEFAULT_LENGTH));
             edge.setVisible(true);
            
             ToulminNode nodedata = new ToulminNode(Toulmin.DATA,Toulmin.DATA + (index),STR_DATA);
             setNodeAttribute(nodedata,STR_DATA);
             ToulminData data = top.getToulmin().getData();
             List arr = new ArrayList();
             arr.add("WARRANT");
            
             nodedata.setNextNode(arr);
             nodedata.setData(data);
             nodedata.setToulmin(top.getToulmin());
             nodedata.setNodeConfig(NodeConfig.DATA,null);
             nodedata.setVisible(true);
             completeEltSet.addNode(nodedata);
             completeEltSet.addEdge(edge = new OLMEdge(command,nodedata,Edge.DEFAULT_LENGTH));
             edge.setVisible(true);
            
             returnNode = nodedata;
         }
         else if (node.equals("WARRANT"))
         {
             //OLMEdge lEdge = null;
             //Node since = tgPanel.addNode("SINCE_" + index,STR_SINCE);
             
             OLMNode since = null;
             if (top.getData() instanceof ToulminSubClaim)
             {
                 since = top;
                 
             }
             else
             {
                 String topId = top.getID();
                 since = new OLMNode("SINCE_" + topId,STR_SINCE);
                 setNodeAttribute(since,STR_SINCE);
                 NodeConfig.BELIEFASSOC.setConfig(since);
                 since.setVisible(true);
                 completeEltSet.addNode(since);
                 completeEltSet.addEdge(edge = new OLMEdge(top,since,Edge.DEFAULT_LENGTH));
                 edge.setVisible(true);
             }

             //Node claim = tgPanel.findNode(Toulmin.CLAIM + index);
             //if (claim !=null) tgPanel.addEdge(new OLMEdge(claim,since,Edge.DEFAULT_LENGTH*5));
             
             //List warrant  = (List)toulmin.getWarrant();
             List warrants  = top.getToulmin().getList();
             //ToulminNode prevnode = null;
             for (int i=0;i<warrants.size();i++)
             {
                 Object sub = warrants.get(i);
                 if (sub instanceof ToulminWarrant) 
                 {
                	 ToulminWarrant warrant = (ToulminWarrant) sub;
                     ToulminBacking backing = warrant.getBacking();
                     String type = (String)backing.getValue(OLMQueryResult.EVIDENCE_DIRECT);

                     int index = warrant.getIndex();
                     
                     ToulminNode node22 = null;
                     if (OLMPrefs.BACKING_ONLY)
                     {
                         node22 = new ToulminNode(Toulmin.BACKING,Toulmin.BACKING + index,STR_WARRANT +" " + index);
                         List arr = new ArrayList();
                         node22.setNextNode(arr);
                         node22.setToulmin(top.getToulmin());
                         node22.setData(backing);
                         if (type.equals("2"))
                             node22.setNodeConfig(NodeConfig.BACKING,new Color(255,128,128));
                         else
                             node22.setNodeConfig(NodeConfig.BACKING,null);
                       
                     }
                     else
                     {
                         node22 = new ToulminNode(Toulmin.WARRANT,Toulmin.WARRANT + index,STR_WARRANT +" " + index);
                         List arr = new ArrayList();
                         arr.add("BACKING");
                         node22.setNextNode(arr);
                         node22.setToulmin(top.getToulmin());
                         node22.setData(warrant);
                         if (type.equals("2"))
                             node22.setNodeConfig(NodeConfig.WARRANT,new Color(255,128,128));
                         else
                             node22.setNodeConfig(NodeConfig.WARRANT,null);
                     }
                     node22.setVisible(warrant.isExpanded());
                     completeEltSet.addNode(node22);
                     edge = new OLMEdge(since,node22,10*Edge.DEFAULT_LENGTH);
                     edge.setVisible(warrant.isExpanded());
                     completeEltSet.addEdge(edge);
                 }
                 else if (sub instanceof Toulmin) 
                 {
                	 Toulmin subtoul = (Toulmin) sub;

                     index++;
                     addClaim(since,subtoul,false);
                 }

             } 
             //lEdge.setLength(since.edgeCount()*Edge.DEFAULT_LENGTH);
         }
         else if (node.equals("BACKING"))
         {
        	 ToulminWarrant warrant = (ToulminWarrant)top.getData();
        	 ToulminBacking backing = warrant.getBacking();
        	 
             Integer dd = (Integer)backing.getValue(OLMQueryResult.EVIDENCE_INDEX);

             OLMNode command = new OLMNode("BECAUSE_" + dd,STR_BECAUSE);
             setNodeAttribute(command,STR_BECAUSE);
             NodeConfig.BELIEFASSOC.setConfig(command);
             command.setVisible(true);
             completeEltSet.addNode(command);
             NodeConfig.BELIEFASSOC.setConfig(command);
             completeEltSet.addEdge(edge = new OLMEdge(top,command,Edge.DEFAULT_LENGTH));
             edge.setVisible(true);
             
             ToulminNode nbacking = new ToulminNode(Toulmin.BACKING,Toulmin.BACKING + (dd),STR_BACKING +" " + dd);
             setNodeAttribute(nbacking,Messages.getString(STR_BACKING) +" " + dd,STR_BACKING);
             nbacking.setToulmin(top.getToulmin());
             nbacking.setData(backing);
             String type = (String)backing.getValue(OLMQueryResult.EVIDENCE_DIRECT);
             if (type.equals("2"))
                 nbacking.setNodeConfig(NodeConfig.BACKING,new Color(255,128,128));
             else
                 nbacking.setNodeConfig(NodeConfig.BACKING,null);
             
             edge.setVisible(true);
             completeEltSet.addNode(nbacking);
             completeEltSet.addEdge(edge = new OLMEdge(command,nbacking,Edge.DEFAULT_LENGTH));
             edge.setVisible(true);
             
             returnNode = nbacking;
             
         }   
         
         return returnNode;
     }
     
     private Node addClaim(Node top,Toulmin toulmin,boolean isTop) throws TGException
     {
         ToulminNode claim = addNewClaim(index,toulmin,isTop);
         OLMEdge edge = new OLMEdge(top,claim,Edge.DEFAULT_LENGTH);
         edge.setVisible(true);
         completeEltSet.addEdge(edge);

         return claim;
     }
     
     // Evidence Scroll ...................

//     /**
//     * @deprecated NOT IN USE ANYMORE
//     * @return
//     */
//    public EvidenceScroll getEvidenceScroll()
//     {
//         return evidenceScroll;
//     }
//
//     /**
//     * @deprecated NOT IN USE ANYMORE
//     * @param radius
//     */
//    public void setEvidenceRadius( int radius ) 
//     {
//         evidenceScroll.setEvidenceRadius(radius);
//     }
//
//     /**
//     * @deprecated NOT IN USE ANYMORE
//     * @return
//     */
//     public int getEvidenceRadius() {
//         return evidenceScroll.getEvidenceRadius();
//     }
}
