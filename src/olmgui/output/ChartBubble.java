package olmgui.output;

import java.awt.Color;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import olmgui.graph.ToulminNode;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.XYZDataset;

import config.OLMQueryResult;

import toulmin.Toulmin;
import toulmin.ToulminBacking;
import toulmin.ToulminList;
import toulmin.ToulminWarrant;
import dialogue.DialoguePlanner;

public class ChartBubble extends OLMChartPanel {

	public class SampleXYZDataset extends AbstractXYZDataset
    							  implements XYZDataset
    {
		ArrayList array = new ArrayList();
		
	    public SampleXYZDataset()
	    {
	    }

	    public int getSeriesCount()
	    {
	        return 1;
	    }

	    public Comparable getSeriesKey(int i)
	    {
	        return "Series 1";
	    }

	    public int getItemCount(int i)
	    {
	        return array.size();
	    }

	    public Number getX(int i, int j)
	    {
	    	if (array.get(j) instanceof ArrayList) {
				ArrayList val = (ArrayList) array.get(j);
				return (Number)val.get(0);
			}
	    	else return null;
	    }

	    public Number getY(int i, int j)
	    {
	    	if (array.get(j) instanceof ArrayList) {
				ArrayList val = (ArrayList) array.get(j);
				return (Number)val.get(1);
			}
	    	else return null;
	    }

	    public Number getZ(int i, int j)
	    {
	    	if (array.get(j) instanceof ArrayList) {
				ArrayList val = (ArrayList) array.get(j);
				return (Number)val.get(2);
			}
	    	else return null;
	    }
	    
	    public void addValue(Object diff, Object comp,Number perf)
	    {
	    	String diffs[]={"very_easy","easy","medium","difficult","very_difficult"};
	    	String comps[]={"elementary","simple_conceptual","multi_step","complex"};
	    	
	    	List dd = Arrays.asList(diffs);
	    	List cc = Arrays.asList(comps);
	    	int ndiff = dd.indexOf(diff);
	    	int  ncpt = cc.indexOf(comp);
	    	
	    	if(ndiff==-1 || ncpt==-1) return;
	    	
	    	ArrayList val = new ArrayList();
	    	val.add(new Integer(ndiff));
	    	val.add(new Integer(ncpt));
	    	val.add(perf);
	    	array.add(val);
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
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public ChartBubble() 
	{
		super("Evidence Scattering");
		// TODO Auto-generated constructor stub
	}

	protected Plot getPlot() 
	{
    	String diffs[]={"very_easy","easy","medium","difficult","very_difficult"};
    	String comps[]={"elementary","simple_conceptual","multi_step","complex"};

    	XYZDataset dataset = new SampleXYZDataset();

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
        renderer.setSeriesPaint(0, Color.blue);
        renderer.setBaseToolTipGenerator(new StandardXYZToolTipGenerator(
        		"Performance: {3}",
        		NumberFormat.getIntegerInstance(),
        		NumberFormat.getIntegerInstance(),
        		NumberFormat.getPercentInstance()));
        plot.setRenderer(renderer);

        return plot;
	}

	public void setListeners(DialoguePlanner planner) {
		// TODO Auto-generated method stub

	}

	public void setSelectedNode(ToulminNode node) {
		// TODO Auto-generated method stub

	}

	public void setToulmin(Toulmin toulmin)
	{
        Plot plot = getJFreeChart().getPlot();
        if (plot instanceof XYPlot)
        {
            ToulminList list = toulmin.getList();
            if (list.getPartition().equals(ToulminList.CLUSTER_NONE)) return;

            SampleXYZDataset dataset = new SampleXYZDataset();
            for (int i=0;i<list.size();i++)
            {
                Toulmin sub = (Toulmin)list.get(i);
                ToulminList sublist = sub.getList();
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
						dataset.addValue(diff, cpt, perf2);
					}
                }

            }
    		XYPlot pieplot = (XYPlot) plot;
    		pieplot.setDataset(dataset);
        }
	}

	public void swapToNextView() {
		// TODO Auto-generated method stub

	}

}
