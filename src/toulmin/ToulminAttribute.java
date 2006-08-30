package toulmin;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import config.OLMQueryResult;

import olmgui.i18n.Messages;

/**
 * A generic wrapper for the attribute/value(s) used in the evidence and displayed
 * in the OLM.
 * An attribute can be one of the following types:
 * <ul>
 * <li>{@link #TYPE_STRING} to declare the value as a string (default type).
 * <li>{@link #TYPE_ENUM} to declare the value as a sorted enumeration of strings.
 * <li>{@link #TYPE_DOUBLE} to declare the value as a double (assumed to be in [0,1]). 
 * <li>{@link #TYPE_INTEGER} to declare the value as an integer (no bounds).
 * </ul>
 * 
 * Each attribute will be associated with a renderer used to represent it graphically. 
 * There are 3 types of renderer:
 * <ul>
 * <li>{@link #RENDERER_DEFAULT} displays both the attribute and its value as a simple string.
 *      This default renderer can be associated with any attribute.
 * <li>{@link #RENDERER_ICON} represents the value of the attribute as a sequence of icons, 
 *      each of them indicating one "level" in the given scale. This renderer can only be 
 *      associated with attributes containing a finite mapping for individual values:
 *      either {@link #TYPE_ENUM} (and its explicit mapping) or {@link #TYPE_DOUBLE}/
 *      {@link #TYPE_INTEGER} if a mapping has been defined.
 * <li>{@link #RENDERER_METER} represents the value as a ratio with its optimal range. 
 *      This renderer could be associated with any attribute having an explicit or implicit 
 *      finite mapping of values.
 * </ul>
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.19 $
 */
public class ToulminAttribute 
{
    /**
     * Identifiers for the various type of Attribute
     */
    public static String TYPE_ENUM = "TYPE_ENUM",              ///< The attribute is an emumeration of strings.
                         TYPE_STRING = "TYPE_STRING",          ///< The attribute is a simple string.
                         TYPE_DOUBLE = "TYPE_DOUBLE",          ///< The attribute is a double.
                         TYPE_INTEGER = "TYPE_INTEGER";        ///< The attribute is an integer.

    /**
     * Identifiers for the various type of Attribute
     */
    public static String CAT_EVIDENCE = "0_EVIDENCE",              ///< The attribute is an emumeration of strings.
                         CAT_USER = "1_USER",          ///< The attribute is a simple string.
                         CAT_CONTEXT = "2_CONTEXT";          ///< The attribute is a double.
    
	/**
     * Identifiers for the different renderers used to display the attribute 
     * in the {@link olmgui.output.OLMWarrantPane}.
	 */
	public static String RENDERER_NONE = "RENDERER_NONE",			///< Don't render the attribute at al.
					     RENDERER_DEFAULT = "RENDERER_DEFAULT",    ///< Render the attribute as a simple attribute/value pair.
                         RENDERER_ICON = "RENDERER_ICON",          ///< Render the attribute as an icon meter.   
                         RENDERER_METER = "RENDERER_METER",        ///< Render the attribute as a ratio meter.
                         RENDERER_LIST = "RENDERER_LIST";          ///< Render the attribute as a list of arguments.

    /**
	 * The type of this attribute.
	 */
	private String 		type = null;
	
	/**
	 * The (unique) identifier of this attribute.
	 * The identifier is used to get its transcription from the properties file.
	 */
	private String 		attributeId = null;
    
    private String      category = null;
	
	/**
	 * The name of the attribute, as retrieved from the i18n properties.
	 */
	private String 		attribute = null;
	
	/**
	 * The value associated with the attribute.
	 * It can be a String, a Double or an Integer, depending on the attribute type.
	 */
	private Object 		value = null;
	
	/**
     * The list of possible values that are associated with this attribute. 
     * If the attribute is of type {@link #TYPE_ENUM}, the mapping will contains all possible 
     * values (assumed to be ordered).
     * If the attribute is of type {@link #TYPE_DOUBLE}, the mapping will consist in dividing 
     * the range of the value into equal parts, each value of the list mapping one of these parts.
     * The values in the mapping list are used as key to retrieve their transcription from the
     * i18n properties file.
	 * Note that mapping is mandatory for the {@link #TYPE_ENUM} attribute
	 */
	private ArrayList 	mapping = null;
	
