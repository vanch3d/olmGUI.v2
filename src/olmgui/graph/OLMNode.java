package olmgui.graph;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;

/**
 * A specialisation of the TouchGraph node, allowing the use of icons and improving
 * its visual rendering.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.3 $
 */
public class OLMNode extends Node 
{

    public NodeConfig   config = null;
    
    protected Icon iconRef = null;
    
    public String hint = null;

    /**
     * @param id    The identifier of the node (need to be unique in the graph).
     * @param label The label of the node.
     */
    public OLMNode(String id, String label) 
    {
        super(id, label);
    }

    /**
     * @param id
     */
    public OLMNode(String id) 
    {
        super(id);
    }
    
    //Added by Brendan to allow image to be set
    public void setImage(ImageIcon iconRef)
    {
        this.iconRef=iconRef;
    }
    
    public void setIcon(Icon icon)
    {
        this.iconRef = icon;
    }
    
    
    /**
     * @return Returns the hint.
     */
    public String getHint() {
        return hint;
    }

    /**
     * @param hint The hint to set.
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * @param config
     */
    public void setNodeConfig(NodeConfig config)
    {
        this.config = config;
        setType(config.getType());
        setFont(config.getFont());
        setBackColor(config.getBkColor());
        setTextColor(config.getTxtColor());
    }

    /**
     * @param config
     * @param bkColor   Override the NodeConfig color, no effect if <code>null</code>.
     */
    public void setNodeConfig(NodeConfig config,Color bkColor)
    {
        this.config = config;
        setType(config.getType());
        setFont(config.getFont());
        if (bkColor==null)
            setBackColor(config.getBkColor());
        else
            setBackColor(bkColor);
       setTextColor(config.getTxtColor());
    }
    
    public NodeConfig getNodeConfig()
    {
        return config;
    }

    //Modified by Brendan to draw a 16x16 icon on the Node
    public void paintNodeBody( Graphics g0, TGPanel tgPanel)
    {
        Graphics2D g2 = (Graphics2D)g0;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(font);
        fontMetrics = g2.getFontMetrics();
        
        Composite originalComposite = g2.getComposite();
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0F));

        Color backCol = getPaintBackColor(tgPanel);
        

        int ix = (int)drawx;
        int iy = (int)drawy;
        int h = getHeight();
        int w = getWidth();
        int r = h/2+1; // arc radius

        //Modified by Brendan : adjust bounding rect for icon
        if (iconRef!=null)
        {
            //w += 16;
            //h = Math.max(h,20);
            //if (h<20) h=20;
            w += iconRef.getIconWidth()+6;
            h = Math.max(h,iconRef.getIconHeight()+6);
        }
    

        Color borderCol = getPaintBorderColor(tgPanel);
        g2.setColor(borderCol);
        
        if ( typ == TYPE_ROUNDRECT ) {
            g2.drawRoundRect(ix - w/2, iy - h / 2, w, h, r, r);
        } else if ( typ == TYPE_ELLIPSE ) {
            g2.drawOval(ix - w/2, iy - h / 2, w, h );
        } else if ( typ == TYPE_CIRCLE ) { // just use width for both dimensions
            g2.drawOval(ix - w/2, iy - w / 2, w, w );
        } else { // TYPE_RECTANGLE
            g2.drawRect(ix - w/2, iy - h / 2, w, h);
        }
        g2.setColor(backCol);
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5F));
     

        if ( typ == TYPE_ROUNDRECT ) {
            g2.fillRoundRect(ix - w/2+1, iy - h / 2+1, w-2, h-1, r, r );
        } else if ( typ == TYPE_ELLIPSE ) {
            g2.fillOval(ix - w/2+1, iy - h / 2+1, w-1, h-1 );
        } else if ( typ == TYPE_CIRCLE ) {
            g2.fillOval(ix - w/2+1, iy - w / 2+1, w-1, w-1 );
        } else { // TYPE_RECTANGLE
            g2.fillRect(ix - w/2+1, iy - h / 2+1, w-1, h-1);
        }

        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1F));
        //added by Brendan
        if (iconRef!=null)
        {
            int delta = Math.max(0,h-16)/2;
            iconRef.paintIcon(null,g2,ix-w/2+3, iy-h/2+delta);
            //g2.drawImage(iconRef.getImage(), ix-w/2+4, iy-h/2+delta, tgPanel);
        }


        Color textCol = getPaintTextColor(tgPanel);
        g2.setColor(textCol);

        //modified by Brendan - moved to right to allow image drawing
        if (iconRef!=null)
        {
            g2.drawString(lbl, (ix - fontMetrics.stringWidth(lbl)/2)+8, iy + fontMetrics.getDescent() +1);
        }
        else
        {
            g2.drawString(lbl, ix - fontMetrics.stringWidth(lbl)/2, iy + fontMetrics.getDescent() +1);
        }
        g2.setComposite(originalComposite);
       
    }
    
    /** Paints a tag with containing a character in a small font. */
    public void paintSmallTag(Graphics g0, TGPanel tgPanel, int tagX, int tagY, 
                              Color backCol, Color textCol, char character) 
    {

        Graphics2D g2 = (Graphics2D)g0;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        g2.setColor(backCol);
        g2.fillRect(tagX, tagY, 8, 8);
        g2.setColor(textCol);
        g2.setFont(SMALL_TAG_FONT);
        g2.drawString(""+character, tagX+2, tagY+7);
    }

    /* (non-Javadoc)
     * @see com.touchgraph.graphlayout.Node#isVisible()
     */
    public boolean isVisible() 
    {
        return super.isVisible();
    }
 
    
    /** Returns true if this Node contains the Point <toulmin>px,py</toulmin>. */
    public boolean containsPoint( double px, double py ) {
        if (iconRef!=null)
            return (( px > drawx-getWidth()/2-iconRef.getIconWidth()/2) && 
                    ( px < drawx+getWidth()/2+iconRef.getIconWidth()/2) 
                && ( py > drawy-getHeight()/2-iconRef.getIconHeight()/2) && 
                    ( py < drawy+getHeight()/2+iconRef.getIconHeight()/2));
        else
            return super.containsPoint(px,py);
    }

    /** Returns true if this Node contains the Point <toulmin>p</toulmin>. */
    public boolean containsPoint( Point p ) {
        if (iconRef!=null)
            return (( p.x > drawx-getWidth()/2-iconRef.getIconWidth()/2) && 
                    ( p.x < drawx+getWidth()/2+iconRef.getIconWidth()/2) 
                && ( p.y > drawy-getHeight()/2-iconRef.getIconHeight()/2) && 
                    ( p.y < drawy+getHeight()/2+iconRef.getIconHeight()/2));
        else
            return super.containsPoint(p);
   }
    
}
