package toulmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import config.OLMEventsConfig;
import config.OLMQueryResult;
import dialogue.DialoguePlanner;

/**
 * Abstract class representing the backing of a belief, in terms of events description.
 * Note that this class should be specialised in order to deal properly with the various 
 * LeAM events handled by the Learner Model.  
 * 
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.10 $
 */
public abstract class ToulminBacking implements XMLRPCWrapper 
{
    /**
     * The identifier of the event associated with this backing.
     */
    protected   String          eventType =null;

    /**
     * A reference to the main TAP containing this bakcing.
     */
    protected Toulmin           toulmin = null;
    
    
    /**
     * A reference to the warrant this backing is associated with.
     */
    protected ToulminWarrant    warrant = null;
    
	/**
	 * The collection of (raw) attributes/values defining the event at the origin of 
     * the evidence.
     * @todo need to get rid of this and use the proper {@link #dataType} below.
	 */
	HashMap attributes;
    
    
	/**
	 * The collection of (formatted) attributes/values defining the event.
     * Contains all the {@link ToulminAttribute} defining the events, indexed in the 
     * map by the identifier of theattribute.
	 */
	HashMap dataType;
    
//    /**
//     * Get the proper instance of the backing according to the event type.
//     */
//    private static HashMap backingList = new HashMap();
//    
//    /**
//     * Get the proper instance of the {@link ToulminAttribute} based on the attribute/value 
//     * pairs extracted from the event 
//     */
//    private static HashMap attribList = new HashMap();
//    
//    
//    /**
//     * Default initialisation of the backing and attribute/value pairs according to LeAM
//     * events and contents.
//     * 
//     * - Events type are associated (in {@link #backingList}) with the relevant specialisation
//     * of {@link ToulminBacking}. ALL events have to be mapped.
//     * 
//     * - Attributes are associated (in {@link #attribList}) with the relevant method 
//     * used to create the instance of the {@link ToulminBacking}. If an attribute is not 
//     * mapped, the default method is therefore used.
//     * 
//     */
//    static {
//        
//        // Map the various event type with the proper c
//        backingList.put("ExerciseFinished",ExerciseFinished.class);
//        backingList.put("SelfReport",SelfReport.class);
//        backingList.put("OLMMetacog",OLMMetacog.class);
//        backingList.put("OLMChallenge",OLMChallenge.class);
//        backingList.put("SituationFactorChanged",SituationFactorChanged.class);
//        
//        Class[]arr = new Class[0];
//        try {
//            ArrayList list = new ArrayList();
//            list.add(String.class);
//            list.add(Object.class);
//            
//            Method mthDiff = ToulminAttribute.class.getMethod("DIFFICULTY",(Class[])list.toArray(arr));
//            Method mthPerf = ToulminAttribute.class.getMethod("PERFORMANCE",(Class[])list.toArray(arr));
//            Method mthMeter = ToulminAttribute.class.getMethod("METER",(Class[])list.toArray(arr));
//            Method mthComp = ToulminAttribute.class.getMethod("COMPETENCYLEVEL",(Class[])list.toArray(arr));
//
//            // attribute displayed as a meter
//            attribList.put(OLMQueryResult.EVIDENCE_LIKING,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_SATISFACTION,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_PRIDE,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_APTITUDE,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_CONFIDENCE,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_INTEREST,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_EFFORT,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_DEPTH,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_CHLGCONFID,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_CHLGINTRAN,mthMeter);
//            attribList.put(OLMQueryResult.EVIDENCE_TRUSTABILITY,mthMeter);
//            
//            // difficulty and its mapping
//            attribList.put(OLMQueryResult.EVIDENCE_DIFF,mthDiff);
//            
//            // competency level and its mapping
//            attribList.put(OLMQueryResult.EVIDENCE_COMPLVL,mthComp);
//            attribList.put(OLMQueryResult.EVIDENCE_CHLGLEVEL,mthComp);
//            
//            // performance and its mapping
//            attribList.put(OLMQueryResult.EVIDENCE_PERF,mthPerf);      
//        } 
//        catch (SecurityException e) {e.printStackTrace();}
//        catch (NoSuchMethodException e) {e.printStackTrace();} 
//    }
//    
//    /**
//     * Get the proper instance of the backing according to the event type.
//     */
//    public static Class getFromType(String type)
//    {
//        return (Class) backingList.get(type);
//    }
    