	/**
	 * The renderer used to display the attribute/vlue in the list.
	 */
//	private JRenderer 	renderer = null;
	private String		rendererType = null;
	private Object		rendererData = null;
    
//	/**
//	 * Default renderer used to display the attribute as its attribute/value pair.
//	 */
//	private class JRendererDefault extends JRenderer 
//	{
//        
//		/**
//         * Default constructor for the Renderer
//         * @param attr  The name of the attribute
//		 */
//		public JRendererDefault(String attr) 
//        {
//            super(attr);
//        }
//
//        /* (non-Javadoc)
//		 * @see java.awt.Component#paint(java.awt.Graphics)
//		 */
//		public void paint(Graphics g) 
//		{
//			super.paint(g);
//            Dimension dim = getSize();
//			
//            g.setColor(txtColor);
//            //int delta = 0;
//            g.drawString(getValueName(),margin+5,dim.height-3);
//            if (isInformation())
//            {
//                try {
//                    ImageIcon img = new ImageIcon(getClass().getResource("/res/help_ico.gif"));
//                    //g.drawImage(img.getImage(), margin, 0, 0, 0, 0, 0, 0, 0, null);
//                    g.setColor(bkgColor);
//                    g.fillRect(dim.width-img.getIconWidth()-8,0,img.getIconWidth()+8,dim.height);
//                    img.paintIcon(this, g, dim.width-img.getIconWidth()-2, 0);
//                    //delta = img.getIconWidth() + 1;
//                } catch (RuntimeException e) {}
//            }
//		}
//
//        /* (non-Javadoc)
//         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//         */
//        public void mouseClicked(MouseEvent e) 
//        {
//            //System.out.println("mouseClicked()"); 
//            if (isInformation() && e.getClickCount()==2)
//            {
//                String txt = getValue().toString();
//                txt = txt.replaceAll("\t", "");
//                txt = txt.replaceAll("\\]\\]>", "\n\\]\\]>");
//                txt = txt.replaceAll("<om:OMOBJ", "\n<om:OMOBJ");
//                txt = txt.replaceAll("<om:OMA", "\n<om:OMA");
//                txt = txt.replaceAll("<om:OMS", "\n<om:OMS");
//                //String gg = txt.replaceAll("\t", " ");
//                //txt = gg.replaceAll("\n", "<br>");
//                //txt = "<html>" + txt + "</html>";
//                JOptionPane.showMessageDialog(getTopLevelAncestor(), txt,getAttributeName(),JOptionPane.INFORMATION_MESSAGE);
//            }
//        }
//
//	}
//
//	/**
//	 * Render used to display the attribute as a sequence of icons, each of them 
//	 * representing one "level" in the (ordered) mapping.
//	 * 
//	 * Note that this renderer makes the mapping mandatory for the attribute.
//	 *
//	 */
//	private class JRendererIcon extends JRenderer 
//	{
//        /**
//		 * The icon to use for displaying the actual "level" of the attribute.
//		 */
//		private ImageIcon icon = null;
//        
//		/**
//		 * The icon to use for displaying the potential "level" of the attribute.
//		 */
//		private ImageIcon mask = null;
//		
//        /**
//         * @param attr  The name of the attribute
//         */
//        public JRendererIcon(String attr) 
//        {
//            super(attr);
//        }
//
//		/* (non-Javadoc)
//		 * @see java.awt.Component#paint(java.awt.Graphics)
//		 */
//		public void paint(Graphics g) 
//		{
//			super.paint(g);
//            Dimension dim = getSize();
//            g.setColor(txtColor);
//            g.drawString(getAttributeName(),0,dim.height-3);
//            
//            if (!hasMapping()) return;
//            
//            int index = getMappingIndex();
//            int max = mapping.size();
//            if (icon!=null)
//            {
//            	int width = icon.getIconWidth();
//            	for (int i=0;i<=index;i++)
//            		icon.paintIcon(this,g,margin+(width+1)*i,0);
//            }
//            if (mask!=null)
//            {
//            	int width = mask.getIconWidth();
//                for (int i=index+1;i<max;i++)
//                    mask.paintIcon(this,g,margin+(width+1)*i,0);
//            }
//
//		}
//
//		/**
//         * Define the icons to use for the actual and potential values of the attribute
//		 * @param icon The icon representing the actual "level".
//		 * @param mask The icon representing the potential "level".
//		 */
//		public void setIcons(ImageIcon icon,ImageIcon mask) 
//		{
//			this.icon = icon;
//			this.mask = mask;
//		}
//
//        /* (non-Javadoc)
//         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//         */
//        public void mouseClicked(MouseEvent e) 
//        {
//            //System.out.println("mouseClicked()"); 
//        }
//	}
//
//	/**
//	 * Renderer used to display the attribute as a meter, ie the value represented as 
//	 * a ratio of its optimal range.
//	 * Note that the meter assumes the value to be a Double and to be restricted to 
//	 * the [0,1] interval.
//	 * 
//	 * @todo could be a good idea to have an explicit [min,max] interval.
//	 */
//	public class JRendererMeter extends JRenderer 
//	{
//		private Color meterColor = new Color(0,150,67);
//		/**
//         * Default constructor for the Meter Renderer.
//         * Set the format to percentage.
//         * @param attr  The name of the attribute
//         * 
//         */
//        public JRendererMeter(String attr) 
//        {
//            super(attr);
//            NumberFormat fmt = new DecimalFormat("###.##%");
//            setFormat(fmt);
//        }
//
//        /* (non-Javadoc)
//		 * @see java.awt.Component#paint(java.awt.Graphics)
//		 */
//		public void paint(Graphics g) 
//		{
//			super.paint(g);
//            Dimension dim = getSize();
//            
//            // draw the attribute name
//            g.setColor(txtColor);
//            g.drawString(getAttributeName(),0,dim.height-3);
//
//            // get the dimension of the meter and draw its background
//			int valueWidth = dim.width - 5 - margin;
//            g.setColor(Color.LIGHT_GRAY);
//            g.fillRect(margin ,1,valueWidth,dim.height-3);
//
//            // get the formatted values
//            String valueTxt = value.toString();
//            double val = 0.;
//            if (value instanceof Double) 
//            {
//            	val = ((Double)value).doubleValue();
//	            //NumberFormat fmt = new DecimalFormat("###.##%");
//	            valueTxt = getFormat().format(value);
//			}
//            else  if (value instanceof Integer) 
//            {
//                val = ((Integer)value).doubleValue();
//            	val = val / 100.;
//	            //NumberFormat fmt = new DecimalFormat("###.##%");
//	            valueTxt = getFormat().format(new Double(val));
//            }
//            
//            // get the dimension of the meter in the [0,1] interval
//            int posWidth= (int)(valueWidth*val);
//            // Draw the meter
//            g.setColor(meterColor);
//            g.fillRect(margin ,2,posWidth,dim.height-4);
//            
//            // draw the value
//            g.setColor(Color.WHITE);
//            g.drawString(valueTxt,margin+5,dim.height-4);
//		}
//        
//        
//
//        /**
//         * @param meterColor
//         */
//        public void setMeterColor(Color meterColor) 
//        {
//            this.meterColor = meterColor;
//        }
//
//        public void mouseClicked(MouseEvent e) 
//        {
//            //System.out.println("mouseClicked()"); 
//        }
//	}
//
//    /**
//     */
//    private class JRendererList extends JRenderer 
//    {
//        int index = -1;
//        int size = -1;
//        boolean selL = false;
//        boolean selR = false;
//
//        /**
//         * @param attr
//         */
//        public JRendererList(String attr) 
//        {
//            super(attr);
//        }
//        
//        /* (non-Javadoc)
//         * @see java.awt.Component#paint(java.awt.Graphics)
//         */
//        public void paint(Graphics g) 
//        {
//            super.paint(g);
//            Dimension dim = getSize();
//            
//            String val = null;
//            if (getValue() instanceof Vector)
//            {
//                index = (index==-1)? 0 : index;
//                Vector vec = (Vector) getValue();
//                val = vec.get(index).toString();
//                size = vec.size();
//            }
//            else
//                val = getValueName();
//            
//            if (index!=-1)
//            {
//                Polygon left = new Polygon();
//                left.addPoint(margin,(dim.height)/2);
//                left.addPoint(margin+(dim.height-4)/2,2);
//                left.addPoint(margin+(dim.height-4)/2,dim.height-2);
//
//                Polygon right = new Polygon();
//                right.addPoint(dim.width-2,(dim.height)/2);
//                right.addPoint(dim.width-2-(dim.height-4)/2,2);
//                right.addPoint(dim.width-2-(dim.height-4)/2,dim.height-2);
//            
//                if (size>1)
//                    if (!selL)
//                        g.setColor(Color.BLACK);
//                    else
//                        g.setColor(Color.RED);
//                else
//                    g.setColor(java.awt.SystemColor.inactiveCaption);
//                g.fillPolygon(left);
//                if (size>1)
//                    if (!selR)
//                        g.setColor(Color.BLACK);
//                    else
//                        g.setColor(Color.RED);
//                else
//                    g.setColor(java.awt.SystemColor.inactiveCaption);
//                g.fillPolygon(right);
//            }
//            
//            g.setColor(txtColor);
//            g.drawString(val,margin+5+(dim.height-4)/2,dim.height-4);
//        }
//
//
//        /* (non-Javadoc)
//         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//         */
//        public void mouseClicked(MouseEvent e)
//        {
//            if (!(getValue() instanceof Vector)) return;
//            
//            Point pt = e.getPoint();
//            Component cpt = e.getComponent();
//            //JList obj = (JList)e.getSource();
//            //obj.locationToIndex(pt);
//            //Point pt = obj.getLocation();
//            Point loc = this.getLocation();
//            Dimension dim = new Dimension(-loc.x,-loc.y);
//            pt.y = dim.height/2;
//            
//            Polygon right = new Polygon();
//            right.addPoint(dim.width-2,(dim.height)/2);
//            right.addPoint(dim.width-2-(dim.height-4)/2,2);
//            right.addPoint(dim.width-2-(dim.height-4)/2,dim.height-2);
//
//            Polygon left = new Polygon();
//            left.addPoint(margin,(dim.height)/2);
//            left.addPoint(margin+(dim.height-4)/2,2);
//            left.addPoint(margin+(dim.height-4)/2,dim.height-2);
//
//            Vector vec = (Vector)getValue();
//            int size = (vec!=null)?vec.size():0;
//            boolean in = right.contains(pt);
//            if (in) 
//            {
//                index++;
//                if (index>=size) index=0;
//            }
//            in = left.contains(pt);
//            if (in) 
//            {
//                index--;
//                if (index<0) index=size-1;
//            }
//            cpt.repaint();
//                       
//        }
//
//        public void mouseMoved(MouseEvent e) 
//        {
//            if (!(getValue() instanceof Vector)) return;
//            selL = false;
//            selR= false;
//            
//            Point pt = e.getPoint();
//            Component cpt = e.getComponent();
//            JList list = (JList)e.getSource();
//            int index = list.locationToIndex(pt);
//            
//            if (index==-1) 
//                {
//                cpt.repaint();
//                return;
//                }
//            
//            Object obj1 = list.getSelectedValue();
//            Object obj2 = list.getModel().getElementAt(index);
//            
//            if (obj1!=obj2) {
//                cpt.repaint();
//                return;
//                }
//            
//            Point loc = this.getLocation();
//            Dimension dim = new Dimension(-loc.x,-loc.y);
//            pt.y = dim.height/2;
//            
//            Polygon right = new Polygon();
//            right.addPoint(dim.width-2,(dim.height)/2);
//            right.addPoint(dim.width-2-(dim.height-4)/2,2);
//            right.addPoint(dim.width-2-(dim.height-4)/2,dim.height-2);
//
//            Polygon left = new Polygon();
//            left.addPoint(margin,(dim.height)/2);
//            left.addPoint(margin+(dim.height-4)/2,2);
//            left.addPoint(margin+(dim.height-4)/2,dim.height-2);
//
//            Vector vec = (Vector)getValue();
//            //int size = (vec!=null)?vec.size():0;
//            boolean in = right.contains(pt);
//            if (in) 
//            {
//                selL = false;
//                selR= true;
//            }
//            in = left.contains(pt);
//            if (in) 
//            {
//                selL = true;
//                selR= false;
//           }
//            cpt.repaint();
//        }
//
//    }
    
