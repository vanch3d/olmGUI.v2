/**
 * @file OLMDialoguePane.java
 */
package olmgui.output;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import olmgui.utils.OLMFormatTemplate;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.20 $
 *
 */
public class OLMDialoguePane extends JScrollPane {

    /**
     * Scrolling directions
     */
    private static final int
        NONE = 0,
        TOP = 1,
        VCENTER = 2,
        BOTTOM = 4,
        LEFT = 8,
        HCENTER = 16,
        RIGHT = 32;

    ///**
    // * A reference to the main applet
    // */
    //private OLMMainGUI sParent = null;

    /**
     * A reference to the text widget containing the dialogue 
     */
    private JTextPane jTextPane = null;

    /**
     * 
     */
    public OLMDialoguePane() {
        super();

        initialize();
    }
    
    /*public void setParent(OLMMainGUI parent)
    {
        sParent = parent;   
    }*/

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setMinimumSize(new Dimension(50,50));
        this.setViewportView(getJTextPane());
        //CurrentLineHighlighter.install(getJTextPane());
    }

    /**
     * This method initializes jTextPane	
     * 	
     * @return javax.swing.JTextPane	
     * 
     * @todo There is a repaint() bug in Java with the "hanging" configuration;
     *       one way to "solve" it is to force a repaint when the caret changes.
     */     
    private JTextPane getJTextPane()
    {
    	if (jTextPane == null) 
        {
    		jTextPane = new JTextPane();
    		jTextPane.setEditable(false);
            jTextPane.setEnabled(true);
            
            StyleContext context = new StyleContext();
            StyledDocument document = new DefaultStyledDocument(context);
            Style style = context.getStyle(StyleContext.DEFAULT_STYLE);           

            // Create a left-aligned tab stop at 100 pixels from the left margin
            float pos = 50;
            int align = TabStop.ALIGN_LEFT;
            int leader = TabStop.LEAD_NONE;
            TabStop tstop = new TabStop(0, align, leader);
            List list = new ArrayList();
            list.add(tstop);
            
            // Create a tab set from the tab stops
            TabStop[] tstops = (TabStop[])list.toArray(new TabStop[0]);
            TabSet tabs = new TabSet(tstops);

            // Set the style of the document to mimick hanging
            StyleConstants.setTabSet(style,tabs);
            StyleConstants.setFirstLineIndent(style,-pos);
            StyleConstants.setLeftIndent(style,pos);
            
            jTextPane.setDocument(document);
            
            // force repaint in order to deal with wrong "hanging"
            jTextPane.addCaretListener(new javax.swing.event.CaretListener() {
                public void caretUpdate(javax.swing.event.CaretEvent e) {
                    jTextPane.repaint();
                }
            });
            

    	}
    	return jTextPane;
    }
    
    public void addUserDialogue(String user,String str)
    {
        //addItem(user,str);
        StyledDocument doc = (StyledDocument)getJTextPane().getDocument();
        OLMFormatTemplate.addItem(doc, user, str, null);
        scroll(BOTTOM);
    }
    
    public void addUserDialogue(String user,String template,Object[] param)
    {
        //addItem(user,template,param);
        StyledDocument doc = (StyledDocument)getJTextPane().getDocument();
        OLMFormatTemplate.addItem(doc, user, template, param);
        scroll(BOTTOM);
    }
    
    private void scroll(int part)
    {
        scroll(getJTextPane(),part & (LEFT|HCENTER|RIGHT), part & (TOP|VCENTER|BOTTOM));
    }
    
    private void scroll(JComponent c,int horizontal, int vertical)
    {
        Rectangle visible = c.getVisibleRect();
        Rectangle bounds = c.getBounds();
      
        switch (vertical)
        {
            case TOP:     
                visible.y = 0; 
                break;
            case VCENTER: 
                visible.y = (bounds.height - visible.height) / 2; 
                break;
            case BOTTOM:  
                visible.y = bounds.height - visible.height +20; 
                break;
        }
    
        switch (horizontal)
        {
            case LEFT:    visible.x = 0; break;
            case HCENTER: visible.x = (bounds.width - visible.width) / 2; break;
            case RIGHT:   visible.x = bounds.width - visible.width; break;
        }
    
        c.scrollRectToVisible(visible);
    }
    
    /*public void addOLMDialogue(String str)
    {
        addItem(OLMUSER,str);
    }*/

