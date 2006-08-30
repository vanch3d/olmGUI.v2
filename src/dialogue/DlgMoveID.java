/**
 * @file DlgMoveID.java
 */
package dialogue;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a type-safe enumaration for the dialogue moves
 *
 * @author Nicolas Van Labeke
 * @version $Revision: 1.14 $
 */
public class DlgMoveID
{
    /**
     * 
     */
    static private final Map moveMap = new HashMap(); // move name -> DlgMoveID

    /**
     * This dialogue is played by the OLM at the initialisation of the GUI.
     */
    public static final DlgMoveID STARTUP = new DlgMoveID("STARTUP",true);

    /**
     * This dialogue move is played when the user request the OLM judgment on 
     * some topic.
     */
    public static final DlgMoveID SHOWME = new DlgMoveID("SHOWME");

    /** 
     * This dialogue move is played when the user agree with the previous 
     * OLM judgement.
     */
    public static final DlgMoveID AGREE = new DlgMoveID("AGREE");

    /** 
     * This dialogue move is played when the user disagree with the previous 
     * OLM judgement.
     */
    public static final DlgMoveID DISAGREE = new DlgMoveID("DISAGREE");

    /** 
     * This dialogue move is played when the user accept a suggestion 
     * made by the OLM.
     */
    public static final DlgMoveID ACCEPT = new DlgMoveID("ACCEPT");

    /** 
     * This dialogue move is played when the user refuse a suggestion 
     * made by the OLM.
     */
    public static final DlgMoveID REJECT = new DlgMoveID("REJECT");
    
    /**
     * This dialogue move is played when the OLM submits its judgement 
     * to the learner.
     */
    public static final DlgMoveID PERHAPS = new DlgMoveID("PERHAPS", true);

    /** 
     * This dialogue move is played when the OLM presents evidence 
     * supporting its judgement to the learner.
     * 
     */
    public static final DlgMoveID HEREIS = new DlgMoveID("HEREIS", true);
    
    /** 
     * This dialogue move is played when the user requests justification 
     * for the OLM last statement. 
     */
    public static final DlgMoveID BAFFLED = new DlgMoveID("BAFFLED");

    /** 
     * This dialogue move is played when the user requests help about what
     * what to do now.
     */
    public static final DlgMoveID LOST = new DlgMoveID("LOST");
    
    /** 
     * This dialogue move is played when the user decides to quit the interaction
     * with the OLM.
     */
    public static final DlgMoveID QUIT = new DlgMoveID("QUIT");

    /** 
     * This dialogue move is played when the user ask for help about the OLM.
     */
    public static final DlgMoveID ABOUT = new DlgMoveID("ABOUT");
    
    /** 
     * This dialogue move is played when the user ask for help about the OLM.
     */
    public static final DlgMoveID TELLMORE = new DlgMoveID("TELLMORE");

    /** 
     * This dialogue move is played when the user swap between multiple ERs.
     */
    public static final DlgMoveID SWAP = new DlgMoveID("SWAP");

    /** 
     * This dialogue move is played when the user ask for the context of an event.
     */
    public static final DlgMoveID CONTEXT = new DlgMoveID("CONTEXT");

    /** 
     * This dialogue move is played when the user decides to give up the 
     * current topicof discussion with the OLM.
     */
    public static final DlgMoveID MOVEON = new DlgMoveID("MOVEON");

    /** 
     * This dialogue move is played by the OLM when responding to the learner's
     * bafflement on a topic.
     */
    public static final DlgMoveID UNRAVEL = new DlgMoveID("UNRAVEL", true);

    /** 
     * This dialogue move is played by the OLM when suggesting the learner to
     * perform a particular activity.
     */
    public static final DlgMoveID SUGGEST = new DlgMoveID("SUGGEST", true);

    /** Indicates a SUGGEST dialogue move (OLM)*/
    public static final DlgMoveID WINDUP = new DlgMoveID("WINDUP", true);

    /** Indicates a LETMOVE dialogue move (OLM)*/
    public static final DlgMoveID LETMOVE = new DlgMoveID("LETMOVE", true);
    
    
    
    /** The unique identifier of the dialogue move */
    private String moveId;

    /** Indicate if this move is a OLM or a user one. */
    private boolean isOLM;

    
    
    /**
     * Default constructor for the dialogue move
     * @param id    The identifier of the move
     */
    private DlgMoveID(String id)
    {
        this.moveId = id;
        this.isOLM = false;
        DlgMoveID.moveMap.put(id, this);
    }

    /**
     * Default constructor for the dialogue move
     *
     * @param id    The identifier of the move
     * @param isOLM True if the move originates from the OLM, false if from the user
     */
    private DlgMoveID(String id, boolean isOLM)
    {
        this.moveId = id;
        this.isOLM = isOLM;
        DlgMoveID.moveMap.put(id, this);
   }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.moveId;
    }
    
    /**
     */
    /**
     * @param moveName  The name of the dialogue move identifier to retrieve.
     * @return  A reference to the dialogue move identifier as given by the name, 
     *          <code>null</code> if incorrect.
     */
    public static final DlgMoveID getByName(String moveName)
    {
        return (DlgMoveID) moveMap.get(moveName);
    }
    
    /**
     * @return  <code>true</code> if this move is played by the OLM, 
     *          <code>false</code> otherwise  
     */
    public boolean isOLM(){return this.isOLM;}
}
