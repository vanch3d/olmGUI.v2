/**
 * @file LevelSet.java
 */
package olmgui.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import olmgui.i18n.Messages;

/**
 * This class implements type-safe enumerations for the set of levels used by
 * the Learner Model to express its belief about the learner.
 * It is a lightweight version of the LevelSet in the Learner Model, it's purpose
 * is to control the internationalisation of the sets.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.6 $
 */
public class LevelSet implements Comparable
{
    /**
     * The internal representation of the set.
     */
    private final String id;
    
    static private final Map setMap = new HashMap(); // set Id -> LevelSet


    public static final LevelSet emptySet = new LevelSet( "0");
    public static final LevelSet I = new LevelSet( "I");
    public static final LevelSet II = new LevelSet( "II");
    public static final LevelSet III = new LevelSet( "III");
    public static final LevelSet IV = new LevelSet( "IV");
    public static final LevelSet I2II = new LevelSet( "I2II");
    public static final LevelSet II2III = new LevelSet( "II2III");
    public static final LevelSet III2IV = new LevelSet( "III2IV");
    public static final LevelSet I2III = new LevelSet( "I2III");
    public static final LevelSet II2IV = new LevelSet( "II2IV");
    public static final LevelSet I2IV = new LevelSet( "I2IV");

    // The set containing all sets is a useful to refer to in a generic
    // way.
    public static final LevelSet theLot = I2IV;

    // This array is for easy access to the level sets on the basis of
    // their ordinal number.
    private static final LevelSet[] allSets = {emptySet, I, II, III, IV, I2II,
            II2III, III2IV, I2III, II2IV, I2IV};

    // This array is for easy access to the singleton sets on the basis of
    // their ordinal number.
    private static final LevelSet[] singelton = {I, II, III, IV};

 
    /**
     * Private constructor
     * @param id    String representation of the object being created.
     */
    private LevelSet(String id)
    {
        this.id = id;
        LevelSet.setMap.put(id, this);

    }

    /**
     * @return
     */
    public String getId() { return id;}
    /**
     * Get the set at the given position in the list.
     * 
     * @param n The index of the set, which must be in the range 0 to 10.
     * @return  The set corresponding to the index or <code>null</code> if the
     *          index is out of range.
     */
    public static LevelSet getSet( int n)
    {
        if (n < 0 || n >= allSets.length)
        {
            System.err.println("Ordinal of set is out of range.");
            return null;
        }
        return allSets[n];
    }
    
    public static final LevelSet getById(String setId)
    {
        return (LevelSet) setMap.get(setId);
    }


    /**
     * Get the singleton at the given position in the list.
     * 
     * @param n The index of the singleton, which must be in the range 0 to 3.
     * @return  The singleton corresponding to the index or <code>null</code> if the
     *          index is out of range.
     */
    public static LevelSet getSingle( int n)
    {
        if (n < 0 || n >= singelton.length)
        {
            System.err.println("Ordinal of set is out of range.");
            return null;
        }
        return singelton[n];
    }
        
    public ArrayList getSetLimits()
    {
        ArrayList list = new ArrayList();
        String id = getId();
        String[] arr = id.split("2");
        //List ll = Arrays.asList(LevelSet.singelton);
        for (int i=0;i<arr.length;i++)
        {
            LevelSet set = LevelSet.getById(arr[i]);
            if (set!=null) list.add(set);
        }
        return list;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return Messages.getString("ATTRIBUTE.LEVELSET." + this.id); //$NON-NLS-1$
    }

    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        if (arg0 instanceof LevelSet) 
        {
           return  this.toString().compareTo(arg0.toString());
        }
        return -1;
    }
}
