package olmgui.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import config.OLMTopicConfig;

public class TopicMaps
{
    private static Locale resLocale=null;
    
    private static final String BUNDLE_NAME = "olmgui.i18n.topicmaps"; //$NON-NLS-1$
    
    private TopicMaps()
    {
        setLocale(Locale.ENGLISH);
    }

    public static ResourceBundle getBundle()
    {
        if (resLocale==null)
            setLocale(Locale.ENGLISH);
        return ResourceBundle.getBundle(BUNDLE_NAME,resLocale);
    }

    public static void setLocale(Locale resLocale) 
    {
        if (resLocale==null)
            TopicMaps.resLocale = Locale.ENGLISH;
        else
            TopicMaps.resLocale = resLocale;
   }

    public static String getString(String key) {
        try {
            return getBundle().getString(key);
        } catch (RuntimeException e) {
            //e.printStackTrace();
            System.err.println("Can't find resource for bundle olmgui.i18n.TopicMaps, key " + key);
            return '!' + key + '!';
        }
       
    }
    
    public static String getSafeString(String key) {
        try {
            return getBundle().getString(key);
        } catch (RuntimeException e) {
            return null;
        }
       
    }

    
    public static String getTitle(OLMTopicConfig config,String id) 
    {
        String title = getSafeString(config.toString() + "." + id + ".title");
        if (title==null)
        	System.err.println("Can't find resource for bundle olmgui.i18n.TopicMaps, key " + config.toString() + "." + id + ".title");
        return title;
    }

    public static String getDescription(OLMTopicConfig config,String id) 
    {
        return getSafeString(config.toString() + "." + id + ".description");
    }



}
