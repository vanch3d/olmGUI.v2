package olmgui.output;

import java.awt.Color;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.util.Rotation;

import config.OLMQueryResult;

import dialogue.DialoguePlanner;

import toulmin.Toulmin;
import toulmin.ToulminBacking;
import toulmin.ToulminList;
import toulmin.ToulminSubClaim;
import toulmin.ToulminWarrant;

public class ChartPartition extends OLMChartPanel
{
    /**
     * The possible display attribute of the Distribution Pane.
     */
    public final static String PARTITION = "PARTITION",           ///< Display the mass distribution (of an evidence).
                               SCATTER = "SCATTER";             ///< Display the decayed mass distribution (of an evidence).
	private Toulmin toulmin = null;

	private int indexexpl = -1;
	
	public class SampleXYZDataset extends AbstractXYZDataset
	implements XYZDataset
	{
		ArrayList array = new ArrayList();
		ArrayList series = new ArrayList();

		public SampleXYZDataset()
		{
		}

		public int getSeriesCount()
		{
			return series.size();
		}

		public Comparable getSeriesKey(int i)
		{
			return (String)series.get(i);
		}

		public int getItemCount(int i)
		{
			ArrayList serie = (ArrayList)array.get(i);
			return serie.size();
		}

		public Number getX(int i, int j)
		{
			ArrayList serie = (ArrayList)array.get(i);
			if (serie.get(j) instanceof ArrayList) {
				ArrayList val = (ArrayList) serie.get(j);
				return (Number)val.get(0);
			}
			else return null;
		}

		public Number getY(int i, int j)
		{
			ArrayList serie = (ArrayList)array.get(i);
			if (serie.get(j) instanceof ArrayList) {
				ArrayList val = (ArrayList) serie.get(j);
				return (Number)val.get(1);
			}
			else return null;
		}

		public Number getZ(int i, int j)
		{
			ArrayList serie = (ArrayList)array.get(i);
			if (serie.get(j) instanceof ArrayList) {
				ArrayList val = (ArrayList) serie.get(j);
				return (Number)val.get(2);
			}
			else return null;
		}

		public void addValue(String serie,Object diff, Object comp,Number perf)
		{
			String diffs[]={"very_easy","easy","medium","difficult","very_difficult"};
			String comps[]={"elementary","simple_conceptual","multi_step","complex"};

			List dd = Arrays.asList(diffs);
			List cc = Arrays.asList(comps);
			int ndiff = dd.indexOf(diff);
			int  ncpt = cc.indexOf(comp);

			if(ndiff==-1 || ncpt==-1) return;
			ArrayList newserie= null;
			int nb = series.indexOf(serie);
			if (nb==-1)
			{
				series.add(serie);
				newserie= new ArrayList();
			}
			else
			{
				newserie = (ArrayList)array.get(nb);
			}

			ArrayList val = new ArrayList();
			val.add(new Integer(ndiff));
			val.add(new Integer(ncpt));
			val.add(perf);
			newserie.add(val);
			if (nb==-1)
				array.add(newserie);
			else
				array.set(nb, newserie);
		}


	}

	public class CategoryFormat extends NumberFormat
	{
		List category = null;

		public CategoryFormat(String items[]) {
			super();
			category = Arrays.asList(items);
		}

		public StringBuffer format(double arg0, StringBuffer arg1, FieldPosition arg2) {
			int index = (int)arg0;
			if (index>=0 && index<category.size())
				return new StringBuffer((String)category.get(index));
			else
				return new StringBuffer("");
		}

		public StringBuffer format(long arg0, StringBuffer arg1, FieldPosition arg2) {
			int index = (int)arg0;
			if (index>=0 && index<category.size())
				return new StringBuffer((String)category.get(index));
			else
				return new StringBuffer("");
		}

		public Number parse(String arg0, ParsePosition arg1) {
//			TODO Auto-generated method stub
			return null;
		}

	}
	
	public ChartPartition() 
	{
		super("Evidence Partition");
		// TODO Auto-generated constructor stub
	}