//    /**
//     * @param user
//     * @param template
//     * @param param
//     */
//    private void addItem(String user,String template,Object[] param)
//    {
//        StyledDocument doc = (StyledDocument)getJTextPane().getDocument();
//
//        //Create a style object and then set the style attributes
//        Style styleUser = doc.addStyle("OLMUser", null);
//        StyleConstants.setBold(styleUser, true);
//        if (user.equals("OLM"))
//            StyleConstants.setForeground(styleUser, Color.red);
//        else
//            StyleConstants.setForeground(styleUser, Color.blue);
//
//        Style styleParam = doc.addStyle("Param", null);
//        StyleConstants.setBold(styleParam, true);
//        StyleConstants.setForeground(styleParam, Color.black);
//
////        TopicIcon[] dd ={
////                new TopicIcon(OLMTopicConfig.DOMAIN,true),
////                new TopicIcon(OLMTopicConfig.COMPET,true),
////                new TopicIcon(OLMTopicConfig.MOTIV,true),
////                new TopicIcon(OLMTopicConfig.AFFECT,true),
////                new TopicIcon(OLMTopicConfig.METACOG,true),
////                new TopicIcon(OLMTopicConfig.CAPES,true)
////        };
//        Color[] dd2 ={
//                OLMTopicConfig.DOMAIN.getColor(),
//                OLMTopicConfig.COMPET.getColor(),
//                OLMTopicConfig.MOTIV.getColor(),
//                OLMTopicConfig.AFFECT.getColor(),
//                OLMTopicConfig.METACOG.getColor(),
//                OLMTopicConfig.CAPES.getColor()
//        };
//        
//
//        Style styleTopic = doc.addStyle("Topic", null);
//        StyleConstants.setBold(styleTopic, true);
//        StyleConstants.setForeground(styleTopic, Color.black);
//        StyleConstants.setBackground(styleTopic,dd2[0]);
//        //StyleConstants.setIcon(styleTopic, dd[0]);
//                
//        // Write the user name
//        try {
//            doc.insertString(doc.getLength(), user + "\t", styleUser);
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }
//        
//        // format the template
//        OLMMessageFormat mf = new OLMMessageFormat(template);
//        //String res = mf.format(param);
//        int nb = mf.elements.length;
//        
//        // alternates between plain text (leader/trailer) and param
//        try {
//        	
//        	// Make sure first car is Uppercase
//        	String str = mf.leader;
//            StringBuffer buf = new StringBuffer(mf.leader);
//            if (buf.length()>0)
//            {
//            	String first = buf.substring(0,1);
//            	first = first.toUpperCase();
//            	buf.replace(0,1,first);
//            	str = buf.toString();
//            }
//        	
//            doc.insertString(doc.getLength(), str, null);
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }
//        
//        for (int i=0;i<nb;i++)
//        {
//            OLMMessageFormat.MessageFormatElement elt = mf.elements[i];
//            Format fm = elt.getFormat();
//            int nbarg = elt.getArgNumber();
//            String resf = null;
//            
//            if (param!=null && param[nbarg] instanceof BeliefDesc) 
//            {
//                BeliefDesc desc = (BeliefDesc) param[nbarg];
//                String mss = Messages.getDescriptionTemplate(desc);
//                
//                // Make sure first/second person is respected in the verbalisation
//                // of the descriptor [should not occur anywhere else]
//                if (!user.equals("OLM"))
//                {
//                	String cOLM = Messages.getString("DESCRIPTOR.CASE.OLM");
//                	String cUser = Messages.getString("DESCRIPTOR.CASE.USER");
//                    mss = mss.replaceAll(cOLM,cUser);
//                }
//                    
//                
//                OLMMessageFormat mf2 = new OLMMessageFormat(mss);
//                Object[] gg= desc.getArgs();
//                int nb2 = mf2.elements.length;
//                try {
//                    doc.insertString(doc.getLength(), mf2.leader, null);
//                } catch (BadLocationException e) {
//                    e.printStackTrace();
//                }
//                for (int j=0;j<nb2;j++)
//                {
//                    OLMMessageFormat.MessageFormatElement elt2 = mf2.elements[j];
//                    Format fm2 = elt2.getFormat();
//                    int nbarg2 = elt2.getArgNumber();
//                    if (fm2==null && gg[nbarg2]!=null)
//                        resf = gg[nbarg2].toString();
//                    else
//                        resf = fm2.format(gg[nbarg2]);
//        
//                    try {
//                        //StyleConstants.setIcon(styleTopic, dd[nbarg2]);
//                        StyleConstants.setBackground(styleTopic,dd2[nbarg2]);
//                        doc.insertString(doc.getLength(), resf, styleTopic);
//                        //doc.insertString(doc.getLength(), resf, styleParam);
//                        doc.insertString(doc.getLength(), elt2.getTrailer(), null);
//                    } catch (BadLocationException e) {
//                        e.printStackTrace();
//                    }
//                   
//                }
//                try {
//                    doc.insertString(doc.getLength(), elt.getTrailer(), null);
//                } catch (BadLocationException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            else
//            {
//                if (fm==null && param!=null && param[nbarg]!=null)
//                    resf = param[nbarg].toString();
//                else if (param!=null && fm!=null)
//                    resf = fm.format(param[nbarg]);
//                else 
//                    resf = ("");
//    
//                try {
//                    doc.insertString(doc.getLength(), resf, styleParam);
//                    doc.insertString(doc.getLength(), elt.getTrailer(), null);
//                } catch (BadLocationException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        try {
//            doc.insertString(doc.getLength(), "\n", null);
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }
//        
//        
//    }    
//    /**
//     * @param user The identifier of the user.
//     * @param str The string to add in the display.
//     */
//    private void addItem(String user,String str)
//    {
//        StyledDocument doc = (StyledDocument)getJTextPane().getDocument();
//
//        StringBuffer buf = new StringBuffer(str);
//        String first = buf.substring(0,1);
//        first = first.toUpperCase();
//        buf.replace(0,1,first);
//        str = buf.toString();
//        
//        //Create a style object and then set the style attributes
//        Style style = doc.addStyle("OLMUser", null);
//
//        StyleConstants.setBold(style, true);
//        if (user.equals("OLM"))
//            StyleConstants.setForeground(style, Color.red);
//        else
//            StyleConstants.setForeground(style, Color.blue);
//        
//        try {
//            doc.insertString(doc.getLength(), user + "\t", style);
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }
//        
//        try {
//            doc.insertString(doc.getLength(),str+"\n",null);
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }
//      
//    }
    
    public void setDialogueEnabled(boolean show)
    {
    	this.setVisible(show);
    }
    

}
