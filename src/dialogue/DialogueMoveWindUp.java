/**
 * @file DialogueMoveWindUp.java
 */
package dialogue;

import java.util.Hashtable;

import olmgui.OLMMainGUI;
import olmgui.graph.ToulminNode;
import olmgui.i18n.Messages;
import olmgui.input.OLMChallengePane;
import toulmin.Toulmin;
import config.OLMQueryResult;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.33 $
 */
public class DialogueMoveWindUp extends DialogueMove {

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     */
    private final static String WINDUP_UNRESOLVED = "DlgMove.WindUp.Unresolved", //$NON-NLS-1$
                                WINDUP_RESOLVED = "DlgMove.WindUp.Resolved", //$NON-NLS-1$
                                WINDUP_CHALLENGE = "DlgMove.WindUp.Challenge", //$NON-NLS-1$
                                WINDUP_ACCEPT = "DlgMove.WindUp.Accept", //$NON-NLS-1$
                                WINDUP_MOVEON = "DlgMove.WindUp.Moveon"; //$NON-NLS-1$

    /**
     * The identifier of the target of the previous move from the learner.
     */
    private String      sTarget = null;
    
    /**
     * 
     */
    private String      sOutcome = null;
    
    /**
     * 
     */
    private Hashtable   sChallenge = null;
    
    
    /**
     * @param prevmove
     */
    public DialogueMoveWindUp(DialogueMove prevmove) {
        super(prevmove, DlgMoveID.WINDUP);
        
        //addPossibleMove(DlgMoveID.SHOWME);
        //addPossibleMove(DlgMoveID.LOST);
    }

    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#doMove()
     * @todo Need to implement MOVEON
     */
    public DialogueMove onMoveExecute() 
    {
        DialogueMove move = this.getLastMove();

        if (move.getMoveID() == DlgMoveID.AGREE)
        {
            setOutcome(move.getMoveID().toString());

            ToulminNode node = null;
            node = getParent().getToulminSelectedNode();
//            if (getParent().getJToulminGraph()!=null)
//                node = getParent().getJToulminGraph().getSelectedNode();
            if (node==null) return this;
            Double oldLevel = new Double(node.getToulmin().getClaim().getClaimSummary());
            Hashtable hash = new Hashtable();
            hash.put(OLMQueryResult.CHALLENGE_OLDLEVEL,oldLevel);
            setChallenge(hash);
            
            sendLogMove();
            setLogItem(Messages.getRandomString(WINDUP_RESOLVED),null);
            addPossibleMove(DlgMoveID.TELLMORE);
            addPossibleMove(DlgMoveID.SHOWME);
            addPossibleMove(DlgMoveID.LOST);
            addPossibleMove(DlgMoveID.QUIT);
            getParent().enableDescriptorView(true);
            getParent().enableTouminView(false);
            getParent().activateView(OLMMainGUI.VIEW_DESCRIPTOR);
            activateGUI(true);
            
            // Get all the Belief Descriptors from the system
            //OLMSwingWorker worker = new OLMSwingWorkerDescript(getPlanner());
            //worker.start();

        }
        else if (move.getMoveID() == DlgMoveID.MOVEON)
        {
            setOutcome(move.getMoveID().toString());
            sendLogMove();
            setLogItem(Messages.getRandomString(WINDUP_UNRESOLVED),null);
            addPossibleMove(DlgMoveID.TELLMORE);
            addPossibleMove(DlgMoveID.SHOWME);
            addPossibleMove(DlgMoveID.LOST);
            addPossibleMove(DlgMoveID.QUIT);
            getParent().enableDescriptorView(true);
            getParent().enableTouminView(false);
            getParent().activateView(OLMMainGUI.VIEW_DESCRIPTOR);
            activateGUI(true);
        }
        else //if (move.getMoveID() == DlgMoveID.DISAGREE)
        {
            getParent().updateDialogueMove(OLMUSER,Messages.getRandomString(WINDUP_CHALLENGE),null);
            //if (sTarget.startsWith("CLAIM"))
            {
                ToulminNode node = null;
                node = getParent().getToulminSelectedNode();
//                if (getParent().getJToulminGraph()!=null)
//                    node = getParent().getJToulminGraph().getSelectedNode();
                if (node==null) return this;
                Toulmin toul = node.getToulmin();
                int lvl = 0;
                if (toul.getTopToulmin()==null)
                    lvl = toul.getClaim().getClaimLevel();
                else 
                    lvl = toul.getTopToulmin().getClaim().getClaimLevel();
                //int lvl = (int) (Math.round((score*3)+1);
                getParent().setChallengeData(OLMChallengePane.VIEW_CLAIM,getBeliefDesc(),lvl);
            }
            getParent().activateView(OLMMainGUI.VIEW_DISAGREE);
            addPossibleMove(DlgMoveID.ACCEPT);
            addPossibleMove(DlgMoveID.REJECT);
            addPossibleMove(DlgMoveID.MOVEON);
            activateGUI(true);
            getParent().enableFlipViews(false);
            getParent().enableToulmin(false);

        }
        

        return this;
    }

    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#goNextMove(org.activemath.xlm.openmodel.dialogue.DlgMoveID)
     */
    public DialogueMove goNextMove(DlgMoveID moveid) 
    {
        if (moveid==DlgMoveID.ACCEPT || moveid==DlgMoveID.REJECT || moveid==DlgMoveID.MOVEON)
        {
            if (DlgMoveID.ACCEPT.equals(moveid))
            {
                //ToulminNode node = getParent().getJToulminGraph().getSelectedNode();
                ToulminNode node = getParent().getToulminSelectedNode();
                Double oldLevel = new Double(node.getToulmin().getClaim().getClaimSummary());
                    
                Hashtable hash = getParent().getChallengeOutcome();
                hash.put(OLMQueryResult.CHALLENGE_OLDLEVEL,oldLevel);
                Double conf = (Double) hash.get(OLMQueryResult.CHALLENGE_CONFIDENCE);
                Double lvl = (Double) hash.get(OLMQueryResult.CHALLENGE_LEVEL);
                String confidence[]=
                {
                    Messages.getString("ATTRIBUTE.CHLGCONFID.low"),      
                    Messages.getString("ATTRIBUTE.CHLGCONFID.medium"),      
                    Messages.getString("ATTRIBUTE.CHLGCONFID.high")    
                };
                Object[] arg = 
                {
                    confidence[(int)(conf.doubleValue()*3)],
                    Messages.getJudgementOn(getBeliefDesc(),lvl.doubleValue()),
                    getBeliefDesc()
                };
                    
                getParent().updateDialogueMove(getUserName(),Messages.getRandomString(WINDUP_ACCEPT),arg);
                setChallenge(hash);
                setLogItem(Messages.getRandomString(WINDUP_RESOLVED),null);
            }
            else if (DlgMoveID.MOVEON.equals(moveid))
            {
                getParent().updateDialogueMove(getUserName(),Messages.getRandomString(WINDUP_MOVEON),null);
                setOutcome(moveid.toString());
                setLogItem(Messages.getRandomString(WINDUP_UNRESOLVED),null);
            }
            
            sendLogMove();
            removePossibleMove(DlgMoveID.ACCEPT);
            addPossibleMove(DlgMoveID.TELLMORE);
            addPossibleMove(DlgMoveID.SHOWME);
            addPossibleMove(DlgMoveID.LOST);
            addPossibleMove(DlgMoveID.QUIT);
            
            activateGUI(true);
            getParent().enableFlipViews(true);
            getParent().enableDescriptorView(true);
            getParent().enableTouminView(false);
            //getParent().getJToulminGraph().setEnabled(true);
            getParent().enableToulmin(true);
            // Show the Descriptor View 
            getParent().activateView(OLMMainGUI.VIEW_DESCRIPTOR);
            // Hide the output panels
            //getParent().getJToggleDistrib().setEnabled(false);
            //getParent().getJToggleBelief().setEnabled(false);
            //getParent().getJToggleEvidence().setEnabled(false);
            //getParent().getJToggleArgument().setEnabled(false);
            
            // Get all the Belief Descriptors from the system
            //OLMSwingWorker worker = new OLMSwingWorkerDescript(getPlanner());
            //worker.start();

            return this;
        }

        DialogueMove next = super.goNextMove(moveid);
        if (next!=null)
            next.onMoveExecute();
        
        return next;
    }
    
    /**
     * @param tg
     */
    public void setTarget(String tg) 
    {
        this.sTarget = tg;
    }

    /**
     * @param outcome
     */
    public void setOutcome(String outcome) 
    {
        this.sOutcome = outcome;
    }

    /**
     * @param chlg
     */
    public void setChallenge(Hashtable chlg) 
    {
        this.sChallenge = chlg;
    }

    /* (non-Javadoc)
     * @see dialogue.DialogueMove#getLogParams()
     */
    public Hashtable getLogParams()
    {
        Hashtable params = super.getLogParams();
        if (sTarget!=null) params.put(OLMQueryResult.LOG_TARGET,sTarget);
        if (sOutcome!=null) params.put(OLMQueryResult.LOG_OUTCOME,sOutcome);
        //if (sChallenge!=null) params.put(OLMQueryResult.LOG_CHALLENGE,sChallenge);
        if (sChallenge!=null) params.putAll(sChallenge);
        return params;
    }
    
}

