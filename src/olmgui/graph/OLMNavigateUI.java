/**
 * @file OLMNavigateUI.java
 */
package olmgui.graph;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import olmgui.i18n.Messages;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.DragNodeUI;
import com.touchgraph.graphlayout.interaction.LocalityScroll;
import com.touchgraph.graphlayout.interaction.TGAbstractDragUI;
import com.touchgraph.graphlayout.interaction.TGUserInterface;


/** 
 * User interface for navigating around the graph, ie dragging nodes, 
 * collapsing or expanding nodes, hide nodes or edges.
 *
 * @see com.touchgraph.graphlayout.interaction.GLNavigateUI
 * 
 * @author  Alexander Shapiro                                        
 * @author  Murray Altheim (abstracted GLPanel to TGScrollPane interface)
 * @author  Nicolas Van Labeke (adapted from GLNavigateUI for the OLM)                                      
 * @version $Revision: 1.13 $
*/
public class OLMNavigateUI extends TGUserInterface {
    
	String STR_EXPAND =Messages.getString("OLMGraphBrowser.Menu.Expand"); //$NON-NLS-1$
	String STR_RECENTRE =Messages.getString("OLMGraphBrowser.Menu.Centre"); //$NON-NLS-1$
    String STR_COLLAPSE =Messages.getString("OLMGraphBrowser.Menu.Collapse"); //$NON-NLS-1$
    String STR_HIDENODE =Messages.getString("OLMGraphBrowser.Menu.HideNode"); //$NON-NLS-1$
    String STR_HIDEEDGE =Messages.getString("OLMGraphBrowser.Menu.HideEdge"); //$NON-NLS-1$
    
    String STR_TOGGLE = Messages.getString("OLMGraphBrowser.Menu.Toggle");    // label for the menu //$NON-NLS-1$
    String STR_LAYOUT = Messages.getString("OLMGraphBrowser.Menu.Layout");    // label for the menu //$NON-NLS-1$
    String STR_EXPORT = Messages.getString("OLMGraphBrowser.Menu.Export");    // label for the menu //$NON-NLS-1$
    
    /**
     * A reference to the panel containing the TouchGraph supporting 
     * this user interface. 
     */
    OLMGraphBrowser graphBrowser;

    /**
     * A reference to the mouse listener associated with the user interface.
     */
    private GLNavigateMouseListener ml;

    /**
     * A reference to the interface for dragging nodes.
     */
    private DragNodeUI dragNodeUI;

    
    JPopupMenu nodePopup;    
    JPopupMenu edgePopup;    
    JPopupMenu controlPopup;    
    
    HashMap popupItems = new HashMap();

    Node popupNode;
    Edge popupEdge;

    /**
     * Default constructor.
     * @param glp   A reference to the panel containing the TG.
     */
    public OLMNavigateUI( OLMGraphBrowser glp ) 
    {
        graphBrowser = glp;
        dragNodeUI = new DragNodeUI(getTGPanel());                    

        setUpNodePopup();
        setUpEdgePopup();
        setUpControlPopup();
        
        setNavigateMouseListener(new GLNavigateMouseListener());
    }
    
    public void setNavigateMouseListener(GLNavigateMouseListener ml)
    {
        this.ml = ml;
    }
    
    protected TGPanel getTGPanel()
    {
        return graphBrowser.getTGPanel();
    }
    
    public void activate() 
    {        
        getTGPanel().addMouseListener(ml);
    }
    
    public void deactivate() 
    {
        getTGPanel().removeMouseListener(ml);
    }
    
    protected TGAbstractDragUI getHVDragUI()
    {
        return graphBrowser.getHVScroll().getHVDragUI();
    }
    
    protected LocalityScroll getLocalityScroll()
    {
        return graphBrowser.getLocalityScroll();
    }
    
    
    class GLNavigateMouseListener extends MouseAdapter {
    
