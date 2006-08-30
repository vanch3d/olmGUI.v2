package olmgui.output;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import olmgui.utils.Gradient;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;

import toulmin.Toulmin;
import dialogue.DialoguePlanner;

public class ChartHistory extends OLMChartPanel
{
	private CategoryPlot upperPlot ;
    private CategoryPlot lowerPlot ;
    private CombinedDomainCategoryPlot theplot;


    
    public ChartHistory() 
    {
		super();
	}


	/* (non-Javadoc)
	 * @see olmgui.output.OLMChartPanel#getPlot()
	 */
	protected Plot getPlot()
	{
		if (theplot==null)
		{
			CategoryAxis categoryAxis = new CategoryAxis("Evidence");
			categoryAxis.setLowerMargin(0.);
			categoryAxis.setUpperMargin(0.);
			categoryAxis.setCategoryMargin(0.05);
			theplot = new CombinedDomainCategoryPlot(categoryAxis)
	        {
	            /* (non-Javadoc)
	             * @see org.jfree.chart.plot.CombinedDomainCategoryPlot#handleClick(int, int, org.jfree.chart.plot.PlotRenderingInfo)
	             */
	            public void handleClick(int x, int y, PlotRenderingInfo info) 
	            {
	            	super.handleClick(x, y, info);
	            }
	
	        };
	        theplot.setForegroundAlpha(0.9F);
	        theplot.setNoDataMessage("No data to display");
	        theplot.setOrientation(PlotOrientation.VERTICAL);
	        theplot.add(getUpperPlot(), 1);
	        theplot.add(getLowerPlot(), 2);
		}
		return theplot;
	}
	

	protected CategoryPlot getUpperPlot()
	{
		if (upperPlot==null)
		{
			CategoryDataset dataset = new DefaultCategoryDataset();
			//CategoryDataset dataset = new DefaultStatisticalCategoryDataset();
			
			//CategoryAxis categoryAxis = new CategoryAxis("Evidence");
			NumberAxis valueAxis = new NumberAxis("summary");
	        valueAxis.setRange(0D, 1D);
	        valueAxis.setTickUnit(new NumberTickUnit(1/3D));
	        valueAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
            //StatisticalLineAndShapeRenderer renderer = new StatisticalLineAndShapeRenderer();
	        StackedBarRenderer renderer = new StackedBarRenderer()
	        {
	            /* (non-Javadoc)
	             * @see org.jfree.chart.renderer.category.BarRenderer#getLegendItem(int, int)
	             */
	            public LegendItem getLegendItem(int datasetIndex, int series)
	            {
	            	return null;
	            }

	        };
	        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
	        upperPlot = new CategoryPlot(dataset, null, valueAxis, renderer);
	        upperPlot.setForegroundAlpha(0.7F);
	        upperPlot.setNoDataMessage("No data to display");
	        upperPlot.setOrientation(PlotOrientation.VERTICAL);
		}
		return upperPlot;
	}

	protected CategoryPlot getLowerPlot()
	{
		if (lowerPlot==null)
		{
			CategoryDataset dataset = new DefaultCategoryDataset();
			
			//CategoryAxis categoryAxis = new CategoryAxis("Evidence");
			NumberAxis valueAxis = new NumberAxis("pignistic");
	        valueAxis.setRange(0D, 1D);
	        valueAxis.setTickUnit(new NumberTickUnit(0.1D));
	        valueAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
	        //CategoryItemRenderer renderer = new StackedAreaRenderer();
	        StackedBarRenderer renderer = new StackedBarRenderer();
	        renderer.setMaximumBarWidth(0.8D);
	        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
	        lowerPlot = new CategoryPlot(dataset, null, valueAxis, renderer);
	        lowerPlot.setForegroundAlpha(0.7F);
	        lowerPlot.setNoDataMessage("No data to display");
	        lowerPlot.setOrientation(PlotOrientation.VERTICAL);
		}
		return lowerPlot;
	}

	public void setListeners(DialoguePlanner planner) {
		// TODO Auto-generated method stub

	}

	public void setSelectedNode(ToulminNode node) {
		// TODO Auto-generated method stub

	}

