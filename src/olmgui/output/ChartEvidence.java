package olmgui.output;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import olmgui.graph.ToulminNode;
import olmgui.utils.LevelSet;
import olmgui.utils.OutputViewPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;

import toulmin.Toulmin;
import toulmin.ToulminAttribute;
import toulmin.ToulminBacking;
import toulmin.ToulminWarrant;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonUI;
import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

import config.OLMQueryResult;
import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;
import dialogue.DialoguePlanner.DlgMoveAction;

public class ChartEvidence extends OutputViewPanel 
{
    /**
     * The possible display attribute of the Distribution Pane.
     */
    public final static String EVIDENCE = "EVIDENCE",           ///< Display the mass distribution (of an evidence).
                               DECAYED = "DECAYED",             ///< Display the decayed mass distribution (of an evidence).
                               ATTRIBUTE = "ATTRIBUTE";         ///< Display the attribute of the evidence 

    private ChartPanel jChartPanel = null;
    private JFreeChart jChart = null;  //  @jve:decl-index=0:
	private PropertySheetPanel propertySheetPanel = null;
	private JPanel jPanel = null;  //  @jve:decl-index=0:visual-constraint="58,232"
	private JButton jButton = null;  //  @jve:decl-index=0:visual-constraint="361,37"
	
	private ToulminWarrant warrant = null;

	static class NoRWProperty extends DefaultProperty 
	{
		public NoRWProperty() 
		{
			super();
			setEditable(true);
		}

		public void readFromObject(Object object){}
	    public void writeToObject(Object object) {}
	}
	
	public class IconCellRenderer extends DefaultCellRenderer
	{
		protected String convertToString(Object value)
		{
			if (value == null) return null;
			ToulminAttribute attr = (ToulminAttribute) value;
			String text =attr.getValueName();
			return text;
		}
		
		protected Icon convertToIcon(Object value)
		{
		    if (value == null) return null;

			ToulminAttribute attr = (ToulminAttribute) value;
			ArrayList icons = (ArrayList)attr.getRendererData();
			int mapping = attr.getMappingIndex();
			int max = attr.getMapping().size();
			ImageIcon icon = (ImageIcon)icons.get(0);
			ImageIcon mask = (ImageIcon)icons.get(1);
		    return new ColorIcon(icon,mask,mapping,max);
		}
		  
		private class ColorIcon implements Icon 
		{
			private ImageIcon icon = null;
			private ImageIcon mask = null;
			private int mapping = -1;
			private int max = -1;
			
			private int width = 0;
			private int height = 0;
			
		    public ColorIcon(ImageIcon icon, ImageIcon mask, int mapping, int max)
		    {
				super();
				this.icon = icon;
				this.mask = mask;
				this.mapping = mapping;
				this.max = max;
				height = Math.max(icon.getIconHeight(),mask.getIconHeight());
				width = Math.max(icon.getIconWidth(),mask.getIconWidth());
				width = (width+1)*max;
			}
		    
		    public int getIconHeight() {return height;}
		    public int getIconWidth() {return width;}
		    
		    public void paintIcon(Component c, Graphics g, int x, int y)
		    {
	            if (icon!=null)
	            {
	            	int width = icon.getIconWidth();
	            	for (int i=0;i<=mapping;i++)
	            		icon.paintIcon(c,g,(width+1)*i,0);
	            }
	            if (mask!=null)
	            {
	            	int width = mask.getIconWidth();
	                for (int i=mapping+1;i<max;i++)
	                    mask.paintIcon(c,g,(width+1)*i,0);
	            }
		    }
		}
	}

	public class MeterCellRenderer extends DefaultCellRenderer
	{
		protected String convertToString(Object value)
		{
			if (value == null) return null;
			ToulminAttribute attr = (ToulminAttribute) value;
			String text =attr.getValueName();
			return text;
		}
		
