package toulmin;

import java.util.Hashtable;

/**
 * This class implements a Toulmin Argumentation Pattern which consists in:
 * <ul>
 * <li> the claim (see {@link toulmin.ToulminClaim}) containing a summary of the 
 *      belief hold by the Learner Model.
 * <li> the data (see {@link toulmin.ToulminData}) containing the numerical
 *      representation of the belief.
 * <li> a list (see {@link toulmin.ToulminList}) of individual pieces of evidence 
 *      justifying the belief; it could be either other (sub) Toulmin Argumentation
 *      Patterns or some warrants.
 * <li> some warrants (see {@link toulmin.ToulminWarrant}) containing the numerical
 *      representation of the piece of evidence.
 * <li> some backing (see {@link toulmin.ToulminBacking}) containing the description 
 *      of the events justifying the piece of evidence.
 * </ul>
 *
 * @author Nicolas Van Labeke
 * @version $Revision: 1.7 $
 *
 */
public class Toulmin implements XMLRPCWrapper
{
    /**
     * Identifier for the different elements of the TAP.
     */
    public static String 	CLAIM = "CLAIM",      ///< Identifier for the claim.
                            SUBCLAIM = "SUBCLAIM",        ///< Identifier for the data.
    						DATA = "DATA",        ///< Identifier for the data.
    						WARRANT = "WARRANT",  ///< Identifier for a warrant.
    						BACKING = "BACKING";  ///< Identifier for a backing.
    
    /**
     * A reference to the main TAP containing this element.
     * If this elements is a sub-TAP, points to the main TAP. Otherwise is null.
     */
    private Toulmin topToulmin = null;

	/**
	 * The Belief Descriptor associated with this argumentation pattern.
	 */
	BeliefDesc		beliefDesc = null;
	
	/**
	 * The claim that is justified by this argumentation pattern.
	 */
	ToulminClaim 	claim = null;
	
	/**
	 * The data that supports the claim.
	 */
	ToulminData 	data = null;
	
	/**
	 * The list of individual pieces of evidence that supports the data.
	 * Note that it could be a list of {@link ToulminWarrant} or a list of sub-patterns
	 * ie {@link Toulmin}.
	 */
	ToulminList		list =null;
	
	/**
	 * The "depth" of this Toulmin Argumentation Pattern.
	 * @todo currently the number of "nodes" in the structure; something a bit more 
	 * 		 elaborated could be useful since it is used for Metacognition diagnosis.
	 */
	int				depth = 0;
	
	/**
	 * Default constructor
	 */
	public Toulmin() 
	{
	}

	/* (non-Javadoc)
	 * @see toulmin.XMLRPCWrapper#toXMLRPC()
	 */
	public Object toXMLRPC() 
	{
		Hashtable tab = new Hashtable();
		
		if (beliefDesc!=null) tab.put(TOULMIN_BDESCRIPT,beliefDesc.toXMLRPC());
		if (claim!=null) tab.put(TOULMIN_CLAIM,claim.toXMLRPC());
		if (data!=null) tab.put(TOULMIN_DATA,data.toXMLRPC());
		if (list!=null) tab.put(TOULMIN_LIST,list.toXMLRPC());
		tab.put(TOULMIN_DEPTH,new Integer(depth));
		return tab;
	}
	
	/**
	 * Create a new instance of a TAP based on the XML-RPC compliant object.
	 * @param src	The object containing the Toulmin Argumentation Pattern.
	 * @return	A new instance of a TAP, filled with the data from the object.
	 */
    public static Toulmin fromXMLRPC(Object src)
    {
        return Toulmin.fromXMLRPC(src,null);
    }
    
