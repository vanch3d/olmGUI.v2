package config;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.ImageIcon;

import olmgui.i18n.Messages;
import toulmin.BeliefDesc;
import toulmin.ToulminAttribute;
import toulmin.ToulminBacking;
import dialogue.DialoguePlanner;
import dialogue.DlgMoveID;

/**
 * A wrapper for the specification of the information coming from outside the XLM and
 * supposedly given to the OLM as parameters.
 * The main source for configuration are the events handled by the LM and used in the 
 * evidence of belief. The configuration consists mainly in defining:
 * <ul>
 * <li>which events are recognised by the OLM and how they are externalised, ie by 
 *      defining the template and its arguments used in the 
        {@link olmgui.output.OLMDialoguePane} - see
 *      {@link toulmin.ToulminBacking#getArguments(DialoguePlanner)} and
 *      {@link toulmin.ToulminBacking#getTemplate(DialoguePlanner)}. 
 *      
 * <li>for every event, which attributes are recognised by the OLM and how they are
 *      externalised (ie by defining the type, mappings and renderer used in
 *      the {@link olmgui.output.OLMWarrantPane} and the 
 *      {@link olmgui.output.OLMDialoguePane} - see {@link toulmin.ToulminAttribute}.
 * </ul>
 * 
 * Since such configuration is (not yet) properly supported, this class provides a
 * environment for hard-coding it. 
 * 
 * The specific configuration of the events are provided by a couple of dedicated
 * methods, building an instance of {@link toulmin.ToulminBacking} corresponding to
 * the supported LeAM events.
 * 
 * Every possible attributes (identified by their {@link OLMQueryResult} flag) is also 
 * associated with one of the possible instance of {@link toulmin.ToulminAttribute}. 
 * A couple of specific methods are also defined in order to ease this procedure.
 * 
 * Finally, the two methods {@link #getBackingFromEvent(String)} and 
 * {@link #getAttribute(String, Object)} are used by the OLM to retrieve the relevant 
 * objects. 
 * 
 * @todo There are a couple of exceptions to deal with (properly).
 *  
 * @author Nicolas Van Labeke
 * @version $Revision: 1.14 $
 */
public class OLMEventsConfig 
{
    /**
     * Map LeAM event types with an instance of the proper backing node in the
     * Toulmin Argumentation Pattern.
     */
    private HashMap backingList = new HashMap();

    /**
     * Map every single attribute/value pair extracted from the event with the
     * proper instance of {@link ToulminAttribute}. 
     */
    private static HashMap attribList = new HashMap();
    
    /**
     * Map every attribute with its category.
     * @see ToulminAttribute#getCategory()
     */
    private static HashMap catList = new HashMap();
    
    /**
     * A reference to the singleton of this class.
     */
    private static OLMEventsConfig _instance = new OLMEventsConfig();

    /**
     * Get the unique instance of this class.
     * @return  A reference to the singleton of this class.
     */
    static public OLMEventsConfig getInstance()
    {
        return _instance;
    }

    /**
     * Build the mapping of events supported by the OLM.
     * 
     * The map associate the type of the event with the method building the 
     * proper instance of {@link ToulminBacking}.
     */
    private void mapEvents()
    {
        try 
        {
            Method mth = getClass().getMethod(OLMQueryResult.EVT_XFINISHED,null); 
            backingList.put(OLMQueryResult.EVT_XFINISHED,mth);                    
            mth = getClass().getMethod(OLMQueryResult.EVT_XSTEP,null);            
            backingList.put(OLMQueryResult.EVT_XSTEP,mth);                        
            mth = getClass().getMethod(OLMQueryResult.EVT_CHALLENGE,null);            
            backingList.put(OLMQueryResult.EVT_CHALLENGE,mth);                        
            mth = getClass().getMethod(OLMQueryResult.EVT_METACOG,null);              
            backingList.put(OLMQueryResult.EVT_METACOG,mth);                          
            mth = getClass().getMethod(OLMQueryResult.EVT_SELFREPORT,null);              
            backingList.put(OLMQueryResult.EVT_SELFREPORT,mth);                          
            mth = getClass().getMethod(OLMQueryResult.EVT_SITFACT,null);  
            backingList.put(OLMQueryResult.EVT_SITFACT,mth);              
            
        } 
        catch (SecurityException e) {e.printStackTrace();}
        catch (NoSuchMethodException e) {e.printStackTrace();}
    }
    
