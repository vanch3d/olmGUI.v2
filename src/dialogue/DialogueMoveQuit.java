package dialogue;

import olmgui.OLMMainGUI;
import olmgui.i18n.Messages;
import config.OLMTopicConfig;

/**
 * This class implements a dialogue move used by the learner to indicate signal the
 * end of the discussion with the OLM.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.9 $
 */
public class DialogueMoveQuit extends DialogueMove
{

    /**
     * Shortcuts for the different verbalisations of the dialogue move.
     */
    private final static String QUIT_PROMPT = "DlgMove.Quit.Prompt", //$NON-NLS-1$
                                QUIT_BYE = "DlgMove.Quit.Bye";       //$NON-NLS-1$
    
    /**
     * @param parent
     */
    public DialogueMoveQuit(DialogueMove parent)
    {
        super(parent, DlgMoveID.QUIT);

        //addPossibleMove(DlgMoveID.AGREE);
        //addPossibleMove(DlgMoveID.DISAGREE);
        removeAllMoves();
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
     * @see dialogue.DialogueMove#onMoveExecute()
     */
    public DialogueMove onMoveExecute()
    {
        super.onMoveExecute();
        setLogItem(Messages.getRandomString(QUIT_PROMPT),null);
        getParent().updateDialogueMove(OLMUSER,Messages.getRandomString(QUIT_BYE),null);
        removeAllMoves();
        
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


        getParent().activateView(OLMMainGUI.VIEW_DESCRIPTOR);
        activateGUI(false);
        getParent().enableFlipViews(false);
        getParent().getJTopicSelector().setEnabled(false);
        return this;
    }

    
    
}
