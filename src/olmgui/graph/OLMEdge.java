package olmgui.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;

/**
 * A specialisation of the TouchGraph edge, improving its visual rendering.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.3 $
 */
public class OLMEdge extends Edge 
{

    /**
     * Create an edge between two nodes.
     * 
     * @param from  A reference to the starting node of the edge
     * @param to    A reference to the ending node of the edge
     */
    public OLMEdge(Node from, Node to) 
    {
        super(from, to);
    }

    /**
     * Create an edge between two nodes, with a given length.
     *
     * @param from  A reference to the starting node of the edge
     * @param to    A reference to the ending node of the edge
     * @param len   The length of the edge.
     */
    public OLMEdge(Node from, Node to, int len)
    {
        super(from, to, len);
    }

    /* (non-Javadoc)
     * @see com.touchgraph.graphlayout.Edge#paint(java.awt.Graphics, com.touchgraph.graphlayout.TGPanel)
     */
    public void paint(Graphics g0, TGPanel tgPanel)
    {
        //super.paint(g, tgPanel);
        
        Graphics2D g2 = (Graphics2D)g0;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        col = Color.decode("#888888");
        int i = col.getRed();
        Color color;
        if(tgPanel.getMouseOverN() == getFrom() || tgPanel.getMouseOverE() == this)
            color = new Color(Math.min(i + 100, 255), Math.max(i - 32, 0), Math.max(i - 32, 0));
        else
        if(tgPanel.getMouseOverN() == getTo())
            color = new Color(Math.max(i - 32, 0), Math.max(i - 32, 0), Math.min(i + 100, 255));
        else
            color = super.col;
        int x1=(int) getFrom().drawx;
        int y1=(int) getFrom().drawy;
        int x2=(int) getTo().drawx;
        int y2=(int) getTo().drawy;
        if (intersects(tgPanel.getSize())) paintArrow(g2, x1, y1, x2, y2, color);

    }

    /* (non-Javadoc)
     * @see com.touchgraph.graphlayout.Edge#isVisible()
     */
    public boolean isVisible() 
    {
        return super.isVisible();
    }

    /* (non-Javadoc)
     * @see com.touchgraph.graphlayout.Edge#setVisible(boolean)
     */
    public void setVisible(boolean v) 
    {
        super.setVisible(v);
    }
    
    

}
