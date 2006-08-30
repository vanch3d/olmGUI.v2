/**
 * @file Messages.java
 */
package olmgui.i18n;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import toulmin.BeliefDesc;
import config.OLMTopicConfig;


/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.21 $
 */
public class Messages {
    private static Locale resLocale=null;
    
    private static Random generator = new Random();

    private static final String BUNDLE_NAME = "olmgui.i18n.messages"; //$NON-NLS-1$

//   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
//            .getBundle(BUNDLE_NAME,resLocale);

        
    private Messages() {
        setLocale(Locale.ENGLISH);
    }

    public static ResourceBundle getBundle()
    {
        if (resLocale==null)
            setLocale(Locale.ENGLISH);
        return ResourceBundle.getBundle(BUNDLE_NAME,resLocale);
    }
    /**
     * @param key   The key for the desired string.
     * @return  The string from the bundle associated with the given key,
     *          an error message if the string does not exist.          
     */
    public static String getString(String key) {
        try {
            return getBundle().getString(key);
        } catch (RuntimeException e) {
            //e.printStackTrace();
            System.err.println(e.getMessage());
            return '!' + key + '!';
        }
       
    }
    
    public static String getRandomString(String key)
    {
        String str = "";
        int index = 1;
        ArrayList templates = new ArrayList();
        templates.add(getString(key));
        
        while (str !=null)
        {
            str = getSafeString(key + "." + index);
            if (str!=null)
                templates.add(str);
            index++;
        }
        index = generator.nextInt(templates.size());
        str = (String)templates.get(index);
        return str;
    }

    /**
     * @param key   The key for the desired string.
     * @return  The string from the bundle associated with the given key,
     *          <code>null</code> if the string does not exist.          
     */
    public static String getSafeString(String key) {
        try {
            return getBundle().getString(key);
        } catch (RuntimeException e) {
            return null;
        }
       
    }
    
    /**
     * @param resLocale The Locale to set for this resource bundle.
     */
    public static void setLocale(Locale resLocale) 
    {
    	if (resLocale==null)
    		Messages.resLocale = Locale.ENGLISH;
    	else
            Messages.resLocale = resLocale;
    	//System.out.println("LOCALE = " + resLocale.toString());
   }
        
    public static String getJudgementOn(BeliefDesc beliefDesc,double summary)
    {
        OLMTopicConfig topic = OLMTopicConfig.getTopicFromDescriptor(beliefDesc);
        if (topic==null) return null;
        
        int lvl = (int) Math.round((summary*3)+1);
        String template = Messages.getString("OLMTopicConfig." + topic.toString() + ".Level" + lvl);  //$NON-NLS-1$
        return template;
    }
    
    public static String getJudgementOn(BeliefDesc beliefDesc,int level)
    {
        OLMTopicConfig topic = OLMTopicConfig.getTopicFromDescriptor(beliefDesc);
        if (topic==null) return null;
        
        String template = Messages.getString("OLMTopicConfig." + topic.toString() + ".Level" + level);  //$NON-NLS-1$
        return template;
    }    
    
    public static String getLayerOn(BeliefDesc beliefDesc)
    {
        OLMTopicConfig topic = OLMTopicConfig.getTopicFromDescriptor(beliefDesc);
        String template = Messages.getString("OLMTopicConfig." + topic.toString() + ".Name");  //$NON-NLS-1$
        return template;
        
    }
    
    
    /**
     * Build a template describing the given belief descriptor by combining the various 
     * layer's description into a single string.
     * @return A String containing the template mapping the belief descriptor.
     */
    public static String getDescriptionTemplate(BeliefDesc beliefDesc)
    {
      //String dom = (String)beliefDesc.get(0);
      String comp = (String)beliefDesc.get(2);
      String motiv = (String)beliefDesc.get(3);
      String affect = (String)beliefDesc.get(4);
      String metac = (String)beliefDesc.get(5);
      String capes = (String)beliefDesc.get(1);
      
      //TopicWrapper wp = null;
      
      String str = "";
      if (!capes.equals(""))
          str = getString("DESCRIPTOR.CAPES") + " ";//"the buggy-rule <b>{5}</b> on ";
      else
      {
          if (!metac.equals(""))
          {
              str = getString("DESCRIPTOR.METACOG") + " ";//"your <b>{4}</b> of ";
          }
          if (!affect.equals(""))
          {
              str = str + getString("DESCRIPTOR.AFFECT") + " ";//"your <b>{3}</b> of ";
          }
          else if (!motiv.equals(""))
          {
              str = str + getString("DESCRIPTOR.MOTIV") + " ";//"your <b>{2}</b> in ";
          }
          if (!comp.equals(""))
          {
              str = str + getString("DESCRIPTOR.COMPET") + " ";//"your ability to <b>{1}</b> on ";
          }
      }

      str = str + getString("DESCRIPTOR.DOMAIN");//"<b>{0}</b>";
      return str;
    }

//    public static String getDescriptionOn(BeliefDesc beliefDesc)
//    {
//        String str = getDescriptionTemplate(beliefDesc);
//        String template = MessageFormat.format(str,beliefDesc.getArgs());
//        return template;
//    }
//    
//    public static String getDescriptionOnHTML(BeliefDesc beliefDesc)
//    {
//        String str = getDescriptionTemplate(beliefDesc);
//        String template = MessageFormat.format(str,beliefDesc.getHTMLArgs());
//        return template;
//    }
}
