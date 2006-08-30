/**
 * @file DialogueMoveStartup.java
 */
package dialogue;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Vector;

import olmgui.i18n.Messages;
import olmgui.utils.OLMSwingWorker;
import config.OLMQueryResult;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.28 $
 *
 */
public class DialogueMoveStartup extends DialogueMove {

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     */
    private final static String STARTUP_USER = "DlgMove.Startup.User", //$NON-NLS-1$
                                STARTUP_LEAM = "DlgMove.Startup.LeAM", //$NON-NLS-1$
                                STARTUP_OLM = "DlgMove.Startup.OLM"; //$NON-NLS-1$
    
    /**
     * SWingWorker used to collect suggested moves from the LM.
     */
    class OLMSwingWorkerSuggest extends OLMSwingWorker
    {

		/**
		 * @param planner
		 */
		public OLMSwingWorkerSuggest(DialoguePlanner planner) 
		{
			super(planner,SUGGEST);
		}

		/* (non-Javadoc)
		 * @see olmgui.utils.OLMSwingWorker#construct()
		 */
		protected Object construct() throws Exception 
		{
			return sendRequestForSuggestion();
		}

		/* (non-Javadoc)
		 * @see olmgui.utils.OLMSwingWorker#finished()
		 */
		protected void finished() 
		{
			super.finished();
			Vector moves = null;
			Object res;
			try 
			{
				res = get();
				if (res instanceof Hashtable) 
				{
					Hashtable tab = (Hashtable)res;
					moves = (Vector) tab.get(OLMQueryResult.CAT_SUGGESTION);
				    if (moves==null || moves.isEmpty()) 
				    	moves = null;
				}
				
				Object[] arg = {
                        getUserName()
                        };      
				if (moves==null)
				{
					// No unresolved issue: leave initiative to learner
					setLogItem(Messages.getRandomString(STARTUP_USER),arg);
					removeAllMoves();
                    addPossibleMove(DlgMoveID.TELLMORE);
	                addPossibleMove(DlgMoveID.SHOWME);
	                addPossibleMove(DlgMoveID.LOST);
	                addPossibleMove(DlgMoveID.QUIT);
	                activateGUI(true);
	                getParent().getJTopicSelector().setFilter(null);

				}
				else
				{
					// Unresolved issue: process as UNRAVEL
                    //setLogItem(WELCOME_OLM_INIT,arg);
                    setLogItem(Messages.getRandomString(STARTUP_OLM),arg);
					removeAllMoves();
                    addPossibleMove(DlgMoveID.TELLMORE);
					addPossibleMove(DlgMoveID.UNRAVEL);
		            activateGUI(true);
                    setMoveData(res);
                    goNextMove(DlgMoveID.UNRAVEL);					
				}
			} 
			catch (InterruptedException e) {e.printStackTrace();} 
			catch (InvocationTargetException e) {e.printStackTrace();}
		}
		
    };
    /*class OLMRequestDescriptors extends OLMRequestCallback
    {
        public void handleResult(Object res, URL url, String handler)
        {
            if (res instanceof Hashtable) 
            {
                Hashtable hash = (Hashtable)res; 
                Vector list = (Vector)hash.get(OLMQueryResult.CAT_BDESCRIPTOR);
                getParent().getJTopicSelector().setDescriptors(list);
            }
        }    	
    };
 
    class OLMRequestCMap extends OLMRequestCallback
    {
    	
 		public void handleError(Exception e, URL url, String handler) {
			
			super.handleError(e, url, handler);
		}

		public void handleResult(Object res, URL url, String handler)
        {
            if (res instanceof Hashtable) 
            {
                Hashtable hash = (Hashtable)res; 
                String map = (String) hash.get(OLMQueryResult.CAT_MAP);
                getParent().getJTopicSelector().setTopicMap(hash,map);
                getParent().getJGraphPanel().setTopicMap(hash,map);
            }
        }    	
    };*/
    
