/**
 * @file DialogueMoveAgree.java
 */
package dialogue;

import java.util.Hashtable;
import java.util.Vector;

import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import toulmin.Toulmin;
import config.OLMQueryResult;

/**
 * This class implements a dialogue move used by the learner to indicate agreement 
 * or disagreement with a statement (or a proposition) made by the OLM.
 * 
 * This move is used for dealing with both challenges (agreeing or disagreeing on a 
 * Toulmin Argumentation Pattern element) and suggestions from the OLM (exploring a 
 * particular belief, stopping the discussion).  
 * 
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.30 $
 *
 */
public class DialogueMoveAgree extends DialogueMove {

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     */
    private final static String ANSWER_AGREE = "DlgMove.Agree.Agree", //$NON-NLS-1$
                                ANSWER_DISAGREE = "DlgMove.Agree.Disagree", //$NON-NLS-1$
                                ANSWER_MOVEON = "DlgMove.Agree.MoveOn", //$NON-NLS-1$
                                ANSWER_ACCEPT = "DlgMove.Agree.Accept", //$NON-NLS-1$
                                ANSWER_REJECT = "DlgMove.Agree.Reject"; //$NON-NLS-1$
    
    
    
    //private static final String TARGET = "<TARGET>"; //$NON-NLS-1$

    /**
     * 
     */
    private String      sTarget = null;


    /**
     * Construct an instance of a dialogue move, referring to the previous move
     * and the identifier of this new move.
     * @param parent  A reference to the previous dialogue move.
     * @param move    A reference to the identifier of the move to implement.
     */
    public DialogueMoveAgree(DialogueMove parent,DlgMoveID move) 
    {
        super(parent, move);

        if (parent.getMoveID() == DlgMoveID.UNRAVEL)
            addPossibleMove(DlgMoveID.UNRAVEL);
        else
            addPossibleMove(DlgMoveID.WINDUP);
    }

    /**
     * Set the target of the agreement/disagreement, if relevant.
     * @param tg    The identifier of the TAP element, target of the challenge.
     */
    public void setTarget(String tg) 
    {
        this.sTarget = tg;
    }
    
    /**
     * Called when dealing with a suggestion to end the discussion.
     * @return  A reference to the next dialogue move to play.
     */
    private DialogueMove doQuit()
    {
        if (getMoveID()==DlgMoveID.AGREE)
        {      
            setLogItem(Messages.getRandomString(ANSWER_ACCEPT),null);
            addPossibleMove(DlgMoveID.SHOWME);
            removeAllMoves();
            setMoveData(null);
        }
        else if (getMoveID()==DlgMoveID.DISAGREE)
        {
        
        }
        return null;
    }
    /**
     * Called when dealing with a suggestion for the belief to explore.
     * @return  A reference to the next dialogue move to play.
     */
    private DialogueMove doUnravel()
    {
        DialogueMove nmove = null;
        
        if (getMoveID()==DlgMoveID.AGREE)
        {
            setLogItem(Messages.getRandomString(ANSWER_ACCEPT),null);
            addPossibleMove(DlgMoveID.TELLMORE);
            addPossibleMove(DlgMoveID.SHOWME);
            setMoveData(null);
           
            nmove = goNextMove(DlgMoveID.SHOWME);
        }
        else if (getMoveID()==DlgMoveID.DISAGREE)
        {
            setLogItem(Messages.getRandomString(ANSWER_REJECT),null);
            Hashtable table = (Hashtable)getMoveData();
            Vector moves = (Vector)table.get(OLMQueryResult.CAT_SUGGESTION);
            moves.remove(0);
            table.put(OLMQueryResult.CAT_SUGGESTION,moves);
            setMoveData(table);
            nmove = goNextMove(DlgMoveID.UNRAVEL);
        }
        return nmove;
    }
    
    /**
     * Called when dealing with a challenge on the argument.
     * @return  A reference to the next dialogue move to play.
     */
    private DialogueMove doBaffled()
    {
        //setTarget(getParent().getJArgumentPane().getSelectedArgument());
        ToulminNode node = getParent().getToulminSelectedNode();
        //if (getParent().getJToulminGraph()!=null)
        //    node = getParent().getJToulminGraph().getSelectedNode();
        if (node==null) 
        {
            return this;
        }
        Toulmin tt = node.getToulmin().getTopToulmin();
        if (tt==null)
            tt = node.getToulmin();
        setTarget(node.getID());

        Object[] arg = {
                //(this.sTarget==null)? TARGET : this.sTarget
                Messages.getJudgementOn(getBeliefDesc(),tt.getClaim().getClaimLevel())

        };
        if (getMoveID()==DlgMoveID.AGREE)
            setLogItem(Messages.getRandomString(ANSWER_AGREE),arg);
        else if (getMoveID()==DlgMoveID.DISAGREE)
            setLogItem(Messages.getRandomString(ANSWER_DISAGREE),arg);
        else if (getMoveID()==DlgMoveID.MOVEON)
            setLogItem(Messages.getRandomString(ANSWER_MOVEON),arg);
        
        // Get the user id from the controller
        //Vector params = new Vector();
        //params.addElement(getUserName());
        //params.addElement(getBeliefDesc());
        //params.addElement(sTarget);
        
        /*getParent().startProgress();
        String strError = null;
        try {
            getParent().executeRequest(OLMServerConfig.HDR_SETARGUMENT, params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            strError = DlgErrorMsg.ERR_CONNECTION;
        } catch (IOException e) {
            e.printStackTrace();
            strError = DlgErrorMsg.ERR_CONNECTION;
        }   
        getParent().stopProgress(strError);*/
        
        DialogueMove nmove = goNextMove(DlgMoveID.WINDUP);
        if (nmove instanceof DialogueMoveWindUp)
        {
            DialogueMoveWindUp windup = (DialogueMoveWindUp) nmove;
            windup.setTarget(sTarget);
            windup.setOutcome(getMoveID().toString());
        }
        
        return nmove;
    }
    
    /* (non-Javadoc)
     * @see dialogue.DialogueMove#doMove()
     */
    public DialogueMove onMoveExecute() {
        super.onMoveExecute();
        
        DialogueMove nmove = null;
        
        // check for the last move
        if (getLastMove().getMoveID() == DlgMoveID.HEREIS)
            nmove = doBaffled();
        else if (getLastMove().getMoveID() == DlgMoveID.UNRAVEL)
            nmove = doUnravel();
        else if (getLastMove().getMoveID() == DlgMoveID.QUIT)
            nmove = doQuit();
        
        if (nmove!=null)
            nmove.onMoveExecute();

        return this;        
    }

    /* (non-Javadoc)
     * @see dialogue.DialogueMove#getLogParams()
     */
    public Hashtable getLogParams() {
        Hashtable params = super.getLogParams();
        if (sTarget!=null) params.put(OLMQueryResult.LOG_TARGET,sTarget);
        return params;
    }
    
    
}
