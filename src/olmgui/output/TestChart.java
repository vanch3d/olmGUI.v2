package olmgui.output;

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

import toulmin.Toulmin;
import dialogue.DialoguePlanner;

public class TestChart extends OLMChartPanel {

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
	
	protected Plot getPlot() {
    	SampleXYZDataset dataset = new SampleXYZDataset();
    	String diffs[]={"very_easy","easy","medium","difficult","very_difficult"};
    	String comps[]={"elementary","simple_conceptual","multi_step","complex"};

        NumberAxis xAxis = new NumberAxis("difficulty");
        xAxis.setRange(-.5D, 4.5D);
        xAxis.setTickUnit(new NumberTickUnit(1D));
        xAxis.setNumberFormatOverride(new CategoryFormat(diffs));

        NumberAxis yAxis = new NumberAxis("competency");
        yAxis.setRange(-.5D, 3.5D);
        yAxis.setTickUnit(new NumberTickUnit(1D));
        yAxis.setNumberFormatOverride(new CategoryFormat(comps));
        yAxis.setVerticalTickLabels(true);

		dataset.addValue("serie 1","difficult", "complex", new Double(0.40));
		dataset.addValue("serie 1","easy", "simple_conceptual", new Double(0.3));
		dataset.addValue("serie 2","easy", "simple_conceptual", new Double(0.8));
		dataset.addValue("serie 2","easy", "complex", new Double(0.51));

        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);
        plot.setForegroundAlpha(0.65F);

        XYItemRenderer renderer = new XYBubbleRenderer(
            XYBubbleRenderer.SCALE_ON_RANGE_AXIS);	
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

	public void setToulmin(Toulmin toulmin) {
		// TODO Auto-generated method stub

	}

	public void swapToNextView() {
		// TODO Auto-generated method stub

	}

}
