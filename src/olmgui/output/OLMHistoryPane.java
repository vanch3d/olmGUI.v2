package olmgui.output;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JSplitPane;

import olmgui.graph.ToulminNode;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;

import toulmin.Toulmin;
import toulmin.ToulminWarrant;
import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;
import dialogue.DialoguePlanner.DlgMoveAction;
import dialogue.DialoguePlanner.DlgSwapView;
import dialogue.DialoguePlanner.DlgTellMeMore;

public class OLMHistoryPane extends JSplitPane {

	private static final long serialVersionUID = 1L;
	private ChartHistory chartHistory = null;
	private ChartEvidence chartEvidence = null;
	private Toulmin toulmin = null;  //  @jve:decl-index=0:

    private class MyListener implements ChartMouseListener
    {

		public void chartMouseClicked(ChartMouseEvent event) {
			// TODO Auto-generated method stub
			JFreeChart chart = event.getChart();
			ChartEntity entity = event.getEntity();
			Object obj = event.getSource();
			MouseEvent evt = event.getTrigger();
			System.out.println(entity);
			if (entity instanceof CategoryItemEntity) {
				CategoryItemEntity cat = (CategoryItemEntity) entity;
				Object obj2 = cat.getCategory();
				if (obj2 instanceof Integer)
				{
					getChartHistory().setMarker((Integer)obj2);
					if (toulmin!=null)
					{
						ToulminWarrant war = toulmin.getEvidenceAt(((Integer)obj2).intValue());
						ToulminNode node = new ToulminNode("a","b","c");
						node.setData(war.getBacking());
						getChartEvidence().setSelectedNode(node);
					}
				}
				
			}
			
		}

		public void chartMouseMoved(ChartMouseEvent event) {
			// TODO Auto-generated method stub
			
		}
    	
    }
	/**
	 * This is the default constructor
	 */
	public OLMHistoryPane() {
		super(HORIZONTAL_SPLIT,true,null,null);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(465, 327);
		this.setRightComponent(getChartEvidence());
		this.setLeftComponent(getChartHistory());
        this.setEnabled(true);
        this.setDividerSize(10);
        this.setOneTouchExpandable(false);
        this.setContinuousLayout(true);
        //this.setRightComponent(null);
        //this.setLeftComponent(null);
        this.setDividerLocation(250);
        this.setDividerLocation(0.99D);
        this.setResizeWeight(0.99);

	}
	
	public void setToulmin(Toulmin toulmin)
	{
		if (toulmin==null) return;
		this.toulmin = toulmin;
		getChartHistory().setToulmin(toulmin);
		
	}
	
	public void setListeners(DialoguePlanner planner)
    {
		getChartEvidence().setListeners(planner);
		getChartEvidence().addSwap(ChartEvidence.EVIDENCE);
		getChartEvidence().addSwap(ChartEvidence.ATTRIBUTE);

		DlgMoveAction act = (DlgMoveAction) planner.getAction(DlgMoveID.TELLMORE);
		getChartEvidence().addTellMeListener((MouseListener)act);
		getChartEvidence().addTellMeListener(planner.setTellMeMoreListener(DlgTellMeMore.BELIEF));

        act = (DlgMoveAction) planner.getAction(DlgMoveID.SWAP);
        getChartEvidence().addSwapListener((MouseListener)act);
        getChartEvidence().addSwapListener(planner.setSwapViewListerner(DlgSwapView.EVIDENCE,getChartEvidence()));
        getChartEvidence().setListeners(planner);

    }

	/**
	 * This method initializes chartHistory	
	 * 	
	 * @return olmgui.output.ChartHistory	
	 */
	private ChartHistory getChartHistory() {
		if (chartHistory == null) {
			chartHistory = new ChartHistory();
			chartHistory. getJChartPanel().addChartMouseListener(new MyListener());

		}
		return chartHistory;
	}

	/**
	 * This method initializes chartEvidence	
	 * 	
	 * @return olmgui.output.ChartEvidence	
	 */
	private ChartEvidence getChartEvidence() {
		if (chartEvidence == null) {
			chartEvidence = new ChartEvidence();
		}
		return chartEvidence;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