    /**
     * Build the mapping the the attribute's category
     */
    private void mapCategory()
    {
        catList.put(OLMQueryResult.EVIDENCE_ITEM,ToulminAttribute.CAT_EVIDENCE);
        catList.put(OLMQueryResult.EVIDENCE_DIFF,ToulminAttribute.CAT_CONTEXT);
        catList.put(OLMQueryResult.EVIDENCE_PERF,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_COMPLVL,ToulminAttribute.CAT_CONTEXT);
        catList.put(OLMQueryResult.EVIDENCE_MISCONCEPTION,ToulminAttribute.CAT_CONTEXT);
        catList.put(OLMQueryResult.EVIDENCE_USERINPUT,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_FOCUS,ToulminAttribute.CAT_EVIDENCE);
        catList.put(OLMQueryResult.EVIDENCE_RELATED,ToulminAttribute.CAT_EVIDENCE);
        catList.put(OLMQueryResult.EVIDENCE_TITLE,ToulminAttribute.CAT_CONTEXT);
        catList.put(OLMQueryResult.EVIDENCE_TYPE,ToulminAttribute.CAT_EVIDENCE);
        catList.put(OLMQueryResult.EVIDENCE_ACTION,ToulminAttribute.CAT_EVIDENCE);
        catList.put(OLMQueryResult.EVIDENCE_INDEX,ToulminAttribute.CAT_EVIDENCE);
        catList.put(OLMQueryResult.EVIDENCE_RELEVANCE,ToulminAttribute.CAT_EVIDENCE);
        catList.put(OLMQueryResult.EVIDENCE_DIRECT,ToulminAttribute.CAT_EVIDENCE);
        catList.put(OLMQueryResult.EVIDENCE_LIKING,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_PRIDE,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_SATISFACTION,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_APTITUDE,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_INTEREST,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_CONFIDENCE,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_EFFORT,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_DLGMOVE,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_DEPTH,ToulminAttribute.CAT_CONTEXT);
        catList.put(OLMQueryResult.EVIDENCE_INITIATIVE,ToulminAttribute.CAT_CONTEXT);
        catList.put(OLMQueryResult.EVIDENCE_BDESCRIPT,ToulminAttribute.CAT_CONTEXT);
        catList.put(OLMQueryResult.EVIDENCE_CHLGCONFID,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_CHLGINTRAN,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_CHLGLEVEL,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_CHLGOLDLEVEL,ToulminAttribute.CAT_USER);
        catList.put(OLMQueryResult.EVIDENCE_COMPETENCY,ToulminAttribute.CAT_CONTEXT);
        catList.put(OLMQueryResult.EVIDENCE_TRUSTABILITY,ToulminAttribute.CAT_CONTEXT);
    }
    
