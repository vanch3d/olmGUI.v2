package config;


/**
 * A placeholder for the various preferences of the GUI.
 * 
 * 
 * @author vanlabeke
 * @version $Revision: 1.5 $
 */
public class OLMPrefs 
{

    /**
     * Indicates whether the Trend (History) pane is to be displayed in the GUI.
     * @see OLMTrendPane
     */
    public static boolean SHOWHISTORY = false;
    
    /**
     * Indicates whether the Toulmin graph represents evidence by backings only or 
     * by the combinations warrant/backing.
     */
    public static boolean BACKING_ONLY = true;
    
    /**
     * Indicates whether the challenge is allowed or not in the OLM.
     */
    public static boolean CHALLENGE_ENABLED = true;

}