    /**
     * @param planner
     */
    public DialogueMoveStartup(DialoguePlanner planner)
    {
        super(planner,DlgMoveID.STARTUP);
        addPossibleMove(DlgMoveID.QUIT);
        addPossibleMove(DlgMoveID.TELLMORE);
        
        //setPlayerOLM();
    }
   

    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#doMove()
     */
    public DialogueMove onMoveExecute() {
        super.onMoveExecute();

        // Get all the Belief Descriptors from the system
        OLMSwingWorker worker =new OLMSwingWorkerDescript(getPlanner());
        worker.start();

        //String maps[]={
        //        OLMTopicConfig.DOMAIN.toString(),
         //       OLMTopicConfig.COMPET.toString(),
        //        OLMTopicConfig.MOTIV.toString(),
        //        OLMTopicConfig.AFFECT.toString(),
        //        OLMTopicConfig.METACOG.toString(),
        //        OLMTopicConfig.CAPES.toString()
        //    };
        // for (int i=0;i<maps.length;i++)
        //{
        //	 ///sendRequestForCMap(maps[i],new OLMRequestCMap());
        //}
        
        // Set the user name in the Graph Panels
//        if (getParent().getJGraphPanel()!=null)
//            getParent().getJGraphPanel().setTopNode(getUserName()); 
//        if (getParent().getJToulminGraph()!=null)
//            getParent().getJToulminGraph().setTopNode(getUserName()); 
        getParent().setToulminUser();

        if (getBeliefDesc()!=null)
        {
            // A descriptor has been given as am argument of the OLM.
            // Don't check for unresolved issues

            Object[] arg = {
                    getUserName(),
                    getBeliefDesc()
                    };        
            setLogItem(Messages.getRandomString(STARTUP_LEAM),arg);
			removeAllMoves();
            addPossibleMove(DlgMoveID.TELLMORE);
            addPossibleMove(DlgMoveID.SHOWME);
            addPossibleMove(DlgMoveID.LOST);
            addPossibleMove(DlgMoveID.QUIT);
            activateGUI(true);
            setConstructor();
        }
        else
        {
            // Check if there are any unresolved issues
        	OLMSwingWorker worker2= new OLMSwingWorkerSuggest(getPlanner());
        	worker2.start();
        	/*OLMRequestCallback callbackIssue = new OLMRequestCallback()
            {
                public void handleResult(Object res, URL url, String handler)
                {
					Vector moves = null;
					if (res instanceof Hashtable) 
					{
						Hashtable tab = (Hashtable)res;
						moves = (Vector) tab.get(OLMQueryResult.CAT_SUGGESTION);
					    if (moves==null || moves.isEmpty()) 
					    	moves = null;
					}
					
					Object[] arg = {
                            getUserName()
                            };      
					if (moves==null)
					{
						// No unresolved issue: leave initiative to learner
						setLogItem(WELCOME_USER_INIT,arg);
		                addPossibleMove(DlgMoveID.SHOWME);
		                addPossibleMove(DlgMoveID.LOST);
		                addPossibleMove(DlgMoveID.QUIT);
		                activateGUI(true);
					}
					else
					{
						// Unresolved issue: process as UNRAVEL
						setLogItem(WELCOME_OLM_INIT,arg);
						removeAllMoves();
						addPossibleMove(DlgMoveID.UNRAVEL);
			            activateGUI(true);
                        setMoveData(res);
                        goNextMove(DlgMoveID.UNRAVEL);					
					}
						
                	
                }
            };*/
            
         }

        return this;
    }

    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#goNextMove(org.activemath.xlm.openmodel.dialogue.DlgMoveID)
     */
    public DialogueMove goNextMove(DlgMoveID moveid) 
    {
        DialogueMove next = super.goNextMove(moveid);
        if (next!=null)
            next.onMoveExecute();
        return next;
    }

    /* (non-Javadoc)
     * @see dialogue.DialogueMove#getLogParams()
     */
    public Hashtable getLogParams() {
        Hashtable params = super.getLogParams();
        return params;
    }
    
    
    

}
