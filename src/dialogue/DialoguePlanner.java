/**
 * @file DialoguePlanner.java
 */
package dialogue;

import java.applet.AppletContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import olmgui.OLMMainGUI;
import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import olmgui.output.OLMDistributionPane;
import olmgui.utils.BeliefConstructorButton;
import olmgui.utils.BrowserLauncher;
import olmgui.utils.DescriptorListWrapper;
import olmgui.utils.OutputViewPanel;
import olmgui.utils.TopicListModel;
import olmgui.utils.TopicWrapper;
import olmgui.utils.FilteringJList.FilteringModel;
import toulmin.BeliefDesc;
import toulmin.Toulmin;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;

import config.OLMTopicConfig;


/**
 * Implements the main controller for the dialogue moves in the OLM.
 * 
 * This controller has two purposes:
 * <ul>
 * <li>keep a reference on the current dialogue move and all the ones played by 
 *     both learners and OLM;
 * <li>centralise the listeners for all widgets in the OLM GUI in order to coordinate
 *     enable/diasble, showing/hiding, etc. depending on the context of the 
 *     interaction.
 * </ul>
 * @author Nicolas Van Labeke
 * @version $Revision: 1.48 $
 *
 */
public class DialoguePlanner
{
    /**
     * Shortcuts for the verbalisation of the OLM name.
     */
    private String OLMUSER = Messages.getString("OLMConfig.OLMUSER"); //$NON-NLS-1$

    /**
     * A reference to the current dialogue move.
     */
    private DialogueMove currentState = null;
        
    /**
     * A reference to the main GUI of the OLM.
     */
    private OLMMainGUI sParent = null;

    /**
     * List containing all the dialogue moves played so far.
     */
    private List moves = null;
    
    /**
     * A map of all the Actions defined in the GUI, indexed by their {@link DlgMoveID}}.
     */
    private HashMap actions = new HashMap();

    /**
     * This is the listener for the SWAP dialogue move.
     * @see OLMDistributionPane
     * @see OLMWarrantPane
     * @see DlgMoveID#SWAP
     */
    public class DlgSwapView implements ActionListener
    {
        /**
         * Shortcuts for the targets of the SWAP move.
         */
        public final static String BELIEF = "DlgMove.Swap.BELIEF", //$NON-NLS-1$
        						   EVIDENCE = "DlgMove.Swap.EVIDENCE", //$NON-NLS-1$
        						   PARTITION = "DlgMove.Swap.EVIDENCE"; //$NON-NLS-1$
        
        /**
         * Shortcuts for the different verbalisations of the SWAP move.
         */
        private final static String PROMPT = "DlgMove.Swap.Prompt"; //$NON-NLS-1$

        /**
         * Identifier of the view associated with the current SWAP move.
         */
        private OutputViewPanel context = null;
        private String 			attribute = null;
        
        /**
         * Default constructor for the listener
         * @param ctx   The identifier of the view associated with this move
         * @see DlgSwapView#BELIEF
         * @see DlgSwapView#EVIDENCE
         */
        public DlgSwapView(String att,OutputViewPanel ctx) 
        {
        	this.attribute = att;
            this.context = ctx;
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            if (sParent==null) return;
            sParent.updateDialogueMove(sParent.getUserName(),Messages.getRandomString(PROMPT),null);
            if (context!=null)
            {
            	context.swapView();
            	context.swapToNextView();
                String typeER = context.getCurrentView();
                String reply = Messages.getRandomString(this.attribute + "." + typeER);
                sParent.updateDialogueMove(OLMUSER,reply,null);
            }
            
        }
    }
    
