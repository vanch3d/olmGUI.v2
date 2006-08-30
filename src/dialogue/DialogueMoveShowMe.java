/**
 * @file DialogueMoveShowMe.java
 */
package dialogue;

import java.util.Hashtable;

import config.OLMDebugMode;

import olmgui.i18n.Messages;
import olmgui.utils.OLMSwingWorker;
import olmgui.utils.StopWatch;
import toulmin.BeliefDesc;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.27 $
 */
public class DialogueMoveShowMe extends DialogueMove {

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     */
    private final static String SHOWME_THINK = "DlgMove.ShowMe.Think"; //$NON-NLS-1$
    
    /**
     * @param parent
     */
    public DialogueMoveShowMe(DialogueMove parent) 
    {
        super(parent, DlgMoveID.SHOWME);

        setMoveData(null);
        addPossibleMove(DlgMoveID.PERHAPS);
   }
    
        
    /* (non-Javadoc)
     * @see org.activemath.xlm.openmodel.dialogue.DialogueMove#doMove()
     */
    public DialogueMove onMoveExecute()
    {
        setBeliefDesc(getParent().getBeliefDescriptor());

        final StopWatch sw = new StopWatch("SHOWME");
        
        sw.start("SHOWME Log Move");
        super.onMoveExecute();

        getParent().getJBeliefConstructor().setModified(true);
        Object[] arg = {
                getBeliefDesc()
                };
        setLogItem(Messages.getRandomString(SHOWME_THINK),arg);
        sw.stop();
       //getParent().sendRequestForBelief(beliefdesc);
        /*Hashtable hash = */
        //sendRequestForBelief(getBeliefDesc(),DlgMoveID.PERHAPS);
        
        /*OLMRequestCallback callBack = new OLMRequestCallback()
        {
                public void handleResult(Object res, URL url, String txt) 
                {
                 }
        };*/
        
        sw.start("SHOWME Get Belief");
        OLMSwingWorker worker = new OLMSwingWorker(getPlanner(),OLMSwingWorker.BELIEF)
        {

			/* (non-Javadoc)
			 * @see olmgui.utils.OLMSwingWorker#construct()
			 */
			protected Object construct() throws Exception
			{
				return sendRequestForBelief(new BeliefDesc(getBeliefDesc()));
			}

			/* (non-Javadoc)
			 * @see olmgui.utils.OLMSwingWorker#finished()
			 */
			protected void finished() 
			{
				super.finished();
				Hashtable hash = null;
 				try 
				{
 	                Object res = get();
					if (res instanceof Hashtable) 
					{
						hash = (Hashtable) res;
					}
				} 
				//catch (InterruptedException e) {e.printStackTrace();} 
				//catch (InvocationTargetException e) {e.printStackTrace();}
				catch (Exception e) 
				{
					//e.printStackTrace();
					System.err.println("Server not available - using dummy data instead.");
					hash = OLMDebugMode.getBelief();
				}
				if (hash!=null)
				{
					setMoveData(hash);
                    sw.stop();
                    System.out.println(sw.prettyPrint());
					DialogueMove nmove = goNextMove(DlgMoveID.PERHAPS);
					if (nmove !=null) nmove.onMoveExecute();
				}
				else
					setMoveData(null);               
 			}
        	
        };
        worker.start();
        
        
        // Get all the Belief Descriptors from the system
        //OLMSwingWorker worker2 = new OLMSwingWorkerDescript(getPlanner());
        //worker2.start();

        
        /*if (hash!=null)
        {
            setMoveData(hash);
            DialogueMove nmove = goNextMove(DlgMoveID.PERHAPS);
            if (nmove instanceof DialogueMovePerhaps)
            {
                DialogueMovePerhaps perhaps = (DialogueMovePerhaps) nmove;
                perhaps.onMoveExecute();
            }
        }
        else
            setMoveData(null);*/

        return this;
    }
   
    /* (non-Javadoc)
     * @see dialogue.DialogueMove#getLogParams()
     */
    public Hashtable getLogParams() {
        Hashtable params = super.getLogParams();
        //if (getBeliefDesc()!=null) params.put("BELIEF",getBeliefDesc());
        return params;
    }
    
}
