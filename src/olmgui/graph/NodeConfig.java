/**
 * @file NodeConfig.java
 */
package olmgui.graph;

import java.awt.Color;
import java.awt.Font;

import com.touchgraph.graphlayout.Node;

import config.OLMTopicConfig;



/**
 * The configuration for the various nodes displayed in the Graph pane.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.10 $
 *
 */
public final class NodeConfig {

    private static String FONTNAME = "Arial"; 

    /**
     * The shape of the node.
     * @see Node for a list of possible shape identifiers.
     * 
     */
    private int type;
    /**
     * The font used to display the node's title.
     */
    private Font font;
    /**
     * The background color for the node.
     */
    private Color bkColor;
    /**
     * The text color for the node
     */
    private Color txtColor;
    
    public static final NodeConfig TOPNODE = new NodeConfig(
            Node.TYPE_RECTANGLE,
            new Font(FONTNAME,Font.BOLD,20),
            new Color(208,96,0),
            Color.BLACK);

    public static final NodeConfig TOPIC = new NodeConfig(
            Node.TYPE_RECTANGLE,
            new Font(FONTNAME,Font.PLAIN,10),
            Color.LIGHT_GRAY,
            Color.BLACK);

    public static final NodeConfig TOPICROOT = new NodeConfig(
            Node.TYPE_RECTANGLE,
            new Font(FONTNAME,Font.BOLD,11),
            Color.LIGHT_GRAY,
            Color.BLACK);
    
    public static final NodeConfig TOPICASSOC = new NodeConfig(
            Node.TYPE_ELLIPSE,
            new Font(FONTNAME,Font.ITALIC,10),
            Color.LIGHT_GRAY,
            Color.BLACK);

    public static final NodeConfig BELIEF = new NodeConfig(
            Node.TYPE_RECTANGLE,
            new Font(FONTNAME,Font.BOLD,10),
            new Color(208,96,0),
            Color.BLACK);

    public static final NodeConfig BELIEFASSOC = new NodeConfig(
            Node.TYPE_ELLIPSE,
            new Font(FONTNAME,Font.ITALIC,10),
            java.awt.SystemColor.control,//new Color(208,96,0),
            Color.BLACK);

    public static final NodeConfig CLAIM = new NodeConfig(
            Node.TYPE_RECTANGLE,
            new Font(FONTNAME,Font.BOLD,10),
            new Color(128,128,255),
            Color.BLACK);
 
    public static final NodeConfig DATA = new NodeConfig(
            Node.TYPE_RECTANGLE,
            new Font(FONTNAME,Font.PLAIN,9),
            new Color(128,128,255),
            Color.BLACK);
    
    public static final NodeConfig WARRANT = new NodeConfig(
            Node.TYPE_RECTANGLE,
            new Font(FONTNAME,Font.PLAIN,9),
            new Color(128,128,255),
            Color.BLACK);

    public static final NodeConfig BACKING = new NodeConfig(
            Node.TYPE_RECTANGLE,
            new Font(FONTNAME,Font.PLAIN,9),
            new Color(128,128,255),
            Color.BLACK);
    
    /**
     * @param type
     * @param font
     * @param bk
     * @param txt
     */
    private NodeConfig(int type, Font font, Color bk, Color txt) {
        super();
        this.type = type;
        this.font = font;
        this.bkColor = bk;
        this.txtColor = txt;
    } 
    
    public void setConfig(Node node)
    {
        node.setType(this.type);
        node.setFont(this.font);
        node.setBackColor(this.bkColor);
        node.setTextColor(this.txtColor);
    }

    public void setConfig(Node node,OLMTopicConfig cfg)
    {
        node.setType(this.type);
        node.setFont(this.font);
        node.setBackColor(cfg.getColor());
        node.setTextColor(Color.BLACK);
        
    }

    /**
     * @return Returns the bkColor.
     */
    public Color getBkColor() {
        return bkColor;
    }

    /**
     * @return Returns the font.
     */
    public Font getFont() {
        return font;
    }

    /**
     * @return Returns the txtColor.
     */
    public Color getTxtColor() {
        return txtColor;
    }

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }
    
    
    
}