	/**
     * Create a new instance of an attribute.
	 * @param type         The type of the attribute.
	 * @param attributeId  The identifier of the attribute.
	 * @param value        The value associated with the attribute.
	 */
	public ToulminAttribute(String type, String attributeId, Object value) 
	{
		super();
		this.attributeId = attributeId;
		this.type = type;
		this.value = value;
		//this.renderer = new JRendererDefault();
		setRenderer(RENDERER_DEFAULT);
        setCategory(CAT_EVIDENCE);
	}


	/**
	 * Return the name of the attribute, as retrieved from the i18n properties.
	 * The key of the attribute is of the form "ATTRIBUTE.X", where X is the attribute
	 * identifier stored in {@link #attributeId}.
	 * @return	<code>null</code> if the attribute cannot be retrieved from the i18n 
	 *			properties file, a string in the proper language otherwise.
     *           
     * @todo Replace getString() by getSafeString() when debugging is OK.
     *           
	 */
	public String getAttributeName()
	{
		if (attribute==null)
			attribute = Messages.getString("ATTRIBUTE." + this.attributeId);
		return attribute;
	}
	
	/**
     * Get the raw value associated with this attribute.
	 * @return A reference to the raw value.
	 */
	public Object getValue()
	{
		return this.value;
	}
	
