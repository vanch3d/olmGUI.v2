package olmgui.utils;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * An extension of a Combobox allowing the use of icons in front of each item.
 * @author Nicolas Van Labeke
 * @version $Revision: 1.6 $
 */
public class JIconComboBox extends JComboBox 
{
    ImageIcon[] images = null;
    String[] strings=null;

    class ComboBoxRenderer extends JLabel implements ListCellRenderer 
    {
        public ComboBoxRenderer() 
        {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
            setIconTextGap(15);
        }
        
        /*
        * This method finds the image and text corresponding
        * to the selected value and returns the label, set up
        * to display the text and image.
        */
        public Component getListCellRendererComponent(
                            JList list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus)
        {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            int selectedIndex = (value==null) ? -1 : ((Integer)value).intValue();
            
            if (isSelected) 
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } 
            else 
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            
            if (selectedIndex!=-1)
            {
                //Set the icon and text.  If icon was null, say so.
                String pet = strings[selectedIndex];
                if (images!=null)
                {
                    ImageIcon icon = images[selectedIndex];
                    setIcon(icon);
                }
                setText(pet);
                setFont(list.getFont());
            }
            return this;
        }
    }
    
    public JIconComboBox() 
    {
        super();
        setRenderer(new ComboBoxRenderer());
        initialize();
        
    }

     /**
     * This method initializes this
     * 
     */
    private void initialize()
    {
        this.setSize(new java.awt.Dimension(130,25));
            
    }
   
    public void setData(String[] strings,ImageIcon[] images)
    {
        this.images = images;
        this.strings = strings;
        removeAllItems();
        for (int i = 0; i < this.strings.length; i++) 
        {
            addItem(new Integer(i));//this.strings[i]);
        }
    }
}  //  @jve:decl-index=0:visual-constraint="10,10" 
