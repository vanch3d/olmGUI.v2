/**
 * @file DialogueMoveUnravel.java
 */
package dialogue;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Vector;

import olmgui.OLMMainGUI;
import olmgui.i18n.Messages;
import olmgui.utils.OLMSwingWorker;
import olmgui.utils.TopicListModel;
import olmgui.utils.TopicWrapper;
import toulmin.BeliefDesc;
import config.OLMQueryResult;
import config.OLMTopicConfig;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.24 $
 *
 */
public class DialogueMoveUnravel extends DialogueMove {

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     */
    private final static String UNRAVEL_URGE = "DlgMove.Unravel.Urge",  //$NON-NLS-1$
                                UNRAVEL_SUGGEST = "DlgMove.Unravel.Suggest",  //$NON-NLS-1$
                                UNRAVEL_NOIDEA = "DlgMove.Unravel.NoIdea";  //$NON-NLS-1$
    
    /**
     * The suggestion for the next move (one of the {@link DlgMoveID} identifier).
     */
    private String nextMove = null;
        
    /**
     * Default constructor
     * @param parent    A reference to the previous dialogue move
     */
    public DialogueMoveUnravel(DialogueMove parent) {
        super(parent, DlgMoveID.UNRAVEL);

        //addPossibleMove(DlgMoveID.SHOWME);
        //addPossibleMove(DlgMoveID.LOST);
    }

    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#doMove()
     */
    public DialogueMove onMoveExecute() {
        super.onMoveExecute();
        
        Hashtable table = (Hashtable)getMoveData();
        if (table==null || table.get(OLMQueryResult.CAT_SUGGESTION)==null)
        {
            // Retrieve the possible next moves from the server
            setMoveData(null);
            
            // Get the Belief Descriptor from the system
           /* OLMRequestCallback callback = new OLMRequestCallback()
            {
                public void handleResult(Object res, URL url, String handler)
                {
                    if (res instanceof Hashtable) 
                    {
                    	setMoveData((Hashtable)res);
                    	proposeMove();
                   }
                }
            };*/
            
            OLMSwingWorker worker =new OLMSwingWorker(getPlanner(),OLMSwingWorker.SUGGEST)
            {

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
					
					try 
					{
						Object res = get();
						if (res instanceof Hashtable) 
	                    {
	                    	setMoveData((Hashtable)res);
	                    	proposeMove();
	                   }
					} 
					catch (InterruptedException e) {e.printStackTrace();}
					catch (InvocationTargetException e) {e.printStackTrace();}
				}
            	
            };
            worker.start();
            
        }
        else
        {
        	proposeMove();
        }
/*
        else{
        // moveData contains, we came back from disagree
        // so just take the first item - if any - in the list
        table = (Hashtable)getMoveData();
        nextMove = (String)table.get(OLMQueryResult.CAT_NEXTMOVE);
        Vector moves = (Vector)table.get(OLMQueryResult.CAT_SUGGESTION);
        
        if (moves.isEmpty())
        {
            setBeliefDesc(null);
        }
        else
        {
            Hashtable move = (Hashtable)moves.get(0);
            setBeliefDesc((Vector)move.get(OLMQueryResult.MOVE_DESCRIPTOR));
        }


        }}*/
        return this;
    }
    
    /**
     * 
     */
    private void proposeMove()
    {
    	Hashtable table = (Hashtable)getMoveData();
        nextMove = (String)table.get(OLMQueryResult.CAT_NEXTMOVE);
        Vector moves = (Vector)table.get(OLMQueryResult.CAT_SUGGESTION);
        if (moves.isEmpty())
        {
            setBeliefDesc(null);
        }
        else
        {
            Hashtable move = (Hashtable)moves.get(0);
            setBeliefDesc(new BeliefDesc((Vector)move.get(OLMQueryResult.MOVE_DESCRIPTOR)));
        }
        // Show the Descriptor View and build the belief suggested
        getParent().activateView(OLMMainGUI.VIEW_DESCRIPTOR);
        
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

        
        if (getBeliefDesc()==null)
        {

            setLogItem(Messages.getRandomString(UNRAVEL_NOIDEA),null);
            addPossibleMove(DlgMoveID.SHOWME);
            activateGUI(true);

        }
        else
        {
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
            }
            //bdesc = sParent.getJBeliefConstructor().getBeliefDescriptor();
            getParent().getJTopicSelector().setFilter(getBeliefDesc());

//            for (int i=0;i<getBeliefDesc().size();i++)
//            {
//                String id = (String)getBeliefDesc().get(i); 
//                if (id!=null && !id.equals(""))
//                    getParent().getJBeliefConstructor().setLayerAttribute(cfg[i],new TopicWrapper(id));
//            }

            Object[] arg = {
                    nextMove,
                    getBeliefDesc()
                    };

            if ( this.getLastMove() instanceof DialogueMoveStartup)
            {
                // If it follows STARTUP, then strongly recommend the action
                setLogItem(Messages.getRandomString(UNRAVEL_URGE),arg);
            }
            else
                setLogItem(Messages.getRandomString(UNRAVEL_SUGGEST),arg);
            
            removeAllMoves();
            addPossibleMove(DlgMoveID.AGREE);
            addPossibleMove(DlgMoveID.DISAGREE);
            activateGUI(true);
        }
        getParent().getJTopicSelector().setFilter(getBeliefDesc());

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