    /**
     * Build the mapping of attributes supported by the OLM.
     * 
     * The map associate the type of the attribute with the method building the 
     * proper instance of {@link ToulminAttribute}.
     */
    private void mapAttributes()
    {
        Class[]arr = new Class[0];
        try {
            ArrayList list = new ArrayList();
            list.add(String.class);
            list.add(Object.class);

            Method mthDiff = getClass().getMethod("DIFFICULTY",(Class[])list.toArray(arr));         //$NON-NLS-1$
            Method mthPerf = getClass().getMethod("PERFORMANCE",(Class[])list.toArray(arr));        //$NON-NLS-1$
            Method mthMeter = getClass().getMethod("METERGREEN",(Class[])list.toArray(arr));        //$NON-NLS-1$
            Method mthMeterR = getClass().getMethod("METERRED",(Class[])list.toArray(arr));         //$NON-NLS-1$
            Method mthList = getClass().getMethod("LIST",(Class[])list.toArray(arr));               //$NON-NLS-1$
            Method mthComp = getClass().getMethod("COMPETENCYLEVEL",(Class[])list.toArray(arr));    //$NON-NLS-1$
            Method mthNone = getClass().getMethod("NONE",(Class[])list.toArray(arr));               //$NON-NLS-1$
            
            // attribute displayed as a meter
            attribList.put(OLMQueryResult.EVIDENCE_RELEVANCE,mthMeterR);
            attribList.put(OLMQueryResult.EVIDENCE_LIKING,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_SATISFACTION,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_PRIDE,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_APTITUDE,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_CONFIDENCE,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_INTEREST,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_EFFORT,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_DEPTH,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_CHLGCONFID,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_CHLGINTRAN,mthMeter);
            attribList.put(OLMQueryResult.EVIDENCE_TRUSTABILITY,mthMeter);
            
            // difficulty and its mapping
            attribList.put(OLMQueryResult.EVIDENCE_DIFF,mthDiff);
            
            // competency level and its mapping
            attribList.put(OLMQueryResult.EVIDENCE_COMPLVL,mthComp);
            attribList.put(OLMQueryResult.EVIDENCE_CHLGLEVEL,mthComp);
            attribList.put(OLMQueryResult.EVIDENCE_CHLGOLDLEVEL,mthComp);
            
            // performance and its mapping
            attribList.put(OLMQueryResult.EVIDENCE_PERF,mthPerf);      

            attribList.put(OLMQueryResult.EVIDENCE_MISCONCEPTION,mthList);      
            attribList.put(OLMQueryResult.EVIDENCE_FOCUS,mthList);      
            attribList.put(OLMQueryResult.EVIDENCE_RELATED,mthList);      

            attribList.put(OLMQueryResult.EVIDENCE_ACTION,mthNone);      
            attribList.put(OLMQueryResult.EVIDENCE_ITEM,mthNone);      
            //attribList.put(OLMQueryResult.EVIDENCE_TYPE,mthNone);      
            attribList.put(OLMQueryResult.EVIDENCE_INITIATIVE,mthNone);      
        } 
        catch (SecurityException e) {e.printStackTrace();}
        catch (NoSuchMethodException e) {e.printStackTrace();}         
    }
    
    /**
     * Default constructor.
     */
    private OLMEventsConfig() 
    {
        // Map the various event type with the proper class.
        mapEvents();
        
        // Map the various attributes with the method creating the object.
        mapAttributes();
        mapCategory();
    }
    
    /**
     * Build a proper Toulmin's evidence attribute, based on its name and value. 
     * @param key   The name of the attribute.
     * @param value The value associated with the attribute.
     * @return  A reference to a new ToulminAttribute instance, reflecting its name and value.
     */
    public ToulminAttribute getAttribute(String key,Object value)
    {
        ToulminAttribute attrib = null;

        Object generator = attribList.get(key);
        if (generator instanceof Method)
        {
            try {
                Object[] obj2 = new Object[2];
                obj2[0] = key;
                obj2[1] = value;

                Method mth = (Method) generator;
                Object res = mth.invoke(null,obj2);
                if (res instanceof ToulminAttribute)
                {
                    attrib = (ToulminAttribute) res;
                    
                }
            } 
            catch (IllegalArgumentException e) {e.printStackTrace();} 
            catch (IllegalAccessException e) {e.printStackTrace();} 
            catch (InvocationTargetException e) {e.printStackTrace();}
            
        }
        else
        {
            attrib = DEFAULT(key,value);
        }
        String cat = (String)catList.get(key);
        if (cat!=null)
            attrib.setCategory(cat);
        return attrib;
    }
 
    /**
     * Build a new instance of a Toulmin's backing based on the identifier
     * of the corresponding LeAM event. 
     *
     * @param evt   The identifier of the LeAM event
     * @return      A reference to the new instance of ToulminBacking encapsulating the 
     *              given event.
     */
    public ToulminBacking getBackingFromEvent(String evt)
    {
        //Class cls = (Class)backingList.get(evt);
        //return cls;
        ToulminBacking backing = null;
        Object generator = backingList.get(evt);
        if (generator instanceof Method)
        {
            try {
                Method mth = (Method) generator;
                Object obj = mth.invoke(null,null);
                if (obj instanceof ToulminBacking) {
                    backing = (ToulminBacking) obj;
                    
                }
            } 
            catch (IllegalArgumentException e) {e.printStackTrace();} 
            catch (IllegalAccessException e) {e.printStackTrace();} 
            catch (InvocationTargetException e) {e.printStackTrace();}
        }
        return backing;
    }
    
    
    /**
     * Create a new Backing object for an ExerciseFinished event.
     * @return  An instance of ToulminBacking, with the abstract methods implemented for 
     *          dealing with ExerciseFinished event.
     */
    public static ToulminBacking  ExerciseFinished()
    {
        ToulminBacking bk = new ToulminBacking(OLMQueryResult.EVT_XFINISHED)
        {
            
            public String getTemplate(DialoguePlanner planner) 
            {
                return Messages.getString("DlgMove.HereIs.BACKING." + getEventType());  //$NON-NLS-1$
            }

            public ArrayList getArguments(DialoguePlanner planner) 
            {
                ArrayList list = new ArrayList();
                
                ToulminAttribute attr = getToulminAttribute(OLMQueryResult.EVIDENCE_DIFF);
                String str = attr.getValueName();
                list.add(str);
                attr = getToulminAttribute(OLMQueryResult.EVIDENCE_PERF);
                str = attr.getValueName();
                list.add(str);
                
                return list;
            }
            
        };
        
        return bk;
    }