    public ToulminWarrant getEvidenceAt(int index)
    {
        ToulminWarrant war = null;
        ToulminList list = getList();
        if (list==null) return null;
        if (list.getPartition().equals(ToulminList.CLUSTER_PERF))
        {
            for (int j=0;j<list.size() && war == null;j++)
            {
                Toulmin obj = (Toulmin) list.get(j);
                ToulminList sublist = obj.getList();
                for (int i=0;i<sublist.size() && war == null;i++)
                {
                    ToulminWarrant subobj = (ToulminWarrant) sublist.get(i);
                    if (index == subobj.getIndex()) war = subobj;
                }
            }
        }
        else
        {
            for (int i=0;i<list.size() && war == null;i++)
            {
                ToulminWarrant obj = (ToulminWarrant) list.get(i);
                if (index == obj.getIndex()) war = obj;
            }
        }
        return war;
    }

    public static Toulmin fromXMLRPC(Object src,Toulmin parent) 
	{
		Toulmin toulmin = new Toulmin();
        toulmin.topToulmin = parent;

        Hashtable xmlrpc=(Hashtable)src;
        Object obj = xmlrpc.get(TOULMIN_DEPTH);
        if (obj!=null && obj instanceof Integer) toulmin.depth = ((Integer)obj).intValue();

        obj = xmlrpc.get(TOULMIN_BDESCRIPT);
        if (obj!=null) toulmin.beliefDesc = BeliefDesc.fromXMLRPC(obj);
        else if (parent!=null)
            toulmin.beliefDesc = parent.getBeliefDesc();

        obj = xmlrpc.get(TOULMIN_CLAIM);
        if (parent==null)
        {
            if (obj!=null) toulmin.claim = ToulminClaim.fromXMLRPC(obj,toulmin);
        }
        else
        {
            if (obj!=null) toulmin.claim = ToulminSubClaim.fromXMLRPC(obj,toulmin);
        }
		obj = xmlrpc.get(TOULMIN_DATA);
		if (obj!=null) toulmin.data = ToulminData.fromXMLRPC(obj,toulmin);
        obj = xmlrpc.get(TOULMIN_LIST);
        if (obj!=null) toulmin.list = ToulminList.fromXMLRPC(obj,toulmin);
		return toulmin;
	}

	/**
	 * Return the belief descriptor associated with the Toulmin Argumntation Pattern.
	 * @return A reference to the belief descriptor associated with this TAP.
	 */
	public BeliefDesc getBeliefDesc() {
		return beliefDesc;
	}

	/**
     * Set the belief descriptor associated with this TAP.
	 * @param beliefDesc A reference to the belief descriptor to associate.
	 */
	public void setBeliefDesc(BeliefDesc beliefDesc) {
		this.beliefDesc = beliefDesc;
	}

	/**
     * Get the claim associated with this TAP.
	 * @return A reference to the claim of the TAP.
	 */
	public ToulminClaim getClaim() {
		return claim;
	}

	/**
     * Set the claim for this TAP.
	 * @param claim A reference to the claim to associate.
	 */
	public void setClaim(ToulminClaim claim) {
		this.claim = claim;
	}

	/**
     * Get the data associated with this TAP.
	 * @return A reference to the data of the TAP.
	 */
	public ToulminData getData() {
		return data;
	}

	/**
     * Set the data for this TAP.
	 * @param data A reference to the data to set.
	 */
	public void setData(ToulminData data) {
		this.data = data;
	}

	/**
     * Get the list of the TAP constituents.
     * Note that the list will contain either other TAP structure or warrants.
	 * @return A reference to the list of TAP's constituent.
	 */
	public ToulminList getList() {
		return list;
	}

	/**
     * Set the list of this TAP's constituents. 
	 * @param list A reference to the list of constituents to add to this TAP.
	 */
	public void setList(ToulminList list) {
		this.list = list;
	}

	/**
     * Get the complexity of this TAP.
	 * @return A positive integer representing the depth of the TAP.
	 */
	public int getDepth() {
		return depth;
	}

	/**
     * Set the complexity of this TAP.
	 * @param depth A positive integer representing the depth of the TAP.
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

    public Toulmin getTopToulmin() {
        return topToulmin;
    }

    public void setTopToulmin(Toulmin topToulmin) {
        this.topToulmin = topToulmin;
    }

	
}