        public void mousePressed(MouseEvent e) {
            Node mouseOverN = getTGPanel().getMouseOverN();
            if (!getTGPanel().isEnabled()) return;

            // Hack for the popup to work in Lynux !!
            if (e.isPopupTrigger()) {
                popupNode = getTGPanel().getMouseOverN();
                popupEdge = getTGPanel().getMouseOverE();
                
                if (popupNode!=null) {
                    getTGPanel().setMaintainMouseOver(true);
                    nodePopup.show(e.getComponent(), e.getX(), e.getY());
                }
                else if (popupEdge!=null) {
                    getTGPanel().setMaintainMouseOver(true);
                    edgePopup.show(e.getComponent(), e.getX(), e.getY());
                }
                else {                    
//                    glPanel.glPopup.show(e.getComponent(), e.getX(), e.getY());
                    controlPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        
            else if (e.getModifiers() == MouseEvent.BUTTON1_MASK) { 
                if (mouseOverN == null) 
                    getHVDragUI().activate(e);
                else 
                    dragNodeUI.activate(e);                    
            }
        }

        public void mouseClicked(MouseEvent e) {
            Node mouseOverN = getTGPanel().getMouseOverN();
            if (!getTGPanel().isEnabled()) return;

            if (e.getModifiers() == MouseEvent.BUTTON1_MASK && e.getClickCount()==1) {
                if ( mouseOverN != null) {                        
                    getTGPanel().setSelect(mouseOverN);
                    //System.out.println("--- node selected : " + mouseOverN.getLabel()); //$NON-NLS-1$
                    //graphBrowser.getHVScroll().slowScrollToCenter(mouseOverN);

                }
            }
            else if (e.getModifiers() == MouseEvent.BUTTON1_MASK && e.getClickCount()==2) { 
                if ( mouseOverN != null) 
                {            
                	if (mouseOverN.visibleEdgeCount()<mouseOverN.edgeCount())
                    {
                		getTGPanel().expandNode(mouseOverN);
                        graphBrowser.getHVScroll().slowScrollToCenter(mouseOverN);
                    }
                	else
                		getTGPanel().collapseNode(mouseOverN);
                	
                        //tgPanel.setSelect(mouseOverN);
//                        try {
//                            //tgPanel.fastFinishAnimation();
//                            getTGPanel().setLocale(mouseOverN, getLocalityScroll().getLocalityRadius()+2);
//                            
//                            //tgPanel.setLocale(mouseOverN,localityScroll.getLocalityRadius(),100,100,true);
//                            
//                        }
//                        catch (TGException ex) {
//                        	//System.err.println("Error setting locale: " + ex); //$NON-NLS-1$
//                            ex.printStackTrace();
//                        }
                        //hvScrollToCenterUI.activate(e);                        
                }
            }    
        }    
        
        public void mouseReleased(MouseEvent e) {
            if (!getTGPanel().isEnabled()) return;

            if (e.isPopupTrigger()) {
                popupNode = getTGPanel().getMouseOverN();
                popupEdge = getTGPanel().getMouseOverE();
                
                if (popupNode!=null) {
                    getTGPanel().setMaintainMouseOver(true);
                    nodePopup.show(e.getComponent(), e.getX(), e.getY());
                }
                else if (popupEdge!=null) {
                    getTGPanel().setMaintainMouseOver(true);
                    edgePopup.show(e.getComponent(), e.getX(), e.getY());
                }
                else {                    
//                    glPanel.glPopup.show(e.getComponent(), e.getX(), e.getY());
                    controlPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }    

    }

    protected void setUpControlPopup()
    {
        controlPopup = new JPopupMenu();
        //controlPopup.setBackground(defaultColor);

        JMenuItem itemControl = new JMenuItem(STR_TOGGLE);
        popupItems.put(STR_TOGGLE,itemControl);
        ActionListener toggleControlsAction = new ActionListener() 
        {
            boolean controlsVisible = true;

            public void actionPerformed(ActionEvent e) {
                controlsVisible = !controlsVisible;
                graphBrowser.getHVScroll().getHorizontalSB().setVisible(controlsVisible);
                graphBrowser.getHVScroll().getVerticalSB().setVisible(controlsVisible);
                graphBrowser.showControlPanel(controlsVisible);//getTopPanel().setVisible(controlsVisible);
            }
        };
        itemControl.addActionListener(toggleControlsAction);
        controlPopup.add(itemControl);

        JMenuItem itemLayout = new JMenuItem(STR_LAYOUT);
        popupItems.put(STR_LAYOUT,itemLayout);
        ActionListener toggleLayoutAction = new ActionListener() 
        {
            boolean isAlive = true;


            public void actionPerformed(ActionEvent e) 
            {
                if (isAlive)
                    getTGPanel().tgLayout.stop();
                else 
                    getTGPanel().tgLayout.start();
                isAlive = !isAlive;
            
            }
        };
        
        itemLayout.addActionListener(toggleLayoutAction);
        controlPopup.add(itemLayout);
        
        JMenuItem menuItem2 = new JMenuItem(STR_EXPORT);
        popupItems.put(STR_EXPORT,menuItem2);
        ActionListener xportControlsAction = new ActionListener() 
        {

            // Returns a generated image.
            public RenderedImage myCreateImage() 
            {
                int width = 1000;
                int height = 1000;
            
                // Create a buffered image in which to draw
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            
                // Create a graphics contents on the buffered image
                Graphics2D g2d = bufferedImage.createGraphics();
            
                // Draw graphics
                getTGPanel().paint(g2d);
            
                // Graphics context no longer needed so dispose it
                g2d.dispose();
                return bufferedImage;
            }
                
            public void actionPerformed(ActionEvent e) 
            {
                // Create an image to save
                RenderedImage rendImage = myCreateImage();
                
                // Write generated image to a file
                try {
                    // Save as PNG
                    File file = new File("newimage.png"); //$NON-NLS-1$
                    ImageIO.write(rendImage, "png", file); //$NON-NLS-1$
                
                    // Save as JPEG
                    file = new File("newimage.jpg"); //$NON-NLS-1$
                    ImageIO.write(rendImage, "jpg", file); //$NON-NLS-1$
                } catch (IOException edd) {
                }
            }
        };
        menuItem2.addActionListener(xportControlsAction);
        controlPopup.add(menuItem2);        
    }
    
    protected void setUpNodePopup() 
    {        
        nodePopup = new JPopupMenu();
        JMenuItem menuItem;
        
        menuItem = new JMenuItem(STR_RECENTRE);
        popupItems.put(STR_RECENTRE,menuItem);
        ActionListener recentreAction = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(popupNode!=null) {
                    	try {
							getTGPanel().setLocale(popupNode, getLocalityScroll().getLocalityRadius());
						} catch (TGException e1) 
                        {
							e1.printStackTrace();
						}
                    }
                }
            };
            
        menuItem.addActionListener(recentreAction);
        nodePopup.add(menuItem);

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

    protected void setUpEdgePopup() 
    {        
        edgePopup = new JPopupMenu();
        JMenuItem menuItem;
                 
        menuItem = new JMenuItem(STR_HIDEEDGE);
        popupItems.put(STR_HIDEEDGE,menuItem);
        ActionListener hideAction = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(popupEdge!=null) {
                        getTGPanel().hideEdge(popupEdge);
                    }
                }
            };
            
        menuItem.addActionListener(hideAction);
        edgePopup.add(menuItem);        
         
        edgePopup.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(PopupMenuEvent e) {}
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                getTGPanel().setMaintainMouseOver(false);
                getTGPanel().setMouseOverE(null);
                getTGPanel().repaint();        
            }
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
        });
    }

} // end com.touchgraph.graphlayout.interaction.GLNavigateUI
