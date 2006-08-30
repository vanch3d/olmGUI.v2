/**
 * @file DialogueMovePerhaps.java
 */
package dialogue;

import java.util.Hashtable;
import java.util.Vector;


import olmgui.OLMMainGUI;
import olmgui.i18n.Messages;
import olmgui.utils.StopWatch;
import toulmin.Toulmin;
import config.OLMQueryResult;

/**
 * This class implements the PERHAPS dialogue move, used by the OLM to present
 * to the learner its judgment on a particular topic.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.43 $
 *
 */
public class DialogueMovePerhaps extends DialogueMove {

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     */
    private final static String PERHAPS_IGNORE = "DlgMove.Perhaps.Ignore", //$NON-NLS-1$
                                PERHAPS_JUGDMENT = "DlgMove.Perhaps.Judgment", //$NON-NLS-1$
                                PERHAPS_ERROR = "DlgMove.Perhaps.Error"; //$NON-NLS-1$
 
    
    /**
     * 
     */
    private Double      score = null;
        
    /**
     * @param parent
     */
    public DialogueMovePerhaps(DialogueMove parent)
    {
        super(parent, DlgMoveID.PERHAPS);
        //if (parent instanceof DialogueMoveShowMe)
        //{
        //    DialogueMoveShowMe showme = (DialogueMoveShowMe) parent;
        //    this.beliefdesc = showme.getBeliefDesc();
        //}
        //setPlayerOLM();
        addPossibleMove(DlgMoveID.TELLMORE);
        addPossibleMove(DlgMoveID.SHOWME);
        addPossibleMove(DlgMoveID.LOST);
        addPossibleMove(DlgMoveID.QUIT);
        addPossibleMove(DlgMoveID.BAFFLED);
   }
    
    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#doMove()
     */
    public DialogueMove onMoveExecute() 
    {
        StopWatch sw = new StopWatch("HEREIS");
        sw.start("HEREIS Log move");
        super.onMoveExecute();
        // check if the return value is null or if there is an ERROR in it
        String strError = null;
        //Vector vecWarning = null;
         
        // There was a problem with the server (connection or whatever)
        Hashtable result = (Hashtable)getMoveData();
        
        // clear the Toulmin data structure
        setToulmin(null);
        sw.stop();
        //if (result==null || (result.get("UPDATEDBELEIF"))!=null)
        //{
        //	Vector vec = (Vector)result.get("UPDATEDBELEIF");
        //	getParent().getJTopicSelector().updateDescriptors(vec);
        //}

        if (result==null || (strError=(String)result.get(OLMQueryResult.CAT_ERROR))!=null)
        {
            // Hide the output panels
            //getParent().getJToggleDistrib().setEnabled(false);
            //getParent().getJToggleBelief().setEnabled(false);
            //getParent().getJToggleEvidence().setEnabled(false);
            getParent().enableTouminView(false);
            //getParent().getJToggleArgument().setEnabled(false);

            // @todo How do we let the learner know about problem here? 
            // Up to now, it is by displaying the error nmessage in the progress bar...
            getParent().progressStop(strError);
            
            // Update the move buttons
            getParent().enableMove(DlgMoveID.SHOWME,false);
            getParent().enableMove(DlgMoveID.BAFFLED,false);

        }
        // The user didnot specify his question properly
        else if ((strError=(String)result.get(OLMQueryResult.CAT_WARNING))!=null)
        {
            // reasons are in evidence
            // @todo How do we let the learner know about problem here? 
            //Vector evidence =  new Vector((Vector)result.get(OLMQueryResult.CAT_EVIDENCE));

            // Hide the output panels
            //getParent().getJToggleDistrib().setEnabled(false);
            //getParent().getJToggleBelief().setEnabled(false);
            //getParent().getJToggleEvidence().setEnabled(false);
            getParent().enableTouminView(false);
            //getParent().getJToggleArgument().setEnabled(false);
            
            // Update the move buttons
            getParent().enableMove(DlgMoveID.SHOWME,false);
            getParent().enableMove(DlgMoveID.BAFFLED,false);

            // Stop the progress bar
            getParent().progressStop(null);
            
            // Update the dialogue pane
            String errMsg = Messages.getRandomString(PERHAPS_ERROR);
            Vector evid =(Vector)result.get("EVIDENCE");
            if (evid.size()>0)
            {
                String evMsg = Messages.getRandomString((String)evid.get(0));
                errMsg += " " + evMsg;
            }
            getParent().updateDialogueMove(getPlayer(),errMsg);
        }
        // Everything is fine: I have a belief to display
        else
        {
            //Toulmin toulmin = Toulmin.build((Hashtable)result.get(OLMQueryResult.CAT_TOULMIN),getBeliefDesc());
            Toulmin toulmin = null;
            sw.start("HEREIS format toulmin");
            try
            {
                toulmin = Toulmin.fromXMLRPC((Hashtable)result.get(OLMQueryResult.CAT_TOULMIN));
            }
            catch (Exception e) {e.printStackTrace();}
            
            // Set the Toulmin data structure
            setToulmin(toulmin);
            score = new Double(toulmin.getClaim().getClaimSummary());
            //ToulminData data = toulmin.getData();
            //Vector belief = (Vector)data.get(OLMQueryResult.CAT_BELIEF);
            //Vector mass = (Vector)data.get(OLMQueryResult.CAT_DISTRIBUTION);
            //Vector pignistic = (Vector)data.get(OLMQueryResult.CAT_PIGNISTIC);
            
            if (score.equals(new Double(-1)))
            {
                //String question = (getBeliefDesc()==null) ?
                //        new String("<the question>") : getBeliefDesc().toString(); //$NON-NLS-1$

                Object[] arg = {
                        //Messages.getDescriptionOn(getBeliefDesc())
                        getBeliefDesc()
                        };
                setLogItem(Messages.getRandomString(PERHAPS_IGNORE),arg);
           
                // Hide the output panels
                //getParent().getJToggleDistrib().setEnabled(false);
                //getParent().getJToggleBelief().setEnabled(false);
                //getParent().getJToggleEvidence().setEnabled(false);
                getParent().enableTouminView(false);
                //getParent().getJToggleArgument().setEnabled(false);

                getParent().enableMove(DlgMoveID.SHOWME,false);
                getParent().enableMove(DlgMoveID.BAFFLED,false);
                getParent().progressStop(null);
            }
            else
            {
                // set the data into the various panes
                //getParent().getJBeliefPane().setData(getBeliefDesc(),score,null);
//                if (getParent().getJHistoryPane()!=null)
//                	getParent().getJHistoryPane().setData(toulmin);
                
                //getParent().getJPartitionPane().setToulmin(toulmin);
                
                //getParent().getJDistributionPane().setData(data.getDistribution(),
                //					data.getCertainty(),data.getPignistic(),null);
                sw.stop();
                sw.start("HEREIS set Topic graph");
                if (getParent().getJGraphPanel()!=null)
                    getParent().getJGraphPanel().addBelief(getBeliefDesc(),(int)(score.doubleValue()*4)+1);
                //********getParent().getJEvidencePane().setEvidence(getBeliefDesc(),evidence);
                sw.stop();
                //getParent().getJWarrantPane().setEvidence(null,null);
                sw.start("HEREIS set ERs");
                getParent().setToulmin(toulmin);
//                if (getParent().getJToulminGraph()!=null)
//                    getParent().getJToulminGraph().setToulmin(toulmin);
                getParent().enableTouminView(true);
                //getParent().getJArgumentPane().setArgumentData(toulmin);
                sw.stop();
                //String question = (getBeliefDesc()==null) ? new String("<the question>") : getBeliefDesc().toString(); //$NON-NLS-1$
                        
                sw.start("HEREIS update UI");
                Object[] arg = {
                        Messages.getJudgementOn(getBeliefDesc(),toulmin.getClaim().getClaimLevel()),
                        //Messages.getDescriptionOn(getBeliefDesc())
                        getBeliefDesc()
                        };
                setLogItem(Messages.getRandomString(PERHAPS_JUGDMENT),arg);
                    
                //getParent().getJToggleDistrib().setEnabled(true);
                //getParent().getJToggleBelief().setEnabled(true);
                //getParent().getJToggleEvidence().setEnabled(true);
                //getParent().getJToggleArgument().setEnabled(true);
                //getParent().getJArgumentPane().initialiseArgument();
                getParent().activateView(OLMMainGUI.VIEW_BELIEF);
     
                getParent().enableMove(DlgMoveID.SHOWME,true);
                getParent().enableMove(DlgMoveID.BAFFLED,true);
                getParent().progressStop(null);
            }
            sw.stop();
            System.out.println(sw.prettyPrint());

        }
        return this;
    }
    
/*    public void setResult(Hashtable result) {
        this.result = result;
    }*/
    
    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#goNextMove(org.activemath.xlm.openmodel.dialogue.DlgMoveID)
     */
    public DialogueMove goNextMove(DlgMoveID moveid)
    {
        DialogueMove next = super.goNextMove(moveid);//goNextMove(DlgMoveID.SHOWME);
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