    /**
     * Create a new Backing object for a ExerciseStep event.
     * @return  An instance of ToulminBacking, with the abstract methods implemented for 
     *          dealing with ExerciseStep event.
     */
    public static ToulminBacking  ExerciseStep()
    {
        ToulminBacking bk = new ToulminBacking(OLMQueryResult.EVT_XSTEP)
        {
            
            public String getTemplate(DialoguePlanner planner) 
            {
                return Messages.getString("DlgMove.HereIs.BACKING." + getEventType());  //$NON-NLS-1$
            }

            public ArrayList getArguments(DialoguePlanner planner) 
            {
                ArrayList list = new ArrayList();
                
                ToulminAttribute attr = getToulminAttribute(OLMQueryResult.EVIDENCE_DIFF);
                String str = (attr==null) ? null : attr.getValueName();
                list.add(str);
                attr = getToulminAttribute(OLMQueryResult.EVIDENCE_PERF);
                str = (attr==null) ? null : attr.getValueName();
                list.add(str);
                
                return list;
            }
            
        };
        
        return bk;
    }
    
    /**
     * Create a new Backing object for a SelfReport event.
     * @return  An instance of ToulminBacking, with the abstract methods implemented for 
     *          dealing with SelfReport event.
     */
    public static ToulminBacking SelfReport()
    {
        ToulminBacking bk = new ToulminBacking(OLMQueryResult.EVT_SELFREPORT)
        {

            /* (non-Javadoc)
             * @see toulmin.ToulminBacking#getTemplate(dialogue.DialoguePlanner)
             */
            public String getTemplate(DialoguePlanner planner) 
            {
                return Messages.getString("DlgMove.HereIs.BACKING." + getEventType());  //$NON-NLS-1$
            }

            /* (non-Javadoc)
             * @see toulmin.ToulminBacking#getArguments(dialogue.DialoguePlanner)
             */
            public ArrayList getArguments(DialoguePlanner planner)
            {
                ArrayList list = new ArrayList();
                Hashtable prop = new Hashtable();
                
                Integer vv = (Integer)getValue(OLMQueryResult.EVIDENCE_LIKING);
                if (vv!=null) prop.put(OLMQueryResult.EVIDENCE_LIKING.toLowerCase(),vv);
                vv = (Integer)getValue(OLMQueryResult.EVIDENCE_PRIDE);
                if (vv!=null) prop.put(OLMQueryResult.EVIDENCE_PRIDE.toLowerCase(),vv);
                vv = (Integer)getValue(OLMQueryResult.EVIDENCE_SATISFACTION);
                if (vv!=null) prop.put(OLMQueryResult.EVIDENCE_SATISFACTION.toLowerCase(),vv);

                
                BeliefDesc bdesc = this.toulmin.getBeliefDesc();
                
                Integer val = (Integer)prop.get((String)bdesc.get(4));
                if (val==null)
                {
                    String str = prop.keySet().toString().replace('[',' ').replace(']',' ').replace(',','/');
                    list.add(str);
                    str = prop.values().toString().replace('[',' ').replace(']',' ').replace(',','/');
                    list.add(str);
                }
                else
                {
                    list.add((String)bdesc.get(4));
                    list.add(val);
                }
                return list;
            }
            
        }; 
        return bk;
    }
    
    
    /**
     * Create a new Backing object for a OLMMetacog event.
     * @return  An instance of ToulminBacking, with the abstract methods implemented for 
     *          dealing with OLMMetacog event.
     */
    public static ToulminBacking OLMMetacog()
    {
        ToulminBacking bk = new ToulminBacking(OLMQueryResult.EVT_METACOG)
        {

            public String getTemplate(DialoguePlanner planner) 
            {
                return Messages.getString("DlgMove.HereIs.BACKING." + getEventType());  //$NON-NLS-1$
            }

            public ArrayList getArguments(DialoguePlanner planner) 
            {
                ArrayList list = new ArrayList();

                Double db = (Double) getValue(OLMQueryResult.EVIDENCE_DEPTH);
                NumberFormat format = new DecimalFormat("###%");    //$NON-NLS-1$
                list.add(format.format(db.doubleValue()));
                //arg[0] = backing.getValue(OLMQueryResult.EVIDENCE_DEPTH);
                String str = (String)getValue(OLMQueryResult.EVIDENCE_DLGMOVE);
                String temp = Messages.getSafeString("ATTRIBUTE." + OLMQueryResult.EVIDENCE_DLGMOVE + "." + str);   //$NON-NLS-1$ //$NON-NLS-2$
                if (temp!=null)
                    list.add(temp);
                else
                    list.add(str);
                list.add(getValue(OLMQueryResult.EVIDENCE_BDESCRIPT));
                
                return list;
            }
        }; 
        return bk;
    }
    