	protected Plot getPlot() 
	{
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        PiePlot pieplot3d = new PiePlot(dataset);
		pieplot3d.setStartAngle(290D);
		pieplot3d.setCircular(true);
		pieplot3d.setDirection(Rotation.CLOCKWISE);
		pieplot3d.setForegroundAlpha(0.5F);
		//pieplot3d.setExplodePercent(1, 0.5D);
		pieplot3d.setNoDataMessage("No data to display");
		//pieplot3d.setLabelGenerator(null);
		pieplot3d.setInteriorGap(0.1);
		pieplot3d.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0} = {2}"));
		return pieplot3d;  	
	}

	public void setSelectedNode(ToulminNode node)
	{
		if (node==null) return;
		// TODO Auto-generated method stub
        Plot plot = getJChartPanel().getChart().getPlot();
        
        if (plot instanceof PiePlot && node.getData() instanceof ToulminSubClaim)
        {
        	PiePlot pieplot = (PiePlot) plot;
            ToulminSubClaim claim = (ToulminSubClaim) node.getData();
            String val = claim.getValue();
        	int index = pieplot.getDataset().getIndex(Messages.getString(val));
        	if (indexexpl!=-1)
        	{
        		pieplot.setExplodePercent(indexexpl, 0.0D);
        	}
        	if (index!=-1)
        	{
        		pieplot.setExplodePercent(index, 0.5D);
        		indexexpl = index;
        	}
            //getJChartPanel().repaint();
        }
	}

	public void setToulmin(Toulmin toulmin)
	{
        if (toulmin==null) return;
        this.toulmin = toulmin;
        swapToNextView();

//        Plot plot = getJFreeChart().getPlot();
//        if (plot instanceof PiePlot)
//        {
//            ToulminList list = toulmin.getList();
//            if (list.getPartition().equals(ToulminList.CLUSTER_NONE)) return;
//
//            DefaultPieDataset defaultpiedataset = new DefaultPieDataset();        
//            double totalValue = 0;
//            for (int i=0;i<list.size();i++)
//            {
//                Toulmin sub = (Toulmin)list.get(i);
//                ToulminList sublist = sub.getList();
//                int nb = sublist.size();
//                totalValue+=nb;
//                //values.add(new Double(nb));
//                ToulminSubClaim claim = (ToulminSubClaim)sub.getClaim();
//                //labels.add(claim.getValue());
//                double tt = 0;
//                for (int j=0;j<sublist.size();j++)
//                {
//                    ToulminWarrant obj = (ToulminWarrant)sublist.get(j);
//                    int nb2= obj.getRelevance();
//                    tt += (nb2/100.);
//                }
//                defaultpiedataset.setValue(Messages.getString(claim.getValue()), new Double(tt));
//                //weight.add(new Double(tt));
//                //totalWeight +=nb*tt;
//            }
//            PiePlot pieplot = (PiePlot) plot;
//            pieplot.setDataset(defaultpiedataset);
//            //getJChartPanel().repaint();
//            
//        }		
	}

	public void swapToNextView()
	{
		String view = getCurrentView();
		if (SCATTER.equals(view))
		{
	    	String diffs[]={"very_easy","easy","medium","difficult","very_difficult"};
	    	String comps[]={"elementary","simple_conceptual","multi_step","complex"};

	    	SampleXYZDataset dataset = new SampleXYZDataset();

	        NumberAxis xAxis = new NumberAxis("difficulty");
	        xAxis.setRange(-.5D, 4.5D);
	        xAxis.setTickUnit(new NumberTickUnit(1D));
	        xAxis.setNumberFormatOverride(new CategoryFormat(diffs));

	        NumberAxis yAxis = new NumberAxis("competency");
	        yAxis.setRange(-.5D, 3.5D);
	        yAxis.setTickUnit(new NumberTickUnit(1D));
	        yAxis.setNumberFormatOverride(new CategoryFormat(comps));
	        yAxis.setVerticalTickLabels(true);

	        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);
	        plot.setForegroundAlpha(0.65F);

	        XYItemRenderer renderer = new XYBubbleRenderer(
	            XYBubbleRenderer.SCALE_ON_RANGE_AXIS);
	        renderer.setSeriesPaint(0, Color.red);
	        renderer.setSeriesPaint(1, Color.blue);
	        renderer.setSeriesPaint(2, Color.green);
	        renderer.setBaseToolTipGenerator(new StandardXYZToolTipGenerator(
	        		"Performance: {3}",
	        		NumberFormat.getIntegerInstance(),
	        		NumberFormat.getIntegerInstance(),
	        		NumberFormat.getPercentInstance()));
	        plot.setRenderer(renderer);			
            ToulminList list = toulmin.getList();
            if (list.getPartition().equals(ToulminList.CLUSTER_NONE)) return;

            String firstserie = null;
            for (int i=0;i<list.size();i++)
            {
                Toulmin sub = (Toulmin)list.get(i);
                ToulminList sublist = sub.getList();
                ToulminSubClaim claim = (ToulminSubClaim)sub.getClaim();
                if (firstserie==null)
                	firstserie = claim.getValue();
                for (int j=0;j<sublist.size();j++)
                {
                    ToulminWarrant obj = (ToulminWarrant)sublist.get(j);
                    ToulminBacking bac = obj.getBacking();
                    Object diff = bac.getValue(OLMQueryResult.EVIDENCE_DIFF);
                    Object perf = bac.getValue(OLMQueryResult.EVIDENCE_PERF);
                    Object cpt = bac.getValue(OLMQueryResult.EVIDENCE_COMPLVL);
                    if (perf instanceof Number && diff!=null && cpt != null) 
                    {
						Number perf2 = (Number) perf;	
						dataset.addValue(Messages.getString(claim.getValue()),diff, cpt, perf2);
					}
                }

            }
            plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
            plot.setDataset(dataset);
            getJChartPanel().setChart(new JFreeChart(
                    "", 
                    JFreeChart.DEFAULT_TITLE_FONT, 
                    plot, 
                    true));
		}
		else //if (PARTITION.equals(view))
		{
	        DefaultPieDataset dataset = new DefaultPieDataset();
	        
	        PiePlot pieplot3d = new PiePlot(dataset);
			pieplot3d.setStartAngle(290D);
			pieplot3d.setCircular(false);
			pieplot3d.setDirection(Rotation.CLOCKWISE);
			pieplot3d.setForegroundAlpha(0.5F);
			//pieplot3d.setExplodePercent(1, 0.5D);
			pieplot3d.setNoDataMessage("No data to display");
			pieplot3d.setLabelGenerator(null);
			pieplot3d.setInteriorGap(0.05);
			//pieplot3d.setLabelGap(0.02);
			//pieplot3d.setMaximumLabelWidth(0.1);
			//pieplot3d.setLabelLinkMargin(0.02);
			pieplot3d.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0} = {2}"));
			pieplot3d.setSectionOutlinePaint(0, Color.red);
			pieplot3d.setSectionOutlinePaint(1, Color.blue);
			pieplot3d.setSectionOutlinePaint(2, Color.green);

            ToulminList list = toulmin.getList();

            if (list.getPartition().equals(ToulminList.CLUSTER_NONE)) return;

            //DefaultPieDataset defaultpiedataset = new DefaultPieDataset();        
            double totalValue = 0;
            for (int i=0;i<list.size();i++)
            {
                Toulmin sub = (Toulmin)list.get(i);
                ToulminList sublist = sub.getList();
                int nb = sublist.size();
                totalValue+=nb;
                //values.add(new Double(nb));
                ToulminSubClaim claim = (ToulminSubClaim)sub.getClaim();
                //labels.add(claim.getValue());
                double tt = 0;
                for (int j=0;j<sublist.size();j++)
                {
                    ToulminWarrant obj = (ToulminWarrant)sublist.get(j);
                    int nb2= obj.getRelevance();
                    tt += (nb2/100.);
                }
                dataset.setValue(Messages.getString(claim.getValue()), new Double(tt));
            }		
            pieplot3d.setDataset(dataset);
            getJChartPanel().setChart(new JFreeChart(
                    "", 
                    JFreeChart.DEFAULT_TITLE_FONT, 
                    pieplot3d, 
                    true));
        }
	}

	public void setListeners(DialoguePlanner planner)
	{
		// TODO Auto-generated method stub
		
	}


}
