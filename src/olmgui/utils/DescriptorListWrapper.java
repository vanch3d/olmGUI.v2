package olmgui.utils;

import java.util.Vector;

import toulmin.BeliefDesc;

public class DescriptorListWrapper {

	public BeliefDesc bdesc;
	public boolean isNew;
	
	
	/**
	 * 
	 */
	public DescriptorListWrapper() 
	{
		this.bdesc = new BeliefDesc();
		this.isNew = false;
	}
	
	/**
	 * @param bdesc
	 * @param isNew
	 */
	public DescriptorListWrapper(Vector bdesc, boolean isNew)
	{
		super();
		this.bdesc = new BeliefDesc(bdesc);
		this.isNew = isNew;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
    {
		return bdesc.toString();
	}
	
	
	
	

}
