package toulmin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import olmgui.utils.TopicWrapper;
import config.OLMTopicConfig;

/**
 * A lightweight implementation of a belief descriptor.
 *
 * @author Nicolas Van Labeke
 * @version $Revision: 1.6 $
 *
 * @todo the Descriptor does not contain any knowledge about the semantic of its elements;
 * 		 it may be a good idea to change that.
 */
public class BeliefDesc extends ArrayList implements XMLRPCWrapper
{
	/**
	 * Default constructor.
	 */
	public BeliefDesc() 
	{
		super();
	}

	/**
	 * Create a Belief Descriptor based on another Collection.
	 * @param c	The collection from which each element will be duplicated in the Descriptor.
	 */
	public BeliefDesc(Collection c)
	{
		super(c);
	}

	/* (non-Javadoc)
	 * @see toulmin.XMLRPCWrapper#toXMLRPC()
	 */
	public Object toXMLRPC() 
	{
		Vector vec= new Vector(this);
		return vec;
	}
	
	/**
	 * Create a new instance of a Belief Descriptor based on the XML-RPC compliant object.
	 * @param obj	The object containing the belief descriptor.
	 * @return	A new instance of a Belief Descriptor, filled with the data from the object.
	 */
	public static BeliefDesc fromXMLRPC(Object obj) 
	{
		BeliefDesc bdesc= null;
		
		if (obj instanceof Vector) 
		{
			bdesc = new BeliefDesc();
			Iterator iter = ((Vector)obj).iterator();
			while (iter.hasNext())
			{
				bdesc.add(iter.next());
			}
		}
		return bdesc;
	}
    
    /**
     * Build a list of the title of the topics included in the descriptor.
     * @return a list of String containing the name of the topics, 
     *         null if there is none.
     */
    public Object[] getArgs()
    {
        String dom = (String)get(0);
        String capes = (String)get(1);
        String comp = (String)get(2);
        String motiv = (String)get(3);
        String affect = (String)get(4);
        String metac = (String)get(5);
        
        TopicWrapper wp = (TopicWrapper)OLMTopicConfig.METACOG.getModel().findElement(metac);
        if (wp!=null) 
        {
            String tt =wp.getTitle(); 
            if (tt!=null && !tt.equals("")) metac = tt; 
        }
        wp = (TopicWrapper)OLMTopicConfig.DOMAIN.getModel().findElement(dom);
        if (wp!=null) 
        {
            String tt =wp.getTitle(); 
            if (tt!=null && !tt.equals("")) dom = tt; 
        }
        wp = (TopicWrapper)OLMTopicConfig.COMPET.getModel().findElement(comp);
        if (wp!=null) 
        {
            String tt =wp.getTitle(); 
            if (tt!=null && !tt.equals("")) comp = tt; 
        }
        wp = (TopicWrapper)OLMTopicConfig.MOTIV.getModel().findElement(motiv);
        if (wp!=null) 
        {
            String tt =wp.getTitle(); 
            if (tt!=null && !tt.equals("")) motiv = tt; 
        }
        wp = (TopicWrapper)OLMTopicConfig.AFFECT.getModel().findElement(affect);
        if (wp!=null) 
        {
            String tt =wp.getTitle(); 
            if (tt!=null && !tt.equals("")) affect = tt; 
        }
        wp = (TopicWrapper)OLMTopicConfig.CAPES.getModel().findElement(capes);
        if (wp!=null) 
        {
            String tt =wp.getTitle(); 
            if (tt!=null && !tt.equals("")) capes = tt; 
        }

        Object[] arg={
          dom,
          comp,
          motiv,
          affect,
          metac,
          capes
        };

        return arg;
    }
}
