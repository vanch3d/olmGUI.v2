package olmgui.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import toulmin.BeliefDesc;
import config.OLMTopicConfig;
   
   public class FilteringJList extends JList {
//     private JTextField input;
   
	   private BeliefDesc desc = null;
	   
   public FilteringJList() {
     //FilteringModel model = new FilteringModel();
     setModel(new FilteringModel());
   }
   
   /**
    * Associates filtering document listener to text
    * component.
    */
   
//    public void installJTextField(JTextField input) {
//      if (input != null) {
//        this.input = input;
//        FilteringModel model = (FilteringModel)getModel();
//        input.getDocument().addDocumentListener(model);
//      }
//    }
   
   /**
    * Disassociates filtering document listener from text
    * component.
    */
   
//    public void uninstallJTextField(JTextField input) {
//      if (input != null) {
//        FilteringModel model = (FilteringModel)getModel();
//        input.getDocument().removeDocumentListener(model);
//        this.input = null;
//      }
//    }
   
   /**
    * Doesn't let model change to non-filtering variety
    */
   
   public void setModel(ListModel model) {
     if (!(model instanceof FilteringModel)) {
       throw new IllegalArgumentException();
     } else {
       super.setModel(model);
     }
   }
   
   /**
    * Adds item to model of list
    */
   public void addElement(Object element) {
     ((FilteringModel)getModel()).addElement(element);
   }
   
   
   public void setFilter(BeliefDesc desc)
   {
	   this.desc = desc;
	   ((FilteringModel)getModel()).filter(desc);
   }
   
   /**
    * The topic-to-topic comparison operator.
    * Sorting the topics in a list is done by alphabetical order on their ID/title, 
    * except that the ROOT node (if any) is always put at the top.
    * @see TopicWrapper
    */
   private static Comparator BDESC_COMPARATOR = new Comparator()
       {
           public int compare( Object o1, Object o2 )
           {
               String str1 = o1.toString();
               String str2 = o2.toString();
               Collator collator = Collator.getInstance();
               int result = collator.compare( str1, str2 );
               return result;
           }
       };
   /**
    * Manages filtering of list model
    */
   
   public class FilteringModel extends AbstractListModel
       implements DocumentListener {
	   SortedSet list;
	   SortedSet filteredList;
	   
	   BeliefDesc lastFilter = null;
   
     public FilteringModel() {
       list = new TreeSet(BDESC_COMPARATOR);
       filteredList = new TreeSet(BDESC_COMPARATOR);
     }
   
     public void addElement(Object element) 
     {
         if (element instanceof DescriptorListWrapper) {
            DescriptorListWrapper wraper = (DescriptorListWrapper) element;
            BeliefDesc ff = new BeliefDesc();
            for (int i=0;i<6;i++) ff.add("");
            boolean contains = true;
            for (int j=0;j<wraper.bdesc.size();j++)
            {
                ff.set(j,wraper.bdesc.get(j));
                OLMTopicConfig cfg = OLMTopicConfig.getTopicFromDescriptor(ff);
                if (!cfg.isVisible())
                {
                    contains = false;
                    break;
                }
            }
            
            if (contains) list.add(element);
                   }
       //filter(lastFilter);
     }
   
     public int getSize() {
       return filteredList.size();
     }
   
     public Object getElementAt(int index) {
       Object returnValue;
       if (index!=-1 && index < filteredList.size()) {
         returnValue = filteredList.toArray()[index];
       } else {
         returnValue = null;
       }
       return returnValue;
     }
   
     void filter(BeliefDesc search) {
       filteredList.clear();
       for (int i=0;i<list.size();i++)
       {
           DescriptorListWrapper element = (DescriptorListWrapper)list.toArray()[i];
           boolean contains = true;

           if (search != null)
           {
               for (int j=0;j<search.size() && contains;j++)
               {
                   String ss = (String)search.get(j);
                   if (ss==null || ss.length()==0) continue;
                   boolean is = element.bdesc.contains(ss);
                   contains = contains & is;
               }
               if (contains)
                   filteredList.add(element);
           }
           else if (contains)
             filteredList.add(element);
       }

       fireContentsChanged(this, 0, getSize());
       }
   
     // DocumentListener Methods
   
     public void insertUpdate(DocumentEvent event) {
//       Document doc = event.getDocument();
//       try {
//         lastFilter = doc.getText(0, doc.getLength());
//         filter(lastFilter);
//       } catch (BadLocationException ble) {
//         System.err.println("Bad location: " + ble);
//       }
     }
   
     /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent event) {
//       Document doc = event.getDocument();
//       try {
//         lastFilter = doc.getText(0, doc.getLength());
//         filter(lastFilter);
//       } catch (BadLocationException ble) {
//         System.err.println("Bad location: " + ble);
//       }
     }
   
     public void changedUpdate(DocumentEvent event) {
     }
    }
   } 