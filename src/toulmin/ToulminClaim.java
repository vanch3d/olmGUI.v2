package toulmin;

import java.util.ArrayList;

import olmgui.i18n.Messages;


/**
 * Implementation of the Claim element of a Toulmin Argumentation Pattern.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.5 $
 */
public class ToulminClaim implements XMLRPCWrapper 
{
	/**
	 * The summary level associated with the claim.
     *  
     * As defined in the Learner Model, it is a numeric representation of the 
     * dominant level in the belief. The levels are considered numbered from 
     * 1 to N, and this method maps them uniformely in the interval [0,1] 
     * such that the value <i>k/N</i> stands for level <i>(k+1)</i>. 
     * If <i>s</i> is the value returned by this method, then
     * Math.round(<i>s</i>*(N-1))+1 would be the dominant level in the belief.
	 */
	double summary = -1;
	
    /**
     * A reference to the main TAP containing this bakcing.
     */
    Toulmin toulmin = null;
	
	/* (non-Javadoc)
	 * @see toulmin.XMLRPCWrapper#toXMLRPC()
	 */
	public Object toXMLRPC() 
	{
		Double claim = new Double(summary);
		return claim;
	}


    /**
     * Create a new instance of a claim based on the XML-RPC compliant object.
     * @param obj       The object containing the claim.
     * @param toulmin   A reference to the TAP containg this claim.
     * @return  A new instance of a claim, filled with the data from the object.
     */
	public static ToulminClaim fromXMLRPC(Object obj,Toulmin toulmin) 
	{
		ToulminClaim claim = null;
		if (obj instanceof Double) 
		{
			Double level = (Double) obj;
			claim = new ToulminClaim();
			claim.summary = level.doubleValue(); 
            claim.toulmin = toulmin;
		}
		return claim;
	}


	/**
     * Get the summary level associated with this claim.
	 * @return A double in [0,1]. 
	 */
	public double getClaimSummary() 
    {
		return summary;
	}


	/**
     * Set the summary level for this claim.
	 * @param summary A double in [0,1].
	 */
	public void setClaimSummary(double summary)
    {
		this.summary = summary;
	}
	
    /**
     * Get the level associated with this claim.
     * @return An integer in [1,4].
     */
    public int getClaimLevel()
    {
        int lvl = (int) (Math.round(summary*3)+1);
        return lvl;
    }
    
    /**
     * Analyse the claim and return elements for verbalising it.
     * The method analyses whether the claim is ABOVE or BELOW the level's threshold 
     * and whether it is AWAY from it.
     * The ArrayList contains, in this order, the complement to the template describing 
     * the claim and the list of all arguments to be matched to the template.
     * @return A list containing the template and its arguments to match.
     */
    public ArrayList analyseClaim()
    {
        ArrayList analyse = new ArrayList();
        
        double numlevel = (getClaimLevel()-1)/3.0;
        double delta = this.summary - numlevel;
        boolean above= (delta>0.0);
        boolean away = Math.abs(delta)>(1/12.0);
        String template = (above)? ".ABOVE" : ".BELOW";
        analyse.add(template);
        analyse.add(Messages.getJudgementOn(toulmin.getBeliefDesc(), this.summary));
        if (away)
        {
            template += ".AWAY";
            analyse.set(0, template);
            int closeto = getClaimLevel();
            closeto = (above)? closeto+1 : closeto-1;
            analyse.add(Messages.getJudgementOn(toulmin.getBeliefDesc(), closeto));
        }
        return analyse;
    }
	

}
