package olmgui.output;

import olmgui.utils.OutputViewPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

public abstract class OLMChartPanel extends OutputViewPanel
{

    private static final long serialVersionUID = 1L;

    private ChartPanel jChartPanel = null;  
    private JFreeChart jChart = null;  //  @jve:decl-index=0:

    //private String chartTitle = "";  //  @jve:decl-index=0:

    
    /**
     * This is the default constructor
     */
    public OLMChartPanel(){
        super();
        initialize();
        //setTitle("");
    }
    
    public OLMChartPanel(String title) 
    {
        super();
        initialize();
        //setTitle(title);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(300, 200);
//        this.setLayout(new GridBagLayout());
//        
//        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
//        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
//        gridBagConstraints2.gridy = 2;
//        gridBagConstraints2.weightx = 1.0;
//        gridBagConstraints2.weighty = 0.6;
//        gridBagConstraints2.gridx = 0;
//
//        this.add(getJChartPanel(), gridBagConstraints2);
        add(getJChartPanel(),java.awt.BorderLayout.CENTER);
        
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
            jChartPanel.setRangeZoomable(false);
        }
        
        return jChartPanel;
    }

    
    protected JFreeChart getJFreeChart()
    {
        if (jChart==null)
        {
            jChart = new JFreeChart(
                    "", 
                    JFreeChart.DEFAULT_TITLE_FONT, 
                    getPlot(), 
                    true);
            //jChart.setBackgroundPaint(Color.white);
            jChart.getTitle().setExpandToFitSpace(true);
        }
        return jChart;
    }
 
    
//    protected String getTitle() 
//    {
//        return getJFreeChart().getTitle().getText();
//    }
//
//    protected void setTitle(String title)
//    {
//        //chartTitle = title;
//        getJFreeChart().setTitle(title);
//    }
    

    abstract protected Plot getPlot();

	


}
