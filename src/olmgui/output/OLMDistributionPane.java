/**
 * @file OLMDistributionPane.java
 */
package olmgui.output;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;

import olmgui.i18n.Messages;
import olmgui.utils.LevelSet;
import olmgui.utils.OutputViewPanel;


/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.25 $
 *
 */
public class OLMDistributionPane extends JScrollPane
{

    /**
     * A reference to the list used to represent the distribution(s). 
     */
    private JList jList = null;
    
    private ArrayList pignistic = null;
    private ArrayList disribution = null;
    private ArrayList confidence = null;
    private ArrayList discount = null;
    
    private ArrayList swapList = new ArrayList();
    
    /**
     * The default height of a row in the distribution list.
     */
    private int       rowSize = 25;

    /**
     * The possible display attribute of the Distribution Pane.
     */
    public final static String CONFIDENCE = "CONFIDENCE",       ///< Display the confidence intervals.
                               MASSDISTRIB = "MASSDISTRIB",     ///< Display the mass distribution (of the belief).
                               EVIDENCE = "EVIDENCE",           ///< Display the mass distribution (of an evidence).
                               DECAYED = "DECAYED",             ///< Display the decayed mass distribution (of an evidence).
                               PIGNISTIC = "PIGNISTIC";         ///< Display the pignistic function.
    /**
     * The current display attribute of the distrubution Pane.
     */
    private String attribute = PIGNISTIC;

    /**
     * A wrapper used to store the values for each set of the mass distribution.
     */
    private class DistributionWrapper 
    {
        /**
         * The name of the set stored in this wrapper
         */
        private LevelSet levelSet;
        
        /**
         * The certainty value to
         */
        private double certain; 
        
        /**
         * 
         */
        private double plausib; 
        
        /**
         * 
         */
        private String attr;
        
        public DistributionWrapper(String attr, LevelSet str,double c,double p)
        {
            this.levelSet = str;
            this.certain =c;
            this.plausib =p;
            this.attr=attr;
        }

        public DistributionWrapper(String attr, LevelSet str,double c)
        {
            this.levelSet = str;
            this.certain =c;
            this.plausib =-1;
            this.attr=attr;
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString() 
        {
            String template = null;
            if (certain<0.005)
                template = Messages.getSafeString("MASS." + attr + ".description.empty");
            else
                template = Messages.getSafeString("MASS." + attr + ".description." + levelSet.getId());
            if (template==null)
                template = Messages.getString("MASS." + attr + ".description");
            
            NumberFormat fmt = new DecimalFormat("###.##%");
            Object[] arg=
            {fmt.format(this.certain),
                    levelSet.toString()};
            return MessageFormat.format(template,arg);
//            if (plausib!=-1)
//                return (fmt.format(this.certain) + " / " + fmt.format(this.plausib));
//            else 
//                return (fmt.format(this.certain));                
        }
        
    }

    /**
     * Renderer used to display every individual set in the Distribution list.
     */
    private class DistributionRenderer extends JLabel implements ListCellRenderer 
    {
        
        private LevelSet    levelSet = null;
        private double      certain = -1;
        private double      plausib =-1;
        private String      attribute = null;
        private boolean     isSelected=false;
        
        private Color CLR_PIGNISTIC = Color.blue;
        private Color CLR_CONFIDENCE_CERTAINTY = Color.green;
        private Color CLR_CONFIDENCE_PLAUSIBILTY = Color.red;
        private Color CLR_MASS_NORMAL = new Color(192,0,192);
        private Color CLR_MASS_DECAYED = new Color(220,0,220);
        
        /**
         * Default constructor 
         */
        public DistributionRenderer() 
        {
            //super();
        }

