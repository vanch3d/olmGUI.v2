/**
 */
package config;

/**
 * This class contains the various keys used to communicate data between XLM and the 
 * OLM GUI (by XML-RPC).
 * 
 * Data are basically exchanged by mean of an Hashtable, containing several key/value 
 * pairs. The keys used in the Hashtable, and the value attached to them, are briefly 
 * described thereafter; for a more cogent description of relevant data structures, 
 * check the appropriate class. 
 * 
 * <ul>
 * 
 * <LI>CAT_ERROR: Only appears if an error occurred when proceeding the request on the server side. 
 * This key is associated with the identifier of the error (see {@link config.DlgErrorMsg}).
 * As it indicates a serious problem, this key replaces all other keys usually expected by the 
 * request; it is therefore recommended to always check for the existence of the key.
 * 
 * <LI>CAT_WARNING: Only appears if the request cannot proceed because of a user-initiated mistake.
 * This key is associated with a String indicating the mistake. 
 * 
 * <LI>CAT_TOULMIN:
 * 
 * <LI>CAT_MAP: 
 *  
 * 
 * </ul>
 * 
 * @todo content of the Hashtable have evolved a lot but not their description above; need to update it. 
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.24 $
 */
public class OLMQueryResult //extends Hashtable
{
    /// @{ 
    /**
     * Labels used for the keys of the Hashtable.
     */
    public static final String CAT_ERROR = "ERROR",             //$NON-NLS-1$ ///< Associated with an error identifier.
                               CAT_WARNING = "WARNING",         //$NON-NLS-1$ ///< Associated with an error identifier.
                               CAT_TOULMIN = "TOULMIN",         //$NON-NLS-1$ ///< Associated with a Toulmin Argumentation Pattern.
                               CAT_NEXTMOVE = "NEXTMOVE",       //$NON-NLS-1$ ///< Associated with a Toulmin Argumentation Pattern.
                               CAT_SUGGESTION = "SUGGESTION",   //$NON-NLS-1$ ///< List of "next move" suggestions.
                               CAT_BDESCRIPTOR = "BDESCRIPTOR"; //$NON-NLS-1$ ///< List of belief descriptors.
    ///  @}

    ///  @{ 
    /**
     * Labels used for the retrieval of topic maps.
     * @see olmgui.OLMMainGUI#sendRequestForCMaps()
     * @see olmgui.graph.OLMGraphBrowser#setTopicMap(Hashtable, String)
     * @see olmgui.input.OLMTopicSelector#setTopicMap(Hashtable, String)
     */
    public static final String  CAT_NODES = "NODES",    //$NON-NLS-1$ ///< List of all nodes in the map.
                                CAT_EDGES = "EDGES",    //$NON-NLS-1$ ///< List of all edges in the map.
                                CAT_ROOT = "ROOT",      //$NON-NLS-1$ ///< Identifier of the top-level node.
                                CAT_MAP = "TOPICMAP";   //$NON-NLS-1$ ///< Identifier of the Topic Map (see OLMTopicConfig). 
    ///  @}

    ///  @{ 
    /**
     * Labels used for the retrieval of user information.
     * @see olmgui.OLMMainGUI#initUser()
     */
    public static final String CAT_USERID = "USERID",           //$NON-NLS-1$ ///< Identifier of the user.
                               CAT_USERNAME = "USERNAME",       //$NON-NLS-1$ ///< Name of the user.
                               CAT_USERLOCALE = "USERLOCALE";   //$NON-NLS-1$ ///< Language used by the user.
    ///  @}

    ///  @{ 
    /**
     * Labels used for the retrieval of next move suggestions
     * @see dialogue.DialogueMoveStartup
     * @see dialogue.DialogueMoveUnravel#proposeMove()
     */
    public static final String MOVE_DESCRIPTOR = "DESCRIPTOR",  //$NON-NLS-1$
                               MOVE_UNRESOLVED = "UNRESOLVED";  //$NON-NLS-1$
    
    ///  @}
    
    ///  @{ 
    /**
     * Labels used for the retrieval of events
     * @see toulmin.ToulminBacking
     * @see toulmin.ToulminAttribute
     */
    public static final String EVT_XFINISHED = "ExerciseFinished",      //$NON-NLS-1$
                               EVT_XSTEP = "ExerciseStep",              //$NON-NLS-1$
                               EVT_CHALLENGE = "OLMChallenge",          //$NON-NLS-1$    
                               EVT_METACOG = "OLMMetacog",              //$NON-NLS-1$
                               EVT_SELFREPORT = "SelfReport",           //$NON-NLS-1$
                               EVT_SITFACT = "SituationFactorChanged";  //$NON-NLS-1$
    ///  @}


