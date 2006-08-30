/**
 * @file OLMServerConfig.java
 */
package config;

/**
 * This class contains shortcuts for the name of the handlers used for the XML-RPC communication.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.11 $
 * 
 */
public class OLMServerConfig 
{
    /**
     * Name of the handler for requesting the concept maps for the OLM.
     */
    public static final String HDR_CONCEPTMAP = "getConceptMap";        //$NON-NLS-1$

    /**
     * Name of the handler for requesting judgement on a belief.
     */
    public static final String HDR_JUDGMENT = "getJudgmentOn";          //$NON-NLS-1$

    /**
     * Name of the handler for requesting a suggestion for the next belief to look at.
     * @deprecated
     */
    public static final String HDR_SUGGESTION = "getActivitySuggestion";    //$NON-NLS-1$

    /**
     * Name of the handler for requesting a suggestion about the next move to perform.
     */
    public static final String HDR_NEXTMOVE = "getNextDialogueMove";        //$NON-NLS-1$

    /**
     * Name of the handler for logging the current move in LeAM.
     */
    public static final String HDR_LOGMOVE = "logMove"; //$NON-NLS-1$                 

    /**
     * Name of the handler for requesting the ID of the learner.
     */
    public static final String HDR_GETUSERINFO = "getUserInfo"; //$NON-NLS-1$


    /**
     * Name of the handler for requesting all the existing Belief Descriptor
     */
    public static final String HDR_DESCRIPTORS = "getAllDescriptors";   //$NON-NLS-1$
}