	/**
     * Get the i18n name of the value associated with this attribute.
     * If the attribute has a mapping associated, the name of the value is retrieved from it.
     * If no explicit mapping is defined, then a string is looked for in the i18n properties file,
     * using the key "ATTRIBUTE.X.Y", where X is the attribute identifier and Y the value identifer.
     * If no such string exists, then the value is directly converted to a string and returned.
	 * @return A string transcribing the value.
	 */
	public String getValueName()
	{
		if (hasMapping())
			return getMappingName();
		else 
        {
            String prop = Messages.getSafeString("ATTRIBUTE." + this.attributeId + "." + this.value);
            if (prop == null)
                prop = value.toString();
			return prop;
        }
	}
    
    public String getAttributeDescription()
    {
        String prop = Messages.getString("ATTRIBUTE." + this.attributeId + ".description");
        return prop;
    }

    /**
     * Set the renderer to be used with this attribute. 
     * @param renderer The identifier of the render.
     */
    public void setRenderer(String renderer)
    {
    	this.rendererType = renderer;
    	this.rendererData = null;
//        if (RENDERER_NONE.equals(renderer))
//            this.renderer = null;
//        else if (RENDERER_ICON.equals(renderer))
//            this.renderer = new JRendererIcon(getAttributeName());
//        else if (RENDERER_METER.equals(renderer))
//            this.renderer = new JRendererMeter(getAttributeName());
//        else if (RENDERER_LIST.equals(renderer))
//            this.renderer = new JRendererList(getAttributeName());
//        else // RENDERER_DEFAULT
//        {
//        	this.rendererType = RENDERER_DEFAULT;
//            this.renderer = new JRendererDefault(getAttributeName());
//        }
    }

//	/**
//	 * Update and return the renderer associated with this attribute.
//	 * @param isSelected <code>true</code> if the component is selected in the container,
//	 * 					 <code>false</code> otherwise
//	 * @return	A reference to the renderer is defined, <code>null</code> otherwise.
//	 */
//	public JRenderer getRenderer(boolean isSelected) 
//	{
//		if (renderer==null) return null;
//		renderer.setSelected(isSelected);
//		return renderer;
//	}
	
//	/**
//     * Update and return the renderer associated with this attribute.
//     * @param isSelected <code>true</code> if the component is selected in the container,
//     *                   <code>false</code> otherwise
//     * @param margin     The width of the attribute column to be used.
//     * @return  A reference to the renderer is defined, <code>null</code> otherwise.
//	 */
//	public JRenderer getRenderer(boolean isSelected,int margin) 
//	{
//		if (renderer==null) return null;
//		renderer.setSelected(isSelected);
//		renderer.setMargin(margin);
//		return renderer;
//	}


