/**
 * @file DialogueMoveBaffled.java
 */
package dialogue;

import java.util.Hashtable;

import config.OLMQueryResult;

import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import toulmin.Toulmin;
import toulmin.ToulminSubClaim;

/**
 * This class implements a dialogue move used by learners to indicate their 
 * puzzlement with a statement made by the OLM.
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.30 $
 */
public class DialogueMoveBaffled extends DialogueMove {

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     * Note that identifier ending by underscore (_) are expected by be chained 
     * with another string.  
     */
    private final static String BAFFLED_WHATNOW = "DlgMove.Baffled.What",  //$NON-NLS-1$
                                BAFFLED_ABOUT_  = "DlgMove.Baffled.";  //$NON-NLS-1$

    //private static final String TARGET = "<TARGET>"; //$NON-NLS-1$
    
    /**
     * 
     */
    private String      sTarget = null;
   
    
    /**
     * @param parent
     * @param move
     * 
     * @todo LETMOVE need to be implemented from this point, allowing the OLM to
     *       give up the discussion trend (but under which conditions?).
     */
    public DialogueMoveBaffled(DialogueMove parent,DlgMoveID move)
    {
        super(parent, move);
        addPossibleMove(DlgMoveID.UNRAVEL);
        addPossibleMove(DlgMoveID.HEREIS);
        //addPossibleMove(DlgMoveID.LETMOVE);
   }

    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#doMove()
     */
    public DialogueMove onMoveExecute()
    {
        //super.onMoveExecute();

        DialogueMove nmove = null;
        if (getMoveID()==DlgMoveID.BAFFLED)
        {
            // Get the BAFLED target from the relevant pane
            ToulminNode node = null;
            node = getParent().getToulminSelectedNode(); 
//            if (getParent().getJToulminGraph()!=null)
//                node = getParent().getJToulminGraph().getSelectedNode();
            if (node==null) 
            {
                return this;
            }
            setTarget(node.getToulminType());
            sendLogMove();

            String template = Messages.getRandomString(BAFFLED_ABOUT_ + this.sTarget);
            Object[] arg = {
                    null,
                    null
                    };
            if (Toulmin.CLAIM.toString().equals(this.sTarget))
            {
                // add the judgement (claim) in the template
                arg[0] = Messages.getJudgementOn(getBeliefDesc(),node.getToulmin().getClaim().getClaimLevel());
            }
            else if (Toulmin.SUBCLAIM.toString().equals(this.sTarget))
            {
                Object obj = node.getData();
                if (obj instanceof ToulminSubClaim) {
                    ToulminSubClaim sub = (ToulminSubClaim) obj;
                    arg[0] = Messages.getString(sub.getValue());
                    arg[1] = Messages.getString(sub.getDimension());
                    
                }
            }


            setLogItem(template,arg);
            getParent().enableDescriptorView(false);
            nmove = doBaffledMove();
        }
        else if (getMoveID()==DlgMoveID.LOST)
        {
            sendLogMove();
            setLogItem(Messages.getRandomString(BAFFLED_WHATNOW),null);
            nmove = doLostMove();
        }

        if (nmove!=null)
            nmove.onMoveExecute();
        
        return this;
    }

    /**
     * @return  A reference to the next dialogue move to play.
     */
    public DialogueMove doBaffledMove() 
    {
        DialogueMove nmove = goNextMove(DlgMoveID.HEREIS);
        if (nmove instanceof DialogueMoveHereIs)
        {
            DialogueMoveHereIs hereis = (DialogueMoveHereIs) nmove;
            hereis.setTarget(sTarget);
        }

        return nmove;
    }
    
    /**
     * @return  A reference to the next dialogue move to play.
     */
    public DialogueMove doLostMove() 
    {
        DialogueMoveUnravel nmove = (DialogueMoveUnravel)goNextMove(DlgMoveID.UNRAVEL);
        if (nmove!=null)
        {
            
        }
        return nmove;
    }
    
    /**
     * Set the target of the puzzlement, if relevant.
     * @param tg    The identifier of the TAP element, target of the challenge.
     */
    public void setTarget(String tg) 
    {
        this.sTarget = tg;
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