		protected Icon convertToIcon(Object value)
		{
		    if (value == null) return null;

			ToulminAttribute attr = (ToulminAttribute) value;
			Color colorMeter = (Color)attr.getRendererData();
			double valMeter = ((Number)attr.getValue()).doubleValue();
			if (valMeter>1) valMeter = valMeter/100.;
		    return new ColorIcon(colorMeter,valMeter);
		}
		
		private class ColorIcon implements Icon 
		{
			private Color	colorMeter = null;
			private double  valMeter = -1D;
			
		    public ColorIcon(Color colorMeter, double valMeter)
		    {
				super();
				this.colorMeter = colorMeter;
				this.valMeter = valMeter;
			}
		    
		    public int getIconHeight() {return 10;}
		    public int getIconWidth() {return 50;}
		    
		    public void paintIcon(Component c, Graphics g, int x, int y)
		    {
		    	Color oldColor = g.getColor();
		        g.setColor(Color.WHITE);
		        g.fillRect(x, y, getIconWidth(), getIconHeight());
		        if (colorMeter != null) 
		        {
			          g.setColor(colorMeter);
			          g.fillRect(x, y, (int)(getIconWidth()*valMeter), getIconHeight());
		        }

		        g.setColor(UIManager.getColor("controlDkShadow"));
		        g.drawRect(x, y, getIconWidth(), getIconHeight());

		        g.setColor(oldColor);
		    }
		}
		
	}
	
	public class DescriptorPropertyEditor extends AbstractPropertyEditor {

