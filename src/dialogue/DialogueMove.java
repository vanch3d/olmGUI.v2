/**
 * @file DialogueMove.java
 */
package dialogue;


import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import olmgui.OLMMainGUI;
import olmgui.i18n.Messages;
import olmgui.utils.OLMSwingWorker;
import olmgui.utils.TopicListModel;
import olmgui.utils.TopicWrapper;

import org.apache.xmlrpc.applet.SimpleXmlRpcClient;
import org.apache.xmlrpc.applet.XmlRpcException;

import toulmin.BeliefDesc;
import toulmin.Toulmin;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import config.OLMQueryResult;
import config.OLMServerConfig;
import config.OLMTopicConfig;



/**
 * This class implements a generic dialogue move that both the learner and the OLM use
 * to communicate and negotiate about learners' abilities.
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.41 $
 * 
 * @see dialogue.DialogueMoveAgree
 * @see dialogue.DialogueMoveBaffled
 * @see dialogue.DialogueMoveHereIs
 * @see dialogue.DialogueMovePerhaps
 * @see dialogue.DialogueMoveShowMe
 * @see dialogue.DialogueMoveStartup
 * @see dialogue.DialogueMoveUnravel
 * @see dialogue.DialogueMoveWindUp
 *  
 * @see dialogue.DialoguePlanner
 *
 */
abstract public class DialogueMove {

    /**
     * Shortcut for the String containing the name of the OLM.
     * Used by the OLM to identify itself as a player of dialogue move.
     */
    protected String OLMUSER = Messages.getString("OLMConfig.OLMUSER"); //$NON-NLS-1$
    
    /**
     * A reference to the dialogue planner.
     */
    private DialoguePlanner planner = null;

    /**
     * A reference to the preceding dialogue move.
     */
    private DialogueMove    lastMove = null;
    
    /**
     * The ID of this dialogue move.
     */
    private DlgMoveID       moveID = null;
    
    private Toulmin         toulmin = null;

    /**
     * Contains all the moves that could follow this one.
     * Note that depending on the execution of this move, the set could change over time.
     * 
     * @see #addPossibleMove(DlgMoveID)
     * @see #removePossibleMove(DlgMoveID)
     * @see #isMovePossible(DlgMoveID)
     */
    private Set             filter;

    /**
     * The name of the player of this dialogue move.
     * It will be either the name of the learner or that of the OLM (as stored in OLMUSER).
     */
    private String          player;
    
    /**
     * The belief descriptor, target of the learners' inquiry.
     */
    private BeliefDesc          beliefDesc = null;
    
    /**
     * Generic storage for the data associated with the dialogue move.
     * The nature of the data will depend from one move to another one.
     * The data are passed from move to move until explicit re-initialisation.
     */
    private Object          moveData = null;

    /**
     * A queue for threads used for logging the dialogue moves
     */
    private QueuedExecutor  exec = null;
      
    
    /**
     * SwingWorker used to collect all Belief Descriptor from the LM.
     *
     */
    class OLMSwingWorkerDescript extends OLMSwingWorker
    {

		/**
         * Default constructor
		 * @param planner A reference to the main dialogue planner.
		 */
		public OLMSwingWorkerDescript(DialoguePlanner planner)
		{
			super(planner,BDESC);
		}
		
		/* (non-Javadoc)
		 * @see olmgui.utils.OLMSwingWorker#construct()
		 */
		protected Object construct() throws Exception 
		{
			return sendRequestForDescriptor();
		}

		/* (non-Javadoc)
		 * @see olmgui.utils.OLMSwingWorker#finished()
		 */
		protected void finished()
		{
			super.finished();
			try {
				Hashtable hash = (Hashtable)get();
                Vector list = (Vector)hash.get(OLMQueryResult.CAT_BDESCRIPTOR);
                getParent().getJTopicSelector().setDescriptors(list);
                getParent().getJTopicSelector().setFilter(null);
			} 
			//catch (InterruptedException e) {e.printStackTrace();} 
			//catch (InvocationTargetException e) {e.printStackTrace();} 
			catch (Exception e) 
			{
				//System.err.println("Server not available - using dummy data instead.");
			} 
 		}   	
    }
        
