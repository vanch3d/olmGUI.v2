package olmgui.output;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;

import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import olmgui.utils.Gradient;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import dialogue.DialoguePlanner;

import toulmin.Toulmin;
import toulmin.ToulminClaim;

public class ChartSummary extends OLMChartPanel 
{
	private Gradient gradient = null;
	
    public ChartSummary() {
		super("Summary Belief");
		gradient = new Gradient();
		gradient.addPoint(Color.RED);
		gradient.addPoint(new Color(255,150,1));
		gradient.addPoint(Color.YELLOW);
		gradient.addPoint(new Color(79,255,1));
		gradient.addPoint(new Color(1,187,1));
		gradient.createGradient(100);
	}
	
	protected Plot getPlot() 
	{
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CategoryAxis categoryAxis = new CategoryAxis3D("category");
        categoryAxis.setVisible(false);
        NumberAxis valueAxis = new NumberAxis3D("value axis");
        valueAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());
        valueAxis.setRange(0D, 1D);
        valueAxis.setTickUnit(new NumberTickUnit(1/3D));
//        BarRenderer renderer = new CustomRenderer(
//                new Paint[] {Color.red, Color.blue});
        
        BarRenderer renderer = new BarRenderer3D();
//        Gradient grd = new Gradient();
//        grd.addPoint(Color.RED);
//        grd.addPoint(new Color(255,150,1));
//        grd.addPoint(Color.YELLOW);
//        grd.addPoint(new Color(79,255,1));
//        grd.addPoint(new Color(1,187,1));
//        grd.createGradient(100);
//
        CategoryPlot plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer);
//        renderer.setSeriesPaint(0, grd.getColour(0));
//        renderer.setSeriesPaint(1, grd.getColour(33));
//        renderer.setSeriesPaint(2, grd.getColour(66));
//        renderer.setSeriesPaint(3, grd.getColour(99));
//
//        intervalmarker = new IntervalMarker(0D, 0.33D/2);
//        intervalmarker.setLabel("Level I");
//        intervalmarker.setLabelFont(new Font("SansSerif", 2, 11));
//        intervalmarker.setLabelAnchor(RectangleAnchor.CENTER);
//        intervalmarker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
//        clr = grd.getColour(0);
//        intervalmarker.setPaint(new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),128));
//        plot.addRangeMarker(intervalmarker, Layer.BACKGROUND);
//
//        intervalmarker = new IntervalMarker(0.33D+0.33D/2,.66D+ 0.33D/2);
//        intervalmarker.setLabel("Level I");
//        intervalmarker.setLabelFont(new Font("SansSerif", 2, 11));
//        intervalmarker.setLabelAnchor(RectangleAnchor.CENTER);
//        intervalmarker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
//        clr = grd.getColour(66);
//        intervalmarker.setPaint(new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),128));
//        plot.addRangeMarker(intervalmarker, Layer.BACKGROUND);
//
//        intervalmarker = new IntervalMarker(.66D+ 0.33D/2, .99D);
//        intervalmarker.setLabel("Level I");
//        intervalmarker.setLabelFont(new Font("SansSerif", 2, 11));
//        intervalmarker.setLabelAnchor(RectangleAnchor.CENTER);
//        intervalmarker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
//        clr = grd.getColour(99);
//        intervalmarker.setPaint(new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),128));
//        plot.addRangeMarker(intervalmarker, Layer.BACKGROUND);

        plot.setForegroundAlpha(0.9F);
        plot.setNoDataMessage("No data to display");
        plot.setOrientation(PlotOrientation.VERTICAL);
       
        return plot;
	}

	public void setSelectedNode(ToulminNode node)
	{
	}

	public void setToulmin(Toulmin toulmin)
	{
        if (toulmin==null) return;
        Plot plot = getJFreeChart().getPlot();
        if (plot instanceof CategoryPlot) 
        {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            ToulminClaim claim = toulmin.getClaim();
//            for (int i=1;i<=4;i++)
//            {
//                String label = Messages.getJudgementOn(toulmin.getBeliefDesc(),i);
//            	dataset.addValue(0.00,"Summay","Summay");
//            }
            String label = Messages.getJudgementOn(toulmin.getBeliefDesc(),claim.getClaimLevel());
            dataset.setValue(claim.getClaimSummary(),label, "Summary");

            CategoryPlot CatPLot = (CategoryPlot) plot;
            setLevelMarker(CatPLot,label,claim.getClaimLevel());
 
            CatPLot.getRenderer().setSeriesPaint(0, gradient.getColour((int)(100*claim.getClaimSummary())));
            CatPLot.setDataset(dataset);
            getJChartPanel().repaint();
        }

	}
	
	public void setLevelMarker(CategoryPlot plot,String lbl,int lvl)
	{
		double intervals[] = {0D,1/6D,3/6D,5/6D,1D};

        IntervalMarker intervalmarker = new IntervalMarker(intervals[lvl-1], intervals[lvl]);
        intervalmarker.setLabel(lbl);
        intervalmarker.setLabelFont(new Font("SansSerif", 2, 11));
        intervalmarker.setLabelAnchor(RectangleAnchor.CENTER);
        intervalmarker.setLabelTextAnchor(TextAnchor.TOP_CENTER);
        Color clr = gradient.getColour(Math.min(99, 100*(lvl-1)/3));
        intervalmarker.setPaint(new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),128));
        
        //plot.clearRangeMarkers();
        //plot.addRangeMarker(intervalmarker, Layer.BACKGROUND);
	
	}

	public void swapToNextView()
	{
	}

	public void setListeners(DialoguePlanner planner) {
		// TODO Auto-generated method stub
		
	}

}