    /**
     * Create a new Backing object for a OLMChallenge event.
     * @return  An instance of ToulminBacking, with the abstract methods implemented for 
     *          dealing with OLMChallenge event.
     */
    public static ToulminBacking OLMChallenge()
    {
        ToulminBacking bk = new ToulminBacking(OLMQueryResult.EVT_CHALLENGE)
        {

            public String getTemplate(DialoguePlanner planner) 
            {
                String move = (String)getValue(OLMQueryResult.EVIDENCE_DLGMOVE);
                DlgMoveID chlg = DlgMoveID.getByName(move);
                
                return Messages.getString("DlgMove.HereIs.BACKING." + getEventType() + "." + chlg); //$NON-NLS-1$ //$NON-NLS-2$
            }

            public ArrayList getArguments(DialoguePlanner planner)
            {
            	//System.out.println("att: " + this.getAttributes());
            	
                ArrayList list = new ArrayList();

                ToulminAttribute attr = getToulminAttribute(OLMQueryResult.EVIDENCE_DLGMOVE);
                //String move = (String)getValue(OLMQueryResult.EVIDENCE_DLGMOVE);
                String move = (String)attr.getValue();
                DlgMoveID chlg = DlgMoveID.getByName(move);
                toulmin.getClaim().getClaimLevel();
                
                attr = getToulminAttribute(OLMQueryResult.EVIDENCE_BDESCRIPT);
                Object obj = attr.getValue();
                if (obj instanceof Collection) 
                {
                    BeliefDesc bdesc = new BeliefDesc((Collection) obj);
                    list.add(bdesc);
                }
                else list.add(obj.toString());
                
                if (DlgMoveID.DISAGREE == chlg)
                {                    
                    //list.add(Messages.getJudgementOn(toulmin.getBeliefDesc(),toulmin.getClaim().getClaimLevel()));
                    attr = getToulminAttribute(OLMQueryResult.EVIDENCE_CHLGLEVEL);
                    //Object lvl = getValue(OLMQueryResult.EVIDENCE_CHLGLEVEL);
                    Object lvl = attr.getValue();
                    attr = getToulminAttribute(OLMQueryResult.EVIDENCE_CHLGOLDLEVEL);
                    //Object oldlvl = getValue(OLMQueryResult.EVIDENCE_CHLGOLDLEVEL);
                    Object oldlvl = attr.getValue();
                    if (oldlvl instanceof Double) 
                    {
                        double db = ((Double)oldlvl).doubleValue();
                        list.add(Messages.getJudgementOn(toulmin.getBeliefDesc(),db));
                    }
                    else list.add(oldlvl.toString());
                    if (lvl instanceof Double) 
                    {
                        double db = ((Double)lvl).doubleValue();
                        list.add(Messages.getJudgementOn(toulmin.getBeliefDesc(),db));
                    }
                    else list.add(lvl.toString());
                }
                else if (DlgMoveID.AGREE == chlg)
                {
                    //list.add(Messages.getJudgementOn(toulmin.getBeliefDesc(),toulmin.getClaim().getClaimLevel()));
                    attr = getToulminAttribute(OLMQueryResult.EVIDENCE_CHLGLEVEL);
                    //Object oldlvl = getValue(OLMQueryResult.EVIDENCE_CHLGOLDLEVEL);
                    Object oldlvl = attr.getValue();
                    if (oldlvl instanceof Double) 
                    {
                        double db = ((Double)oldlvl).doubleValue();
                        list.add(Messages.getJudgementOn(toulmin.getBeliefDesc(),db));
                    }
                    else list.add(oldlvl.toString());
                }
                
                return list;
            }
        }; 
        return bk;
    }
    
