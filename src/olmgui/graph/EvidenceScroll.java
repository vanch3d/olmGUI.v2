package olmgui.graph;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

import toulmin.Toulmin;
import toulmin.ToulminList;
import toulmin.ToulminWarrant;

import com.touchgraph.graphlayout.GraphListener;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;

/**
 * Implement a lkistener used to modify the number of evidence displayed simultaneously
 * in the Toulmin Graph View.
 * 
 * This is based on the density of evidence.
 * 
 * @todo The algorith for changing the evidence size is too slow; need to find a better 
 *       way of doing it.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.8 $
 * @deprecated NOT IN USE ANYMORE
 */
public class EvidenceScroll implements GraphListener {

    /**
     * 
     */
    private final int DEFAULT_EVIDENCE = 50;
    
    /**
     * 
     */
    private final int MAX_EVIDENCE = 100;
    
    /**
     * A reference to the evidence scrollbar.
     */
    private JScrollBar evidenceSB;

    /**
     * A reference to the TouchGraph panel containing this evidence listener.
     */
    private TGPanel tgPanel;

    /**
     * Listener for detecting and reacting to changes in the evidence scrollbar.
     */
    private class EvidenceAdjustmentListener implements AdjustmentListener 
    {
        /**
         * Store the previous value to detect "real" changes in the scrollbar.
         */
        private int oldRadius = -1;
        
        
        /* (non-Javadoc)
         * @see java.awt.event.AdjustmentListener#adjustmentValueChanged(java.awt.event.AdjustmentEvent)
         */
        public void adjustmentValueChanged(AdjustmentEvent e) 
        {
            final int radius = getEvidenceRadius();
            if (oldRadius == radius) return;
            oldRadius = radius;
            new Thread() {
                public void run() 
                {
                    Node node = tgPanel.findNode(Toulmin.CLAIM + "0");
                    if (node instanceof ToulminNode) {
                        Node find = tgPanel.findNode("SINCE_0");
                        tgPanel.fastFinishAnimation();
                        tgPanel.expandNode(find);
                        tgPanel.stopDamper();
                        ToulminNode claim = (ToulminNode) node;
                        ToulminList list = claim.getToulmin().getList();
                        int nb = list.size();
                        for (int i=0;i<nb;i++)
                        {
                            Node war = tgPanel.findNode(Toulmin.WARRANT + i);
                            if (war instanceof ToulminNode) 
                            {
                                ToulminNode warrant = (ToulminNode) war;
                                Object obj = warrant.getData();
                                if (obj instanceof ToulminWarrant)
                                {
                                    ToulminWarrant w = (ToulminWarrant)obj;
                                    int rel = w.getRelevance();
                                    if (rel<radius)
                                    {
                                        tgPanel.stopDamper();
                                        tgPanel.hideNode(war);
                                    }
                                }
                            }
                        }
                        tgPanel.resetDamper();

                    }
               }
                
            }.start();
        }
    }
    
    
    /**
     * Default constructor for the Evidence Scroll.
     * @param tgPanel   A reference to the Touch Graph panel.
     */
    public EvidenceScroll(TGPanel tgPanel) {
        this.tgPanel=tgPanel;
        evidenceSB = new JScrollBar(JScrollBar.HORIZONTAL, 50, 10, 0, MAX_EVIDENCE);
        evidenceSB.setBlockIncrement(10);
        evidenceSB.setUnitIncrement(10);
        evidenceSB.addAdjustmentListener(new EvidenceAdjustmentListener());
        tgPanel.addGraphListener(this);
    }
    
    /**
     * Get the scroll bar associated with the evidence listener.
     * @return  A reference to the scrollbar in the GUI.
     */
    public JScrollBar getEvidenceSB() 
    {
        return evidenceSB;
    }

    /**
     * Get the current density for the evidence.
     * @return  An integer comprised between 0 and MAX_EVIDENCE.
     */
    public int getEvidenceRadius() {
        int locVal = evidenceSB.getValue();
        if(locVal>=MAX_EVIDENCE) return MAX_EVIDENCE;
        else if(locVal<=0) return 0;
        else return locVal;
    }

    /**
     * Set the density of evidence for the Toulmin Argumentation Pattern.
     * @param radius    An integer comprised between 0 and MAX_EVIDENCE.
     */
    public void setEvidenceRadius(int radius) {
        if (radius <= 0 ) 
            evidenceSB.setValue(0);
        else if (radius <= MAX_EVIDENCE) //and > 0
            evidenceSB.setValue(radius);
        else // radius > 5
            evidenceSB.setValue(MAX_EVIDENCE);        
    }
    
    /**
     * Update the evidence scrollbar when the graph is modified.
     * @see com.touchgraph.graphlayout.GraphListener#graphMoved()
     */
    public void graphMoved() {}

    /**
     * Update the evidence scrollbar when the graph is reset. 
     * @see com.touchgraph.graphlayout.GraphListener#graphReset()
     */
    public void graphReset()
    {
        evidenceSB.setValue(DEFAULT_EVIDENCE);
    }
}