	/**
     * Get the identifier of this attribute.
	 * @return The identifer of this attribute.
	 */
	public String getAttributeId() {
		return attributeId;
	}


	/**
     * Get the type of this attribute.
	 * @return The type of the attribute.
	 */
	public String getType() {
		return type;
	}
    
    
	
	public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    /**
     * Add a new key into the mapping list.
     * Note that the mapping list is assumed to be orderer.
	 * @param key  The identifier of the new element to add in the mapping list.
	 */
	public void addMapping(String key)
	{
		if (mapping==null)
			mapping = new ArrayList();
		mapping.add(key);
	}
	
	/**
     * Check whether the attribute has a mapping or not.
	 * @return <code>true</code> is a mapping list is defined, <code>false</code> otherwise.
	 */
	public boolean hasMapping()
	{
		return (mapping!=null && mapping.size()!=0);
	}

	
	/**
	 * Return the index of the value in the mapping list.
	 * 
	 * If the value is a string, the mapping should contains the value which index 
	 * is returned. If the value is a double, the mapping contains X intervals of 
	 * equal size, the index of the interval containing the value being returned.
	 * 
	 * @return 	-1 if the attribute has no mapping associated with it or if the value 
	 * 			canot be find in the mapping, the index of the value in the mapping
	 * 			otherwise.
	 */
	public int getMappingIndex()
	{
		if (!hasMapping()) return -1;
		if (this.value instanceof Double)
		{
			int index = (int) (((Double)value).doubleValue() * mapping.size());
            if (index >= mapping.size())
                index = mapping.size()-1;
			return index;
		}
		else
		{
			return this.mapping.indexOf(this.value);
		}
	}
	