    /**
     * Construct an instance of a dialogue move, referring the dialogue planner used
     * in the OLM GUI (see DialoguePlanner) and the identifier of the move (see DlgMoveID).
     * @param planner   A reference to the dialogue planner.
     * @param move      A reference to the move to implement.
     */
    public DialogueMove(DialoguePlanner planner,DlgMoveID move)
    {
        this.exec = new QueuedExecutor();
        this.beliefDesc = null;
        this.moveData = null;
        this.toulmin = null;
        this.moveID = move;
        this.planner = planner;
        if (move.isOLM())
            setPlayerOLM();
        else
            setPlayer(getUserName());
    }

    /**
     * Construct an instance of a dialogue move, referring to the previous move
     * and the identifier of this new move.
     * @param prevmove  A reference to the previous dialogue move.
     * @param move      A reference to the identifier of the move to implement.
     */
    public DialogueMove(DialogueMove prevmove,DlgMoveID move)
    {
        this.exec = prevmove.exec;
        this.moveID = move;
        this.planner = prevmove.planner;
        this.moveData = prevmove.moveData;
        this.beliefDesc = prevmove.beliefDesc;
        this.toulmin = prevmove.toulmin;
        if (move.isOLM())
            setPlayerOLM();
        else
            setPlayer(getUserName());
    }
    
    /**
     * This method is called to execute this dialogue move.
     * @return  A reference to the active dialogue move, after execution of this move.
     */
    public DialogueMove onMoveExecute()
    {
       sendLogMove();
       return this;
    }
    
    /**
     * This method is called when this move is completed, ie before moving to the 
     * next one. 
     */
    public void onMoveExit()
    {
        //if (filter!=null)
        //    System.out.println("Clean moves : " + filter.toString());//$NON-NLS-1$
        activateGUI(false);
    }
    
    /**
     * This method is called when this move is initialised.
     */
    public void onMoveEntry()
    {
        //System.out.println("*** Current move : " + moveID.toString());//$NON-NLS-1$
        //if (filter!=null)
        //    System.out.println("Prepare moves : " + filter.toString());//$NON-NLS-1$ 
        activateGUI(true);
    }

    /**
     * This method is called when the execution of this move has failed.
     */
    public void onMoveFail()
    {
        if (filter!=null)
        {
            //System.out.println("Clean moves : " + filter.toString());//$NON-NLS-1$
            filter.clear();
        }
        activateGUI(false);
    }
    

    /**
     * This method is called to initiate the next dialogue move.
     * @param moveid    The ID of the next dialogue move to initialise.  
     * @return          A reference to the next dialogue move, null if none.
     */
    public DialogueMove goNextMove(DlgMoveID moveid)
    {
        
        if (!isMovePossible(moveid))
        {
            //System.out.println("(Dialogue Move) this move is not allowed.");//$NON-NLS-1$
            return null;
        }
        
        onMoveExit();
        DialogueMove move = null;
        if (moveid==DlgMoveID.STARTUP) move = new DialogueMoveStartup(this.planner);
        if (moveid==DlgMoveID.SHOWME) move = new DialogueMoveShowMe(this);
        if (moveid==DlgMoveID.PERHAPS) move = new DialogueMovePerhaps(this);
        if (moveid==DlgMoveID.LOST) move = new DialogueMoveBaffled(this,moveid);
        if (moveid==DlgMoveID.BAFFLED) move = new DialogueMoveBaffled(this,moveid);
        if (moveid==DlgMoveID.HEREIS) move = new DialogueMoveHereIs(this);
        if (moveid==DlgMoveID.UNRAVEL) move = new DialogueMoveUnravel(this);
        if (moveid==DlgMoveID.AGREE) move = new DialogueMoveAgree(this,moveid);
        if (moveid==DlgMoveID.DISAGREE) move = new DialogueMoveAgree(this,moveid);
        if (moveid==DlgMoveID.MOVEON) move = new DialogueMoveAgree(this,moveid);
        if (moveid==DlgMoveID.WINDUP) move = new DialogueMoveWindUp(this);
        if (moveid==DlgMoveID.QUIT) move = new DialogueMoveQuit(this);
        if (move!=null)
        {
            move.setLastMove(this);
            this.planner.setCurrentState(move);
            move.onMoveEntry();
        }
        else
            activateGUI(true);
        return move;
    }
    