    /**
     * Create a new Backing object for a SituationFactorChanged event.
     * @return  An instance of ToulminBacking, with the abstract methods implemented for 
     *          dealing with SituationFactorChanged event.
     */
    public static ToulminBacking SituationFactorChanged()
    {
        ToulminBacking bk = new ToulminBacking(OLMQueryResult.EVT_SITFACT)
        {

            public String getTemplate(DialoguePlanner planner) 
            {
                return Messages.getString("DlgMove.HereIs.BACKING." + getEventType());  //$NON-NLS-1$
            }

            public ArrayList getArguments(DialoguePlanner planner) 
            {
                ArrayList list = new ArrayList();
                Hashtable prop = new Hashtable();
                
                Double vv = (Double)getValue(OLMQueryResult.EVIDENCE_APTITUDE);
                if (vv!=null) prop.put(OLMQueryResult.EVIDENCE_APTITUDE.toLowerCase(),
                        new Integer((int)(vv.doubleValue()*100)));
                vv = (Double)getValue(OLMQueryResult.EVIDENCE_CONFIDENCE);
                if (vv!=null) prop.put(OLMQueryResult.EVIDENCE_CONFIDENCE.toLowerCase(),
                        new Integer((int)(vv.doubleValue()*100)));
                vv = (Double)getValue(OLMQueryResult.EVIDENCE_INTEREST);
                if (vv!=null) prop.put(OLMQueryResult.EVIDENCE_INTEREST.toLowerCase(),
                        new Integer((int)(vv.doubleValue()*100)));
                vv = (Double)getValue(OLMQueryResult.EVIDENCE_EFFORT);
                if (vv!=null) prop.put(OLMQueryResult.EVIDENCE_EFFORT.toLowerCase(),
                        new Integer((int)(vv.doubleValue()*100)));
               
                BeliefDesc bdesc = toulmin.getBeliefDesc();
                
                Integer val = (Integer)prop.get((String)bdesc.get(3));
                if (val==null)
                {
                    String str = prop.keySet().toString().replace('[',' ').replace(']',' ').replace(',','/');
                    list.add(str);
                    str = prop.values().toString().replace('[',' ').replace(']',' ').replace(',','/');
                    list.add(str);
                }
                else
                {
                    list.add((String)bdesc.get(3));
                    list.add(val);
                }
                return list;
            }
        }; 
        return bk;
    }
    
