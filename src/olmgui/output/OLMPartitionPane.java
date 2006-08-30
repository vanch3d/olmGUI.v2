package olmgui.output;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import olmgui.i18n.Messages;
import toulmin.Toulmin;
import toulmin.ToulminList;
import toulmin.ToulminSubClaim;
import toulmin.ToulminWarrant;

public class OLMPartitionPane extends JLabel implements MouseListener
{
	private static double piby2 = Math.PI / 2.0;
	private static double twopi = Math.PI * 2.0;
	private static double d2r   = Math.PI / 180.0; // Degrees to radians.
	private static int xGap = 5;
	private static int inset = 40;

	protected int originX, originY;
	protected int radius;
	
	protected int selected = -1;

	protected double totalValue= 0;
	protected double totalWeight= 0;
	protected ArrayList values = new ArrayList();
	protected ArrayList weight = new ArrayList();
	protected ArrayList labels = new ArrayList();
//	  protected double values[] = null;
//	  protected String labels[] = null;

	  protected Color colors[] = new Color[] {
			    Color.red, Color.blue, Color.yellow, Color.black, Color.green,
			    Color.white, Color.gray, Color.cyan, Color.magenta, Color.darkGray
			  };
	  
	  protected Font textFont = new Font("Serif", Font.PLAIN, 12);
	  protected Color textColor = Color.black;


	public OLMPartitionPane() 
	{
		super("");
		initialize();
		addMouseListener(this);

	}

	protected void paintComponent(Graphics gr) 
	{
		super.paintComponent(gr);
		
		
        Graphics2D g2d = (Graphics2D)gr;
        
        // Enable antialiasing for shapes
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

		Dimension size = getSize();
		int margin = size.width/4;
		int marginy=size.height/4;
		
		size.width -=  margin;
		size.height -=  marginy;
	    originX = margin/2+size.width / 2;
	    originY = marginy/2+size.height / 2;
	    int diameter = (originX < originY ? size.width - inset 
	                                      : size.height - inset);
	    radius = (diameter / 2) + 1;
	    int cornerX = (originX - (diameter / 2));
	    int cornerY = (originY - (diameter / 2));
	    
	    int startAngle = 0;
	    int arcAngle = 0;
	    int mstart=-1,mangle=-1;
	    for (int i = 0; i < values.size(); i++) 
	    {
	      double dd = ((Double)values.get(i)).doubleValue();
	      double rr = ((Double)weight.get(i)).doubleValue();
	      arcAngle = (int)(i < values.size() - 1 ?
	                       Math.round(((rr * dd) / (totalWeight)) * 360) :
	                       360 - startAngle);
	      g2d.setColor(colors[i % colors.length]);
	      //g2d.fillArc(cornerX, cornerY, diameter, diameter, 
	      //          startAngle, arcAngle);
	      g2d.fill(new Arc2D.Double(cornerX, cornerY, diameter, diameter, 
	                startAngle, arcAngle,Arc2D.PIE));
	      if (i==selected)
	      {
	    	  mstart = startAngle;
	    	  mangle = arcAngle;
	      }
          drawLabel(g2d, labels.get(i).toString(), startAngle + (arcAngle / 2));
	      startAngle += arcAngle;
	    }
	    if (mstart!=-1)
	    {
	    	g2d.setColor(Color.black);
	    	  Stroke str = g2d.getStroke();
	    	  g2d.setStroke(new BasicStroke(4.0f));
		      g2d.draw(new Arc2D.Double(cornerX, cornerY, diameter, diameter, 
		    		  mstart, mangle,Arc2D.PIE));
	    	  g2d.setStroke(str);
	    }
	    //g2d.setColor(Color.black);
	    //g2d.drawOval(cornerX, cornerY, diameter, diameter);  // Cap the circle.
	    
//	  Disable antialiasing for shapes
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	public void drawLabel(Graphics g, String text, double angle) 
	{
	    g.setFont(textFont);
	    g.setColor(textColor);
	    double radians = angle * d2r;
	    int x = (int) ((radius + xGap) * Math.cos(radians));
	    int y = (int) ((radius + xGap) * Math.sin(radians));
	    if (x < 0) { 
	      x -= SwingUtilities.computeStringWidth(g.getFontMetrics(), Messages.getString(text));
	    }
	    if (y < 0) {
	      y -= g.getFontMetrics().getHeight();
	    }
	    g.drawString(Messages.getString(text), x + originX, originY - y);
	  }

	
	public void setToulmin(Toulmin toulmin)
	{
		totalValue = 0;
		totalWeight = 0;
		selected = -1;
		values.clear();
		labels.clear();
		weight.clear();

		if (toulmin==null) return;
		ToulminList list = toulmin.getList();
		if (list.getPartition().equals(ToulminList.CLUSTER_NONE)) return;
		
		for (int i=0;i<list.size();i++)
		{
			Toulmin sub = (Toulmin)list.get(i);
			ToulminList sublist = sub.getList();
			int nb = sublist.size();
			totalValue+=nb;
			values.add(new Double(nb));
			ToulminSubClaim claim = (ToulminSubClaim)sub.getClaim();
			labels.add(claim.getValue());
			double tt = 0;
			for (int j=0;j<sublist.size();j++)
			{
				ToulminWarrant obj = (ToulminWarrant)sublist.get(j);
				int nb2= obj.getRelevance();
				tt += (nb2/100.);
			}
			weight.add(new Double(tt));
			totalWeight +=nb*tt;
		}
	}
	
	public void setPartition(String value)
	{
		selected = labels.indexOf(value);
		updateUI();
		setToolTipText("");
	}
	
	/**
	 * @param me
	 * @return
	 */
	public int indexOfEntryAt(Point pt) 
	{
	    int x = (int)pt.getX() - originX;
	    int y = originY - (int)pt.getY();  // Upside-down coordinate system.
	    
	    // Is (x,y) in the circle?
	    if (Math.sqrt(x*x + y*y) > radius) { return -1; }

	    double percent = Math.atan2(Math.abs(y), Math.abs(x));
	    if (x >= 0) {
	      if (y <= 0) { // (IV)
	        percent = (piby2 - percent) + 3 * piby2; // (IV)
	      }
	    }
	    else {
	      if (y >= 0) { // (II)
	        percent = Math.PI - percent;
	      }
	      else { // (III)
	        percent = Math.PI + percent;
	      }
	    }
	    percent /= twopi;
	    double t = 0.0;
	    if (values != null) {
	      for (int i = 0; i < values.size(); i++)
	      {
	    	  double dd = ((Double)values.get(i)).doubleValue();
	        if (t + (dd/totalValue) > percent) {
	          return i;
	        }
	        t += dd/totalValue;
	      }
	    }
	    return -1;
	  }

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new java.awt.Dimension(300,300));
			
	}

	public void mouseClicked(MouseEvent e) 
	{
		int sel = indexOfEntryAt(e.getPoint());
		System.err.println("selection = " + sel);
		
	}
	
	

	public String getToolTipText()
	{
		return super.getToolTipText();
	}

	public String getToolTipText(MouseEvent e) 
	{
		int sel = indexOfEntryAt(e.getPoint());
		if (sel!=-1)
			return "selection = " + sel + " (" + values.get(sel)+")";
		else
			return null;
		//return super.getToolTipText(event);
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {
		setToolTipText(null);
		
	}

}
