/**
 * @file BeliefConstructorButton.java
 */
package olmgui.utils;

import javax.swing.JButton;

import config.OLMTopicConfig;

import olmgui.i18n.Messages;

/**
 * A Widget implementing placeholder for topics in the Belief Constructor
 * (see {@link olmgui.input.OLMBeliefConstructor}).
 * 
 * @author Nicolas Van Labeke
 * @version $Revision: 1.9 $
 *
 */
public class BeliefConstructorButton extends JButton
{

    /**
     * A reference to the wrapper containing information about the topic
     * associated with the placeholder.
     */
    private TopicWrapper wrapper = null;
    
    /**
     * The identifier of the placeholder (ie the topic map it is associated with).
     */
    private OLMTopicConfig config = null;
    
    /**
     * Default constructor.
     * 
     * @param config    The identifier of the topic map to associate 
     *                  with the placeholder.
     */
    public BeliefConstructorButton(OLMTopicConfig config)
    {
        super();
        this.config = config;
		initialize();
    }

    /**
     * Initializes the GUI of the placeholder.
     */
    private void initialize()
    {
        this.setSize(200,50);
        this.setEnabled(false);
        this.setText(Messages.getString("OLMTopicConfig." + config + ".Name"));
        this.setIcon(new TopicIcon(this.config));
        this.setActionCommand(this.config.toString());
        this.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        this.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

    }

    /**
     * Associate a topic with this placeholder.
     * @param wrapper   A reference to the wrapper containing info about the
     *                  topic to associate, <code>null</code> to clean the
     *                  placeholder.
     */
    public void setWrapper(TopicWrapper wrapper) {
        this.wrapper = wrapper;
        setEnabled(this.wrapper!=null);
        if (this.wrapper != null)
        {
            setText("<HTML><B>"+this.wrapper.getID()+"</b><BR>" + this.wrapper.getTitle());
            //setToolTipText(this.wrapper.getTitle());
        }
        else
        {
            setText(Messages.getString("OLMTopicConfig." + config + ".Name"));
            //setToolTipText(null);
        }
    }
    
    /**
     * Get the Topic Wrapper associated with the placeholder.
     * @return  A reference to the wrapper, <code>null</code> if none.
     */
    public TopicWrapper getWrapper() {
        return this.wrapper;
    }
    
    /**
     * Get the identifier of the Topic MAp associated with the placeholder. 
     * @return  A reference to the topic map identifier.
     */
    public OLMTopicConfig getTopic() {
        return this.config;
    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