    /**
     * Return the name of the player of this move.
     * @return  A string containing the name of player of this move.
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Set the name of the player of this move.
     * @param player    The name of the player to set.
     */
    public void setPlayer(String player) {
        this.player = player;
    }
    
    /**
     * Set the OLM as the player of this move.
     */
    public void setPlayerOLM() {
        this.player = OLMUSER;
    }  
    
    /**
     * Return the name of the learner currently using the OLM.
     * @return  The name of the learner, null if none
     * @see     OLMMainGUI#getUserName()
     */
    public String getUserName() {
        //return this.parent.getUserName();
        return this.planner.getUserName();
    }
    
    /**
     * Set the belief descriptor associated with this move.
     * @param desc  The belief descriptor to associate, null to re-initialise.
     */
    public void setBeliefDesc(BeliefDesc desc) 
    {
        this.beliefDesc = desc;
    }
    
    /**
     * Return the belief descriptor associated with this move.
     * @return  A reference to the belief descriptor, null is none.
     */
    public BeliefDesc getBeliefDesc()
    {
        return this.beliefDesc;
    }
    
    /**
     * Return a reference to the main component of the OLM GUI.
     * @return  A reference to the main applet.
     */
    public OLMMainGUI getParent() {
        return this.planner.getMainGUI();
    }
    
    
    
    /**
	 * @return Returns the planner.
	 */
	public DialoguePlanner getPlanner() {
		return planner;
	}

	/**
     * Return the previous dialogue move played.
     * @return  A reference to the previous dialogue move, null if none.
     */
    public DialogueMove getLastMove() {
        return this.lastMove;
    }
    
    /**
     * Set the last move played.
     * @param move  A reference to the last dialogue move played, null if none.
     */
    public void setLastMove(DialogueMove move)
    {
        this.lastMove = move;
    }
    
    /**
     * Return the data associated with this dialogue move.
     * Remember that the nature of the data will change from one move to another one.
     * @return  A reference to the object containing the data, null if none. 
     */
    public Object getMoveData() {
        return moveData;
    }

    /**
     * Associate some data with this dialogue move.
     * 
     * @param moveData  A reference to the object containing the data to associate, 
     *                  null to re-initialise (ie empty) it.
     */
    public void setMoveData(Object moveData) {
        this.moveData = moveData;
    }

    /**
     * Return the identifier of this dialogue move.
     * @return The ID of the dialogue move.
     */
    public DlgMoveID getMoveID() {
        return this.moveID;
    }
    
    
    
    public Toulmin getToulmin() 
    {
        return toulmin;
    }

    public void setToulmin(Toulmin toulmin) 
    {
        this.toulmin = toulmin;
    }

    /**
     * Return a log describing this dialogue move.
     * Consists in a list of attribute/value containing the important elements of the 
     * dialogue move, such as the name of the user, if the move is played by the OLM 
     * or the user, the identifier of the move, etc.
     * @return  A Hashtable containing all the attribute/value pairs describing this move.
     */
    public Hashtable getLogParams()
    {
        Hashtable params = new Hashtable();
        params.put(OLMQueryResult.LOG_USER,getUserName());
        params.put(OLMQueryResult.LOG_ISOLM,new Boolean(getMoveID().isOLM()));
        params.put(OLMQueryResult.LOG_MOVEID,getMoveID().toString());
        if (this.beliefDesc!=null) 
            params.put(OLMQueryResult.LOG_BELIEF,this.beliefDesc.toXMLRPC());
        return params;
    }
    
    /**
     * Add a move in the list of candidates for the next possible dialogue move. 
     * @param move  The identifier of the move to add.
     */
    public void addPossibleMove(DlgMoveID move)
    {
        if (filter==null)
            filter = new HashSet();
        filter.add(move);
    }
    
    /**
     * Get all the candidates for the next possible dialogue move.
     * @return The set of all the next possible dialogue move.
     */
    public Set getPossibleMoves()
    {
        return filter;
    }
    
    /**
     * Remove the move from the list of candidates for the next possible dialogue move.
     * @param move  The identifier of the move to remove.
     */
    public void removePossibleMove(DlgMoveID move)
    {
        if (filter!=null)
        {
            filter.remove(move);
        }
    }