    /**
     * This is the listener for the TELLMEMORE dialogue move.
     * @see OutputViewPanel
     * @see DlgMoveID#TELLMORE
     */
    public class DlgTellMeMore implements ActionListener
    {
        /**
         * Shortcuts for the targets of the TELLMEMORE move.
         */
        public final static String DESCRIPTOR = "DlgMove.TellMeMore.DESCRIPTOR", //$NON-NLS-1$
                                   SUMMARY = "DlgMove.TellMeMore.SUMMARY", //$NON-NLS-1$
                                   BELIEF = "DlgMove.TellMeMore.BELIEF", //$NON-NLS-1$
                                   PARTITION = "DlgMove.TellMeMore.PARTITION", //$NON-NLS-1$
                                   EVIDENCE = "DlgMove.TellMeMore.EVIDENCE"; //$NON-NLS-1$

        /**
         * Shortcuts for the different verbalisations of the TELLMEMORE move.
         */
        private final static String PROMPT = "DlgMove.TellMeMore.Prompt", //$NON-NLS-1$
                                    DESCRIPTOR_NULL = "DlgMove.TellMeMore.DESCRIPTOR.Empty"; //$NON-NLS-1$

        /**
         * Identifier of the the view associated with the current TELLMEMORE move.
         */
        private String context = null;

        /**
         * Default constructor for the listener.
         * @param ctx   The identifier of the view associated with this move.
         * @see DlgTellMeMore#DESCRIPTOR
         * @see DlgTellMeMore#SUMMARY
         * @see DlgTellMeMore#BELIEF
         * @see DlgTellMeMore#EVIDENCE
         */
        public DlgTellMeMore(String ctx) 
        {
            this.context = ctx;
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) 
        {
            if (sParent==null) return;
            sParent.updateDialogueMove(sParent.getUserName(),Messages.getRandomString(PROMPT),null);
            if (DESCRIPTOR.equals(this.context))
            {
                boolean valid = sParent.getJBeliefConstructor().isDescriptorValid();
                BeliefDesc desc = sParent.getBeliefDescriptor();
                Object[] arg = 
                {
                        desc
                };

                if (valid)
                    sParent.updateDialogueMove(OLMUSER,Messages.getRandomString(DESCRIPTOR),arg);
                else
                    sParent.updateDialogueMove(OLMUSER,Messages.getRandomString(DESCRIPTOR_NULL),null);
                
            }
            else if (SUMMARY.equals(this.context))
            {
                //String attr = sParent.getJDistributionPane().getAttribute();
                Toulmin toulmin = getCurrentState().getToulmin();
                if (toulmin!=null)
                {
                    ArrayList list = new ArrayList();
                    list.add(Messages.getLayerOn(toulmin.getBeliefDesc()));
                    list.add(Messages.getJudgementOn(toulmin.getBeliefDesc(),1));
                    list.add(Messages.getJudgementOn(toulmin.getBeliefDesc(),4));
                    sParent.updateDialogueMove(OLMUSER,Messages.getRandomString(SUMMARY),list.toArray());
                    
                    list = toulmin.getClaim().analyseClaim();
                    String template = SUMMARY + (String)list.remove(0);
                    sParent.updateDialogueMove(OLMUSER,Messages.getRandomString(template),list.toArray());
                }                
                //
            }
            else if (BELIEF.equals(this.context))
            {
                sParent.updateDialogueMove(OLMUSER,Messages.getRandomString(BELIEF),null);

            }
            else if (EVIDENCE.equals(this.context))
            {
                sParent.updateDialogueMove(OLMUSER,Messages.getRandomString(EVIDENCE),null);
            }
            
        }
        
    }

    /**
     * Implementation of the Action associated with (most of the) dialogue moves.
     */
    public class DlgMoveAction extends AbstractAction implements MouseListener
    {
        /**
         * Indicates whether the widgets associated with this action should be 
         * visible or not.
         * @deprecated DOES NOT WORK!
         */
        protected boolean visible = true;
        
        /**
         * Indicated whether the widgets associated with this action are permanently 
         * enabled or not.
         */
        protected boolean permanent = false;
        
