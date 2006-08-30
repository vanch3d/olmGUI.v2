/**
 * @file ToulminNode.java
 */
package olmgui.graph;


import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import olmgui.utils.Gradient;

import toulmin.Toulmin;
import toulmin.ToulminBacking;
import toulmin.ToulminClaim;
import toulmin.ToulminData;
import toulmin.ToulminSubClaim;
import toulmin.ToulminWarrant;
import toulmin.XMLRPCWrapper;
import config.OLMQueryResult;

/**
 * A specialisation of the OLM Node tailored for the Toulmin Argumentation Pattern.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.16 $
 */
public class ToulminNode extends OLMNode
{
    
    
    /**
     * The type of the node.
     * Could be one of the constant defining the various element of TAP.
     */
    private String toulminType = null;
    
    /**
     * A reference to the Toulmin Argumentation Pattern whose node is part of.
     */
    private Toulmin toulmin = null;
    
    /**
     * The data (from the TAP) associated with this particular node 
     */
    private XMLRPCWrapper data = null;
    
//    /**
//     * A reference to the Node Configuration used for drawing it.
//     */
//    private NodeConfig config = null;
    
    /**
     * A list of the nodes that can be expanded from this one.
     * Reflect the organisation of the TAP, ie 
     *  - CLAIM will be expanded in DATA
     *  - DATA will be expanded in WARRANT(s)
     *  - WARRANT will be expanded in BACKINGS
     */
    private List nextNode = null;
    
    //private Icon iconRef = null;

    /**
     * Implementation of the icon for the Claim node.
     */
    public static class ClaimIcon implements Icon
    {
        /**
         * Default dimension of the icon
         */
        private static final int ICON_HEIGHT = 15,
                                 ICON_WIDTH = 20;

        private double score = 0.0;
        /**
         * Default constructor
         */
        public ClaimIcon() {
            super();
        }

        public ClaimIcon(double score) {
            super();
            this.score = score;
       }
        
        public void setScore(double score)
        {
            this.score = score;
        }

        public int getIconWidth()
        {
            return ICON_WIDTH;
        }

        public int getIconHeight()
        {
            return ICON_HEIGHT;
        }

        public void paintIcon(Component cpn, Graphics g, int x, int y)
        {
            int w = ICON_WIDTH;
            int h = ICON_HEIGHT;
            Rectangle rect = new Rectangle(x+2,y+2,w-4,h-4);

            Graphics2D g2d = (Graphics2D)g;
            
            // Fill the background
            //g2d.setColor(UIManager.getColor("ToggleButton.highlight"));
            //g2d.fillRect(x,y,w,h);
            
            Gradient grd = new Gradient();
            grd.addPoint(Color.RED);
            grd.addPoint(new Color(255,150,1));
            //grd.addPoint(Color.ORANGE);
            grd.addPoint(Color.YELLOW);
            grd.addPoint(new Color(79,255,1));
            grd.addPoint(new Color(1,187,1));
            //grd.addPoint(Color.GREEN);

            grd.createGradient(rect.height);


            // Draw the bar (with gradiant)
            //Color startColor = Color.green; 
            //Color endColor = Color.red;
           // GradientPaint gradient = new GradientPaint(rect.x, rect.y, startColor, rect.x, rect.y+rect.height, endColor,true);
            //g2d.setPaint(gradient);

            int sc = (int)(score*rect.height);
            g2d.setColor(grd.getColour(sc));
            g2d.fillRect(rect.x+1,rect.y+rect.height-sc, rect.width-3, sc);

            // Draw the scale
            g2d.setColor(Color.BLACK);
            //int inc = rect.y;
            for (int i=0;i<4;i++)
            {
                sc = (int)( rect.y + (1-(i/3.0))*rect.height);
                g2d.drawLine(rect.x,sc,rect.x+rect.width-2,sc);
            }
        }
    }
    
    /**
     * Implementation of the icon for the Data node.
     */
    public static class DataIcon implements Icon
    {
        /**
         * Default dimension of the icon
         */
        private static final int ICON_HEIGHT = 15,
                                 ICON_WIDTH = 30;

        private ArrayList pignistic = null;
        /**
         * Default constructor
         */
        public DataIcon() {
            super();
        }

        public DataIcon(ArrayList pignistic) {
            super();
            this.pignistic = pignistic;
       }
        
        public void setScore(ArrayList pignistic)
        {
            this.pignistic = pignistic;
        }

        public int getIconWidth()
        {
            return ICON_WIDTH;
        }

        public int getIconHeight()
        {
            return ICON_HEIGHT;
        }

