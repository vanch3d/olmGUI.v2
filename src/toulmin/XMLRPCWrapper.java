package toulmin;

/**
 * Interface for the various class to be exchanged, by XML-RPC, between the OLM core
 * and the OLM GUI.
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.8 $
 */
public interface XMLRPCWrapper 
{
	/**
	 * XML-RPC key for the belief descriptor.
	 */
	public static String TOULMIN_BDESCRIPT = "TOULMIN_BDESCRIPT";
	
	/**
	 * XML-RPC keys for the elements of the Toulmin Argumentation Pattern.
	 */
	public static String TOULMIN_CLAIM = "TOULMIN_CLAIM",
                         TOULMIN_DATA = "TOULMIN_DATA",
                         TOULMIN_LIST = "TOULMIN_LIST",
                         TOULMIN_WARRANT = "TOULMIN_WARRANT",
                         TOULMIN_BACKING = "TOULMIN_BACKING";
    
    /**
     * XML-RPC key for the attribute of the Toulmin Argumentation Pattern.
     */
    public static String TOULMIN_DEPTH = "TOULMIN_DEPTH"; 
    
    /**
     * XML-RPC keys for the attributes of the Warrant
     */
    public static String TOULMIN_INDEX = "TOULMIN_INDEX",
                         TOULMIN_RELEVANT = "TOULMIN_RELEVANT"; 
    
	/**
	 * XML-RPC keys for the attributes of the Data
	 */
	public static String DATA_PIGNISTIC = "DATA_PIGNISTIC",
                         DATA_CONFIDENCE = "DATA_CONFIDENCE",
                         DATA_DISTRIBUTION = "DATA_DISTRIBUTION",
                         DATA_DISCOUNT = "DATA_DISCOUNT", 
                         DATA_HISTORY = "DATA_HISTORY", 
                         DATA_UNCERTAINTY = "DATA_UNCERTAINTY", 
                         DATA_CONFLICT = "DATA_CONFLICT"; 

	/**
	 * Export the object in XML-RPC compliant data structures.
	 * @return	An XML-RPC compliant data structure representing the TAP element to 
     *          export.
	 */
	Object toXMLRPC();
	
}
