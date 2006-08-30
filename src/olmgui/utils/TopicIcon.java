/**
 * @file TopicIcon.java
 */
package olmgui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import config.OLMTopicConfig;



/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.6 $
 *
 */
public class TopicIcon implements Icon {

    /**
     * Static dimension for the icon (width=height).
     */
    private int ICON_SIZE = 12;

    /**
     * The map this icon is associated with.
     * 
     */
    private OLMTopicConfig config;
    
    /**
     * Constructor for the icon, given a cmap identifier 
     * @param config
     */
    public TopicIcon(OLMTopicConfig config) {
        super();
        this.config = config;
    }
    
    public TopicIcon(OLMTopicConfig config,boolean small) {
        super();
        this.config = config;
        this.ICON_SIZE = 10;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.ImageIcon#getIconHeight()
     */
    public int getIconHeight() {
        return ICON_SIZE;
    }

    /* (non-Javadoc)
     * @see javax.swing.ImageIcon#getIconWidth()
     */
    public int getIconWidth() {
        return ICON_SIZE;
    }

    /* (non-Javadoc)
     * @see javax.swing.ImageIcon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
     */
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        //super.paintIcon(c, g, x, y);
        if (!c.isEnabled())
        {
            g.setColor(Color.LIGHT_GRAY);
            //g.fillOval(x+1,y+2,ICON_SIZE-2,ICON_SIZE-2);
            g.fillOval(x,y,ICON_SIZE,ICON_SIZE);
       }
        else
        {
            g.setColor(Color.BLACK);
            g.fillOval(x,y,ICON_SIZE,ICON_SIZE);
            g.setColor(config.getColor());
            g.fillOval(x+1,y+1,ICON_SIZE-2,ICON_SIZE-2);
        }
    }


}
