package olmgui.utils;

import java.awt.Color;
import java.text.Format;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import olmgui.i18n.Messages;
import toulmin.BeliefDesc;
import config.OLMTopicConfig;

public class OLMFormatTemplate 
{
    // Belief Descriptor Style 
    private static Color[] layers ={
            OLMTopicConfig.DOMAIN.getColor(),
            OLMTopicConfig.COMPET.getColor(),
            OLMTopicConfig.MOTIV.getColor(),
            OLMTopicConfig.AFFECT.getColor(),
            OLMTopicConfig.METACOG.getColor(),
            OLMTopicConfig.CAPES.getColor()
    };
    
    private static Style styleUser = null;
    private static Style styleParam = null;
    private static Style styleTopic = null;
    
    private static void checkStyles(StyledDocument doc)
    {
        // Create the style objects and set the attributes
    	// User Style
    	Style test= doc.getStyle("OLMUser");
    	if (test != null) return;

    	styleUser = doc.addStyle("OLMUser", null);
		StyleConstants.setBold(styleUser, true);

        // Parameter Style
        styleParam = doc.addStyle("Param", null);
        StyleConstants.setBold(styleParam, true);
        StyleConstants.setForeground(styleParam, Color.black);

        styleTopic = doc.addStyle("Topic", null);
        StyleConstants.setBold(styleTopic, true);
        StyleConstants.setForeground(styleTopic, Color.black);
        StyleConstants.setBackground(styleTopic,layers[0]);
    }

    public static void formatTemplate(StyledDocument doc,String user,String template,Object[] param)
    {
    	checkStyles(doc);

    	// format the template and retrieve the various formatting elements
        OLMMessageFormat mf = new OLMMessageFormat(template);
        
        // Print the leader part of the formatted string 
        try
        {
        	String str = mf.leader;
            StringBuffer buf = new StringBuffer(mf.leader);
            if (buf.length()>0)
            {
            	// Make sure first car is Uppercase
            	String first = buf.substring(0,1);
            	first = first.toUpperCase();
            	buf.replace(0,1,first);
            	str = buf.toString();
            }
            doc.insertString(doc.getLength(), str, null);
        } 
        catch (BadLocationException e) {e.printStackTrace();}

        // Alternatively print argument/trailer 
        int nb = mf.elements.length;
        for (int i=0;i<nb;i++)
        {
            OLMMessageFormat.MessageFormatElement elt = mf.elements[i];
            Format fm = elt.getFormat();
            int nbarg = elt.getArgNumber();
            
            if (param!=null && param[nbarg] instanceof BeliefDesc) 
            {
                BeliefDesc desc = (BeliefDesc) param[nbarg];
                formatDescriptor(desc,doc,user);
                try 
                {
                    doc.insertString(doc.getLength(), elt.getTrailer(), null);
                }
                catch (BadLocationException e) {e.printStackTrace();}    	
            }
            else
            {
                String resf = null;
                if (fm==null && param!=null && param[nbarg]!=null)
                    resf = param[nbarg].toString();
                else if (param!=null && fm!=null)
                    resf = fm.format(param[nbarg]);
                else 
                    resf = ("");
    
                try 
                {
                    doc.insertString(doc.getLength(), resf, styleParam);
                    doc.insertString(doc.getLength(), elt.getTrailer(), null);
                }
                catch (BadLocationException e) {e.printStackTrace();}
            }
        }    	
    }

    public static void addItem(StyledDocument doc,String user,String template,Object[] param)
    {
    	checkStyles(doc);
    	
        // Write the user name
        if (user.equals("OLM"))
            StyleConstants.setForeground(styleUser, Color.red);
        else
            StyleConstants.setForeground(styleUser, Color.blue);

        try
        {
            doc.insertString(doc.getLength(), user + "\t", styleUser);
        } 
        catch (BadLocationException e) {e.printStackTrace();}
        
        formatTemplate(doc,user,template,param);
        
        try 
        {
            doc.insertString(doc.getLength(), "\n", null);
        }
        catch (BadLocationException e) {e.printStackTrace();}
    }
    
    private static void formatDescriptor(BeliefDesc desc,StyledDocument doc,String user)
    {
        String mss = Messages.getDescriptionTemplate(desc);
        
        // Make sure first/second person is respected in the verbalisation
        // of the descriptor [should not occur anywhere else]
        if (!user.equals("OLM"))
        {
        	String cOLM = Messages.getString("DESCRIPTOR.CASE.OLM");
        	String cUser = Messages.getString("DESCRIPTOR.CASE.USER");
            mss = mss.replaceAll(cOLM,cUser);
        }
            
        
        OLMMessageFormat mf2 = new OLMMessageFormat(mss);
        Object[] gg= desc.getArgs();
        int nb2 = mf2.elements.length;
        try {
            doc.insertString(doc.getLength(), mf2.leader, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        for (int j=0;j<nb2;j++)
        {
            OLMMessageFormat.MessageFormatElement elt2 = mf2.elements[j];
            Format fm2 = elt2.getFormat();
            int nbarg2 = elt2.getArgNumber();
            String resf = null;
            if (fm2==null && gg[nbarg2]!=null)
                resf = gg[nbarg2].toString();
            else
                resf = fm2.format(gg[nbarg2]);

            try {
                StyleConstants.setBackground(styleTopic,layers[nbarg2]);
                doc.insertString(doc.getLength(), resf, styleTopic);
                doc.insertString(doc.getLength(), elt2.getTrailer(), null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
           
        }
    }
}
