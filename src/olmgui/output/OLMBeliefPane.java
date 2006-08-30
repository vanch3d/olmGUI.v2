/**
 * @file OLMBeliefPane.java
 */
package olmgui.output;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.UIManager;

import olmgui.i18n.Messages;
import olmgui.utils.Gradient;
import toulmin.BeliefDesc;
import dialogue.DlgMoveID;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.21 $
 *
 */
public class OLMBeliefPane extends JLabel implements ActionListener {

    private Double  summary = new Double(0.0);
    private ArrayList  history = null;
    private BeliefDesc  beliefdesc = null;
    
    private ArrayList clippings = null;
    
    private boolean showSummary = true;
    private int     selected = -1;
    
    /**
     * 
     */
    public OLMBeliefPane() {
        super();
        initialize();
    }
    
    /**
     * This method initializes this
     * 
     */
    private void initialize()
    {
        this.setToolTipText("");
        //this.setBackground(java.awt.SystemColor.activeCaptionText);
        this.setSize(new java.awt.Dimension(300,150));
    }
    

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isEnabled()) return;
        if (beliefdesc==null) return;

        Graphics2D g2d = (Graphics2D)g;
        
        Font oldF = g2d.getFont();
        g2d.setFont(new Font(oldF.getFontName(),Font.BOLD,oldF.getSize()));
        
        Rectangle rect = getRect();
        FontMetrics fontMetrics = g.getFontMetrics();
        String label = Messages.getJudgementOn(this.beliefdesc,3);
        int width = fontMetrics.stringWidth(label);
        int height = fontMetrics.getHeight();
        rect.x += width +10;
        rect.width -= (width +10);
        
        
        Gradient grd = new Gradient();
        grd.addPoint(Color.RED);
        grd.addPoint(new Color(255,150,1));
        //grd.addPoint(Color.ORANGE);
        grd.addPoint(Color.YELLOW);
        grd.addPoint(new Color(79,255,1));
        grd.addPoint(new Color(1,187,1));
        //grd.addPoint(Color.GREEN);

        grd.createGradient(rect.height);
        
//            GradientPaint gradientRO = new GradientPaint(
//            		rect.x, rect.y+rect.height, Color.RED, 
//            		rect.x, rect.y+rect.height/2, Color.ORANGE);
        	
            //g2d.setPaint(gradientRO);
            //g2d.fillRect(rect.x,rect.y+rect.height/2, rect.width, rect.height/2);
            