        /**
         * Default constructor for a dialogue move
         * @param name  The identifier of the dialogue move to associate with the action.
         * @see DlgMoveID
         */
        public DlgMoveAction(String name) 
        {
            super(name);
        }
        
        /**
         * Returns true if the action is visible in the GUI.
         * @return true if the action is visible, false otherwise
         * @deprecated DOES NOT WORK!
         */
        public boolean isVisible() { return visible;}
        
        /**
         * Returns true if the action is permanently enabled in the GUI.
         * @return true if the action is permanently enabled, false otherwise
         */
        public boolean isPermanent() { return permanent;}
        
        /**
         * Set the action visible or not in the GUI.
         * 
         * @param newValue  true to make the action visible, 
         *                  false otherwise
         * @deprecated DOES NOT WORK! Needs a particular handler in every widget.
         */
        public void setVisible(boolean newValue)
        {
            boolean oldValue = this.visible;
            if (oldValue != newValue) 
            {
                firePropertyChange("visible", 
                       Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
            }
        }

        /**
         * @param newValue
         */
        public void setPermanent(boolean newValue)
        {
            permanent = newValue;
        }        

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent evt) 
        {
            String action = (String)this.getValue(ACTION_COMMAND_KEY);
            //String action = evt.getActionCommand();
            DlgMoveID move = DlgMoveID.getByName(action);
            if (DlgMoveID.ABOUT == move)
                showHelpOn(null);
            else
                getCurrentState().goNextMove(move);
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent evt) 
        {
            if (sParent==null) return;
            Object source = evt.getSource();
            if (source instanceof JButton) {
                JButton btn = (JButton) source;
                //String action = btn.getActionCommand();
                if (btn.isEnabled())
                {
                    Object obj = this.getValue(Action.LONG_DESCRIPTION);
                    //sParent.progressStop("OLMMoveSelector." + action + ".Description");
                    sParent.progressText(obj.toString());
                }
            }
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent evt)
        {
            if (sParent==null) return;
            Object source = evt.getSource();
            if (source instanceof JButton) {
                JButton btn = (JButton) source;
                if (btn.isEnabled())
                    sParent.progressStop(null);
            }
        }

        public void mouseClicked(MouseEvent arg0) {}
        public void mousePressed(MouseEvent arg0) {}
        public void mouseReleased(MouseEvent arg0) {}
    }