        public void paintIcon(Component cpn, Graphics g, int x, int y)
        {
            int w = ICON_WIDTH;
            int h = ICON_HEIGHT;
            Rectangle rect = new Rectangle(x+2,y+2,w-4,h-4);

            Graphics2D g2d = (Graphics2D)g;
            
            // Fill the background
            //g2d.setColor(UIManager.getColor("ToggleButton.highlight"));
            //g2d.fillRect(x,y,w,h);

            // Draw the bar (with gradiant)
            Color startColor = Color.green; 
            Color endColor = Color.red;
            GradientPaint gradient = new GradientPaint(rect.x, rect.y, startColor, rect.x, rect.y+rect.height, endColor,true);
            g2d.setPaint(gradient);

            int nb =pignistic.size();
            double max = 0;

            for (int i=0;i<nb;i=i+2)
            {
                double score =( (Double)pignistic.get(i+1)).doubleValue();
                if (score> max) max = score;
            }
            
            for (int i=0;i<nb;i=i+2)
            {
                int dw = 1+(rect.width-(nb/2))/(nb/2);
                double score =( (Double)pignistic.get(i+1)).doubleValue();
                
                int sc = (int)(score*rect.height/max);
                //g2d.fillRect(rect.x+1,rect.y+1+rect.height-sc, rect.width-3, sc-1);
                g2d.fillRect(rect.x+1+dw*(i/2),rect.y+1+rect.height-sc,dw-1,sc-1);
            }

            // Draw the scale
            g2d.setColor(Color.BLACK);
            //int inc = rect.y;
            for (int i=0;i<4;i++)
            {
                int sc = (int)( rect.y + (1-(i/3.0))*rect.height);
                g2d.drawLine(rect.x,sc,rect.x+rect.width-2,sc);
            }
        }
    }
    
    public static class BackingIcon implements Icon
    {
        /**
         * Default dimension of the icon
         */
        private static final int ICON_HEIGHT = 15,
                                 ICON_WIDTH = 15;

        private Hashtable info = null;
        /**
         * Default constructor
         */
        public BackingIcon() {
            super();
        }

        public BackingIcon(Hashtable info) {
            super();
            this.info = info;
       }
        
        public void setScore(Hashtable info)
        {
            this.info = info;
        }
        
        public Hashtable getScore()
        {
            return this.info;
        }

        public int getIconWidth()
        {
            return ICON_WIDTH;
        }

        public int getIconHeight()
        {
            return ICON_HEIGHT;
        }

        public void paintIcon(Component cpn, Graphics g, int x, int y)
        {
            int w = ICON_WIDTH;
            int h = ICON_HEIGHT;
            //Rectangle rect = new Rectangle(x+2,y+2,w-4,h-5);

            Graphics2D g2d = (Graphics2D)g;
            
            // Fill the background
            g2d.setColor(UIManager.getColor("ToggleButton.highlight"));
            g2d.fillRect(x,y,w,h);
            g2d.setColor( Color.BLACK);
            g2d.draw3DRect(x,y,w,h,true);

            
        }
    }    

    /**
     * Create a new node in the Toulmin graph. 
     * @param type  The type of the node in the TAP.
     * @param id    The identifier of the node.
     * @param label The label of the node.
     */
    public ToulminNode(String type,String id, String label) 
    {
        super(id, label);
        setToulminType(type);
    }
    
    /**
     * @return Returns the data.
     */
    public XMLRPCWrapper getData() {
        return this.data;
    }
    /**
     * @param data The data to set.
     */
    public void setData(XMLRPCWrapper data) {
        this.data = data;
    }
//    /**
//     * @return Returns the type.
//     */
//    public NodeConfig getNodeConfig() {
//        return this.config;
//    }
//    
    
    /**
     * @return Returns the toulmin.
     */
    public Toulmin getToulmin() {
        return this.toulmin;
    }
    
    /**
     * @return Returns the toulminType.
     */
    public String getToulminType() {
        return toulminType;
    }

    /**
     * @param toulminType The toulminType to set.
     */
    public void setToulminType(String toulminType) {
        this.toulminType = toulminType;
    }

    /**
     * @param toulmin The toulmin to set.
     */
    public void setToulmin(Toulmin toulmin) {
        this.toulmin = toulmin;
    }

