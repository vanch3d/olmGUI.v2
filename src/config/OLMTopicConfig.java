/**
 * @file OLMTopicConfig.java
 */
package config;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import olmgui.i18n.Messages;
import olmgui.utils.TopicListModel;
import toulmin.BeliefDesc;


/**
 * This class contains the configuration of the various topic maps used throughout the OLM.
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.10 $
 *
 */
public final class OLMTopicConfig 
{
    /**
     * Associate the names of the topic map with their OLMTopicConfig instance.
     */
    static private final Map configMap = new HashMap(); // config name -> OLMTopicConfig
    
    /**
     * The identifier of the topic map
     */
    private String mapId;
    
    /**
     * The default colour used to materialise this topic map
     */
    private Color mapColor;
    
    /**
     * The list of topic belonging to the map.
     */
    private TopicListModel model;
    
    /**
     * TRUE is the map is visible in the OLM, FALSE otherwise.
     */
    private boolean mapVisible;
       
    /**
     * Definition of the Domain topic map.
     */
    public static final OLMTopicConfig DOMAIN = 
        new OLMTopicConfig("DOMAIN",new Color(232,238,247));    //$NON-NLS-1$
    
    /**
     * Definition of the Competency topic map.
     */
    public static final OLMTopicConfig COMPET = 
        new OLMTopicConfig("COMPET",new Color(255,255,153));    //$NON-NLS-1$

    /**
     * Definition of the Motivation topic map.
     */
    public static final OLMTopicConfig MOTIV = 
        new OLMTopicConfig("MOTIV",new Color(255,174,215)); //$NON-NLS-1$

    /**
     * Definition of the Affect topic map.
     */
    public static final OLMTopicConfig AFFECT = 
        new OLMTopicConfig("AFFECT",new Color(255,174,215)); //$NON-NLS-1$   

    /**
     * Definition of the Metacognition topic map.
     */
    public static final OLMTopicConfig METACOG = 
        new OLMTopicConfig("METACOG",new Color(172,255,132)); //$NON-NLS-1$

    
    /**
     * Definition of the Misconception topic map.
     */
    public static final OLMTopicConfig CAPES = 
        new OLMTopicConfig("CAPES",new Color(255,145,145)); //$NON-NLS-1$

    /**
     * Definition of the Belief Descriptors "map".
     */
    public static final OLMTopicConfig BDESC = 
        new OLMTopicConfig("BDESC",new Color(27,255,192)); //$NON-NLS-1$
    
  
    /**
     * 
     * @param configName A string containing the name of one of the topic map
     * @return  An instance of the OLMTopicConfig corresponding to the given string, null is none.
     */
    public static final OLMTopicConfig getByName(String configName)
    {
        return (OLMTopicConfig) OLMTopicConfig.configMap.get(configName);
    }

    /**
     * Construct an instance of OLMTopicConfig given a name for the map and its default colour.
     * @param id    The unique identifier for the map.
     * @param clr   The default colour associated with the map.
     */
    private OLMTopicConfig(String id, Color clr) 
    {
        this.mapId = id;
        this.mapColor = clr;
        this.mapVisible = true;
        OLMTopicConfig.configMap.put(id, this);

    }
    
    /**
     * Return the colour associated with this topic map.
     * @return  The colour associated with this topic map.
     */
    public Color getColor()
    {
        return this.mapColor;
    }
        
    /**
     * Return the name of this topic map.
     * @return  A String containing the name of this topic map.
     */
    public String getName()
    {
        String name = Messages.getString("OLMTopicConfig." + this.mapId + ".Name"); //$NON-NLS-1$//$NON-NLS-2$
        return name;
    }

    /**
     * Get the topic map associated with a belief descriptor.
     * The map is identified by the uppermost layer in the descriptor.
     * @param beliefDesc    The belief descriptor to identify.
     * @return  The instance of the topic map, whose descriptor is about.    
     */
    public static OLMTopicConfig getTopicFromDescriptor(BeliefDesc beliefDesc)
    {
        if (beliefDesc==null) return null;
        String metacog = (String)beliefDesc.get(5);
        String affect = (String)beliefDesc.get(4);
        String motiv = (String)beliefDesc.get(3);
        String comp = (String)beliefDesc.get(2);
        String capes = (String)beliefDesc.get(1);
        //String domain = (String)beliefDesc.get(0);
        
        if (metacog!=null && metacog.length()!=0) return OLMTopicConfig.METACOG;
        else if (affect!=null && affect.length()!=0) return OLMTopicConfig.AFFECT;
        else if (motiv!=null && motiv.length()!=0) return OLMTopicConfig.MOTIV;
        else if (comp!=null && comp.length()!=0) return OLMTopicConfig.COMPET;
        else if (capes!=null && capes.length()!=0) return OLMTopicConfig.CAPES;
        else return OLMTopicConfig.DOMAIN;
    }

    /**
     * @return Returns the mapVisible.
     */
    public boolean isVisible() 
    {
        return mapVisible;
    }

    /**
     * Set the list of topics for this map.
     * Note that this class mimicks a type-safe enumeration. Each type being final, 
     * this list of topics will be initialise only ONCE. So better make sure to do 
     * it once you know you have retrieve all topics server-side. 
     * 
     * @param md    A reference to the list model
     * 
     */
    public void setModel(TopicListModel md)
    {
        model = md;
    }
    
    /**
     * Get the list of topics associated with this map.
     * @return  A reference to the list model.
     */
    public TopicListModel getModel()
    {
        if (model==null)
            return new TopicListModel(OLMTopicConfig.DOMAIN);
        else return model;
    }
    
    /**
     * @param mapVisible The mapVisible to set.
     */
    public void setVisible(boolean visible) 
    {
        this.mapVisible = visible;
    }
    
    

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.mapId;
    }
    
}
