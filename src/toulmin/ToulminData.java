package toulmin;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


/**
 * Implementation of the Data element of a Toulmin Argumentation Pattern.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.12 $
 */
public class ToulminData implements XMLRPCWrapper 
{
    
    /**
     * A reference to the main TAP containing this bakcing.
     */
    //Toulmin toulmin = null;
    
    /**
     * The pignistic function. 
     */
    ArrayList pignistic = null;
    
    /**
     * The confidence intervals.
     */
    ArrayList confidence = null;
    
    /**
     * The mass distribution.
     */
    ArrayList distribution = null;

    /**
     * The (discounted) mass distribution.
     */
    ArrayList discount = null;

    ArrayList history = null;
    
    public double uncertainty = -1;
    
    public double conflict = -1;


    /* (non-Javadoc)
     * @see toulmin.XMLRPCWrapper#toXMLRPC()
     */
    public Object toXMLRPC() 
    {
        Hashtable tab = new Hashtable();
        if(pignistic!=null) tab.put(DATA_PIGNISTIC,new Vector(pignistic));
        if(confidence!=null) tab.put(DATA_CONFIDENCE,new Vector(confidence));
        if(distribution!=null) tab.put(DATA_DISTRIBUTION,new Vector(distribution));
        if(discount!=null) tab.put(DATA_DISCOUNT,new Vector(discount));
        if(history!=null) tab.put(DATA_HISTORY,new Vector(history));
        if(uncertainty!=-1) tab.put(DATA_UNCERTAINTY,new Double(uncertainty));
        if(conflict!=-1) tab.put(DATA_CONFLICT,new Double(conflict));
        return tab;
    }

    /**
     * Create a new instance of a data based on the XML-RPC compliant object.
     * @param obj       The object containing the data.
     * @param toulmin   A reference to the TAP containg this data.
     * @return  A new instance of a data, filled with the data from the object.
     */
    public static ToulminData fromXMLRPC(Object obj,Toulmin toulmin) 
    {
        ToulminData data = null;
        if (obj instanceof Hashtable) 
        {
            data = new ToulminData();
            Hashtable tab = (Hashtable) obj;
            Object o=tab.get(DATA_PIGNISTIC);
            if (o instanceof Vector)
                data.pignistic = fromVector((Vector) o);
            o=tab.get(DATA_CONFIDENCE);
            if (o instanceof Vector)
                data.confidence = fromVector((Vector) o);
            o=tab.get(DATA_DISTRIBUTION);
            if (o instanceof Vector)
                data.distribution = fromVector((Vector) o);
            o=tab.get(DATA_DISCOUNT);
            if (o instanceof Vector)
                data.discount = fromVector((Vector) o);
            o=tab.get(DATA_HISTORY);
            if (o instanceof Vector)
                data.history = fromVector((Vector) o);
            o=tab.get(DATA_UNCERTAINTY);
            if (o instanceof Double)
                data.uncertainty = ((Double) o).doubleValue();
            o=tab.get(DATA_CONFLICT);
            if (o instanceof Double)
                data.conflict = ((Double) o).doubleValue();
           
            //data.toulmin = toulmin;

        }
        return data;
    }
    
    /**
     * Copy the elements of a Vector into an ArrayList.
     * @param vec   The Vector to duplicate.
     * @return      An ArrayList containing the elements of the vector in the same order.
     */
    private static ArrayList fromVector(Vector vec)
    {
        if (vec==null) return null;
        ArrayList array = new ArrayList();
        Iterator iter = vec.iterator();
        while (iter.hasNext())
            array.add(iter.next());
        return array;
    }

    /**
     * Return the confidence interval distribution of the data.
     * @return  A List containing triplets of "level set" (Integer), certainty and
     *          ignorance for the given set (both Double).
     */
    public ArrayList getCertainty() 
    {
        return confidence;
    }

    /**
     * Set the certainty distribution for this data element.
     * @param certainty The certainty to set.
     */
    public void setCertainty(ArrayList certainty) 
    {
        this.confidence = certainty;
    }

    /**
     * Return the mass distribution of the data
     * @return  A List containing pairs of "level set" (Integer) and the relevant
     *          mass (Double)
     */
    public ArrayList getDistribution() 
    {
        return distribution;
    }

    /**
     * @param distribution The distribution to set.
     */
    public void setDistribution(ArrayList distribution) 
    {
        this.distribution = distribution;
    }

    /**
     * Return the pignistic distribution of the data associated with the belief.
     * @return  A List containing a sequence of doublets for every singleton of the level sets 
     *          (ie the LevelSet - Integer - and the value - Double).
     */
    public ArrayList getPignistic() 
    {
        return pignistic;
    }

    /**
     * Add a pignistic distribution.
     * @param pignistic The pignistic distribution to add to the dat
     */
    public void setPignistic(ArrayList pignistic) {
        this.pignistic = pignistic;
    }

    /**
     * @return Returns the discount.
     */
    public ArrayList getDiscount() {
        return discount;
    }

    /**
     * @param discount The discount to set.
     */
    public void setDiscount(ArrayList discount) {
        this.discount = discount;
    }

    
    public ArrayList getHistory() {
        return history;
    }

    public void setHistory(ArrayList history) {
        this.history = history;
    }

    
    public ArrayList analysePignistic(int lvl)
    {
    	double max = -1;
    	double delta = -1;
    	int index = 0;
        for (int i=0;i<this.pignistic.size();i=i+2)
        {
            //Integer level = (Integer)this.pignistic.get(i);
            Double dc = (Double)this.pignistic.get(i+1);
            if (dc.doubleValue()>max) index = i;
            max = Math.max(max,dc.doubleValue());
        }
        for (int i=0;i<this.pignistic.size();i=i+2)
        {
            //Integer level = (Integer)this.pignistic.get(i);
            Double dc = (Double)this.pignistic.get(i+1);
            double dd = (max - dc.doubleValue())/max;
            delta = Math.max(delta,dd);
        }
        ArrayList info = new ArrayList();
        
        if (delta<0.08)
        {
            info.add(new String(".flat"));
            info.add(null);
        }
        else
        {
            Double d1 = (Double)this.pignistic.get((lvl-1)*2+1);
            delta = (max - d1.doubleValue())/max;
            String template = (delta<0.30)? ".low" : ".high";
            int level = (index/2)+1;
            template +=(level==lvl)? "for" : "against";

            info.add(template);
            info.add("OLMTopicConfig.COMPET.Level" + level);
        }
        return info;
    }
    	
    

}