    /**
     * Shortcut used to create a new default attribute (type string, no mapping, 
     * default renderer).
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return             A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute DEFAULT(String attributeId,Object value)
    {
        ToulminAttribute attribute = new ToulminAttribute(ToulminAttribute.TYPE_STRING,attributeId,value);
        return attribute;
    }
    
    /**
     * Shortcut used to create a new attribute without any renderer (ie not displayed in
     * the GUI).
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return             A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute NONE(String attributeId,Object value)
    {
        ToulminAttribute attribute = new ToulminAttribute(ToulminAttribute.TYPE_STRING,attributeId,value);
        attribute.setRenderer(ToulminAttribute.RENDERER_NONE);
        return attribute;
    }

    /**
     * Shortcut used to create a new METER attribute (type double, no mapping, 
     * meter renderer).
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return             A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute METERGREEN(String attributeId,Object value)
    {
        ToulminAttribute attribute = new ToulminAttribute(ToulminAttribute.TYPE_DOUBLE,attributeId,value);
        //attribute.renderer = attribute.new JRendererMeter();
        attribute.setRenderer(ToulminAttribute.RENDERER_METER);
        attribute.setRendererMeterColor(new Color(0,150,67));
        return attribute;
    }
    
    /**
     * Shortcut used to create a new METER attribute (type double, no mapping, 
     * meter renderer).
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute METERRED(String attributeId,Object value)
    {
        ToulminAttribute attribute = new ToulminAttribute(ToulminAttribute.TYPE_DOUBLE,attributeId,value);
        //attribute.renderer = attribute.new JRendererMeter();
        attribute.setRenderer(ToulminAttribute.RENDERER_METER);
        attribute.setRendererMeterColor(new Color(255,64,64));
        return attribute;
    }

    /**
     * Shortcut used to create a new LIST attribute (type string, list renderer).
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return             A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute LIST(String attributeId,Object value)
    {
        ToulminAttribute attribute = new ToulminAttribute(ToulminAttribute.TYPE_STRING,attributeId,value);
        //attribute.renderer = attribute.new JRendererMeter();
        attribute.setRenderer(ToulminAttribute.RENDERER_LIST);
        return attribute;
    }
    
    /**
     * Shortcut used to create a new ENUM attribute (type enum, empty mapping, 
     * default renderer). Note that the mapping list being mandatory, values will 
     * have to be added.
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return             A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute ENUM(String attributeId,Object value)
    {
        ToulminAttribute attribute = new ToulminAttribute(ToulminAttribute.TYPE_ENUM,attributeId,value);
        //attribute.mapping = new ArrayList();
        return attribute;
    }
    
    /**
     * Shortcut used to create a new DIFFICULTY attribute (enum type and icon renderer).
     * The mapping is initialised with the proper values. 
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return             A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute DIFFICULTY(String attributeId,Object value)
    {
        ToulminAttribute attribute = ENUM(attributeId,value);
        attribute.addMapping("very_easy");      //$NON-NLS-1$
        attribute.addMapping("easy");           //$NON-NLS-1$
        attribute.addMapping("medium");         //$NON-NLS-1$
        attribute.addMapping("difficult");      //$NON-NLS-1$
        attribute.addMapping("very_difficult"); //$NON-NLS-1$
        attribute.setRenderer(ToulminAttribute.RENDERER_ICON);
        attribute.setRendererIcons(
                new ImageIcon(attribute.getClass().getResource("/res/star.gif")),       //$NON-NLS-1$
                new ImageIcon(attribute.getClass().getResource("/res/star_mask.gif"))); //$NON-NLS-1$
        
        return attribute;
    }
    
    /**
     * Shortcut used to create a new COMPETENCYLEVEL attribute (enum type and icon renderer).
     * The mapping is initialised with the proper values. 
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return             A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute COMPETENCYLEVEL(String attributeId,Object value)
    {
        ToulminAttribute attribute = ENUM(attributeId,value);
        attribute.addMapping("elementary");         //$NON-NLS-1$
        attribute.addMapping("simple_conceptual");  //$NON-NLS-1$
        attribute.addMapping("multi_step");         //$NON-NLS-1$
        attribute.addMapping("complex");            //$NON-NLS-1$
        attribute.setRenderer(ToulminAttribute.RENDERER_ICON);
        attribute.setRendererIcons(
                new ImageIcon(attribute.getClass().getResource("/res/compet.gif")),         //$NON-NLS-1$
                new ImageIcon(attribute.getClass().getResource("/res/compet_mask.gif")));   //$NON-NLS-1$    
        
        return attribute;
    }

    /**
     * Shortcut used to create a new PERFORMANCE attribute (double type and meter renderer).
     * The mapping is initialised with the proper values. 
     * @param attributeId  The identifier of the attribute.
     * @param value        The value associated with this attribute.
     * @return             A new instance of a Toulmin attribute, properly initialised.
     */
    public static ToulminAttribute PERFORMANCE(String attributeId,Object value)
    {
        ToulminAttribute attribute = METERGREEN(attributeId,value);
        attribute.addMapping("20");     //$NON-NLS-1$
        attribute.addMapping("40");     //$NON-NLS-1$
        attribute.addMapping("60");     //$NON-NLS-1$
        attribute.addMapping("80");     //$NON-NLS-1$
        attribute.addMapping("100");    //$NON-NLS-1$
        //attribute.setRenderer(ToulminAttribute.RENDERER_METER);
        
        return attribute;
    }
        
}