        /* (non-Javadoc)
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        protected void paintComponent(Graphics gr) 
        {
            //super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)gr;
            Dimension dim = getSize();

            if (isSelected)
                g2.setColor(UIManager.getColor("List.selectionBackground"));
            else
                g2.setColor(Color.WHITE);
            g2.fillRect(0,0,dim.width,dim.height-1);

            
            if (attribute.equals(PIGNISTIC))
                g2.setColor(CLR_PIGNISTIC);
            else if (attribute.equals(MASSDISTRIB) || attribute.equals(EVIDENCE))
                g2.setColor(CLR_MASS_NORMAL);
            else if (attribute.equals(DECAYED))
            	g2.setColor(CLR_MASS_DECAYED);
            else if (attribute.equals(CONFIDENCE))
                g2.setColor(CLR_CONFIDENCE_CERTAINTY);
            g2.fillRect(0,0,(int)(dim.width*certain),dim.height-1);

            if (attribute.equals(CONFIDENCE) && plausib!=-1)
            {
                // draw the plausibility part of the bar chart
                g2.setColor(CLR_CONFIDENCE_PLAUSIBILTY);
                g2.fillRect((int)(dim.width*certain),0,(int)(dim.width*plausib),dim.height-1);
            }
            if ((attribute.equals(MASSDISTRIB)|| attribute.equals(EVIDENCE)) && plausib!=-1)
            {
                // if mass and decayed are in the same view, draw the decay
            	g2.setColor(CLR_MASS_DECAYED);
                g2.fillRect(0,0,(int)(dim.width*plausib),dim.height-1);
                if (LevelSet.theLot == levelSet)
                {
                    g2.setColor(CLR_MASS_NORMAL);
                    g2.fillRect(0,0,(int)(dim.width*certain),dim.height-1);

                }
            }

            g2.setColor(java.awt.SystemColor.controlShadow);
            Stroke stroke = new BasicStroke(
                    1f,                  // Width of stroke
                    BasicStroke.CAP_ROUND,  // End cap style
                    BasicStroke.JOIN_MITER, // Join style
                    15.0f,                  // Miter limit
                    new float[] {2.f,2.f}, // Dash pattern
                    5.0f); 
            g2.setStroke(stroke);
            for (int i=0;i<3;i++)
            {
                double locx = .25*(i+1);
                g2.drawLine((int)(dim.width*locx),0,(int)(dim.width*locx),dim.height-1);
            }
            
            g2.setColor(Color.black);
            FontMetrics fontMetrics = g2.getFontMetrics();
            int asc = fontMetrics.getMaxAscent();
            int desc = fontMetrics.getMaxDescent();

            Font ft = g2.getFont();
            Font ftnew = ft.deriveFont(Font.BOLD);
            g2.setFont(ftnew);
            int y = dim.height-1;
            y = y -(y -asc+desc)/2;//dim.height-(dim.height-asc)/2
            g2.drawString(getText(),5,y);
        }

        /* (non-Javadoc)
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(
                JList list,
                Object value,   // value to display
                int index,      // cell index
                boolean iss,    // is the cell selected
                boolean chf)    // the list and the cell have the focus
        {
            ListModel model = list.getModel();
            setText("");
            if (model!=null)
            {
                Object obj = model.getElementAt(index);
                if (obj instanceof DistributionWrapper) 
                {
                    DistributionWrapper wrapper = (DistributionWrapper) obj;
                    certain = wrapper.certain;
                    plausib = wrapper.plausib;
                    levelSet = wrapper.levelSet;
                    attribute = wrapper.attr;
                    isSelected = iss;
                    setText(wrapper.levelSet.toString());
                }
            }
            // Adjust the size of the row
            Dimension dim = getPreferredSize();
            setPreferredSize(new Dimension(dim.width,rowSize));
            return this;        
        }

    }


    /**
     * Renderer for the header of the distribution list. 
     */
    private class DistributionHeader extends JLabel
    {
        
        /**
         * Default constructor for the list header.
         * Initialise the label with the empty string, in order to define its default
         * dimensions.
         */
        private DistributionHeader() 
        {
            super(" ");
        }