    /**
     * Remove all the move from the list of candidates for the next possible dialogue move.
     */
    public void removeAllMoves()
    {
        if (filter!=null)
        {
            filter.clear();
        }
    }
   
    /**
     * Check if a move is among the candidates for the next possible dialogue move.
     * @param move  The identifier of the move to verify.
     * @return      <code>true</code> if the move is allowed, 
     *              <code>false</code> otherwise.
     */
    public boolean isMovePossible(DlgMoveID move)
    {
        if (filter==null)
            return false;
        else
            return filter.contains(move);
    }
    
    /**
     * Specify the outcome of the move (template and argument) to be displayed in the dialogue pane.
     * @param template  The string template that describes the outcome of the current move.
     * @param args      An array of objects that contains the arguments to be inserted in the template.
     */
    public void setLogItem(String template,Object[] args)
    {
       getParent().updateDialogueMove(getPlayer(),template,args);
    }

    /**
     * Enable or disable the interface widgets, depending on the possible next moves.
     * @param activ TRUE if the interface is enabled (ie on move entry), 
     *              FALSE if the interface is disabled (ie on move exit).
     *              
     * @todo This approach is not really good; do the check in the Dialogue Planner,
     *       by taking into account ALL the necessary widgets              
     */
    public void activateGUI(boolean activ)
    {
        
        if (filter==null || planner==null) return;
        planner.activateGUI(activ);
//        getParent().enableAllMoves(false);
//        for (Iterator ite = filter.iterator();ite.hasNext();)
//        {
//            DlgMoveID mov = (DlgMoveID)ite.next();
//            getParent().enableMove(mov,activ);
//        }
//        getParent().enableFlipViews(activ);
    }
    
    
    /**
     * Initialise the Belief Constructor View with the belief descriptor stored in the 
     * dialogue move.
     * 
     */
    public void setConstructor()
    {
    	OLMTopicConfig cfg[] = 
        {
                OLMTopicConfig.DOMAIN,
                OLMTopicConfig.CAPES, 
                OLMTopicConfig.COMPET, 
                OLMTopicConfig.MOTIV, 
                OLMTopicConfig.AFFECT, 
                OLMTopicConfig.METACOG 
        };

        // put the empty descriptor in place
        for (int i=0;i<cfg.length;i++)
            getParent().setLayerAttribute(cfg[i],null);

        for (int i=0;i<getBeliefDesc().size();i++)
        {
            String id = (String)getBeliefDesc().get(i);
            TopicListModel topicModel = cfg[i].getModel();
            if (topicModel==null) continue;
            Object obj2 = topicModel.findElement(id);
            if (obj2 instanceof TopicWrapper) {
                TopicWrapper wrap = (TopicWrapper) obj2;
                getParent().setLayerAttribute(cfg[i], wrap);
            }
            else
            {
            	TopicWrapper wrap = new TopicWrapper(id,id,id);
            	getParent().setLayerAttribute(cfg[i], wrap);
            }
        }
        //bdesc = sParent.getJBeliefConstructor().getBeliefDescriptor();
        getParent().getJTopicSelector().setFilter(getBeliefDesc());

//        for (int i=0;i<getBeliefDesc().size();i++)
//        {
//            String id = (String)getBeliefDesc().get(i); 
//            if (id!=null && !id.equals(""))
//                getParent().getJBeliefConstructor().setLayerAttribute(cfg[i],new TopicWrapper(id));
//            addPossibleMove(DlgMoveID.SHOWME);
//            activateGUI(true);
//        }
    }
    /**
     * This method is called to retrieve - from the OLM-Core - a suggestion about what 
     * the learner should do now.
     * @return A Hashtable containing the next move to play (OLMQueryResult.CAT_NEXTMOVE)
     *         and a Vector containing the suggested belief descriptors 
     *         (OLMQueryResult.CAT_SUGGESTION). 
     * @throws IOException 
     * @throws XmlRpcException 
     * @see dialogue.DialogueMoveStartup.OLMSwingWorkerSuggest
     */

    protected Object sendRequestForSuggestion() throws XmlRpcException, IOException
    {
    	SimpleXmlRpcClient client = getParent().initClient();

        final Vector params = new Vector();
        params.addElement(new String(getUserName()));
        return client.execute(OLMServerConfig.HDR_NEXTMOVE, params);
    }
    


