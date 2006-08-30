package olmgui.utils;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;

public class DescriptorListModel extends AbstractListModel
{

    /**
     * A sorted set of topics.
     */
    SortedSet model;
    SortedSet filteredModel;
    
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
     * Default contructor.
     * Create a TreeSet and store it in a SortedSet variable
     */
    public DescriptorListModel() 
    {
        super();
        this.model = new TreeSet(BDESC_COMPARATOR);
        this.filteredModel = new TreeSet(BDESC_COMPARATOR);
    }
        
    public int getSize() 
    {
        return model.size();
    }

    public Object getElementAt(int index) 
    {
        return model.toArray()[index];
    }

    // Other methods
    /**
     * Add a topic into the list model.
     * @param element   The topic to add into the list.
     * @todo Elements are assumed to be instance of TopicWrapper but no checks are made.
     */
    public void add(Object element) {
      if (model.add(element)) {
        fireContentsChanged(this, 0, getSize());
      }
    }

    /**
     * Add a complete array of topics into the list model.
     * @param elements  An array of topic to add into the list.
     */
    public void addAll(Object elements[]) {
      Collection c = Arrays.asList(elements);
      model.addAll(c);
      fireContentsChanged(this, 0, getSize());
    }

    /**
     * Remove all the topics from the list model.
     */
    public void clear() {
      model.clear();
      fireContentsChanged(this, 0, getSize());
    }

    /**
     * Check if a topic is already present in the list model.
     * @param element   The topic to look for.
     * @return          true if this list model contains the specified element, false otherwise.
     */
    public boolean contains(Object element) {
      return model.contains(element);
    }

    /**
     * Return the first (lowest) topic currently in the list model.
     * @return  A reference to the first element in the set, NULL if none.
     */
    public Object firstElement() {
      // Return the appropriate element
      return model.first();
    }

    /**
     * Get an iterator over the topics in the list model.
     * The elements are returned in ascending order.
     * @return  A reference to the iterator on the topics.
     */
    public Iterator iterator() {
      return model.iterator();
    }

    /**
     * Return the last (highest) topic currently in the list model.
     * @return A reference to the last element in the set, NULL if none.
     */
    public Object lastElement() {
      // Return the appropriate element
      return model.last();
    }

    /**
     * Remove the specified topic - if present - from the list model.
     * @param element   A reference to the topic to remove.
     * @return true if the set contained the specified element, false otherwise.
     */
    public boolean removeElement(Object element) {
      boolean removed = model.remove(element);
      if (removed) {
        fireContentsChanged(this, 0, getSize());
      }
      return removed;   
    }    
}
