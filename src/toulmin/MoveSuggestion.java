package toulmin;

import java.util.ArrayList;

import dialogue.DlgMoveID;

/**
 * A lightweight implementation for the "next move" suggestion

 * @author Nicolas Van Labeke
 * @version $Revision: 1.3 $
 * @deprecated not (yet) in use.
 */
public class MoveSuggestion implements XMLRPCWrapper 
{
	/**
	 * A reference to the suggested move to be played next
	 */
	DlgMoveID 	moveId;
    
	/**
	 * A list of the dialogue moves candidate for the next move.
	 */
    ArrayList   descriptors;

	public Object toXMLRPC() 
	{
		return null;
	}

}
