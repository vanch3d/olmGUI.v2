package toulmin;

import java.util.Hashtable;

/**
 * Implementation of a Warrant element of the Toulmin Argumentation Pattern. 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.8 $
 */
public class ToulminWarrant implements XMLRPCWrapper
{

    Toulmin          toulmin = null;
    ToulminData      data;
    ToulminBacking   backing;
    int              index;
    int              relevance;
    
    boolean 		 expanded = true;

    public Object toXMLRPC() 
    {
        Hashtable tab = new Hashtable();
        if(data!=null) tab.put(TOULMIN_WARRANT,data.toXMLRPC());
        if(backing!=null) tab.put(TOULMIN_BACKING,backing.toXMLRPC());
        tab.put(TOULMIN_INDEX,new Integer(index));
        tab.put(TOULMIN_RELEVANT,new Integer(relevance));
        return tab;
    }

    /**
     * Create a new instance of a warrant based on the XML-RPC compliant object.
     * @param obj       The object containing the warrant.
     * @param toulmin   A reference to the TAP containg this warrant.
     * @return  A new instance of a warrant, filled with the data from the object.
     */
    public static ToulminWarrant fromXMLRPC(Object obj,Toulmin toulmin) 
    {
        ToulminWarrant warrant = null;
        if (obj instanceof Hashtable) 
        {
            warrant= new ToulminWarrant();
            warrant.toulmin = toulmin;
            
            Hashtable tab = (Hashtable)obj;
            warrant.data = ToulminData.fromXMLRPC(tab.get(TOULMIN_WARRANT),toulmin);
            warrant.backing = ToulminBacking.fromXMLRPC(tab.get(TOULMIN_BACKING),toulmin);
            if (warrant.backing!=null)
                warrant.backing.setWarrant(warrant);
            Object index = tab.get(TOULMIN_INDEX);
            if (index!=null && index instanceof Integer) 
                warrant.index = ((Integer)index).intValue();
            Object relevant = tab.get(TOULMIN_RELEVANT);
            if (relevant!=null && relevant instanceof Integer) 
                warrant.relevance = ((Integer)relevant).intValue();
        }
        return warrant;
    }

    /**
     * @return Returns the backing.
     */
    public ToulminBacking getBacking() {
        return backing;
    }

    /**
     * @param backing The backing to set.
     */
    public void setBacking(ToulminBacking backing) {
        this.backing = backing;
    }

    /**
     * @return Returns the data.
     */
    public ToulminData getData() {
        return data;
    }

    /**
     * @param data The data to set.
     */
    public void setData(ToulminData data) {
        this.data = data;
    }

    /**
     * @return Returns the index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return Returns the relevance.
     */
    public int getRelevance() {
        return relevance;
    }

    /**
     * @param relevance The relevance to set.
     */
    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

    
}