        /* (non-Javadoc)
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        protected void paintComponent(Graphics gr)
        {
            //super.paintComponent(gr);
            Dimension dim = getSize();
            
            gr.setColor(java.awt.SystemColor.control);
            gr.fillRect(0,0,dim.width,dim.height-1);
            
            gr.setColor(Color.BLACK);
            gr.setFont(new Font("Arial",Font.BOLD,11));
            FontMetrics fm = gr.getFontMetrics();

            gr.drawString("0%",2,fm.getHeight()-3);
            
            int width = fm.stringWidth("50%");
            gr.drawString("50%",dim.width/2-width/2,fm.getHeight()-3);

            width = fm.stringWidth("100%");
            gr.drawString("100%",dim.width-width-2,fm.getHeight()-3);
        }
        
    }
    
    /**
     * Default constructor. 
     */
    public OLMDistributionPane() 
    {
        super();

        initialize();
    }

    /**
     * Perform default initialisation of the Distribution pane.
     */
    private void initialize() 
    {
        this.setSize(300, 200);
        this.setViewportView(getJList());
        this.setColumnHeaderView(new DistributionHeader());
    }

    /**
     * Get the list containing the distributions.	
     * @return  A reference to the widget containing the list of sets.
     */
    private JList getJList() 
    {
    	if (jList == null) {
    		jList = new JList()
            {

                public String getToolTipText(MouseEvent evt) {
                    Point p = evt.getPoint();
                    int index = locationToIndex(p);
                    Rectangle cellBounds;
                    boolean in =  (index != -1 && (cellBounds =
                        getCellBounds(index, index)) != null &&
                        cellBounds.contains(p.x, p.y)) ;
                    Object obj = getModel().getElementAt(index);
                    if (in && obj instanceof DistributionWrapper) 
                    {
                        DistributionWrapper wrapper = (DistributionWrapper) obj;
                        String text = wrapper.toString();
                        int length = text.length();
                        if (length>40)
                        {
                            String hh = "";
                            String tt = "";
                            String[] list = text.split(" ");
                            for (int i=0;i<list.length;i++)
                            {
                                hh += list[i];
                                tt += list[i];
                                if (hh.length()>40)
                                {
                                    tt+="<br>";
                                    hh="";
                                }
                                else
                                {
                                    hh+=" ";
                                    tt+=" ";
                                }
                            }
                            tt = tt.trim();
                            text = tt;
                        }
                        text = "<html>" + text + "</html>";
                        return text;
                    }
                    return super.getToolTipText(evt);
                }
                
            };
            
            jList.setCellRenderer(new OLMDistributionPane.DistributionRenderer());
            jList.setBackground(java.awt.SystemColor.control);
    	}
    	return jList;
    }
    
    /**
     * Set the various distributions to be displayed in the pane.
     * @param distribution
     * @param confidence
     * @param pignistic
     * @param discount
     */
    public void setData(ArrayList distribution,ArrayList confidence,ArrayList pignistic,ArrayList discount)
    {
        this.pignistic = new ArrayList(pignistic);
        this.disribution = new ArrayList(distribution);
        this.confidence = new ArrayList(confidence);
        this.discount = (discount==null)? null:new ArrayList(discount);
        setPignitisc();
    }
    
    /**
     * Switch the display mode of the pane to the Confidence intervals 
     * ({@link #CONFIDENCE}).
     */
    private void setConfidence()
    {
        if (this.confidence==null) return;
        
        DefaultListModel model = new DefaultListModel();
        for (int i=0;i<this.confidence.size();i=i+3)
        {
            int level = ((Integer)this.confidence.get(i)).intValue();
            LevelSet lset = LevelSet.getSet(level);
            //if (lset==LevelSet.emptySet) continue;
            if (lset==LevelSet.theLot) continue;
            
            Double dc = (Double)this.confidence.get(i+1);
            Double dp = (Double)this.confidence.get(i+2);
            DistributionWrapper dw = new DistributionWrapper(CONFIDENCE,lset,dc.doubleValue(),dp.doubleValue());
            model.addElement(dw);
         }
        getJList().setModel(model);
    }
    
