package toulmin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;


/**
 * Implement a list of Toulmin Argumentation Pattern elements.
 * It is expected that the list is either a list of warrants for the current TAP 
 * ({@link ToulminWarrant}) or a list of sub-TAP ({@link Toulmin}).
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.10 $
 */
public class ToulminList extends ArrayList implements XMLRPCWrapper 
{
    /**
     * Types of partition of the evidence stored in the ToulminList.
     */
    public static String CLUSTER_NONE = "NOCLUSTER";
    public static String CLUSTER_PERF = "PERFORMANCE";
    
    private String partition = CLUSTER_NONE;
    
    private static Comparator RELEVANCE_COMPARATOR = new Comparator()
    {
        public int compare( Object o1, Object o2 )
        {
        	int res = 0;
            if (o1 instanceof ToulminWarrant && o2 instanceof ToulminWarrant) {
            	ToulminWarrant w1 = (ToulminWarrant) o1;
            	ToulminWarrant w2 = (ToulminWarrant) o2;
                res = w2.relevance - w1.relevance;
                if (res==0)
                {
                	res = w2.index - w1.index;
                }
            }
            return res;
        }
    };

    
//    /**
//     * A reference to the main TAP containing this bakcing.
//     */
//    Toulmin toulmin = null;
    
    /* (non-Javadoc)
     * @see toulmin.XMLRPCWrapper#toXMLRPC()
     */
    public Object toXMLRPC() 
    {
        Vector vec= new Vector();
        Iterator iter = this.iterator();
        while (iter.hasNext())
        {
            Object obj = iter.next();
            if (obj instanceof XMLRPCWrapper) 
            {
                XMLRPCWrapper toulObj = (XMLRPCWrapper) obj;
                vec.add(toulObj.toXMLRPC());
            }
        }
        return vec;
    }

    /**
     * Create a new instance of a list based on the XML-RPC compliant object.
     * It is expected that the elements of the list will be either a list of warrants
     * for the current TAP ({@link ToulminWarrant}) or a list of sub-TAP ({@link Toulmin}).
     *  
     * @param obj       The object containing the list.
     * @param toulmin   A reference to the TAP containg this list.
     * @return  A new instance of a list, filled with the data from the object.
     */
    public static ToulminList fromXMLRPC(Object obj,Toulmin toulmin) 
    {
        ToulminList list = null;
        if (obj instanceof Vector) 
        {
            list = new ToulminList();
            //list.toulmin = toulmin;
            Iterator iter = ((Vector)obj).iterator();
            while (iter.hasNext())
            {
                Object o = iter.next();
                if (o instanceof Hashtable)
                {
                    Object item = ((Hashtable)o).get(TOULMIN_CLAIM);
                    if (item!=null)
                    {
                        list.add(Toulmin.fromXMLRPC(o,toulmin));
                        list.partition = CLUSTER_PERF;
                    }
                    else
                    {
                        item = ((Hashtable)o).get(TOULMIN_WARRANT);
                        if (item!=null)
                        list.add(ToulminWarrant.fromXMLRPC(o,toulmin));
                        list.partition = CLUSTER_NONE;
                    }
                }
            }
        }
        return list;
    }
    
    public SortedSet getSortedEvidence()
    {
    	SortedSet set = new TreeSet(RELEVANCE_COMPARATOR);
    	if (partition.equals(CLUSTER_PERF))
    		set = null;
    	else
    	{
    		for (int i=0;i<this.size();i++)
    		{
    			Object obj = this.get(i);
    			if (obj instanceof ToulminWarrant) 
    			{
					ToulminWarrant war = (ToulminWarrant) obj;
					set.add(war);
				}
    		}
    	}
    	return set;
    }

    /**
     * Returns the type of evidence partition used in this TAP.
     * @see #setPartition(String)
     * @return A String containing the type of evidence partition.
     */
    public String getPartition() 
    {
        return partition;
    }

    /**
     * Define the type of partition used in this TAP.
     * Evidence (ie the pairs warrant/backing) could be partitioned according 
     * to several criteria:
     * - CLUSTER_NONE (the default value) indicates that no partition is used
     *   (warrants are associated "flat" with the data/claim).
     * - CLUSTER_PERF indicates that warrants are partitioned according to the
     *   performance of the learner.
     *   
     * @param partition The type of evidence partition to be used.
     */
    public void setPartition(String partition) 
    {
        this.partition = partition;
    }

    public Toulmin analysePartition()
    {
    	Toulmin result = null;

    	if (CLUSTER_NONE.equals(getPartition())) return result;
    	
    	ArrayList map = new ArrayList();
		for (int i=0;i<this.size();i++)
		{
			Toulmin sub = (Toulmin)this.get(i);
			ToulminList sublist = sub.getList();
			//int nbEvidence = sublist.size();
			//ToulminSubClaim claim = (ToulminSubClaim)sub.getClaim();
			double tt = 0;
			// get the total weight (impact factor) of the set
			for (int j=0;j<sublist.size();j++)
			{
				ToulminWarrant obj = (ToulminWarrant)sublist.get(j);
				int nb2= obj.getRelevance();
				tt += (nb2/100.);
			}
			// or just get the number of important evidence 
			// TODO
			
			map.add(new Double(tt));

		}
		// get the maximum weight of the set
        double max = -1;
        for (int i=0;i<map.size();i++)
        {
        	max = Math.max(((Double)map.get(i)).doubleValue(),max);
        }
        int index = map.indexOf(new Double(max));
        if (index!=-1)
        	result = (Toulmin)this.get(index);
        return result;
    }
    
}
