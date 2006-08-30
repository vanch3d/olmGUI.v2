package toulmin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 * Abstract renderer used to display the attribute and its value in a JList.
 * 
 * @see ToulminAttribute
 * @author Nicolas Van Labeke
 * @version $Revision: 1.4 $
 * @deprecated
 */
public abstract class JRenderer extends JLabel implements MouseListener,MouseMotionListener
                                               
{
    /**
     * Indicate if the attribute is selected or not in the container.
     */
    private boolean isSelected = false;
    
    /**
     * Indicate if the attribute has an "information" icon.
     */
    private boolean isInformation = false;
    
    private String attrName = null;
    
    /**
     * The width of the "column" to be used for displaying the attribute's name.
     */
    protected int   margin = 20;

    /**
     * The background color for displaying the attribute/value.
     */
    protected Color bkgColor;
    
    /**
     * The text color to be used for displaying the attribute/value names. 
     */
    protected Color txtColor;
    
    /**
     * The formatting used to display number in the renderer.
     */
    protected Format format = new DecimalFormat(".##");
    
    /**
     * Default constructor for the renderer.
     * The underlying label is initialised as an empty string in order 
     * to ensure proper dimensions. 
     * @param attr  The name of the attribute
     */
    public JRenderer(String attr) 
    {
        super(" ");
        this.attrName = attr;
    }
    
    public String getAttributeName()
    {
        return attrName;
    }

    /* (non-Javadoc)
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) 
    {
        //super.paintComponent(g);
        Dimension dim = getSize();
        if (isSelected)
        {
            bkgColor = UIManager.getColor("List.selectionBackground");
            txtColor= Color.WHITE;
        }
        else
        {
            bkgColor = Color.WHITE;
            txtColor= Color.BLACK;
        }

        g.setColor(bkgColor);
        g.fillRect(0,0,dim.width,dim.height);
        g.setColor(txtColor);
        g.drawString(getAttributeName(),0,dim.height-3);
    }

    /**
     * Get the width of the attribute column.
     * @return    A positive integer.
     */
    public int getMargin() 
    {
        return margin;
    }
    
    /**
     * Set the width of the attribute column.
     * @param margin  A positive integer.
     */
    public void setMargin(int margin) 
    {
        this.margin = margin;
    }

    /**
     * Get the selection status of this attribute in its container.
     * @return  <code>true</code> is the attribute is currently selected,
     *          <code>false</code> otherwise.
     */
    public boolean isSelected() 
    {
        return isSelected;
    }
    
    /**
     * Set the selection status of this attribute in its container.
     * @param isSelected <code>true</code> to select the attribute,
     *                   <code>false</code> otherwise.
     */
    public void setSelected(boolean isSelected) 
    {
        this.isSelected = isSelected;
    }
    
    
    
    public boolean isInformation() {
        return isInformation;
    }

    public void setInformation(boolean isInformation) {
        this.isInformation = isInformation;
    }

    /**
     * Set the format for the renderer.
     * @param format The format to set.
     */
    public void setFormat(Format format)
    {
        this.format = format;
    }

    /**
     * Get the current format of the value in the renderer.
     * @return Returns the format.
     */
    public Format getFormat() 
    {
        return format;
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e){}

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {}   
    
    
}