		  public DescriptorPropertyEditor() {
		    editor = new JComboBox();
		    ((JComboBox)editor).setOpaque(false);
		    ((JComboBox)editor).addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
//		        firePropertyChange(
//		          ((JComboBox)editor).isSelected() ? Boolean.FALSE : Boolean.TRUE,
//		          ((JComboBox)editor).isSelected() ? Boolean.TRUE : Boolean.FALSE);
		        ((JComboBox)editor).transferFocus();
		      }
		    });
		  }

		  public Object getValue() {
		    //return ((JCheckBox)editor).isSelected() ? Boolean.TRUE : Boolean.FALSE;
			  return null;
		  }

		  public void setValue(Object value)
		  {
			  ToulminAttribute attr = (ToulminAttribute)value;
			  if (attr!=null)
			  {
				  Vector vec = (Vector)attr.getValue();
				  for (int i=0;i<vec.size();i++)
					  ((JComboBox)editor).addItem(vec.get(i).toString());
			  }
		  }

		}

	
	public class DescriptorCellRenderer extends JComboBox
								        implements TableCellRenderer, ListCellRenderer 
    {
		  public Component getTableCellRendererComponent(
				    JTable table,Object value,
				    boolean isSelected,boolean hasFocus,
				    int row,int column)
		  {
			    
			  if (isSelected)
			  {
				  setBackground(table.getSelectionBackground());
				  setForeground(table.getSelectionForeground());
				  setBorder(BorderFactory.createLineBorder(UIManager.getColor("controlDkShadow"),1));			  
			  } 
			  else 
			  {
				  setBackground(table.getBackground());
				  setForeground(table.getForeground());
				  setBorder(null);			  
			  }
			  this.setEnabled(isSelected);
			  ToulminAttribute attr = (ToulminAttribute)value;
			  if (attr!=null)
			  {
				  Vector vec = (Vector)attr.getValue();
				  for (int i=0;i<vec.size();i++)
					  this.addItem(vec.get(i).toString());
			  }
			  return this;
		  }
				  
		  public Component getListCellRendererComponent(
				    JList list,Object value,
				    int index,boolean isSelected,boolean cellHasFocus)
		  {
			  if (isSelected)
			  {
				  setBackground(list.getSelectionBackground());
			      setForeground(list.getSelectionForeground());
			  } else 
			  {
			      setBackground(list.getBackground());
			      setForeground(list.getForeground());
			  }
			  this.setEnabled(isSelected);
			  ToulminAttribute attr = (ToulminAttribute)value;
			  if (attr!=null)
			  {
				  Vector vec = (Vector)attr.getValue();
				  for (int i=0;i<vec.size();i++)
					  this.addItem(vec.get(i).toString());
			  }
			  return this;    
		  }	
	}

	
    public ChartEvidence()
    {
        super();
        initialize();
    }
	
    private void initialize()
    {
        this.setSize(300, 200);
        add(getJPanel(),java.awt.BorderLayout.CENTER);
        getJMorePanel().add(getJButton());
    }
    /**
     * This method initializes jFreeChart   
     *  
     * @return org.jfree.chart.JFreeChart   
     */
    protected ChartPanel getJChartPanel() {
        if (jChartPanel == null) {
            jChartPanel = new ChartPanel(
                    getJFreeChart(),
                    false,
                    false,
                    false,
                    false,
                    true
                    );
            jChartPanel.setDomainZoomable(false);
            jChartPanel.setName("jChartPanel");
            jChartPanel.setRangeZoomable(false);
        }
        
        return jChartPanel;
    }

    
    protected JFreeChart getJFreeChart()
    {
        if (jChart==null)
        {
            jChart = new JFreeChart(
                    "Evidence", 
                    JFreeChart.DEFAULT_TITLE_FONT, 
                    getPlot(), 
                    true);
            //jChart.setBackgroundPaint(Color.white);
            jChart.getTitle().setExpandToFitSpace(true);
        }
        return jChart;
    }
    
    protected Plot getPlot()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CategoryAxis categoryAxis = new CategoryAxis3D("category");
        categoryAxis.setVisible(false);
        NumberAxis valueAxis = new NumberAxis3D("value axis");
        valueAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        BarRenderer renderer = new BarRenderer3D();
        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
        plot.setForegroundAlpha(0.9F);
        plot.setNoDataMessage("No data to display");

    	return plot;
    }

	/**
	 * This method initializes propertySheetPanel	
	 * 	
	 * @return com.l2fprod.common.propertysheet.PropertySheetPanel	
	 */
	private PropertySheetPanel getPropertySheetPanel() {
		if (propertySheetPanel == null) {
			propertySheetPanel = new PropertySheetPanel();
			propertySheetPanel.setMode( PropertySheet.VIEW_AS_FLAT_LIST );
			//propertySheetPanel.setProperties( new Property[] { new ColorProperty(), level0, root } );
			propertySheetPanel.setDescriptionVisible( true );
			propertySheetPanel.setSortingCategories( true );
			propertySheetPanel.setSortingProperties( true );
		}
		return propertySheetPanel;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new CardLayout());
			jPanel.setSize(new Dimension(232, 240));
			jPanel.add(getPropertySheetPanel(), ChartEvidence.ATTRIBUTE);
			jPanel.add(getJChartPanel(), ChartEvidence.EVIDENCE);
		}
		return jPanel;
	}

	public void setSelectedNode(ToulminNode node) 
	{
		ToulminBacking backing = (ToulminBacking)node.getData();
        warrant = backing.getWarrant();
        swapToNextView();
	}

	public void setToulmin(Toulmin toulmin) {
		// TODO Auto-generated method stub
		
	}
	
	public void swapToNextView()
	{
		String view = getCurrentView();
		if (view!=null)
		{
	        CardLayout cl2 = (CardLayout)(getJPanel().getLayout());
	        cl2.show(getJPanel(),view);
		}
		if (warrant==null) return;
        Plot plot = getJFreeChart().getPlot();
        if (plot instanceof CategoryPlot) 
        {
            DefaultCategoryDataset dataset = getDataset(getCurrentView());
            CategoryPlot CatPLot = (CategoryPlot) plot;

            CatPLot.setDataset(dataset);
            getJChartPanel().repaint();
        }
        setAttributeList();
	}
	
	public void setAttributeList()
	{
        String backingItemId = null;
        Action act = getJButton().getAction();
        if (act!=null)
        {
            act.putValue(Action.ACTION_COMMAND_KEY, null);
        }
        getJButton().setEnabled(false);

		ToulminBacking back = warrant.getBacking();
		HashMap map = back.getDataTypes();
		Set keys = map.keySet();
		Iterator iter = keys.iterator();
		ArrayList arr = new ArrayList();
		PropertyRendererRegistry reg = (PropertyRendererRegistry) getPropertySheetPanel().getRendererFactory();
		PropertyEditorRegistry edit = (PropertyEditorRegistry) getPropertySheetPanel().getEditorFactory();
		while (iter.hasNext())
		{
			String key = (String)iter.next();
			ToulminAttribute attr = (ToulminAttribute)map.get(key);
			String render = attr.getRendererType();

			// do not display attribute without renderer
			if (render.equals(ToulminAttribute.RENDERER_NONE))
				continue;
			
			String hint = attr.getAttributeDescription();
			String name = attr.getAttributeName();
			String value = attr.getValueName();
			String cat = attr.getCategory();
			
			NoRWProperty prop = new NoRWProperty();
			prop.setCategory(cat);
			prop.setDisplayName(name);
			prop.setShortDescription(hint);
			
            // Extracting the MBase ID from the event
            if (OLMQueryResult.EVIDENCE_ITEM.equals(key)) 
            {
                //backingItemId = value.getValue().toString();
                backingItemId = "/ActiveMath2/search/show.cmd?id=" + attr.getValue().toString();
            }
			if (render.equals(ToulminAttribute.RENDERER_METER))
			{
				//JRendererMeter new_name = (JRendererMeter) rf;
				reg.registerRenderer(prop, new MeterCellRenderer());
				prop.setValue(attr);
			}
			else if (render.equals(ToulminAttribute.RENDERER_ICON))
			{
				//JRendererMeter new_name = (JRendererMeter) rf;
				reg.registerRenderer(prop, new IconCellRenderer());
				prop.setValue(attr);
			}
			else if (render.equals(ToulminAttribute.RENDERER_LIST))
			{
				//JRendererMeter new_name = (JRendererMeter) rf;
				reg.registerRenderer(prop, new DescriptorCellRenderer());
				edit.registerEditor(prop, new DescriptorPropertyEditor());
				prop.setValue(attr);
			}
			else
				prop.setValue(value);
			arr.add(prop);

		}
		Property[]dd = new Property[0];
		getPropertySheetPanel().setProperties((Property[])arr.toArray(dd));
        getJButton().setEnabled(backingItemId!=null);
        if (act!=null)
        {
            act.putValue(Action.ACTION_COMMAND_KEY, backingItemId);
        }
	}
	
	public DefaultCategoryDataset getDataset(String view)
	{
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (warrant==null) return dataset;
        
        ArrayList distrib = null;
        if (DECAYED.equals(view))
        	distrib = warrant.getData().getDiscount();
        else
        	distrib = warrant.getData().getDistribution();
        
        for (int i=0;i<distrib.size();i=i+2)
        {
        	Object obj = distrib.get(i);
        	int level =0;
        	if (obj instanceof Number)
        	{
				level = ((Number) obj).intValue();
			}
            LevelSet lset = LevelSet.getSet(level);

            Double dc = (Double)distrib.get(i+1);
            dataset.addValue(dc.doubleValue(),"MASSDISTRIB", lset);
        }
        return dataset;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setUI(new BlueishButtonUI());
			jButton.setIcon(new ImageIcon(getClass().getResource("/res/exercise_s.gif")));
			jButton.setActionCommand("");

		}
		return jButton;
	}

	public void setListeners(DialoguePlanner planner) 
	{
        if (planner==null) return;
        
        DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.CONTEXT);
        getJButton().addMouseListener((MouseListener)act);
        getJButton().addActionListener(planner.setShowItemListener());
        getJButton().setAction(act);
		
	}


}
