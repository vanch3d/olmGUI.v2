package olmgui.output;

import java.awt.Color;
import java.awt.Font;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import olmgui.utils.Gradient;
import olmgui.utils.LevelSet;

import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategorySeriesLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.jfree.util.SortOrder;

import toulmin.Toulmin;
import dialogue.DialoguePlanner;

public class ChartDistribution extends OLMChartPanel
{
    /**
     * The possible display attribute of the Distribution Pane.
     */
    public final static String CONFIDENCE = "CONFIDENCE",       ///< Display the confidence intervals.
                               MASSDISTRIB = "MASSDISTRIB",     ///< Display the mass distribution (of the belief).
                               EVIDENCE = "EVIDENCE",           ///< Display the mass distribution (of an evidence).
                               DECAYED = "DECAYED",             ///< Display the decayed mass distribution (of an evidence).
                               PIGNISTIC = "PIGNISTIC";         ///< Display the pignistic function.
    
	private Toulmin toulmin = null;
    
    static class DistributionLabelGenerator extends AbstractCategoryItemLabelGenerator
    implements CategoryItemLabelGenerator
    {

    	public String generateLabel(CategoryDataset dataset, int row, int column) 
        {
    		// TODO Auto-generated method stub
    		return "label";
    	}
        
       



        public DistributionLabelGenerator(Integer integer)
    	{
    		super("", NumberFormat.getInstance());
    		//formatter = NumberFormat.getPercentInstance();
    		//category = integer;
    	}


    }


    
	public ChartDistribution() {
		super("Mass Distribution");
		// TODO Auto-generated constructor stub
	}

	protected Plot getPlot() 
	{
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CategoryAxis categoryAxis = new CategoryAxis3D("category");
        categoryAxis.setVisible(false);
        NumberAxis valueAxis = new NumberAxis3D("value axis");
        valueAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        //valueAxis.setUpperMargin(1.0D);

//        BarRenderer renderer = new CustomRenderer(
//                new Paint[] {Color.red, Color.blue});
        BarRenderer renderer= new StackedBarRenderer3D();
        //StandardCategoryItemLabelGenerator standardcategoryitemlabelgenerator = new StandardCategoryItemLabelGenerator("{1}", NumberFormat.getInstance());
        //renderer.setItemLabelGenerator(standardcategoryitemlabelgenerator);
        renderer.setItemLabelGenerator(new DistributionLabelGenerator(null));
        renderer.setLegendItemLabelGenerator(new StandardCategorySeriesLabelGenerator()
        {

            public String generateLabel(CategoryDataset dataset, int series) {
                Comparable serie= dataset.getRowKey(series);
                //return super.generateLabel(dataset, series);
                return serie.toString();
            }
            
        });
        renderer.setItemLabelsVisible(true);
        renderer.setItemLabelAnchorOffset(15);
        ItemLabelPosition position1 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3, TextAnchor.BOTTOM_LEFT);
        renderer.setPositiveItemLabelPosition(position1);
        ItemLabelPosition position2 = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE9, TextAnchor.CENTER_RIGHT);
        renderer.setNegativeItemLabelPosition(position2);
        renderer.setItemLabelFont(new Font("SansSerif", Font.BOLD, 12));
        //renderer.setDrawBarOutline(true);
            
        renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator("",
                NumberFormat.getPercentInstance())
        {
            /* (non-Javadoc)
             * @see org.jfree.chart.labels.StandardCategoryToolTipGenerator#generateToolTip(org.jfree.data.category.CategoryDataset, int, int)
             */
            public String generateToolTip(CategoryDataset dataset, 
                    int row, int column) 
            {
                Object objr = dataset.getRowKey(row);
                LevelSet objc = (LevelSet)dataset.getColumnKey(column);
                Double dd = (Double)dataset.getValue(row,column);
                String txt = generateLabelString(dataset, row, column);
                
                String template = null;
                if (dd.doubleValue()<0.005)
                    template = Messages.getSafeString("MASS." + objr + ".description.empty");
                else
                    template = Messages.getSafeString("MASS." + objr + ".description." + objc.getId());
                if (template==null)
                    template = Messages.getString("MASS." + objr + ".description");
                
                Object[] items = createItemArray(dataset, row, column);
                Object tt = items[0];
                items[0]=items[2];
                items[2] = tt;
                txt = MessageFormat.format(template, items);
                
                return txt;
            }
        });
        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
        plot.setForegroundAlpha(0.9F);
        plot.setNoDataMessage("No data to display");
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        plot.setRowRenderingOrder(SortOrder.DESCENDING);
        plot.setColumnRenderingOrder(SortOrder.DESCENDING);
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);


        return plot;  
	}

	public void setSelectedNode(ToulminNode node) {
		// TODO Auto-generated method stub
		
	}
	
	public DefaultCategoryDataset getDataset(String view)
	{
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        ArrayList distrib = null;
        if (MASSDISTRIB.equals(view))
        	distrib = toulmin.getData().getDistribution();
        else
        	distrib = toulmin.getData().getPignistic();
        
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
            if (MASSDISTRIB.equals(view))
            {
            	String serie = "mass";
            	if (lset==LevelSet.emptySet)
            		serie = "conflict";
            	if (lset==LevelSet.theLot)
            		serie = "ignorance";
            	//dataset.addValue(dc.doubleValue(),view, lset);
            	dataset.addValue(dc.doubleValue(),serie, lset);
            	
            }
            else
            {
            	//dataset.addValue(dc.doubleValue(),view, lset);
                String label = Messages.getJudgementOn(toulmin.getBeliefDesc(),level);
            	dataset.addValue(dc.doubleValue(),lset, lset);
            }
        }
        return dataset;
	}
	
	public void setToulmin(Toulmin toulmin)
	{
        if (toulmin==null) return;
        this.toulmin = toulmin;
        swapToNextView();
	}

	public void swapToNextView()
	{
        Plot plot = getJFreeChart().getPlot();
        if (plot instanceof CategoryPlot) 
        {
            DefaultCategoryDataset dataset = getDataset(getCurrentView());
            CategoryPlot CatPLot = (CategoryPlot) plot;
            if (MASSDISTRIB.equals(getCurrentView()))
            {
            	CatPLot.getRenderer().setSeriesPaint(1, new Color(0,0,191));
        		CatPLot.getRenderer().setSeriesPaint(0, new Color(195,3,35));
        		CatPLot.getRenderer().setSeriesPaint(2, new Color(3,195,17));
            }
            else
            {
        		Gradient gradient = new Gradient();
        		gradient.addPoint(Color.RED);
        		gradient.addPoint(new Color(255,150,1));
        		gradient.addPoint(Color.YELLOW);
        		gradient.addPoint(new Color(79,255,1));
        		gradient.addPoint(new Color(1,187,1));
        		gradient.createGradient(100);

        		CatPLot.getRenderer().setSeriesPaint(0, gradient.getColour(0));
            	CatPLot.getRenderer().setSeriesPaint(1, gradient.getColour(33));
            	CatPLot.getRenderer().setSeriesPaint(2, gradient.getColour(66));
            	CatPLot.getRenderer().setSeriesPaint(3, gradient.getColour(99));
            	
            }

            CatPLot.setDataset(dataset);
            getJChartPanel().repaint();
        }
	}

	public void setListeners(DialoguePlanner planner) {
		// TODO Auto-generated method stub
		
	}

}