    /**
     * Default constructor.
     */
    public ToulminBacking() 
    {
        super();
        dataType = new HashMap();
    }

    /**
     * Constructor for a backing associated with the given event.
     * @param event The identifier of the event type.
     */
    public ToulminBacking(String event)
    {
        super();
        setEventType(event);
        dataType = new HashMap();
    }
    
	/* (non-Javadoc)
	 * @see toulmin.XMLRPCWrapper#toXMLRPC()
	 */
	public Object toXMLRPC() 
	{
		Hashtable tab = new Hashtable(attributes);
		return tab;
	}

    /**
     * Create a new instance of a backing based on the XML-RPC compliant object.
     * @param src       The object containing the backing.
     * @param toulmin   A reference to the TAP containg this backing.
     * @return          A new instance of a backing, filled with the data from the object.
     */
	public static ToulminBacking fromXMLRPC(Object src,Toulmin toulmin) 
	{
        OLMEventsConfig cfg = OLMEventsConfig.getInstance();
        
		ToulminBacking backing = null;
		if (src instanceof Hashtable) {
			Hashtable hash = (Hashtable) src;
            String evidType = (String)hash.get(OLMQueryResult.EVIDENCE_TYPE);
            
            backing = cfg.getBackingFromEvent(evidType);
            if (backing!=null)
            {
                backing.setEventType(evidType);
                backing.toulmin = toulmin;
                backing.setAttributes(hash);
                backing.setDatatypes();
               
            }

		}
		return backing;
	}
	
	/**
	 * Add a pair attribute/value to the current backing.
	 * @param attribute	The attribute to define (assumed to be a String).
	 * @param value		The value toassociate withthe attribute (assumed to be XML-RPC compliant).
	 */
	public void addAttribute(String attribute,Object value)
	{
		if (attributes==null)
			attributes = new HashMap();
		attributes.put(attribute,value);
	}

	/**
     * Get the list of all (raw) attributes associated with the backing.
	 * @return A reference to the list of raw attributes.
	 */
	public HashMap getAttributes() {
		return attributes;
	}
    
    /**
     * Get the list of all Toulmin attributes associated with the backing.
     * @return A reference to the list of Toulmin attributes.
     */
    public HashMap getDataTypes()
    {
        return this.dataType;
    }


	/**
     * Get the value associated with the given attribute.
	 * @param key  The identifier of the attribute to retrieve.
     * @return  the value which maps the specified key, or
     *          <tt>null</tt> if the attribute list does not contains the key.
	 */
	public Object getValue(String key) {
		return attributes.get(key);
	}
    
    /**
     * Get the Toulmin Attribute associated with the given key.
     * @param key  The identifier of the attribute to retrieve.
     * @return  A reference to the relevant instance of the attribute, 
     *          <tt>null</tt> if the list does not contains the key.
     * @todo Need to merge both list and getter/setter.
     */
    public ToulminAttribute getToulminAttribute(String key)
    {
        ToulminAttribute attr = null;
        Object obj = dataType.get(key);
        if (obj instanceof ToulminAttribute)
        {
            attr = (ToulminAttribute) obj;
        }
        else
        {
            attr = (ToulminAttribute)ToulminAttribute.UNKNOWN(key);
        }
        return attr;
    }