	public ArrayList getMapping() {
		return mapping;
	}


	/**
     * Get the name of the mapping associated with the current value.
     * Retrieve the string from the i18n properties file, using "ATTRIBUTE.X.Y" as a key,
     * where X is the identifier of the attribute and Y is the mapping identifier.
	 * @return A string transcribing the value, <code>null</code> is no relevant mapping
     *         could be found.
     *         
     * @todo Replace getString() by getSafeString() when debugging is OK.
	 */
	public String getMappingName()
	{
		String mapname = null;
		
		int index = getMappingIndex();
		if (index!=-1)
		{
			String mapvalue = (String)mapping.get(index);
			mapname = Messages.getString("ATTRIBUTE." + this.attributeId + "." + mapvalue);
		}
		return mapname;
	}
	
	

    public Object getRendererData() {
		return rendererData;
	}


	public String getRendererType() {
		return rendererType;
	}


	/**
     * Define the icons to use for the actual and potential values of the attribute.
     * Note that it will ony have an effect if the renderer is {@link #RENDERER_ICON}
     * @param icon The icon representing the actual "level".
     * @param mask The icon representing the potential "level".
     */
    public void setRendererIcons(ImageIcon icon,ImageIcon mask) 
    {
        if (RENDERER_ICON.equals(rendererType))
        {
//            JRendererIcon rIcon = (JRendererIcon) renderer;
//            rIcon.setIcons(icon,mask);
            ArrayList icons = new ArrayList();
            icons.add(icon);
            icons.add(mask);
            rendererData = icons;
        }
        
    }   
    
    public void setRendererMeterColor(Color clr)
    {
        if (RENDERER_METER.equals(rendererType))
        {
//            JRendererMeter rmeter = (JRendererMeter) renderer;
//            rmeter.setMeterColor(clr);
            rendererData = clr;
        }
    }
    
    	
    public static ToulminAttribute UNKNOWN(String attributeId)
    {
        ToulminAttribute attribute = new ToulminAttribute(ToulminAttribute.TYPE_STRING,attributeId,OLMQueryResult.EVIDENCE_XXX)
        {
            
            
            public Object getValue() 
            {
                return Messages.getSafeString("ATTRIBUTE." + OLMQueryResult.EVIDENCE_XXX);
            }

            public String getValueName() {
                return Messages.getSafeString("ATTRIBUTE." + this.getAttributeId());

            }

            public String getAttributeName() 
            {
                System.err.println("Problem with missing attribute " + this.getAttributeId());
                return super.getAttributeName();
            }
            
        };
        return attribute;
    }

}