    /**
     * This method is called to retrieve a complete belief from the OLM-Core. 
     * @param bDesc     The belief descriptor identifying the belief to retrieve.
     * @throws IOException 
     * @throws XmlRpcException 
     */
    protected Object sendRequestForBelief(BeliefDesc bDesc) throws Exception
    {
    	//System.out.println("Get Justification for " + bDesc); //$NON-NLS-1$
        //activateGUI(false);
        //getParent().getJContentPane().updateUI();
        //getParent().startProgress();
        
        SimpleXmlRpcClient client = getParent().initClient();
        
        Vector params = new Vector();
        params.addElement(new String(getUserName()));
        params.addElement(new Vector(bDesc));
        params.addElement(new String("ATTRIBUTE.PERFORMANCE"));//$NON-NLS-1$
        //params.addElement(new String(""));
        
        return client.execute(OLMServerConfig.HDR_JUDGMENT, params);
    }
    
 
 
    /**
     * @return A Hashtable containing (OLMQueryResult.CAT_BDESCRIPTOR) a Vector 
     *         holding all the belief descriptors in the Learner Model.
     *         
     * @throws XmlRpcException
     * @throws IOException
     * 
     * @see OLMSwingWorkerDescript
     */
    protected Object sendRequestForDescriptor() throws Exception
    {
    	SimpleXmlRpcClient client = getParent().initClient();
        Vector params = new Vector();
        return client.execute(OLMServerConfig.HDR_DESCRIPTORS, params);
    }
    
    /**
     * @param map   The identifier of the topic map to retrieve.
     * @return The list of all topics in the given map.
     * @throws XmlRpcException
     * @throws IOException
     * @deprecated  Not in use anymore; use the similar method in {@link OLMMainGUI}.
     */
    protected Object sendRequestForCMap(String map) throws XmlRpcException, IOException, Exception
    {
    	SimpleXmlRpcClient client = getParent().initClient();
        Vector params = new Vector();
        params.addElement(new String(map));
        return client.execute(OLMServerConfig.HDR_CONCEPTMAP, params);
    }
    
    
    /**
     * This method is called to send the move's log to the OLM-Core.
     * @todo I'm now using a {@link QueuedExecutor} to get the moves in the right
     *       order; need to check if there is a better solution.
     * @todo QueuedExecutor seems to be messing around. Until I can think of a better 
     *       solution, I'm just removing the separate thread and run it in the main.
     */
    protected void sendLogMove()
    {
        //Hashtable params = getLogParams();//new Vector();
        //Vector vec = new Vector();
        //vec.add(params);

        //XmlRpcClient client = getParent().initClient();
        //client.executeAsync(OLMServerConfig.HDR_LOGMOVE, vec,null);
        
        if (exec==null) exec = new QueuedExecutor();
        try 
        {
			exec.execute(new Runnable()
	        {
	            public void run()  
	            {
	                Hashtable params = getLogParams();//new Vector();
	                Vector vec = new Vector();
	                vec.add(params);
	                SimpleXmlRpcClient client = getParent().initClient();
					try 
					{
						client.execute(OLMServerConfig.HDR_LOGMOVE, vec);
					} 
					catch (XmlRpcException e) {e.printStackTrace();} 
					catch (IOException e) {e.printStackTrace();}
					catch (Exception e) {}
	            }
	        });
		} 
        catch (InterruptedException e) {e.printStackTrace();}
        
//        OLMSwingWorker worker = new OLMSwingWorker(getPlanner(),OLMSwingWorker.BELIEF)
//        {
//            /* (non-Javadoc)
//             * @see olmgui.utils.OLMSwingWorker#construct()
//             */
//            protected Object construct() throws Exception
//            {
//                Hashtable params = getLogParams();
//                Vector vec = new Vector();
//                vec.add(params);
//                SimpleXmlRpcClient client = getParent().initClient();
//                
//                return client.execute(OLMServerConfig.HDR_LOGMOVE, vec);
//            }
//
//            protected void finished() 
//            {
//                super.finished();
//            }
//        };
//        worker.start();
    }    
    
}