//            GradientPaint gradientOG = new GradientPaint(
//            		rect.x, rect.y+rect.height/2, Color.ORANGE, 
//            		rect.x, rect.y, Color.GREEN);
        	
            //g2d.setPaint(gradientOG);
            //g2d.fillRect(rect.x,rect.y, rect.width, rect.height/2);
        
        g2d.setColor(Color.BLACK);
        g2d.drawLine(rect.x,rect.y,rect.x,rect.y+rect.height);
        if (showSummary && !summary.equals(new Double(-1)))
        {
//            Color startColor = Color.red;
//            Color endColor = Color.green;
//            GradientPaint gradient = new GradientPaint(rect.x, rect.height, startColor, rect.x, rect.y+rect.height/2, endColor);

            int score = (int)(summary.doubleValue()*rect.height);
            

                clippings = new ArrayList();
                //clippings.add(new Rectangle(rect.x+6,rect.y+1+rect.height-score, rect.width-12, score-1));

//                if (score <rect.height/2)
//                {
//                    g2d.setPaint(gradientRO);
//                    g2d.fillRect(rect.x+6,rect.y+1+rect.height-score, rect.width-12, score-1);
//                	
//                }
//                else
//                {
//                    g2d.setPaint(gradientOG);
//                    g2d.fillRect(rect.x+6,rect.y+1+rect.height-score, rect.width-12, rect.height/2);
//                    g2d.setPaint(gradientRO);
//                    g2d.fillRect(rect.x+6,rect.y+1+rect.height/2, rect.width-12, rect.height/2);
//
//                }
                //g2d.setPaint(gradient);
                //g2d.fillRect(rect.x+6,rect.y+1+rect.height-score, rect.width-12, score-1);
            g2d.setColor(grd.getColour(score));
            g2d.fillRect(rect.x+6,rect.y+1+rect.height-score, rect.width-12, score-1);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(rect.x+6,rect.y+1+rect.height-score, rect.width-12, score-1);
        }
        else if (!showSummary && history!=null && history.size()!=0)
        {
//            Color startColor = Color.red;
//            Color endColor = Color.green;
//            GradientPaint gradient = new GradientPaint(rect.x, rect.height, startColor, rect.x, rect.y+rect.height/2, endColor);

            clippings = new ArrayList();
            
            int w = rect.width-12;
            int nb = this.history.size();
            int delta = (int) ((double)w/(double)nb*2);
            //int rest = w-delta*nb;]
            //int i=0;
            for (int j=0;j<nb;j=j+2)
            {
                int i = j/2;
                Double ss = (Double) history.get(j);
                Double ff = (Double) history.get(j+1);
                int score = (int)(ss.doubleValue()*rect.height);
                int stdev = (int)(ff.doubleValue()*rect.height / 2);
                clippings.add(new Rectangle(rect.x+6+(delta)*i,rect.y+1+rect.height-score, delta, score-1));
                
                if (selected==i)
                {
                    g2d.setColor(UIManager.getColor("List.selectionBackground"));
                    g2d.fillRect(rect.x+6+(delta)*i,rect.y+1, delta+1, rect.height);
                }


//                if (score <rect.height/2)
//                {
//                    g2d.setPaint(gradientRO);
//                    g2d.fillRect(rect.x+6+(delta)*i+1,rect.y+1+rect.height-score, delta-2, score-1);
//                	
//                }
//                else
//                {
//                    g2d.setPaint(gradientOG);
//                    g2d.fillRect(rect.x+6+(delta)*i+1,rect.y+1+rect.height-score, delta-2, rect.height/2);
//                    g2d.setPaint(gradientRO);
//                    g2d.fillRect(rect.x+6+(delta)*i+1,rect.y+1+rect.height/2, delta-2, rect.height/2);
//
//                }
                //g2d.setPaint(gradient);
                //g2d.fillRect(rect.x+6+(delta)*i,rect.y+1+rect.height-score, delta, score-1);
                g2d.setColor(grd.getColour(score));
                g2d.fillRect(rect.x+6+(delta)*i+1,rect.y+1+rect.height-score, delta-2, score-1);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(rect.x+6+(delta)*i+1,rect.y+1+rect.height-score, delta-2, score-1);
                
                g2d.setColor(new Color(0,0,192));
                g2d.drawLine(rect.x+6+(delta)*i+1+delta/4,rect.y+rect.height-score + stdev,
                        rect.x+6+(delta)*i+1+3*delta/4,rect.y+rect.height-score + stdev);
                g2d.drawLine(rect.x+6+(delta)*i+1+delta/4,rect.y+rect.height-score - stdev,
                        rect.x+6+(delta)*i+1+3*delta/4,rect.y+rect.height-score - stdev);

                g2d.drawLine(rect.x+6+(delta)*i+1+delta/2,rect.y+rect.height-score - stdev,
                        rect.x+6+(delta)*i+1+delta/2,rect.y+rect.height-score + stdev);
            }
        }

        g2d.setColor(Color.BLACK);
        double inc = rect.y;
        for (int i=0;i<=12;i++)
        {
            g2d.drawLine(rect.x-2,(int)inc,rect.x+2,(int)inc);
            inc += rect.height/12.00;
        }
        
        inc = rect.y;
        for (int i=0;i<4;i++)
        {
            //FontMetrics fontMetrics = g.getFontMetrics();
            int score = (int)( rect.y + (1-(i/3.0))*rect.height);
            g2d.drawLine(rect.x+4,score,rect.x+rect.width-2,score);
            label = Messages.getJudgementOn(this.beliefdesc,i+1);
            g2d.drawString(label,rect.x+5-width -10,score+height/4);
        }
        
        //for (int i=0;i<3;i++)

        g2d.setFont(oldF);

    }
    
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#getToolTipText()
     */
    public String getToolTipText() {
        if (!isEnabled() || summary==null) 
            return null;
        else
        {
            NumberFormat fmt = new DecimalFormat(".000");
            return fmt.format(summary);

        }
    }

    public void clear()
    {
        this.setEnabled(false);
        this.updateUI();
    }
    
    public void setData(BeliefDesc belief,Double summary)
    {
        this.beliefdesc = belief;
        this.summary = summary;
        this.setEnabled(true);
    }

    public void setData(BeliefDesc belief,Double summary,ArrayList list)
    {
        this.beliefdesc = belief;
        this.summary = summary;
        this.history = list;
        this.setEnabled(true);
    }
    
    private Rectangle getRect()
    {
        return new Rectangle(5,5,getWidth()-10,getHeight()-10);
    }

    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals(DlgMoveID.TELLMORE.toString()))
        {
            //showSummary = !showSummary;
            //updateUI();
        }
    }

    public void showSummary(boolean show) 
    {
        this.showSummary = show;
        clippings = null;
        this.selected = -1;
    }
    
    
    public int getHistory(Point pt)
    {
        int nb =-1;
        
        if (clippings!=null)
        {
            for (int i=0;i<clippings.size();i++)
            {
                Rectangle rect = (Rectangle)clippings.get(i);
                if (rect.contains(pt))
                {
                    nb = i;
                    break;
                }
            }
        }

        return nb;
    }

    public int getSelected() 
    {
        return selected;
    }

    public void setSelected(int selected) 
    {
        this.selected = selected;
    }

    
}  //  @jve:decl-index=0:visual-constraint="10,2" 