    ///  @{ 
    /**
     * Labels used for the retrieval of evidence
     * @see olmgui.output.OLMWarrantPane#setEvidence(String, ToulminWarrant)
     * @see toulmin.ToulminBacking
     * @see toulmin.ToulminAttribute
     */
    public static final String EVIDENCE_XXX = "_UNKNOWN",                   //$NON-NLS-1$
                               EVIDENCE_ITEM = "ITEM",                      //$NON-NLS-1$
                               EVIDENCE_DIFF = "DIFFICULTY",                //$NON-NLS-1$
                               EVIDENCE_PERF = "PERFORMANCE",               //$NON-NLS-1$
                               EVIDENCE_COMPLVL = "COMPETLEVEL",            //$NON-NLS-1$
                               EVIDENCE_MISCONCEPTION = "MISCONCEPTION",    //$NON-NLS-1$
                               EVIDENCE_USERINPUT = "USERINPUT",            //$NON-NLS-1$
                               EVIDENCE_FOCUS = "FOCUS",                    //$NON-NLS-1$
                               EVIDENCE_RELATED = "RELATED",                //$NON-NLS-1$
                               EVIDENCE_TITLE = "TITLE",                    //$NON-NLS-1$
                               EVIDENCE_TYPE = "TYPE",                      //$NON-NLS-1$
                               EVIDENCE_ACTION = "ACTION",                  //$NON-NLS-1$
                               EVIDENCE_INDEX = "INDEX",                    //$NON-NLS-1$
                               EVIDENCE_RELEVANCE = "RELEVANCE",            //$NON-NLS-1$    
                               EVIDENCE_DIRECT = "DIRECT",                  //$NON-NLS-1$
                               EVIDENCE_LIKING = "LIKING",                  //$NON-NLS-1$
                               EVIDENCE_PRIDE = "PRIDE",                    //$NON-NLS-1$
                               EVIDENCE_SATISFACTION = "SATISFACTION",      //$NON-NLS-1$
                               EVIDENCE_APTITUDE = "APTITUDE",              //$NON-NLS-1$
                               EVIDENCE_INTEREST = "INTEREST",              //$NON-NLS-1$
                               EVIDENCE_CONFIDENCE = "CONFIDENCE",          //$NON-NLS-1$
                               EVIDENCE_EFFORT = "EFFORT",                  //$NON-NLS-1$
                               EVIDENCE_DLGMOVE = "DLGMOVE",                //$NON-NLS-1$
                               EVIDENCE_DEPTH = "DEPTH",                    //$NON-NLS-1$
                               EVIDENCE_INITIATIVE = "INITIATIVE",          //$NON-NLS-1$
                               EVIDENCE_BDESCRIPT = "BDESCRIPT",            //$NON-NLS-1$
                               EVIDENCE_CHLGCONFID = "CHLGCONFID",          //$NON-NLS-1$
                               EVIDENCE_CHLGINTRAN = "CHLGINTRAN",          //$NON-NLS-1$
                               EVIDENCE_CHLGLEVEL = "CHLGLEVEL",            //$NON-NLS-1$
                               EVIDENCE_CHLGOLDLEVEL = "CHLGOLDLEVEL",      //$NON-NLS-1$     
                               EVIDENCE_COMPETENCY = "COMPETENCY",          //$NON-NLS-1$
                               EVIDENCE_TRUSTABILITY = "TRUSTABILITY";      //$NON-NLS-1$     
    ///  @}

    ///  @{ 
    /**
     * Labels used for logging dialogue Moves
     * @see dialogue.DialogueMove#getLogParams()
     */
    public static final String LOG_USER = "USER",               //$NON-NLS-1$
                               LOG_ISOLM = "ISOLM",             //$NON-NLS-1$
                               LOG_MOVEID = "MOVE",             //$NON-NLS-1$
                               LOG_BELIEF = "BELIEF",           //$NON-NLS-1$
                               LOG_TARGET = "TARGET",           //$NON-NLS-1$
                               LOG_OUTCOME = "OUTCOME",         //$NON-NLS-1$
                               LOG_CHALLENGE = "CHALLENGE";     //$NON-NLS-1$
    ///  @}
   
    ///  @{ 
    /**
     * Labels used for challenging OLM judgments
     * @see olmgui.input.OLMChallengePane#getOutcome()
     */
    public static final String CHALLENGE_TYPE = "CHALLENGE",            //$NON-NLS-1$
                               CHALLENGE_CLAIM = "CHALLENGE_CLAIM",     //$NON-NLS-1$
                               CHALLENGE_ATTRIB = "CHALLENGE_ATTRIB",   //$NON-NLS-1$
                               CHALLENGE_EVID = "CHALLENGE_EVID",       //$NON-NLS-1$
                               CHALLENGE_LEVEL = "LEVEL",               //$NON-NLS-1$
                               CHALLENGE_OLDLEVEL = "OLDLEVEL",         //$NON-NLS-1$
                               CHALLENGE_CONFIDENCE = "CONFIDENCE",     //$NON-NLS-1$
                               CHALLENGE_INTRANSIG = "INTRANSIGENCE";   //$NON-NLS-1$      
    ///  @}
  
}