	public void setToulmin(Toulmin toulmin)
	{
		if (toulmin==null) return;
		Random rand = new Random();
		
		Gradient gradient = new Gradient();
		gradient.addPoint(Color.RED);
		gradient.addPoint(new Color(255,150,1));
		gradient.addPoint(Color.YELLOW);
		gradient.addPoint(new Color(79,255,1));
		gradient.addPoint(new Color(1,187,1));
		gradient.createGradient(100);

        //DefaultStatisticalCategoryDataset upperDataset = new DefaultStatisticalCategoryDataset();
        DefaultCategoryDataset upperDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset lowerDataset = new DefaultCategoryDataset();
        //ArrayList distrib = toulmin.getData().getPignistic();
        ArrayList history = toulmin.getData().getHistory();
        if (history==null)
        {
        	history = new ArrayList();
        	int nb = toulmin.getDepth();
        	for (int i=0;i<nb;i++)
        	{
        		history.add(new Double(rand.nextDouble()));
        		history.add(new Double(rand.nextInt(20)/100D));
        		int total = 100;
    	        for (int j=0;j<4;j++)
    	        {
    	            int newnb = (j==3)? total : Math.min(total/2,rand.nextInt(total+1));
    	            total = total-newnb;
    	            Double dc = new Double(newnb/100.);
    	            history.add(dc);
    	        }
        	}
        	
        }
        for (int j=0;j<history.size();j=j+6)
        {	
            //int nb = 100;
        
            // get the history valeus
            Double summary = (Double)history.get(j);
            Double uncertainty = (Double)history.get(j+1);
            Double level[] = 
                {   (Double)history.get(j+2),
                    (Double)history.get(j+3),
                    (Double)history.get(j+4),
                    (Double)history.get(j+5)
                };
            
            // add the summary in the upper graph
            //upperDataset.add(summary.doubleValue(),Math.pow(uncertainty.doubleValue(),2),"summary", new Integer(j/6));
            {
                //LevelSet lset = LevelSet.getSet(k+1);
                String label = Messages.getJudgementOn(toulmin.getBeliefDesc(),summary.doubleValue());
                upperDataset.addValue(summary.doubleValue(),label, new Integer(j/6));
               
            }

//            ToulminWarrant war = toulmin.getEvidenceAt(j/6);
//            if (war!=null)
//            {
//                int rel = war.getRelevance();
//                upperDataset.add(rel/100D,0,"impact", new Integer(j/6));
//                
//            }
            // add the pignistic in the lower graph
            for (int k=0;k<level.length;k++)
            {
                //LevelSet lset = LevelSet.getSet(k+1);
                String label = Messages.getJudgementOn(toulmin.getBeliefDesc(),k+1);
                lowerDataset.addValue(level[k].doubleValue(),label, new Integer(j/6));
               
            }

        }
        getUpperPlot().setDataset(upperDataset);
        getLowerPlot().setDataset(lowerDataset);
        getUpperPlot().getRenderer().setSeriesPaint(0, gradient.getColour(0));
        getUpperPlot().getRenderer().setSeriesPaint(1, gradient.getColour(33));
        getUpperPlot().getRenderer().setSeriesPaint(2, gradient.getColour(66));
        getUpperPlot().getRenderer().setSeriesPaint(3, gradient.getColour(99));
        getLowerPlot().getRenderer().setSeriesPaint(0, gradient.getColour(0));
        getLowerPlot().getRenderer().setSeriesPaint(1, gradient.getColour(33));
        getLowerPlot().getRenderer().setSeriesPaint(2, gradient.getColour(66));
        getLowerPlot().getRenderer().setSeriesPaint(3, gradient.getColour(99));

        Plot plot = getJFreeChart().getPlot();
        plot.notifyListeners(new PlotChangeEvent(plot));
//        Plot plot = getJFreeChart().getPlot();
        CombinedDomainCategoryPlot CatPLot = (CombinedDomainCategoryPlot) plot;
//        List tt = CatPLot.getSubplots();
//        for (int i=0;i<tt.size();i++)
//        	CatPLot.remove((CategoryPlot)tt.get(i));
//        CatPLot.add(upperPlot, 1);
//        CatPLot.add(lowerPlot, 2);
        //getJChartPanel().repaint();

	}
	
	void setMarker(Comparable key)
	{
        CategoryMarker marker = new CategoryMarker(key);
        marker.setAlpha(0.8f);
        //marker.setDrawAsLine(true);
        marker.setStroke(new BasicStroke(
                4f,                  // Width of stroke
                BasicStroke.CAP_ROUND,  // End cap style
                BasicStroke.JOIN_MITER, // Join style
                15.0f,                  // Miter limit
                new float[] {2.f,2.f}, // Dash pattern
                5.0f));
        getLowerPlot().clearDomainMarkers();
        getUpperPlot().clearDomainMarkers();
        getLowerPlot().addDomainMarker(marker,Layer.BACKGROUND);
        getUpperPlot().addDomainMarker(marker,Layer.BACKGROUND);
		
	}

	public void swapToNextView() {
		// TODO Auto-generated method stub

	}


}