    private void setDistribution(String attribute)
    {
    	ArrayList list = null;
        if (MASSDISTRIB.equals(attribute) || EVIDENCE.equals(attribute))
        	list = this.disribution;
        else
        	list = this.discount;
    	
        if (list==null) return;
        
        DefaultListModel model = new DefaultListModel();
        for (int i=0;i<list.size();i=i+2)
        {
            
        	int level =0;
        	Object obj = list.get(i);
        	if (obj instanceof Number) {
				level = ((Number) obj).intValue();
			}
            //int level = ((Integer)list.get(i)).intValue();
                LevelSet lset = LevelSet.getSet(level);
                //if (lset==LevelSet.emptySet) continue;
                Double dr = new Double(-1) ;
                Double dc = (Double)list.get(i+1);
                DistributionWrapper dw = new DistributionWrapper(attribute,lset,dc.doubleValue(),dr.doubleValue());
                model.addElement(dw);
        }
        getJList().setModel(model);
    	
//        if (this.disribution==null) return;
//        
//        DefaultListModel model = new DefaultListModel();
//        for (int i=0;i<this.disribution.size();i=i+2)
//        {
//            
//            int level = ((Integer)this.disribution.get(i)).intValue();
//                LevelSet lset = LevelSet.getSet(level);
//                //if (lset==LevelSet.emptySet) continue;
//                Double dr = (discount==null)? new Double(-1) : (Double)this.discount.get(i+1);
//                Double dc = (Double)this.disribution.get(i+1);
//                DistributionWrapper dw = new DistributionWrapper(attribute,lset,dc.doubleValue(),dr.doubleValue());
//                model.addElement(dw);
//        }
//        getJList().setModel(model);
    }

    private void setPignitisc()
    {
        if (this.pignistic==null) return;
        
        DefaultListModel model = new DefaultListModel();
        LevelSet roman[]={
                LevelSet.I,
                LevelSet.II,
                LevelSet.III,
                LevelSet.IV};
        
        for (int i=0;i<this.pignistic.size();i=i+2)
        {
        	Object obj = this.pignistic.get(i);
        	int level = 1;
        	if (obj instanceof Number) {
				level = ((Number) obj).intValue();
			}
            //level = (Integer)this.pignistic.get(i);
            Double dc = (Double)this.pignistic.get(i+1);

            LevelSet str = roman[level-1];
            DistributionWrapper dw = new DistributionWrapper(PIGNISTIC,str,dc.doubleValue());
            model.addElement(dw);
        }
        getJList().setModel(model);
    }

    public void setAttribute(String attribute) 
    {
        this.attribute = attribute;
        
        if (CONFIDENCE.equals(attribute))
            setConfidence();
        else if (PIGNISTIC.equals(attribute))
            setPignitisc();
        else //if (DISTRIBUTION.equals(attribute) || EVIDENCE.equals(attribute) || DECAYED.equals(attribute))
            setDistribution(attribute);
        updateUI();
    }

    public String getAttribute() {
        return this.attribute;
    }
    
    public void setVisibleRowSize(int size)
    {
        this.rowSize = size;
    }
    public void setVisibleRowCount(int size)
    {
       getJList().setVisibleRowCount(size);
    }

    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals(OutputViewPanel.VIEW_SWAP))
        {
        }
    }
    
    public void addSwap(String key)
    {
    	swapList.add(key);
    }
    
    public void swapToNextView()
    {
        int index = swapList.indexOf(getAttribute());
        index++;
        if (index<0 || index>=swapList.size())
            index=0;
        setAttribute((String)swapList.get(index));
    }

    public void setSelectionListener(ListSelectionListener listen)
    {
        getJList().addListSelectionListener(listen);
    }

    
}