	/**
	 * @param attributes The collection of attributes to set.
	 */
	public void setAttributes(HashMap attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * @param attributes The collection of attributes to set.
	 */
	public void setAttributes(Hashtable attributes) {
		this.attributes = new HashMap(attributes);
	}

	/**
	 * @return Returns the warrant.
	 */
	public ToulminWarrant getWarrant() 
    {
		return warrant;
	}
    
    

	/**
     * @return Returns the eventType.
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * @param eventType The eventType to set.
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
	 * @param warrant The warrant to set.
	 */
	public void setWarrant(ToulminWarrant warrant) {
		this.warrant = warrant;
	}
	
	
	public void setDatatypes()
	{
        
        OLMEventsConfig cfg = OLMEventsConfig.getInstance();

		Set keys = attributes.keySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext())
		{
			String key = (String)iter.next();
			Object value = attributes.get(key);
            
            ToulminAttribute attrib = cfg.getAttribute(key,value);
            if (attrib!=null)
            {
                dataType.put(key,attrib);
//                if (attrib.getAttributeId().equals(OLMQueryResult.EVIDENCE_USERINPUT))
//                    attrib.getRenderer(false).setInformation(true);
            }
//            // build the argunments
//            ArrayList list = new ArrayList();
//            list.add(key);
//            list.add(value);
//            
//            Object[] obj2 = new Object[0];
//            obj2 = list.toArray(obj2);
//            
//            Object attrib = null;
//            Object generator = attribList.get(key);
//            if (generator instanceof Method)
//            {
//                try {
//                    Method mth = (Method) generator;
//                    attrib = mth.invoke(null,obj2);
//                } 
//                catch (IllegalArgumentException e) {e.printStackTrace();} 
//                catch (IllegalAccessException e) {e.printStackTrace();} 
//                catch (InvocationTargetException e) {e.printStackTrace();}
//            }
//            
//            if (attrib==null)
//                dataType.put(key,ToulminAttribute.DEFAULT(key,value));
//            else
//                dataType.put(key,attrib);

            //Object obj = mth.invoke(null,obj2);
            //obj.toString();
            
            /*
            if (OLMQueryResult.EVIDENCE_LIKING.equals(key) || 
           		 OLMQueryResult.EVIDENCE_SATISFACTION.equals(key) ||
           		 OLMQueryResult.EVIDENCE_PRIDE.equals(key))
            {
            	dataType.put(key,ToulminAttribute.METER(key,value));
            }
            else if (OLMQueryResult.EVIDENCE_DIFF.equals(key))
               {
               	dataType.put(key,ToulminAttribute.DIFFICULTY(key,value));
               	
               }
            else if (OLMQueryResult.EVIDENCE_COMPLVL.equals(key))
               {
               	dataType.put(key,ToulminAttribute.COMPETENCYLEVEL(key,value));
               }
            else if (OLMQueryResult.EVIDENCE_CHLGLEVEL.equals(key))
            {
                dataType.put(key,ToulminAttribute.COMPETENCYLEVEL(key,value));
                
            }
            else if (OLMQueryResult.EVIDENCE_PERF.equals(key))
            {
                dataType.put(key,ToulminAttribute.PERFORMANCE(key,value));
                
            }
            else if (value.getClass().equals(Double.class))
            {
            	dataType.put(key,ToulminAttribute.METER(key,value));
            }
            else //if (value instanceof String)
            {
                
            	dataType.put(key,ToulminAttribute.DEFAULT(key,value));
            	
            }*/
		}
	}

    
    /**
     * Get the template used to transcribe this backing in "natural language".
     * This method has to be specifically implemented for all specialisation, ie for all 
     * events supported by the XLM. Used jointly with {@link #getArguments(DialoguePlanner)}, 
     * it should provide a cogent description of the backing the learner is currently exploring.
     *  
     * The current dialogue planner can be used to contextualise the template to use (for 
     * example by having a different template if it is the first time (or not) this backing 
     * is presented to the learner). 
     * 
     * @param planner   A reference to the current dialogue planner.
     * @return A string containing the template
     */
    public abstract String getTemplate(DialoguePlanner planner);

    /**
     * Get the arguments to be mapped into the dialogue template.
     * 
     * This method has to be specifically implemented for all specialisation, ie for all 
     * events supported by the XLM. Used jointly with {@link #getTemplate(DialoguePlanner)}, 
     * it should provide a cogent description of the backing the learner is currently exploring.
     *
     * The current dialogue planner can be used to contextualise the arguments to use (for 
     * example by having a different format if it is the first time (or not) this backing 
     * is presented to the learner). 
     *
     * @param planner
     * @return  A list containing the arguments associated with the template.
     *          Their position in the list does reflect the placeholders in the template 
     *          (ie. the first argument will be mapped with the placeholder {0}).
     */
    public abstract ArrayList getArguments(DialoguePlanner planner);
}
