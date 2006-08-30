/**
 * @file TopicWrapper.java
 */
package olmgui.utils;

/**
 * @author Nicolas Van Labeke
 * @version $Revision: 1.2 $
 *
 */
public class TopicWrapper {

    private String itemDesc=null;
    private String itemTitle=null;
    private String itemID=null;
    private boolean isItemRoot=false;
    
    public TopicWrapper()
    {
    }
    
    public TopicWrapper(String sID)
    {
        this.itemID = sID;
        this.isItemRoot = false;
    }
    public TopicWrapper(String sID,String sTitle,String sDesc)
    {
        this.itemID = sID;
        this.itemDesc = sTitle;
        this.itemTitle = sDesc;
        this.isItemRoot = false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        //return this.getID() + " / " + this.getTitle();
        if (this.getTitle()==null || this.getTitle().equals(""))
            return this.getID();
        else return this.getTitle();
    }

    /**
     * @return Returns the itemDesc.
     */
    public String getDescription() {
        return this.itemDesc;
    }
    
    public void setDescription(String desc)
    {
        this.itemDesc = desc;
    }

    /**
     * @return Returns the itemTitle.
     */
    public String getTitle() {
        return this.itemTitle;
    }

    public void setTitle(String title)
    {
        this.itemTitle = title;
    }
    /**
     * @return Returns the itemTitle.
     */
    public String getID() {
        return this.itemID;
    }

    public boolean isRoot()
    {
        return this.isItemRoot;
    }
    public void setRoot(boolean root)
    {
        this.isItemRoot = root;
    }
}