     /* (non-Javadoc)
     * @see olmgui.graph.OLMNode#setNodeConfig(olmgui.graph.NodeConfig, java.awt.Color)
     */
    public void setNodeConfig(NodeConfig type,Color bkColor) 
    {
        super.setNodeConfig(type,bkColor);
//        this.config = type;
//        setType(type.getType());
//        setFont(type.getFont());
//        if (bkColor==null)
//            setBackColor(type.getBkColor());
//        else
//            setBackColor(bkColor);
//        setTextColor(type.getTxtColor());
        
        if (type==NodeConfig.CLAIM && (data instanceof ToulminSubClaim))
        {
            Hashtable icons = new Hashtable();
            icons.put("ATTRIBUTE.PERFORMANCE.40","/res/perf_low.gif");
            icons.put("ATTRIBUTE.PERFORMANCE.80","/res/perf_high.gif");
            icons.put("ATTRIBUTE.DIFFICULTY.easy","/res/diff_low.gif");
            icons.put("ATTRIBUTE.DIFFICULTY.difficult","/res/diff_high.gif");
            icons.put("ATTRIBUTE.OTHERS","/res/other.gif");
            
            String icon = (String) icons.get(((ToulminSubClaim)data).getValue());
            if (icon == null) 
                setIcon(new BackingIcon());
            else
                setIcon(new ImageIcon(getClass().getResource(icon)));
        }
        
        else if (type==NodeConfig.CLAIM && (data instanceof ToulminClaim))
        {
            setIcon(new ClaimIcon(((ToulminClaim)data).getClaimSummary()));
        }
        if (type==NodeConfig.DATA && (data instanceof ToulminData))
        {
            ToulminData hash = (ToulminData) data;
            setIcon(new DataIcon(hash.getPignistic()));
        }
        if (type==NodeConfig.WARRANT && (data instanceof ToulminWarrant))
        {
            ToulminWarrant hash = (ToulminWarrant) data;
            setIcon(new DataIcon(hash.getData().getPignistic()));
            
        }
        if (type==NodeConfig.BACKING && (data instanceof ToulminBacking))
        {
            ToulminBacking hash = (ToulminBacking) data;
            String res = (String)hash.getValue(OLMQueryResult.EVIDENCE_ITEM);
            //setIcon(new BackingIcon(hash));
            Icon ico = null;
            if (res==null)
                ico = new ImageIcon(getClass().getResource("/res/home.gif")); 
            else
                ico = new ImageIcon(getClass().getResource("/res/exercise_s.gif")); 
                
            setIcon(ico);
        }
    }
    
    public List getNextNode()
    {
        return nextNode;
    }
    
    public void setNextNode(List nextNode) 
    {
        this.nextNode = nextNode;
    }
    
//    /* (non-Javadoc)
//     * @see com.touchgraph.graphlayout.Node#paintNodeBody(java.awt.Graphics, com.touchgraph.graphlayout.TGPanel)
//     * Modified by Brendan to draw a 16x16 icon on the Node
//     */
//    public void paintNodeBody( Graphics g, TGPanel tgPanel)
//    {
//        g.setFont(font);
//        fontMetrics = g.getFontMetrics();
//
//        int ix = (int)drawx;
//        int iy = (int)drawy;
//        int h = getHeight();
//        int w = getWidth();
//        int r = h/2+1; // arc radius
//
//        //Modified by Brendan : adjust bounding rect for icon
//        if (iconRef!=null)
//        {
//            w += iconRef.getIconWidth()+6;
//            h = Math.max(h,iconRef.getIconHeight()+6);
//            //if (h<20) h=20;
//        }
//    
//
//        Color borderCol = getPaintBorderColor(tgPanel);
//        g.setColor(borderCol);
//        
//        if ( typ == TYPE_ROUNDRECT ) {
//            g.fillRoundRect(ix - w/2, iy - h / 2, w, h, r, r);
//        } else if ( typ == TYPE_ELLIPSE ) {
//            g.fillOval(ix - w/2, iy - h / 2, w, h );
//        } else if ( typ == TYPE_CIRCLE ) { // just use width for both dimensions
//            g.fillOval(ix - w/2, iy - w / 2, w, w );
//        } else { // TYPE_RECTANGLE
//            g.fillRect(ix - w/2, iy - h / 2, w, h);
//        }
//        
//        Color backCol = getPaintBackColor(tgPanel);
//        g.setColor(backCol);
//
//        if ( typ == TYPE_ROUNDRECT ) {
//            g.fillRoundRect(ix - w/2+2, iy - h / 2+2, w-4, h-4, r, r );
//        } else if ( typ == TYPE_ELLIPSE ) {
//            g.fillOval(ix - w/2+2, iy - h / 2+2, w-4, h-4 );
//        } else if ( typ == TYPE_CIRCLE ) {
//            g.fillOval(ix - w/2+2, iy - w / 2+2, w-4, w-4 );
//        } else { // TYPE_RECTANGLE
//            g.fillRect(ix - w/2+2, iy - h / 2+2, w-4, h-4);
//        }
//
//        //added by Brendan
//        if (iconRef!=null)
//        {
//            int delta = Math.max(0,h-iconRef.getIconHeight())/2;
//            iconRef.paintIcon(null,g,ix-w/2+3, iy-h/2+delta);
//            //g.drawImage(, ix-w/2+4, iy-h/2+delta, tgPanel);
//        }
//
//
//        Color textCol = getPaintTextColor(tgPanel);
//        g.setColor(textCol);
//
//        //modified by Brendan - moved to right to allow image drawing
//        if (iconRef!=null)
//        {
//            g.drawString(lbl, (ix - fontMetrics.stringWidth(lbl)/2)+iconRef.getIconWidth()/2, iy + fontMetrics.getDescent() +1);
//        }
//        else
//        {
//            g.drawString(lbl, ix - fontMetrics.stringWidth(lbl)/2, iy + fontMetrics.getDescent() +1);
//        }
//    }

    
}