    /**
     * Listener for the "Show Item" associated with the BACKING of a claim 
     * (ie display LeAM learning object associated with the evidence).
     * 
     * The evidence will always contain the identifier of LEAM learning object from 
     * which the evidence has been generated; this ID is passed to the Planner by the
     * <code>ActionCommand</code> of the button.
     * 
     * This listener intercepts the ID with {@link #actionPerformed(ActionEvent)}
     * and opens the relevant learning object in an external browser window.
     * 
     * @todo In order to open the URL when not in an applet (debug mode mostly), 
     *       I'm using the {@link olmgui.utils.BrowserLauncher} class to do the trick; it's nasty
     *       but it works fine.
     *       
     * @see olmgui.output.OLMWarrantPane#jShowBtn
     * @see javax.swing.AbstractButton#setActionCommand(java.lang.String)
     */
    private class ShowItemListener implements ActionListener
    {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent evt)
        {
            String itemid = evt.getActionCommand();
            if (itemid==null) return;

            showItem(itemid);
        }
    }    
    
    /**
     * This is the listener for all actions taking place in the Toulmin graph
     * view (see {@link olmgui.graph.OLMToulminGraph}).
     */
    private class ToulminGraphMouseListener implements MouseListener
    {

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent evt)
        {
            Object source = evt.getSource();

            if (!(source instanceof TGPanel)) return;
            if (evt.getModifiers() != MouseEvent.BUTTON1_MASK) return;

            TGPanel tgPanel = (TGPanel) source;
            Node mouseOverN = tgPanel.getMouseOverN();
            if (!tgPanel.isEnabled()) return;
            if (mouseOverN == null) return;
            if (!(mouseOverN instanceof ToulminNode)) return; 

            ToulminNode tt = (ToulminNode)mouseOverN;

            // Deal with single click (node selection)
            if (evt.getClickCount()==1) 
            { 
            	activateToulminView(tt);
            	DlgMoveAction act = (DlgMoveAction) getAction(DlgMoveID.BAFFLED);
            	if (act!=null)
            	{
//            		String type = tt.getToulminType();
//            		if (Toulmin.BACKING.equals(type))
//            			act.setEnabled(false);
//            		else
//            			act.setEnabled(true);
            	}
            }
        }        

        public void mouseEntered(MouseEvent arg0) {}
        public void mouseExited(MouseEvent arg0) {}
        public void mousePressed(MouseEvent arg0) {}
        public void mouseReleased(MouseEvent arg0) {}
    }

    /**
     * This is the listener for all theactions taking place in the Challenge view
     * {see {@link olmgui.input.OLMChallengePane}).
     *   
     * @author Nicolas Van Labeke
     * @version $Revision: 1.48 $
     */
    private class ChallengeListener implements MouseListener,
                                               ChangeListener,
                                               ItemListener
   {

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent evt)
        {
            if (sParent==null) return;
            Object source = evt.getSource();
            if (source instanceof JSlider || source instanceof JComboBox)
            {
                JComponent btn = (JComponent) source;
                String action = btn.getName();
                if (action!=null)
                    action = action + ".description"; //$NON-NLS-1$
                if (btn.isEnabled()) sParent.progressStop(action);
            }
           
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent evt)
        {
            if (sParent==null) return;
            JComponent btn = (JComponent) evt.getSource();
            if (btn.isEnabled()) sParent.progressStop(null);
        }
        
        public void mouseClicked(MouseEvent arg0) {}
        public void mousePressed(MouseEvent arg0) {}
        public void mouseReleased(MouseEvent arg0) {}
        public void stateChanged(ChangeEvent arg0) {}
        public void itemStateChanged(ItemEvent arg0) {}

   }

    /**
     * This is the listener for all actions in the Topic Selectors
     * (see {@link olmgui.input.OLMTopicSelector}).
     */
    private class TopicSelectorListener implements ListSelectionListener, 
                                                   MouseListener,
                                                   ChangeListener
    {

        /* (non-Javadoc)
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent evt)
        {
            if (!evt.getValueIsAdjusting())
            {
                JList slist = (JList)evt.getSource();
                if (slist.getSelectedValue()!=null)
                {
                    //String str = ((JList)evt.getSource()).getSelectedValue().toString();
                }
            }
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent evt) 
        {
            if (sParent==null) return;
            Object source = evt.getSource();
            if (!(source instanceof JList)) return;
            if (evt.getClickCount() != 2) return;
            
            if (!((JList) evt.getSource()).isEnabled()) return;
                
            // if the source of double-click is a list, check the model
            ListModel model = ((JList) evt.getSource()).getModel();
            
            if (model instanceof TopicListModel)
            {
                // if it is a TopicListModel (containing TopicWrapper)
                TopicListModel topic = (TopicListModel) model;
                Object obj = model.getElementAt(((JList) evt.getSource()).getSelectedIndex());
                if (obj instanceof TopicWrapper)
                {
                    TopicWrapper wrapper = (TopicWrapper) obj;
    
                    //String str = ((JList)evt.getSource()).getSelectedValue().toString();
                    sParent.setLayerAttribute(topic.getTopic(), wrapper);
                    BeliefDesc bdesc = sParent.getJBeliefConstructor().getBeliefDescriptor();
                    sParent.getJTopicSelector().setFilter(bdesc);
                    

                }
            }
            else if (model instanceof FilteringModel)
            {
                // if it is a DescriptorListModel (containing Vector)
                //DescriptorListModel descs = (DescriptorListModel) model;
                Object obj = model.getElementAt(((JList) evt.getSource()).getSelectedIndex());
                if (obj instanceof DescriptorListWrapper)
                {
                    BeliefDesc bdesc = ((DescriptorListWrapper)obj).bdesc; 
                    OLMTopicConfig cfg[] = 
                    {
                            OLMTopicConfig.DOMAIN,
                            OLMTopicConfig.CAPES, 
                            OLMTopicConfig.COMPET, 
                            OLMTopicConfig.MOTIV, 
                            OLMTopicConfig.AFFECT, 
                            OLMTopicConfig.METACOG 
                    };

                    // put the empty descriptor in place
                    for (int i=0;i<cfg.length;i++)
                        sParent.setLayerAttribute(cfg[i], null);
                    
                    for (int i=0;i<bdesc.size();i++)
                    {
                        String id = (String)bdesc.get(i);
                        TopicListModel topicModel = cfg[i].getModel();
                        if (topicModel==null) continue;
                        Object obj2 = topicModel.findElement(id);
                        if (obj2 instanceof TopicWrapper) {
                            TopicWrapper wrap = (TopicWrapper) obj2;
                            sParent.setLayerAttribute(cfg[i], wrap);
                        }
                    }
                    //bdesc = sParent.getJBeliefConstructor().getBeliefDescriptor();
                    sParent.getJTopicSelector().setFilter(bdesc);

                }                
            }
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent evt) 
        {
            if (sParent==null) return;
            Object source = evt.getSource();
            if (!(source instanceof JList)) return;
                
            // if the source of double-click is a list, check the model
            ListModel model = ((JList) evt.getSource()).getModel();
            
            OLMTopicConfig cfg = null;
            if (model instanceof TopicListModel)
            {
            	cfg = ((TopicListModel)model).getTopic();
            }
            else if (model instanceof FilteringModel)
            {
            	cfg = OLMTopicConfig.BDESC;
            }
            
            JList btn = (JList) evt.getSource();
            if (cfg!=null && btn.isEnabled()) 
            	sParent.progressStop("OLMTopicConfig." + cfg.toString() + ".Description");
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent evt) 
        {
            if (sParent==null) return;
            JComponent btn = (JComponent) evt.getSource();
            if (btn.isEnabled()) sParent.progressStop(null);
        }
        
        public void mousePressed(MouseEvent evt) {}
        public void mouseReleased(MouseEvent evt) {}
        public void stateChanged(ChangeEvent evt) {}
    }
    
    /**
     * This is the listener for all actions in the Belief Constructor view
     * (see {@link olmgui.input.OLMBeliefConstructor}).
     */
    private class BeliefConstructorListener implements MouseListener
    {

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent evt) 
        {
            if (sParent==null) return;
            Object source = evt.getSource();
            if (!(source instanceof BeliefConstructorButton)) return;
            if (evt.getClickCount() != 2) return;
            
            BeliefDesc bdesc = sParent.getJBeliefConstructor().getBeliefDescriptor();
            sParent.getJTopicSelector().setFilter(bdesc);
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent evt) 
        {
            Object source = evt.getSource();
            if (source instanceof JButton) {
                JButton btn = (JButton) source;
                String action = btn.getActionCommand();
                if (btn.isEnabled()) sParent.progressStop("OLMTopicConfig." + action + ".Constructor");
            }
            
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent evt) 
        {
            if (sParent==null) return;
            JComponent btn = (JComponent) evt.getSource();
            if (btn.isEnabled()) sParent.progressStop(null);
        }

        public void mousePressed(MouseEvent evt) {}
        public void mouseReleased(MouseEvent evt) {}
        
    }

    /**
     * @param parent
     */
    public DialoguePlanner(OLMMainGUI parent)
    {
        super();
        this.sParent = parent;
        this.moves = new ArrayList();
    }

    /**
     * @param parent
     */
    public DialoguePlanner(JApplet parent)
    {
        super();
        this.sParent = null;
        this.moves = new ArrayList();
    }
    
    /**
     * @return  DialogueMove
     */
    public DialogueMove getCurrentState()
    {
        return this.currentState;
    }

    /**
     * Set the current move of the dialogue.
     * The previous move is stored in the {@link DialoguePlanner#moves} list.
     * @param move  A reference to the next move to go to (should not be null).
     */
    public void setCurrentState(DialogueMove move)
    {
        if (currentState!=null)
            moves.add(currentState);
        currentState = move;
    }

    /**
     * @return A string containing the name of the current user.
     */
    public String getUserName() 
    {
        if (sParent==null) return "USER";
        return this.sParent.getUserName();
    }

    /**
     * @return	A reference to the main GUI of the OLM.
     */
    public OLMMainGUI getMainGUI() 
    {
        return this.sParent;
    }

    /**
     * Set the listener 
     * @return  A new instance of the listener for the Show Item widget.
     */
    public ActionListener setShowItemListener()
    {
        return new ShowItemListener();
    }

    /**
     * @return A new instance of the listener for the Toulmin Graph.
     */
    public MouseListener setToulminMouseListener() 
    {
        return new ToulminGraphMouseListener();
    }

    /**
     * @return A new instance of the listener for the Topic Selector.
     */
    public MouseListener setTopicMouseListener() 
    {
        return new TopicSelectorListener();
    }
    
    /**
     * @return A new instance of the listener for the Topic Selector.
     */
    public EventListener setTopicSelectionListener() 
    {
        return new TopicSelectorListener();
    }

    /**
     * @return A new instance of the listener for the Belief Constructor.
     */
    public MouseListener setBeliefConstructorListener() 
    {
        return new BeliefConstructorListener();
    }
    
    /**
     * @param context
     * @return
     */
    public ActionListener setTellMeMoreListener(String context)
    {
        return new DlgTellMeMore(context);
    }
    
    /**
     * @param context
     * @return
     */
    public ActionListener setSwapViewListerner(String att,OutputViewPanel context)
    {
        return new DlgSwapView(att,context);
    }
    
    /**
     * @return A new instance of the listener for the Challenge View.
     */
    public EventListener setChallengeListener() 
    {
        return new ChallengeListener();
    }
    
    /**
     * @param activ
     */
    public void activateGUI(boolean activ)
    {
        Iterator iter = actions.values().iterator();
        while (iter.hasNext())
        {
            DlgMoveAction act = (DlgMoveAction)iter.next();
            if (act.isPermanent())
                act.setEnabled(true);
            else
                act.setEnabled(false);
        }
        
        if (currentState==null) return;
        Set moves = currentState.getPossibleMoves();
        if (moves==null) return;
        for (Iterator ite = moves.iterator();ite.hasNext();)
        {
            DlgMoveID mov = (DlgMoveID)ite.next();
            activateMove(mov,activ);
        }

    }
    
    /**
     * @param mvt
     * @param activ
     */
    public void activateMove(DlgMoveID mvt,boolean activ)
    {
        if (sParent==null) return;
        DlgMoveAction act = (DlgMoveAction)actions.get(mvt.toString());
        if (act==null) return;
        if (DlgMoveID.SHOWME == mvt)
        {
            // check if the belief descriptor is valid
            boolean val = sParent.getJBeliefConstructor().isDescriptorValid();
            act.setEnabled(activ & val);
        }
        else
            act.setEnabled(activ);
    }
    
    /**
     * @param node
     */
    public void activateToulminView(ToulminNode node)
    {
        if (sParent==null) return;
    	if (node==null) return; 
        currentState.activateGUI(true);
        sParent.setSelectedToulminNode(node);
//        if (sParent.getJToulminGraph()!=null)
//            sParent.getJToulminGraph().getTGPanel().setSelect(node);

    }

    
    /**
     * @param moveId
     * @return
     */
    public Action getAction(DlgMoveID moveId)
    {
        HashMap icons = new HashMap();
        icons.put(DlgMoveID.ABOUT,"/res/helpOLM.gif");
        icons.put(DlgMoveID.ACCEPT,"/res/agree.gif");
        icons.put(DlgMoveID.AGREE,"/res/agree.gif");
        icons.put(DlgMoveID.BAFFLED,"/res/baffled.gif");
        icons.put(DlgMoveID.DISAGREE,"/res/disagree.gif");
        icons.put(DlgMoveID.LOST,"/res/lost.gif");
        icons.put(DlgMoveID.MOVEON,"/res/moveon.gif");
        icons.put(DlgMoveID.QUIT,"/res/home.gif");
        icons.put(DlgMoveID.REJECT,"/res/disagree.gif");
        icons.put(DlgMoveID.SHOWME,"/res/showme.gif");
        icons.put(DlgMoveID.TELLMORE,"/res/tellme.gif");
        icons.put(DlgMoveID.SWAP,"/res/swap.gif");
        icons.put(DlgMoveID.CONTEXT,"/res/exercise_s.gif");
        
        DlgMoveAction act = (DlgMoveAction)actions.get(moveId.toString());
        if (act==null)
        {
            act = new DlgMoveAction(moveId.toString());
            act.putValue(Action.NAME,
                    Messages.getString("OLMMoveSelector." + moveId.toString() + ".name"));
            act.putValue(Action.SHORT_DESCRIPTION,
                        Messages.getString("OLMMoveSelector." + moveId.toString() + ".name"));
            act.putValue(Action.ACTION_COMMAND_KEY,moveId.toString());
            act.putValue(Action.LONG_DESCRIPTION,
                    Messages.getString("OLMMoveSelector." + moveId.toString() + ".Description"));
            
            String strIcon = (String)icons.get(moveId);
            if (strIcon!=null)
                act.putValue(Action.SMALL_ICON,new ImageIcon(getClass().getResource(strIcon)));
            actions.put(moveId.toString(),act);
        }
        return act;
    }

    /**
     * @param itemid
     */
    public void showItem(String itemid)
    {
        if (sParent==null) return;
        AppletContext ctx = sParent.getAppletContext();
        String clrun = ctx.getClass().getName();
        if (clrun.equals("sun.applet.AppletViewer"))
        {
            try 
            {
                URL urlCore = sParent.getOLMCore();
                URL url = new URL(urlCore.getProtocol(),urlCore.getHost(),urlCore.getPort(),itemid);
                BrowserLauncher.openURL(url.toString());
            }
            catch (MalformedURLException e) {e.printStackTrace();} 
            catch (IOException e) {e.printStackTrace();}
            catch (Exception e) {e.printStackTrace();}
        }
        else
        {
            try 
            {
                URL url = new URL("javascript:doAlert(\"" + itemid +"\")");
                ctx.showDocument(url);
            } 
            catch (MalformedURLException e) {e.printStackTrace();}
        }    
    }
    
    /**
     * @param index
     */
    public void showHelpOn(String index)
    {
        if (sParent==null) return;
        AppletContext ctx = sParent.getAppletContext();
        String clrun = ctx.getClass().getName();
        if (clrun.equals("sun.applet.AppletViewer"))
        {
            try 
            {
                URL urlCore = sParent.getOLMCore();
                if (urlCore==null) return;
                URL url = new URL(urlCore.getProtocol(),urlCore.getHost(),urlCore.getPort(),"/ActiveMath2/olm/help.cmd");
                BrowserLauncher.openURL(url.toString());
            } 
            catch (MalformedURLException e) {e.printStackTrace();} 
            catch (IOException e) {e.printStackTrace();}
        }
        else
        {
            try 
            {
                URL url = new URL("javascript:openOLMHelp(\"" + "index" +"\")");
                ctx.showDocument(url);
            } 
            catch (MalformedURLException e) {e.printStackTrace();}
        }

    }
}
