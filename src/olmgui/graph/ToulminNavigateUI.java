/**
 * @file ToulminNavigateUI.java
 */
package olmgui.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.touchgraph.graphlayout.Node;


/**
 * User interface for navigating around the Toulmin graph.
 *
 * @author Nicolas Van Labeke
 * @version $Revision: 1.11 $
 */
public class ToulminNavigateUI extends OLMNavigateUI 
{
    /**
     * A listener tailored for the particulr interactions in the Toulmin graph.
     */
    class GLToulminNavigateMouseListener extends GLNavigateMouseListener 
    {
        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent e)
        {
            Node mouseOverN = getTGPanel().getMouseOverN();
            //if (mouseOverN!=null)
            //    System.out.println("id: " + mouseOverN.getID());
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK && e.getClickCount()==1)
            {
                if (!getTGPanel().isEnabled()) return;
                if ( mouseOverN == null) return;
                if (mouseOverN instanceof ToulminNode)
                {
                    getTGPanel().setSelect(mouseOverN);
                }
            }
         }   
    }    
    
    /**
     * Default constructor.
     * @param glp A reference to the Toulmin graph panel.
     */
    public ToulminNavigateUI(OLMGraphBrowser glp) 
    {
        super(glp);
        setNavigateMouseListener(new GLToulminNavigateMouseListener());
    }

	protected void setUpNodePopup() 
	{
        nodePopup = new JPopupMenu();
        JMenuItem menuItem;
        
//        menuItem = new JMenuItem(STR_RECENTRE);
//        popupItems.put(STR_RECENTRE,menuItem);
//        ActionListener recentreAction = new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    if(popupNode!=null) {
//                    	try {
//							getTGPanel().setLocale(popupNode, getLocalityScroll().getLocalityRadius());
//						} catch (TGException e1) {
//							e1.printStackTrace();
//						}
//                    }
//                }
//            };
//            
//        menuItem.addActionListener(recentreAction);
//        nodePopup.add(menuItem);

        menuItem = new JMenuItem(STR_EXPAND);
        popupItems.put(STR_EXPAND,menuItem);
        ActionListener expandAction = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(popupNode!=null) {
                        getTGPanel().expandNode(popupNode);
                    }
                }
            };
            
        menuItem.addActionListener(expandAction);
        nodePopup.add(menuItem);
         
        menuItem = new JMenuItem(STR_COLLAPSE);
        popupItems.put(STR_COLLAPSE,menuItem);
        ActionListener collapseAction = new ActionListener() {
                public void actionPerformed(ActionEvent e) {                    
                    if(popupNode!=null) {
                        getTGPanel().collapseNode(popupNode );
                    }
                }
            };
            
        menuItem.addActionListener(collapseAction);
        nodePopup.add(menuItem);

        menuItem = new JMenuItem(STR_HIDENODE);
        popupItems.put(STR_HIDENODE,menuItem);
        ActionListener hideAction = new ActionListener() {
                public void actionPerformed(ActionEvent e) {                    
                    if(popupNode!=null) {
                        getTGPanel().hideNode(popupNode );
                    }
                }
            };
            
        menuItem.addActionListener(hideAction);
        nodePopup.add(menuItem);

        nodePopup.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(PopupMenuEvent e) {}
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                getTGPanel().setMaintainMouseOver(false);
                getTGPanel().setMouseOverN(null);
                getTGPanel().repaint();        
            }
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
        });
        }
    
    

}
